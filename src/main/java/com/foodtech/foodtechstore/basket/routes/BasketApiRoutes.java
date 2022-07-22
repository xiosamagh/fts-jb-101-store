package com.foodtech.foodtechstore.basket.routes;

import com.foodtech.foodtechstore.base.routers.BaseApiRoutes;

public class BasketApiRoutes {
    public static final String ROOT = BaseApiRoutes.V1 + "/basket";
    public static final String BY_ID = ROOT + "/{id}";
    public static final String ADD = ROOT + "/addProduct";
    public static final String INSERT = ROOT + "/insertProduct";
    public static final String DEL = ROOT + "/deleteProduct";
}
