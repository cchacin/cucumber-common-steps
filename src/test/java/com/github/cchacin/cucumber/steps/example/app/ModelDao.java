package com.github.cchacin.cucumber.steps.example.app;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Singleton
public class ModelDao {

    @PersistenceContext
    private EntityManager em;

    public List<Model> get() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Model> q = cb.createQuery(Model.class);
        Root<Model> c = q.from(Model.class);
        q.select(c);
        return em.createQuery(q).getResultList();
    }
}
