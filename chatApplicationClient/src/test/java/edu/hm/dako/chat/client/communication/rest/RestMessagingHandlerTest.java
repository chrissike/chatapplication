package edu.hm.dako.chat.client.communication.rest;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RestMessagingHandlerTest {

	private static final String ADDRESS = "127.0.0.1";
	private static final Integer PORT = 8089;


	@Test
	public void testRestLogin() throws Exception {
		MessagingHandler handler = new MessagingHandlerImpl(ADDRESS, PORT);
		String testUser = "HansWurst"+Math.random();
		assertTrue(handler.login(testUser));
		assertTrue(handler.logout(testUser));
	}
}
