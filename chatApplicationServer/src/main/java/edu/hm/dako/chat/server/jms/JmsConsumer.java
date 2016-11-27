package edu.hm.dako.chat.server.jms;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.model.ChatPDU;
import edu.hm.dako.chat.server.process.ProcessChatPDU;

// http://docs.jboss.org/jbossmessaging/docs/usermanual-2.0.0.beta4/html/appserver-integration.html

@MessageDriven(name = "JmsConsumer", activationConfig = {
			@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
			@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/queue/chatreq2"),
			@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
		})
public class JmsConsumer implements MessageListener {

	private static Log log = LogFactory.getLog(JmsConsumer.class);

	@Inject
	ProcessChatPDU process;

	@Resource
	private MessageDrivenContext mdc;

	public void onMessage(Message message) {
		log.info(">>>> onMessage()-Methode gestartet" + message.toString());

		try {
			ChatPDU chatPDU = message.getBody(ChatPDU.class);
			process.processMessage(chatPDU);
		} catch (JMSException e) {
			log.error(e.toString());
			mdc.setRollbackOnly();
		}

	}
}