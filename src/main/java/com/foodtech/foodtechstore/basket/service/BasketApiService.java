package com.foodtech.foodtechstore.basket.service;


import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.basket.api.request.BasketAddProductRequest;
import com.foodtech.foodtechstore.basket.api.request.BasketRequest;
import com.foodtech.foodtechstore.basket.mapping.BasketMapping;
import com.foodtech.foodtechstore.basket.exception.BasketExistException;
import com.foodtech.foodtechstore.basket.exception.BasketNotExistException;
import com.foodtech.foodtechstore.basket.model.BasketDoc;
import com.foodtech.foodtechstore.basket.repository.BasketRepository;
import com.foodtech.foodtechstore.guest.repository.GuestRepository;
import com.foodtech.foodtechstore.guest.service.GuestApiService;
import com.foodtech.foodtechstore.product.model.ProductDoc;
import com.foodtech.foodtechstore.product.repository.ProductRepository;
import com.foodtech.foodtechstore.session.repository.SessionRepository;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketApiService {
    private final BasketRepository basketRepository;
    private  final MongoTemplate mongoTemplate;
    private final SessionRepository sessionRepository;
    private final ProductRepository productRepository;
    private final GuestRepository guestRepository;
    private final GuestApiService guestApiService;

    public BasketDoc create(BasketRequest request) throws BasketExistException {


        BasketDoc basketDoc = BasketMapping.getInstance().getRequestMapping().convert(request);

        basketRepository.save(basketDoc);

        return basketDoc;
    }

    public Optional<BasketDoc> findById(ObjectId id) {
        return basketRepository.findById(id);
    }

    public SearchResponse<BasketDoc> search(SearchRequest request) {

        Criteria criteria = new Criteria();
        if (request.getQuery() != null && request.getQuery() != "") {
            criteria = criteria.orOperator(
                    // TODO : Add Criteria
            );
        }

        Query query = new Query(criteria);

        Long count = mongoTemplate.count(query,BasketDoc.class);

        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<BasketDoc> basketDocs = mongoTemplate.find(query, BasketDoc.class);
        return SearchResponse.of(basketDocs,count);
    }


    public BasketDoc update(BasketRequest request) throws BasketNotExistException {
        Optional<BasketDoc> basketDocOptional = basketRepository.findById(request.getId());
        if (basketDocOptional == null) {
            throw new BasketNotExistException();
        }
        BasketDoc oldDoc = basketDocOptional.get();


        BasketDoc basketDoc = BasketMapping.getInstance().getRequestMapping().convert(request);
        basketDoc.setId(request.getId());
        basketDoc.setSessionId(oldDoc.getSessionId());

        basketRepository.save(basketDoc);

        return basketDoc;
    }

    public BasketDoc addProduct(BasketAddProductRequest request) throws BasketNotExistException {
        Optional<BasketDoc> basketDocOptional = basketRepository.findById(request.getId());
        if (basketDocOptional == null) {
            throw new BasketNotExistException();
        }
        BasketDoc oldDoc = basketDocOptional.get();
        Map<ObjectId, Integer> products = oldDoc.getProducts();
        products.put(request.getProductId(),request.getCount());
        ProductDoc productDoc = productRepository.findById(request.getProductId()).get();
        String[] price = productDoc.getPrice().split(" ");
        Integer amount = Integer.parseInt(price[0]) * request.getCount();

        String amountDelivery = oldDoc.getAmountDelivery().toLowerCase();
        if (amountDelivery.equals("бесплатно")) {
            oldDoc.setAmountDelivery("0");
        }
        else {
            String[] amountDelivers = amountDelivery.split(" ");
            oldDoc.setAmountDelivery(amountDelivers[0]);
        }
        oldDoc.setProducts(products);
        if (oldDoc.getAmountOrder() == null) {
            oldDoc.setAmountOrder(amount);
        }
        else  {
            oldDoc.setAmountOrder(oldDoc.getAmountOrder() + amount);
        }


        oldDoc.setAmountTotal(oldDoc.getAmountOrder() + Integer.parseInt(oldDoc.getAmountDelivery()));

        basketRepository.save(oldDoc);

        return oldDoc;


    }

    public void insertProduct(BasketAddProductRequest request) throws BasketNotExistException {
        Optional<BasketDoc> basketDocOptional = basketRepository.findById(request.getId());
        if (basketDocOptional == null) {
            throw new BasketNotExistException();
        }
        BasketDoc oldDoc = basketDocOptional.get();
        Map<ObjectId, Integer> products = oldDoc.getProducts();
        for (Map.Entry<ObjectId, Integer> entry : products.entrySet()) {
            if (entry.getKey().equals(request.getProductId())) {
                entry.setValue(entry.getValue()+1);
                ProductDoc productDoc = productRepository.findById(request.getProductId()).get();
                String[] price = productDoc.getPrice().split(" ");
                Integer amount = Integer.parseInt(price[0]) * 1;
                oldDoc.setAmountOrder(oldDoc.getAmountOrder()+amount);
                oldDoc.setAmountTotal(oldDoc.getAmountOrder() + Integer.parseInt(oldDoc.getAmountDelivery()));

                basketRepository.save(oldDoc);
            }
        }

    }

    public void deleteProduct(BasketAddProductRequest request) throws BasketNotExistException {
        Optional<BasketDoc> basketDocOptional = basketRepository.findById(request.getId());
        if (basketDocOptional == null) {
            throw new BasketNotExistException();
        }
        BasketDoc oldDoc = basketDocOptional.get();
        Map<ObjectId, Integer> products = oldDoc.getProducts();
        for (Map.Entry<ObjectId, Integer> entry : products.entrySet()) {
            if (entry.getKey().equals(request.getProductId())) {
                entry.setValue(entry.getValue()-1);
                if (entry.getValue() == 0) {
                    products.remove(entry.getKey());
                    ProductDoc productDoc = productRepository.findById(request.getProductId()).get();
                    String[] price = productDoc.getPrice().split(" ");
                    Integer amount = Integer.parseInt(price[0]) * 1;
                    oldDoc.setAmountOrder(oldDoc.getAmountOrder()-amount);
                    oldDoc.setAmountTotal(oldDoc.getAmountOrder() + Integer.parseInt(oldDoc.getAmountDelivery()));
                    basketRepository.save(oldDoc);
                }
                else {
                    ProductDoc productDoc = productRepository.findById(request.getProductId()).get();
                    String[] price = productDoc.getPrice().split(" ");
                    Integer amount = Integer.parseInt(price[0]) * 1;
                    oldDoc.setAmountOrder(oldDoc.getAmountOrder()-amount);
                    oldDoc.setAmountTotal(oldDoc.getAmountOrder() + Integer.parseInt(oldDoc.getAmountDelivery()));
                    basketRepository.save(oldDoc);
                }



            }
        }

    }

    public void delete(ObjectId id) {
        guestApiService.delete(guestRepository.findByBasketId(id).getId());
        basketRepository.deleteById(id);
    }
}
