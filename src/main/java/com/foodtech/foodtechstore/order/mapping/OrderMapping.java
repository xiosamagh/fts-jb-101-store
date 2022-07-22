package com.foodtech.foodtechstore.order.mapping;

import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.base.mapping.BaseMapping;

import com.foodtech.foodtechstore.guest.mapping.GuestMapping;

import com.foodtech.foodtechstore.order.api.request.OrderRequest;
import com.foodtech.foodtechstore.order.api.response.OrderResponse;
import com.foodtech.foodtechstore.order.model.OrderDoc;
import lombok.Getter;

import java.util.stream.Collectors;
@Getter
public class OrderMapping  {
    
    public static class RequestMapping  {

        
        public OrderDoc convert(OrderRequest orderRequest) {
            return OrderDoc.builder()
                    .id(orderRequest.getId())
                    .guestId(orderRequest.getGuestId())
                    .numberOrder(orderRequest.getNumberOrder())
                    .build();
        }
        
    }

    public static class ResponseMapping extends BaseMapping<OrderDoc, OrderResponse> {
        @Override
        public OrderResponse convert(OrderDoc orderDoc) {
            return OrderResponse.builder().
                    id(orderDoc.getId().toString())
                    .guestId(orderDoc.getGuestId().toString())
                    .numberOrder(orderDoc.getNumberOrder())
                    .build();
        }

        @Override
        public OrderDoc unmapping(OrderResponse orderResponse) {
            throw new RuntimeException("dont use this");
        }
    }

    public static class SearchMapping extends BaseMapping<SearchResponse<OrderDoc>, SearchResponse<OrderResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();
        @Override
        public SearchResponse<OrderResponse> convert(SearchResponse<OrderDoc> searchResponse) {
            return SearchResponse.of(searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount());

        }

        @Override
        public SearchResponse<OrderDoc> unmapping(SearchResponse<OrderResponse> OrderResponses) {
            throw new RuntimeException("dont use this");
        }
    }

    private final RequestMapping requestMapping = new RequestMapping();
    private final ResponseMapping responseMapping = new ResponseMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static OrderMapping getInstance() {
        return new OrderMapping();
    }
}
