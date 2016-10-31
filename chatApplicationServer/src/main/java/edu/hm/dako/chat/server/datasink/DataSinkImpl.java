package edu.hm.dako.chat.server.datasink;

import java.util.List;

import javax.inject.Inject;

import edu.hm.dako.chat.server.datasink.model.CountEntity;
import edu.hm.dako.chat.server.datasink.model.TraceEntity;
import edu.hm.dako.chat.server.datasink.repo.CountRepository;
import edu.hm.dako.chat.server.datasink.repo.TraceRepository;

public class DataSinkImpl implements DataSink {

	@Inject
	private CountRepository countRepo;

	@Inject
	private TraceRepository traceRepo;

	public Boolean createOrUpdateCount(CountEntity count) {

		Boolean success = false;
		List<CountEntity> entityList = countRepo.getCountByClientname();

		if (entityList == null) {
			countRepo.addCount(count);
			success = true;
		}
		if (entityList.size() == 0 || entityList.isEmpty()) {
			countRepo.addCount(count);
			success = true;
		}
		if (entityList.size() == 1) {
			countRepo.addCount(count);
			success = true;
		}
		
		return success;
	}

	public void persistTrace(TraceEntity trace) {		
		traceRepo.addTrace(trace);
	}

}
