package com.foodtech.foodtechstore.admin.mapping;

import com.foodtech.foodtechstore.admin.api.request.AdminRequest;
import com.foodtech.foodtechstore.admin.api.response.AdminResponse;
import com.foodtech.foodtechstore.admin.model.AdminDoc;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.base.mapping.BaseMapping;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.stream.Collectors;

@Getter
public class AdminMapping {

    public static class RequestMapping  {

        public AdminDoc convert(AdminRequest adminRequest, ObjectId adminId) {
            return AdminDoc.builder()
                    .id(adminId)
                    .email(adminRequest.getEmail())
                    .build();
        }


    }

    public static class ResponseMapping extends BaseMapping<AdminDoc, AdminResponse> {

        @Override
        public AdminResponse convert(AdminDoc adminDoc) {
            return AdminResponse.builder()
                    .id(adminDoc.getId().toString())
                    .email(adminDoc.getEmail())
                    .build();
        }

        @Override
        public AdminDoc unmapping(AdminResponse adminResponse) {
            throw new RuntimeException("dont use this");
        }
    }

    public static class SearchMapping extends BaseMapping<SearchResponse<AdminDoc>, SearchResponse<AdminResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();

        @Override
        public SearchResponse<AdminResponse> convert(SearchResponse<AdminDoc> searchResponse) {
            return SearchResponse.of(searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount());
        }

        @Override
        public SearchResponse<AdminDoc> unmapping(SearchResponse<AdminResponse> adminResponseSearchResponse) {
            throw new RuntimeException("dont use this");
        }
    }

    private final RequestMapping requestMapping = new RequestMapping();
    private final ResponseMapping responseMapping = new ResponseMapping();
    private final SearchMapping searchMapping = new SearchMapping();


    public static AdminMapping getInstance() {
        return new AdminMapping();
    }


}
