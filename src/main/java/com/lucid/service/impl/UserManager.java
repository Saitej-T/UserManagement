package com.lucid.service.impl;

import com.lucid.dao.UserDAO;
import com.lucid.dao.impl.UserMongoDAO;
import com.lucid.model.User;
import com.lucid.service.IUserManager;
import com.lucid.utilities.UserQueryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class UserManager implements IUserManager {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserMongoDAO userMongoDAO;

    @Override
    public User createUser(User user) {
        //before creatingUser convert givenMail to lowerCase
        user.setMailId(user.getMailId().toLowerCase());
        return userDAO.save(user);
    }

    @Override
    public boolean editUser(User user) {
        return userMongoDAO.editUser(user);
    }

    @Override
    public boolean deleteUser(String mailId) {
        return userMongoDAO.deleteUser(mailId);
    }

    @Override
    public User findUser(String mailId) {
       return  userDAO.findById(mailId).get();
    }

    @Override
    public Page<User> findAll(UserQueryData userQueryData) {
        return userMongoDAO.findAll(userQueryData);
    }

    @Override
    public boolean userExists(String userId){
        return userDAO.existsById(userId.toLowerCase());
    }

}
