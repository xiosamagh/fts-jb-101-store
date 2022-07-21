package com.foodtech.foodtechstore.basket.controller;

import com.foodtech.foodtechstore.base.api.response.OkResponse;
import com.foodtech.foodtechstore.basket.api.request.BasketAddProductRequest;
import com.foodtech.foodtechstore.basket.api.response.BasketResponse;
import com.foodtech.foodtechstore.basket.exception.BasketNotExistException;
import com.foodtech.foodtechstore.basket.mapping.BasketMapping;
import com.foodtech.foodtechstore.basket.routes.BasketApiRoutes;
import com.foodtech.foodtechstore.basket.service.BasketApiService;
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
@Api(value = "Basket API")
public class BasketController {
    private final BasketApiService basketApiService;

    @PutMapping(BasketApiRoutes.ADD)
    @ApiOperation(value = "ADD", notes = "Use this when you need add products to basket")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Basket already exist")
    })
    public OkResponse<String> addProduct( @RequestBody BasketAddProductRequest request) throws BasketNotExistException {

        return OkResponse.of(BasketMapping.getInstance().getResponseMapping().convert(basketApiService.addProduct(request)));
    }

    @PutMapping(BasketApiRoutes.INSERT)
    @ApiOperation(value = "INSERT", notes = "Use this when you need add one unit product (button +)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")

    })
    public OkResponse<String> insertProduct( @RequestBody BasketAddProductRequest request) throws BasketNotExistException {

        basketApiService.insertProduct(request);
        return OkResponse.of(HttpStatus.OK.toString());
    }

    @PutMapping(BasketApiRoutes.DEL)
    @ApiOperation(value = "DEL", notes = "Use this when you need delete one unit product (button -)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")

    })
    public OkResponse<String> deleteProduct( @RequestBody BasketAddProductRequest request) throws BasketNotExistException {

        basketApiService.deleteProduct(request);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
