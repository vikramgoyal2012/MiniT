package com.springapp.mvc.controller;

import com.springapp.mvc.data.TweetRepository;
import com.springapp.mvc.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
public class TweetController {
    private final TweetRepository repository;

    @Autowired
    public TweetController(TweetRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{lasttweetid}")
    @ResponseBody
    public List<Tweet> getFeed(@PathVariable("lasttweetid") int lasttweetid,HttpServletRequest request,HttpServletResponse response)
    {
        System.out.println("Started the process of getting tweets for the user");
        return repository.getRelevanttweets(request.getAttribute("currentEmail").toString(),lasttweetid);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tweets")
    @ResponseBody
    public void addTweet(@RequestBody Map<String,String> keyMappedData,HttpServletRequest request,HttpServletResponse response)
    {
        Tweet tweet=new Tweet();
        tweet.setContent(keyMappedData.get("content"));
        int userid=repository.getUserIDByEmail(request.getAttribute("currentEmail").toString());
        tweet.setUserid(userid);
        repository.addTweet(tweet.getUserid(),tweet.getContent());
    }

    @RequestMapping(value="{userid}/{lasttweetid}" ,method = RequestMethod.GET)
    @ResponseBody
    public List<Tweet> fetchUserTweets(@PathVariable("userid") int userid,@PathVariable("lasttweetid") int lasttweetid)
    {
         return repository.getTweetByUserid(userid,lasttweetid);
    }

    @RequestMapping(value = "/tweets/{tweetid}")
    @ResponseBody
    public List<Tweet> fetchAnyTweet(@PathVariable("tweetid") int tweetid)
    {
         return repository.getTweetsByTweetID(tweetid);
    }
}
