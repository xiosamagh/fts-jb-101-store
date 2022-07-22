package com.foodtech.foodtechstore.city.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value ="CityRequest",description = "Model for update City")
public class CityRequest {
            private ObjectId id;
            private String priceDelivery;
            private String timeDelivery;
            private String title;
            private ObjectId adminId;
}
