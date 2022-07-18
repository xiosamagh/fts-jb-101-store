package com.foodtech.foodtechstore.category.service;

import com.foodtech.foodtechstore.admin.model.AdminDoc;
import com.foodtech.foodtechstore.admin.repository.AdminRepository;
import com.foodtech.foodtechstore.auth.exceptions.AuthException;
import com.foodtech.foodtechstore.auth.exceptions.NotAccessException;
import com.foodtech.foodtechstore.auth.service.AuthService;
import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.base.service.CheckAccess;
import com.foodtech.foodtechstore.category.api.request.CategoryRequest;
import com.foodtech.foodtechstore.category.mapping.CategoryMapping;
import com.foodtech.foodtechstore.category.api.response.CategoryResponse;
import com.foodtech.foodtechstore.category.exeception.CategoryExistException;
import com.foodtech.foodtechstore.category.exeception.CategoryNotExistException;
import com.foodtech.foodtechstore.category.model.CategoryDoc;
import com.foodtech.foodtechstore.category.repository.CategoryRepository;
import com.foodtech.foodtechstore.city.mapping.CityMapping;
import com.foodtech.foodtechstore.product.api.request.ProductRequest;
import com.foodtech.foodtechstore.product.api.request.ProductSearchRequest;
import com.foodtech.foodtechstore.product.model.ProductDoc;
import com.foodtech.foodtechstore.product.service.ProductApiService;
import com.foodtech.foodtechstore.street.api.request.StreetSearchRequest;
import com.foodtech.foodtechstore.street.model.StreetDoc;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryApiService extends CheckAccess<CategoryDoc> {
    private final CategoryRepository categoryRepository;
    private final MongoTemplate mongoTemplate;
    private final AuthService authService;
    private final AdminRepository adminRepository;
    private final ProductApiService productApiService;


    public CategoryDoc create(CategoryRequest request) throws AuthException {

        AdminDoc adminDoc = authService.currentAdmin();

        CategoryDoc categoryDoc =CategoryMapping.getInstance().getRequest().convert(request,adminDoc.getId());
        categoryRepository.save(categoryDoc);
        return  categoryDoc;
    }

    public Optional<CategoryDoc> findByID(ObjectId id){
        return categoryRepository.findById(id);
    }
    public SearchResponse<CategoryDoc> search(
             SearchRequest request
    ){
        Criteria criteria = new Criteria();
        if(request.getQuery() != null && request.getQuery()!=""){
            criteria = criteria.orOperator(
                    //TODO: Add Criteria
                    Criteria.where("title").regex(request.getQuery(), "i")

            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, CategoryDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<CategoryDoc> categoryDocs = mongoTemplate.find(query, CategoryDoc.class);
        return SearchResponse.of(categoryDocs, count);
    }

    public CategoryDoc update(CategoryRequest request) throws CategoryNotExistException, NotAccessException, AuthException {


        Optional<CategoryDoc> categoryDocOptional = categoryRepository.findById(request.getId());
        if(categoryDocOptional== null){
            throw new CategoryNotExistException();
        }
        CategoryDoc oldDoc = categoryDocOptional.get();
        AdminDoc admin = checkAccess(oldDoc);

        CategoryDoc categoryDoc = CategoryMapping.getInstance().getRequest().convert(request,admin.getId());
        categoryDoc.setId(request.getId());
        categoryDoc.setAdminId(admin.getId());

        categoryRepository.save(categoryDoc);

        return categoryDoc;
    }

    public void delete(ObjectId id) throws ChangeSetPersister.NotFoundException, NotAccessException, AuthException {
        checkAccess(categoryRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new));

        List<ProductDoc> productDocs = productApiService.search(ProductSearchRequest.builder().categoryId(id).size(1000).skip(0l).build()).getList();

        for (ProductDoc productDoc : productDocs) {
            productApiService.delete(productDoc.getId());
        }
        categoryRepository.deleteById(id);
    }

    @Override
    protected ObjectId getOwnerFromEntity(CategoryDoc entity) {
        return entity.getAdminId();
    }

    @Override
    protected AuthService authService() {
        return authService;
    }
}
