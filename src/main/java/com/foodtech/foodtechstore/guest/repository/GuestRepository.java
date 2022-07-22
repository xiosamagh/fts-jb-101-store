package com.foodtech.foodtechstore.guest.repository;

import com.foodtech.foodtechstore.guest.model.GuestDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepository extends MongoRepository<GuestDoc, ObjectId> {

    GuestDoc findByBasketId(ObjectId id);

}
