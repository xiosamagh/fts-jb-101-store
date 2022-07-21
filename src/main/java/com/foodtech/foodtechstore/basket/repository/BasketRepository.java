package com.foodtech.foodtechstore.basket.repository;

import com.foodtech.foodtechstore.basket.model.BasketDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasketRepository extends MongoRepository<BasketDoc, ObjectId> {

    BasketDoc findBySessionId(ObjectId id);

}
