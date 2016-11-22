package edu.hm.dako.chat.client.benchmarking;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.client.communication.jms.JmsConsumer;
import edu.hm.dako.chat.client.communication.jms.JmsProducer;
import edu.hm.dako.chat.common.ChatPDU;

public class ProcessBenchmarking {

	private static Log log = LogFactory.getLog(ProcessBenchmarking.class);

	private static String message = null;

	public ProcessBenchmarking(String message) {
		ProcessBenchmarking.setMessage(message);
	}

	public void startNewBenchmarkingClient(String name) {

		Thread thread = new Thread() {
			public void run() {
				log.info("Neuer Client gestartet.");

				ChatPDU chatPdu = new ChatPDU();
				chatPdu.setUserName(name);
				chatPdu.setServerThreadName(Thread.currentThread().getName());
				chatPdu.setMessage(ProcessBenchmarking.getMessage());
				chatPdu.setClientStartTime(System.nanoTime());

				JmsProducer jms = new JmsProducer();
				try {
					jms.sendMessage(chatPdu);
					log.info("Nachricht an Queue gesendet.");
				} catch (NamingException e) {
					log.error(e.getMessage() + ", " + e.getCause());
				} catch (JMSException e) {
					log.error(e.getMessage() + ", " + e.getCause());
				}
			}
		};

		thread.start();

	}

	public static String getMessage() {
		return message;
	}

	public static void setMessage(String message) {
		ProcessBenchmarking.message = message;
	}

}
