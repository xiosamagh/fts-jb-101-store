package

        com.foodtech.foodtechstore.product.mapping;


import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.base.mapping.BaseMapping;
import com.foodtech.foodtechstore.product.api.request.ProductRequest;
import com.foodtech.foodtechstore.product.api.response.ProductResponse;
import com.foodtech.foodtechstore.product.model.ProductDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.stream.Collectors;

@Getter
public class ProductMapping{

    public static class RequestMapping {


        public ProductDoc convert(ProductRequest productRequest,ObjectId adminId){
            return ProductDoc.builder()
                .id(productRequest.getId())
                .adminId(adminId)
                .categoryId(productRequest.getCategoryId())
                    .price(productRequest.getPrice())
                    .priceId(productRequest.getPriceId())
                .imageURL(productRequest.getImageURL())
                .title(productRequest.getTitle())
                .description(productRequest.getDescription())
                .bju(productRequest.getBju())
                .calories(productRequest.getCalories())
            .build();
            }


    }


    public static class ResponseMapping extends BaseMapping<ProductDoc,ProductResponse>{
        @Override
        public ProductResponse convert(ProductDoc productDoc){
            return ProductResponse.builder()
                .id(productDoc.getId().toString())
                .category(productDoc.getCategory())
                .imageURL(productDoc.getImageURL())
                .price(productDoc.getPrice())
                .title(productDoc.getTitle())
                .description(productDoc.getDescription())
                .bju(productDoc.getBju())
                .calories(productDoc.getCalories())
                .build();
            }

        @Override
        public ProductDoc unmapping(ProductResponse productResponse){
            throw new RuntimeException("dont use this");
            }
            }



    public static class SearchMapping extends BaseMapping<SearchResponse< ProductDoc>,SearchResponse<ProductResponse>>{
        private ResponseMapping responseMapping=new ResponseMapping();

        @Override
        public SearchResponse<ProductResponse>convert(SearchResponse<ProductDoc> searchResponse){
                return SearchResponse.of(
                searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                searchResponse.getCount()
                );

                }

        @Override
        public SearchResponse<ProductDoc>unmapping(SearchResponse<ProductResponse> productResponses){
                throw new RuntimeException("dont use this");
                }
                }

        private final RequestMapping request=new RequestMapping();
        private final ResponseMapping response=new ResponseMapping();
        private final SearchMapping search=new SearchMapping();

        public static ProductMapping getInstance(){
            return new ProductMapping();
            }
}

