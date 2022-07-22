package com.foodtech.foodtechstore.city.api.response;

import com.foodtech.foodtechstore.street.model.StreetDoc;
import com.foodtech.foodtechstore.street.model.StreetDocWithCityResponse;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value ="CityResponse",description = "City data(for search and list)")
public class CityResponse {
            protected String id;
            protected String priceDelivery;
            protected String timeDelivery;
            protected String title;
             protected List<StreetDocWithCityResponse> streets;
}
