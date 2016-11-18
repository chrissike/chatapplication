package edu.hm.dako.chat.server.datasink.repo;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

import edu.hm.dako.chat.server.datasink.model.TraceEntity;

import java.util.List;

@Stateless
public class TraceRepository {

	private static final String PERSISTENCE_UNIT_NAME = "tracePersistence";

	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME, type = PersistenceContextType.TRANSACTION)
	EntityManager em;

	public void addTrace(TraceEntity trace) {
		trace.setId(null);
		em.persist(trace);
		em.flush();
	}

	public void removeTrace(Integer id) {
		TraceEntity trace = em.find(TraceEntity.class, id);
		em.remove(trace);
		em.flush();
	}

	public List<TraceEntity> showTrace() {
		TypedQuery<TraceEntity> query = em.createNamedQuery("TraceEntity.findAll", TraceEntity.class);
		return query.getResultList();
	}

}