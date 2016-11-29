package edu.hm.dako.chat.client.communication.jms;

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

import edu.hm.dako.chat.model.ChatPDU;

/**
 * JMS Producer zum senden einer Nachricht.
 */
public class JmsProducer {

	private static final Log log = LogFactory.getLog(JmsProducer.class.getName());

	private ConnectionFactory confac;

	private JMSContext context;
	private JMSProducer producer;
	private Queue queue;

	private static final String DEFAULT_CONNECTION_FACTORY = "jms/HTTPConnectionFactory"; // "jms/RemoteConnectionFactory";
	private static final String QUEUE = "jms/queue/chatreq2";
	
	public Boolean sendMessage(ChatPDU chatPdu) throws NamingException, JMSException {

		Context ctx = new InitialContext(createProperties());

		confac = (ConnectionFactory) ctx.lookup(DEFAULT_CONNECTION_FACTORY);

		try {
			context = confac.createContext("guest", "guest");
			queue = (Queue) ctx.lookup(QUEUE);
			
			// Perform the JNDI lookups
			producer = context.createProducer();
			producer.send(queue, chatPdu);
		} catch (Exception e) {
			log.error(e.getMessage() + ", " + e.getCause());
			return false;
		} finally {
			if (context != null) {
				context.close();
			}
		}

		return true;
	}

	private Properties createProperties() {
		Properties jndiProps = new Properties();
		jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		jndiProps.put(Context.PROVIDER_URL, "http-remoting://127.0.0.1:8089");
		jndiProps.put(Context.SECURITY_PRINCIPAL, "guest");
		jndiProps.put(Context.SECURITY_CREDENTIALS, "guest");
		jndiProps.put("jboss.naming.client.ejb.context", true);
		return jndiProps;
	}
}