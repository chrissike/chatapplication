package edu.hm.dako.chat.client.data.util;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

@SuppressWarnings("restriction")
public class SystemResourceCalculator {

//	private static OperatingSystemMXBean osbean;
//	private static int nCPUs;
//
//	private static Long startWallclockTime;
//	private static Long startCpuTime;

	public SystemResourceCalculator() {
//		startWallclockTime = System.nanoTime();
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
