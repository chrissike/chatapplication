package edu.hm.dako.chat.client.communication.jms;

import static org.junit.Assert.assertTrue;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import edu.hm.dako.chat.client.communication.SimpleMessageListenerThreadImpl;
import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.common.PduType;

public class JmsProducerTest {

	private static Log log = LogFactory.getLog(JmsProducerTest.class);
	
	JmsProducer jmsProducer;
	
	ChatPDU chatPdu;
	
	@Before
	public void prepareTest(){
		
		jmsProducer = new JmsProducer();
		
		chatPdu = new ChatPDU();
		chatPdu.setMessage("Testnachricht");
		chatPdu.setUserName("Hans Wurst");
		chatPdu.setPduType(PduType.CHAT_MESSAGE_REQUEST);
		
	}
	
	
	@Test
	public void testSendMessage(){
		Boolean success = false;
		try {
			success = jmsProducer.sendMessage(chatPdu);
		} catch (NamingException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (JMSException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		assertTrue(success);
	}
}
