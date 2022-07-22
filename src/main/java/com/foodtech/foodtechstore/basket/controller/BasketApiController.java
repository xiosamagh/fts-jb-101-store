package com.foodtech.foodtechstore.basket.controller;


import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import com.foodtech.foodtechstore.base.api.response.OkResponse;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.basket.api.request.BasketRequest;
import com.foodtech.foodtechstore.basket.api.response.BasketResponse;
import com.foodtech.foodtechstore.basket.exception.BasketExistException;
import com.foodtech.foodtechstore.basket.exception.BasketNotExistException;
import com.foodtech.foodtechstore.basket.mapping.BasketMapping;
import com.foodtech.foodtechstore.basket.model.BasketDoc;
import com.foodtech.foodtechstore.basket.routes.BasketApiRoutes;
import com.foodtech.foodtechstore.basket.service.BasketApiService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "Basket API")
public class BasketApiController {
    private final BasketApiService basketApiService;

    @PostMapping(BasketApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need create new basket")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Basket already exist")
    })
    public OkResponse<BasketResponse> create(@RequestBody BasketRequest request) throws BasketExistException {

        return OkResponse.of(BasketMapping.getInstance().getResponseMapping().convert(basketApiService.create(request)));
    }

    @GetMapping(BasketApiRoutes.BY_ID)
    @ApiOperation(value = "Find basket by ID", notes = "Use this when you need full info about basket")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<BasketResponse> byId(@ApiParam(value = "Basket id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(BasketMapping.getInstance().getResponseMapping().convert(basketApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)));
    }

    @GetMapping(BasketApiRoutes.ROOT)
    @ApiOperation(value = "Search basket", notes = "Use this when you need find list basket")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Basket not found")
    })
    public OkResponse<SearchResponse<BasketResponse>> search(
            @ModelAttribute SearchRequest request
            ) {

        return OkResponse.of(BasketMapping.getInstance().getSearchMapping().convert(
                basketApiService.search(request)
        ));
    }

    @PutMapping(BasketApiRoutes.BY_ID)
    @ApiOperation(value = "Update basket", notes = "Use this when you need update basket info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Basket ID Invalid")
    })
    public OkResponse<BasketResponse> updateById(
            @ApiParam(value = "Basket id") @PathVariable String id,
            @RequestBody BasketRequest basketRequest
            ) throws BasketNotExistException {
        return OkResponse.of(BasketMapping.getInstance().getResponseMapping().convert(
                basketApiService.update(basketRequest)
        ));

    }

    @DeleteMapping(BasketApiRoutes.BY_ID)
    @ApiOperation(value = "Delete basket", notes = "Use this when you need delete basket")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<String> deleteById(

            @ApiParam(value = "Basket id") @PathVariable ObjectId id
    ) {
         basketApiService.delete(id);
         return OkResponse.of(HttpStatus.OK.toString());
    }


}
