package edu.hm.dako.chat.client.benchmarking;

import javax.jms.JMSException;
import javax.jms.Message;

import edu.hm.dako.chat.client.ui.BenchmarkingClientFxGUI;
import edu.hm.dako.chat.common.ChatPDU;

public class ReceiverThread implements Runnable {

	private ChatPDU chatPDU;
	private String messageNumber;
	
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
			BenchmarkingClientFxGUI.instance.showResults(chatPDU);
		}
	}
}