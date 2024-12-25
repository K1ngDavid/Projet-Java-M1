package frames;

import entity.ClientEntity;
import service.ClientService;

import javax.security.auth.login.FailedLoginException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterForm extends JFrame{
    private JPanel pnlRoot;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton creerSonCompteButton;
    private JLabel lblErrorPM;

    public RegisterForm(){
        this.setVisible(true);
        this.setSize(400,200);
        this.setContentPane(pnlRoot);
        this.pnlRoot.setLayout(new BorderLayout());
        ClientService clientService = new ClientService();
//        this.setResizable(false);
        creerSonCompteButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = textField1.getText();
                    String password = textField2.getText();
                    String email = textField3.getText();
                    // Expression régulière pour valider le nom d'utilisateur et le mot de passe
                    String regex = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+=\\-\\[\\]{}|;:'\",.<>?/])[a-zA-Z0-9!@#$%^&*()_+=\\-\\[\\]{}|;:'\",.<>?/]{6,}$";

                    // Expression régulière pour valider l'email
                    String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

                    // Création de l'objet Pattern à partir de l'expression régulière
                    Pattern pattern = Pattern.compile(regex);

                    // Création de Matcher pour tester le nom d'utilisateur
                    Matcher usernameMatcher = pattern.matcher(username);
                    // Création de Matcher pour tester le mot de passe
                    Matcher passwordMatcher = pattern.matcher(password);

                    // Création de l'objet Pattern pour l'email
                    Pattern emailPattern = Pattern.compile(emailRegex);

                    // Création de Matcher pour tester l'email
                    Matcher emailMatcher = emailPattern.matcher(email);

                    // Validation du nom d'utilisateur
                    if (!usernameMatcher.matches()) {
                        JOptionPane.showMessageDialog(null, "Le nom d'utilisateur doit contenir au moins 6 caractères, un chiffre et un caractère spécial.");
                    }
                    // Validation du mot de passe
                    else if (!passwordMatcher.matches()) {
                        JOptionPane.showMessageDialog(null, "Le mot de passe doit contenir au moins 6 caractères, un chiffre et un caractère spécial.");
                    } else if (!emailMatcher.matches()) {
                        JOptionPane.showMessageDialog(null, "L'adresse email n'est pas valide. Veuillez réessayer.");
                    } else{
                        ClientEntity client = new ClientEntity();
                        client.setName(username);
                        client.setPassword(password);
                        client.setEmail(email);

                        clientService.addClient(client);
                        // Si les deux sont valides
                        JOptionPane.showMessageDialog(null, "Validation réussie !");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Une erreur inattendue s'est produite : " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
        }
}
