package edu.hm.dako.chat.client.benchmarking;

import javax.jms.Message;
import javax.jms.MessageListener;


/**
 * JMS Nachrichtenempfänger
 *
 */
public class TopicSubscriber implements MessageListener {

	private Integer messageCount = 0;
	
	public void onMessage(Message message) {
		Runnable r = new ReceiverThread(message, messageCount);
		new Thread(r).start();
		messageCount++;		
	}
}