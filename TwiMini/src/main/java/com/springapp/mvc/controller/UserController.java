package com.springapp.mvc.controller;

import com.springapp.mvc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.springapp.mvc.data.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


@Controller
public class UserController {

    private final UserRepository repository;

    @Autowired
    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @RequestMapping("/")
    @ResponseBody
    public List<User> printWelcome(ModelMap model) {
        return null;
    }

    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    @ResponseBody
    public void userUpdate(@RequestBody Map<String, String> user, HttpServletResponse response)
    {
        if(repository.isUserPresent(user.get("email")))
        {
            System.out.println("Modifying new User "+user.get("password"));
            repository.modifyUser(user.get("email"),user.get("password"));
        }
    }

    @RequestMapping(value = "/{id}/followers",method = RequestMethod.GET)
    @ResponseBody
    public List<User> followersList(@PathVariable("id") int userid)
    {
        return repository.getFollowers(userid);
    }

    @RequestMapping(value = "/{id}/subscriptions",method = RequestMethod.GET)
    @ResponseBody
    public List<User> subscriptionsList(@PathVariable("id") int userid)
    {
        return repository.getSubscriptions(userid);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public void add(@RequestBody Map<String, String> user, HttpServletResponse response) {
        System.out.println("Creating new user: " + user.get("email") + " " + user.get("password"));
            System.out.println("Not found in the database");
            repository.addUser(user.get("name"),user.get("password"),user.get("email"));
    }

    @RequestMapping(value = "/{id}/subscriptions" ,method = RequestMethod.DELETE)
    @ResponseBody
    public void unfollow(@RequestBody Map<String,String> info,HttpServletResponse response,HttpServletRequest request)
    {
        int userid=repository.getUserIDByEmail(request.getAttribute("currentEmail").toString());
        int otheruserID=repository.getUserIDByEmail(info.get("userEmail"));
        repository.unfollow(userid,otheruserID);

    }

    @RequestMapping(value = "/users/{id}" ,method = RequestMethod.PUT)
    @ResponseBody
    public void follow(@RequestBody Map<String,String> info,HttpServletResponse response,HttpServletRequest request)
    {
        int userid=repository.getUserIDByEmail(request.getAttribute("currentEmail").toString());
        int otheruserID=repository.getUserIDByEmail(info.get("userEmail"));
        repository.follow(userid,otheruserID);

    }
}