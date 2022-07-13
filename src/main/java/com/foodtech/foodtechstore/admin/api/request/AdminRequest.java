package com.foodtech.foodtechstore.admin.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "AdminRequest",description = "Model for update admin")
public class AdminRequest {


    private String email;


}
