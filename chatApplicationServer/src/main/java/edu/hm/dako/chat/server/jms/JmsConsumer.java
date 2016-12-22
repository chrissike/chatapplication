package edu.hm.dako.chat.server.jms;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.hm.dako.chat.model.BenchmarkPDU;
import edu.hm.dako.chat.model.PDU;
import edu.hm.dako.chat.server.service.ProcessPDU;

@MessageDriven(name = "JmsConsumer", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/queue/chatreq2"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "DUPS_OK_ACKNOWLEDGE")
})
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JmsConsumer implements MessageListener {

	private static Log log = LogFactory.getLog(JmsConsumer.class);

	@Inject
	ProcessPDU process;

	@Resource
	private MessageDrivenContext mdc;

	private final ObjectMapper mapper = new ObjectMapper();

	Integer rollbackCount = 0;

	public void onMessage(Message message) {
		log.debug(">>>> onMessage()-Methode gestartet" + message.toString());
		PDU pdu = null;

		try {
			if (message instanceof TextMessage) {
				TextMessage txtMsg = (TextMessage) message;
				pdu = mapper.readValue(txtMsg.getText(), BenchmarkPDU.class);
			} else {
				pdu = message.getBody(PDU.class);
			}

			process.processMessage(pdu);
		} catch (JsonParseException | JsonMappingException e) {
			try {
				log.error(e.getMessage() + message.getBody(String.class));
			} catch (JMSException e1) {
				log.error(e.getMessage());
			}
		} catch (Throwable e) {
			rollbackCount++;
			log.error(">>>>>>> Exception ist in der XA-Transaktion geflogen und wird nun als Rollback gekennzeichnet, Count: "
					+ rollbackCount);
			mdc.setRollbackOnly();
		}

	}
}