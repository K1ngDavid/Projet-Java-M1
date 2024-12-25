package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Service {

    protected EntityManager entityManager;
    private EntityManagerFactory entityManagerFactory;

    public Service(){
        entityManagerFactory = Persistence.createEntityManagerFactory("LeTresBonCoin");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
