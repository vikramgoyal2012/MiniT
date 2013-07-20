package com.springapp.mvc.controller;

import com.springapp.mvc.data.CassandraRepository;
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
    private final CassandraRepository cassandraRepository;

    @Autowired
    public TweetController(TweetRepository repository,CassandraRepository cassandraRepository) {
        this.repository = repository;
        this.cassandraRepository=cassandraRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    @ResponseBody
    public List<Tweet> getFeed(HttpServletRequest request,HttpServletResponse response)
    {
        return repository.getRelevanttweets(request.getAttribute("currentEmail").toString());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tweets")
    @ResponseBody
    public void addTweet(@RequestBody Map<String,String> keyMappedData,HttpServletRequest request,HttpServletResponse response)
    {
        Tweet tweet=new Tweet();
        tweet.setContent(keyMappedData.get("content"));
        //int userid=repository.getUserIDByEmail(request.getAttribute("currentEmail").toString());
        //tweet.setUserid(userid);
        //repository.addTweet(tweet.getUserid(),tweet.getContent());
        cassandraRepository.addTweet(request.getAttribute("currentEmail").toString(),tweet.getContent());
    }

    @RequestMapping(value="{userid}" ,method = RequestMethod.GET)
    @ResponseBody
    public List<Tweet> fetchUserTweets(@PathVariable("userid") int userid)
    {
         return repository.getTweetByUserid(userid);
    }

    @RequestMapping(value = "/tweets/{tweetid}")
    @ResponseBody
    public List<Tweet> fetchAnyTweet(@PathVariable("tweetid") int tweetid)
    {
         return repository.getTweetsByTweetID(tweetid);
    }
}
