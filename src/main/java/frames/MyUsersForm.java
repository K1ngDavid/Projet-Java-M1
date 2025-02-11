package frames;

import entity.ClientEntity;
import entity.CommandEntity;
import service.ClientService;
import tools.AddUserDialog;

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
    private JButton btnViewAccount;
    private JButton btnAddUser;

    public MyUsersForm(ClientEntity admin) {
        super(admin);
        // On suppose que seul un administrateur peut accéder à cette vue
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
            // Rendre toutes les cellules non éditables
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

        btnViewAccount = new JButton("Voir le compte");
        btnViewAccount.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnViewAccount.setBackground(new Color(23, 162, 184)); // bleu
        btnViewAccount.setForeground(Color.WHITE);
        btnViewAccount.setFocusPainted(false);
        btnViewAccount.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnViewAccount.addActionListener(this::viewSelectedUserAccount);

        btnAddUser = new JButton("Ajouter utilisateur");
        btnAddUser.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAddUser.setBackground(new Color(40, 167, 69)); // vert
        btnAddUser.setForeground(Color.WHITE);
        btnAddUser.setFocusPainted(false);
        btnAddUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAddUser.addActionListener(e -> {
            AddUserDialog addUserDialog = new AddUserDialog(this);
            addUserDialog.setVisible(true);
            // Recharger la table après ajout
            loadUsersData();
        });

        actionPanel.add(btnDelete);
        actionPanel.add(btnViewAccount);
        actionPanel.add(btnAddUser);
        pnlRoot.add(actionPanel, BorderLayout.SOUTH);
    }

    /**
     * Charge les données des utilisateurs et les affiche dans la table.
     */
    private void loadUsersData() {
        tableModel.setRowCount(0);
        List<ClientEntity> clients = clientService.getAllClients();
        // Pour chaque client, calculer le total dépensé
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
                boolean deleted = clientService.deleteClient(userId);
                if (!deleted) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la suppression de l'utilisateur ID " + userId, "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
            loadUsersData();
        }
    }

    /**
     * Ouvre le compte de l'utilisateur sélectionné.
     * Si plusieurs sont sélectionnés, affiche le compte du premier.
     */
    private void viewSelectedUserAccount(ActionEvent evt) {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur pour voir son compte.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = usersTable.convertRowIndexToModel(selectedRow);
        int userId = (int) tableModel.getValueAt(modelRow, 0);
        ClientEntity user = clientService.getClientById(userId);
        if (user != null) {
            new AccountForm(user).setVisible(true);
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
