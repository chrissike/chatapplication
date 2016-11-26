package edu.hm.dako.chat.server.datasink;

import edu.hm.dako.chat.server.datasink.model.CountEntity;
import edu.hm.dako.chat.server.datasink.model.TraceEntity;

import java.util.List;

public interface DataSink {

	public Boolean createOrUpdateCount(CountEntity count);

	public void persistTrace(TraceEntity trace);

	public List<TraceEntity> getAllTraceData();

	public List<CountEntity> getCountByClientname(String clientName);

	public void deleteAllData();
}
