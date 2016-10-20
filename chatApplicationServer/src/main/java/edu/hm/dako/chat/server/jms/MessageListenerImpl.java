package edu.hm.dako.chat.server.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.ejb.ActivationConfigProperty;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.server.process.ProcessChatPDU;

@MessageDriven( // Message-driven bean (MDB)
activationConfig = {
	@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
	@ActivationConfigProperty(propertyName = "destination", propertyValue = "/queue/JarfillerQueue" // destination's JNDI name											// name
) })
public class MessageListenerImpl implements MessageListener {

	@Inject
	ProcessChatPDU processChatPDU;
	
	public void onMessage(Message message) {
		
		try {
			ChatPDU chatPDU = message.getBody(ChatPDU.class);
			processChatPDU.process(chatPDU);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}