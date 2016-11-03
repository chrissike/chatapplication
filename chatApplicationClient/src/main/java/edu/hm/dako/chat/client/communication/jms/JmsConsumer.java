package edu.hm.dako.chat.client.communication.jms;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.MessageListener;
import javax.jms.JMSRuntimeException;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Context;

public class JmsConsumer{
	private static ConnectionFactory connectionFactory;
	private static Topic topic;
	
public static void main(String[] args) throws NamingException { 
Context ctx = new InitialContext();
connectionFactory = (ConnectionFactory) ctx.lookup("java:comp/DefaultJMSConnectionFactory");
topic = (Topic)ctx.lookup("topic/chatresp2");


try (JMSContext context = connectionFactory.createContext();) {
    JMSConsumer consumer = context.createSharedConsumer(topic, "SubName");
    //JMSConsumer consumer=context.createSharedDurableConsumer(topic, "MakeItLast");
    System.out.println("Waiting for messages on topic");
    TopicSubscriber listener = new TopicSubscriber();
    consumer.setMessageListener(listener);
    System.out.println(
            "To end program, enter Q or q, " + "then <return>");
    InputStreamReader inputStreamReader = new InputStreamReader(System.in);
    char answer = '\0';
    while (!((answer == 'q') || (answer == 'Q'))) {
        try {
            answer = (char) inputStreamReader.read();
        } catch (IOException e) {
            System.err.println("I/O exception: " + e.toString());
        }
    }
    System.out.println("Text messages received: " + listener.getCount());
} catch (JMSRuntimeException e) {
    System.err.println("Exception occurred: " + e.toString());
    System.exit(1);
}
System.exit(0);
}
}
