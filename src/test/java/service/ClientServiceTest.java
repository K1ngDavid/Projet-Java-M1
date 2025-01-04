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


import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class ClientServiceTest {

    private ClientService clientService;
    private EntityManager entityManager;

@BeforeEach
    void setUp() {
        // Création de l'EntityManager pour la connexion à la base de données
        clientService = new ClientService();

//        // Démarre une transaction pour chaque test
//        entityManager.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        // Nettoyage de
    }




}
