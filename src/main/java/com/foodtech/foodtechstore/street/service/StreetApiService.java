package com.foodtech.foodtechstore.street.service;

import com.foodtech.foodtechstore.admin.model.AdminDoc;
import com.foodtech.foodtechstore.admin.repository.AdminRepository;
import com.foodtech.foodtechstore.auth.exceptions.AuthException;
import com.foodtech.foodtechstore.auth.exceptions.NotAccessException;
import com.foodtech.foodtechstore.auth.service.AuthService;
import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.base.service.CheckAccess;
import com.foodtech.foodtechstore.city.exeception.CityNotExistException;
import com.foodtech.foodtechstore.city.model.CityDoc;
import com.foodtech.foodtechstore.city.repository.CityRepository;
import com.foodtech.foodtechstore.street.api.request.StreetRequest;
import com.foodtech.foodtechstore.street.api.request.StreetSearchRequest;
import com.foodtech.foodtechstore.street.mapping.StreetMapping;
import com.foodtech.foodtechstore.street.api.response.StreetResponse;
import com.foodtech.foodtechstore.street.exeception.StreetExistException;
import com.foodtech.foodtechstore.street.exeception.StreetNotExistException;
import com.foodtech.foodtechstore.street.model.StreetDoc;
import com.foodtech.foodtechstore.street.model.StreetDocWithCityResponse;
import com.foodtech.foodtechstore.street.repository.StreetRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StreetApiService extends CheckAccess<StreetDoc> {
    private final StreetRepository streetRepository;
    private final MongoTemplate mongoTemplate;
    private final AuthService authService;
    private final AdminRepository adminRepository;
    private final CityRepository cityRepository;

    public StreetDoc create(StreetRequest request) throws AuthException, CityNotExistException {

        AdminDoc adminDoc = authService.currentAdmin();
        if (cityRepository.findById(request.getCityId()).isPresent()==false) {
            throw new CityNotExistException();
        }

        StreetDoc streetDoc = StreetMapping.getInstance().getRequest().convert(request,adminDoc.getId());
        streetDoc.setCityTitle(cityRepository.findById(request.getCityId()).get().getTitle());
        streetRepository.save(streetDoc);

        // При создании улицы сохранение ее в список улиц конкретного города
        // Сделана отдельная модель, чтобы пользователь видел только то, что ему нужно
        CityDoc cityDoc = cityRepository.findById(streetDoc.getCityId()).get();
        StreetDocWithCityResponse street = StreetDocWithCityResponse.builder()
                .id(streetDoc.getId().toString())
                .title(streetDoc.getTitle())
                .cityId(streetDoc.getCityId().toString())
                .cityTitle(streetDoc.getCityTitle())
                .build();

        cityDoc.getStreets().add(street);
        cityRepository.save(cityDoc);


        return  streetDoc;
    }

    public Optional<StreetDoc> findByID(ObjectId id){
        return streetRepository.findById(id);
    }

    public SearchResponse<StreetDoc> search(
             StreetSearchRequest request
    ){
        List<Criteria> orCriterias = new ArrayList<>();
        if (request.getQuery() != null && request.getQuery() != "") {
            orCriterias.add(Criteria.where("cityTitle").regex(request.getQuery(), "i"));
        }

        if (request.getQuery() != null && request.getQuery() != "") {
            orCriterias.add(Criteria.where("title").regex(request.getQuery(), "i"));
        }

        if (request.getCityId() != null) {
            orCriterias.add(Criteria.where("cityId").is(request.getCityId()));
        }

        Criteria criteria = new Criteria();

        if (orCriterias.size() > 0) {
            criteria = criteria.orOperator(
                    orCriterias.toArray(new Criteria[orCriterias.size()])

            );

        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, StreetDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<StreetDoc> streetDocs = mongoTemplate.find(query, StreetDoc.class);
        return SearchResponse.of(streetDocs, count);
    }

    public StreetDoc update(StreetRequest request) throws StreetNotExistException, NotAccessException, AuthException {
        Optional<StreetDoc> streetDocOptional = streetRepository.findById(request.getId());
        if(streetDocOptional == null){
            throw new StreetNotExistException();
        }
        StreetDoc oldDoc = streetDocOptional.get();
        AdminDoc admin = checkAccess(oldDoc);

        StreetDoc streetDoc = StreetMapping.getInstance().getRequest().convert(request,admin.getId());
        streetDoc.setId(request.getId());
        streetDoc.setCityId(oldDoc.getCityId());
        streetDoc.setCityTitle(oldDoc.getCityTitle());
        streetDoc.setAdminId(oldDoc.getAdminId());

        streetRepository.save(streetDoc);

        return streetDoc;
    }

    public void delete(ObjectId id) throws ChangeSetPersister.NotFoundException, NotAccessException, AuthException {
        checkAccess(streetRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new));
        streetRepository.deleteById(id);
    }

    @Override
    protected ObjectId getOwnerFromEntity(StreetDoc entity) {
        return entity.getAdminId();
    }

    @Override
    protected AuthService authService() {
        return authService;
    }
}
