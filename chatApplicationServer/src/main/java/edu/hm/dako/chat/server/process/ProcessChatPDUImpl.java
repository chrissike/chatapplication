package edu.hm.dako.chat.server.process;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.common.PduType;
import edu.hm.dako.chat.server.datasink.DataSink;
import edu.hm.dako.chat.server.datasink.model.CountEntity;
import edu.hm.dako.chat.server.datasink.model.TraceEntity;
import edu.hm.dako.chat.server.jms.JmsProducer;
import edu.hm.dako.chat.server.user.SharedChatClientList;

@Stateless
public class ProcessChatPDUImpl implements ProcessChatPDU {

	private static Log log = LogFactory.getLog(ProcessChatPDUImpl.class);

	@Inject
	private DataSink dataSink;
	
	@Inject
	private JmsProducer publisher;
	
	
	public void processMessage(ChatPDU pdu) {
		log.info("JMS-Nachricht ist angekommen: " + pdu.toString());

		pdu.setServerTime(System.nanoTime());
		pdu.setServerThreadName(Thread.currentThread().getName());
		pdu.setPduType(PduType.CHAT_MESSAGE_EVENT);
		
//		persistChatData(pdu);
		
		sendPDU(pdu);
	}
	
	public void processClientListChange(SharedChatClientList clientList, long startTime) {
		log.info("JMS-Nachricht ist angekommen: " + clientList.printClientList());
		
		ChatPDU pdu = new ChatPDU();
		pdu.setPduType(PduType.LOGIN_EVENT);
		pdu.setClients(clientList.getClientNameList());
		pdu.setServerThreadName(Thread.currentThread().getName());
		
		sendPDU(pdu);
	}
	
	private void sendPDU(ChatPDU pdu) {
		pdu.setServerTime((System.nanoTime() - pdu.getServerTime()));
		log.info("Die Verarbeitungszeit für die Nachricht beträgt: " + pdu.getServerTime() + " ms");
		try {
			publisher.sendMessage(pdu);
		} catch (NamingException e) {
			log.error(e);
		}
	}

	private void persistChatData(ChatPDU pdu) {
		TraceEntity trace = new TraceEntity(pdu.getClientThreadName(), pdu.getServerThreadName(), pdu.getMessage());
		CountEntity count = new CountEntity(pdu.getClientThreadName(), 1);

		dataSink.persistTrace(trace);
		dataSink.createOrUpdateCount(count);
	}
}
