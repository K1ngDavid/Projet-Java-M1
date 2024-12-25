package service;

import entity.ClientEntity;
import entity.VehicleEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class VehicleService extends Service{



    public List<VehicleEntity> getAllVehicles(){
        String hql = "FROM VehicleEntity";
        TypedQuery<VehicleEntity> query = entityManager.createQuery(hql, VehicleEntity.class);
        return query.getResultList();
    }

    public VehicleEntity getVehicleById(int vehicleId) {
        // Requête HQL pour récupérer un véhicule en fonction de son ID
        String hql = "FROM VehicleEntity v WHERE v.idVehicle = :vehicleId";  // Filtrage par ID du véhicule

        // Création de la requête avec le paramètre dynamique
        TypedQuery<VehicleEntity> query = entityManager.createQuery(hql, VehicleEntity.class);
        query.setParameter("vehicleId", vehicleId);  // Passage du paramètre "vehicleId" à la requête

        // Retourner le résultat (un seul véhicule ou null si non trouvé)
        return query.getResultList().stream().findFirst().orElse(null);  // Utilisation de stream pour obtenir un seul élément ou null
    }

}
