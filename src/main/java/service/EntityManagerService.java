package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class EntityManagerService {

    private static EntityManager em;

    public EntityManagerService(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("LeTresBonCoin");
        em = emf.createEntityManager();
    }

    private boolean checkIfInTransaction(){
        if(!em.getTransaction().isActive()){
            em.getTransaction().begin();
        }
        return false;
    }

    public static EntityManager getEm() {
        return em;
    }
}
