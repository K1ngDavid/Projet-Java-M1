package service;

import entity.MotorcycleEntity;
import entity.VehicleEntity;
import enumerations.PowerSource;
import enumerations.TransmissionType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleServiceTest {

    private static VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        // ✅ Évite de recréer `VehicleService` à chaque test
        if (vehicleService == null) {
            vehicleService = new VehicleService();
        }
    }

    @Test
    void getVehicleNameById() {
        // 🔍 Récupération du véhicule
        VehicleEntity vehicle = vehicleService.getVehicleById(1);

        // ✅ Vérifications pour éviter les NullPointerException
        assertNotNull(vehicle, "Le véhicule avec l'ID 1 n'existe pas dans la base.");
        assertNotNull(vehicle.getModel(), "Le véhicule n'a pas de modèle associé.");

        // ✅ Vérifie que le modèle correspond à "Corolla"
        assertEquals("Corolla", vehicle.getModel().getModelName());
    }

//    @Test
//    void updateVehicule() {
//        // 🔍 Récupération du véhicule
//        VehicleEntity vehicle = vehicleService.getVehicleById(1);
//        assertNotNull(vehicle, "Le véhicule avec l'ID 1 n'existe pas.");
//
//        // ✅ Démarre la transaction
//        vehicleService.getEntityManager().getTransaction().begin();
//
//        // ✅ Modifications du véhicule
//        vehicle.setNumberOfDoors(4);
//        vehicle.setTransmissionType(TransmissionType.AUTOMATIC);
//        vehicle.setVehiclePowerSource(PowerSource.DIESEL);
//
//        // ✅ Commit la transaction
//        vehicleService.getEntityManager().getTransaction().commit();
//
//        // 🔍 Vérification après mise à jour
//        VehicleEntity updatedVehicle = vehicleService.getVehicleById(1);
//        assertEquals(4, updatedVehicle.getNumberOfDoors());
//        assertEquals(TransmissionType.AUTOMATIC, updatedVehicle.getTransmissionType());
//        assertEquals(PowerSource.DIESEL, updatedVehicle.getVehiclePowerSource());
//    }

//    @Test
//    void createMotorCycle() {
//        // 🔥 Création d'une nouvelle moto
//        MotorcycleEntity motorcycleEntity = new MotorcycleEntity();
//        motorcycleEntity.setCountryOfOrigin("Germany");
//        motorcycleEntity.setPrice(new BigDecimal("15000.00"));
//        motorcycleEntity.setHorsePower(100);
//        motorcycleEntity.setVehiclePowerSource(PowerSource.PETROL);
//        motorcycleEntity.setTransmissionType(TransmissionType.MANUAL);
//        motorcycleEntity.setNumberOfDoors(0); // Une moto n'a pas de portes
//
//        // ✅ Associe un modèle existant
//        motorcycleEntity.setModel(vehicleService.getModelById(1));
//
//        // ✅ Démarre et commit la transaction
//        vehicleService.getEntityManager().getTransaction().begin();
//        vehicleService.getEntityManager().persist(motorcycleEntity);
//        vehicleService.getEntityManager().getTransaction().commit();
//
//        // ✅ Vérifie que l'insertion a réussi
//        assertNotNull(motorcycleEntity.getIdVehicle(), "L'insertion de la moto a échoué.");
//    }
}
