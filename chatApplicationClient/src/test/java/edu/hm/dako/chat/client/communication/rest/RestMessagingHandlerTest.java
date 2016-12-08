package edu.hm.dako.chat.client.communication.rest;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.hm.dako.chat.rest.MessagingHandler;
import edu.hm.dako.chat.rest.MessagingHandlerImpl;

public class RestMessagingHandlerTest {

	private static final String ADDRESS = "10.28.56.110";
	private static final Integer PORT = 8089;


	@Test
	public void testRestLoginAndLogout() throws Exception {
		MessagingHandler handler = new MessagingHandlerImpl(ADDRESS, PORT);
		String testUser = "HansWurst"+Math.random();
		assertTrue(handler.login(testUser));
		assertTrue(handler.logout(testUser));
	}
}
