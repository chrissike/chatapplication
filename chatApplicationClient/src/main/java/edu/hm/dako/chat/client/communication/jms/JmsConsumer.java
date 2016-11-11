package edu.hm.dako.chat.client.communication.jms;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSPasswordCredential;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import edu.hm.dako.chat.common.ChatPDU;

public class JmsConsumer {

	public static void main(String args[]) {
		JmsConsumer jc = new JmsConsumer();
		try {
			jc.initJmsConsumer();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@JMSConnectionFactory("java:jboss/exported/jms/RemoteConnectionFactory") //"java:/ConnectionFactory") //"java:jboss/exported/jms/RemoteConnectionFactory")
	private ConnectionFactory confac;
	
	@JMSPasswordCredential(userName = "guest", password = "guest")
	private JMSContext context;

	private static final String DEFAULT_CONNECTION_FACTORY = "jms/HTTPConnectionFactory"; //"jms/RemoteConnectionFactory";
	private static final String USERNAME = "guest";
	private static final String PASSWORD = "guest";
	private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8089";
	private static final String TOPIC = "jms/topic/chatresp2";

	public void initJmsConsumer() throws NamingException {
		final Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
		env.put(Context.SECURITY_PRINCIPAL, System.getProperty("username", USERNAME));
		env.put(Context.SECURITY_CREDENTIALS, System.getProperty("password", PASSWORD));

		Context ctx = new InitialContext(env);
		System.out.println(ctx.getEnvironment().toString());

		confac = (ConnectionFactory) ctx.lookup(DEFAULT_CONNECTION_FACTORY);
		Topic topic = (Topic) ctx.lookup(TOPIC);
		System.out.println("Topic: " + topic.toString());
		
		context = confac.createContext(USERNAME, PASSWORD);
//		context.setClientID(Thread.currentThread().getId() + Thread.currentThread().getName() + Math.random());

		JMSConsumer consumer = context.createConsumer(topic);

		TopicSubscriber sub = new TopicSubscriber();
		consumer.setMessageListener(sub);
		
//		JMSProducer producer = context.createProducer();
//		producer.send(topic, new ChatPDU());
		
//		Message msg = consumer.receiveNoWait();
//		ChatPDU pdu = null;
//		if(msg != null) {
//			try {
//				pdu = msg.getBody(ChatPDU.class);
//			} catch (JMSException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
		
//		System.out.println("Msg: " + pdu.toString());
		
//		System.out.println("Waiting for messages");
//		ChatPDU pdu2 = consumer.receiveBody(ChatPDU.class);
//		System.out.println(pdu2.toString());

		consumer.close();
		context.stop();
	}
}