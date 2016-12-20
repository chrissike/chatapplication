package edu.hm.dako.chat.server.datasink.repo;

import edu.hm.dako.chat.server.datasink.model.CountEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

import java.util.List;

@Stateless
public class CountRepository {

	private static final String PERSISTENCE_UNIT_NAME = "countPersistence";

	@PersistenceContext(unitName = PERSISTENCE_UNIT_NAME, type = PersistenceContextType.TRANSACTION)
	EntityManager em;

	public void addCount(CountEntity count) throws Exception {
		count.setId(null);
		try {
			em.persist(count);
		} catch (Exception e) {
			throw new Exception("Ein Fehler ist während AddCount aufgetreten. ");
		}
	}

	public void updateCount(CountEntity count) throws Exception {
		try {
			em.merge(count);
		} catch (Exception e) {
			throw new Exception("Ein Fehler ist während UpdateCount aufgetreten. ");
		}
	}

//Erzeugt DEADLOCKS!
	// public synchronized Integer updateCountIfExists(String nameOfClient)
	// throws Exception {
	// Query query = em.createQuery("UPDATE CountEntity b SET b.messageCount =
	// b.messageCount + 1 WHERE b.nameOfClient = :nameOfClient");
	// query.setParameter("nameOfClient", nameOfClient);
	// return query.executeUpdate();
	// }

	public void removeCount(Integer id) throws Exception {
		CountEntity count = em.find(CountEntity.class, id);
		em.remove(count);
	}

	public void deleteAllCount() throws Exception {
		em.createQuery("DELETE FROM CountEntity").executeUpdate();
	}

	public List<CountEntity> getAllCount() throws Exception {
		TypedQuery<CountEntity> query = em.createNamedQuery("CountEntity.findAll", CountEntity.class);
		return query.getResultList();
	}

	public List<CountEntity> getCountByClientname(String nameOfClient) throws Exception {
		TypedQuery<CountEntity> query = em.createNamedQuery("CountEntity.findByName", CountEntity.class);
		query.setParameter("nameOfClient", nameOfClient);
		query.setMaxResults(1);
		query.setLockMode(LockModeType.PESSIMISTIC_READ);
		return query.getResultList();
	}
}