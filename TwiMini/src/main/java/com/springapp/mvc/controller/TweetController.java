package com.springapp.mvc.controller;

import com.springapp.mvc.data.TweetRepository;
import com.springapp.mvc.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Controller
public class TweetController {
    private final TweetRepository repository;

    @Autowired
    public TweetController(TweetRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{lastTimeStamp}")
    @ResponseBody
    public List<Tweet> getFeed(@PathVariable("lastTimeStamp") String lastTimeStamp,HttpServletRequest request,HttpServletResponse response)
    {
        System.out.println("Started the process of getting tweets for the user");
        return repository.getRelevanttweets(request.getAttribute("currentEmail").toString(),lastTimeStamp);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tweets")
    @ResponseBody
    public void addTweet(@RequestBody Map<String,String> keyMappedData,HttpServletRequest request,HttpServletResponse response)
    {
        repository.addTweet(request.getAttribute("currentEmail").toString(),keyMappedData.get("content"),getCurrentTimeStamp());
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
         return repository.getTweetByUserEmail(finalEmail,lastTimeStamp);
    }

}
