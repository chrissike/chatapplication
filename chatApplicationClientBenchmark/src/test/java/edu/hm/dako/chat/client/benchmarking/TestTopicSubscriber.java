package edu.hm.dako.chat.client.benchmarking;

import javax.jms.Message;
import javax.jms.MessageListener;


/**
 * JMS Nachrichtenempfänger
 *
 */
public class TestTopicSubscriber implements MessageListener {

	private Integer messageCount = 0;
	
	public void onMessage(Message message) {
		messageCount++;		
	}

	public Integer getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(Integer messageCount) {
		this.messageCount = messageCount;
	}
	
}