package com.foodtech.foodtechstore.street.model;

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
public class StreetDocWithCityResponse {


        @Id
        private String id;
        private String cityId;
        private String cityTitle;
        private String title;


}
