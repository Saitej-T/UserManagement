package com.lucid.service;

import com.lucid.model.User;
import com.lucid.utilities.UserQueryData;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface IUserManager {
    public User createUser(User user);
    public boolean editUser(User user);
    public boolean deleteUser(String userId);
    public User findUser(String userId);
    public Page<User> findAll(UserQueryData userQueryData);

    public boolean userExists(String userId);
}
