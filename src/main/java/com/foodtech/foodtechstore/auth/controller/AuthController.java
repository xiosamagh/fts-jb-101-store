package com.foodtech.foodtechstore.auth.controller;



import com.foodtech.foodtechstore.admin.api.request.RegistrationRequest;
import com.foodtech.foodtechstore.admin.api.response.AdminResponse;
import com.foodtech.foodtechstore.admin.exception.AdminExistException;
import com.foodtech.foodtechstore.admin.exception.AdminNotExistException;
import com.foodtech.foodtechstore.admin.mapping.AdminMapping;
import com.foodtech.foodtechstore.admin.service.AdminApiService;
import com.foodtech.foodtechstore.auth.api.request.AuthRequest;
import com.foodtech.foodtechstore.auth.api.response.AuthResponse;
import com.foodtech.foodtechstore.auth.exceptions.AuthException;
import com.foodtech.foodtechstore.auth.routes.AuthRoutes;
import com.foodtech.foodtechstore.auth.service.AuthService;
import com.foodtech.foodtechstore.base.api.response.OkResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AdminApiService userApiService;
    private final AuthService authService;


    @PostMapping(AuthRoutes.REGISTRATION)
    @ApiOperation(value = "Register", notes = "Use this when you need register and create new admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Admin already exist")
    })
    public OkResponse<AdminResponse> registration(@RequestBody RegistrationRequest request) throws AdminExistException {
        return OkResponse.of(AdminMapping.getInstance().getResponseMapping().convert(userApiService.registration(request)));
    }

    @PostMapping(AuthRoutes.AUTH)
    @ApiOperation(value = "Auth", notes = "Get Token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Admin not exist"),
            @ApiResponse(code = 401, message = "Bad password")
    })
    public OkResponse<AuthResponse> auth(@RequestBody AuthRequest authRequest) throws  AdminNotExistException, AuthException {
        return OkResponse.of(authService.auth(authRequest));
    }
}
