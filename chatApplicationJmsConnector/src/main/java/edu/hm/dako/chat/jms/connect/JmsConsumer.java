package edu.hm.dako.chat.jms.connect;

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

	public Message initJmsConsumer(MessageListener listener, JmsChatContext jmsContext) throws NamingException {
	    Context ctx = new InitialContext(initProperties(jmsContext));	
	    
		confac = (ConnectionFactory) ctx.lookup(jmsContext.getDefaultConnectionFactory());
		Topic topic = (Topic) ctx.lookup(jmsContext.getTopic());
				 
		context = confac.createContext(jmsContext.getSecurityUser(), jmsContext.getSecurityPassword());
		
		context.setClientID(Thread.currentThread().getId() + Thread.currentThread().getName() + Math.random());

		consumer = context.createConsumer(topic);
		
		if(listener != null) {
			consumer.setMessageListener(listener);
		} else {
			return consumer.receive();
		}
		
		return null;
	}

	private Properties initProperties(JmsChatContext jmsContext) {
		Properties jndiProps = new Properties();
	    jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, jmsContext.getInitialContextFactory());
	    jndiProps.put(Context.URL_PKG_PREFIXES, jmsContext.getUrlPkgPrefixes());
	    jndiProps.put(Context.PROVIDER_URL, jmsContext.getProviderURL());
	    jndiProps.put(Context.SECURITY_PRINCIPAL, jmsContext.getSecurityUser());
	    jndiProps.put(Context.SECURITY_CREDENTIALS, jmsContext.getSecurityPassword());
	    jndiProps.put("jboss.naming.client.ejb.context", jmsContext.getEjbContext());
		return jndiProps;
	}
	
	public void closeJmsConsumer() throws NamingException {
		consumer.close();
		context.stop();
	}
}