package com.lucid.controller;

import com.lucid.model.User;
import com.lucid.service.IUserManager;
import com.lucid.utilities.Strings;
import com.lucid.utilities.UserQueryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("api/v1/user/")
public class UserController {

    @Autowired
    private IUserManager userManager;

    //for email validation
    private final String emailFormat = "^(.+)@(.+)$";

    @PostMapping("createUser")
    public ResponseEntity<Object> createUser(@RequestBody User user){
      List<String> errors = validateUser(user,true);
      if(errors.size() >0){
          return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
      }
      else {
          return ResponseEntity.ok(userManager.createUser(user));
      }
    }

    @PutMapping("editUser")
    public ResponseEntity<Object> editUser(@RequestBody User user){
        List<String> errors = validateUser(user,false);
        if(errors.size() >0){
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }
        else {
            return ResponseEntity.ok(userManager.editUser(user));
        }
    }

    //validate user details before create or edit user
    private List<String> validateUser(User user,boolean create) {
        List<String> errors = new ArrayList<>();
        if (Strings.isNullOrEmpty(user.getMailId())) {
            errors.add("emailId mandatory");
        }
        else if(!isValidMail(user.getMailId())){
            errors.add("Invalid email format");
        }
        else if(create) {
            if (userManager.userExists(user.getMailId().toLowerCase())) {
                errors.add("UserAlreadyExists with given mailId");
            }
        }
        else if(!userManager.userExists(user.getMailId().toLowerCase())){
            errors.add("User Not Found With Given mailId");
        }
        if(Strings.isNullOrEmpty(user.getFirstName())){
            errors.add("FirstName mandatory");
        }
        if(Strings.isNullOrEmpty(user.getLastName())){
            errors.add("LastName mandatory");
        }
        return errors;
    }

    //validate given emil format
    private boolean isValidMail(String userId) {
        Pattern emailPattern = Pattern.compile(emailFormat);
        if(emailPattern.matcher(userId).matches())
            return true;
        else
            return false;
    }

    @DeleteMapping("deleteUser/{mailId}")
    public ResponseEntity<Object> deleteUser(@PathVariable String mailId){
       if(!userManager.userExists(mailId.toLowerCase())){
          return new ResponseEntity<>("User Not Found To Delete",HttpStatus.BAD_REQUEST);
       }
        else {
            return ResponseEntity.ok(userManager.deleteUser(mailId.toLowerCase()));
        }
    }

    @GetMapping("findAll")
    public ResponseEntity<Page<User>> findAll(@RequestBody UserQueryData queryData){
       return ResponseEntity.ok(userManager.findAll(queryData));
    }

    @GetMapping("findUser/{mailId}")
    public ResponseEntity<Object> findUser(@PathVariable String mailId){
        if(userManager.userExists(mailId)){
            return ResponseEntity.ok(userManager.findUser(mailId));
        }
        else {
            return new ResponseEntity<>("User Not found on given mailId123",HttpStatus.BAD_REQUEST);
        }
    }

}
