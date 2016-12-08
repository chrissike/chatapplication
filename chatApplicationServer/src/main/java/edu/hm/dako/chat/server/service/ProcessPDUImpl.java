package edu.hm.dako.chat.server.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.server.service.SystemResourceCalculator;
import edu.hm.dako.chat.model.BenchmarkPDU;
import edu.hm.dako.chat.model.ChatPDU;
import edu.hm.dako.chat.model.PDU;
import edu.hm.dako.chat.model.PduType;
import edu.hm.dako.chat.server.datasink.DataSink;
import edu.hm.dako.chat.server.datasink.model.CountEntity;
import edu.hm.dako.chat.server.datasink.model.TraceEntity;
import edu.hm.dako.chat.server.jms.JmsProducer;
import edu.hm.dako.chat.server.user.ClientListEntry;
import edu.hm.dako.chat.server.user.SharedChatClientList;

@Stateless
public class ProcessPDUImpl implements ProcessPDU {

	private static Log log = LogFactory.getLog(ProcessPDUImpl.class);

	@Inject
	private DataSink dataSink;

	@Inject
	private JmsProducer publisher;

	private SharedChatClientList clientList = SharedChatClientList.getInstance();

	@Transactional
	public void processMessage(PDU pdu) throws Exception {
		log.debug("JMS-Nachricht ist angekommen: " + pdu.toString());

		pdu.setServerTime(Long.valueOf(System.nanoTime()));
		pdu.setServerThreadName(Thread.currentThread().getName());
		pdu.setPduType(PduType.MESSAGE);

		persistChatData(pdu);

		if (pdu instanceof BenchmarkPDU) {
			pdu = fillBenchmarkPDU(pdu);
		}

		sendPDU(pdu);
	}

	private PDU fillBenchmarkPDU(PDU pdu) {
		BenchmarkPDU bPDU = (BenchmarkPDU) pdu;
		SystemResourceCalculator sysResource = new SystemResourceCalculator();
		bPDU.setFreeMemory(sysResource.getFreeMemory().doubleValue());
		bPDU.setUsedMemory(sysResource.getUsedMemory().doubleValue());

		return bPDU;
	}

	public boolean processClientListChange(ChatPDU pdu, long startTime) {
		log.debug("JMS-Login/Logout-Request ist mit der folgenden Userliste angekommen: " + pdu.getClients());
		pdu.setServerTime(Long.valueOf(System.nanoTime()));
		boolean success = updateServersideClientList(pdu);

		if (success) {
			pdu.setClients(SharedChatClientList.getInstance().getClientNameList());
			pdu.setServerThreadName(Thread.currentThread().getName());
			sendPDU(pdu);
		}

		return success;
	}

	public boolean updateServersideClientList(ChatPDU pdu) {
		log.debug("updateServersideClientList() mit PDU-Username aufgerufen: " + pdu.getUserName());
		boolean success = false;

		if (pdu.getPduType().equals(PduType.LOGIN)) {
			if (!clientList.existsClient(pdu.getUserName())) {
				clientList.createClient(pdu.getUserName(), new ClientListEntry(pdu.getUserName()));
				success = true;
			}
		}
		if (pdu.getPduType().equals(PduType.LOGOUT)) {
			if (clientList.existsClient(pdu.getUserName())) {
				clientList.deleteClient(pdu.getUserName());
				success = true;
			}
		}

		log.debug("clientList: " + clientList.getClientNameList());
		return success;
	}

	private void sendPDU(PDU pdu) {
		Long serverTime = Long.valueOf((System.nanoTime())) - pdu.getServerTime();
		log.debug("pdu servertime: " + serverTime);

		pdu.setServerTime(serverTime);
		try {
			publisher.sendMessage(pdu);
		} catch (NamingException e) {
			log.error(e);
		}
	}

	private void persistChatData(PDU pdu) throws Exception {
		TraceEntity trace = new TraceEntity(pdu.getUserName(), pdu.getServerThreadName(), pdu.getMessage());
		CountEntity count = new CountEntity(pdu.getUserName(), 1);

		dataSink.persistTrace(trace);
		dataSink.createOrUpdateCount(count);
	}

	public ChatPDU createPDU(String username, PduType requestType) {
		ChatPDU requestPdu = new ChatPDU(requestType);
		Thread.currentThread().setName("Client-" + username);
		requestPdu.setServerThreadName(Thread.currentThread().getName());
		requestPdu.setUserName(username);
		requestPdu.setClients(clientList.getClientNameList());

		return requestPdu;
	}
	
	public void clearSharedClientList() {
		clientList.deleteAllClients();
	}
}
