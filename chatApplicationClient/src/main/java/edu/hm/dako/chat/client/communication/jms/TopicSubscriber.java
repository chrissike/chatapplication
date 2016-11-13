package edu.hm.dako.chat.client.communication.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import edu.hm.dako.chat.common.ChatPDU;

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
		}
	}
}