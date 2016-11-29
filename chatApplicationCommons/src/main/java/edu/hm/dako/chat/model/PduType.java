package edu.hm.dako.chat.model;

/**
 * Enumeration zur Definition der Chat-PDU-Typen
 */
public enum PduType {
	UNDEFINED(0, "Undefined"), 
	MESSAGE(7, "Chat-Message-Event"), 
	LOGIN(8, "Login-Event"), 
	LOGOUT(9, "Logout-Event");

	private final int id;
	private final String description;

	PduType(int id, String description) {
		this.id = id;
		this.description = description;
	}

	public static PduType getId(int id) {
		for (PduType e : values()) {
			if (e.getId() == id) {
				return e;
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return description;
	}
}