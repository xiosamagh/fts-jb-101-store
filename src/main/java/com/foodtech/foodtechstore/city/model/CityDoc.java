package com.foodtech.foodtechstore.city.model;

import com.foodtech.foodtechstore.street.model.StreetDoc;
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
public class CityDoc {
    @Id
     private ObjectId id;
     private String priceDelivery;
     private String timeDelivery;
     private String title;
     private ObjectId adminId;
     private List<StreetDocWithCityResponse> streets = new ArrayList<>();
}
