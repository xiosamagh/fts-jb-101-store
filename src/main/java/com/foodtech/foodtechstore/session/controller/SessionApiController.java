package com.foodtech.foodtechstore.session.controller;

import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import com.foodtech.foodtechstore.base.api.response.OkResponse;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.city.exeception.CityNotExistException;
import com.foodtech.foodtechstore.price.exeception.PriceNotExistException;
import com.foodtech.foodtechstore.session.api.request.SessionRequest;
import com.foodtech.foodtechstore.session.api.response.SessionResponse;
import com.foodtech.foodtechstore.session.exception.SessionExistException;
import com.foodtech.foodtechstore.session.exception.SessionNotExistException;
import com.foodtech.foodtechstore.session.mapping.SessionMapping;
import com.foodtech.foodtechstore.session.model.SessionDoc;
import com.foodtech.foodtechstore.session.routes.SessionApiRoutes;
import com.foodtech.foodtechstore.session.service.SessionApiService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "Session API")
public class SessionApiController {
    private final SessionApiService sessionApiService;

    @PostMapping(SessionApiRoutes.ROOT)
    @ApiOperation(value = "Create", notes = "Use this when you need create new session")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Session already exist")
    })
    public OkResponse<SessionResponse> create(@RequestBody SessionRequest request) throws SessionExistException, CityNotExistException, PriceNotExistException {

        return OkResponse.of(SessionMapping.getInstance().getResponseMapping().convert(sessionApiService.create(request)));
    }

    @GetMapping(SessionApiRoutes.BY_ID)
    @ApiOperation(value = "Find session by ID", notes = "Use this when you need full info about session")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<SessionResponse> byId(@ApiParam(value = "Session id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
        return OkResponse.of(SessionMapping.getInstance().getResponseMapping().convert(sessionApiService.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new)));
    }

    @GetMapping(SessionApiRoutes.ROOT)
    @ApiOperation(value = "Search session", notes = "Use this when you need find session by ?????")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Session not found")
    })
    public OkResponse<SearchResponse<SessionResponse>> search(
            @ModelAttribute SearchRequest request
            ) {

        return OkResponse.of(SessionMapping.getInstance().getSearchMapping().convert(
                sessionApiService.search(request)
        ));
    }

    @PutMapping(SessionApiRoutes.BY_ID)
    @ApiOperation(value = "Update session", notes = "Use this when you need update session info")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Session ID Invalid")
    })
    public OkResponse<SessionResponse> updateById(
            @ApiParam(value = "Session id") @PathVariable String id,
            @RequestBody SessionRequest sessionRequest
            ) throws SessionNotExistException, PriceNotExistException {
        return OkResponse.of(SessionMapping.getInstance().getResponseMapping().convert(
                sessionApiService.update(sessionRequest)
        ));

    }

    @DeleteMapping(SessionApiRoutes.BY_ID)
    @ApiOperation(value = "Delete session", notes = "Use this when you need delete session")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success")
    })
    public OkResponse<String> deleteById(

            @ApiParam(value = "Session id") @PathVariable ObjectId id
    ) {
         sessionApiService.delete(id);
         return OkResponse.of(HttpStatus.OK.toString());
    }


}
