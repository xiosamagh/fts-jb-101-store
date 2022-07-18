package com.foodtech.foodtechstore.product.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDoc {
    @Id
     private ObjectId id;
     private ObjectId adminId;
     private ObjectId categoryId;
     private String category;
     private ObjectId priceId;
     private String imageURL;
     private String price;
     private String title;
     private String description;
     private Bju bju;
     private String calories;
}
