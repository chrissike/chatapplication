package edu.hm.dako.chat.server.jms;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.model.PDU;
import edu.hm.dako.chat.server.service.ProcessPDU;
import edu.hm.dako.chat.server.user.SharedChatClientList;

@MessageDriven(name = "JmsConsumer", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/queue/chatreq2"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "minSessions", propertyValue = "50"),
        @ActivationConfigProperty(propertyName = "maxSessions", propertyValue = "250")})
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JmsConsumer implements MessageListener {

	private static Log log = LogFactory.getLog(JmsConsumer.class);
	
	@Inject
	ProcessPDU process;

	@Resource
	private MessageDrivenContext mdc;

	Integer rollbackCount = 0;

	public void onMessage(Message message) {
		log.debug(">>>> onMessage()-Methode gestartet" + message.toString());
		PDU pdu = null;

		try {
			pdu = message.getBody(PDU.class);
			process.processMessage(pdu);
		} catch (Throwable e) {
			rollbackCount++;
			log.error(e.getMessage()
					+ ">>>>>>> Exception ist in der XA-Transaktion geflogen und wird nun als Rollback gekennzeichnet. User: "
					+ pdu.getUserName() + ", Count: " + rollbackCount);
			mdc.setRollbackOnly();
		}

	}
}