package com.foodtech.foodtechstore.session.model;

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
public class SessionDoc {

    @Id
            private ObjectId id;
            private ObjectId cityId;
            private ObjectId basketId;





}
