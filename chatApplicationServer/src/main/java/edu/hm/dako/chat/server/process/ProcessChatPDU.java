package edu.hm.dako.chat.server.process;

import edu.hm.dako.chat.model.ChatPDU;
import edu.hm.dako.chat.model.PDU;
import edu.hm.dako.chat.model.PduType;

public interface ProcessChatPDU {

	public void processMessage(PDU pdu);

	public boolean processClientListChange(ChatPDU pdu, long startTime);

	public ChatPDU createPDU(String username, PduType requestType);
}
