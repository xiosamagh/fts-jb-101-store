package com.foodtech.foodtechstore.price.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ApiModel(value ="PriceRequest",description = "Model for update Price")
public class PriceRequest {
            private ObjectId id;
            private ObjectId cityId;
            private String title;
            private Map<ObjectId,String> priceList = new HashMap<>();
}
