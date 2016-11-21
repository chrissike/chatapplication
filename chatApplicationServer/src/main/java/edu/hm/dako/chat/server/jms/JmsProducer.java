package edu.hm.dako.chat.server.jms;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Topic;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.ChatPDU;

public class JmsProducer {

	private static Log log = LogFactory.getLog(JmsProducer.class);
	
	@JMSConnectionFactory("java:jboss/exported/jms/RemoteConnectionFactory")
	private ConnectionFactory confac;

	@Inject
	private JMSContext context;

	@Resource(mappedName = "java:/jms/topic/chatresp2")
	private Topic topic;
	
	public void sendMessage(ChatPDU pdu) throws NamingException {
		log.info("Send ChatPDU: " + pdu.toString());
		JMSProducer producer = context.createProducer();
		producer.send(topic, pdu);
	}
}