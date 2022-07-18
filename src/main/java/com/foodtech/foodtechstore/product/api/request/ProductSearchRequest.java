package com.foodtech.foodtechstore.product.api.request;


import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequest extends SearchRequest {
    @ApiParam(name = "categoryId",value = "Search by category", required = false)
    private ObjectId categoryId;

}
