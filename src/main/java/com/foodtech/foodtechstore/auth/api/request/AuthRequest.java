package com.foodtech.foodtechstore.auth.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequest {
    private String email;
    private String password;
}
