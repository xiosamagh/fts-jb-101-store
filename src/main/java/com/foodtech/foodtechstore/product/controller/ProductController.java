package com.foodtech.foodtechstore.product.controller;

import com.foodtech.foodtechstore.auth.exceptions.AuthException;
import com.foodtech.foodtechstore.auth.exceptions.NotAccessException;
import com.foodtech.foodtechstore.base.api.response.OkResponse;
import com.foodtech.foodtechstore.product.api.request.ProductAddPriceRequest;
import com.foodtech.foodtechstore.product.api.request.ProductRequest;
import com.foodtech.foodtechstore.product.api.response.ProductResponse;
import com.foodtech.foodtechstore.product.exeception.ProductNotExistException;
import com.foodtech.foodtechstore.product.mapping.ProductMapping;
import com.foodtech.foodtechstore.product.routes.ProductApiRoutes;
import com.foodtech.foodtechstore.product.service.ProductApiService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ProductController {
//    private final ProductApiService productApiService;
//    @PostMapping (ProductApiRoutes.ADD)
//    public  OkResponse<ProductResponse> addPriseList(
//
//            @RequestBody ProductAddPriceRequest productRequest
//
//
//    ) throws NotAccessException, ProductNotExistException, AuthException, ChangeSetPersister.NotFoundException {
//        return OkResponse.of(ProductMapping.getInstance().getResponse().convert(productApiService.addPriceList(productRequest)));
//    }
}
