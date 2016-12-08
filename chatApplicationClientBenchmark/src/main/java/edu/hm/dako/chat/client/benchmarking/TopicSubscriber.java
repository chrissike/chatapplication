package edu.hm.dako.chat.client.benchmarking;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * JMS Nachrichtenempfänger
 *
 */
public class TopicSubscriber implements MessageListener {

	private static Log log = LogFactory.getLog(TopicSubscriber.class);
	
	public void onMessage(Message message) {
		try {
			Runnable r = new ReceiverThread(message);
			new Thread(r).start();
		}catch(Exception e) {
			log.debug("Nachricht empfangen, die keiner der Benchmark-Nachrichten entspricht.");
		}

	}
}