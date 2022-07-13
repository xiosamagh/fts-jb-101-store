package com.foodtech.foodtechstore.base.mapping;

public abstract class BaseMapping<From, To>{
    public abstract To convert(From from);
    public abstract From unmapping(To to);
}
