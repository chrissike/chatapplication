package edu.hm.dako.chat.client.communication.jms;

import static org.junit.Assert.assertTrue;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import edu.hm.dako.chat.jms.connect.JmsChatContext;
import edu.hm.dako.chat.jms.connect.JmsConsumer;
import edu.hm.dako.chat.jms.connect.JmsProducer;
import edu.hm.dako.chat.model.ChatPDU;
import edu.hm.dako.chat.model.PDU;
import edu.hm.dako.chat.model.PduType;

public class JmsProducerTest {

	private static Log log = LogFactory.getLog(JmsProducerTest.class);

	private static final String USERNAME = "Hans Wurst";
	private static final String MESSAGE = "Habe Hunger";
	
	private JmsProducer<ChatPDU> jmsProducer;
	private JmsConsumer jmsConsumer;

	private ChatPDU chatPdu;
	
	private Boolean successFlag;

	@Before
	public void prepareTest() {
		successFlag = false;
		jmsProducer = new JmsProducer<ChatPDU>();
		jmsConsumer = new JmsConsumer();

		chatPdu = new ChatPDU(USERNAME, MESSAGE, PduType.MESSAGE);
	}

	@Test
	public void testJms() {
		Boolean success = false;
		createReceiver();
		try {
			success = jmsProducer.sendMessage(chatPdu, new JmsChatContext());
		} catch (NamingException e) {
			log.error(e.getMessage());
		} catch (JMSException e) {
			log.error(e.getMessage());
		}
		assertTrue(success);
		try {
			Thread.sleep(9000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(successFlag);
	}

	private void createReceiver() {
		Thread thread = new Thread() {
			public void run() {
				try {
					PDU pdu = jmsConsumer.initJmsConsumer(null, new JmsChatContext()).getBody(PDU.class);
					if (pdu.getUserName().equals(USERNAME) && pdu.getMessage().equals(MESSAGE)) {
						successFlag = true;
					}
				} catch (NamingException e) {
					e.printStackTrace();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		};

		thread.start();
	}
}
