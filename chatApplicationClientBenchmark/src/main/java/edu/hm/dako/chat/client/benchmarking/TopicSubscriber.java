package edu.hm.dako.chat.client.benchmarking;

import javax.jms.Message;
import javax.jms.MessageListener;


/**
 * JMS Nachrichtenempfänger
 *
 */
public class TopicSubscriber implements MessageListener {
	
	public void onMessage(Message message) {
		Runnable r = new ReceiverThread(message);
		new Thread(r).start();
	}
}