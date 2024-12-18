package service;

import entity.ClientEntity;
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
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("LeTresBonCoin");
        entityManager = entityManagerFactory.createEntityManager();
        clientService = new ClientService(entityManager);

//        // Démarre une transaction pour chaque test
//        entityManager.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        // Nettoyage de la base de données après chaque test
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
        entityManager.close();
    }

    @Test
    void getClientByName() {
        // Ajouter un client pour les tests
        ClientEntity client = new ClientEntity();
        client.setName("Ima Frye");
        client.setEmail("ima.frye@example.com");

        entityManager.persist(client);

        // Tester la méthode getClientByName
        ClientEntity foundClient = clientService.getClientByName("Ima Frye");
        assertNotNull(foundClient);
        assertEquals("Ima Frye", foundClient.getName());
    }

    @Test
    void getAllClients() {
        // Ajouter quelques clients pour tester
        ClientEntity client1 = new ClientEntity();
        client1.setName("John Doe");
        client1.setEmail("john.doe@example.com");

        entityManager.persist(client1);

        ClientEntity client2 = new ClientEntity();
        client2.setName("Jane Doe");
        client2.setEmail("jane.doe@example.com");

        entityManager.persist(client2);

        // Tester la méthode getAllClients
        List<ClientEntity> clients = clientService.getAllClients();
        assertNotNull(clients);
        assertTrue(clients.size() > 1);  // On devrait avoir plus d'un client
    }

    @Test
    void getClientById() {
        // Ajouter un client pour les tests
        ClientEntity newClient = new ClientEntity();
        newClient.setName("Anna Smith");
        newClient.setEmail("anna.smith@example.com");

        clientService.addClient(newClient);

        // Tester la méthode getClientById
        ClientEntity clientById = clientService.getClientById(newClient.getIdClient());
        assertNotNull(clientById);
        assertEquals("Anna Smith", clientById.getName());
    }

    @Test
    void createClient() {
        // Créer un client

        ClientEntity newClient = new ClientEntity();
        newClient.setName("Anna Smith");
        newClient.setEmail("anna.smith@example.com");

        // Tester la méthode createClient
        boolean createdClient = clientService.addClient(newClient);
        assertTrue(createdClient);

    }

    @Test
    void updateClient() {
        // Créer un client
        ClientEntity existingClient = new ClientEntity();
        existingClient.setName("Old Name");
        existingClient.setEmail("old.name@example.com");
        entityManager.persist(existingClient);

        // Modifier le client
        existingClient.setName("Updated Name");
        existingClient.setEmail("updated.name@example.com");

        // Tester la méthode updateClient
        ClientEntity updatedClient = clientService.updateClient(existingClient);
        assertNotNull(updatedClient);
        assertEquals("Updated Name", updatedClient.getName());
        assertEquals("updated.name@example.com", updatedClient.getEmail());
    }

    @Test
    void deleteClient() {
        // Créer un client pour les tests
        ClientEntity clientToDelete = new ClientEntity();
        clientToDelete.setName("Delete Me");
        clientToDelete.setEmail("delete.me@example.com");
        clientService.addClient(clientToDelete);

        System.out.println("-----> " + clientService.getClientById(clientToDelete.getIdClient()));
        // Tester la méthode deleteClient
        boolean isDeleted = clientService.deleteClient(clientToDelete.getIdClient());
        assertTrue(isDeleted);

        // Vérifier que le client n'existe plus
        ClientEntity deletedClient = clientService.getClientById(clientToDelete.getIdClient());
        assertNull(deletedClient);
    }
}
