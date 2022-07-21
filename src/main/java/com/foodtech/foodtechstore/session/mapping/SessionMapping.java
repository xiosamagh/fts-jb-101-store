package com.foodtech.foodtechstore.session.mapping;


import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.base.mapping.BaseMapping;
import com.foodtech.foodtechstore.session.api.request.SessionRequest;
import com.foodtech.foodtechstore.session.api.response.SessionResponse;
import com.foodtech.foodtechstore.session.model.SessionDoc;
import lombok.Getter;


import java.util.stream.Collectors;

@Getter
public class SessionMapping {

    public static class RequestMapping  {

        public SessionDoc convert(SessionRequest sessionRequest) {
            return SessionDoc.builder()

                    .id(sessionRequest.getId())
                    .cityId(sessionRequest.getCityId())
                    .basketId(sessionRequest.getBasketId())
                    .build();
        }


    }



    public static class ResponseMapping extends BaseMapping<SessionDoc, SessionResponse> {
        @Override
        public SessionResponse convert(SessionDoc sessionDoc) {
            return SessionResponse.builder()
        .id(sessionDoc.getId().toString())
        .cityId(sessionDoc.getCityId().toString())
                    .basketId(sessionDoc.getBasketId().toString())
                    .build();
        }

        @Override
        public SessionDoc unmapping(SessionResponse sessionResponse) {
            throw new RuntimeException("dont use this");
        }
    }




    public static class SearchMapping extends BaseMapping<SearchResponse<SessionDoc>, SearchResponse<SessionResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();
        @Override
        public SearchResponse<SessionResponse> convert(SearchResponse<SessionDoc> searchResponse) {
            return SearchResponse.of(searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount());

        }

        @Override
        public SearchResponse<SessionDoc> unmapping(SearchResponse<SessionResponse> sessionResponses) {
            throw new RuntimeException("dont use this");
        }
    }

    private final RequestMapping requestMapping = new RequestMapping();
    private final ResponseMapping responseMapping = new ResponseMapping();
    private final SearchMapping searchMapping = new SearchMapping();

    public static SessionMapping getInstance() {
        return new SessionMapping();
    }
}

