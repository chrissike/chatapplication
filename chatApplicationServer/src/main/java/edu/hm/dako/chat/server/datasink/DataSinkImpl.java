package edu.hm.dako.chat.server.datasink;

import edu.hm.dako.chat.server.datasink.model.CountEntity;
import edu.hm.dako.chat.server.datasink.model.TraceEntity;
import edu.hm.dako.chat.server.datasink.repo.CountRepository;
import edu.hm.dako.chat.server.datasink.repo.TraceRepository;

import javax.inject.Inject;
import java.util.List;

//import edu.hm.dako.chat.server.datasink.model.CountEntity;
//import edu.hm.dako.chat.server.datasink.repo.CountRepository;

public class DataSinkImpl implements DataSink {

	@Inject
	private CountRepository countRepo;

	@Inject
	private TraceRepository traceRepo;

	public Boolean createOrUpdateCount(CountEntity count) {

		Boolean success = false;
		List<CountEntity> entityList = countRepo.getCountByClientname(count.getNameOfClients());

		if (entityList == null) {
			countRepo.addCount(count);
			success = true;
		}
		if (entityList.size() == 0 || entityList.isEmpty()) {
			countRepo.addCount(count);
			success = true;
		}
		if (entityList.size() == 1) {
			CountEntity existingCount = entityList.get(0);
			existingCount.setMessageCount(existingCount.getMessageCount() + 1);
			countRepo.updateCount(existingCount);
			success = true;
		}
		
		return success;
	}

	public void persistTrace(TraceEntity trace) {		
		traceRepo.addTrace(trace);
	}

	public List<TraceEntity> getAllTraceData() {
		return traceRepo.showTrace();
	}

	@Override
	public List<CountEntity> getCountByClientname(final String clientName) {
		return countRepo.getCountByClientname(clientName);
	}

	@Override
	public void deleteAllData() {
		countRepo.deleteAllCount();
		traceRepo.deleteAllTrace();
	}
}
