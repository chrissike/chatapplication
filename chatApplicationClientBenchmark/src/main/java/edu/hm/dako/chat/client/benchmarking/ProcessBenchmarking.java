package edu.hm.dako.chat.client.benchmarking;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.client.communication.jms.JmsProducer;
import edu.hm.dako.chat.model.BenchmarkPDU;
import edu.hm.dako.chat.model.ChatPDU;

public class ProcessBenchmarking {

	private static Log log = LogFactory.getLog(ProcessBenchmarking.class);

	private static String message = null;

	final CyclicBarrier startingGate;

	public ProcessBenchmarking(String message, Integer clientCount) {
		ProcessBenchmarking.setMessage(message);
		startingGate = new CyclicBarrier(clientCount);
	}

	public void createNewBenchmarkingClient(String name) {

		Thread thread = new Thread() {
			public void run() {
				log.info("Client: " + name + " wartet auf den Start!");
				try {
					startingGate.await();
				} catch (InterruptedException e) {
					log.error(e.getStackTrace());
				} catch (BrokenBarrierException e) {
					log.error(e.getStackTrace());
				}

				ChatPDU chatPdu = new BenchmarkPDU();
				chatPdu.setUserName(name);
				chatPdu.setServerThreadName(Thread.currentThread().getName());
				chatPdu.setMessage(ProcessBenchmarking.getMessage());
				chatPdu.setClientStartTime(System.nanoTime());

				JmsProducer jms = new JmsProducer();
				try {
					jms.sendMessage(chatPdu);
				} catch (NamingException e) {
					log.error(e.getMessage() + ", " + e.getCause());
				} catch (JMSException e) {
					log.error(e.getMessage() + ", " + e.getCause());
				}
			}
		};

		thread.start();

	}

	public void startAllClients() {
		try {
			startingGate.await();
		} catch (InterruptedException e) {
			log.error(e.getStackTrace());
		} catch (BrokenBarrierException e) {
			log.error(e.getStackTrace());
		}
		log.info("all threads started");
	}

	public static String getMessage() {
		return message;
	}

	public static void setMessage(String message) {
		ProcessBenchmarking.message = message;
	}
}
