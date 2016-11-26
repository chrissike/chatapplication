package edu.hm.dako.chat.client.benchmarking;

public class TrackSystemStatus {

	Runtime runtime = Runtime.getRuntime();

	public void memory() {
		long allocatedMemory = runtime.totalMemory();
	}
}
