package edu.hm.dako.chat.server.ui;

import edu.hm.dako.chat.server.ServerStartData;

/**
 * Interface, das der ServerGUI bereitstellen muss
 * 
 * @author Paul Mandl
 */
public interface ChatServerGuiInterface {

	public void showStartData(ServerStartData data);

	public void incrNumberOfLoggedInClients();

	public void decrNumberOfLoggedInClients();

	public void incrNumberOfRequests();

}
