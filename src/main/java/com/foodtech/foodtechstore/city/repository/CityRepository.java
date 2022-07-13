package com.foodtech.foodtechstore.city.repository;

import com.foodtech.foodtechstore.city.model.CityDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends MongoRepository<CityDoc, ObjectId> {

}
