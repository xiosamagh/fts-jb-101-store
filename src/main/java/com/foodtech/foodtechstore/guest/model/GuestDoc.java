package com.foodtech.foodtechstore.guest.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class GuestDoc {

                @Id
            private ObjectId id;
            private ObjectId basketId;
            private String name;
            private String phoneNumber;
            private Address address = new Address();
            private String timeDelivery;
            private String paymentMethod;
            private String amountOrder;





}
