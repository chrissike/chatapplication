package edu.hm.dako.chat.server.jms;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.JMSException;
import javax.jms.JMSPasswordCredential;
import javax.jms.JMSProducer;
import javax.jms.Topic;
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
@JMSDestinationDefinitions({
	@JMSDestinationDefinition(name = "java:global/jms/topic/chatresp2", interfaceName = "javax.jms.Topic") 
})
@Stateless
public class JmsPublisher2 {

	private static final Log log = LogFactory.getLog(JmsPublisher2.class.getName());

	@Resource
	private SessionContext sc;

	@Resource(name = "ChatResponseTopic2") //lookup = "jms/topic/chatresp2") // "java:module/jms/topic/chatresp2")
	private Topic topic;

	@Inject
	@JMSConnectionFactory("java:jboss/exported/jms/RemoteConnectionFactory")
	@JMSPasswordCredential(userName = "guest", password = "guest")
	private JMSContext context;

	// private static final String PROVIDER_URL =
	// "http-remoting://127.0.0.1:8089";

	public void sendMessage(ChatPDU pdu) throws NamingException, JMSException {
		log.info("sendMessage() gestartet");

		JMSProducer producer = context.createProducer();
		producer.send(topic, pdu);

		log.info("sendMessage() beendet");
	}
}