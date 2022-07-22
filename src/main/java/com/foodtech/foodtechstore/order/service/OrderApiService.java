package com.foodtech.foodtechstore.order.service;


import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.guest.api.request.GuestRequest;
import com.foodtech.foodtechstore.guest.exception.GuestNotExistException;

import com.foodtech.foodtechstore.guest.model.GuestDoc;
import com.foodtech.foodtechstore.guest.repository.GuestRepository;
import com.foodtech.foodtechstore.order.api.request.OrderRequest;
import com.foodtech.foodtechstore.order.exception.OrderNotExistException;
import com.foodtech.foodtechstore.order.mapping.OrderMapping;
import com.foodtech.foodtechstore.order.model.OrderDoc;
import com.foodtech.foodtechstore.order.repository.OrderRepository;


import com.foodtech.foodtechstore.session.repository.SessionRepository;
import com.foodtech.foodtechstore.session.service.SessionApiService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OrderApiService {
    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;
    private final GuestRepository guestRepository;
    public OrderDoc create(OrderRequest request) throws GuestNotExistException {

        if (guestRepository.findById(request.getGuestId()).isPresent()==false) {
            throw new GuestNotExistException();
        }

        OrderDoc orderDoc = OrderMapping.getInstance().getRequestMapping().convert(request);
        Random random = new Random();
        orderDoc.setNumberOrder(String.valueOf(random.nextInt(1000000)));

        orderRepository.save(orderDoc);
        return orderDoc;
    }

    public Optional<OrderDoc> findById(ObjectId id) {
        return orderRepository.findById(id);
    }

    public SearchResponse<OrderDoc> search(SearchRequest request) {
        Criteria criteria = new Criteria();
        if (request.getQuery() != null && request.getQuery() != "") {
            criteria = criteria.orOperator(

                    // TODO : Add Criteria

            );
        }

        Query query = new Query(criteria);

        Long count = mongoTemplate.count(query, OrderDoc.class);

        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<OrderDoc> orderDocs = mongoTemplate.find(query, OrderDoc.class);
        return SearchResponse.of(orderDocs,count);
    }

    public OrderDoc update(OrderRequest request) throws OrderNotExistException {
        Optional<OrderDoc> orderDocOptional = orderRepository.findById(request.getId());
        if (orderDocOptional == null) {
            throw new OrderNotExistException();
        }

        OrderDoc oldDoc = orderDocOptional.get();

        OrderDoc orderDoc = OrderMapping.getInstance().getRequestMapping().convert(request);
        orderDoc.setId(request.getId());
        orderDoc.setGuestId(oldDoc.getGuestId());

        orderRepository.save(orderDoc);
        return orderDoc;
    }

    public void delete(ObjectId id) {
        orderRepository.deleteById(id);
    }
}
