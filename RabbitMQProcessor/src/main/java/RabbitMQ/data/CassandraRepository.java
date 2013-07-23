package RabbitMQ.data;

import RabbitMQ.model.Tweet;
import RabbitMQ.model.User;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.OperationType;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.ConsistencyLevelPolicy;
import me.prettyprint.hector.api.HConsistencyLevel;
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

    private static Keyspace keyspace;
    private static CassandraHelper helper;

    public CassandraRepository(Keyspace keyspace) {
        this.keyspace=keyspace;
        this.helper=new CassandraHelper(keyspace);
    }

    public void addTweet(String email,String tweet,String timestamp)
    {
        System.out.println("adding tweet of "+email+" "+tweet);
        String tweetID=helper.getMD5(timestamp + tweet);
        helper.addInOwn(email, tweet, timestamp, tweetID);
        helper.addInFollowers(email, tweetID, timestamp);
    }

    public void registerUser(String email,String name,String password,boolean isModifyRequest)
    {
        System.out.println("reg user "+email);
        if(helper.isEmailTaken(email) && !isModifyRequest)
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
        if (helper.checkrev(user, subscription)) {
            helper.makeLink(subscription, user, 0);
            helper.makeLink(user, subscription, 0);
        } else {
            helper.makeLink(user, subscription, 1);
            helper.makeLink(subscription, user, 2);
        }
    }

    public void unfollowUser(String user,String subscription)
    {
        if (helper.checkIfTwoWayConnected(user, subscription)) {
            helper.makeLink(subscription, user, 1);
            helper.makeLink(user, subscription, 2);
        } else if(helper.checkIfOneConnected(user, subscription)){
            helper.makeLink(user, subscription, -1);
            helper.makeLink(subscription, user, -1);
        }
    }

    public List<User> getFollowers(String email,String lastEmail)
    {
        List<User> ret = new LinkedList<User>();
        SliceQuery sliceQuery1 = HFactory.createSliceQuery(keyspace, StringSerializer.get(), CompositeSerializer.get(), StringSerializer.get());
        sliceQuery1.setColumnFamily("edges");

        sliceQuery1.setKey(email).setRange(helper.getCompositeLS(2L, lastEmail), helper.getCompositeLS(2L, "zzzzzzzz"), false, 10);
        QueryResult<ColumnSlice<Composite, String>> result1 = sliceQuery1.execute();
        List<HColumn<Composite, String>> list = result1.get().getColumns();


        SliceQuery sliceQuery2 = HFactory.createSliceQuery(keyspace, StringSerializer.get(), CompositeSerializer.get(), StringSerializer.get());
        if(result1.get().getColumns().size()<10)
        {
            sliceQuery2.setColumnFamily("edges");
            sliceQuery2.setKey(email).setRange(helper.getCompositeLS(0L, lastEmail), helper.getCompositeLS(0L, "zzzzzzzz"), false, 1000000);
            QueryResult<ColumnSlice<Composite, String>> result2 = sliceQuery2.execute();
            list.addAll(result2.get().getColumns());
        }

        int i = 0;
        for (HColumn<Composite, String> value : list) {
            User user=new User();
            user.setEmail(list.get(i).getValue());
            user.setName(helper.getUsername(email));
            ret.add(user);
            i++;
        }
        return ret;
    }

    public List<User> getSubscriptions(String email,String lastEmail)
    {
        List<User> ret = new LinkedList<User>();
        SliceQuery sliceQuery1 = HFactory.createSliceQuery(keyspace, StringSerializer.get(), CompositeSerializer.get(), StringSerializer.get());
        sliceQuery1.setColumnFamily("edges");

        sliceQuery1.setKey(email).setRange(helper.getCompositeLS(1L, lastEmail), helper.getCompositeLS(1L, "zzzzzzzz"), true, 10);
        QueryResult<ColumnSlice<Composite, String>> result1 = sliceQuery1.execute();
        List<HColumn<Composite, String>> list = result1.get().getColumns();


        SliceQuery sliceQuery2 = HFactory.createSliceQuery(keyspace, StringSerializer.get(), CompositeSerializer.get(), StringSerializer.get());
        if(result1.get().getColumns().size()<10)
        {
            sliceQuery2.setColumnFamily("edges");
            sliceQuery2.setKey(email).setRange(helper.getCompositeLS(0L, lastEmail), helper.getCompositeLS(0L, "zzzzzzzz"), true, 1000000);
            QueryResult<ColumnSlice<Composite, String>> result2 = sliceQuery2.execute();
            list.addAll(result2.get().getColumns());
        }

        int i = 0;
        for (HColumn<Composite, String> value : list) {
            User user=new User();
            user.setEmail(list.get(i).getValue());
            user.setName(helper.getUsername(email));
            ret.add(user);
            i++;
        }
        return ret;
    }

    public List<Tweet> getUserTweets(String email,String lastTimestamp)
    {
        List<Tweet> ret=new LinkedList<Tweet>();
        SliceQuery sliceQuery=HFactory.createSliceQuery(keyspace, StringSerializer.get(), CompositeSerializer.get(), StringSerializer.get());
        Composite start=helper.getCompositeSS(lastTimestamp,"");
        Composite end=helper.getCompositeSS("zzzzzzzzz", "zzzzzzzzzz");

        sliceQuery.setColumnFamily("tweets").setKey(email).setRange(start,end,true,10);
        QueryResult<ColumnSlice<Composite, String>> result = sliceQuery.execute();
        List<HColumn<Composite,String>> list=result.get().getColumns();

        for(HColumn<Composite,String> tweetinfo : list)
        {
            Tweet tweet=new Tweet();
            tweet.setTweetid(tweetinfo.getName().getComponent(1).getValue(StringSerializer.get()).toString());
            tweet.setContent(tweetinfo.getValue());
            tweet.setEmail(email);
            tweet.setTimestamp(tweetinfo.getName().getComponent(0).getValue(StringSerializer.get()).toString());
            ret.add(tweet);
        }
        return ret;
    }

    public List<Tweet> getFeed(String email,String lastTimestamp)
    {
        List<Tweet> ret=new LinkedList<Tweet>();
        SliceQuery sliceQuery=HFactory.createSliceQuery(keyspace, StringSerializer.get(), CompositeSerializer.get(), CompositeSerializer.get());
        Composite start=helper.getCompositeSS(lastTimestamp, "");
        Composite end=helper.getCompositeSS("zzzzzzzzz", "zzzzzzzzzz");

        sliceQuery.setColumnFamily("tweetsforuser").setKey(email).setRange(start, end, true, 10);
        QueryResult<ColumnSlice<Composite,Composite>> result=sliceQuery.execute();
        List<HColumn<Composite,Composite>> list=result.get().getColumns();

        for(HColumn<Composite,Composite> tweetinfo :list)
        {
            Tweet tweet=new Tweet();
            tweet.setTweetid(tweetinfo.getName().getComponent(1).getValue(StringSerializer.get()).toString());
            tweet.setTimestamp(tweetinfo.getName().getComponent(0).getValue(StringSerializer.get()).toString());
            tweet.setEmail(tweetinfo.getValue().getComponent(0).getValue(StringSerializer.get()).toString());
            tweet.setContent(helper.getTweetContent(tweet.getEmail(),tweet.getTweetid()));
            ret.add(tweet);
        }

        return ret;
    }
}
