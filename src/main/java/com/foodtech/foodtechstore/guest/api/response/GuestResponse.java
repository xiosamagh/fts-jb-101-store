package com.foodtech.foodtechstore.guest.api.response;

import com.foodtech.foodtechstore.guest.model.Address;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value = "GuestResponse",description = "Guest data(for search and list)")
public class GuestResponse {

            protected String id;
            protected String basketId;
            protected String name;
            protected String phoneNumber;
            protected Address address;
            protected String timeDelivery;
            protected String paymentMethod;
            protected String amountOrder;

}
