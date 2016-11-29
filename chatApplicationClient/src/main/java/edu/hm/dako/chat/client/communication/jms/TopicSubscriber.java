package edu.hm.dako.chat.client.communication.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import edu.hm.dako.chat.client.ui.ClientFxGUI;
import edu.hm.dako.chat.model.ChatPDU;

/**
 * JMS Nachrichtenempfänger
 *
 */
public class TopicSubscriber implements MessageListener {

	public void onMessage(Message message) {
		ChatPDU chatPDU = null;
		try {
			chatPDU = message.getBody(ChatPDU.class);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
		if (chatPDU != null && ClientFxGUI.instance != null) {
			switch (chatPDU.getPduType()) {
			case MESSAGE:
				ClientFxGUI.instance.setMessageLine(chatPDU.getUserName(), chatPDU.getMessage());
				break;
			case LOGIN:
				ClientFxGUI.instance.setUserList(chatPDU.getClients());
				break;
			case LOGOUT:
				ClientFxGUI.instance.setUserList(chatPDU.getClients());
				break;
			case UNDEFINED:
				// TODO was tun??
				break;
			default:
				// TODO was tun??
				break;
			}
		}
	}
}