package com.foodtech.foodtechstore.price.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceDoc {
    @Id
     private ObjectId id;
     private ObjectId adminId;
     private ObjectId cityId;
     private String cityTitle;
     private String title;
     private Map<ObjectId,String> priceList = new HashMap<>();
}
