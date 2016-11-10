package edu.hm.dako.chat.server.jms;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.server.process.ProcessChatPDU;

// http://docs.jboss.org/jbossmessaging/docs/usermanual-2.0.0.beta4/html/appserver-integration.html

@MessageDriven(name = "JmsConsumer", activationConfig = {
			@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
			@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/chatreq2"), // destination's JNDI name
			@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
		})
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
public class JmsConsumer implements MessageListener {

	private static Log log = LogFactory.getLog(JmsConsumer.class);

	@Inject
	ProcessChatPDU processChatPDU;

	@Resource
	private MessageDrivenContext mdc;

	public void onMessage(Message message) {
		System.out.println(">>>> Nachricht erhalten: " + message.toString());
		log.info(">>>> onMessage()-Methode gestartet" + message.toString());

		try {
			ChatPDU chatPDU = message.getBody(ChatPDU.class);
			processChatPDU.process(chatPDU);
		} catch (JMSException e) {
			log.error(e.toString());
			mdc.setRollbackOnly();
		}

	}

//	@Override
//	public void setMessageDrivenContext(MessageDrivenContext ctx) throws EJBException {
//		log.info("setMessageDrivenContext() wird aufgerufen!!!"); 
//	}
//
//	@Override
//	public void ejbRemove() throws EJBException {
//		log.info("ejbRemove() wird aufgerufen!!!"); 
//	}
}