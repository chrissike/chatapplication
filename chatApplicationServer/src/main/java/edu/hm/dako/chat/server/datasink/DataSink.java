package edu.hm.dako.chat.server.datasink;

import edu.hm.dako.chat.server.datasink.model.CountEntity;
import edu.hm.dako.chat.server.datasink.model.TraceEntity;

public interface DataSink {

	public Boolean createOrUpdateCount(CountEntity count);
	
	public void persistTrace(TraceEntity trace);
}
