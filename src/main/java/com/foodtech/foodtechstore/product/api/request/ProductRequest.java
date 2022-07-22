package com.foodtech.foodtechstore.product.api.request;


import com.foodtech.foodtechstore.product.model.Bju;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value ="ProductRequest",description = "Model for update Product")
public class ProductRequest {
            private ObjectId id;
            private ObjectId categoryId;
            private ObjectId priceId;
            private String price;
            private String imageURL;
            private String title;
            private String description;
            private Bju bju;
            private String calories;
}
