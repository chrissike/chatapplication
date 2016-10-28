package edu.hm.dako.chat.server.process;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.server.datasink.DataSink;
import edu.hm.dako.chat.server.datasink.model.CountEntity;
import edu.hm.dako.chat.server.datasink.model.TraceEntity;
import edu.hm.dako.chat.server.jms.JmsProducer2;

public class ProcessChatPDU {

	private static Log log = LogFactory.getLog(ProcessChatPDU.class);

	@Inject
	DataSink dataSink;

	public void process(ChatPDU pdu) {
		log.info("JMS-Nachricht ist angekommen: " + pdu.toString());

		// ***** START Serverarbeitung *****
		pdu.setServerTime(System.nanoTime());

		persistChatData(pdu);

		pdu.setServerTime((System.nanoTime() - pdu.getServerTime()));
		// ***** END Serverarbeitung *****

		log.info("Die Verarbeitungszeit für die Nachricht beträgt: " + pdu.getServerTime() + " ms");
		try {
			new JmsProducer2().sendMessage(pdu);
		} catch (NamingException e) {
			log.error(e);
		} catch (JMSException e) {
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
