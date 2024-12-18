import entity.ClientEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import service.ClientService;

public class Main {

    static ClientService clientService;
    static EntityManager entityManager;
    public static void main(String[] args) {
        System.out.println("Hello World");
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("LeTresBonCoin");
        entityManager = entityManagerFactory.createEntityManager();
        clientService = new ClientService(entityManager);


//        // Crée un nouveau client
//        ClientEntity newClient = new ClientEntity();
//        newClient.setName("toto");
//        newClient.setEmail("da@example.com"); // Exemple d'email
//
//        // Ajoute le client via le service
//        clientService.addClient(newClient);


        // Affiche un message de confirmation
//        System.out.println("Client ajouté avec succès !");

        System.out.println(clientService.getAllClients());
        // Ferme l'EntityManager
        entityManager.close();

    }
}
