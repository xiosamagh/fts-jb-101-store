package com.foodtech.foodtechstore.admin.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Document
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDoc {

    @Id
    private ObjectId id;

    private String email;

    private String password;

    private Integer failLogin = 0;


    public static String hexPassword(String clearPassword) {
        return DigestUtils.md5DigestAsHex(clearPassword.getBytes(StandardCharsets.UTF_8));
    }
}
