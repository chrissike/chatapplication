package edu.hm.dako.benchmarkUtil;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

@SuppressWarnings("restriction")
public class SystemResourceCalculator {

	Runtime r;
//	private static OperatingSystemMXBean osbean;
//	private static int nCPUs;
//
//	private static Long startWallclockTime;
//	private static Long startCpuTime;

	public SystemResourceCalculator() {
		r = Runtime.getRuntime();
//		startWallclockTime = System.nanoTime();
	}

	/**
	 * Liefert den freien Speicher
	 */
	public Long getFreeMemory() {
		return r.freeMemory() / (1024 * 1024);
	}

	/**
	 * Berechnet den tatsaechlich benutzten Heap-Speicher Heap-Groesse in MiB
	 * 
	 * @return
	 */
	public Long getUsedMemory() {
		return (r.totalMemory() - r.freeMemory());
	}

	/**
	 * Berechnet die durchschnittliche CPU-Auslastung
	 * 
	 * @return Average CPU
	 */
	public static Float getAvgCPUUsage() {
//		SystemResourceCalculator.osbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
//		SystemResourceCalculator.nCPUs = osbean.getAvailableProcessors();
//		startCpuTime = osbean.getProcessCpuTime();
//		float wallclockTimeDelta = System.nanoTime() - startWallclockTime;
//		float cpuTimeDelta = osbean.getProcessCpuTime() - startCpuTime;
//		cpuTimeDelta = Math.max(cpuTimeDelta, 1);
//
//		return (cpuTimeDelta / (float) nCPUs) / wallclockTimeDelta;
		return new Float(1.0);
	}
}
