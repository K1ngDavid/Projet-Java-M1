package service;

import entity.MotorcycleEntity;
import entity.VehicleEntity;
import enumerations.PowerSource;
import enumerations.TransmissionType;
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


    @BeforeEach
    void setUp() {
        // Création de l'EntityManager pour la connexion à la base de données
        vehicleService = new VehicleService();
    }

    @Test
    void getVehicleNameById() {
        // Récupération de la session Hibernate à partir de l'EntityManager
        VehicleEntity vehicle = vehicleService.getVehicleById(1);
        System.out.println(vehicle);
        assertEquals("Corolla",vehicle.getModel().getModelName());
    }

    @Test
    void updateVehicule(){
        VehicleEntity vehicle = vehicleService.getVehicleById(1);
        vehicleService.getEntityManager().getTransaction().begin();
        vehicle.setNumberOfDoors(4);
        vehicle.setTransmissionType(TransmissionType.AUTOMATIC);
        vehicle.setVehiclePowerSource(PowerSource.DIESEL);
        vehicleService.getEntityManager().getTransaction().commit();

    }

    @Test
    void createMotorCycle(){
        MotorcycleEntity motorcycleEntity = new MotorcycleEntity();
        motorcycleEntity.setCountryOfOrigin("Germany");

        vehicleService.getEntityManager().getTransaction().begin();
        vehicleService.getEntityManager().persist(motorcycleEntity);
        vehicleService.getEntityManager().getTransaction().commit();
    }


}
