package edu.hm.dako.chat.benchmarking;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.benchmarking.service.BenchmarkingClientUserInterface;

/**
 * Thread berechnet die Laufzeit eines Benchmarks und sendet diese zyklisch an die GIU
 * 
 * @author mandl
 *
 */
public class BenchmarkingTimeCounterThread extends Thread {

  private static Log log = LogFactory.getLog(BenchmarkingTimeCounterThread.class);

  private static final int numberOfSeconds = 1;

  private BenchmarkingClientUserInterface out = null;

  private boolean running = true;

  public BenchmarkingTimeCounterThread(BenchmarkingClientUserInterface clientGui) {
    setName("TimeCounterThread");
    this.out = clientGui;
  }

  /**
   * Run-Methode fuer den Thread: Erzeugt alle n Sekunden einen Zaehler und sendet ihn an
   * die Ausgabe
   */
  public void run() {
    log.debug(getName() + " gestartet");

    out.resetCurrentRunTime();

    while (running) {
	try {
	  TimeUnit.SECONDS.sleep(numberOfSeconds);
	} catch (InterruptedException e) {
	  log.debug("Sleep unterbrochen");
	}

	out.addCurrentRunTime(numberOfSeconds);
    }
  }

  /**
   * Beenden des Threads
   */
  public void stopThread() {
    running = false;
    log.debug(getName() + " gestoppt");
  }
}
