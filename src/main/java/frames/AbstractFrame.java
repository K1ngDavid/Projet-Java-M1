package frames;

import entity.ClientEntity;
import tools.SideMenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public abstract class AbstractFrame extends JFrame {

    private ClientEntity client;
    protected JPanel pnlRoot;
    protected JPanel mainPanel1;
    protected JPanel sidebar;
    protected JButton home;
    protected JButton jButton1;
    protected JButton jButton2;
    protected JLabel jLabel1;
    protected JLabel jLabel13;
    protected JLabel jLabel2;
    protected JLabel jLabel3;
    protected JLabel jLabel6;
    protected JLabel jLabel7;
    protected JPanel jPanel1;
    protected JButton men;
    protected JButton catalogue;
    protected JButton myCart;

    protected JButton account;
    protected SideMenuPanel sp;
    public AbstractFrame(ClientEntity client){
        this.client = client;
        initComponents();
        this.pack();
        sp = new SideMenuPanel(this);
        sp.setMain(pnlRoot);
        sp.setSide(sidebar);
        sp.setMinWidth(55);
        sp.setMaxWidth(150);
        sp.setMainAnimation(true);
        sp.setSpeed(4);
        sp.setResponsiveMinWidth(600);

        this.setVisible(true);
    }

    public AbstractFrame(){
        initComponents();
        this.pack();
        sp = new SideMenuPanel(this);
        sp.setMain(pnlRoot);
        sp.setSide(sidebar);
        sp.setMinWidth(55);
        sp.setMaxWidth(150);
        sp.setMainAnimation(true);
        sp.setSpeed(4);
        sp.setResponsiveMinWidth(600);

        this.setVisible(true);
    }


    public ClientEntity getClient() {
        return client;
    }

    private void initComponents() {

        jPanel1 = new JPanel();
        sidebar = new JPanel();
        pnlRoot = new JPanel();
        catalogue = new JButton();
        myCart = new JButton();
        home = new JButton();
        account = new JButton();
        men = new JButton();
        jLabel13 = new JLabel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        jButton1 = new JButton();
        jButton2 = new JButton();



        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        sidebar.setBackground(new java.awt.Color(16, 84, 129));
        sidebar.setPreferredSize(new java.awt.Dimension(60, 32));

        catalogue.setFont(new java.awt.Font("Microsoft PhagsPa", 0, 14)); // NOI18N
        catalogue.setForeground(new java.awt.Color(195, 217, 233));
        catalogue.setIcon(new javax.swing.ImageIcon()); // NOI18N
        catalogue.setText("Catalogue");
        catalogue.setBorderPainted(false);
        catalogue.setContentAreaFilled(false);
        catalogue.setFocusPainted(false);
        catalogue.setHideActionText(true);
        catalogue.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        catalogue.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        catalogue.setIconTextGap(20);
        catalogue.setMargin(new java.awt.Insets(2, 0, 2, 14));
        catalogue.setMinimumSize(new java.awt.Dimension(0, 35));
        catalogue.setPreferredSize(new java.awt.Dimension(50, 574));

        catalogue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    catalogActionPerformed(e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        myCart.setFont(new java.awt.Font("Microsoft PhagsPa", 0, 14)); // NOI18N
        myCart.setForeground(new java.awt.Color(195, 217, 233));
        myCart.setIcon(new javax.swing.ImageIcon()); // NOI18N
        myCart.setText("My Cart");
        myCart.setBorderPainted(false);
        myCart.setContentAreaFilled(false);
        myCart.setFocusPainted(false);
        myCart.setHideActionText(true);
        myCart.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        myCart.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        myCart.setIconTextGap(20);
        myCart.setMargin(new java.awt.Insets(2, 0, 2, 14));
        myCart.setMinimumSize(new java.awt.Dimension(0, 35));
        myCart.setPreferredSize(new java.awt.Dimension(50, 574));

        myCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    myCartActionPerformed(e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        account.setFont(new java.awt.Font("Microsoft PhagsPa", 0, 14)); // NOI18N
        account.setForeground(new java.awt.Color(195, 217, 233));
        account.setIcon(new javax.swing.ImageIcon()); // NOI18N
        account.setText("Account");
        account.setBorderPainted(false);
        account.setContentAreaFilled(false);
        account.setFocusPainted(false);
        account.setHideActionText(true);
        account.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        account.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        account.setIconTextGap(20);
        account.setMargin(new java.awt.Insets(2, 0, 2, 14));
        account.setMinimumSize(new java.awt.Dimension(0, 35));
        account.setPreferredSize(new java.awt.Dimension(50, 574));

        account.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountActionPerformed(e);
            }
        });

        home.setFont(new java.awt.Font("Microsoft PhagsPa", 0, 14)); // NOI18N
        home.setForeground(new java.awt.Color(195, 217, 233));
        home.setIcon(new javax.swing.ImageIcon()); // NOI18N
        home.setText("Home");
        home.setBorderPainted(false);
        home.setContentAreaFilled(false);
        home.setHideActionText(true);
        home.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        home.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        home.setIconTextGap(20);
        home.setMargin(new java.awt.Insets(2, 0, 2, 14));
        home.setMinimumSize(new java.awt.Dimension(0, 0));
        home.setPreferredSize(new java.awt.Dimension(50, 574));

        home.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homeActionPerformed(e);
            }
        });

        men.setBackground(new java.awt.Color(34, 40, 47));
        men.setFont(new java.awt.Font("Microsoft PhagsPa", 0, 14)); // NOI18N
        men.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/img.png"))); // NOI18N
        men.setBorderPainted(false);
        men.setContentAreaFilled(false);
        men.setFocusPainted(false);
        men.setHideActionText(true);
        men.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        men.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        men.setIconTextGap(20);
        men.setMargin(new java.awt.Insets(2, 3, 2, 14));
        men.setMinimumSize(new java.awt.Dimension(0, 35));
        men.setPreferredSize(new java.awt.Dimension(50, 574));
        men.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Microsoft PhagsPa", 0, 10)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(126, 170, 214));
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        GroupLayout sidebarLayout = new javax.swing.GroupLayout(sidebar);
        sidebar.setLayout(sidebarLayout);
        sidebarLayout.setHorizontalGroup(
                sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(home, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(men, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(catalogue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(myCart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(account, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sidebarLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        sidebarLayout.setVerticalGroup(
                sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(sidebarLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(men, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(home, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(catalogue, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(myCart, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(account, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
        );


        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(sidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(pnlRoot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(sidebar, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                        .addComponent(pnlRoot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );


        pnlRoot.setPreferredSize(new Dimension(800,800));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menActionPerformed
        // TODO add your handling code here:
        sp.onSideMenu();
    }//GEN-LAST:event_menActionPerformed

    abstract void accountActionPerformed(ActionEvent evt);

    abstract void homeActionPerformed(ActionEvent evt);

    private void myCartActionPerformed(ActionEvent event) throws IOException {
        dispose();
        MyCartForm myCartForm = new MyCartForm(client);
        myCartForm.setVisible(true);
    }

    private void catalogActionPerformed(ActionEvent evt) throws IOException {
        dispose();
        CatalogForm catalogForm = new CatalogForm(client);
        catalogForm.setVisible(true);
        catalogue.setBorderPainted(true);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        sp.openMenu();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        sp.closeMenu();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed
}
