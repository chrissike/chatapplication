package edu.hm.dako.chat.server.datasink.model;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@NamedQuery(name="CountEntity.findAll", query="SELECT b FROM CountEntity b")
public class CountEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nameOfClients;

    private String messageCount;

    public CountEntity() {
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

	public String getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(String messageCount) {
		this.messageCount = messageCount;
	}

}