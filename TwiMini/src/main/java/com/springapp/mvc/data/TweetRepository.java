package com.springapp.mvc.data;


import com.springapp.mvc.model.Tweet;
import com.springapp.mvc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TweetRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TweetRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addTweet(int userid,String content){
        jdbcTemplate.execute("INSERT INTO posts(userid,content) values (" + userid + ", '"+ content +"')");
        System.out.println("Tweet Inserted");
        return 0;
    }

    public List<Tweet> getTweetByUserid(int userid)
    {
        return jdbcTemplate.query("SELECT tweetid,userid,content from posts where userid=?",new Object[]{userid},new BeanPropertyRowMapper<Tweet>(Tweet.class));
    }

    public int getUserIDByEmail(String email)
    {
        return jdbcTemplate.queryForObject("Select userid from users where email=?",new Object[]{email},new BeanPropertyRowMapper<User>(User.class)).getUserID();
    }

    public List<Tweet> getTweetsByTweetID(int tweetID)
    {
        return jdbcTemplate.query("Select content from posts where tweetid=?",new Object[]{tweetID},new BeanPropertyRowMapper<Tweet>(Tweet.class));
    }

    public List<Tweet> getRelevanttweets(String email)
    {
        int userid=getUserIDByEmail(email);
        return jdbcTemplate.query("Select userid,content from posts where tweetid in(select tweetid from tweetsforuser where userid=?)",new Object[]{userid},new BeanPropertyRowMapper<Tweet>(Tweet.class));
    }
}
