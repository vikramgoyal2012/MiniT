package com.springapp.mvc.controller;

import com.springapp.mvc.data.CassandraRepository;
import com.springapp.mvc.data.UserRepository;
import com.springapp.mvc.model.User;
import com.springapp.mvc.rabbitmq.DataExchange;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vivek
 * Date: 7/22/13
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class UserController {

    private final CassandraRepository cassandraRepository;
    private final DataExchange dataExchange;
    private final UserRepository userRepository;

    @Autowired
    public UserController(CassandraRepository cassandraRepository,DataExchange dataExchange,UserRepository userRepository) {
        this.cassandraRepository=cassandraRepository;
        this.dataExchange=dataExchange;
        this.userRepository=userRepository;
    }

    @RequestMapping("/")
    @ResponseBody
    public List<User> printWelcome(ModelMap model) {
        return null;
    }

    @RequestMapping(value = "/login" , method = RequestMethod.POST)
    @ResponseBody
    public void login(@RequestBody Map<String, String> user)
    {
        if(cassandraRepository.isEmailPresent(user.get("email")))
        {
            cassandraRepository.addToken(user.get("email"),user.get("token"));
        }
    }

    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    @ResponseBody
    public void userUpdate(@RequestBody Map<String, String> user) throws JSONException {
        if(cassandraRepository.isEmailPresent(user.get("email")))
        {
            System.out.println("Modifying new User "+user.get("password"));
            JSONObject jsonObject=new JSONObject(user);
            jsonObject.put("type",1);
            String passable=jsonObject.toString();
            dataExchange.insert(passable);
        }
    }

    @RequestMapping(value = "/followers/{email}/{lastFollowerEmail}",method = RequestMethod.GET)
    @ResponseBody
    public List<User> followersList(@PathVariable("email") String email,@PathVariable("lastFollowerEmail") String lastFollowerEmail) throws UnsupportedEncodingException {
        System.out.println("Mapped");
        byte[] authbytes= DatatypeConverter.parseBase64Binary(email) ;
        String finalEmail=new String(authbytes,"UTF-8");
        return cassandraRepository.getFollowers(finalEmail,lastFollowerEmail);
    }

    @RequestMapping(value = "/subscriptions/{email}/{lastSubscriptionEmail}",method = RequestMethod.GET)
    @ResponseBody
    public List<User> subscriptionsList(@PathVariable("email") String email,@PathVariable("lastSubscriptionEmail") String lastSubscriptionEmail) throws UnsupportedEncodingException {
        byte[] authbytes= DatatypeConverter.parseBase64Binary(email) ;
        String finalEmail=new String(authbytes,"UTF-8");
        return cassandraRepository.getSubscriptions(finalEmail,lastSubscriptionEmail);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject add(@RequestBody Map<String, String> user) throws JSONException {
        System.out.println("Creating new user: " + user.get("email") + " " + user.get("password"));
        JSONObject result=new JSONObject();
        if(!userRepository.isUserPresent(user.get("email")))
        {
            userRepository.addUser(user.get("email"),user.get("password"),user.get("name"));
            JSONObject jsonObject=new JSONObject(user);
            jsonObject.put("type",2);
            String passable=jsonObject.toString();
            dataExchange.insert(passable);
            result.put("result",1);
            return result;
        }
        result.put("result",0);
        return result;
    }

    @RequestMapping(value = "/subscriptions/{id}" ,method = RequestMethod.DELETE)
    @ResponseBody
    public void unfollow(@RequestBody Map<String,String> info) throws JSONException {
        JSONObject jsonObject=new JSONObject(info);
        jsonObject.put("email",cassandraRepository.getEmailFromToken(info.get("token")));
        jsonObject.put("type",4);
        String passable=jsonObject.toString();
        dataExchange.insert(passable);
    }

    @RequestMapping(value = "/users/{id}" ,method = RequestMethod.PUT)
    @ResponseBody
    public void follow(@RequestBody Map<String,String> info) throws JSONException {
        JSONObject jsonObject=new JSONObject(info);
        jsonObject.put("email",cassandraRepository.getEmailFromToken(info.get("token")));
        jsonObject.put("type",3);
        String passable=jsonObject.toString();
        dataExchange.insert(passable);
    }
}