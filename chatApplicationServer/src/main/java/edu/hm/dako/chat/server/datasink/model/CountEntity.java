package edu.hm.dako.chat.server.datasink.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NamedQueries({
		@NamedQuery(name = "CountEntity.findAll", query = "SELECT b FROM CountEntity b"),
		@NamedQuery(name = "CountEntity.findByName", query = "SELECT b FROM CountEntity b WHERE b.nameOfClients = :nameOfClients")
})
public class CountEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nameOfClients;

	private Integer messageCount;

	public CountEntity() {
	}

	public CountEntity(String nameOfClients, Integer messageCount) {
		this.nameOfClients = nameOfClients;
		this.messageCount = messageCount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNameOfClients() {
		return nameOfClients;
	}

	public void setNameOfClients(String nameOfClients) {
		this.nameOfClients = nameOfClients;
	}

	public Integer getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(Integer messageCount) {
		this.messageCount = messageCount;
	}

}