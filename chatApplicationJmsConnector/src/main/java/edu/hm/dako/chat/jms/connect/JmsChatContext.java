package edu.hm.dako.chat.jms.connect;

public class JmsChatContext {

	private String initialContextFactory = "org.jboss.naming.remote.client.InitialContextFactory";
	private String providerURL = "http-remoting://127.0.0.1:8089";
	private String securityUser = "guest";
	private String securityPassword = "guest";
	private Boolean ejbContext = true;
	private String urlPkgPrefixes = "org.jnp.interfaces";
	private String defaultConnectionFactory = "jms/HTTPConnectionFactory";
	private String topic = "jms/topic/chatresp2";
	private String queue = "jms/queue/chatreq2";

	/**
	 * Default Konstruktor. Es werden alle hinterlegten Standardwerte für eine
	 * lokale Umgebung verwendet.
	 */
	public JmsChatContext() {
	}

	/**
	 * Erzeuge einen Kontext in dem alle Standardwerte herangezogen, jedoch
	 * Adresse und Port selbst bestimmt werden. 
	 * 
	 * @param address - Als Adresse reicht die IP oder Domain ohne angabe von Protokoll und Port.
	 * @param port
	 */
	public JmsChatContext(String address, Integer port) {
		this.providerURL = "http-remoting://" + address + ":" + port;
	}

	/**
	 * Erzeuge einen Kontext in dem alle Standardwerte herangezogen, jedoch eine
	 * Adresse, der Port, die Credentials sowie Topic und Queue selbst bestimmt.
	 * werden.
	 * 
	 * @param address - Als Adresse reicht die IP oder Domain ohne angabe von Protokoll und Port.
	 * @param port
	 * @param username
	 * @param password
	 * @param topic
	 * @param queue
	 */
	public JmsChatContext(String address, Integer port, String username, String password, String topic, String queue) {
		this.providerURL = "http-remoting://" + address + ":" + port;
		this.securityUser = username;
		this.securityPassword = password;
		this.topic = topic;
		this.queue = queue;
	}

	/**
	 * Erzeuge einen eigenen JMS-Context.
	 * @param initialContextFactory
	 * @param providerUrl
	 * @param username
	 * @param password
	 * @param ejbContext
	 * @param urlPKGPrefixes
	 * @param defaultConnectionFactory
	 * @param topic
	 * @param queue
	 */
	public JmsChatContext(String initialContextFactory, String providerUrl, String username, String password,
			Boolean ejbContext, String urlPKGPrefixes, String defaultConnectionFactory, String topic, String queue) {
		this.initialContextFactory = initialContextFactory;
		this.providerURL = providerUrl;
		this.securityUser = username;
		this.securityPassword = password;
		this.ejbContext = ejbContext;
		this.urlPkgPrefixes = urlPKGPrefixes;
		this.defaultConnectionFactory = defaultConnectionFactory;
		this.topic = topic;
		this.queue = queue;
	}

	public String getInitialContextFactory() {
		return initialContextFactory;
	}

	public void setInitialContextFactory(String initialContextFactory) {
		this.initialContextFactory = initialContextFactory;
	}

	public String getProviderURL() {
		return providerURL;
	}

	public void setProviderURL(String providerURL) {
		this.providerURL = providerURL;
	}

	public String getSecurityUser() {
		return securityUser;
	}

	public void setSecurityUser(String securityUser) {
		this.securityUser = securityUser;
	}

	public String getSecurityPassword() {
		return securityPassword;
	}

	public void setSecurityPassword(String securityPassword) {
		this.securityPassword = securityPassword;
	}

	public Boolean getEjbContext() {
		return ejbContext;
	}

	public void setEjbContext(Boolean ejbContext) {
		this.ejbContext = ejbContext;
	}

	public String getUrlPkgPrefixes() {
		return urlPkgPrefixes;
	}

	public void setUrlPkgPrefixes(String urlPkgPrefixes) {
		this.urlPkgPrefixes = urlPkgPrefixes;
	}

	public String getDefaultConnectionFactory() {
		return defaultConnectionFactory;
	}

	public void setDefaultConnectionFactory(String defaultConnectionFactory) {
		this.defaultConnectionFactory = defaultConnectionFactory;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

}
