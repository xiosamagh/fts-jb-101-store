package com.foodtech.foodtechstore.order.controller;

import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import com.foodtech.foodtechstore.base.api.response.OkResponse;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.guest.exception.GuestNotExistException;
import com.foodtech.foodtechstore.order.api.request.OrderRequest;
import com.foodtech.foodtechstore.order.api.response.OrderResponse;
import com.foodtech.foodtechstore.order.exception.OrderNotExistException;
import com.foodtech.foodtechstore.order.mapping.OrderMapping;
import com.foodtech.foodtechstore.order.routes.OrderApiRoutes;
import com.foodtech.foodtechstore.order.service.OrderApiService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderApiService orderApiService;

    @PostMapping(OrderApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need create new order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Order already exist")
    })
    public OkResponse<OrderResponse> create(@RequestBody OrderRequest request) throws GuestNotExistException {

        return OkResponse.of(OrderMapping.getInstance().getResponseMapping().convert(orderApiService.create(request)));
    }

    @GetMapping(OrderApiRoutes.BY_ID)
    @ApiOperation(value = "Find order by ID", notes = "Use this when you need full info about order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<OrderResponse> byId(@ApiParam(value = "Order id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(OrderMapping.getInstance().getResponseMapping().convert(orderApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)));
    }

    @GetMapping(OrderApiRoutes.ROOT)
    @ApiOperation(value = "Search order", notes = "Use this when you need find list order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Order not found")
    })
    public OkResponse<SearchResponse<OrderResponse>> search(
            @ModelAttribute SearchRequest request
    ) {

        return OkResponse.of(OrderMapping.getInstance().getSearchMapping().convert(
                orderApiService.search(request)
        ));
    }

    @PutMapping(OrderApiRoutes.BY_ID)
    @ApiOperation(value = "Update order", notes = "Use this when you need update order info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Order ID Invalid")
    })
    public OkResponse<OrderResponse> updateById(
            @ApiParam(value = "Order id") @PathVariable String id,
            @RequestBody OrderRequest request
    ) throws OrderNotExistException {
        return OkResponse.of(OrderMapping.getInstance().getResponseMapping().convert(
                orderApiService.update(request)
        ));

    }

    @DeleteMapping(OrderApiRoutes.BY_ID)
    @ApiOperation(value = "Delete order", notes = "Use this when you need delete order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<String> deleteById(

            @ApiParam(value = "Order id") @PathVariable ObjectId id
    ) {
        orderApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
