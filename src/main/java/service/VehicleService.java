package service;

import entity.ClientEntity;
import entity.VehicleEntity;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VehicleService extends Service {

    /**
     * Récupère tous les véhicules.
     *
     * @return Liste de tous les véhicules.
     */
    public List<VehicleEntity> getAllVehicles() {
        try {
            String hql = "FROM VehicleEntity";
            TypedQuery<VehicleEntity> query = entityManager.createQuery(hql, VehicleEntity.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Long> getSalesCountByVehicle() {
        try {
            String hql = "SELECT v.model.modelName, COUNT(cl) " +
                    "FROM CommandLineEntity cl " +
                    "JOIN cl.vehicle v " +
                    "JOIN cl.command c " +
                    "WHERE c.commandStatus = 'Payée' " +
                    "GROUP BY v.model.modelName";
            TypedQuery<Object[]> query = entityManager.createQuery(hql, Object[].class);
            List<Object[]> results = query.getResultList();
            Map<String, Long> salesCount = new HashMap<>();
            for (Object[] result : results) {
                String modelName = (String) result[0];
                Long count = (Long) result[1];
                salesCount.put(modelName, count);
            }
            return salesCount;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Récupère tous les véhicules apparaissant dans les commandes dont le statut est "Payée".
     * (Les doublons sont conservés, ce qui permet de compter les ventes par véhicule.)
     *
     * @return Liste des véhicules vendus.
     */
    public List<VehicleEntity> getAllPaidVehicles() {
        try {
            String hql = "SELECT cl.vehicle FROM CommandLineEntity cl JOIN cl.command c WHERE c.commandStatus = 'Payée'";
            TypedQuery<VehicleEntity> query = entityManager.createQuery(hql, VehicleEntity.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<VehicleEntity> getAllPaidVehiclesByClient(ClientEntity client) {
        try {
            String hql = "SELECT cl.vehicle FROM CommandLineEntity cl JOIN cl.command c WHERE c.commandStatus = 'Payée' AND cl.command.client = :client";
            TypedQuery<VehicleEntity> query = entityManager.createQuery(hql, VehicleEntity.class);
            query.setParameter("client",client);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * Récupère les véhicules uniques basés sur leur modèle.
     *
     * @return Liste de véhicules avec un modèle unique.
     */
    public List<VehicleEntity> getUniqueVehicles() {
        try {
            String hql = "SELECT v FROM VehicleEntity v GROUP BY v.model.idModel";
            TypedQuery<VehicleEntity> query = entityManager.createQuery(hql, VehicleEntity.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Récupère un véhicule par son ID.
     *
     * @param vehicleId ID du véhicule.
     * @return L'entité VehicleEntity trouvée ou null.
     */
    public VehicleEntity getVehicleById(int vehicleId) {
        try {
            return entityManager.find(VehicleEntity.class, vehicleId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 🔥 Récupère les véhicules achetés ou possédés par un client.
     *
     * @param client L'entité ClientEntity.
     * @return Liste des véhicules du client.
     */


    /**
     * Ajoute un nouveau véhicule à la base de données.
     *
     * @param vehicle L'entité VehicleEntity à ajouter.
     * @return true si l'ajout est réussi, false sinon.
     */
    public boolean addVehicle(VehicleEntity vehicle) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(vehicle);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }

    /**
     * Met à jour un véhicule existant.
     *
     * @param vehicle L'entité VehicleEntity mise à jour.
     * @return Le véhicule mis à jour.
     */
    public VehicleEntity updateVehicle(VehicleEntity vehicle) {
        try {
            entityManager.getTransaction().begin();
            VehicleEntity updatedVehicle = entityManager.merge(vehicle);
            entityManager.getTransaction().commit();
            return updatedVehicle;
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return null;
        }
    }

    /**
     * Supprime un véhicule par son ID.
     *
     * @param vehicleId ID du véhicule à supprimer.
     * @return true si la suppression a réussi, false sinon.
     */
    public boolean deleteVehicle(int vehicleId) {
        try {
            entityManager.getTransaction().begin();
            VehicleEntity vehicle = entityManager.find(VehicleEntity.class, vehicleId);
            if (vehicle != null) {
                entityManager.remove(vehicle);
                entityManager.getTransaction().commit();
                return true;
            }
            entityManager.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return false;
        }
    }
}
