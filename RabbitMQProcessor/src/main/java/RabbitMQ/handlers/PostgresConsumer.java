package RabbitMQ.handlers;

import com.RabbitmqClient.RabbitMQConsumer.ManagableThread;
import com.RabbitmqClient.messageWorker.ConsumerQueue;
import com.RabbitmqClient.messageWorker.IConsumer;
import com.RabbitmqClient.utils.AppConfigQueue;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;import java.lang.Exception;import java.lang.Override;import java.lang.String;import java.lang.System;

/**
 * Created with IntelliJ IDEA.
 * User: vivek
 * Date: 7/21/13
 * Time: 2:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class PostgresConsumer extends ManagableThread implements IConsumer {
    private static final AppConfigQueue appConfigQueue = AppConfigQueue.getInstance();
    private static ConsumerQueue<String> queue=new ConsumerQueue<String>(appConfigQueue.getInt("MAX_LENGTH_MESSAGE_QUEUE"),"PostgresConsumerQueue"){
        @Override
        public boolean doWantToListen(String message)
        {
            try
            {
                JsonObject jsonMsg = new JsonParser().parse(message).getAsJsonObject();
                return true;
            }
            catch (Exception e)
            {
                System.out.println("Error in Json Parsing");
                return false;
            }
        }
    };

    private static PostgresMessageProcessor postgresMessageProcessor=new PostgresMessageProcessor();

    public PostgresConsumer(int threadNum)
    {
        super(threadNum);
        new TwitterQueueProcessor((int) (java.lang.Math.random()*10)).subscribe(this);
    }

    public ConsumerQueue<String> getQueue()
    {
        return queue;
    }

    public void run()
    {
        System.out.println("dsaa####################sdasdadss");
        String str;
        while(true)
        {
           // System.out.println("dsaasdasdadss");
            try
            {

                str=queue.take();
                System.out.println(str);
                if(!postgresMessageProcessor.processMessage(str))
                {

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
