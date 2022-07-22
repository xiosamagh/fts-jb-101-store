package com.foodtech.foodtechstore.guest.controller;


import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import com.foodtech.foodtechstore.base.api.response.OkResponse;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.basket.exception.BasketNotExistException;
import com.foodtech.foodtechstore.guest.api.request.GuestRequest;
import com.foodtech.foodtechstore.guest.api.response.GuestResponse;
import com.foodtech.foodtechstore.guest.exception.GuestExistException;
import com.foodtech.foodtechstore.guest.exception.GuestNotExistException;
import com.foodtech.foodtechstore.guest.mapping.GuestMapping;
import com.foodtech.foodtechstore.guest.model.GuestDoc;
import com.foodtech.foodtechstore.guest.routes.GuestApiRoutes;
import com.foodtech.foodtechstore.guest.service.GuestApiService;
import com.foodtech.foodtechstore.street.exeception.StreetNotExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "Guest API")
public class GuestApiController {
    private final GuestApiService guestApiService;

    @PostMapping(GuestApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need create new guest")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Guest already exist")
    })
    public OkResponse<GuestResponse> create(@RequestBody GuestRequest request) throws GuestExistException, StreetNotExistException, BasketNotExistException {

        return OkResponse.of(GuestMapping.getInstance().getResponseMapping().convert(guestApiService.create(request)));
    }

    @GetMapping(GuestApiRoutes.BY_ID)
    @ApiOperation(value = "Find guest by ID", notes = "Use this when you need full info about guest")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<GuestResponse> byId(@ApiParam(value = "Guest id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(GuestMapping.getInstance().getResponseMapping().convert(guestApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)));
    }

    @GetMapping(GuestApiRoutes.ROOT)
    @ApiOperation(value = "Search guest", notes = "Use this when you need find list guest")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Guest not found")
    })
    public OkResponse<SearchResponse<GuestResponse>> search(
            @ModelAttribute SearchRequest request
            ) {

        return OkResponse.of(GuestMapping.getInstance().getSearchMapping().convert(
                guestApiService.search(request)
        ));
    }

    @PutMapping(GuestApiRoutes.BY_ID)
    @ApiOperation(value = "Update guest", notes = "Use this when you need update guest info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Guest ID Invalid")
    })
    public OkResponse<GuestResponse> updateById(
            @ApiParam(value = "Guest id") @PathVariable String id,
            @RequestBody GuestRequest guestRequest
            ) throws GuestNotExistException, StreetNotExistException {
        return OkResponse.of(GuestMapping.getInstance().getResponseMapping().convert(
                guestApiService.update(guestRequest)
        ));

    }

    @DeleteMapping(GuestApiRoutes.BY_ID)
    @ApiOperation(value = "Delete guest", notes = "Use this when you need delete guest")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<String> deleteById(

            @ApiParam(value = "Guest id") @PathVariable ObjectId id
    ) {
         guestApiService.delete(id);
         return OkResponse.of(HttpStatus.OK.toString());
    }


}
