package edu.hm.dako.chat.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagingHandlerImpl implements MessagingHandler {

	private static final Logger log = LoggerFactory.getLogger(MessagingHandlerImpl.class);


	private static final String USER_RESOURCE = "chatapp/resources/user";
	private Client restClient;
	private URI uri;

	/**
	 * Erzeugt eine neue Instanz des REST-Clients.
	 *
	 * @param uriToEndpoint
	 *            String, URI zum Endpunkt.
	 * @param serverPort
	 * @throws URISyntaxException
	 */
	public MessagingHandlerImpl(final String uriToEndpoint, Integer serverPort) throws URISyntaxException, TechnicalRestException {
		this.uri = new URI("http://" + uriToEndpoint + ":" + serverPort);
		this.restClient = ClientBuilder.newClient();
	}

	public Boolean login(final String anmeldename) {
		if (anmeldename == null) {
			return false;
		}

		Boolean result = false;
		try {
			final Response response = this.restClient.target(uri)
					.path(USER_RESOURCE).path("login").path(anmeldename)
					.request(MediaType.APPLICATION_JSON).get();
			if (Status.NOT_FOUND.getStatusCode() == response.getStatus()) {
				throw new Exception("Sie sind nicht eingeloggt.");
			}
			result = handleResponse(response, Status.OK);
		} catch (final Throwable th) {
			handleTechnicalException(th);
		}
		return result;
	}

	public Boolean logout(String anmeldename) throws TechnicalRestException {
		if (anmeldename == null) {
			return false;
		}

		Boolean result = false;
		try {
			final Response response = this.restClient.target(uri).path(USER_RESOURCE)
					.path("logout").path(anmeldename)
					.request(MediaType.APPLICATION_JSON).get();
			result = handleResponse(response, Status.NO_CONTENT);
		} catch (final Throwable th) {
			handleTechnicalException(th);
		}
		return result;
	}

	private Boolean handleResponse(final Response response, final Status expectedStatus)
			throws TechnicalRestException {
		Validate.notNull(response);
		Validate.notNull(expectedStatus);

		if (expectedStatus.getStatusCode() == response.getStatus()) {
			return true;
		} else if (Status.BAD_REQUEST.getStatusCode() == response.getStatus()) {
			final ErrorItem errorItem = errorItem(response);
			if (errorItem != null) {
				handleResponseWithErrorItem(errorItem);
			}
		}
		throw new TechnicalRestException(response.getStatusInfo().getReasonPhrase());
	}

	private ErrorItem errorItem(final Response response) {
		return response.hasEntity() ? response.readEntity(ErrorItem.class) : null;
	}

	private void handleResponseWithErrorItem(final ErrorItem errorItem) {
		final String errorMessage = errorMessageFromErrorItem(errorItem);
		log.warn(errorMessage);
		if (ErrorType.VALIDATION_ERROR.equals(errorItem.getErrorType())) {
			throw new TechnicalRestException(errorMessage);
		} else {
			throw new NonTechnicalException(errorMessage);
		}
	}

	private String errorMessageFromErrorItem(final ErrorItem errorItem) {
		return StringUtils.join(errorItem.getErrorMessages(), ", ");
	}

	private void handleTechnicalException(final Throwable th) throws TechnicalRestException {
		log.error("Es ist ein technischer Fehler aufgetreten", th);
		throw new TechnicalRestException(th.getMessage());
	}

}