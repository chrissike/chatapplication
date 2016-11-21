package edu.hm.dako.chat.server.datasink.model;

import javax.persistence.*;
import java.io.Serializable;


@NamedQuery(name = "TraceEntity.findAll", query = "SELECT b FROM TraceEntity b")
@Entity
public class TraceEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	//TODO raus nehmen? Serverseitig ist doch vollkommen egal, wie der Name des clientThreads ist!?
	private String clientThreadName;

	private String serverThreadNamen;

	private String nachricht;

	public TraceEntity() {
	}

	public TraceEntity(String clientThreadName, String serverThreadNamen, String nachricht) {
		this.clientThreadName = clientThreadName;
		this.serverThreadNamen = serverThreadNamen;
		this.nachricht = nachricht;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClientThreadName() {
		return clientThreadName;
	}

	public void setClientThreadName(String clientThreadName) {
		this.clientThreadName = clientThreadName;
	}

	public String getServerThreadNamen() {
		return serverThreadNamen;
	}

	public void setServerThreadNamen(String serverThreadNamen) {
		this.serverThreadNamen = serverThreadNamen;
	}

	public String getNachricht() {
		return nachricht;
	}

	public void setNachricht(String nachricht) {
		this.nachricht = nachricht;
	}

}