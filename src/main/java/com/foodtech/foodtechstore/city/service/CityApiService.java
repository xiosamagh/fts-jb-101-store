package com.foodtech.foodtechstore.city.service;

import com.foodtech.foodtechstore.admin.model.AdminDoc;
import com.foodtech.foodtechstore.admin.repository.AdminRepository;
import com.foodtech.foodtechstore.auth.exceptions.AuthException;
import com.foodtech.foodtechstore.auth.exceptions.NotAccessException;
import com.foodtech.foodtechstore.auth.service.AuthService;
import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.base.service.CheckAccess;
import com.foodtech.foodtechstore.city.api.request.CityRequest;
import com.foodtech.foodtechstore.city.mapping.CityMapping;
import com.foodtech.foodtechstore.city.api.response.CityResponse;
import com.foodtech.foodtechstore.city.exeception.CityExistException;
import com.foodtech.foodtechstore.city.exeception.CityNotExistException;
import com.foodtech.foodtechstore.city.model.CityDoc;
import com.foodtech.foodtechstore.city.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityApiService extends CheckAccess<CityDoc> {
    private final CityRepository cityRepository;
    private final MongoTemplate mongoTemplate;
    private final AuthService authService;
    private final AdminRepository adminRepository;

    public CityDoc create(CityRequest request) throws CityExistException, AuthException {

        AdminDoc adminDoc = authService.currentAdmin();


        CityDoc cityDoc =CityMapping.getInstance().getRequest().convert(request,adminDoc.getId());
        cityRepository.save(cityDoc);
        return  cityDoc;
    }

    public Optional<CityDoc> findByID(ObjectId id){
        return cityRepository.findById(id);
    }
    public SearchResponse<CityDoc> search(
             SearchRequest request
    ){
        Criteria criteria = new Criteria();
        if(request.getQuery() != null && request.getQuery()!=""){
            criteria = criteria.orOperator(
                    //TODO: Add Criteria
                    Criteria.where("title").regex(request.getQuery(), "i")

            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, CityDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<CityDoc> cityDocs = mongoTemplate.find(query, CityDoc.class);
        return SearchResponse.of(cityDocs, count);
    }

    public CityDoc update(CityRequest request) throws CityNotExistException, NotAccessException, AuthException {
        Optional<CityDoc> cityDocOptional = cityRepository.findById(request.getId());
        if(cityDocOptional == null){
            throw new CityNotExistException();
        }
        CityDoc oldDoc = cityDocOptional.get();
        AdminDoc admin = checkAccess(oldDoc);

        CityDoc cityDoc = CityMapping.getInstance().getRequest().convert(request,admin.getId());
        cityDoc.setId(request.getId());
        cityDoc.setAdminId(oldDoc.getAdminId());

        cityRepository.save(cityDoc);

        return cityDoc;
    }

    public void delete(ObjectId id) throws ChangeSetPersister.NotFoundException, NotAccessException, AuthException {
        checkAccess(cityRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new));
        cityRepository.deleteById(id);
    }

    @Override
    protected ObjectId getOwnerFromEntity(CityDoc entity) {
        return entity.getAdminId();
    }

    @Override
    protected AuthService authService() {
        return authService;
    }
}
