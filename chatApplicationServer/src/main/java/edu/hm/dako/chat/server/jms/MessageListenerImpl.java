package edu.hm.dako.chat.server.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import edu.hm.dako.chat.common.ChatPDU;

public class MessageListenerImpl implements MessageListener {

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			ChatPDU chatPDU = message.getBody(ChatPDU.class);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
