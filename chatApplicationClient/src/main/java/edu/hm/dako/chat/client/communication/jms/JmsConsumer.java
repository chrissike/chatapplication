package edu.hm.dako.chat.client.communication.jms;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JmsConsumer {

	private ConnectionFactory confac;
	private JMSContext context;
	private JMSConsumer consumer;
	private Topic topic;
	
	private static final String DEFAULT_CONNECTION_FACTORY = "jms/HTTPConnectionFactory";
	private static final String TOPIC = "jms/topic/chatresp2";

	public Message initJmsConsumer(MessageListener listener) throws NamingException {
	    Context ctx = new InitialContext(initProperties());	
	    
		confac = (ConnectionFactory) ctx.lookup(DEFAULT_CONNECTION_FACTORY);
		topic = (Topic) ctx.lookup(TOPIC);
				 
		context = confac.createContext("guest", "guest");
		
		context.setClientID(Thread.currentThread().getId() + Thread.currentThread().getName() + Math.random());

		consumer = context.createConsumer(topic);
		
		if(listener != null) {
			consumer.setMessageListener(listener);
		} else {
			return consumer.receive();
		}
		
		return null;
	}

	private Properties initProperties() {
		Properties jndiProps = new Properties();
	    jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
	    jndiProps.put(Context.URL_PKG_PREFIXES, "org.jnp.interfaces");
	    jndiProps.put(Context.PROVIDER_URL, "http-remoting://127.0.0.1:8089");
	    jndiProps.put(Context.SECURITY_PRINCIPAL, "guest");
	    jndiProps.put(Context.SECURITY_CREDENTIALS, "guest");
	    jndiProps.put("jboss.naming.client.ejb.context", true);
		return jndiProps;
	}
	
	public void closeJmsConsumer() throws NamingException {
		consumer.close();
		context.stop();
	}
}