package

        com.foodtech.foodtechstore.city.mapping;


import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.base.mapping.BaseMapping;
import com.foodtech.foodtechstore.city.api.request.CityRequest;
import com.foodtech.foodtechstore.city.api.response.CityResponse;
import com.foodtech.foodtechstore.city.model.CityDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CityMapping{

    public static class RequestMapping{


        public CityDoc convert(CityRequest cityRequest, ObjectId adminId){
            return CityDoc.builder()
                .id(cityRequest.getId())
                .priceDelivery(cityRequest.getPriceDelivery())
                .timeDelivery(cityRequest.getTimeDelivery())
                .title(cityRequest.getTitle())
                .adminId(adminId)
            .build();
            }


    }

    public static class ResponseMapping extends BaseMapping<CityDoc,CityResponse>{
        @Override
        public CityResponse convert(CityDoc cityDoc){
            return CityResponse.builder()
                .id(cityDoc.getId().toString())
                .priceDelivery(cityDoc.getPriceDelivery())
                .timeDelivery(cityDoc.getTimeDelivery())
                .title(cityDoc.getTitle())
                .build();
            }

        @Override
        public CityDoc unmapping(CityResponse cityResponse){
            throw new RuntimeException("dont use this");
            }
            }



    public static class SearchMapping extends BaseMapping<SearchResponse< CityDoc>,SearchResponse<CityResponse>>{
        private ResponseMapping responseMapping=new ResponseMapping();

        @Override
        public SearchResponse<CityResponse>convert(SearchResponse<CityDoc> searchResponse){
                return SearchResponse.of(
                searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                searchResponse.getCount()
                );

                }

        @Override
        public SearchResponse<CityDoc>unmapping(SearchResponse<CityResponse> cityResponses){
                throw new RuntimeException("dont use this");
                }
                }

        private final RequestMapping request=new RequestMapping();
        private final ResponseMapping response=new ResponseMapping();
        private final SearchMapping search=new SearchMapping();

        public static CityMapping getInstance(){
            return new CityMapping();
            }
}

