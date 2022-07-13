package com.foodtech.foodtechstore.admin.api.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value = "AdminResponse",description = "Admin data(for search and list)")
public class AdminResponse {
    protected String id;
    protected String email;

}
