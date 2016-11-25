package edu.hm.dako.chat.server.datasink;

import java.util.List;

import edu.hm.dako.chat.server.datasink.model.CountEntity;
import edu.hm.dako.chat.server.datasink.model.TraceEntity;

public interface DataSink {

	public Boolean createOrUpdateCount(CountEntity count);

	public void persistTrace(TraceEntity trace);

	public List<CountEntity> getAllCountData();

	public List<TraceEntity> getAllTraceData();
}
