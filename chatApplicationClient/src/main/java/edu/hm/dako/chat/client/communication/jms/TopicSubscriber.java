package edu.hm.dako.chat.client.communication.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import edu.hm.dako.chat.client.ui.ClientFxGUI;
import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.common.PduType;

/**
 * JMS Nachrichtenempfänger
 *
 */
public class TopicSubscriber implements MessageListener {

	public void onMessage(Message message) {
		System.out.println("+++++++++++++++++++>>>> Nachricht erhalten: " + message.toString());

		ChatPDU chatPDU = null;
		try {
			chatPDU = message.getBody(ChatPDU.class);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		if (chatPDU != null) {
			System.out.println("Topicnachricht erhalten: " + chatPDU.toString());

			switch (chatPDU.getPduType()) {
				case CHAT_MESSAGE_EVENT:
					ClientFxGUI.instance.setMessageLine(chatPDU.getUserName(), chatPDU.getMessage());
					break;
				case LOGIN_EVENT:
					ClientFxGUI.instance.setUserList(chatPDU.getClients());
					break;
				case LOGOUT_EVENT:
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