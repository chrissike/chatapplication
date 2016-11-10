package edu.hm.dako.chat.client.communication.jms;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.MessageListener;
import javax.jms.JMSRuntimeException;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Context;

public class JmsConsumer {
	private static ConnectionFactory connectionFactory;
	private static Topic topic;

//	public void main(String[] args) throws NamingException {
//		Context ctx = new InitialContext();
//		connectionFactory = (ConnectionFactory) ctx.lookup("java:comp/DefaultJMSConnectionFactory");
//		topic = (Topic) ctx.lookup("topic/chatresp2");
//
//		try (JMSContext context = connectionFactory.createContext();) {
//			JMSConsumer consumer = context.createSharedConsumer(topic, "SubName");
//			// JMSConsumer consumer=context.createSharedDurableConsumer(topic,
//			// "MakeItLast");
//			System.out.println("Waiting for messages on topic");
//			TopicSubscriber listener = new TopicSubscriber();
//			consumer.setMessageListener(listener);
//			System.out.println("To end program, enter Q or q, " + "then <return>");
//			InputStreamReader inputStreamReader = new InputStreamReader(System.in);
//			char answer = '\0';
//			while (!((answer == 'q') || (answer == 'Q'))) {
//				try {
//					answer = (char) inputStreamReader.read();
//				} catch (IOException e) {
//					System.err.println("I/O exception: " + e.toString());
//				}
//			}
//			System.out.println("Text messages received: " + listener.getCount());
//		} catch (JMSRuntimeException e) {
//			System.err.println("Exception occurred: " + e.toString());
//			System.exit(1);
//		}
//		System.exit(0);
//	}
	
	private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private static final String DEFAULT_MESSAGE_COUNT = "1";
	private static final String DEFAULT_USERNAME = "guest";
	private static final String DEFAULT_PASSWORD = "guest";
	private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8089";
	
	public void initJmsConsumer() throws NamingException {
		final Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
		env.put(Context.SECURITY_PRINCIPAL, System.getProperty("username", DEFAULT_USERNAME));
		env.put(Context.SECURITY_CREDENTIALS, System.getProperty("password", DEFAULT_PASSWORD));
		
		Context ctx = new InitialContext(env);

		connectionFactory = (ConnectionFactory) ctx.lookup("jms/RemoteConnectionFactory");
		topic = (Topic) ctx.lookup("jms/topic/chatresp");

		try (JMSContext context = connectionFactory.createContext(System.getProperty("username", DEFAULT_USERNAME),
				System.getProperty("password", DEFAULT_PASSWORD));) {
			JMSConsumer consumer = context.createSharedConsumer(topic, "SubName");
			// JMSConsumer consumer=context.createSharedDurableConsumer(topic,
			// "MakeItLast");
			System.out.println("Waiting for messages on topic");
			TopicSubscriber listener = new TopicSubscriber();
			consumer.setMessageListener(listener);
			System.out.println("To end program, enter Q or q, " + "then <return>");
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
