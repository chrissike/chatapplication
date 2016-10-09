/**
 * 
 * Sehr einfaches und noch nicht ausgereiftes Swing-basiertes GUI fuer den Anstoss von
 * Testlaeufen im Benchmarking-Client
 * 
 * @author Mandl
 */
package edu.hm.dako.chat.benchmarking;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Formatter;

import javafx.scene.control.ProgressBar;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Painter;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.apache.log4j.PropertyConfigurator;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import edu.hm.dako.chat.benchmarking.UserInterfaceInputParameters.MeasurementType;
import edu.hm.dako.chat.common.ImplementationType;

/**
 * User-Interface fuer den Benchmarking-Client
 * 
 * @author mandl
 *
 */
public class BenchmarkingClientGuiSwing extends JPanel implements
    BenchmarkingClientUserInterface, ActionListener {

  public static final String IMPL_TCP_ADVANCED = "TCPAdvanced";
  public static final String IMPL_UDP_ADVANCED = "UDPAdvanced";
  public static final String IMPL_TCP_SIMPLE = "TCPSimple";

  // Zeitzaehler fuer Testlaufzeit
  private long timeCounter = 0;

  // Client-Simulation
  BenchmarkingClientCoordinator benchClient;

  private static JFrame frameBenchmarkingGui; // Frame fuer Anwendungs-GUI
  private static JPanel panelBenchmarkingClientGui;

  /**
   * GUI-Komponenten
   */

  private JComboBox optionListImplType;
  private JComboBox optionListMeasureType;
  private JTextField textFieldNumberOfClientThreads;
  private JFormattedTextField textFieldAvgCpuUsage;
  private JFormattedTextField textFieldNumberOfMessagesPerClients;
  private JTextField textFieldServerport;
  private JTextField textFieldThinkTime;
  private JFormattedTextField textFieldServerIpAddress;
  private JFormattedTextField textFieldMessageLength;
  private JFormattedTextField textFieldNumberOfMaxRetries;
  private JFormattedTextField textFieldResponseTimeout;
  private JFormattedTextField textFieldSeperator;
  private JFormattedTextField textFieldPlannedRequests;
  private JFormattedTextField textFieldTestBegin;
  private JFormattedTextField textFieldSentRequests;
  private JFormattedTextField textFieldTestEnd;
  private JFormattedTextField textFieldReceivedResponses;
  private JFormattedTextField textFieldTestDuration;
  private JFormattedTextField textFieldAvgRTT;
  private JFormattedTextField textFieldAvgServerTime;
  private JFormattedTextField textFieldMaxRTT;
  private JFormattedTextField textFieldMaxHeapUsage;
  private JFormattedTextField textFieldMinRTT;

  private JFormattedTextField textField10Percentile;
  private JFormattedTextField textField25Percentile;
  private JFormattedTextField textField50Percentile;
  private JFormattedTextField textField75Percentile;
  private JFormattedTextField textField90Percentile;
  private JFormattedTextField textFieldRange;
  private JFormattedTextField textFieldStandardDeviation;
  private JFormattedTextField textFieldInterquartilRange;

  // Anzahl der Uebertragungswiederholungen
  private JFormattedTextField textFieldNumberOfRetries;

  private JFormattedTextField textFieldPlannedEventMessages;
  private JFormattedTextField textFieldSentEventMessages;
  private JFormattedTextField textFieldReceivedConfirmEvents;
  private JFormattedTextField textFieldLostConfirmEvents;
  private JFormattedTextField textFieldRetriedEvents;

  private JTextArea messageArea;
  private JScrollPane scrollPane;

  private Button startButton;
  private Button newButton;
  private Button finishButton;
  private Button abortButton;

  private JProgressBar progressBar;

  private static final int TEXTFIELD_WIDTH = 200;
  private static final int TEXTFIELD_HEIGHT = 28;

  private static final long serialVersionUID = 100001000L;
  private Formatter formatter;

  public BenchmarkingClientGuiSwing() {
    super(new BorderLayout());
  }

  private void initComponents() {

    /**
     * Erzeugen der GUI-Komponenten
     */
    String[] optionStrings = { IMPL_TCP_ADVANCED, IMPL_TCP_SIMPLE,
	  IMPL_UDP_ADVANCED };
    optionListImplType = new JComboBox(optionStrings);

    String[] optionStrings1 = { "Variable Threads", "Variable Length" };
    optionListMeasureType = new JComboBox(optionStrings1);

    textFieldNumberOfClientThreads = new JTextField();
    // text = new JFormattedTextField();
    textFieldAvgCpuUsage = new JFormattedTextField();
    // textFieldNumberOfMessages = new JFormattedTextField();
    textFieldNumberOfMessagesPerClients = new JFormattedTextField();
    textFieldServerport = new JTextField();
    textFieldThinkTime = new JTextField();
    textFieldServerIpAddress = new JFormattedTextField();
    textFieldMessageLength = new JFormattedTextField();

    textFieldNumberOfMaxRetries = new JFormattedTextField();
    textFieldResponseTimeout = new JFormattedTextField();

    textFieldSeperator = new JFormattedTextField();
    textFieldPlannedRequests = new JFormattedTextField();
    textFieldTestBegin = new JFormattedTextField();
    textFieldSentRequests = new JFormattedTextField();
    textFieldTestEnd = new JFormattedTextField();
    textFieldReceivedResponses = new JFormattedTextField();
    textFieldTestDuration = new JFormattedTextField();
    textFieldAvgRTT = new JFormattedTextField();
    textFieldAvgServerTime = new JFormattedTextField();
    textFieldMaxRTT = new JFormattedTextField();
    textFieldMaxHeapUsage = new JFormattedTextField();
    textFieldMinRTT = new JFormattedTextField();

    textField10Percentile = new JFormattedTextField();
    textField25Percentile = new JFormattedTextField();
    textField50Percentile = new JFormattedTextField();
    textField75Percentile = new JFormattedTextField();
    textField90Percentile = new JFormattedTextField();
    textFieldRange = new JFormattedTextField();
    textFieldInterquartilRange = new JFormattedTextField();
    textFieldStandardDeviation = new JFormattedTextField();

    // Anzahl der Uebertragungswiederholungen
    textFieldNumberOfRetries = new JFormattedTextField();

    textFieldPlannedEventMessages = new JFormattedTextField();
    textFieldPlannedEventMessages = new JFormattedTextField();
    textFieldSentEventMessages = new JFormattedTextField();
    textFieldReceivedConfirmEvents = new JFormattedTextField();
    textFieldLostConfirmEvents = new JFormattedTextField();
    textFieldRetriedEvents = new JFormattedTextField();

    // Nachrichtenbereich mit Scrollbar
    messageArea = new JTextArea("", 5, 100);

    // messageArea.setLineWrap(true);
    scrollPane = new JScrollPane(messageArea);
    scrollPane
	  .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    // Buttons
    startButton = new Button("Starten");
    newButton = new Button("Neu");
    finishButton = new Button("Beenden");
    abortButton = new Button("Test abbrechen");
  }

  /**
   * Panel anlegen
   *
   * @return
   */
  public JComponent buildPanel() {

    initComponents();

    FormLayout layout = new FormLayout(
	  // Spalten
	  "right:max(40dlu;pref), 3dlu, 80dlu, 7dlu, right:max(40dlu;pref), 3dlu, 80dlu",

	  // Zeilen
	  // Erster Block, Eingabepearameter
	  "p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p,"
		+
		// Zweiter Block, Laufzeitdaten
		"20dlu, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p,"
		+
		// Dritter Block, Statistische Auswertung
		"10dlu, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p,"
		+
		// Vierter Block, Meldungszeilen
		"20dlu, p, p, p," +
		// Fuenfter Block, Buttons
		"p, p, p, p");

    panelBenchmarkingClientGui = new JPanel(layout);
    panelBenchmarkingClientGui.setBorder(Borders.DIALOG_BORDER);

    /*
     * Panel mit Labels und Komponenten fuellen
     */

    CellConstraints cc = new CellConstraints();
    panelBenchmarkingClientGui.add(createSeparator("Eingabeparameter"), cc.xyw(1, 1, 7));
    panelBenchmarkingClientGui.add(new JLabel("Implementierungsstyp"), cc.xy(1, 3));
    panelBenchmarkingClientGui.add(optionListImplType, cc.xyw(3, 3, 1));

    panelBenchmarkingClientGui.add(new JLabel("Anzahl Client-Threads"), cc.xy(5, 3));
    panelBenchmarkingClientGui.add(textFieldNumberOfClientThreads, cc.xy(7, 3));
    textFieldNumberOfClientThreads.setText("1");

    panelBenchmarkingClientGui.add(new JLabel("Art der Messung"), cc.xy(1, 5));
    panelBenchmarkingClientGui.add(optionListMeasureType, cc.xyw(3, 5, 1));

    panelBenchmarkingClientGui.add(new JLabel(
	  "Anzahl Nachrichten je Client"), cc.xy(5, 5));
    panelBenchmarkingClientGui.add(textFieldNumberOfMessagesPerClients, cc.xy(7, 5));
    textFieldNumberOfMessagesPerClients.setText("10");

    panelBenchmarkingClientGui.add(new JLabel("Serverport"), cc.xy(1, 7));
    panelBenchmarkingClientGui.add(textFieldServerport, cc.xy(3, 7));
    textFieldServerport.setText("50000");

    panelBenchmarkingClientGui.add(new JLabel("Denkzeit in ms"), cc.xy(5, 7));
    panelBenchmarkingClientGui.add(textFieldThinkTime, cc.xy(7, 7));
    textFieldThinkTime.setText("100");

    panelBenchmarkingClientGui.add(new JLabel("Server-IP-Adresse"), cc.xy(1, 9));
    panelBenchmarkingClientGui.add(textFieldServerIpAddress, cc.xy(3, 9));
    textFieldServerIpAddress.setText("localhost");

    panelBenchmarkingClientGui.add(new JLabel("Nachrichtenlaenge in Byte"), cc.xy(5, 9));
    panelBenchmarkingClientGui.add(textFieldMessageLength, cc.xy(7, 9));
    textFieldMessageLength.setText("20");

    panelBenchmarkingClientGui
	  .add(new JLabel("Max. Anzahl Wiederholungen"), cc.xy(1, 11));
    panelBenchmarkingClientGui.add(textFieldNumberOfMaxRetries, cc.xy(3, 11));
    textFieldNumberOfMaxRetries.setText("1");

    panelBenchmarkingClientGui.add(new JLabel("Response-Timeout in ms"), cc.xy(5, 11));
    panelBenchmarkingClientGui.add(textFieldResponseTimeout, cc.xy(7, 11));
    textFieldResponseTimeout.setText("2000");

    panelBenchmarkingClientGui.add(createSeparator("Laufzeitdaten"), cc.xyw(1, 17, 7));
    panelBenchmarkingClientGui.add(new JLabel("Geplante Requests"), cc.xy(1, 19));
    panelBenchmarkingClientGui.add(textFieldPlannedRequests, cc.xy(3, 19));
    textFieldPlannedRequests.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel("Testbeginn"), cc.xy(5, 19));
    panelBenchmarkingClientGui.add(textFieldTestBegin, cc.xy(7, 19));
    textFieldTestBegin.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel("Gesendete Requests"), cc.xy(1, 21));
    panelBenchmarkingClientGui.add(textFieldSentRequests, cc.xy(3, 21));
    textFieldSentRequests.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel("Testende"), cc.xy(5, 21));
    panelBenchmarkingClientGui.add(textFieldTestEnd, cc.xy(7, 21));
    textFieldTestEnd.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel("Empfangene Responses"), cc.xy(1, 23));
    panelBenchmarkingClientGui.add(textFieldReceivedResponses, cc.xy(3, 23));
    textFieldReceivedResponses.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel("Testdauer in s"), cc.xy(5, 23));
    panelBenchmarkingClientGui.add(textFieldTestDuration, cc.xy(7, 23));
    textFieldTestDuration.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel("Max. geplante Event-Nachrichten"),
	  cc.xy(1, 26));
    panelBenchmarkingClientGui.add(textFieldPlannedEventMessages, cc.xy(3, 26));
    textFieldPlannedEventMessages.setEditable(false);

    panelBenchmarkingClientGui.add(
	  new JLabel("Vom Server gesendete Event-Nachrichten"), cc.xy(5, 26));
    panelBenchmarkingClientGui
	  .add(textFieldSentEventMessages, cc.xy(7, 26));
    textFieldSentEventMessages.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel(
	  "Vom Server empfangene Confirm-Nachrichten"), cc.xy(1, 28));
    panelBenchmarkingClientGui.add(textFieldReceivedConfirmEvents, cc.xy(3, 28));
    textFieldReceivedConfirmEvents.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel(
	  "Verlorene Confirm-Nachrichten"), cc.xy(5, 28));
    panelBenchmarkingClientGui.add(textFieldLostConfirmEvents, cc.xy(7, 28));
    textFieldLostConfirmEvents.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel(
	  "Wiederholte Event-Nachrichten"), cc.xy(1, 30));
    panelBenchmarkingClientGui.add(textFieldRetriedEvents, cc.xy(3, 30));
    textFieldRetriedEvents.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel(
	  "Anzahl aller Uebertragungswiederholungen"), cc.xy(5, 30));
    panelBenchmarkingClientGui.add(textFieldNumberOfRetries, cc.xy(7, 30));
    textFieldNumberOfRetries.setEditable(false);

    panelBenchmarkingClientGui.add(
	  createSeparator("Statistische Auswertung"), cc.xyw(1, 37, 7));

    panelBenchmarkingClientGui.add(new JLabel(
	  "Mittlere RTT (Arithm. Mittel) in ms"), cc.xy(1, 39));
    panelBenchmarkingClientGui.add(textFieldAvgRTT, cc.xy(3, 39));
    textFieldAvgRTT.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel(
	  "50%-Percentile (Median) in ms"), cc.xy(5, 39));
    panelBenchmarkingClientGui.add(textField50Percentile, cc.xy(7, 39));
    textField50Percentile.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel("Maximale RTT in ms"),
	  cc.xy(1, 41));
    panelBenchmarkingClientGui.add(textFieldMaxRTT, cc.xy(3, 41));
    textFieldMaxRTT.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel("Standardabweichung in ms"), cc.xy(5, 41));
    panelBenchmarkingClientGui.add(textFieldStandardDeviation, cc.xy(7, 41));
    textFieldAvgServerTime.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel("Minimale RTT in ms"), cc.xy(1, 43));
    panelBenchmarkingClientGui.add(textFieldMinRTT, cc.xy(3, 43));
    textFieldMinRTT.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel("Spannweite in ms"), cc.xy(5, 43));
    panelBenchmarkingClientGui.add(textFieldRange, cc.xy(7, 43));
    textFieldMaxHeapUsage.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel("10%-Percentile in ms"), cc.xy(1, 45));
    panelBenchmarkingClientGui.add(textField10Percentile, cc.xy(3, 45));
    textField10Percentile.setEditable(false);

    panelBenchmarkingClientGui
	  .add(new JLabel("Interquartilsabstand in ms"), cc.xy(5, 45));
    panelBenchmarkingClientGui.add(textFieldInterquartilRange, cc.xy(7, 45));
    textFieldAvgCpuUsage.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel("25%-Percentile in ms"),
	  cc.xy(1, 47));
    panelBenchmarkingClientGui.add(textField25Percentile, cc.xy(3, 47));
    textField25Percentile.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel("Mittlere Serverzeit in ms"),
	  cc.xy(5, 47));
    panelBenchmarkingClientGui.add(textFieldAvgServerTime, cc.xy(7, 47));
    textFieldAvgServerTime.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel("75%-Percentile in ms"),
	  cc.xy(1, 49));
    panelBenchmarkingClientGui.add(textField75Percentile, cc.xy(3, 49));
    textField75Percentile.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel(
	  "Maximale Heap-Belegung in MiB"), cc.xy(5, 49));
    panelBenchmarkingClientGui.add(textFieldMaxHeapUsage, cc.xy(7, 49));
    textFieldMaxHeapUsage.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel("90%-Percentile in ms"),
	  cc.xy(1, 51));
    panelBenchmarkingClientGui.add(textField90Percentile, cc.xy(3, 51));
    textField90Percentile.setEditable(false);

    panelBenchmarkingClientGui.add(new JLabel(
	  "Durchschnittliche CPU-Auslastung in %"), cc.xy(5, 51));
    panelBenchmarkingClientGui.add(textFieldAvgCpuUsage, cc.xy(7, 51));
    textFieldAvgCpuUsage.setEditable(false);

    // Ladebalken

    progressBar = new JProgressBar();
    progressBar.setMinimum(0);
    progressBar.setValue(0);
    progressBar.setStringPainted(true);

    panelBenchmarkingClientGui.add(progressBar, cc.xywh(1, 61, 7, 3));
    panelBenchmarkingClientGui.add(createSeparator(""), cc.xyw(1, 63, 7));

    // Meldungsbereich erzeugen
    panelBenchmarkingClientGui.add(scrollPane, cc.xyw(1, 65, 7));
    messageArea.setLineWrap(true);
    messageArea.setWrapStyleWord(true);
    messageArea.setEditable(false);
    messageArea.setCaretPosition(0);
    panelBenchmarkingClientGui.add(createSeparator(""), cc.xyw(1, 67, 7));

    // Buttons erzeugen

    // Test anhalten
    panelBenchmarkingClientGui.add(abortButton, cc.xyw(1, 69, 2));
    // Starten
    panelBenchmarkingClientGui.add(startButton, cc.xyw(2, 69, 2));
    // Loeschen
    panelBenchmarkingClientGui.add(newButton, cc.xyw(4, 69, 2));
    // Abbrechen
    panelBenchmarkingClientGui.add(finishButton, cc.xyw(6, 69, 2));

    // Listener fuer Buttons registrieren
    startButton.addActionListener(this);
    abortButton.addActionListener(this);
    newButton.addActionListener(this);
    finishButton.addActionListener(this);
    return panelBenchmarkingClientGui;
  }

  /**
   * actionPerformed Listener-Methode zur Bearbeitung der Button-Aktionen Reagiert auf das
   * Betaetigen eines Buttons
   *
   * @param e
   *          Ereignis
   */
  // @SuppressWarnings("deprecation")
  public void actionPerformed(ActionEvent e) {

    // Analysiere Ereignis und fuehre entsprechende Aktion aus

    if (e.getActionCommand().equals("Starten")) {
	startAction(e);
	startButton.setEnabled(false);
	newButton.setEnabled(false);
	abortButton.setEnabled(true);
	finishButton.setEnabled(false);

    } else if (e.getActionCommand().equals("Neu")) {

	if (!benchClient.isRunning()) {
	  newAction(e);
	  startButton.setEnabled(true);
	  abortButton.setEnabled(false);
	  finishButton.setEnabled(true);
	} else {
	  setMessageLine("Einrichten eines neuen Tests ist bei laufendem Test nicht moeglich");
	}
    } else if (e.getActionCommand().equals("Beenden")) {
	finishAction(e);
    } else if (e.getActionCommand().equals("Test abbrechen")) {
	if (benchClient.isRunning()) {
	  abortAction(e);
	  abortButton.setEnabled(false);
	  startButton.setEnabled(false);
	  newButton.setEnabled(true);
	  finishButton.setEnabled(true);
	} else {
	  setMessageLine("Derzeit laeuft kein Test");
	  abortButton.setEnabled(false);
	  startButton.setEnabled(false);
	  newButton.setEnabled(true);
	  finishButton.setEnabled(true);

	}
    }
  }

  /**
   * Aktion bei Betaetigung des "Start"-Buttons ausfuehren Eingabefelder werden validiert.
   *
   * @param e
   *          Ereignis
   */
  private void startAction(ActionEvent e) {
    // Input-Parameter aus GUI lesen
    UserInterfaceInputParameters iParm = new UserInterfaceInputParameters();

    String testString;

    /*
     * GUI sammmelt Eingabedaten und validieren
     */

    // Validierung fuer Denkzeit
    testString = textFieldThinkTime.getText();
    if (!testString.matches("[0-9]+")) {
	// nicht numerisch
	// Aktualisieren des Frames auf dem Bildschirm
	setMessageLine("Denkzeit bitte numerisch angeben");
	frameBenchmarkingGui.update(frameBenchmarkingGui.getGraphics());
	return;
    } else {
	Integer iThinkTime = new Integer(textFieldThinkTime.getText());
	System.out.println("Denkzeit: " + iThinkTime + " ms");
	iParm.setClientThinkTime(iThinkTime.intValue());
    }

    // Validierung fuer Serverport
    testString = textFieldServerport.getText();
    if (testString.matches("[0-9]+")) {
	Integer iServerPort = new Integer(textFieldServerport.getText());
	if ((iServerPort < 1) || (iServerPort > 65535)) {
	  // nicht im Wertebereich
	  // Aktualisieren des Frames auf dem Bildschirm
	  setMessageLine("Serverport im Bereich von 1 bis 65535 angeben");
	  frameBenchmarkingGui.update(frameBenchmarkingGui.getGraphics());
	  return;
	} else {
	  System.out.println("Serverport: " + iServerPort);
	  iParm.setRemoteServerPort(iServerPort.intValue());
	}
    } else {
	setMessageLine("Serverport nicht numerisch");
	frameBenchmarkingGui.update(frameBenchmarkingGui.getGraphics());
	return;
    }

    // Validierung fuer Anzahl Client-Threads
    testString = textFieldNumberOfClientThreads.getText();
    if (testString.matches("[0-9]+")) {
	Integer iClientThreads = new Integer(
	    textFieldNumberOfClientThreads.getText());
	if (iClientThreads < 1) {
	  // nicht im Wertebereich
	  // Aktualisieren des Frames auf dem Bildschirm
	  setMessageLine("Anzahl Client-Threads bitte groesser als 0 angeben");
	  frameBenchmarkingGui.update(frameBenchmarkingGui.getGraphics());
	  return;
	} else {
	  System.out.println("Anzahl Client-Threads:" + iClientThreads);
	  iParm.setNumberOfClients(iClientThreads.intValue());
	}
    } else {
	setMessageLine("Anzahl Client-Threads nicht numerisch");
	frameBenchmarkingGui.update(frameBenchmarkingGui.getGraphics());
	return;
    }

    // Validierung fuer Anzahl Nachrichten
    testString = textFieldNumberOfMessagesPerClients.getText();
    if (testString.matches("[0-9]+")) {
	Integer iNumberOfMessages = new Integer(
	    textFieldNumberOfMessagesPerClients.getText());
	if (iNumberOfMessages < 1) {
	  // nicht numerisch
	  // Aktualisieren des Frames auf dem Bildschirm
	  setMessageLine("Anzahl Nachrichten groesser als 0 angeben");
	  frameBenchmarkingGui.update(frameBenchmarkingGui.getGraphics());
	  return;
	} else {
	  System.out.println("Anzahl Nachrichten:" + iNumberOfMessages);
	  iParm.setNumberOfMessages(iNumberOfMessages.intValue());
	}
    } else {
	setMessageLine("Anzahl Nachrichten nicht numerisch");
	frameBenchmarkingGui.update(frameBenchmarkingGui.getGraphics());
	return;
    }

    // Validierung fuer Nachrichtenlaenge
    testString = textFieldMessageLength.getText();
    if (testString.matches("[0-9]+")) {
	Integer iMessageLength = new Integer(
	    textFieldMessageLength.getText());
	if ((iMessageLength < 1) || (iMessageLength > 50000)) {
	  // nicht im Wertebereich
	  // Aktualisieren des Frames auf dem Bildschirm
	  setMessageLine("Nachrichtenlaenge bitte im Bereich von 1 bis 50000 angeben");
	  frameBenchmarkingGui.update(frameBenchmarkingGui.getGraphics());
	  return;
	} else {
	  System.out.println("Nachrichtenlaenge:" + iMessageLength
		+ " Byte");
	  iParm.setMessageLength(iMessageLength.intValue());
	}
    } else {
	setMessageLine("Nachrichtenlaenge nicht numerisch");
	frameBenchmarkingGui.update(frameBenchmarkingGui.getGraphics());
	return;
    }

    System.out.println("RemoteServerAdress:"
	  + textFieldServerIpAddress.getText());
    iParm.setRemoteServerAddress(textFieldServerIpAddress.getText());

    // Validierung fuer Response-Timeout
    testString = textFieldResponseTimeout.getText();
    if (!testString.matches("[0-9]+")) {
	// nicht numerisch
	// Aktualisieren des Frames auf dem Bildschirm
	setMessageLine("Response-Timeout nicht numerisch");
	frameBenchmarkingGui.update(frameBenchmarkingGui.getGraphics());
	return;
    } else {
	Integer iResponseTimeout = new Integer(
	    textFieldResponseTimeout.getText());
	System.out.println("Response-Timeout:" + iResponseTimeout);
	iParm.setResponseTimeout(iResponseTimeout.intValue());
    }

    // Validierung fuer maximalen Nachrichtenwiederholung
    testString = textFieldNumberOfMaxRetries.getText();
    if (!testString.matches("[0-9]+")) {
	// nicht numerisch
	// Aktualisieren des Frames auf dem Bildschirm
	setMessageLine("Maximale Anzahl Wiederholungen nicht numerisch");
	frameBenchmarkingGui.update(frameBenchmarkingGui.getGraphics());
	return;
    } else {
	Integer iNumberOfMaxRetries = new Integer(
	    textFieldNumberOfMaxRetries.getText());
	System.out.println("Maximale Anzahl Wiederholungen:"
	    + iNumberOfMaxRetries);
	iParm.setNumberOfRetries(iNumberOfMaxRetries.intValue());
    }

    /*
     * Benchmarking-Client instanziieren und Benchmark starten
     */

    // Eingegebenen Implementierungstyp auslesen
    String item1 = (String) optionListImplType.getSelectedItem();
    System.out.println("Implementierungstyp eingegeben: " + item1);
    if (item1.equals(IMPL_TCP_ADVANCED))
	iParm.setImplementationType(ImplementationType.TCPAdvancedImplementation);
    if (item1.equals(IMPL_TCP_SIMPLE))
	iParm.setImplementationType(ImplementationType.TCPSimpleImplementation);
    if (item1.equals(IMPL_UDP_ADVANCED))
	iParm.setImplementationType(ImplementationType.UDPAdvancedImplementation);

    // Eingegebenen Messungstyp auslesen
    String item2 = (String) optionListMeasureType.getSelectedItem();
    System.out.println("Messungstyp eingegeben: " + item2);

    if (item1.equals("Variable Threads"))
	iParm.setMeasurementType(MeasurementType.VarThreads);
    if (item1.equals("Variable Length"))
	iParm.setMeasurementType(MeasurementType.VarMsgLength);

    // Aufruf des Benchmarks
    benchClient = new BenchmarkingClientCoordinator();
    benchClient.executeTest(iParm, this);
  }

  /**
   * Aktion bei Betaetigung des "Neu"-Buttons ausfuehren
   *
   * @param e
   *          Ereignis
   */
  private void newAction(ActionEvent e) {
    /*
     * Loeschen bzw. initialisieren der Ausgabefelder
     */
    textFieldSeperator.setText("");
    textFieldPlannedRequests.setText("");
    textFieldTestBegin.setText("");
    textFieldSentRequests.setText("");
    textFieldTestEnd.setText("");
    textFieldReceivedResponses.setText("");
    textFieldTestDuration.setText("");
    textFieldAvgRTT.setText("");
    textFieldAvgServerTime.setText("");
    textFieldMaxRTT.setText("");
    textFieldAvgCpuUsage.setText("");
    textFieldMaxHeapUsage.setText("");
    textFieldMinRTT.setText("");
    textField10Percentile.setText("");
    textField25Percentile.setText("");
    textField50Percentile.setText("");
    textField75Percentile.setText("");
    textField90Percentile.setText("");
    textFieldRange.setText("");
    textFieldStandardDeviation.setText("");
    textFieldInterquartilRange.setText("");
    textFieldNumberOfRetries.setText("");
    textFieldPlannedEventMessages.setText("");
    textFieldSentEventMessages.setText("");
    textFieldReceivedConfirmEvents.setText("");
    textFieldLostConfirmEvents.setText("");
    textFieldRetriedEvents.setText("");
    getProgressBar().setValue(0);
  }

  /**
   * Aktion bei Betaetigung des "Beenden"-Buttons ausfuehren
   *
   * @param e
   *          Ereignis
   */
  private void finishAction(ActionEvent e) {
    setMessageLine("Programm wird beendet...");

    // Programm beenden
    System.exit(0);
  }

  /**
   * Aktion bei Betaetigung des "Abbrechen"-Buttons ausfuehren
   *
   * @param e
   *          Ereignis
   */
  private void abortAction(ActionEvent e) {

    benchClient.abortTest();
    setMessageLine("Aktueller Testlauf wird abgebrochen ...");
    try {
	benchClient.join();
    } catch (Exception exception) {
    }
    setMessageLine("Aktueller Testlauf beendet");

    // Neue Testlaeufge wieder zulassen
    benchClient.releaseTest();
  }

  /**
   * Schliessen des Fensters und Beenden des Programms
   *
   * @param e
   */
  public void windowClosing(WindowEvent e) {
    System.exit(0);
  }

  public void windowOpened(WindowEvent e) {
  }

  public void windowActivated(WindowEvent e) {
  }

  public void windowIconified(WindowEvent e) {
  }

  public void windowDeiconified(WindowEvent e) {
  }

  public void windowDeactivated(WindowEvent e) {
  }

  public void windowClosed(WindowEvent e) {
  }

  @Override
  public synchronized void showStartData(UserInterfaceStartData data) {
    String strNumberOfRequests = (new Long(data.getNumberOfRequests()))
	  .toString();
    textFieldPlannedRequests.setText(strNumberOfRequests);

    String strNumberOfPlannedEventMessages = (new Long(
	  data.getNumberOfPlannedEventMessages())).toString();
    textFieldPlannedEventMessages.setText(strNumberOfPlannedEventMessages);

    textFieldTestBegin.setText(data.getStartTime());
    // Aktualisieren der Ausgabefelder auf dem Bildschirm
    textFieldPlannedRequests.update(textFieldPlannedRequests.getGraphics());
    textFieldTestBegin.update(textFieldTestBegin.getGraphics());
    textFieldPlannedEventMessages.update(textFieldPlannedEventMessages
	  .getGraphics());
  }

  @Override
  public synchronized void showResultData(UserInterfaceResultData data) {

    textFieldSentRequests
	  .setText((new Long(data.getNumberOfSentRequests())).toString());

    textFieldTestEnd.setText(data.getEndTime());

    textFieldReceivedResponses.setText((new Long(data
	  .getNumberOfResponses())).toString());
    textFieldMaxHeapUsage.setText((new Long(data.getMaxHeapSize()))
	  .toString());
    textFieldNumberOfRetries.setText((new Long(data.getNumberOfRetries()))
	  .toString());

    formatter = new Formatter();
    textFieldAvgRTT.setText(formatter.format("%.2f",
	  (new Double(data.getMean()))).toString());
    formatter = new Formatter();
    textFieldAvgServerTime.setText(formatter.format("%.2f",
	  (new Double(data.getAvgServerTime()))).toString());
    formatter = new Formatter();
    textFieldMaxRTT.setText(formatter.format("%.2f",
	  (new Double(data.getMaximum()))).toString());
    formatter = new Formatter();
    textFieldMinRTT.setText(formatter.format("%.2f",
	  (new Double(data.getMinimum()))).toString());
    formatter = new Formatter();
    textField10Percentile.setText(formatter.format("%.2f",
	  (new Double(data.getPercentile10()))).toString());
    formatter = new Formatter();
    textField25Percentile.setText(formatter.format("%.2f",
	  (new Double(data.getPercentile25()))).toString());
    formatter = new Formatter();
    textField50Percentile.setText(formatter.format("%.2f",
	  (new Double(data.getPercentile50()))).toString());
    formatter = new Formatter();
    textField75Percentile.setText(formatter.format("%.2f",
	  (new Double(data.getPercentile75()))).toString());
    formatter = new Formatter();
    textField90Percentile.setText(formatter.format("%.2f",
	  (new Double(data.getPercentile90()))).toString());
    formatter = new Formatter();
    textFieldRange.setText(formatter.format("%.2f",
	  (new Double(data.getRange()))).toString());
    formatter = new Formatter();
    textFieldInterquartilRange.setText(formatter.format("%.2f",
	  (new Double(data.getInterquartilRange()))).toString());
    formatter = new Formatter();
    textFieldStandardDeviation.setText(formatter.format("%.2f",
	  (new Double(data.getStandardDeviation()))).toString());
    formatter = new Formatter();
    textFieldAvgCpuUsage.setText(formatter.format("%.2f",
	  (new Double(data.getMaxCpuUsage() * 100))).toString());
    textFieldSentEventMessages.setText((new Long(data
	  .getNumberOfSentEventMessages())).toString());
    textFieldReceivedConfirmEvents.setText((new Long(data
	  .getNumberOfReceivedConfirmEvents())).toString());
    textFieldLostConfirmEvents.setText((new Long(data
	  .getNumberOfLostConfirmEvents())).toString());
    textFieldRetriedEvents.setText((new Long(data
	  .getNumberOfRetriedEvents())).toString());

    // Aktualisieren des Frames auf dem Bildschirm
    frameBenchmarkingGui.update(frameBenchmarkingGui.getGraphics());
  }

  @Override
  public synchronized void testFinished() {
    abortButton.setEnabled(false);
    startButton.setEnabled(false);
    newButton.setEnabled(true);
    finishButton.setEnabled(true);
  }

  @Override
  public synchronized void setMessageLine(String message) {
    // messageArea.append(message + "\n");
    // messageArea.update(messageArea.getGraphics());
    // messageArea.setCaretPosition(messageArea.getText().length());
  }

  @Override
  public synchronized void resetCurrentRunTime() {
    timeCounter = 0;
    String strTimeCounter = (new Long(timeCounter)).toString();
    textFieldTestDuration.setText(strTimeCounter);

    // Aktualisieren des Ausgabefeldes auf dem Bildschirm
    textFieldTestDuration.update(textFieldTestDuration.getGraphics());
  }

  @Override
  public synchronized void addCurrentRunTime(long sec) {
    timeCounter += sec;
    String strTimeCounter = (new Long(timeCounter)).toString();
    textFieldTestDuration.setText(strTimeCounter);

    // Aktualisieren des Ausgabefeldes auf dem Bildschirm

    textFieldTestDuration.update(textFieldTestDuration.getGraphics());
  }

  @Override
  public JProgressBar getProgressBar() {
    return progressBar;
  }

  @Override
  public ProgressBar getProgressBarFx() {
    return null;
  }

  @Override
  public void countUpProgressTask() {
    getProgressBar().setValue(getProgressBar().getValue() + 1);
  }

  private Component createSeparator(String text) {
    return DefaultComponentFactory.getInstance().createSeparator(text);
  }

  public static void main(String[] args) {

    PropertyConfigurator.configureAndWatch("log4j.client.properties",
	  60 * 1000);

    try {
	// UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
	UIManager.setLookAndFeel(new NimbusLookAndFeel());

	// Wird benoetigt um Ladebalken gruen darzustellen, ueberschreibt
	// aber das orange in der kompletten GUI mit gruen
	UIManager.getLookAndFeelDefaults().put("nimbusOrange", Color.GREEN);
    } catch (Exception e) {
	// Likely PlasticXP is not in the class path; ignore.
    }

    frameBenchmarkingGui = new JFrame("Benchmarking Client GUI");
    frameBenchmarkingGui.setTitle("Benchmark");
    frameBenchmarkingGui.add(new BenchmarkingClientGuiSwing());
    frameBenchmarkingGui
	  .setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    JComponent panel = new BenchmarkingClientGuiSwing().buildPanel();
    frameBenchmarkingGui.getContentPane().add(panel);
    frameBenchmarkingGui.pack();
    frameBenchmarkingGui.setMinimumSize(frameBenchmarkingGui.getSize());
    frameBenchmarkingGui.setVisible(true);

  }

  class MyPainter implements Painter<JProgressBar> {

    private final Color color;

    public MyPainter(Color c1) {
	this.color = c1;
    }

    @Override
    public void paint(Graphics2D gd, JProgressBar t, int width, int height) {
	gd.setColor(color);
	gd.fillRect(0, 0, width, height);
    }
  }
}