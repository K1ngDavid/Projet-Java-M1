package frames;

import entity.ClientEntity;
import service.ClientService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountForm extends AbstractFrame{
    private JPanel pnlRoot1;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JButton validerButton;
    private JLabel lblErrors;
    private ClientEntity client;
    private ClientService clientService;

    public AccountForm(ClientEntity client){
        super();
        this.client = client;
        initComponents();

        clientService = new ClientService();
        pnlRoot1.setBackground(new java.awt.Color(5, 231, 255));
        pnlRoot.setLayout(new BorderLayout());
        pnlRoot.add(pnlRoot1);



        validerButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(" password ----->" + String.valueOf(passwordField.getPassword()));
                try{
                    if(clientService.isEmailAlreadyExists(emailField.getText())){
                        throw new Exception("Email already exists for another user :/");
                    }
                }
                catch (Exception err){
                    lblErrors.setText(err.getMessage());
                    pnlRoot.revalidate();
                }
//                client.setName(nameField.getText());
//                clientService.updateClient(client);
            }
        });
    }



//    private boolean userCanBeUpdated(){
//        if(this.emailField.getText())
//        return false;
//    }




    private void initComponents(){
        this.nameField.setText(this.client.getName());
        this.emailField.setText(this.client.getEmail());
        this.passwordField.setText(this.client.getPassword());
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
        System.out.println("DEJA SUR ACCOUNT");
    }

    @Override
    void homeActionPerformed(ActionEvent evt) {
        dispose();
        HomeForm homeForm = new HomeForm(this.client);
    }


}
