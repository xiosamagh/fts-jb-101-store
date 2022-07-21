package com.foodtech.foodtechstore.basket.api.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

import java.util.Map;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value = "BasketResponse",description = "Basket data(for search and list)")
public class BasketResponse {

            protected String id;
            protected String sessionId;
            protected Map<ObjectId,Integer> products;
            protected Integer amountOrder;
            protected String amountDelivery;
            protected Integer amountTotal;

}
