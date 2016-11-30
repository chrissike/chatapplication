package edu.hm.dako.chat.server.service;

import edu.hm.dako.chat.model.ChatPDU;
import edu.hm.dako.chat.model.PDU;
import edu.hm.dako.chat.model.PduType;

public interface ProcessPDU {

	public void processMessage(PDU pdu) throws Exception;

	public boolean processClientListChange(ChatPDU pdu, long startTime);

	public ChatPDU createPDU(String username, PduType requestType);
}
