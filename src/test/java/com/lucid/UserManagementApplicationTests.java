package com.lucid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucid.dao.UserDAO;
import com.lucid.model.User;
import com.lucid.model.UserStatus;
import com.lucid.utilities.UserQueryData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UserManagementApplicationTests {


    @Autowired
    private UserDAO userDAO;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @After
    @Before
    public void setup(){
        userDAO.deleteAll();
    }

    @Test
    public void createAndEditUserTest() throws Exception {
        userDAO.deleteAll();
        User user = new User();
        String data = mapper.writeValueAsString(user);
        //create user with out any data
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/createUser")
                .contentType(APPLICATION_JSON).content(data));
            resultActions.andExpect(status().is4xxClientError());
        //create user with invalid email formate
        user.setMailId("saitej");
        user.setFirstName("saiteja");
        user.setLastName("Tanniru");
        data = mapper.writeValueAsString(user);

        resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/createUser")
                .contentType(APPLICATION_JSON).content(data));
        resultActions.andExpect(status().is4xxClientError());

        //create user with valid details
        user.setMailId("saitej@123");
        user.setFirstName("saiteja");
        user.setLastName("Tanniru");
        data = mapper.writeValueAsString(user);

        resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/createUser")
                .contentType(APPLICATION_JSON).content(data));
        resultActions.andExpect(status().isOk());

        //create user with alreadyExisted email Id
        user.setMailId("saitej@123");
        user.setFirstName("saiteja");
        user.setLastName("Tanniru");
        data = mapper.writeValueAsString(user);

        resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/createUser")
                .contentType(APPLICATION_JSON).content(data));
        resultActions.andExpect(status().is4xxClientError());

    }

    @Test
    public void editUserTest() throws Exception{

        //create user with valid details
        User user = new User();
        user.setMailId("saitej@123");
        user.setFirstName("saiteja");
        user.setLastName("Tanniru");
        String  data = mapper.writeValueAsString(user);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/createUser")
                .contentType(APPLICATION_JSON).content(data));
        //edit user with non existed mail id
        user.setMailId("saitej@12");
        user.setFirstName("saiteja");
        user.setLastName("Tanniru");
         data = mapper.writeValueAsString(user);

         resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/user/editUser")
                .contentType(APPLICATION_JSON).content(data));
        resultActions.andExpect(status().is4xxClientError());

        //edit user with existed mailId
        user.setMailId("saitej@123");
        user.setFirstName("saiteja");
        user.setLastName("Tanniru");
        data = mapper.writeValueAsString(user);

        resultActions = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/user/editUser")
                .contentType(APPLICATION_JSON).content(data));
        resultActions.andExpect(status().isOk());
    }


    @Test
    public void deleteUserTest() throws Exception{

        //create user with valid details
        User user = new User();
        user.setMailId("saitej@123");
        user.setFirstName("saiteja");
        user.setLastName("Tanniru");
        String  data = mapper.writeValueAsString(user);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/createUser")
                .contentType(APPLICATION_JSON).content(data));

        //checks active status of user
        Assert.assertEquals(userDAO.findById(user.getMailId()).get().getStatus(),UserStatus.ACTIVE);

        //delete user with invalid mail
        resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/user/deleteUser/saiteja"));
        resultActions.andExpect(status().is4xxClientError());

        //delete user with existed mailId
        resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/user/deleteUser/saitej@123"));
        resultActions.andExpect(status().isOk());

        //checks inActive status of user
        Assert.assertEquals(userDAO.findById(user.getMailId()).get().getStatus(),UserStatus.INACTIVE);
    }

    @Test
    public void findUserTest() throws Exception{
        //create user with valid details
        User user = new User();
        user.setMailId("saitej@123");
        user.setFirstName("saiteja");
        user.setLastName("Tanniru");
        String  data = mapper.writeValueAsString(user);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/createUser")
                .contentType(APPLICATION_JSON).content(data));


        //find user with invalid mail
        resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/findUser/saiteja"));
        resultActions.andExpect(status().is4xxClientError());

        //delete user with existed mailId
        resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/findUser/saitej@123"));
        resultActions.andExpect(status().isOk());

    }

    @Test
    public void findAllUserTest() throws Exception{
        //create  3users with valid details
        User user = new User();
        user.setMailId("saitej@1");
        user.setFirstName("saiteja1");
        user.setLastName("Tanniru1");
        String  data = mapper.writeValueAsString(user);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/createUser")
                .contentType(APPLICATION_JSON).content(data));

        user.setMailId("saitej@12");
        user.setFirstName("saiteja2");
        user.setLastName("Tanniru2");
        data = mapper.writeValueAsString(user);

        resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/createUser")
                .contentType(APPLICATION_JSON).content(data));

        user.setMailId("saitej@123");
        user.setFirstName("saiteja3");
        user.setLastName("Tanniru3");
        data = mapper.writeValueAsString(user);

        resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/createUser")
                .contentType(APPLICATION_JSON).content(data));


        //create userDataFilter Object
         UserQueryData queryData = new UserQueryData();
         data = mapper.writeValueAsString(queryData);
        //find All Users
        resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/findAll")
                .contentType(APPLICATION_JSON).content(data));
        resultActions.andExpect(status().isOk());

        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status").value("ACTIVE"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.content[1].status").value("ACTIVE"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.content[2].status").value("ACTIVE"));


        //checks active status of user
        Assert.assertEquals(userDAO.findById("saitej@123").get().getStatus(),UserStatus.ACTIVE);

        //delete user with existed mailId
        resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/user/deleteUser/saitej@123"));
        resultActions.andExpect(status().isOk());


        queryData = new UserQueryData();
        queryData.setStatus("Active");
        data = mapper.writeValueAsString(queryData);
        //find Active Users
        resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/findAll")
                .contentType(APPLICATION_JSON).content(data));
        resultActions.andExpect(status().isOk());

        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status").value("ACTIVE"));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.content[1].status").value("ACTIVE"));

        queryData = new UserQueryData();
        queryData.setStatus("InActive");
        data = mapper.writeValueAsString(queryData);
        //find InActive Users
        resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/findAll")
                .contentType(APPLICATION_JSON).content(data));
        resultActions.andExpect(status().isOk());

        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].status").value("INACTIVE"));


    }

}
