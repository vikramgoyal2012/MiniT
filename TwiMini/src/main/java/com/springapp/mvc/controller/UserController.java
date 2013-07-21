package com.springapp.mvc.controller;

import com.springapp.mvc.data.CassandraRepository;
import com.springapp.mvc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.springapp.mvc.data.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;


@Controller
public class UserController {

    @Autowired
    private UserRepository repository;
    @Autowired
    private CassandraRepository cassandraRepository;

    /*@Autowired
    public UserController(UserRepository repository,CassandraRepository cassandraRepository) {
        this.repository = repository;
        this.cassandraRepository=cassandraRepository;
    } */

    @RequestMapping("/")
    @ResponseBody
    public List<User> printWelcome(ModelMap model) {
        return null;
    }

    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    @ResponseBody
    public void userUpdate(@RequestBody Map<String, String> user, HttpServletResponse response)
    {
        /*if(repository.isUserPresent(user.get("email")))
        {
            System.out.println("Modifying new User "+user.get("password"));
<<<<<<< HEAD
            repository.modifyUser(user.get("email"),user.get("name"),user.get("password"));
        }
=======
            repository.modifyUser(user.get("email"),user.get("password"));
        }*/
        cassandraRepository.registerUser(user.get("email"),user.get("name"),user.get("password"),true);
>>>>>>> 47e73d66e1bb83d0dae2d1b92092a6648e8e1573
    }

    @RequestMapping(value = "/{email}/followers/{lastFollowerEmail}",method = RequestMethod.GET)
    @ResponseBody
    public List<User> followersList(@PathVariable("email") String email,@PathVariable("lastFollowerEmail") String lastFollowerEmail) throws UnsupportedEncodingException {
        System.out.println("Mapped");
        byte[] authbytes= DatatypeConverter.parseBase64Binary(email) ;
        String finalEmail=new String(authbytes,"UTF-8");
        return repository.getFollowers(finalEmail,lastFollowerEmail);
    }

    @RequestMapping(value = "/{email}/subscriptions/{lastSubscriptionEmail}",method = RequestMethod.GET)
    @ResponseBody
    public List<User> subscriptionsList(@PathVariable("email") String email,@PathVariable("lastSubscriptionEmail") String lastSubscriptionEmail) throws UnsupportedEncodingException {
        byte[] authbytes= DatatypeConverter.parseBase64Binary(email) ;
        String finalEmail=new String(authbytes,"UTF-8");
        return repository.getSubscriptions(finalEmail,lastSubscriptionEmail);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public void add(@RequestBody Map<String, String> user, HttpServletResponse response) {
        System.out.println("Creating new user: " + user.get("email") + " " + user.get("password"));
            System.out.println("Not found in the database");
            //repository.addUser(user.get("name"),user.get("password"),user.get("email"));
            cassandraRepository.registerUser(user.get("email"),user.get("name"),user.get("password"),false);
    }

    @RequestMapping(value = "/subscriptions/{id}" ,method = RequestMethod.DELETE)
    @ResponseBody
    public void unfollow(@RequestBody Map<String,String> info,HttpServletResponse response,HttpServletRequest request)
    {
<<<<<<< HEAD
        System.out.println();
        repository.unfollow(request.getAttribute("currentEmail").toString(),info.get("userEmail"));
=======
        //int userid=repository.getUserIDByEmail(request.getAttribute("currentEmail").toString());
        //int otheruserID=repository.getUserIDByEmail(info.get("userEmail"));
        //repository.unfollow(userid,otheruserID);
        cassandraRepository.unfollowUser(request.getAttribute("currentEmail").toString(),info.get("userEmail"));

>>>>>>> 47e73d66e1bb83d0dae2d1b92092a6648e8e1573
    }

    @RequestMapping(value = "/users/{id}" ,method = RequestMethod.PUT)
    @ResponseBody
    public void follow(@RequestBody Map<String,String> info,HttpServletResponse response,HttpServletRequest request)
    {
<<<<<<< HEAD
        repository.follow(request.getAttribute("currentEmail").toString(),info.get("userEmail"));
=======
        //int userid=repository.getUserIDByEmail(request.getAttribute("currentEmail").toString());
        //int otheruserID=repository.getUserIDByEmail(info.get("userEmail"));
        //repository.follow(userid,otheruserID);
        cassandraRepository.followUser(request.getAttribute("currentEmail").toString(),info.get("userEmail"));
>>>>>>> 47e73d66e1bb83d0dae2d1b92092a6648e8e1573
    }
}