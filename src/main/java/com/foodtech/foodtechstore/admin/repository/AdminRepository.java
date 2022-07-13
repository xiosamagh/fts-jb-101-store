package com.foodtech.foodtechstore.admin.repository;

import com.foodtech.foodtechstore.admin.model.AdminDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<AdminDoc, ObjectId>  {

    public Optional<AdminDoc> findByEmail(String email);

}
