package edu.hm.dako.benchmarkUtil;

public class SystemResourceCalculator {

	Runtime r;

	public SystemResourceCalculator() {
		r = Runtime.getRuntime();
	}

	/**
	 * Liefert den freien Speicher
	 */
	public Long getFreeMemory() {
		return r.freeMemory() / (1024 * 1024);
	}

	/**
	 * Berechnet den tatsaechlich benutzten Heap-Speicher Heap-Groesse in MiB
	 */
	public Long getUsedMemory() {
		return (r.totalMemory() - r.freeMemory());
	}
}
