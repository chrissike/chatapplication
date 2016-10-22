package edu.hm.dako.chat.server.jms;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.ChatPDU;

public class JmsProducer {

	private static Log log = LogFactory.getLog(JmsProducer.class);

	@Resource
	ConnectionFactory connectionFactory;

	@Resource(mappedName = "topic/chatresp2")
	Topic replyTopic;

	public void spreadMessage(ChatPDU chatPdu) {
		log.info("Sendevorgang zum Topic gestartet!!!");
		Connection conn = null;
		try {
			conn = connectionFactory.createConnection();
			Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = sess.createProducer(replyTopic);
			log.info("MessageProducer: " +producer.toString());
			log.info("Session: " +sess.toString());
			producer.send(sess.createObjectMessage(chatPdu));

		} catch (Exception e) {
			log.error(e.getMessage());
			log.error(e.getStackTrace());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (JMSException e) {
				}
			}
		}
	}
}