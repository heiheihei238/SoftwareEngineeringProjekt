package service;

import entities.Brand;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
public class BrandService {
    @PersistenceContext
    EntityManager em;

    public void remove(Brand brand) {
            em.remove(em.merge(brand));
            em.flush();
    }

    public void save(Brand brand) {
        em.persist(brand);
    }

    public List<Brand> findAll() {
        return em.createQuery("select b from Brand b", Brand.class).getResultList();
    }

    // pagination
    public List<Brand> findAll(int page, int size) {
        return em.createQuery("select b from Brand b", Brand.class)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }
}
