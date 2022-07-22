package com.foodtech.foodtechstore.price.service;

import com.foodtech.foodtechstore.admin.model.AdminDoc;
import com.foodtech.foodtechstore.admin.repository.AdminRepository;
import com.foodtech.foodtechstore.auth.exceptions.AuthException;
import com.foodtech.foodtechstore.auth.exceptions.NotAccessException;
import com.foodtech.foodtechstore.auth.service.AuthService;
import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.base.service.CheckAccess;
import com.foodtech.foodtechstore.city.exeception.CityNotExistException;
import com.foodtech.foodtechstore.city.repository.CityRepository;
import com.foodtech.foodtechstore.price.api.request.PriceRequest;
import com.foodtech.foodtechstore.price.mapping.PriceMapping;
import com.foodtech.foodtechstore.price.exeception.PriceExistException;
import com.foodtech.foodtechstore.price.exeception.PriceNotExistException;
import com.foodtech.foodtechstore.price.model.PriceDoc;
import com.foodtech.foodtechstore.price.repository.PriceRepository;
import com.foodtech.foodtechstore.product.model.ProductDoc;
import com.foodtech.foodtechstore.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceApiService extends CheckAccess<PriceDoc> {
    private final PriceRepository priceRepository;
    private final MongoTemplate mongoTemplate;
    private final AuthService authService;
    private final AdminRepository adminRepository;
    private final CityRepository cityRepository;
    private final ProductRepository productRepository;

    public PriceDoc create(PriceRequest request) throws PriceExistException, AuthException, CityNotExistException {

        AdminDoc adminDoc = authService.currentAdmin();
        if (cityRepository.findById(request.getCityId()).isPresent()==false) {
            throw new CityNotExistException();
        }
        PriceDoc priceDoc = PriceMapping.getInstance().getRequest().convert(request,adminDoc.getId());
        priceDoc.setCityTitle(cityRepository.findById(priceDoc.getCityId()).get().getTitle());
        priceRepository.save(priceDoc);
        return  priceDoc;
    }

    public Optional<PriceDoc> findByID(ObjectId id){
        return priceRepository.findById(id);
    }

    public SearchResponse<PriceDoc> search(
             SearchRequest request
    ){
        Criteria criteria = new Criteria();
        if(request.getQuery() != null && request.getQuery()!=""){
            criteria = criteria.orOperator(
                    //TODO: Add Criteria
                    Criteria.where("title").regex(request.getQuery(), "i"),
                    Criteria.where("cityTitle").regex(request.getQuery(),"i")

            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, PriceDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());




       List<PriceDoc> priceDocs = mongoTemplate.find(query, PriceDoc.class);
        return SearchResponse.of(priceDocs, count);
    }



    public PriceDoc update(PriceRequest request) throws PriceNotExistException, NotAccessException, AuthException {
        Optional<PriceDoc> priceDocOptional = priceRepository.findById(request.getId());
        if(priceDocOptional == null){
            throw new PriceNotExistException();
        }
        PriceDoc oldDoc = priceDocOptional.get();
        AdminDoc admin = checkAccess(oldDoc);

        PriceDoc priceDoc = PriceMapping.getInstance().getRequest().convert(request,admin.getId());
        priceDoc.setId(request.getId());
        priceDoc.setAdminId(oldDoc.getAdminId());
        priceDoc.setCityId(oldDoc.getCityId());
        priceDoc.setCityTitle(oldDoc.getCityTitle());


        priceRepository.save(priceDoc);

        return priceDoc;
    }

    public void delete(ObjectId id) throws ChangeSetPersister.NotFoundException, NotAccessException, AuthException {
        checkAccess(priceRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new));
        priceRepository.deleteById(id);
    }

    public void addProductToPriceList(PriceRequest request) throws PriceNotExistException, NotAccessException, AuthException {
        Optional<PriceDoc> priceDocOptional = priceRepository.findById(request.getId());
        if(priceDocOptional == null){
            throw new PriceNotExistException();
        }
        PriceDoc oldDoc = priceDocOptional.get();

        AdminDoc adminDoc = checkAccess(oldDoc);

        PriceDoc priceDoc = PriceMapping.getInstance().getRequest().convert(request,adminDoc.getId());
        priceDoc.setCityId(oldDoc.getCityId());
        priceDoc.setTitle(oldDoc.getTitle());
        priceDoc.setAdminId(oldDoc.getAdminId());


//        List<ProductDoc> productDocs = productRepository.findAllByPriceId(request.getId());
        List<ProductDoc> productDocs = productRepository.findAll();

        if (priceDoc.getPriceList() == null) {
            priceDoc.setPriceList(new HashMap<ObjectId, String>());
        }
        for (ProductDoc productDoc : productDocs) {
            priceDoc.getPriceList().put(productDoc.getId(),productDoc.getPrice());
        }

        priceRepository.save(priceDoc);

    }

    @Override
    protected ObjectId getOwnerFromEntity(PriceDoc entity) {
        return entity.getAdminId();
    }

    @Override
    protected AuthService authService() {
        return authService;
    }
}
