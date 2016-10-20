package edu.hm.dako.chat.server.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.server.SimpleChatServerImpl;

public class ProcessChatPDU {

	private static Log log = LogFactory.getLog(SimpleChatServerImpl.class);
	
	public void process(ChatPDU pdu) {
		log.info("JMS-Nachricht ist angekommen: " + pdu.toString());
	}
	
}
