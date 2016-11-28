package edu.hm.dako.chat.model;

import java.io.Serializable;
import java.util.List;

public interface PDU extends Serializable {
	
	public void setClients(List<String> clients);
	public void setPduType(PduType pduType);
	public void setUserName(String userName);
	public void setServerThreadName(String threadName);
	public void setMessage(String msg);
	public void setServerTime(Long time);
	public PduType getPduType();
	public List<String> getClients();
	public String getUserName();
	public String getServerThreadName();
	public String getMessage();
	public Long getServerTime();
	public Long getNumberOfRetries();
	public void setNumberOfRetries(Long nr);
	public Long getClientStartTime();
	public void setClientStartTime(Long clientStartTime);
}
