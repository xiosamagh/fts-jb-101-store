package com.foodtech.foodtechstore.order.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDoc {
    @Id
    private ObjectId id;
    private ObjectId guestId;
    private String numberOrder;

}
