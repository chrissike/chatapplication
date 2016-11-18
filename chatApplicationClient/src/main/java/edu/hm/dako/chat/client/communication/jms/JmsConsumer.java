package edu.hm.dako.chat.client.communication.jms;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JmsConsumer {

	@JMSConnectionFactory("java:jboss/exported/jms/RemoteConnectionFactory")
	private ConnectionFactory confac;
	private JMSContext context;
	private JMSConsumer consumer;
	private Topic topic;
	
	private static final String DEFAULT_CONNECTION_FACTORY = "jms/HTTPConnectionFactory";
	private static final String USERNAME = "guest";
	private static final String PASSWORD = "guest";
	private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8089";
	private static final String TOPIC = "jms/topic/chatresp2";

	public Message initJmsConsumer(MessageListener listener) throws NamingException {
		final Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
		env.put(Context.SECURITY_PRINCIPAL, System.getProperty("username", USERNAME));
		env.put(Context.SECURITY_CREDENTIALS, System.getProperty("password", PASSWORD));

		Context ctx = new InitialContext(env);

		confac = (ConnectionFactory) ctx.lookup(DEFAULT_CONNECTION_FACTORY);
		topic = (Topic) ctx.lookup(TOPIC);
		context = confac.createContext(USERNAME, PASSWORD);
		context.setClientID(Thread.currentThread().getId() + Thread.currentThread().getName() + Math.random());

		consumer = context.createConsumer(topic);
		
		if(listener != null) {
			consumer.setMessageListener(listener);
		} else {
			return consumer.receive();
		}
		
		return null;
	}
	
	public void closeJmsConsumer() throws NamingException {
		consumer.close();
		context.stop();
	}
}