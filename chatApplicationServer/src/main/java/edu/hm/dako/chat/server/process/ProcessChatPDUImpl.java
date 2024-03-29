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
import edu.hm.dako.chat.server.user.ClientListEntry;
import edu.hm.dako.chat.server.user.SharedChatClientList;

@Stateless
public class ProcessChatPDUImpl implements ProcessChatPDU {

	private static Log log = LogFactory.getLog(ProcessChatPDUImpl.class);

	@Inject
	private DataSink dataSink;

	@Inject
	private JmsProducer publisher;
	
	private SharedChatClientList clientList = SharedChatClientList.getInstance();

	public void processMessage(ChatPDU pdu) {
		log.info("JMS-Nachricht ist angekommen: " + pdu.toString());

		pdu.setServerTime(Long.valueOf(System.nanoTime()));
		pdu.setServerThreadName(Thread.currentThread().getName());
		pdu.setPduType(PduType.CHAT_MESSAGE_EVENT);

//		TODO: Datenbank einbinden
		persistChatData(pdu);

		sendPDU(pdu);
	}

	public boolean processClientListChange(ChatPDU pdu, long startTime) {
		log.info("JMS-Login/Logout-Request ist mit der folgenden Userliste angekommen: " + pdu.getClients());
		pdu.setServerTime(Long.valueOf(System.nanoTime()));
		boolean success = updateServersideClientList(pdu);

		if(success) {
			pdu.setClients(SharedChatClientList.getInstance().getClientNameList());
			pdu.setServerThreadName(Thread.currentThread().getName());			
			sendPDU(pdu);
		}

		return success;
	}

	
	public boolean updateServersideClientList(ChatPDU pdu) {
		log.info("updateServersideClientList() mit PDU-Username aufgerufen: " + pdu.getUserName());
		boolean success = false;
		
		if (pdu.getPduType().equals(PduType.LOGIN_EVENT)) {
			if(!clientList.existsClient(pdu.getUserName())) {
				clientList.createClient(pdu.getUserName(), new ClientListEntry(pdu.getUserName()));
				success = true;
			}
		}
		if (pdu.getPduType().equals(PduType.LOGOUT_EVENT)) {
			if (clientList.existsClient(pdu.getUserName())) {
				clientList.deleteClientWithoutCondition(pdu.getUserName());
				success = true;
			}
		}
		return success;
	}

	private void sendPDU(ChatPDU pdu) {
		log.info("pdu servertime: " + pdu.getServerTime() + ", nowtime: " + Long.valueOf((System.nanoTime())));
		Long serverTime = Long.valueOf((System.nanoTime())) - pdu.getServerTime();
		pdu.setServerTime(serverTime);
		log.info("Die Verarbeitungszeit für die Nachricht beträgt: " + pdu.getServerTime() + " ms");
		try {
			publisher.sendMessage(pdu);
		} catch (NamingException e) {
			log.error(e);
		}
	}

	private void persistChatData(ChatPDU pdu) {
		TraceEntity trace = new TraceEntity(pdu.getUserName(), pdu.getServerThreadName(), pdu.getMessage());
		CountEntity count = new CountEntity(pdu.getUserName(), 1);

		dataSink.persistTrace(trace);
		dataSink.createOrUpdateCount(count);
	}
	

	public ChatPDU createPDU(String username, PduType requestType) {
		ChatPDU requestPdu = new ChatPDU();
		requestPdu.setPduType(requestType);
		Thread.currentThread().setName("Client-" + username);
		requestPdu.setServerThreadName(Thread.currentThread().getName());
		requestPdu.setUserName(username);
		requestPdu.setClients(clientList.getClientNameList());
		
		return requestPdu;
	}
}
