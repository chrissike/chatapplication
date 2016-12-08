package edu.hm.dako.chat.client.communication.rest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

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
