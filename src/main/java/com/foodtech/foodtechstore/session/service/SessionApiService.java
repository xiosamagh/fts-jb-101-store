package com.foodtech.foodtechstore.session.service;


import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.basket.api.request.BasketRequest;
import com.foodtech.foodtechstore.basket.exception.BasketExistException;
import com.foodtech.foodtechstore.basket.mapping.BasketMapping;
import com.foodtech.foodtechstore.basket.model.BasketDoc;
import com.foodtech.foodtechstore.basket.repository.BasketRepository;
import com.foodtech.foodtechstore.basket.service.BasketApiService;
import com.foodtech.foodtechstore.city.exeception.CityNotExistException;
import com.foodtech.foodtechstore.city.model.CityDoc;
import com.foodtech.foodtechstore.city.repository.CityRepository;
import com.foodtech.foodtechstore.price.exeception.PriceNotExistException;
import com.foodtech.foodtechstore.price.model.PriceDoc;
import com.foodtech.foodtechstore.price.repository.PriceRepository;
import com.foodtech.foodtechstore.product.model.ProductDoc;
import com.foodtech.foodtechstore.product.repository.ProductRepository;
import com.foodtech.foodtechstore.session.api.request.SessionRequest;
import com.foodtech.foodtechstore.session.mapping.SessionMapping;
import com.foodtech.foodtechstore.session.exception.SessionExistException;
import com.foodtech.foodtechstore.session.exception.SessionNotExistException;
import com.foodtech.foodtechstore.session.model.SessionDoc;
import com.foodtech.foodtechstore.session.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionApiService {
    private final SessionRepository sessionRepository;
    private  final MongoTemplate mongoTemplate;
    private final CityRepository cityRepository;
    private final PriceRepository priceRepository;
    private final ProductRepository productRepository;
    private final BasketApiService basketApiService;
    private final BasketRepository basketRepository;

    public SessionDoc create(SessionRequest request) throws SessionExistException, CityNotExistException, PriceNotExistException, BasketExistException {

        if (cityRepository.findById(request.getCityId()).isPresent()==false) {
            throw new CityNotExistException();
        }

        if (priceRepository.findPriceDocByCityId(request.getCityId()) == null) {
            throw new PriceNotExistException();
        }

        PriceDoc priceDoc = priceRepository.findPriceDocByCityId(request.getCityId());

        List<ProductDoc> productDocs = productRepository.findAll();

        for (ProductDoc productDoc: productDocs) {
            productDoc.setPrice(priceDoc.getPriceList().get(productDoc.getId()));
            productDoc.setPriceId(priceDoc.getId());
            productRepository.save(productDoc);
        }

        SessionDoc sessionDoc = SessionMapping.getInstance().getRequestMapping().convert(request);
        sessionRepository.save(sessionDoc);



        CityDoc cityDoc = cityRepository.findById(sessionDoc.getCityId()).get();
        BasketDoc basketDoc = basketApiService.create(
                BasketRequest.builder()
                        .sessionId(sessionDoc.getId())
                        .amountDelivery(cityDoc.getPriceDelivery())
                        .build()
        );

        sessionDoc.setBasketId(basketRepository.findBySessionId(sessionDoc.getId()).getId());


        sessionRepository.save(sessionDoc);

        return sessionDoc;
    }

    public Optional<SessionDoc> findById(ObjectId id) {
        return sessionRepository.findById(id);
    }

    public SearchResponse<SessionDoc> search(SearchRequest request) {

        Criteria criteria = new Criteria();
        if (request.getQuery() != null && request.getQuery() != "") {
            criteria = criteria.orOperator(

                    // TODO : Add Criteria
//
            );
        }

        Query query = new Query(criteria);

        Long count = mongoTemplate.count(query,SessionDoc.class);

        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<SessionDoc> sessionDocs = mongoTemplate.find(query, SessionDoc.class);
        return SearchResponse.of(sessionDocs,count);
    }


    public SessionDoc update(SessionRequest request) throws SessionNotExistException, PriceNotExistException {
        Optional<SessionDoc> sessionDocOptional = sessionRepository.findById(request.getId());
        if (sessionDocOptional == null) {
            throw new SessionNotExistException();
        }

        if (priceRepository.findPriceDocByCityId(request.getCityId()) == null) {
            throw new PriceNotExistException();
        }

        PriceDoc priceDoc = priceRepository.findPriceDocByCityId(request.getCityId());

        List<ProductDoc> productDocs = productRepository.findAll();

        for (ProductDoc productDoc: productDocs) {
            productDoc.setPrice(priceDoc.getPriceList().get(productDoc.getId()));
            productDoc.setPriceId(priceDoc.getId());
            productRepository.save(productDoc);
        }

        SessionDoc sessionDoc = SessionMapping.getInstance().getRequestMapping().convert(request);
        sessionDoc.setId(request.getId());


        sessionRepository.save(sessionDoc);

        return sessionDoc;
    }

    public void delete(ObjectId id) {
        basketApiService.delete(basketRepository.findBySessionId(id).getId());
        sessionRepository.deleteById(id);
    }
}
