package edu.hm.dako.chat.model;

public class BenchmarkPDU extends ChatPDU implements PDU {

	private static final long serialVersionUID = 1L;

	/**
	 * Der noch verfügbare Arbeitsspeicher (gesamter Arbeitsspeicher -
	 * verwendeter Arbeitsspeicher)
	 */
	private Double freeMemory;

	/**
	 * Der aktuell verwendete Arbeitsspeicher
	 */
	private Double usedMemory;

	/**
	 * Durchschnittliche CPU-Auslastung
	 */
	private Double avgCPUUsage;

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

	public Double getAvgCPUUsage() {
		return avgCPUUsage;
	}

	public void setAvgCPUUsage(Double avgCPUUsage) {
		this.avgCPUUsage = avgCPUUsage;
	}

}
