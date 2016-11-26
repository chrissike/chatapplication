package edu.hm.dako.chat.server.datasink.repo;

import edu.hm.dako.chat.server.datasink.model.CountEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class CountRepository {
    
	private static final String PERSISTENCE_UNIT_NAME = "countPersistence";

	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME, type = PersistenceContextType.TRANSACTION)
	EntityManager em;

	public void addCount(CountEntity count) {
		count.setId(null);
		em.persist(count);
	}

	public void updateCount(CountEntity count) {
		em.persist(count);
	}
	
	public void removeCount(Integer id) {
		CountEntity count = em.find(CountEntity.class, id);
		em.remove(count);
	}

	public void deleteAllCount() {
		em.createQuery("DELETE FROM CountEntity").executeUpdate();
	}

	public List<CountEntity> getAllCount() {
		TypedQuery<CountEntity> query = em.createNamedQuery("CountEntity.findAll", CountEntity.class);
		return query.getResultList();
	}

	public List<CountEntity> getCountByClientname(String nameOfClients) {
		TypedQuery<CountEntity> query = em.createNamedQuery("CountEntity.findByName", CountEntity.class);
		query.setParameter("nameOfClients", nameOfClients);
		return query.getResultList();
	}
}