package edu.hm.dako.chat.jms.connect;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * JMS Producer zum senden einer Nachricht.
 */
public class JmsProducer<T extends Serializable> {

	private ConnectionFactory confac;

	private JMSContext context;
	private JMSProducer producer;
	
	public Boolean sendMessage(String dto, JmsChatContext jmsContext) throws NamingException, JMSException {
		
		Context ctx = new InitialContext(createProperties(jmsContext));

		confac = (ConnectionFactory) ctx.lookup(jmsContext.getDefaultConnectionFactory());

		try {
			context = confac.createContext(jmsContext.getSecurityUser(), jmsContext.getSecurityPassword());
			
			// Perform the JNDI lookup
			Queue queue = (Queue) ctx.lookup(jmsContext.getQueue());
			
			producer = context.createProducer();
			producer.setDisableMessageID(true);
			producer.setDisableMessageTimestamp(true);
			producer.send(queue, context.createTextMessage(dto));
			
		} catch (Exception e) {
			return false;
		} finally {
			if (context != null) {
				context.close();
			}
		}

		return true;
	}

	private Properties createProperties(JmsChatContext jmsContext) {
		Properties jndiProps = new Properties();
		jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, jmsContext.getInitialContextFactory());
		jndiProps.put(Context.PROVIDER_URL, jmsContext.getProviderURL());
		jndiProps.put(Context.SECURITY_PRINCIPAL, jmsContext.getSecurityUser());
		jndiProps.put(Context.SECURITY_CREDENTIALS, jmsContext.getSecurityPassword());
		jndiProps.put("jboss.naming.client.ejb.context", jmsContext.getEjbContext());
		return jndiProps;
	}
}