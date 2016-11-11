package edu.hm.dako.chat.client.communication.jms;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.ChatPDU;

/**
 * JMS Producer zum senden einer Nachricht.
 */
public class JmsProducer {

	private static final Log log = LogFactory.getLog(JmsProducer.class.getName());

	private static final String DEFAULT_CONNECTION_FACTORY = "jms/HTTPConnectionFactory"; //"jms/RemoteConnectionFactory";
	private static final String DEFAULT_DESTINATION = "jms/queue/chatreq2";
	private static final String DEFAULT_MESSAGE_COUNT = "1";
	private static final String DEFAULT_USERNAME = "guest";
	private static final String DEFAULT_PASSWORD = "guest";
	private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8089";

	public Boolean sendMessage(ChatPDU chatPdu) throws NamingException, JMSException {

		ConnectionFactory connectionFactory = null;
		Connection connection = null;
		Session session = null;
		MessageProducer producer = null;
		Destination destination = null;
		ObjectMessage message = null;
		Context context = null;

		try {
			// Set up the context for the JNDI lookup
			final Properties env = new Properties();
			env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
			env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
			env.put(Context.SECURITY_PRINCIPAL, System.getProperty("username", DEFAULT_USERNAME));
			env.put(Context.SECURITY_CREDENTIALS, System.getProperty("password", DEFAULT_PASSWORD));
			context = new InitialContext(env);

			// Perform the JNDI lookups
			String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
			connectionFactory = (ConnectionFactory) context.lookup(connectionFactoryString);
			log.info("Found connection factory \"" + connectionFactoryString + "\" in JNDI");

			String destinationString = System.getProperty("destination", DEFAULT_DESTINATION);
			destination = (Destination) context.lookup(destinationString);
			log.info("Found destination \"" + destinationString + "\" in JNDI");

			// Create the JMS connection, session, producer, and consumer
			connection = connectionFactory.createConnection(System.getProperty("username", DEFAULT_USERNAME),
					System.getProperty("password", DEFAULT_PASSWORD));
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			producer = session.createProducer(destination);
			connection.start();

			int count = Integer.parseInt(System.getProperty("message.count", DEFAULT_MESSAGE_COUNT));

			// Send the specified number of messages
			for (int i = 0; i < count; i++) {
				message = session.createObjectMessage(chatPdu);
				producer.send(message);
			}
			
			return true;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		} finally {
		      try { if( null != producer  ) producer.close();  } catch( Exception ex ) {/*ok*/}
		      try { if( null != session ) session.close(); } catch( Exception ex ) {/*ok*/}
		      try { if( null != connection ) connection.close(); } catch( Exception ex ) {/*ok*/}
		      try { if( null != context ) context.close();     } catch( Exception ex ) {/*ok*/}
		    }
	}
}