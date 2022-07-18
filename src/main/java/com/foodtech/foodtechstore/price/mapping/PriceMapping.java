package

        com.foodtech.foodtechstore.price.mapping;


import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.base.mapping.BaseMapping;
import com.foodtech.foodtechstore.price.api.request.PriceRequest;
import com.foodtech.foodtechstore.price.api.response.PriceResponse;
import com.foodtech.foodtechstore.price.model.PriceDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.stream.Collectors;

@Getter
public class PriceMapping{

    public static class RequestMapping{


        public PriceDoc convert(PriceRequest priceRequest,ObjectId adminId){
            return PriceDoc.builder()
                .id(priceRequest.getId())
                .adminId(adminId)
                .cityId(priceRequest.getCityId())
                .priceList(priceRequest.getPriceList())
                    .title(priceRequest.getTitle())
            .build();
            }

    }



    public static class ResponseMapping extends BaseMapping<PriceDoc,PriceResponse>{
        @Override
        public PriceResponse convert(PriceDoc priceDoc){
            return PriceResponse.builder()
                .id(priceDoc.getId().toString())
                .cityId(priceDoc.getCityId().toString())
                    .title(priceDoc.getTitle())
                .priceList(priceDoc.getPriceList())
                    .cityTitle(priceDoc.getCityTitle())
                .build();
            }

        @Override
        public PriceDoc unmapping(PriceResponse priceResponse){
            throw new RuntimeException("dont use this");
            }
            }



    public static class SearchMapping extends BaseMapping<SearchResponse< PriceDoc>,SearchResponse<PriceResponse>>{
        private ResponseMapping responseMapping=new ResponseMapping();

        @Override
        public SearchResponse<PriceResponse>convert(SearchResponse<PriceDoc> searchResponse){
                return SearchResponse.of(
                searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                searchResponse.getCount()
                );

                }

        @Override
        public SearchResponse<PriceDoc>unmapping(SearchResponse<PriceResponse> priceResponses){
                throw new RuntimeException("dont use this");
                }
                }

        private final RequestMapping request=new RequestMapping();
        private final ResponseMapping response=new ResponseMapping();
        private final SearchMapping search=new SearchMapping();


        public static PriceMapping getInstance(){
            return new PriceMapping();
            }
}

