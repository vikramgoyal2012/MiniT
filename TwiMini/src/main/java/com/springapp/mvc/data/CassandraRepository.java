package com.springapp.mvc.data;

import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: vivek
 * Date: 7/19/13
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CassandraRepository {

    private final Keyspace keyspace;

    @Autowired
    public CassandraRepository(Keyspace keyspace) {
        this.keyspace = keyspace;
    }

    public String getMD5(String plaintext)
    {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.reset();
        m.update(plaintext.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString(16);
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }

    public void addTweet(String email,String tweet)
    {
        java.util.Date date= new java.util.Date();
        String timestamp=(new Timestamp(date.getTime())).toString();
        String tweetID=getMD5(timestamp+tweet);
        addInOwn(email,tweet,timestamp,tweetID);
        addInFollowers(email,tweetID,timestamp);
    }

    public boolean isEmailTaken(String email)
    {
        SliceQuery sliceQuery=HFactory.createSliceQuery(keyspace,StringSerializer.get(),StringSerializer.get(),StringSerializer.get());
        sliceQuery.setColumnFamily("users");
        sliceQuery.setKey(email).setRange("","",false,1);
        QueryResult<ColumnSlice<String,String>> result = sliceQuery.execute();
        if(result.get().getColumns().isEmpty())
            return false;
        return true;
    }

    private boolean checkrev(String first, String second)
    {
        SliceQuery sliceQuery = HFactory.createSliceQuery(keyspace, StringSerializer.get(), CompositeSerializer.get(), StringSerializer.get());
        sliceQuery.setColumnFamily("edges");
        sliceQuery.setKey(second).setRange(getCompositeLS(0L, first), getCompositeLS(1L, first), false, 1);

        QueryResult<ColumnSlice<Composite, String>> result = sliceQuery.execute();
        if (result.get().getColumns().isEmpty() || result.get().getColumns().get(0) == null ||result.get().getColumns().get(0).getValue() == null)
            return false;
        return true;
    }



    public void registerUser(String email,String name,String password,boolean isModifyRequest)
    {
        System.out.println("reg user "+email);
        if(isEmailTaken(email) && !isModifyRequest)
        {
            //do Something
            System.out.println("email taken");
            return;
        }
        Mutator<String> mutator=HFactory.createMutator(keyspace,StringSerializer.get());
        if(isModifyRequest)
        {
            mutator.addDeletion(email,"users","password",StringSerializer.get());
            mutator.addDeletion(email,"users","name",StringSerializer.get());
            mutator.execute();
        }
        HColumn<String,String> column1=HFactory.createColumn("name",name);
        HColumn<String,String> column2=HFactory.createColumn("password",password);
        mutator.addInsertion(email,"users",column1);
        mutator.addInsertion(email,"users",column2);
        mutator.execute();
    }


    public void followUser(String user,String subscription)
    {
        if (checkrev(user, subscription)) {
            makeLink(subscription, user, 0);
            makeLink(user, subscription, 0);
        } else {
            makeLink(user, subscription, 1);
            makeLink(subscription, user, 2);
        }
    }

    public void unfollowUser(String user,String subscription)
    {
        if (checkIfTwoWayConnected(user,subscription)) {
            makeLink(subscription, user, 1);
            makeLink(user, subscription, 2);
        } else if(checkIfOneConnected(user, subscription)){
            makeLink(user, subscription, -1);
            makeLink(subscription, user, -1);
        }
    }

    public boolean checkIfTwoWayConnected(String first,String second)
    {
        SliceQuery sliceQuery = HFactory.createSliceQuery(keyspace, StringSerializer.get(), CompositeSerializer.get(), StringSerializer.get());
        sliceQuery.setColumnFamily("edges");

        sliceQuery.setKey(second).setRange(getCompositeLS(0L, first), getCompositeLS(0L, first), false, 1);
        QueryResult<ColumnSlice<Composite, String>> result = sliceQuery.execute();
        if (result.get().getColumns().isEmpty() || result.get().getColumns().get(0).getValue() == null)
            return false;
        return true;
    }

    public boolean checkIfOneConnected(String first,String second)
    {
        SliceQuery sliceQuery = HFactory.createSliceQuery(keyspace, StringSerializer.get(), CompositeSerializer.get(), StringSerializer.get());
        sliceQuery.setColumnFamily("edges");

        sliceQuery.setKey(second).setRange(getCompositeLS(2L, first), getCompositeLS(2L, first), false, 1);
        QueryResult<ColumnSlice<Composite, String>> result = sliceQuery.execute();
        if (result.get().getColumns().isEmpty() || result.get().getColumns().get(0).getValue() == null)
            return false;
        return true;
    }

    private void makeLink(String first, String second, int i) {
        System.out.println(first + " " + second);
        Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
        try {
            mutator.addDeletion(first, "edges", getCompositeLS(0L, second), CompositeSerializer.get());
            mutator.addDeletion(first, "edges", getCompositeLS(1L, second), CompositeSerializer.get());
            mutator.addDeletion(first, "edges", getCompositeLS(2L, second), CompositeSerializer.get());
            mutator.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(i==-1)
                return;
            HColumn<Composite, String> column = HFactory.createColumn(getCompositeLS(new Long(i), second), second);
            Mutator<String> addInsertion = mutator.addInsertion(first, "edges", column);
            mutator.execute();
        }

    }

    public Composite getCompositeLS(Long l, String first) {
        Composite composite = new Composite();
        composite.addComponent(l, LongSerializer.get());
        composite.addComponent(first, StringSerializer.get());
        return composite;
    }

    public Composite getCompositeSS(String first, String second) {
        Composite composite = new Composite();
        composite.addComponent(first, StringSerializer.get());
        composite.addComponent(second, StringSerializer.get());
        return composite;
    }

    public List<String> getFollowers(String email) {
        List<String> ret = new LinkedList<String>();
        SliceQuery sliceQuery1 = HFactory.createSliceQuery(keyspace, StringSerializer.get(), CompositeSerializer.get(), StringSerializer.get());
        sliceQuery1.setColumnFamily("edges");

        SliceQuery sliceQuery2 = HFactory.createSliceQuery(keyspace, StringSerializer.get(), CompositeSerializer.get(), StringSerializer.get());
        sliceQuery2.setColumnFamily("edges");

        sliceQuery1.setKey(email).setRange(getCompositeLS(2L, ""), getCompositeLS(2L, "zzzzzzzz"), false, 1000000);
        QueryResult<ColumnSlice<Composite, String>> result1 = sliceQuery1.execute();

        sliceQuery2.setKey(email).setRange(getCompositeLS(0L, ""), getCompositeLS(0L, "zzzzzzzz"), false, 1000000);
        QueryResult<ColumnSlice<Composite, String>> result2 = sliceQuery2.execute();

        List<HColumn<Composite, String>> list = result2.get().getColumns();
        list.addAll(result1.get().getColumns());
        int i = 0;
        for (HColumn<Composite, String> value : list) {
            ret.add(list.get(i).getValue());
            i++;
        }
        return ret;
    }

    private void addInFollowers(String email, String tweetID, String timestamp)
    {
        List<String> followers = getFollowers(email);
        for(String follower:followers)
            addRelevantTweet(email,follower,timestamp,tweetID);
    }

    private void addRelevantTweet(String email, String follower, String timestamp, String tweetID)
    {
        Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
        HColumn<Composite,Composite> column=HFactory.createColumn(getCompositeSS(timestamp, tweetID),getCompositeSS(email, tweetID));
        mutator.addInsertion(follower,"tweetsforuser",column);
        mutator.execute();
    }

    public void addInOwn(String email,String tweet,String timestamp,String tweetID)
    {
        Composite composite=getCompositeSS(timestamp, tweetID);

        Mutator<String> mutator= HFactory.createMutator(keyspace,StringSerializer.get());
        HColumn<Composite,String> column = HFactory.createColumn(composite,tweet);
        mutator.addInsertion(email,"tweets",column);

        mutator.execute();
    }
}
