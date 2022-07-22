package com.foodtech.foodtechstore.order.api.response;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@ApiModel(value ="OrderResponse",description = "Order data(for search and list)")
public class OrderResponse {
    protected String id;
    protected String guestId;
    protected String numberOrder;
}
