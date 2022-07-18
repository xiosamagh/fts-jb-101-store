package com.foodtech.foodtechstore.product.api.request;

import com.foodtech.foodtechstore.product.model.Bju;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value ="ProductAddPriceRequest",description = "Model for add product to price list")
public class ProductAddPriceRequest {
    private ObjectId id;
    private ObjectId priceId;
    private ObjectId productId;
    private String price;



}
