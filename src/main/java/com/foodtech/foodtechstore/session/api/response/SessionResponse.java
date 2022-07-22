package com.foodtech.foodtechstore.session.api.response;

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
@ApiModel(value = "SessionResponse",description = "Session data(for search and list)")
public class SessionResponse {

            protected String id;
            protected String cityId;
            protected String basketId;

}
