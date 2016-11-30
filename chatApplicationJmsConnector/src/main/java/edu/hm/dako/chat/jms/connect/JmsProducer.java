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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JMS Producer zum senden einer Nachricht.
 */
public class JmsProducer<T extends Serializable> {

	private static final Log log = LogFactory.getLog(JmsProducer.class.getName());

	private ConnectionFactory confac;

	private JMSContext context;
	private JMSProducer producer;
	
	public Boolean sendMessage(T dto, JmsChatContext jmsContext) throws NamingException, JMSException {
		
		Context ctx = new InitialContext(createProperties(jmsContext));

		confac = (ConnectionFactory) ctx.lookup(jmsContext.getDefaultConnectionFactory());

		try {
			context = confac.createContext(jmsContext.getSecurityUser(), jmsContext.getSecurityPassword());
			Queue queue = (Queue) ctx.lookup(jmsContext.getQueue());
			
			// Perform the JNDI lookups
			producer = context.createProducer();
			producer.send(queue, dto);
		} catch (Exception e) {
			//TODO sollte nur throws machen!
			log.error(e.getMessage() + ", " + e.getCause());
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