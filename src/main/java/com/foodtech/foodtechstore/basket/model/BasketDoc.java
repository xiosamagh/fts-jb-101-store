package com.foodtech.foodtechstore.basket.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BasketDoc {

    @Id
            private ObjectId id;
            private ObjectId sessionId;
            private Map<ObjectId,Integer> products = new HashMap<>();
            private Integer amountOrder;
            private String amountDelivery;
            private Integer amountTotal;





}
