package edu.hm.dako.chat.client.benchmarking;

import javax.jms.JMSException;
import javax.jms.Message;

import edu.hm.dako.chat.client.ui.BenchmarkingClientFxGUI;
import edu.hm.dako.chat.model.BenchmarkPDU;
import edu.hm.dako.chat.model.PDU;

public class ReceiverThread implements Runnable {
	
	private BenchmarkPDU bmPDU;
	private Long rtt;
	private Double rttMs;
	private Double rttServerMs;

	public ReceiverThread(Message message) {
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
			stopAndCalculateRTT();
			BenchmarkingClientFxGUI.instance.getModel().addRTTToSharedRTTClientList(bmPDU.getUserName(), getRtt());
			bmPDU.setAvgCPUUsage(BenchmarkingClientFxGUI.getSysResourceCalc().getAverageCpuUtilisation().doubleValue() * 100);
			bmPDU.setMessageNr(BenchmarkingClientFxGUI.getAndIncreaseClientNameReceivedCounter());
			BenchmarkingClientFxGUI.instance.showResults(bmPDU, getRtt(), getRttMs(), getRttServerMs());
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