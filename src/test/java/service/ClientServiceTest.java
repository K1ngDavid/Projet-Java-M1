package service;

import entity.ClientEntity;
import entity.CommandEntity;
import entity.VehicleEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import repository.ClientRepository;


import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class ClientServiceTest extends Service {

    private ClientService clientService;

@BeforeEach
void setUp() {
    clientService = new ClientService();
}

@Test
void test(){
    System.out.println(clientService.getClientByEmail("david.roufe@gmail.com"));
}

@AfterEach
void tearDown() {
    // Nettoyage de
}




}
