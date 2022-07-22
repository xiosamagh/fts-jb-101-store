package com.foodtech.foodtechstore.order.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@Builder
@ApiModel(value ="OrderRequest",description = "Model for update Order")
public class OrderRequest {
    private ObjectId id;
    private ObjectId guestId;
    private String numberOrder;
}
