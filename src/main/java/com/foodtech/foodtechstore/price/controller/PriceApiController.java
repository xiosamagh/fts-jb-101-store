package com.foodtech.foodtechstore.price.controller;

import com.foodtech.foodtechstore.auth.exceptions.AuthException;
import com.foodtech.foodtechstore.auth.exceptions.NotAccessException;
import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import com.foodtech.foodtechstore.base.api.response.OkResponse;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.city.exeception.CityNotExistException;
import com.foodtech.foodtechstore.price.api.request.PriceRequest;
import com.foodtech.foodtechstore.price.api.response.PriceResponse;
import com.foodtech.foodtechstore.price.exeception.PriceExistException;
import com.foodtech.foodtechstore.price.exeception.PriceNotExistException;
import com.foodtech.foodtechstore.price.mapping.PriceMapping;
import com.foodtech.foodtechstore.price.routes.PriceApiRoutes;
import com.foodtech.foodtechstore.price.service.PriceApiService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(value = "Price api")
public class PriceApiController {
    private final PriceApiService priceApiService;

    @PostMapping(PriceApiRoutes.ROOT)
    @ApiOperation(value = "Create",notes="use this when you need create new Price")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success"),
           @ApiResponse(code = 400,message = "Price already exist")
    })
    public OkResponse<PriceResponse> create(@RequestBody PriceRequest request) throws PriceExistException, CityNotExistException, AuthException {
        return OkResponse.of(PriceMapping.getInstance().getResponse().convert(priceApiService.create(request)));
    }

    @GetMapping(PriceApiRoutes.BY_ID)
    @ApiOperation(value = "find Price by id",notes = "use this if you need full information by Price")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 404,message = "Price not found"),
    })
    public OkResponse<PriceResponse> byId( @ApiParam(value = "Price id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
    return  OkResponse.of(PriceMapping.getInstance().getResponse().convert(
            priceApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    ));
    }
    @GetMapping(PriceApiRoutes.ROOT)
    @ApiOperation(value = "search Price",notes = "use this if you need find Price by title or city title")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<SearchResponse<PriceResponse>> search(
            @ModelAttribute SearchRequest request
            ){
        return  OkResponse.of(PriceMapping.getInstance().getSearch().convert(
                priceApiService.search(request)
        ));
    }



    @PutMapping(PriceApiRoutes.BY_ID)
    @ApiOperation(value = "update Price",notes = "use this if you need update Price")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400,message = "Price ID invalid"),
    })
    public OkResponse<PriceResponse> updateById(
            @ApiParam(value = "Price id") @PathVariable String id,
            @RequestBody PriceRequest priceRequest
            ) throws PriceNotExistException, NotAccessException, AuthException {
        return OkResponse.of(PriceMapping.getInstance().getResponse().convert(
                priceApiService.update(priceRequest)
        ));
    }

    @DeleteMapping(PriceApiRoutes.BY_ID)
    @ApiOperation(value = "delete Price",notes = "use this if you need delete Price")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "Price id") @PathVariable ObjectId id) throws NotAccessException, ChangeSetPersister.NotFoundException, AuthException {
        priceApiService.delete(id);

        return OkResponse.of(HttpStatus.OK.toString());

    }
}
