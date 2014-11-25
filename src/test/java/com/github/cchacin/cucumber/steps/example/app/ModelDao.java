package com.github.cchacin.cucumber.steps.example.app;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Singleton
@Startup
public class ModelDao {

    @PersistenceContext
    EntityManager em;

    @PostConstruct
    public void init() {
        System.out.println(this.get());
    }

    public List<Model> get() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Model> q = cb.createQuery(Model.class);
        Root<Model> c = q.from(Model.class);
        q.select(c);
        return em.createQuery(q).getResultList();
    }
}
