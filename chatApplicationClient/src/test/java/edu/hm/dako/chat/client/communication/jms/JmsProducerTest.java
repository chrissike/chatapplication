package edu.hm.dako.chat.client.communication.jms;

import static org.junit.Assert.assertTrue;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import edu.hm.dako.chat.jms.connect.JmsConsumer;
import edu.hm.dako.chat.jms.connect.JmsProducer;
import edu.hm.dako.chat.model.ChatPDU;
import edu.hm.dako.chat.model.PduType;

public class JmsProducerTest {

	private static Log log = LogFactory.getLog(JmsProducerTest.class);

	JmsProducer<ChatPDU> jmsProducer;
	JmsConsumer jmsConsumer;

	ChatPDU chatPdu;

	@Before
	public void prepareTest() {

		jmsProducer = new JmsProducer<ChatPDU>();
		jmsConsumer = new JmsConsumer();

		chatPdu = new ChatPDU("Hans Wurst", "Testnachricht", PduType.MESSAGE);
	}

	@Test
	public void testJms() {
		Boolean success = false;
		try {
			jmsConsumer.initJmsConsumer(new TopicSubscriber());
			success = jmsProducer.sendMessage(chatPdu);
		} catch (NamingException e) {
			log.error(e.getMessage());
		} catch (JMSException e) {
			log.error(e.getMessage());
		}
		assertTrue(success);
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
