package edu.hm.dako.chat.benchmarking.ui;

import javafx.scene.control.ProgressBar;
import javax.swing.JProgressBar;

import edu.hm.dako.chat.benchmarking.data.UserInterfaceInputParameters;
import edu.hm.dako.chat.benchmarking.data.UserInterfaceResultData;
import edu.hm.dako.chat.benchmarking.data.UserInterfaceStartData;
import edu.hm.dako.chat.benchmarking.service.BenchmarkingClientUserInterface;
import edu.hm.dako.chat.benchmarking.service.impl.BenchmarkingClientCoordinator;

/**
 * Diese Klasse simuliert eine Benutzeroberflaeche.
 *
 * @author mandl
 */

public class BenchmarkingUserInterfaceSimulation implements
    BenchmarkingClientUserInterface {

  private int timeCounter = 0;

  public void showStartData(UserInterfaceStartData data) {

    System.out.println("Testbeginn: " + data.getStartTime());
    System.out.println("Geplante Requests: " + data.getNumberOfRequests());
  }

  public void showResultData(UserInterfaceResultData data) {

    System.out.println("Testende: " + data.getEndTime());
    System.out.println("Testdauer in s: " + data.getElapsedTime());

    System.out.println("Gesendete Requests: "
	  + data.getNumberOfSentRequests());
    System.out.println("Anzahl Responses: " + data.getNumberOfResponses());
    System.out.println("Anzahl verlorener Responses: "
	  + data.getNumberOfLostResponses());

    System.out.println("Mittlere RTT in ms: " + data.getMean());
    System.out.println("Maximale RTT in ms: " + data.getMaximum());
    System.out.println("Minimale RTT in ms: " + data.getMinimum());
    System.out.println("Mittlere Serverbearbeitungszeit in ms: "
	  + data.getAvgServerTime());

    System.out.println("Maximale Heap-Belegung in MByte: "
	  + data.getMaxHeapSize());
    System.out.println("Maximale CPU-Auslastung in %: "
	  + data.getMaxCpuUsage());
  }

  public void setMessageLine(String message) {
    System.out.println("*** Meldung: " + message + " ***");
  }

  public void addCurrentRunTime(long sec) {
    // Feld Testdauer um sec erhoehen
    timeCounter += sec;
    System.out.println("Laufzeitzaehler: " + timeCounter);
  }

  public void resetCurrentRunTime() {
    // Feld Testdauer auf 0 setzen
    timeCounter = 0;
  }

  public synchronized void testFinished() {
    System.out.println("Testlauf beendet");
  }

  public JProgressBar getProgressBar() {
    return null;
  }

  public ProgressBar getProgressBarFx() {
    return null;
  }

  public void countUpProgressTask() {
  }

  /**
   * main
   *
   * @param args
   */
  public static void main(String args[]) {
    new BenchmarkingUserInterfaceSimulation().doWork();
  }

  public void doWork() {
    // Input-parameter aus GUI
    UserInterfaceInputParameters parm = new UserInterfaceInputParameters();

    // GUI sammmelt Eingabedaten ...

    // Benchmarking-Client instanzieren und Benchmark starten

    BenchmarkingClientCoordinator benchClient = new BenchmarkingClientCoordinator();
    benchClient.executeTest(parm, this);
  }
}