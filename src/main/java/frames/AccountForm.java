package frames;

import entity.ClientEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AccountForm extends AbstractFrame{
    private JPanel pnlRoot1;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private ClientEntity client;

    public AccountForm(ClientEntity client){
        super();
        this.client = client;
        initComponents();

        pnlRoot1.setBackground(new java.awt.Color(5, 231, 255));
        pnlRoot.setLayout(new BorderLayout());
        pnlRoot.add(pnlRoot1);
        this.repaint();
        this.pack();
        this.revalidate();


    }

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
