package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.DataSource;
import com.krukovska.paymentsystem.persistence.Field;
import com.krukovska.paymentsystem.persistence.mapper.UserMapper;
import com.krukovska.paymentsystem.persistence.model.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class UserRepositoryTest extends  AbstractDBTest{
    private UserRepository repository = new UserRepository(DataSource.getInstance(), new UserMapper());


    @Test
    void findById() throws SQLException {
        User user = repository.findById(1L);

        assertThat(user.getId(), equalTo(1L));
        assertThat(user.getName(), equalTo("Oleg Vynnyk"));
        assertThat(user.getEmail(), equalTo("olegvynnyk@gmail.com"));
        assertThat(user.getPassword(), equalTo("1111"));
        assertThat(user.getAdmin(), equalTo(true));
    }


    @Test
    void findByName() throws SQLException {
        List<User> users;
        users = repository.findByField(new Field("email", "olegvynnyk@gmail.com"));
        assertThat(users, hasSize(1));
        User user = users.get(0);
        assertThat(user.getId(), equalTo(1L));
        assertThat(user.getName(), equalTo("Oleg Vynnyk"));
        assertThat(user.getEmail(), equalTo("olegvynnyk@gmail.com"));
        assertThat(user.getPassword(), equalTo("1111"));
        assertThat(user.getAdmin(), equalTo(true));

    }

    @Test
    void getTableName() {
        assertThat(repository.getTableName(), equalToIgnoringCase("users"));
    }
}