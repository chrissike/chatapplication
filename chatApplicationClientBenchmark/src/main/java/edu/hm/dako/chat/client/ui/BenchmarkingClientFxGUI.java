package edu.hm.dako.chat.client.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.client.data.ClientModel;
import edu.hm.dako.chat.client.data.ResultTableModel;
import edu.hm.dako.chat.common.ChatPDU;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Chat-GUI
 * 
 * @author Paul Mandl
 *
 */
@SuppressWarnings("restriction")
public class BenchmarkingClientFxGUI extends Application {

	private static Log log = LogFactory.getLog(BenchmarkingClientFxGUI.class);

	private ClientModel model = new ClientModel();
	public static BenchmarkingClientFxGUI instance;

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Diese Methode wird von Java FX bei Aufruf der launch-Methode implizit
	 * aufgerufen
	 */
	public void start(Stage primaryStage) throws Exception {

		BenchmarkingClientFxGUI.instance = this;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/BenchmarkingClient.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
		
		BenchmarkingGuiController lc = (BenchmarkingGuiController) loader.getController();
		lc.setAppController(this);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Benchmarking");
		primaryStage.setResizable(true);
		primaryStage.show();
	}

	public ClientModel getModel() {
		return model;
	}
	
	public void setModel(ClientModel clientModel) {
		this.model = clientModel;
	}

	public void showResults(ChatPDU chatPDU, Long rtt, Double rttMs, Double rttServerMs) {
		Platform.runLater(new Runnable() {
			public void run() {
				Integer userNameNumber = Integer.valueOf(chatPDU.getUserName());
				
				log.info("Client: " + userNameNumber + "ClientTime: " + rttMs + ", ServerTime: " + rttServerMs);
				
				//ChartBars
				getModel().addClientTime(userNameNumber, rttMs);
				getModel().addServerTime(userNameNumber, rttServerMs);
				
				//StockedBarChart
				getModel().setAnteilsChartClient((rtt.longValue() - chatPDU.getServerTime().longValue()) / 1000000000.0);
				getModel().setAnteilsChartServer(rttServerMs);
				
				//Table
				ResultTableModel resulttable = new ResultTableModel(chatPDU.getUserName(), "1", String.valueOf(rttMs), String.valueOf(rttServerMs));
				getModel().addToResultList(resulttable);
				
			}
		});
	}
}
