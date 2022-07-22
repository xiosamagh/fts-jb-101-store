package com.foodtech.foodtechstore.street.api.request;


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
public class StreetSearchRequest extends SearchRequest {
    @ApiParam(name = "cityId",value = "Search by city", required = false)
    private ObjectId cityId;

}
