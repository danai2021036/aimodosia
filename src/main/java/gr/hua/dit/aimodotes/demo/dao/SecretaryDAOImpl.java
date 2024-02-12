package gr.hua.dit.aimodotes.demo.dao;

import gr.hua.dit.aimodotes.demo.entity.AppForm;
import gr.hua.dit.aimodotes.demo.entity.Secretary;
import gr.hua.dit.aimodotes.demo.repository.AppFormRepository;
import gr.hua.dit.aimodotes.demo.repository.SecretaryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SecretaryDAOImpl implements SecretaryDAO{

    @Autowired
    private AppFormRepository appFormRepository;

    @PersistenceContext
    private EntityManager entityManager;

    //get all secretaries
    @Override
    @Transactional
    public List<Secretary> getSecretaries() {
        TypedQuery<Secretary> query = entityManager.createQuery("from Secretary", Secretary.class);
        return query.getResultList();
    }

    //get one secretary
    @Override
    public Secretary getSecretary(Integer secretary_id) {
        return entityManager.find(Secretary.class,secretary_id);
    }

    //save one secretary
    @Override
    @Transactional
    public Secretary saveSecretary(Secretary secretary) {
        if (secretary.getId() == null) {
            entityManager.persist(secretary);
        } else {
            entityManager.merge(secretary);
        }
        return secretary;
    }

    //delete one secretary
    @Override
    @Transactional
    public void deleteSecretary(Integer secretaryId) {

        Secretary secretary = entityManager.find(Secretary.class, secretaryId);
        if (secretary == null) {
            throw new IllegalArgumentException("Secretary with ID " + secretaryId + " not found.");
        }

        secretary.getAppForms().forEach(appForm -> appForm.setSecretary(null));
        secretary.getDonationRequests().forEach(donationRequest -> donationRequest.setSecretary(null));

        entityManager.remove(secretary);
    }

}
