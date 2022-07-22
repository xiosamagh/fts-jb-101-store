package com.foodtech.foodtechstore.basket.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

import java.util.Map;

@Getter
@Setter
@Builder
@ApiModel(value = "BasketRequest",description = "Model for update basket")
public class BasketRequest {


            private ObjectId id;
            private ObjectId sessionId;
            private Map<ObjectId,Integer> products;
            private Integer amountOrder;
            private String amountDelivery;
            private Integer amountTotal;

}
