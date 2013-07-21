package com.springapp.mvc.controller;

import com.springapp.mvc.data.CassandraRepository;
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
    private final CassandraRepository cassandraRepository;

    @Autowired
    public TweetController(TweetRepository repository,CassandraRepository cassandraRepository) {
        this.repository = repository;
        this.cassandraRepository=cassandraRepository;
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
<<<<<<< HEAD
        repository.addTweet(request.getAttribute("currentEmail").toString(),keyMappedData.get("content"),getCurrentTimeStamp());
=======
        Tweet tweet=new Tweet();
        tweet.setContent(keyMappedData.get("content"));
        //int userid=repository.getUserIDByEmail(request.getAttribute("currentEmail").toString());
        //tweet.setUserid(userid);
        //repository.addTweet(tweet.getUserid(),tweet.getContent());
        cassandraRepository.addTweet(request.getAttribute("currentEmail").toString(),tweet.getContent());
>>>>>>> 47e73d66e1bb83d0dae2d1b92092a6648e8e1573
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
