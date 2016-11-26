package edu.hm.dako.chat.client.data;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

@SuppressWarnings("restriction")
public class SystemStatus {


	private List<Double> cpuList;
	private List<Double> memoryList;

	private DoubleProperty avgCPU;
	private IntegerProperty avgMemory;
	
	public SystemStatus() {
		cpuList = new ArrayList<Double>();
		memoryList = new ArrayList<Double>();
		
		avgCPU = new SimpleDoubleProperty(0.0);
		avgMemory = new SimpleIntegerProperty(0);
	}
	
	public synchronized void addCpuList(Double cpuList) {
		this.cpuList.add(cpuList);
	}
	
	public synchronized void addMemoryList(Double avgMemory) {
		this.memoryList.add(avgMemory);
	}
	
	public DoubleProperty getAvgCPU() {
		return avgCPU;
	}

	public synchronized void setAvgCPU(Double avgCPU) {
		this.avgCPU.set(avgCPU);
	}

	public IntegerProperty getAvgMemory() {
		return avgMemory;
	}

	public synchronized void setAvgMemory(Integer avgMemory) {
		this.avgMemory.set(avgMemory);
	}
}
