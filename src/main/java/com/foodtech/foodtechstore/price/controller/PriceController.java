package com.foodtech.foodtechstore.price.controller;

import com.foodtech.foodtechstore.auth.exceptions.AuthException;
import com.foodtech.foodtechstore.auth.exceptions.NotAccessException;
import com.foodtech.foodtechstore.base.api.response.OkResponse;
import com.foodtech.foodtechstore.price.api.request.PriceRequest;
import com.foodtech.foodtechstore.price.exeception.PriceNotExistException;
import com.foodtech.foodtechstore.price.routes.PriceApiRoutes;
import com.foodtech.foodtechstore.price.service.PriceApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Api(value = "Price api")
public class PriceController {

    private final PriceApiService priceApiService;

    @PutMapping(PriceApiRoutes.ADD)
    @ApiOperation(value = "add price",notes="use this when you need add list product to Price")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400,message = "Price already exist")
    })
    public OkResponse<String> addProductToPriceList(@RequestBody PriceRequest request) throws NotAccessException, AuthException, PriceNotExistException {
        priceApiService.addProductToPriceList(request);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
