package frames;

import entity.ClientEntity;
import entity.CommandEntity;
import service.ClientService;
import tools.AddUserDialog;
import tools.EditUserDialog; // Cette classe doit permettre d'éditer un ClientEntity

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MyUsersForm extends AbstractFrame {

    private ClientService clientService;
    private JTable usersTable;
    private DefaultTableModel tableModel;
    private JButton btnDelete;
    private JButton btnEditAccount; // Anciennement btnViewAccount
    private JButton btnAddUser;

    public MyUsersForm(ClientEntity admin) {
        super(admin);
        // Seul un administrateur peut accéder à cette vue
        clientService = new ClientService();
        initComponents();
        loadUsersData();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
    }

    private void initComponents() {
        pnlRoot.setLayout(new BorderLayout());
        pnlRoot.setBackground(Color.WHITE);
        pnlRoot.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Titre principal
        JLabel lblTitle = new JLabel("👥 Liste des Utilisateurs et Dépenses", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 33, 33));
        lblTitle.setBorder(new EmptyBorder(10, 0, 20, 0));
        pnlRoot.add(lblTitle, BorderLayout.NORTH);

        // Création du modèle de table et de la JTable
        String[] columns = {"ID", "Nom", "Email", "Dépenses (€)", "Rôle"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        usersTable = new JTable(tableModel);
        usersTable.setFillsViewportHeight(true);
        usersTable.setRowHeight(30);
        usersTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usersTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Personnalisation de l'en-tête
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(220, 220, 220));
        headerRenderer.setForeground(Color.DARK_GRAY);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        for (int i = 0; i < usersTable.getColumnCount(); i++) {
            usersTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        usersTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel contenant la table
        JPanel panelMain = new JPanel(new BorderLayout());
        panelMain.add(scrollPane, BorderLayout.CENTER);
        pnlRoot.add(panelMain, BorderLayout.CENTER);

        // Panel des actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionPanel.setBackground(Color.WHITE);

        btnDelete = new JButton("Supprimer");
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDelete.setBackground(new Color(220, 53, 69)); // rouge
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.addActionListener(this::deleteSelectedUsers);

        // Bouton modifié : texte "Modifier le compte" et action pour éditer un seul utilisateur
        btnEditAccount = new JButton("Modifier le compte");
        btnEditAccount.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnEditAccount.setBackground(new Color(23, 162, 184)); // bleu
        btnEditAccount.setForeground(Color.WHITE);
        btnEditAccount.setFocusPainted(false);
        btnEditAccount.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditAccount.addActionListener(this::editSelectedUserAccount);

        btnAddUser = new JButton("Ajouter utilisateur");
        btnAddUser.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAddUser.setBackground(new Color(40, 167, 69)); // vert
        btnAddUser.setForeground(Color.WHITE);
        btnAddUser.setFocusPainted(false);
        btnAddUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddUser.addActionListener(e -> {
            AddUserDialog addUserDialog = new AddUserDialog(this);
            addUserDialog.setVisible(true);
            loadUsersData();
        });

        actionPanel.add(btnDelete);
        actionPanel.add(btnEditAccount);
        actionPanel.add(btnAddUser);
        pnlRoot.add(actionPanel, BorderLayout.SOUTH);
    }

    /**
     * Charge les données des utilisateurs et les affiche dans la table.
     */
    private void loadUsersData() {
        tableModel.setRowCount(0);
        List<ClientEntity> clients = clientService.getAllClients();
        List<Object[]> data = clients.stream()
                .map(c -> {
                    BigDecimal totalSpent = c.getCommands().stream()
                            .filter(cmd -> "Payée".equalsIgnoreCase(cmd.getCommandStatus()))
                            .map(CommandEntity::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new Object[]{c.getIdClient(), c.getName(), c.getEmail(), totalSpent, c.getRole()};
                })
                .sorted((a, b) -> ((BigDecimal) b[3]).compareTo((BigDecimal) a[3]))
                .collect(Collectors.toList());
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    /**
     * Supprime les utilisateurs sélectionnés dans la table.
     */
    private void deleteSelectedUsers(ActionEvent evt) {
        int[] selectedRows = usersTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner au moins un utilisateur à supprimer.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Confirmez-vous la suppression des utilisateurs sélectionnés ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            for (int row : selectedRows) {
                int modelRow = usersTable.convertRowIndexToModel(row);
                int userId = (int) tableModel.getValueAt(modelRow, 0);
                if (getClient().getIdClient() == userId) {
                    JOptionPane.showMessageDialog(this, "Vous ne pouvez pas vous supprimer vous-même.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean deleted = clientService.deleteClient(userId);
                if (!deleted) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'utilisateur ID " + userId, "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
            loadUsersData();
        }
    }

    /**
     * Permet à un administrateur de modifier les informations d'un utilisateur.
     * Si plusieurs utilisateurs sont sélectionnés, affiche une erreur.
     */
    private void editSelectedUserAccount(ActionEvent evt) {
        int[] selectedRows = usersTable.getSelectedRows();
        if (selectedRows.length != 1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un seul utilisateur pour modifier son compte.", "Sélection invalide", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = usersTable.convertRowIndexToModel(selectedRows[0]);
        int userId = (int) tableModel.getValueAt(modelRow, 0);
        ClientEntity user = clientService.getClientById(userId);
        if (user != null) {
            // Ouvre une boîte de dialogue pour modifier les informations du client
            EditUserDialog editUserDialog = new EditUserDialog(this, user);
            editUserDialog.setVisible(true);
            loadUsersData();
        } else {
            JOptionPane.showMessageDialog(this, "Utilisateur non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    void accountActionPerformed(ActionEvent evt) {
        dispose();
        new AccountForm(getClient()).setVisible(true);
    }
}
