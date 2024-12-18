package service;

import entity.VehicleEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleServiceTest {


    private VehicleService vehicleService;
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        // Création de l'EntityManager pour la connexion à la base de données
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("LeTresBonCoin");
        entityManager = entityManagerFactory.createEntityManager();
        vehicleService = new VehicleService(entityManager);
    }

    @Test
    void getVehicleNameById() {
        // Récupération de la session Hibernate à partir de l'EntityManager
        VehicleEntity vehicle = vehicleService.getVehicleById(1);
        assertEquals("Corolla",vehicle.getModel().getModelName());
    }


}
