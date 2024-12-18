package service;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Test {

    static EntityManager entityManager;

    public static EntityManager getEntityManager() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("LeTresBonCoin");
        entityManager = entityManagerFactory.createEntityManager();
        return entityManager;
    }
}
