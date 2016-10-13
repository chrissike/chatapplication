package edu.hm.dako.chat.client.communication.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MessagingHandlerImpl implements MessagingHandler {

	private static Log LOG = LogFactory.getLog(MessagingHandlerImpl.class);

	private Client restClient;

	/**
	 * Erzeugt eine neue Instanz des REST-Clients.
	 *
	 * @param uriToEndpoint
	 *            String, URI zum Endpunkt.
	 * @throws URISyntaxException 
	 */
	public MessagingHandlerImpl(final String uriToEndpoint) throws URISyntaxException {
		URI uri = new URI(uriToEndpoint);
		buildNewClient(uri);
	}

	private void buildNewClient(URI uri) {
		this.restClient = ClientBuilder.newClient();
		this.restClient.target(uri);
	}


	public Boolean login(final String anmeldename, String resource, String uri) {
		if (anmeldename == null) {
			return false;
		}

		Boolean result = false;
		try {
			final Response response = this.restClient.target(uri).path(resource).path(anmeldename)
					.request(MediaType.APPLICATION_JSON).get();
			result = handleResponseContainingSingleExample(response, Status.OK);
		} catch (final Throwable th) {
			handleTechnicalException(th);
		}
		return result;
	}
	

	public Boolean logout(String anmeldename, String resource, String uri) {
		// TODO Max: LOGOUT schreiben
		return null;
	}

	private Boolean handleResponseContainingSingleExample(final Response response, final Status expectedStatus)
			throws Exception {
		Validate.notNull(response);
		Validate.notNull(expectedStatus);

		if (expectedStatus.getStatusCode() == response.getStatus()) {
			return response.hasEntity() ? true : null;
		} else if (Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
			final ErrorItem errorItem = errorItem(response);
			if (errorItem != null) {
				handleResponseWithErrorItem(errorItem);
			}
		} else if (Status.NOT_FOUND.getStatusCode() == response.getStatus()) {
			throw new Exception("Login war nicht möglich.");
		}

		throw new Exception();
	}

	private ErrorItem errorItem(final Response response) {
		return response.hasEntity() ? response.readEntity(ErrorItem.class) : null;
	}

	private void handleResponseWithErrorItem(final ErrorItem errorItem) {
		final String errorMessage = errorMessageFromErrorItem(errorItem);
		LOG.warn(errorMessage);
		if (ErrorType.VALIDATION_ERROR.equals(errorItem.getErrorType())) {
			throw new TechnicalException(errorMessage);
		} else {
			throw new NonTechnicalException(errorMessage);
		}
	}

	private String errorMessageFromErrorItem(final ErrorItem errorItem) {
		return StringUtils.join(errorItem.getErrorMessages(), ", ");
	}

	private void handleTechnicalException(final Throwable th) {
		LOG.error("Es ist ein technischer Fehler aufgetreten", th);
		throw new TechnicalException(th.getMessage());
	}

}