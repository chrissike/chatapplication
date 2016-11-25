package edu.hm.dako.chat.client.benchmarking;

import javax.jms.JMSException;
import javax.jms.Message;

import edu.hm.dako.chat.client.ui.BenchmarkingClientFxGUI;
import edu.hm.dako.chat.common.ChatPDU;

public class ReceiverThread implements Runnable {

	private ChatPDU chatPDU;
	private String messageNumber;
	private Long rtt;
	private Double rttMs;
	private Double rttServerMs;
	
	public ReceiverThread(Message message, Integer messageNumber) {
		try {
			chatPDU = message.getBody(ChatPDU.class);
			this.messageNumber = String.valueOf(messageNumber);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		if (chatPDU != null && BenchmarkingClientFxGUI.instance != null) {
			chatPDU.setUserName(this.messageNumber);
			stopAndCalculateRTT();
			BenchmarkingClientFxGUI.instance.showResults(chatPDU, getRtt(), getRttMs(), getRttServerMs());
		}
	}

	private void stopAndCalculateRTT() {
		setRtt(System.nanoTime() - chatPDU.getClientStartTime());
		setRttMs(rtt.longValue() / 1000000000.0);
		setRttServerMs(chatPDU.getServerTime().longValue() / 1000000000.0);
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