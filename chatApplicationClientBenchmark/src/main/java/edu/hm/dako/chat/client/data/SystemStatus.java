package edu.hm.dako.chat.client.data;

import java.util.ArrayList;
import java.util.List;

import edu.hm.dako.chat.client.data.util.ModelCalculator;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

@SuppressWarnings("restriction")
public class SystemStatus {

	private ModelCalculator calculator;

	private List<Double> cpuList;
	private List<Integer> freeServerMemoryList;

	private DoubleProperty avgCPU;
	private IntegerProperty minServerMemory;

	public SystemStatus() {
		calculator = new ModelCalculator();

		cpuList = new ArrayList<Double>();
		freeServerMemoryList = new ArrayList<Integer>();

		avgCPU = new SimpleDoubleProperty(0.0);
		minServerMemory = new SimpleIntegerProperty(0);
	}

	public synchronized void calculateSysStatus() {
		//Memory
		setMinServerMemory(Math.round(calculator.getMinOfIntegerList(getFreeServerMemoryList())));
		
		//CPU
		setAvgCPU(Math.round(calculator.calcAverageOfDouble(getCpuList()) * 100.0) / 100.0);
	}

	public synchronized void addCpuList(Double cpuList) {
		this.cpuList.add(cpuList);
	}
	
	public List<Double> getCpuList() {
		return cpuList;
	}

	public synchronized void addFreeServerMemoryList(Integer freeMemory) {
		this.freeServerMemoryList.add(freeMemory);
	}

	public DoubleProperty getAvgCPU() {
		return avgCPU;
	}

	public synchronized void setAvgCPU(Double avgCPU) {
		this.avgCPU.set(avgCPU);
	}

	public IntegerProperty getMinServerMemory() {
		return minServerMemory;
	}

	public synchronized void setMinServerMemory(Integer minServerMemory) {
		this.minServerMemory.set(minServerMemory);
	}

	public List<Integer> getFreeServerMemoryList() {
		return freeServerMemoryList;
	}

}
