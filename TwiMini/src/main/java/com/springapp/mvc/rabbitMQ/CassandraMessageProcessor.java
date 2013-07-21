package com.springapp.mvc.rabbitMQ;

import com.springapp.mvc.data.CassandraRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CassandraMessageProcessor extends MessageProcessor {
    private final CassandraRepository cassandraRepository;

    @Autowired
    public CassandraMessageProcessor(CassandraRepository cassandraRepository) {
        this.cassandraRepository = cassandraRepository;
    }

    public void processModifyUserMessage(JSONObject jsonObject)
    {
        try
        {
            cassandraRepository.registerUser(jsonObject.getString("email"),jsonObject.getString("name"),jsonObject.getString("password"),false);
        }
        catch (JSONException e){e.printStackTrace();}
    }

    public void processAddTweetMessage(JSONObject jsonObject)
    {
        try {
            cassandraRepository.addTweet(jsonObject.getString("email"),jsonObject.getString("message"),jsonObject.getString("timestamp"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void processRegisterMessage(JSONObject jsonObject)
    {
        try
        {
            cassandraRepository.registerUser(jsonObject.getString("email"),jsonObject.getString("name"),jsonObject.getString("password"),false);
        }
        catch (JSONException e){e.printStackTrace();}
    }

    public void processFollowMessage(JSONObject jsonObject)
    {
        try {
            cassandraRepository.followUser(jsonObject.getString("email"),jsonObject.getString("otherEmail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void processUnFollowMessage(JSONObject jsonObject)
    {
        try {
            cassandraRepository.unfollowUser(jsonObject.getString("email"),jsonObject.getString("otherEmail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
