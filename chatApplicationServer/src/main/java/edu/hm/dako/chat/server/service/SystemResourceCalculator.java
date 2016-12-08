package edu.hm.dako.chat.server.service;

import javax.ejb.Stateless;

@Stateless
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
	 * 
	 * @return
	 */
	public Long getUsedMemory() {
		return (r.totalMemory() - r.freeMemory());
	}
}
