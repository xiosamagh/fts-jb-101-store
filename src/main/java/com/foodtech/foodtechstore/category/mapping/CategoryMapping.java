package

        com.foodtech.foodtechstore.category.mapping;


import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.base.mapping.BaseMapping;
import com.foodtech.foodtechstore.category.api.request.CategoryRequest;
import com.foodtech.foodtechstore.category.api.response.CategoryResponse;
import com.foodtech.foodtechstore.category.model.CategoryDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CategoryMapping{

    public static class RequestMapping{


        public CategoryDoc convert(CategoryRequest categoryRequest,ObjectId adminId){
            return CategoryDoc.builder()
                .id(categoryRequest.getId())
                .adminId(adminId)
                .title(categoryRequest.getTitle())
            .build();
            }


    }

    public static class ResponseMapping extends BaseMapping<CategoryDoc,CategoryResponse>{
        @Override
        public CategoryResponse convert(CategoryDoc categoryDoc){
            return CategoryResponse.builder()
                .id(categoryDoc.getId().toString())
                .title(categoryDoc.getTitle())
                    .products(categoryDoc.getProducts())
                .build();
            }

        @Override
        public CategoryDoc unmapping(CategoryResponse categoryResponse){
            throw new RuntimeException("dont use this");
            }
            }



    public static class SearchMapping extends BaseMapping<SearchResponse< CategoryDoc>,SearchResponse<CategoryResponse>>{
        private ResponseMapping responseMapping=new ResponseMapping();

        @Override
        public SearchResponse<CategoryResponse>convert(SearchResponse<CategoryDoc> searchResponse){
                return SearchResponse.of(
                searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                searchResponse.getCount()
                );

                }

        @Override
        public SearchResponse<CategoryDoc>unmapping(SearchResponse<CategoryResponse> categoryResponses){
                throw new RuntimeException("dont use this");
                }
                }

        private final RequestMapping request=new RequestMapping();
        private final ResponseMapping response=new ResponseMapping();
        private final SearchMapping search=new SearchMapping();

        public static CategoryMapping getInstance(){
            return new CategoryMapping();
            }
}

