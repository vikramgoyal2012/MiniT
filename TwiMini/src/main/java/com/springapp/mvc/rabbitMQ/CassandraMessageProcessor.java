package com.springapp.mvc.rabbitMQ;

import com.springapp.mvc.data.CassandraRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.stereotype.Repository;

@Repository
public class CassandraMessageProcessor extends MessageProcessor {
=======

public class CassandraMessageProcessor {
>>>>>>> 47e73d66e1bb83d0dae2d1b92092a6648e8e1573
    private final CassandraRepository cassandraRepository;

    @Autowired
    public CassandraMessageProcessor(CassandraRepository cassandraRepository) {
        this.cassandraRepository = cassandraRepository;
    }

<<<<<<< HEAD
    public void processModifyUserMessage(JSONObject jsonObject)
=======
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
>>>>>>> 47e73d66e1bb83d0dae2d1b92092a6648e8e1573
    {
        try
        {
            cassandraRepository.registerUser(jsonObject.getString("email"),jsonObject.getString("name"),jsonObject.getString("password"),false);
        }
        catch (JSONException e){e.printStackTrace();}
    }

<<<<<<< HEAD
    public void processAddTweetMessage(JSONObject jsonObject)
    {
        try {
            cassandraRepository.addTweet(jsonObject.getString("email"),jsonObject.getString("message"),jsonObject.getString("timestamp"));
=======
    private void processAddTweetMessage(JSONObject jsonObject)
    {
        try {
            cassandraRepository.addTweet(jsonObject.getString("email"),jsonObject.getString("message"));
>>>>>>> 47e73d66e1bb83d0dae2d1b92092a6648e8e1573
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
    public void processRegisterMessage(JSONObject jsonObject)
=======
    private void processRegisterMessage(JSONObject jsonObject)
>>>>>>> 47e73d66e1bb83d0dae2d1b92092a6648e8e1573
    {
        try
        {
            cassandraRepository.registerUser(jsonObject.getString("email"),jsonObject.getString("name"),jsonObject.getString("password"),false);
        }
        catch (JSONException e){e.printStackTrace();}
    }

<<<<<<< HEAD
    public void processFollowMessage(JSONObject jsonObject)
=======
    private void processFollowMessage(JSONObject jsonObject)
>>>>>>> 47e73d66e1bb83d0dae2d1b92092a6648e8e1573
    {
        try {
            cassandraRepository.followUser(jsonObject.getString("email"),jsonObject.getString("otherEmail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
    public void processUnFollowMessage(JSONObject jsonObject)
=======
    private void processUnFollowMessage(JSONObject jsonObject)
>>>>>>> 47e73d66e1bb83d0dae2d1b92092a6648e8e1573
    {
        try {
            cassandraRepository.unfollowUser(jsonObject.getString("email"),jsonObject.getString("otherEmail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
