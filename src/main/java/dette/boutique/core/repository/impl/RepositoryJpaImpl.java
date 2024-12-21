package dette.boutique.core.repository.impl;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.TypedQuery;

import dette.boutique.core.repository.Repository;
import jakarta.persistence.EntityManager;

public class RepositoryJpaImpl<T> implements Repository<T> {
    protected EntityManager em;
    protected Class<T> type;

    public RepositoryJpaImpl(Class<T> type, EntityManager em) {
        this.type = type;
        this.em = em;
    }

    @Override
    public void insert(T entity) {
        try {
            em.getTransaction().begin();

            // Vérifiez si l'entité a un identifiant
            Object id = em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);

            if (id != null  && !id.equals(0)) {
                // Si l'identifiant existe, MAJ
                em.merge(entity);
            } else {
                // Si l'entité est nouvelle Persist
                em.persist(entity);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    @Override
    public List<T> selectAll() {
        try {
            TypedQuery<T> query = em.createQuery("SELECT e FROM " + type.getSimpleName() + " e", type);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des entités : " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public T selectById(int id) {
        try {
            return em.find(type, id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void remove(T element) {
        try {
            em.getTransaction().begin();

            T managedElement = em.contains(element) ? element : em.merge(element);

            em.remove(managedElement);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

}