package com.foodtech.foodtechstore.order.repository;

import com.foodtech.foodtechstore.order.model.OrderDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<OrderDoc, ObjectId> {

}
