package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.User;

public interface UserService {
    User getById(long id);
    User getByEmail(String email);
}
