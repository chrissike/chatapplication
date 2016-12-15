package edu.hm.dako.chat.client.communication.jms;

import static org.junit.Assert.assertTrue;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.hm.dako.chat.jms.connect.JmsChatContext;
import edu.hm.dako.chat.jms.connect.JmsConsumer;
import edu.hm.dako.chat.jms.connect.JmsProducer;
import edu.hm.dako.chat.model.ChatPDU;
import edu.hm.dako.chat.model.PDU;
import edu.hm.dako.chat.model.PduType;

public class JmsProducerTest {

	private static final Logger log = LoggerFactory.getLogger(JmsProducerTest.class);

	private static final String USERNAME = "Hans Wurst";
	private static final String MESSAGE = "Habe Hunger";
	
	private JmsProducer<ChatPDU> jmsProducer;
	private JmsConsumer jmsConsumer;

	private final ObjectMapper mapper = new ObjectMapper();

	private PDU pdu;
	
	private Boolean successFlag;

	@Before
	public void prepareTest() {
		successFlag = false;
		jmsProducer = new JmsProducer<ChatPDU>();
		jmsConsumer = new JmsConsumer();

		pdu = new ChatPDU(USERNAME, MESSAGE, PduType.MESSAGE);
	}

	@Test
	public void testJms() {
		Boolean success = false;
		createReceiver();
		try {
			String msg = mapper.writeValueAsString(pdu);
			success = jmsProducer.sendMessage(msg, new JmsChatContext());
		} catch (NamingException e) {
			log.error(e.getMessage());
		} catch (JMSException e) {
			log.error(e.getMessage());
		} catch (JsonProcessingException e) {
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
