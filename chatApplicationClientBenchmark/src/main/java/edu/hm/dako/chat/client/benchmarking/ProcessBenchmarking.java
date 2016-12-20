package edu.hm.dako.chat.client.benchmarking;

import java.net.URISyntaxException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.hm.dako.chat.client.ui.BenchmarkingClientFxGUI;
import edu.hm.dako.chat.jms.connect.JmsProducer;
import edu.hm.dako.chat.model.BenchmarkPDU;
import edu.hm.dako.chat.model.PDU;
import edu.hm.dako.chat.model.PduType;
import edu.hm.dako.chat.rest.MessagingHandler;
import edu.hm.dako.chat.rest.MessagingHandlerImpl;
import edu.hm.dako.chat.rest.TechnicalRestException;
import javafx.application.Platform;


@SuppressWarnings("restriction")
public class ProcessBenchmarking {

	private static Log log = LogFactory.getLog(ProcessBenchmarking.class);

	private static String message = null;
	
	private final ObjectMapper mapper = new ObjectMapper();

	final CyclicBarrier startingGate;

	public ProcessBenchmarking(String message, Integer clientCount) {
		ProcessBenchmarking.setMessage(message);
		startingGate = new CyclicBarrier(clientCount);
	}

	public void createNewBenchmarkingClient(String name, Integer messageCount) {

		Thread thread = new Thread() {
			public void run() {
				log.debug("Client: " + name + " wartet auf den Start!");

				// Client legt einen Eintrag in die SharedRTTClientList um bei
				// den eingehenden Nachrichten eine Zuordnung zu schaffen.
				BenchmarkingClientFxGUI.instance.getModel().addClientToSharedRTTClientList(name);

				try {
					startingGate.await();
				} catch (InterruptedException e) {
					log.error(e.getStackTrace());
				} catch (BrokenBarrierException e) {
					log.error(e.getStackTrace());
				}

				try {
					performLogin(name);
					performMessaging(name, messageCount);
					performLogout(name);
				} catch (TechnicalRestException e) {
					log.error(e.getMessage());
				} catch (URISyntaxException e) {
					log.error(e.getMessage());
				}

				startingGate.reset();
			}

			private void performMessaging(String name, Integer messageCount) {
				PDU pdu = createBenchmarkCPU(name);
				JmsProducer<PDU> jms = new JmsProducer<PDU>();
				for (int i = 1; i <= messageCount; i++) {
					sendBenchmarkCPU(pdu, jms);
				}
			}

			private void performLogin(String name) throws URISyntaxException {
				MessagingHandler handler;
				PDU loginPDU = createBenchmarkCPU(name);
				handler = new MessagingHandlerImpl(BenchmarkingClientFxGUI.getIp(), BenchmarkingClientFxGUI.getPort());
				handler.login(loginPDU.getUserName());
				Long endTime = System.nanoTime()-loginPDU.getClientStartTime();
				Platform.runLater(() -> {
					BenchmarkingClientFxGUI.instance.addEntryToGroupedClientRTTList(name, endTime);
				});
			}
			
			private void performLogout(String name) throws URISyntaxException {
				MessagingHandler handler;
				PDU loginPDU = createBenchmarkCPU(name);
				handler = new MessagingHandlerImpl(BenchmarkingClientFxGUI.getIp(), BenchmarkingClientFxGUI.getPort());
				handler.logout(loginPDU.getUserName());
				Long endTime = System.nanoTime()-loginPDU.getClientStartTime();
				Platform.runLater(() -> {
					BenchmarkingClientFxGUI.instance.addEntryToGroupedClientRTTList(name, endTime);
				});
			}

			private void sendBenchmarkCPU(PDU pdu, JmsProducer<PDU> jms) {
				int retryCounter = 0;
				boolean success = false;
				while (success == false && retryCounter <= 10) {
					try {
						String msg = mapper.writeValueAsString(pdu);
						success = jms.sendMessage(msg, BenchmarkingClientFxGUI.getJmsContext());
						retryCounter++;
					} catch (NamingException e) {
						log.error(e.getMessage() + ", " + e.getCause());
					} catch (JMSException e) {
						log.error(e.getMessage() + ", " + e.getCause());
					} catch (JsonProcessingException e) {
						log.error(e.getMessage() + ", " + e.getCause());
					}
				}
			}
		};

		thread.start();

	}

	public void performLogoutAll() throws TechnicalRestException, URISyntaxException {
		MessagingHandler handler;
		handler = new MessagingHandlerImpl(BenchmarkingClientFxGUI.getIp(), BenchmarkingClientFxGUI.getPort());
		handler.logoutAll();				
	}
	
	public PDU createBenchmarkCPU(String name) {
		PDU chatPdu = new BenchmarkPDU(PduType.MESSAGE);
		chatPdu.setUserName(name);
		chatPdu.setServerThreadName(Thread.currentThread().getName());
		chatPdu.setMessage(ProcessBenchmarking.getMessage());
		chatPdu.setClientStartTime(System.nanoTime());
		return chatPdu;
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
