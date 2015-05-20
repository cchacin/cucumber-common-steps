/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.cchacin.cucumber.steps.example.app;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
