package com.foodtech.foodtechstore.price.api.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value ="PriceResponse",description = "Price data(for search and list)")
public class PriceResponse {
            protected String id;
            protected String cityId;
            protected String title;
            protected String cityTitle;
            protected Map<ObjectId,String> priceList;
}
