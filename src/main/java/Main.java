import entity.ClientEntity;
import frames.HomeForm;
import frames.LoginForm;
import service.ClientService;
import tools.JPAUtil;

public class Main {
    public static void main(String[] args) {
        try {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
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
