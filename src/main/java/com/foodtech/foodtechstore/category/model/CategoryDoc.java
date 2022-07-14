package com.foodtech.foodtechstore.category.model;

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
public class CategoryDoc {
    @Id
     private ObjectId id;
     private ObjectId adminId;
     private String title;
}
