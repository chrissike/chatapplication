package edu.hm.dako.chat.server.process;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.common.PduType;

public interface ProcessChatPDU {

	public void processMessage(ChatPDU pdu);

	public boolean processClientListChange(ChatPDU pdu, long startTime);

	public ChatPDU createPDU(String username, PduType requestType);
}
