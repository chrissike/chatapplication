package edu.hm.dako.chat.client.communication.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJBException;
import javax.ejb.TransactionManagementType;
import javax.ejb.TransactionAttributeType;

import edu.hm.dako.chat.common.ChatPDU;


/**
 * JMS Nachrichtenempfänger
 *
 */
@MessageDriven(name = "JmsConsumer", activationConfig = {
			@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
			@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/chatresp2"), // destination's JNDI name
			@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
		})
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
public class JmsConsumer implements MessageListener { // MessageDrivenBean

	private static Log log = LogFactory.getLog(JmsConsumer.class);

	@Resource
	private MessageDrivenContext mdc;

	public void onMessage(Message message) {
		System.out.println(">>>> Nachricht erhalten: " + message.toString());
		log.info(">>>> onMessage()-Methode gestartet" + message.toString());

		try {
			ChatPDU chatPDU = message.getBody(ChatPDU.class);
			System.out.println("Topicnachricht erhalten: " + chatPDU.toString());
		} catch (JMSException e) {
			log.error(e.toString());
			mdc.setRollbackOnly();
		}

	}

//	@Override
//	public void setMessageDrivenContext(MessageDrivenContext ctx) throws EJBException {
//		log.info("setMessageDrivenContext() wird aufgerufen!!!"); 
//		
//	}
//
//	@Override
//	public void ejbRemove() throws EJBException {
//		log.info("ejbRemove() wird aufgerufen!!!"); 
//		
//	}
}