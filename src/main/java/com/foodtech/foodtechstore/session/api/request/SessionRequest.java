package com.foodtech.foodtechstore.session.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value = "SessionRequest",description = "Model for update session")
public class SessionRequest {


            private ObjectId id;
            private ObjectId cityId;

}
