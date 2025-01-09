import entity.ClientEntity;
import entity.VehicleEntity;
import frames.AccountForm;
import frames.HomeForm;
import frames.LoginForm;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import service.ClientService;

import java.util.List;

public class Main {

    static ClientService clientService;
    static EntityManager entityManager;
    public static void main(String[] args) {

        clientService = new ClientService();
        ClientEntity client = clientService.getClientById(2);
        HomeForm homeForm = new HomeForm(client);

    }
}
