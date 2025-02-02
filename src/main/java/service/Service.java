package service;
import jakarta.persistence.EntityManager;
import tools.JPAUtil;

public class Service {
    protected EntityManager entityManager;

    public Service(){
        this.entityManager = JPAUtil.getEntityManager(); // ✅ Récupère l'instance unique
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
