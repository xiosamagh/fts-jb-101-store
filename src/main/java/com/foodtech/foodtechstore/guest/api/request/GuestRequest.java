package com.foodtech.foodtechstore.guest.api.request;


import com.foodtech.foodtechstore.guest.model.Address;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value = "GuestRequest",description = "Model for update guest")
public class GuestRequest {


            private ObjectId id;
            private ObjectId basketId;
            private String name;
            private String phoneNumber;
            private Address address;
            private String timeDelivery;
            private String paymentMethod;
            private String amountOrder;

}
