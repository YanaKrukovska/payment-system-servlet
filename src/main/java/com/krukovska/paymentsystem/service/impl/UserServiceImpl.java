package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.model.User;
import com.krukovska.paymentsystem.persistence.repository.Repository;
import com.krukovska.paymentsystem.service.ServiceException;
import com.krukovska.paymentsystem.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
    private final Repository<User> userRepository;

    public UserServiceImpl(Repository<User> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getById(long id) {
        logger.debug("Get user by id = {}", id);
        try {
            return userRepository.findById(id);
        } catch (SQLException e) {
            String errorMessage = "Error during getting user with id=" + id;
            logger.error("errorMessage", e);
            throw new ServiceException(errorMessage, e);
        }
    }

    @Override
    public User getByEmail(String email) {
        logger.debug("Get user by email = {}", email);
        try {
            List<User> users = userRepository.findByField(new Field("email", email));
            if  (users.isEmpty()){
                return null;
            }
            return users.get(0);
        } catch (SQLException e) {
            String errorMessage = "Error during getting user with email=" + email;
            logger.error("errorMessage", e);
            throw new ServiceException(errorMessage, e);
        }
    }

}
