package com.foodtech.foodtechstore.street.repository;

import com.foodtech.foodtechstore.street.model.StreetDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StreetRepository extends MongoRepository<StreetDoc, ObjectId> {

}
