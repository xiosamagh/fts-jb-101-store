package com.foodtech.foodtechstore.guest.mapping;


import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.base.mapping.BaseMapping;
import com.foodtech.foodtechstore.guest.api.request.GuestRequest;
import com.foodtech.foodtechstore.guest.api.response.GuestResponse;
import com.foodtech.foodtechstore.guest.model.GuestDoc;
import lombok.Getter;


import java.util.stream.Collectors;

@Getter
public class GuestMapping {

    public static class RequestMapping extends BaseMapping<GuestRequest, GuestDoc> {
        @Override
        public GuestDoc convert(GuestRequest guestRequest) {
            return GuestDoc.builder()

                    .id(guestRequest.getId())
                    .basketId(guestRequest.getBasketId())
                    .name(guestRequest.getName())
                    .phoneNumber(guestRequest.getPhoneNumber())
                    .address(guestRequest.getAddress())
                    .timeDelivery(guestRequest.getTimeDelivery())
                    .paymentMethod(guestRequest.getPaymentMethod())
                    .amountOrder(guestRequest.getAmountOrder())
                    .build();
        }

        @Override
        public GuestRequest unmapping(GuestDoc guestDoc) {
            throw new RuntimeException("dont use this");
        }
    }



    public static class ResponseMapping extends BaseMapping<GuestDoc, GuestResponse> {
        @Override
        public GuestResponse convert(GuestDoc guestDoc) {
            return GuestResponse.builder()
        .id(guestDoc.getId().toString())
        .basketId(guestDoc.getBasketId().toString())
        .name(guestDoc.getName())
        .phoneNumber(guestDoc.getPhoneNumber())
        .address(guestDoc.getAddress())
        .timeDelivery(guestDoc.getTimeDelivery())
        .paymentMethod(guestDoc.getPaymentMethod())
        .amountOrder(guestDoc.getAmountOrder())
                    .build();
        }

        @Override
        public GuestDoc unmapping(GuestResponse guestResponse) {
            throw new RuntimeException("dont use this");
        }
    }




    public static class SearchMapping extends BaseMapping<SearchResponse<GuestDoc>, SearchResponse<GuestResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();
        @Override
        public SearchResponse<GuestResponse> convert(SearchResponse<GuestDoc> searchResponse) {
            return SearchResponse.of(searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount());

        }

        @Override
        public SearchResponse<GuestDoc> unmapping(SearchResponse<GuestResponse> guestResponses) {
            throw new RuntimeException("dont use this");
        }
    }

    private final RequestMapping requestMapping = new RequestMapping();
    private final ResponseMapping responseMapping = new ResponseMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static GuestMapping getInstance() {
        return new GuestMapping();
    }
}

