package com.foodtech.foodtechstore.category.model;

import com.foodtech.foodtechstore.product.model.ProductDocWithCategoryResponse;
import com.foodtech.foodtechstore.street.model.StreetDocWithCityResponse;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

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
     private List<ProductDocWithCategoryResponse> products = new ArrayList<>();

}
