package com.foodtech.foodtechstore.guest.service;


import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.basket.exception.BasketNotExistException;
import com.foodtech.foodtechstore.basket.model.BasketDoc;
import com.foodtech.foodtechstore.basket.repository.BasketRepository;
import com.foodtech.foodtechstore.city.model.CityDoc;
import com.foodtech.foodtechstore.city.repository.CityRepository;
import com.foodtech.foodtechstore.guest.api.request.GuestRequest;
import com.foodtech.foodtechstore.guest.mapping.GuestMapping;
import com.foodtech.foodtechstore.guest.exception.GuestExistException;
import com.foodtech.foodtechstore.guest.exception.GuestNotExistException;
import com.foodtech.foodtechstore.guest.model.Address;
import com.foodtech.foodtechstore.guest.model.GuestDoc;
import com.foodtech.foodtechstore.guest.repository.GuestRepository;
import com.foodtech.foodtechstore.session.model.SessionDoc;
import com.foodtech.foodtechstore.session.repository.SessionRepository;
import com.foodtech.foodtechstore.street.exeception.StreetNotExistException;
import com.foodtech.foodtechstore.street.model.StreetDoc;
import com.foodtech.foodtechstore.street.repository.StreetRepository;
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
public class GuestApiService {
    private final GuestRepository guestRepository;
    private  final MongoTemplate mongoTemplate;
    private final BasketRepository basketRepository;
    private final CityRepository cityRepository;
    private final StreetRepository streetRepository;
    private final SessionRepository sessionRepository;

    public GuestDoc create(GuestRequest request) throws GuestExistException, BasketNotExistException, StreetNotExistException {

        if (guestRepository.findByBasketId(request.getBasketId()) != null) {
            guestRepository.delete(guestRepository.findByBasketId(request.getBasketId()));
        }



        if (basketRepository.findById(request.getBasketId()).isPresent()==false) {
            throw new BasketNotExistException();
        }
        StreetDoc streetDoc = streetRepository.findById(request.getAddress().getStreetId()).get();
        BasketDoc basketDoc = basketRepository.findById(request.getBasketId()).get();
        SessionDoc sessionDoc = sessionRepository.findById(basketDoc.getSessionId()).get();
        CityDoc cityDoc = cityRepository.findById(sessionDoc.getCityId()).get();

        if (!streetDoc.getCityId().equals(cityDoc.getId())) {
            throw new StreetNotExistException();
        }
        GuestDoc guestDoc = GuestMapping.getInstance().getRequestMapping().convert(request);
        Address address = guestDoc.getAddress();
        address.setStreet(streetDoc.getTitle());
        guestDoc.setAddress(address);
        guestDoc.setAmountOrder(basketDoc.getAmountOrder().toString());


        guestRepository.save(guestDoc);

        return guestDoc;
    }

    public Optional<GuestDoc> findById(ObjectId id) {
        return guestRepository.findById(id);
    }

    public SearchResponse<GuestDoc> search(SearchRequest request) {

        Criteria criteria = new Criteria();
        if (request.getQuery() != null && request.getQuery() != "") {
            criteria = criteria.orOperator(

                    // TODO : Add Criteria

            );
        }

        Query query = new Query(criteria);

        Long count = mongoTemplate.count(query,GuestDoc.class);

        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<GuestDoc> guestDocs = mongoTemplate.find(query, GuestDoc.class);
        return SearchResponse.of(guestDocs,count);
    }


    public GuestDoc update(GuestRequest request) throws GuestNotExistException, StreetNotExistException {
        Optional<GuestDoc> guestDocOptional = guestRepository.findById(request.getId());
        if (guestDocOptional == null) {
            throw new GuestNotExistException();
        }

        GuestDoc guestDoc = GuestMapping.getInstance().getRequestMapping().convert(request);
        guestDoc.setId(request.getId());

        StreetDoc streetDoc = streetRepository.findById(request.getAddress().getStreetId()).get();
        BasketDoc basketDoc = basketRepository.findById(request.getBasketId()).get();
        SessionDoc sessionDoc = sessionRepository.findById(basketDoc.getSessionId()).get();
        CityDoc cityDoc = cityRepository.findById(sessionDoc.getCityId()).get();

        if (!streetDoc.getCityId().equals(cityDoc.getId())) {
            throw new StreetNotExistException();
        }

        Address address = guestDoc.getAddress();
        address.setStreet(streetDoc.getTitle());
        guestDoc.setAddress(address);
        guestDoc.setAmountOrder(basketDoc.getAmountOrder().toString());
        guestRepository.save(guestDoc);

        return guestDoc;
    }

    public void delete(ObjectId id) {
        guestRepository.deleteById(id);
    }
}
