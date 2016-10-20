package edu.hm.dako.chat.client.communication.jms;

import java.util.Properties;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.naming.Context;

import edu.hm.dako.chat.common.ChatPDU;

/**
 * <p>
 * This is a very simple example of a JMS producer. This is a simplified version
 * of the quickstarts provided by JBoss.
 * </p>
 *
 */
public class SimpleJmsProducer2 {

	// TODO: Die Konfiguration entsprechend im Code hinterlegen
	// <jms-destinations>
	// <jms-queue name="ExpiryQueue">
	// <entry name="java:/jms/queue/ExpiryQueue"/>
	// </jms-queue>
	// <jms-queue name="DLQ">
	// <entry name="java:/jms/queue/DLQ"/>
	// </jms-queue>
	// <jms-queue name="ChatRequestQueue">
	// <entry name="jms/queue/chatreq"/>
	// <entry name="java:jboss/exported/jms/queue/chatreq"/>
	// </jms-queue>
	// <jms-topic name="ChatResponseTopic">
	// <entry name="jms/topic/chatresp"/>
	// <entry name="java:jboss/exported/jms/topic/chatresp"/>
	// </jms-topic>
	// </jms-destinations>

	private static final String QUEUE_DESTINATION = "jms/queue/chatreq";
	private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
	private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8080";

	@Resource(mappedName = QUEUE_DESTINATION)
	private Queue myQueue;

	@Inject
	private JMSContext jmsContext;

	public void sendJms() {
		// Set up the namingContext for the JNDI lookup
		// Make sure you create an application user in Wildfly that matches the
		// username and password below. Usually a bad practice to have passwords
		// in code, but this is just a simple example.
		final Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
		env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
		env.put(Context.SECURITY_PRINCIPAL, "guest"); // username
		env.put(Context.SECURITY_CREDENTIALS, "guest"); // password

		jmsContext.createProducer().send(myQueue, jmsContext.createObjectMessage(new ChatPDU()));

		// namingContext = new InitialContext(env);

		// Use JNDI to look up the connection factory and queue
		// QueueConnectionFactory connectionFactory = (QueueConnectionFactory)
		// namingContext.lookup(CONNECTION_FACTORY);
		// Destination destination = (Destination)
		// namingContext.lookup(QUEUE_DESTINATION);

		// Create a JMS context to use to create producers
	}

}
