package edu.hm.dako.chat.client.data.util;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

@SuppressWarnings("restriction")
public class SystemResourceCalculator {

	private static final OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory
			.getOperatingSystemMXBean();
	private static final int nCPUs = osbean.getAvailableProcessors();

	private Long startWallclockTime;
	private Long startCpuTime;

	public SystemResourceCalculator() {
		startWallclockTime = System.nanoTime();
		startCpuTime = osbean.getProcessCpuTime();
	}

	public Float getAverageCpuUtilisation() {
		float wallclockTimeDelta = System.nanoTime() - startWallclockTime;
		float cpuTimeDelta = osbean.getProcessCpuTime() - startCpuTime;
		cpuTimeDelta = Math.max(cpuTimeDelta, 1);

		return (cpuTimeDelta / (float) nCPUs) / wallclockTimeDelta;
	}
	
//	private static OperatingSystemMXBean osbean;
//	private static int nCPUs;
//
//	private static Long startWallclockTime;
//	private static Long startCpuTime;
//
//	public SystemResourceCalculator() {
//		startWallclockTime = System.nanoTime();
//		osbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
//	}
//
//	/**
//	 * Berechnet die durchschnittliche CPU-Auslastung
//	 * 
//	 * @return Average CPU
//	 */
//	public Float getAvgCPUUsage() {
//		SystemResourceCalculator.nCPUs = osbean.getAvailableProcessors();
//		startCpuTime = osbean.getProcessCpuTime();
//		float wallclockTimeDelta = System.nanoTime() - startWallclockTime;
//		float cpuTimeDelta = osbean.getProcessCpuTime() - startCpuTime;
//		cpuTimeDelta = Math.max(cpuTimeDelta, 1);
//
//		Float avgCPU = ((cpuTimeDelta / (float) nCPUs) / wallclockTimeDelta)*100;
//		return avgCPU;
//	}
}
