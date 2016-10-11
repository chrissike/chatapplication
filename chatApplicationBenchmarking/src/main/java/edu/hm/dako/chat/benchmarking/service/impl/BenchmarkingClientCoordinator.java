package edu.hm.dako.chat.benchmarking.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.benchmarking.BenchmarkingClientFactory;
import edu.hm.dako.chat.benchmarking.BenchmarkingTimeCounterThread;
import edu.hm.dako.chat.benchmarking.data.UserInterfaceInputParameters;
import edu.hm.dako.chat.benchmarking.data.UserInterfaceResultData;
import edu.hm.dako.chat.benchmarking.data.UserInterfaceStartData;
import edu.hm.dako.chat.benchmarking.service.BenchmarkingClientUserInterface;
import edu.hm.dako.chat.benchmarking.service.BenchmarkingStartInterface;
import edu.hm.dako.chat.client.ClientUserInterface;
import edu.hm.dako.chat.benchmarking.service.impl.CpuUtilisationWatch;
import edu.hm.dako.chat.benchmarking.service.impl.DistributionMetrics;
import edu.hm.dako.chat.benchmarking.service.impl.SharedClientStatistics;

/**
 * Basisklasse zum Starten eines Benchmarks
 *
 * @author Mandl
 */
public class BenchmarkingClientCoordinator extends Thread implements BenchmarkingStartInterface, ClientUserInterface {
	private static Log log = LogFactory.getLog(BenchmarkingClientCoordinator.class);

	// Daten aller Client-Threads zur Verwaltung der Statistik
	private SharedClientStatistics sharedData;
	private CpuUtilisationWatch cpuUtilisationWatch;

	// Kennzeichen, ob gerade ein Test laeuft (es darf nur einer zu einer Zeit
	// laufen)
	private boolean running = false;

	// Kennzeichen, ob Test in der GUI gestoppt wurde
	private boolean abortedFlag = false;

	// Uebergebene Parameter vom User-Interface
	UserInterfaceInputParameters parm;

	// GUI-Schnittstelle
	BenchmarkingClientUserInterface clientGui;

	// Anzahl aller Requests, die auszufuehren sind
	long numberOfAllRequests;

	// Startzeit des Tests
	long startTime;

	// Startzeit als String
	String startTimeAsString;

	// Kalender zur Umrechnung der Startzeit
	Calendar cal;

	// Thread zur Zeitzaehlung fuer die Dauer des Tests
	BenchmarkingTimeCounterThread timeCounterThread;

	/**
	 * Methode liefert die aktuelle Zeit als String
	 *
	 * @param cal
	 *            Kalender
	 * @return Zeit als String
	 */
	private String getCurrentTime(Calendar cal) {
		return new SimpleDateFormat("dd.MM.yy HH:mm:ss:SSS").format(cal.getTime());
	}

	public void executeTest(UserInterfaceInputParameters parm, BenchmarkingClientUserInterface clientGui) {

		this.parm = parm;
		this.clientGui = clientGui;

		clientGui.setMessageLine(
				parm.mapImplementationTypeToString(parm.getImplementationType()) + ": Benchmark gestartet");

		// Anzahl aller erwarteten Requests ermitteln

		numberOfAllRequests = parm.getNumberOfClients() * parm.getNumberOfMessages();

		// Gemeinsamen Datenbereich fuer alle Threads anlegen
//		sharedData = new SharedClientStatistics(parm.getNumberOfClients(), parm.getNumberOfMessages(),
//				parm.getClientThinkTime(), this.clientGui);

		// Berechnung aller Messages fuer Progress-Bar
		if (clientGui.getProgressBar() != null) {
			clientGui.getProgressBar().setMaximum(parm.getNumberOfClients() * parm.getNumberOfMessages()
					+ parm.getNumberOfClients() + parm.getNumberOfClients());
		}

		/**
		 * Startzeit ermitteln
		 */
		startTime = 0;
		cal = Calendar.getInstance();
		startTime = cal.getTimeInMillis();
		startTimeAsString = getCurrentTime(cal);

		/**
		 * Laufzeitzaehler-Thread erzeugen
		 */
		timeCounterThread = new BenchmarkingTimeCounterThread(clientGui);
		timeCounterThread.start();

		cpuUtilisationWatch = new CpuUtilisationWatch();

		start();
	}

	/**
	 * Thread zur Entkoppelung des User-Interface von der Testausfuehrung, damit
	 * im User-Interface Eingaben moeglich sind, waehrend der Benchmark laeuft
	 * (z.B. Abbruch).
	 */

	public void run() {

		// Test aktiv
		running = true;

		// Client-Threads in Abhaengigkeit des Implementierungstyps
		// instanziieren
		// und starten
		ExecutorService executorService = Executors.newFixedThreadPool(parm.getNumberOfClients());

		for (int i = 0; i < parm.getNumberOfClients(); i++) {
			executorService.submit(BenchmarkingClientFactory.getClient(this, parm, i, sharedData));
		}

		// Startwerte anzeigen
		UserInterfaceStartData startData = new UserInterfaceStartData();
		startData.setNumberOfRequests(numberOfAllRequests);
		startData.setStartTime(getCurrentTime(cal));

		// Maximal moegliche Events = ChatMessage-Events + Login-Events +
		// Logout-Events
		long numberOfPlannedEventMessages = (numberOfAllRequests * parm.getNumberOfClients())
				+ (parm.getNumberOfClients() * parm.getNumberOfClients())
				+ (parm.getNumberOfClients() * parm.getNumberOfClients());

		log.debug("Anzahl geplanter Event-Nachrichten: " + numberOfPlannedEventMessages);
		startData.setNumberOfPlannedEventMessages(numberOfPlannedEventMessages);

		clientGui.showStartData(startData);

		clientGui.setMessageLine("Alle " + parm.getNumberOfClients() + " Clients-Threads gestartet");

		// Auf das Ende aller Clients warten
		executorService.shutdown();

		try {
			executorService.awaitTermination(10000, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			log.error("Das Beenden des ExecutorService wurde unterbrochen");
			e.printStackTrace();
		}

		// Laufzeitzaehler-Thread beenden
		timeCounterThread.stopThread();

		// Analyse der Ergebnisse durchfuehren, Statistikdaten berechnen und
		// ausgeben
		// sharedData.printStatistic();

		// Testergebnisse ausgeben
		clientGui.setMessageLine("Alle Clients-Threads beendet");

		UserInterfaceResultData resultData = getResultData(parm, startTime);

		clientGui.showResultData(resultData);
		clientGui.setMessageLine(
				parm.mapImplementationTypeToString(parm.getImplementationType()) + ": Benchmark beendet");

		clientGui.testFinished();

		log.debug("Anzahl aller erneuten Sendungen wegen Nachrichtenverlust (Uebertragungswiederholungen): "
				+ sharedData.getSumOfAllRetries());

		// Datensatz fuer Benchmark-Lauf auf Protokolldatei schreiben
		sharedData.writeStatisticSet("Benchmarking-ChatApp-Protokolldatei",
				parm.mapImplementationTypeToString(parm.getImplementationType()),
				parm.mapMeasurementTypeToString(parm.getMeasurementType()), startTimeAsString, resultData.getEndTime(),
				cpuUtilisationWatch.getAverageCpuUtilisation());

		// In der GUI erneute Testlaeufe zulassen
		running = false;
	}

	// Wird nicht genutzt, nur fuer ChatClientGUI relevant
	public synchronized void setUserList(Vector<String> names) {
	}

	// Wird nicht genutzt, nur fuer ChatClientGUI relevant
	public synchronized void setMessageLine(String sender, String message) {
	}

	// Wird nicht genutzt, nur fuer BenchmarkingClientImpl relevant
	public synchronized void setLock(boolean lock) {
	}

	// Wird nicht genutzt, nur fuer ChatClientGUI
	public void setErrorMessage(String sender, String errorMessage, long errorCode) {
	}

	// Wird nicht genutzt, nur fuer BenchmarkingClientImpl relevant
	public void loginComplete() {
	}

	// Wird nicht genutzt, nur fuer BenchmarkingClientImpl relevant
	public void logoutComplete() {
	}

	// Wird nicht genutzt, nur fuer BenchmarkingClientImpl relevant
	public synchronized boolean getLock() {
		return false;
	}

	// Wird nicht genutzt, nur fuer BenchmarkingClientImpl relevant
	public synchronized void setLastServerTime(long lastServerTime) {
	}

	public synchronized void abortTest() {
		abortedFlag = true;
	}

	public synchronized boolean isRunning() {
		return (this.running);
	}

	public synchronized void releaseTest() {
		this.abortedFlag = false;
	}

	public synchronized boolean isTestAborted() {
		return abortedFlag;
	}


	/**
	 * Ergebnisdaten des Tests aufbereiten
	 * 
	 * @param parm
	 *            Eingabedaten fuer die GUI
	 * @param startTime
	 *            Startzeit des Tests
	 * @return
	 */
	private UserInterfaceResultData getResultData(UserInterfaceInputParameters parm, long startTime) {

		Calendar cal;
		UserInterfaceResultData resultData = new UserInterfaceResultData();
		DistributionMetrics distributionMetrics = sharedData.calculateMetrics();

		resultData.setPercentile10(distributionMetrics.getPercentile10());
		resultData.setMean(distributionMetrics.getMean());
		resultData.setPercentile25(distributionMetrics.getPercentile25());
		resultData.setPercentile50(distributionMetrics.getPercentile50());
		resultData.setPercentile75(distributionMetrics.getPercentile75());
		resultData.setPercentile90(distributionMetrics.getPercentile90());
		resultData.setStandardDeviation(distributionMetrics.getStandardDeviation());
		resultData.setRange(distributionMetrics.getRange());
		resultData.setInterquartilRange(distributionMetrics.getInterquartilRange());
		resultData.setMinimum(distributionMetrics.getMinimum());
		resultData.setMaximum(distributionMetrics.getMaximum());

		resultData.setAvgServerTime(sharedData.getAverageServerTime() / 1000000.0);

		cal = Calendar.getInstance();
		resultData.setEndTime(getCurrentTime(cal));

		long elapsedTimeInSeconds = (cal.getTimeInMillis() - startTime) / 1000;
		resultData.setElapsedTime(elapsedTimeInSeconds);

		resultData.setMaxCpuUsage(cpuUtilisationWatch.getAverageCpuUtilisation());

		resultData.setMaxHeapSize(sharedData.getMaxHeapSize() / (1024 * 1024));

		resultData.setNumberOfResponses(sharedData.getSumOfAllReceivedMessages());
		resultData.setNumberOfSentRequests(sharedData.getNumberOfSentRequests());
		resultData.setNumberOfLostResponses(sharedData.getNumberOfLostResponses());
		resultData.setNumberOfRetries(sharedData.getSumOfAllRetries());
		resultData.setNumberOfSentEventMessages(sharedData.getSumOfAllSentEventMessages());
		resultData.setNumberOfReceivedConfirmEvents(sharedData.getSumOfAllReceivedConfirmEvents());
		resultData.setNumberOfLostConfirmEvents(sharedData.getSumOfAllLostConfirmEvents());
		resultData.setNumberOfRetriedEvents(sharedData.getSumOfAllRetriedEvents());
		return resultData;
	}

	public long getLastServerTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setSessionStatisticsCounter(long numberOfSentEvents, long numberOfReceivedConfirms,
			long numberOfLostConfirms, long numberOfRetries, long numberOfReceivedChatMessages) {
		// TODO Auto-generated method stub
		
	}

	public long getNumberOfSentEvents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getNumberOfReceivedConfirms() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getNumberOfLostConfirms() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getNumberOfRetries() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getNumberOfReceivedChatMessages() {
		// TODO Auto-generated method stub
		return 0;
	}
}