package com.foodtech.foodtechstore.street.api.response;

import com.foodtech.foodtechstore.street.model.StreetDoc;
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
@ApiModel(value ="StreetResponse",description = "Street data(for search and list)")
public class StreetResponse {
            protected String id;
            protected String cityTitle;
            protected String title;

}
