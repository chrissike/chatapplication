package edu.hm.dako.chat.server.datasink.repo;

import edu.hm.dako.chat.server.datasink.model.TraceEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class TraceRepository {

	private static final String PERSISTENCE_UNIT_NAME = "tracePersistence";

	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME, type = PersistenceContextType.TRANSACTION)
	EntityManager em;

	public void addTrace(TraceEntity trace) throws Exception {
		trace.setId(null);
		em.persist(trace);
	}

	public void removeTrace(Integer id) {
		TraceEntity trace = em.find(TraceEntity.class, id);
		em.remove(trace);
	}

	public List<TraceEntity> showTrace() {
		TypedQuery<TraceEntity> query = em.createNamedQuery("TraceEntity.findAll", TraceEntity.class);
		return query.getResultList();
	}

	public void deleteAllTrace() {
		em.createQuery("DELETE FROM TraceEntity").executeUpdate();
	}

}