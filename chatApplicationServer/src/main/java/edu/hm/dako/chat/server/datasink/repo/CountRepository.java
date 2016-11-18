package edu.hm.dako.chat.server.datasink.repo;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

import edu.hm.dako.chat.server.datasink.model.CountEntity;

import java.util.List;

@Stateless
public class CountRepository {
	     
	private static final String PERSISTENCE_UNIT_NAME = "countPersistence";

	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME, type = PersistenceContextType.TRANSACTION)
	EntityManager em;

	public void addCount(CountEntity count) {
//		count.setId(null);
		em.persist(count);
//		em.flush();
	}

	public void removeCount(Integer id) {
		CountEntity count = em.find(CountEntity.class, id);
		em.remove(count);
//		em.flush();
	}

	public List<CountEntity> getAllCount() {
		TypedQuery<CountEntity> query = em.createNamedQuery("CountEntity.findAll", CountEntity.class);
		return query.getResultList();
	}

	public List<CountEntity> getCountByClientname() {
		TypedQuery<CountEntity> query = em.createNamedQuery("CountEntity.findByName", CountEntity.class);
//		return query.getResultList();
		return null;
	}
}