package com.foodtech.foodtechstore.session.repository;

import com.foodtech.foodtechstore.session.model.SessionDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends MongoRepository<SessionDoc, ObjectId> {


}
