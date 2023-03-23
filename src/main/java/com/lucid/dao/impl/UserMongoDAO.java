package com.lucid.dao.impl;

import com.lucid.model.User;
import com.lucid.model.UserStatus;
import com.lucid.utilities.Strings;
import com.lucid.utilities.UserQueryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;


import java.util.Locale;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class UserMongoDAO {
    @Autowired
    public MongoTemplate mongoTemplate;

    public boolean editUser(User user) {
        final Query query = new Query();
        query.addCriteria(where("mailId").is(user.getMailId().toLowerCase()));
        Update update = new Update();
        update.set("firstName",user.getFirstName());
        update.set("lastName",user.getLastName());
        return mongoTemplate.updateMulti(query,update,User.class).getModifiedCount()>0;
    }

    public boolean deleteUser(String mailId) {
        final Query query = new Query();
        query.addCriteria(where("mailId").is(mailId.toLowerCase()));
        Update update = new Update();
        update.set("status", UserStatus.INACTIVE);
        return mongoTemplate.updateMulti(query,update,User.class).getModifiedCount()>0;
    }

    public Page<User> findAll(UserQueryData userQueryData) {
        final Query query = new Query();
        if(!Strings.isNullOrEmpty(userQueryData.getUserName())){
            query.addCriteria(new Criteria().orOperator(where("firstName").regex(userQueryData.getUserName()),where("lastName").regex(userQueryData.getUserName()),where("emailId").regex(userQueryData.getUserName())));
        }
        if(!Strings.isNullOrEmpty(userQueryData.getStatus())){
            if(userQueryData.getStatus().equalsIgnoreCase("Active")){
                query.addCriteria(where("status").is(UserStatus.ACTIVE));
            }
            else if(userQueryData.getStatus().equalsIgnoreCase("InActive")){
                query.addCriteria(where("status").is(UserStatus.INACTIVE));
            }
        }
        Pageable pageable = null;
        if(userQueryData.getPageNo() >0){
           pageable = PageRequest.of(userQueryData.getPageNo()-1,10) ;
        }
        else {
            pageable = PageRequest.of(0,10) ;
        }

        //find total noOf records
        long totalRecords = mongoTemplate.count(query,User.class);
        //for applying pagination
        query.with(pageable);
        return new PageImpl<>(mongoTemplate.find(query,User.class),pageable,totalRecords);
    }

}
