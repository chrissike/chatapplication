package edu.hm.dako.chat.client.benchmarking;

import java.net.URISyntaxException;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.client.ui.BenchmarkingClientFxGUI;
import edu.hm.dako.chat.model.BenchmarkPDU;
import edu.hm.dako.chat.model.PDU;
import edu.hm.dako.chat.rest.MessagingHandler;
import edu.hm.dako.chat.rest.MessagingHandlerImpl;
import edu.hm.dako.chat.rest.TechnicalRestException;

public class ReceiverThread implements Runnable {

	private static Log log = LogFactory.getLog(ReceiverThread.class);
	
	private BenchmarkPDU bmPDU;
	private Long rtt;
	private Double rttMs;
	private Double rttServerMs;

	public ReceiverThread(Message message) throws ClassCastException {
		PDU pdu = null;
		try {
			pdu = message.getBody(PDU.class);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		if(pdu instanceof BenchmarkPDU) {
			bmPDU = (BenchmarkPDU) pdu;
		}else{
			throw new ClassCastException();
		}
	}

	public void run() {
		if (bmPDU != null && BenchmarkingClientFxGUI.instance != null) {
//TODO LOGOUT
//			if(letzte Nachricht von einem Client erhalten) {
				logoutClient(bmPDU.getUserName());				
//			}
			stopAndCalculateRTT();
			BenchmarkingClientFxGUI.instance.getModel().addRTTToSharedRTTClientList(bmPDU.getUserName(), getRtt());
			bmPDU.setAvgCPUUsage(BenchmarkingClientFxGUI.getSysResourceCalc().getAverageCpuUtilisation().doubleValue() * 100);
			bmPDU.setMessageNr(BenchmarkingClientFxGUI.getAndIncreaseClientNameReceivedCounter());
			BenchmarkingClientFxGUI.instance.showResults(bmPDU, getRtt(), getRttMs(), getRttServerMs());
		}
	}
	
	private void logoutClient(String username) {
		MessagingHandler handler = null;
		try {
			handler = new MessagingHandlerImpl("127.0.0.1", 8089);
			handler.logout(username);
		} catch (TechnicalRestException e) {
			log.error(e.getMessage());
		} catch (URISyntaxException e) {
			log.error(e.getMessage());
		}
	}

	private void stopAndCalculateRTT() {
		setRtt(System.nanoTime() - bmPDU.getClientStartTime());
		setRttMs(rtt.longValue() / 1000000000.0);
		setRttServerMs(bmPDU.getServerTime().longValue() / 1000000000.0);
	}

	public Long getRtt() {
		return rtt;
	}

	public void setRtt(Long rtt) {
		this.rtt = rtt;
	}

	public Double getRttMs() {
		return rttMs;
	}

	public void setRttMs(Double rttMs) {
		this.rttMs = rttMs;
	}

	public Double getRttServerMs() {
		return rttServerMs;
	}

	public void setRttServerMs(Double rttServerMs) {
		this.rttServerMs = rttServerMs;
	}
}