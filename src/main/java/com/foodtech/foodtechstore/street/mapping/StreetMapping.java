package

        com.foodtech.foodtechstore.street.mapping;


import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.base.mapping.BaseMapping;
import com.foodtech.foodtechstore.city.model.CityDoc;
import com.foodtech.foodtechstore.street.api.request.StreetRequest;
import com.foodtech.foodtechstore.street.api.response.StreetResponse;
import com.foodtech.foodtechstore.street.model.StreetDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class StreetMapping{

    public static class RequestMapping {


        public StreetDoc convert(StreetRequest streetRequest, ObjectId adminId){




            return StreetDoc.builder()
                .id(streetRequest.getId())
                .cityId(streetRequest.getCityId())
                .adminId(adminId)
                .title(streetRequest.getTitle())
            .build();
            }


    }

    public static class ResponseMapping extends BaseMapping<StreetDoc,StreetResponse>{
        @Override
        public StreetResponse convert(StreetDoc streetDoc){
            return StreetResponse.builder()
                .id(streetDoc.getId().toString())
                    .cityTitle(streetDoc.getCityTitle())
                .title(streetDoc.getTitle())
                .build();
            }

        @Override
        public StreetDoc unmapping(StreetResponse streetResponse){
            throw new RuntimeException("dont use this");
            }
            }



    public static class SearchMapping extends BaseMapping<SearchResponse< StreetDoc>,SearchResponse<StreetResponse>>{
        private ResponseMapping responseMapping=new ResponseMapping();

        @Override
        public SearchResponse<StreetResponse>convert(SearchResponse<StreetDoc> searchResponse){
                return SearchResponse.of(
                searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                searchResponse.getCount()
                );

                }

        @Override
        public SearchResponse<StreetDoc>unmapping(SearchResponse<StreetResponse> streetResponses){
                throw new RuntimeException("dont use this");
                }
                }

        private final RequestMapping request=new RequestMapping();
        private final ResponseMapping response=new ResponseMapping();
        private final SearchMapping search=new SearchMapping();

        public static StreetMapping getInstance(){
            return new StreetMapping();
            }
}

