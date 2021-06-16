package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.User;

public interface UserService {

    /**
     * finds user by given id
     *
     * @param id id of user that needs to be found
     * @return found user
     */
    User getById(long id);

    /**
     * finds user by given email
     *
     * @param email email of user that needs to be found
     * @return found user
     */
    User getByEmail(String email);
}
