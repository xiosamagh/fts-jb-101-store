package com.foodtech.foodtechstore.product.routes;

import com.foodtech.foodtechstore.base.routers.BaseApiRoutes;

public class ProductApiRoutes {
    public static final String ROOT = BaseApiRoutes.V1 +"/product";
    public static final String BY_ID = ROOT+"/{id}";
    public static final String ADD = ROOT+"/addPriceList/{id}";
}
