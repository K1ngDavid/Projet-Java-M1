import entity.ClientEntity;
import frames.HomeForm;
import frames.LoginForm;
import service.ClientService;
import tools.JPAUtil;

public class Main {
    public static void main(String[] args) {
        try {
            ClientService clientService = new ClientService();
            ClientEntity client = clientService.getClientById(2);
            HomeForm homeForm = new HomeForm(client);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ Fermer Hibernate proprement à la fin de l'application (via shutdown hook)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Fermeture propre de Hibernate...");
            JPAUtil.close();
        }));
    }
}
