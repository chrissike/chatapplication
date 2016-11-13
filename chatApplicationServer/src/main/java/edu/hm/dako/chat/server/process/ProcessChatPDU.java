package edu.hm.dako.chat.server.process;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.server.user.SharedChatClientList;

public interface ProcessChatPDU {

	public void processMessage(ChatPDU pdu);
	
	public void processClientListChange(SharedChatClientList clientList, long startTime);
}
