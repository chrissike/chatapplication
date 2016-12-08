/**
 * Organisation: University of Applied Sciences Munich, FK07
 * Project: Software-Architektur Praktikum
 * Sub-Project: chatApplication
 * Author: David Sautter (04358212)
 * Created On: 29.11.16
 * Operating-System: Arch-Linux
 * Java-Version: 1.8.0_40
 * CPU: Intel(R) Core(TM)2 Duo CPU T5870 @ 2.00GHz
 */

package edu.hm.dako.chat.server.rest.model;

import edu.hm.dako.chat.server.datasink.model.TraceEntity;
import edu.hm.dako.chat.server.user.SharedChatClientList;

import java.io.Serializable;
import java.util.List;
import java.util.OptionalDouble;

public class StatisticsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long numClients;
	
	private Integer totalMessages;
	
	private OptionalDouble avgMsgLength;

	public StatisticsDTO(List<TraceEntity> traceList) {
		SharedChatClientList sharedChatClientList = SharedChatClientList.getInstance();
		numClients = sharedChatClientList.size();
		List<TraceEntity> traceData = traceList;
		totalMessages = traceData.size();

		if (totalMessages > 0) {
			avgMsgLength = traceData
					.parallelStream()
					.mapToInt(trace -> trace.getNachricht().length())
					.average();
		}
	}

	public Long getNumClients() {
		return numClients;
	}

	public void setNumClients(Long numClients) {
		this.numClients = numClients;
	}

	public Integer getTotalMessages() {
		return totalMessages;
	}

	public void setTotalMessages(Integer totalMessages) {
		this.totalMessages = totalMessages;
	}

	public OptionalDouble getAvgMsgLength() {
		return avgMsgLength;
	}

	public void setAvgMsgLength(OptionalDouble avgMsgLength) {
		this.avgMsgLength = avgMsgLength;
	}
}
