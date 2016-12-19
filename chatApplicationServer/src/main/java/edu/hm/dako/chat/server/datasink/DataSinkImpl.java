package edu.hm.dako.chat.server.datasink;

import edu.hm.dako.chat.server.datasink.model.CountEntity;
import edu.hm.dako.chat.server.datasink.model.TraceEntity;
import edu.hm.dako.chat.server.datasink.repo.CountRepository;
import edu.hm.dako.chat.server.datasink.repo.TraceRepository;

import javax.inject.Inject;

import java.util.List;

public class DataSinkImpl implements DataSink {

	@Inject
	private CountRepository countRepo;

	@Inject
	private TraceRepository traceRepo;
	
	public synchronized void createOrUpdateCount(CountEntity count) throws Exception {

		List<CountEntity> entityList = countRepo.getCountByClientname(count.getNameOfClient());

		if (entityList.size() >= 1) {
			CountEntity existingCount = entityList.get(0);
			existingCount.setMessageCount(existingCount.getMessageCount() + 1);
			countRepo.updateCount(existingCount);
		}
		if (entityList.size() == 0 || entityList.isEmpty()) {
			countRepo.addCount(count);
		}
	}

	public void persistTrace(TraceEntity trace) throws Exception {
		traceRepo.addTrace(trace);
	}

	public List<TraceEntity> getAllTraceData() {
		return traceRepo.showTrace();
	}

	@Override
	public synchronized List<CountEntity> getCountByClientname(final String clientName) throws Exception {
		return countRepo.getCountByClientname(clientName);
	}

	@Override
	public void deleteAllData() throws Exception {
		traceRepo.deleteAllTrace();
		countRepo.deleteAllCount();
	}
}
