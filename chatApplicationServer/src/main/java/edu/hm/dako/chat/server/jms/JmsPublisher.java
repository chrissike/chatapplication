package edu.hm.dako.chat.server.jms;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.ChatPDU;

/**
 * <p>
 * This is a very simple example of a JMS producer. This is a simplified version
 * of the quickstarts provided by JBoss.
 * </p>
 *
 */
public class JmsPublisher {

	private static final Log log = LogFactory.getLog(JmsPublisher.class.getName());

	private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private static final String DEFAULT_DESTINATION = "jms/topic/chatresp2";
	private static final String DEFAULT_MESSAGE_COUNT = "1";
	private static final String DEFAULT_USERNAME = "guest";
	private static final String DEFAULT_PASSWORD = "guest";
	private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8089";

	public void sendMessage(ChatPDU pdu) throws NamingException, JMSException {
		
		log.info("sendMessage() gestartet");
		
		TopicConnectionFactory connectionFactory = null;
		TopicConnection connection = null;
		TopicSession session = null;
		TopicPublisher publisher = null;
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
			connectionFactory = (TopicConnectionFactory) context.lookup(connectionFactoryString);
			log.info("Found connection factory \"" + connectionFactoryString + "\" in JNDI");

			String destinationString = System.getProperty("destination", DEFAULT_DESTINATION);
//			context = connectionFactory.createContext("guest", "guest");
//			
			destination = (Destination) context.lookup(destinationString);
			log.info("Found destination \"" + destinationString + "\" in JNDI");
			
			// Create the JMS connection, session, producer, and consumer
			connection = connectionFactory.createTopicConnection(System.getProperty("username", DEFAULT_USERNAME),
					System.getProperty("password", DEFAULT_PASSWORD));
					
			session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			publisher = session.createPublisher(session.createTopic(destinationString));
			connection.start();

			int count = Integer.parseInt(System.getProperty("message.count", DEFAULT_MESSAGE_COUNT));

			// Send the specified number of messages
			for (int i = 0; i < count; i++) {
				message = session.createObjectMessage(pdu);
				publisher.send(message);
			}

		} catch (Exception e) {
			log.info(e);
			throw e;
		} finally {
		      try { if( null != publisher  ) publisher.close();  } catch( Exception ex ) {/*ok*/}
		      try { if( null != session ) session.close(); } catch( Exception ex ) {/*ok*/}
		      try { if( null != connection ) connection.close(); } catch( Exception ex ) {/*ok*/}
		      try { if( null != context ) context.close();     } catch( Exception ex ) {/*ok*/}
		    }
	}
}