package com.foodtech.foodtechstore.product.model;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "Bju", description = "Bju")
public class Bju {
    private String squirrels;
    private String fats;
    private String carbons;

}
