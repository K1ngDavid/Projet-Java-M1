package service;

import entity.ReviewEntity;
import entity.VehicleEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ReviewService extends Service{

    public void saveReview(ReviewEntity review) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(review);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            throw e;  // Optionnel : relancez l'exception pour la gestion par la couche appelante
        } finally {
            entityManager.close();
        }
    }

    public List<ReviewEntity> getReviewsByVehicle(VehicleEntity vehicle){
        String hql = "SELECT ALL FROM ReviewEntity WHERE vehicle.idVehicle = :idVehicle";
        TypedQuery<ReviewEntity> query = entityManager.createQuery(hql, ReviewEntity.class);
        query.setParameter("idVehicle", vehicle.getIdVehicle());


        return query.getResultList();

    }
}
