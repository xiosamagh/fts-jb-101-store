package com.foodtech.foodtechstore.product.repository;

import com.foodtech.foodtechstore.product.model.ProductDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<ProductDoc, ObjectId> {

    List<ProductDoc> findAllByPriceId(ObjectId id);

}
