package com.foodtech.foodtechstore.category.api.response;

import com.foodtech.foodtechstore.product.model.ProductDocWithCategoryResponse;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value ="CategoryResponse",description = "Category data(for search and list)")
public class CategoryResponse {
            protected String id;

            protected String title;

            protected List<ProductDocWithCategoryResponse> products;
}
