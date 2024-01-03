package gr.hua.dit.aimodotes.demo.dao;

import gr.hua.dit.aimodotes.demo.entity.Aimodotis;
import gr.hua.dit.aimodotes.demo.entity.Secretary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SecretaryDAOImpl implements SecretaryDAO{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Secretary> getSecretaries() {
        TypedQuery<Secretary> query = entityManager.createQuery("from Secretary", Secretary.class);
        return query.getResultList();
    }

    @Override
    public Secretary getSecretary(Integer secretary_id) {
        return entityManager.find(Secretary.class,secretary_id);
    }

    @Override
    @Transactional
    public Secretary saveSecretary(Secretary secretary) {
        System.out.println("Secretary "+ secretary.getId());
        if (secretary.getId() == null) {
            entityManager.persist(secretary);
        } else {
            entityManager.merge(secretary);
        }
        return secretary;
    }

    @Override
    @Transactional
    public void deleteSecretary(Integer secretary_id) {
        System.out.println("Deleting secretary with id: " + secretary_id);
        entityManager.remove(entityManager.find(Secretary.class, secretary_id));
    }
}
