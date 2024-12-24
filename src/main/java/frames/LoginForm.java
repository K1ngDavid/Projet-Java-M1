package frames;

import jakarta.persistence.EntityManager;
import service.ClientService;

import javax.security.auth.login.LoginException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame{
    private JTextField textField1;
    private JTextField textField2;
    private JButton connexionButton;
    private JPanel pnlRoot;
    private JLabel lblError;
    private JPanel pnlUsername;
    private JPanel pnlPassword;
    private JPanel pnlConnection;
    private JButton sInscrireButton;
    private JPanel pnlImage;

    private ClientService clientService;
    private EntityManager entityManager;

    public LoginForm(){
        this.setVisible(true);
        this.setSize(400,200);
        this.setContentPane(pnlRoot);
        this.pnlRoot.setLayout(new BorderLayout());
        this.setResizable(false);

        clientService = new ClientService();



        connexionButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Bouton selectionné");
                loginVerfification();
            }
        });
        sInscrireButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterForm registerForm = new RegisterForm();
            }
        });
    }


    void loginVerfification(){
        try {
            if(textField1.getText().matches("^[a-zA-Z0-9._-]{3,}$") && textField2.getText().matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")){
                if (clientService.clientCanLogIn(textField1.getText(), textField2.getText())) {
                    System.out.println("Client found");
                    HomeForm homeForm = new HomeForm();
                } else {
                    throw new LoginException("User not found");
//                    System.out.println("Client not found");
                }
            }else{
                throw new LoginException("Login or password not in right format");
            }
        } catch (LoginException e) {
            lblError.setText(e.getMessage());
            lblError.setForeground(Color.red);
            this.revalidate();
        }
    }


    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        g.drawImage(new ImageIcon("/src/main/resources/images/voiture-connexion.jpg").getImage(),0,0,null);
    }
}
