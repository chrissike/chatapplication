package edu.hm.dako.chat.server.datasink;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import edu.hm.dako.chat.server.datasink.model.CountEntity;

import java.util.List;

@Stateless
public class CountRepository {
	private static final String PERSISTENCE_UNIT_NAME = "countPersistence";
	private EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME,
			DBConfig.getPersistConfig(Database.countdb));
	private EntityManager em = factory.createEntityManager();

	public void addCount(CountEntity count) {
		count.setId(null);
		em.getTransaction().begin();
		em.persist(count);
		em.getTransaction().commit();
	}

	public void removeCount(Integer id) {
		CountEntity count = em.find(CountEntity.class, id);
		em.getTransaction().begin();
		em.remove(count);
		em.getTransaction().commit();
	}

	public List<CountEntity> showCount() {
		TypedQuery<CountEntity> query = em.createNamedQuery("CountEntity.findAll", CountEntity.class);
		return query.getResultList();
	}
}