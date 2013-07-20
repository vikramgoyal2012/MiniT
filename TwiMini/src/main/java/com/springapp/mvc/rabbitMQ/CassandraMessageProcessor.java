package com.springapp.mvc.rabbitMQ;

import com.springapp.mvc.data.CassandraRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class CassandraMessageProcessor {
    private final CassandraRepository cassandraRepository;

    @Autowired
    public CassandraMessageProcessor(CassandraRepository cassandraRepository) {
        this.cassandraRepository = cassandraRepository;
    }

    public void processMsg(String message)
    {
        JSONObject jsonObject;
        try
        {
            jsonObject=new JSONObject(message);
            if((Integer)jsonObject.get("type")==1)
                processAddTweetMessage(jsonObject);
            else if((Integer)jsonObject.get("type")==2)
                processRegisterMessage(jsonObject);
            else if((Integer)jsonObject.get("type")==3)
                processFollowMessage(jsonObject);
            else if((Integer)jsonObject.get("type")==4)
                processUnFollowMessage(jsonObject);
            else
                processModifyUserMessage(jsonObject);
        }
        catch (JSONException e) {e.printStackTrace();}

    }

    private void processModifyUserMessage(JSONObject jsonObject)
    {
        try
        {
            cassandraRepository.registerUser(jsonObject.getString("email"),jsonObject.getString("name"),jsonObject.getString("password"),false);
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private void processAddTweetMessage(JSONObject jsonObject)
    {
        try {
            cassandraRepository.addTweet(jsonObject.getString("email"),jsonObject.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processRegisterMessage(JSONObject jsonObject)
    {
        try
        {
            cassandraRepository.registerUser(jsonObject.getString("email"),jsonObject.getString("name"),jsonObject.getString("password"),false);
        }
        catch (JSONException e){e.printStackTrace();}
    }

    private void processFollowMessage(JSONObject jsonObject)
    {
        try {
            cassandraRepository.followUser(jsonObject.getString("email"),jsonObject.getString("otherEmail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processUnFollowMessage(JSONObject jsonObject)
    {
        try {
            cassandraRepository.unfollowUser(jsonObject.getString("email"),jsonObject.getString("otherEmail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
