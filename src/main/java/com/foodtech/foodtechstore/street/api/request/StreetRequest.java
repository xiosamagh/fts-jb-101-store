package com.foodtech.foodtechstore.street.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value ="StreetRequest",description = "Model for update Street")
public class StreetRequest {
            private ObjectId id;
            private ObjectId cityId;
            private String title;
}
