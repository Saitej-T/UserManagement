package com.lucid.dao;

import com.lucid.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDAO extends MongoRepository<User,String> {

}
