package com.foodtech.foodtechstore.product.api.response;

import com.foodtech.foodtechstore.product.model.Bju;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value ="ProductResponse",description = "Product data(for search and list)")
public class ProductResponse {
            protected String id;
            protected String category;
            protected String imageURL;
            protected String price;
            protected String title;
            protected String description;
            protected Bju bju;
            protected String calories;
}
