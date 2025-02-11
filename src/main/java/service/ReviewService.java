package service;

import entity.ReviewEntity;
import entity.VehicleEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ReviewService extends Service {

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
            throw e;  // Relancez l'exception pour la gestion par la couche appelante
        } finally {
            // Attention : Fermer l'entityManager ici n'est recommandé que si son cycle de vie est géré dans cette méthode.
            entityManager.close();
        }
    }

    public List<ReviewEntity> getReviewsByVehicle(VehicleEntity vehicle) {
        String jpql = "SELECT r FROM ReviewEntity r WHERE r.vehicle.idVehicle = :idVehicle";
        TypedQuery<ReviewEntity> query = entityManager.createQuery(jpql, ReviewEntity.class);
        query.setParameter("idVehicle", vehicle.getIdVehicle());
        return query.getResultList();
    }

    public boolean deleteReview(ReviewEntity review) {
        try {
            entityManager.getTransaction().begin();
            // Rattache l'entité si elle est détachée
            if (!entityManager.contains(review)) {
                review = entityManager.merge(review);
            }
            entityManager.remove(review);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
            throw e;
        }
        // Ne fermez pas l'EntityManager ici si celui-ci est partagé/géré par le conteneur
    }



}
