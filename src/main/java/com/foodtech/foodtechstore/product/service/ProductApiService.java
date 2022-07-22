package com.foodtech.foodtechstore.product.service;

import com.foodtech.foodtechstore.admin.model.AdminDoc;
import com.foodtech.foodtechstore.admin.repository.AdminRepository;
import com.foodtech.foodtechstore.auth.exceptions.AuthException;
import com.foodtech.foodtechstore.auth.exceptions.NotAccessException;
import com.foodtech.foodtechstore.auth.service.AuthService;
import com.foodtech.foodtechstore.base.api.request.SearchRequest;
import com.foodtech.foodtechstore.base.api.response.SearchResponse;
import com.foodtech.foodtechstore.base.service.CheckAccess;
import com.foodtech.foodtechstore.category.exeception.CategoryNotExistException;
import com.foodtech.foodtechstore.category.model.CategoryDoc;
import com.foodtech.foodtechstore.category.repository.CategoryRepository;
import com.foodtech.foodtechstore.price.repository.PriceRepository;
import com.foodtech.foodtechstore.price.service.PriceApiService;
import com.foodtech.foodtechstore.product.api.request.ProductRequest;
import com.foodtech.foodtechstore.product.mapping.ProductMapping;
import com.foodtech.foodtechstore.product.exeception.ProductExistException;
import com.foodtech.foodtechstore.product.exeception.ProductNotExistException;
import com.foodtech.foodtechstore.product.model.ProductDoc;
import com.foodtech.foodtechstore.product.model.ProductDocWithCategoryResponse;
import com.foodtech.foodtechstore.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductApiService extends CheckAccess<ProductDoc> {
    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;
    private final AuthService authService;
    private final AdminRepository adminRepository;
    private final CategoryRepository categoryRepository;
    private final PriceRepository priceRepository;
    private final PriceApiService priceApiService;


    public ProductDoc create(ProductRequest request) throws ProductExistException, AuthException, CategoryNotExistException {

        AdminDoc adminDoc = authService.currentAdmin();

        if (categoryRepository.findById(request.getCategoryId()).isPresent()==false) {
            throw new CategoryNotExistException();
        }
        ProductDoc productDoc =ProductMapping.getInstance().getRequest().convert(request,adminDoc.getId());
        productDoc.setCategory(categoryRepository.findById(request.getCategoryId()).get().getTitle());
        productRepository.save(productDoc);

        //
        CategoryDoc categoryDoc = categoryRepository.findById(productDoc.getCategoryId()).get();
        ProductDocWithCategoryResponse product = ProductDocWithCategoryResponse.builder()
                        .id(productDoc.getId().toString())
                                .title(productDoc.getTitle())
                                        .description(productDoc.getDescription())
                                                .category(productDoc.getCategory())
                                                        .bju(productDoc.getBju())
                                                                .imageURL(productDoc.getImageURL())
                                                                        .price(productDoc.getPrice())
                                                                                .build();

        categoryDoc.getProducts().add(product);
        categoryRepository.save(categoryDoc);
        return  productDoc;
    }

    public Optional<ProductDoc> findByID(ObjectId id){
        return productRepository.findById(id);
    }
    public SearchResponse<ProductDoc> search(
             SearchRequest request
    ){
        Criteria criteria = new Criteria();
        if(request.getQuery() != null && request.getQuery()!=""){
            criteria = criteria.orOperator(
                    //TODO: Add Criteria
                    Criteria.where("category").regex(request.getQuery(), "i"),
                    Criteria.where("title").regex(request.getQuery(), "i")


            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, ProductDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<ProductDoc> productDocs = mongoTemplate.find(query, ProductDoc.class);
        return SearchResponse.of(productDocs, count);
    }

    public ProductDoc update(ProductRequest request) throws ProductNotExistException, NotAccessException, AuthException {
        Optional<ProductDoc> productDocOptional = productRepository.findById(request.getId());
        if(productDocOptional == null){
            throw new ProductNotExistException();
        }
        ProductDoc oldDoc = productDocOptional.get();
        AdminDoc admin = checkAccess(oldDoc);

        ProductDoc productDoc = ProductMapping.getInstance().getRequest().convert(request,admin.getId());
        productDoc.setId(request.getId());
        productDoc.setAdminId(oldDoc.getAdminId());
        productDoc.setCategoryId(oldDoc.getCategoryId());


        productRepository.save(productDoc);

        return productDoc;
    }

    public void delete(ObjectId id) throws ChangeSetPersister.NotFoundException, NotAccessException, AuthException {
        checkAccess(productRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new));

        productRepository.deleteById(id);
    }


    @Override
    protected ObjectId getOwnerFromEntity(ProductDoc entity) {
        return entity.getAdminId();
    }

    @Override
    protected AuthService authService() {
        return authService ;
    }
}
