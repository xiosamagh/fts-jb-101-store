package com.foodtech.foodtechstore.category.repository;

import com.foodtech.foodtechstore.category.model.CategoryDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<CategoryDoc, ObjectId> {

}
