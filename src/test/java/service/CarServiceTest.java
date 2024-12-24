package service;

import entity.ClientEntity;
import entity.CommandEntity;
import entity.VehicleEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class CarServiceTest {

    ClientService client;
    ClientEntity clientEntity;

    @BeforeEach
    void setUp() {
        client = new ClientService();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllCars() {
        ClientEntity client = service.Test.getEntityManager().find(ClientEntity.class, 1);
        List<VehicleEntity> vehicles = client.getVehicles();
        vehicles.forEach(vehicle -> System.out.println(vehicle.getIdVehicle()));
    }


}
