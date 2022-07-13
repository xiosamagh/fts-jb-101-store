package com.foodtech.foodtechstore.base.service;


import com.foodtech.foodtechstore.admin.model.AdminDoc;
import com.foodtech.foodtechstore.auth.exceptions.AuthException;
import com.foodtech.foodtechstore.auth.exceptions.NotAccessException;
import com.foodtech.foodtechstore.auth.service.AuthService;
import org.bson.types.ObjectId;


public abstract class CheckAccess<T> {



    protected abstract ObjectId getOwnerFromEntity(T entity);

    protected AdminDoc checkAccess(T entity) throws NotAccessException, AuthException {
        ObjectId ownerId = getOwnerFromEntity(entity);

        AdminDoc owner = authService().currentAdmin();

        if (owner.getId().equals(ownerId) == false) {
            throw new NotAccessException();
        }
        return owner;
    }

    protected abstract AuthService authService();

}
