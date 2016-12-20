package edu.hm.dako.chat.server.datasink;

import edu.hm.dako.chat.server.datasink.model.CountEntity;
import edu.hm.dako.chat.server.datasink.model.TraceEntity;

import java.util.List;

public interface DataSink {

	public void createOrUpdateCount(CountEntity count) throws Exception;

	public void persistTrace(TraceEntity trace) throws Exception;

	public List<TraceEntity> getAllTraceData() throws Exception;

	public List<CountEntity> getCountByClientname(String clientName) throws Exception;

	public void deleteAllData() throws Exception;
}
