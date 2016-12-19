package edu.hm.dako.chat.server.datasink.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NamedQueries({
		@NamedQuery(name = "CountEntity.findAll", query = "SELECT b FROM CountEntity b"),
		@NamedQuery(name = "CountEntity.findByName", query = "SELECT b FROM CountEntity b WHERE b.nameOfClient = :nameOfClient")
})
@Table(uniqueConstraints=
           @UniqueConstraint(columnNames = {"nameOfClient"})) 
public class CountEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique=true)
	private String nameOfClient;

	private Integer messageCount;

	public CountEntity() {
	}

	public CountEntity(String nameOfClient, Integer messageCount) {
		this.nameOfClient = nameOfClient;
		this.messageCount = messageCount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNameOfClient() {
		return nameOfClient;
	}

	public void setNameOfClient(String nameOfClient) {
		this.nameOfClient = nameOfClient;
	}

	public Integer getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(Integer messageCount) {
		this.messageCount = messageCount;
	}

}