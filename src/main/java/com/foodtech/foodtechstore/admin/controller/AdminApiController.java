package com.foodtech.foodtechstore.admin.controller;

import com.foodtech.foodtechstore.admin.api.response.AdminResponse;
import com.foodtech.foodtechstore.admin.mapping.AdminMapping;
import com.foodtech.foodtechstore.admin.routes.AdminApiRoutes;
import com.foodtech.foodtechstore.admin.service.AdminApiService;
import com.foodtech.foodtechstore.auth.exceptions.AuthException;
import com.foodtech.foodtechstore.auth.exceptions.NotAccessException;
import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import com.foodtech.foodtechstore.base.api.response.OkResponse;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Api(value = "Admin API")
public class AdminApiController {

    private final AdminApiService adminApiService;


    @GetMapping(AdminApiRoutes.BY_ID)
    @ApiOperation(value = "Find admin by ID", notes = "Use this when you need info about admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<AdminResponse> byId(@ApiParam(value = "Admin id") @PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(AdminMapping.getInstance().getResponseMapping().convert(adminApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)));
    }

    @GetMapping(AdminApiRoutes.ROOT)

    @ApiOperation(value = "Search admin", notes = "Use this when you need find admin by email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Admin not found")
    })
    public OkResponse<SearchResponse<AdminResponse>> search(@ModelAttribute SearchRequest request) {
        return OkResponse.of(AdminMapping.getInstance().getSearchMapping().convert(adminApiService.search(request)));
    }


    @DeleteMapping(AdminApiRoutes.BY_ID)
    @ApiOperation(value = "Delete admin", notes = "Use this when you need delete admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "Admin id")@PathVariable ObjectId id) throws NotAccessException, AuthException {
        adminApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }

}
