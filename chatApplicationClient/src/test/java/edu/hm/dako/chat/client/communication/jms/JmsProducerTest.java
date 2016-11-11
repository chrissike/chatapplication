package edu.hm.dako.chat.client.communication.jms;

import static org.junit.Assert.assertTrue;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.common.PduType;

public class JmsProducerTest {

	private static Log log = LogFactory.getLog(JmsProducerTest.class);

	JmsProducer jmsProducer;
	JmsConsumer jmsConsumer;

	ChatPDU chatPdu;

	@Before
	public void prepareTest() {

		jmsProducer = new JmsProducer();
		jmsConsumer = new JmsConsumer();

		chatPdu = new ChatPDU();
		chatPdu.setMessage("Testnachricht");
		chatPdu.setUserName("Hans Wurst");
		chatPdu.setPduType(PduType.CHAT_MESSAGE_REQUEST);
		chatPdu.setClientThreadName(Thread.currentThread().getName());

		Thread one = new Thread() {
			public void run() {
				System.out.println("JMS Consumer wird initialisiert!");
				try {
					jmsConsumer.initJmsConsumer();
				} catch (NamingException e) {
					e.printStackTrace();
				}
			}
		};

		one.start();
	}

	@Test
	public void testJms() {
		// for (int i = 0; i < 10; i++) {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Boolean success = false;
		try {
			System.out.println("JMS Producer wird initialisiert!");
			success = jmsProducer.sendMessage(chatPdu);
		} catch (NamingException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (JMSException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		assertTrue(success);
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
