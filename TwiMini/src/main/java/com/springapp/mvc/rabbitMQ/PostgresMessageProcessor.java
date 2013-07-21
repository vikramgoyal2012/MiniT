package com.springapp.mvc.rabbitMQ;

import com.springapp.mvc.data.TweetRepository;
import com.springapp.mvc.data.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: vivek
 * Date: 7/21/13
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class PostgresMessageProcessor extends MessageProcessor {

    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Autowired
    public PostgresMessageProcessor(UserRepository userRepository, TweetRepository tweetRepository) {
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    @Override
    protected void processModifyUserMessage(JSONObject jsonObject) {
        try {
            userRepository.modifyUser(jsonObject.getString("email"),jsonObject.getString("name"),jsonObject.getString("password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processUnFollowMessage(JSONObject jsonObject) {
        try {
            userRepository.unfollow(jsonObject.getString("email"),jsonObject.getString("otherEmail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processFollowMessage(JSONObject jsonObject) {
        try {
            userRepository.follow(jsonObject.getString("email"),jsonObject.getString("otherEmail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processRegisterMessage(JSONObject jsonObject) {
        try {
            userRepository.addUser(jsonObject.getString("email"),jsonObject.getString("name"),jsonObject.getString("password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void processAddTweetMessage(JSONObject jsonObject) {
        try {
            tweetRepository.addTweet(jsonObject.getString("email"),jsonObject.getString("content"),jsonObject.getString("timestamp"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
