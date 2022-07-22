package com.foodtech.foodtechstore.guest.model;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.bson.types.ObjectId;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Address",description = "Guest or Company address")
public class Address {
    private ObjectId streetId;
    private String street;
    private String house;
    private String apartment;
    private String entrance;
    private String storey;
}
