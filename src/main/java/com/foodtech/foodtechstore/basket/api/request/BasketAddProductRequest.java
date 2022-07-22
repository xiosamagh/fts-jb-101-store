package com.foodtech.foodtechstore.basket.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@Builder
@ApiModel(value = "BasketAddProductRequest",description = "Model for add product to basket")
public class BasketAddProductRequest {
    private ObjectId id;
    private ObjectId sessionId;
    private ObjectId productId;
    private Integer count;
}
