package com.foodtech.foodtechstore.basket.mapping;


import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.base.mapping.BaseMapping;
import com.foodtech.foodtechstore.basket.api.request.BasketRequest;
import com.foodtech.foodtechstore.basket.api.response.BasketResponse;
import com.foodtech.foodtechstore.basket.model.BasketDoc;
import lombok.Getter;


import java.util.stream.Collectors;

@Getter
public class BasketMapping {

    public static class RequestMapping {

        public BasketDoc convert(BasketRequest basketRequest) {
            return BasketDoc.builder()

                    .id(basketRequest.getId())
                    .products(basketRequest.getProducts())
                    .sessionId(basketRequest.getSessionId())
                    .amountOrder(basketRequest.getAmountOrder())
                    .amountDelivery(basketRequest.getAmountDelivery())
                    .amountTotal(basketRequest.getAmountTotal())
                    .build();
        }


    }



    public static class ResponseMapping extends BaseMapping<BasketDoc, BasketResponse> {
        @Override
        public BasketResponse convert(BasketDoc basketDoc) {
            return BasketResponse.builder()
        .id(basketDoc.getId().toString())
        .sessionId(basketDoc.getSessionId().toString())
        .products(basketDoc.getProducts())
        .amountOrder(basketDoc.getAmountOrder())
        .amountDelivery(basketDoc.getAmountDelivery())
        .amountTotal(basketDoc.getAmountTotal())
                    .build();
        }

        @Override
        public BasketDoc unmapping(BasketResponse basketResponse) {
            throw new RuntimeException("dont use this");
        }
    }




    public static class SearchMapping extends BaseMapping<SearchResponse<BasketDoc>, SearchResponse<BasketResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();
        @Override
        public SearchResponse<BasketResponse> convert(SearchResponse<BasketDoc> searchResponse) {
            return SearchResponse.of(searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount());

        }

        @Override
        public SearchResponse<BasketDoc> unmapping(SearchResponse<BasketResponse> basketResponses) {
            throw new RuntimeException("dont use this");
        }
    }

    private final RequestMapping requestMapping = new RequestMapping();
    private final ResponseMapping responseMapping = new ResponseMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static BasketMapping getInstance() {
        return new BasketMapping();
    }
}

