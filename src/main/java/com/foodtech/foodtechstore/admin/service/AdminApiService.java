package com.foodtech.foodtechstore.admin.service;

import com.foodtech.foodtechstore.admin.api.request.RegistrationRequest;
import com.foodtech.foodtechstore.admin.exception.AdminExistException;
import com.foodtech.foodtechstore.admin.model.AdminDoc;
import com.foodtech.foodtechstore.admin.repository.AdminRepository;
import com.foodtech.foodtechstore.auth.exceptions.AuthException;
import com.foodtech.foodtechstore.auth.exceptions.NotAccessException;
import com.foodtech.foodtechstore.auth.service.AuthService;
import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.base.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class AdminApiService {

    private final AdminRepository adminRepository;
    private final MongoTemplate mongoTemplate;
    private final EmailSenderService emailSenderService;
    private final AuthService authService;


    public AdminDoc registration(RegistrationRequest request) throws AdminExistException {

        if (adminRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AdminExistException();
        }

        AdminDoc adminDoc = new AdminDoc();
        adminDoc.setEmail(request.getEmail());
        adminDoc.setPassword(AdminDoc.hexPassword(request.getPassword()));

        adminDoc = adminRepository.save(adminDoc);

        emailSenderService.sendEmailRegistration(request.getEmail());

        return adminDoc;
    }

    public Optional<AdminDoc> findById(ObjectId id) {
        return adminRepository.findById(id);
    }

    public SearchResponse<AdminDoc> search(SearchRequest request) {
        Criteria criteria = new Criteria();
        if (request.getQuery() != null && request.getQuery() != "") {
            criteria = criteria.orOperator(
                    Criteria.where("email").regex(request.getQuery(),"i")
            );
        }

        Query query = new Query(criteria);

        Long count = mongoTemplate.count(query,AdminDoc.class);

        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<AdminDoc> adminDocs = mongoTemplate.find(query,AdminDoc.class);
        return SearchResponse.of(adminDocs,count);

    }

    public void delete(ObjectId id) throws NotAccessException, AuthException {

        if (authService.currentAdmin().getId().equals(id) == false) throw new NotAccessException();

        adminRepository.deleteById(id);
    }

}
