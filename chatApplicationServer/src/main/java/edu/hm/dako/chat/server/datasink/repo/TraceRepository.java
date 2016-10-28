package edu.hm.dako.chat.server.datasink.repo;

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
//	private EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME,
//			DBConfig.getPersistConfig(Database.tracedb, 3316));
	
	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME, type = PersistenceContextType.EXTENDED)
	EntityManager em;
//	private EntityManager em = factory.createEntityManager();

	public void addTrace(TraceEntity trace) {
		trace.setId(null);
		em.getTransaction().begin();
		em.persist(trace);
		em.getTransaction().commit();
	}

	public void removeTrace(Integer id) {
		TraceEntity trace = em.find(TraceEntity.class, id);
		em.getTransaction().begin();
		em.remove(trace);
		em.getTransaction().commit();
	}

	public List<TraceEntity> showTrace() {
		TypedQuery<TraceEntity> query = em.createNamedQuery("TraceEntity.findAll", TraceEntity.class);
		return query.getResultList();
	}

}