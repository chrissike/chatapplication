package edu.hm.dako.chat.server.process;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.server.jms.JmsProducer;
import edu.hm.dako.chat.server.jms.JmsProducer2;

@Transactional
public class ProcessChatPDU {

	private static Log log = LogFactory.getLog(ProcessChatPDU.class);
	
	public void process(ChatPDU pdu) {
		log.info("JMS-Nachricht ist angekommen: " + pdu.toString());
		pdu.setMessage("Hallo hier ist der Server!");
//		new JmsProducer().spreadMessage(pdu);
		try {
			new JmsProducer2().sendMessage(pdu);
		} catch (NamingException e) {
			log.error(e);
		} catch (JMSException e) {
			log.error(e);
		}
	}
	
}
