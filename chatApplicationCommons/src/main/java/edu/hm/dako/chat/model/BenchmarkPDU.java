package edu.hm.dako.chat.model;

public class BenchmarkPDU extends ChatPDU implements PDU {

	private static final long serialVersionUID = 1L;

	private Double freeMemory;
	
	private Double usedMemory;
	
	
	public BenchmarkPDU() {
		super();
	}

	public Double getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(Double freeMemory) {
		this.freeMemory = freeMemory;
	}

	public Double getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(Double usedMemory) {
		this.usedMemory = usedMemory;
	}
	
}
