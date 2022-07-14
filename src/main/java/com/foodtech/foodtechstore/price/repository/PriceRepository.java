package com.foodtech.foodtechstore.price.repository;

import com.foodtech.foodtechstore.price.model.PriceDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceRepository extends MongoRepository<PriceDoc, ObjectId> {

}
