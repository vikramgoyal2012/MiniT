package com.springapp.mvc.controller;

import com.springapp.mvc.data.CassandraRepository;
import com.springapp.mvc.data.TweetRepository;
import com.springapp.mvc.model.Tweet;
import com.springapp.mvc.rabbitmq.DataExchange;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
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
public class TweetController {
    private final CassandraRepository cassandraRepository;
    private final DataExchange dataExchange;

    @Autowired
    public TweetController(CassandraRepository cassandraRepository,DataExchange dataExchange) throws IOException {
        this.cassandraRepository=cassandraRepository;
        this.dataExchange=dataExchange;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/feed/{lastTimeStamp}")
    @ResponseBody
    public List<Tweet> getFeed(@PathVariable("lastTimeStamp") String lastTimeStamp,@RequestBody Map<String,String> keyMappedData)
    {
        System.out.println("Started the process of getting tweets for the user");
        return cassandraRepository.getFeed(cassandraRepository.getEmailFromToken(keyMappedData.get("token")), lastTimeStamp);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tweets")
    @ResponseBody
    public void addTweet(@RequestBody Map<String,String> keyMappedData,HttpServletRequest request,HttpServletResponse response) throws JSONException {
        System.out.println("tweet call");
        JSONObject jsonObject=new JSONObject(keyMappedData);
        jsonObject.put("email",cassandraRepository.getEmailFromToken(keyMappedData.get("token")));
        jsonObject.put("timestamp",getCurrentTimeStamp());
        jsonObject.put("type",1);
        String passable=jsonObject.toString();
        dataExchange.insert(passable);
    }

    private String getCurrentTimeStamp()
    {
        java.util.Date date= new java.util.Date();
        Timestamp timestamp=(new Timestamp(date.getTime()));
        return timestamp.toString();
    }

    @RequestMapping(value="/tweets/{email}/{lastTimeStamp}" ,method = RequestMethod.GET)
    @ResponseBody
    public List<Tweet> fetchUserTweets(@PathVariable("email") String email,@PathVariable("lastTimeStamp") String lastTimeStamp) throws UnsupportedEncodingException {
        System.out.println("Mapped");
        byte[] authbytes= DatatypeConverter.parseBase64Binary(email) ;
        String finalEmail=new String(authbytes,"UTF-8");
        return cassandraRepository.getUserTweets(finalEmail, lastTimeStamp);
    }

}
