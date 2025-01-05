package frames;

import entity.ClientEntity;
import tools.SideMenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    protected JButton settings;

    protected JButton account;
    protected SideMenuPanel sp;
    public AbstractFrame(){
        initComponents();

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











    private void initComponents() {

        jPanel1 = new JPanel();
        sidebar = new JPanel();
        pnlRoot = new JPanel();
        settings = new JButton();
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

        settings.setFont(new java.awt.Font("Microsoft PhagsPa", 0, 14)); // NOI18N
        settings.setForeground(new java.awt.Color(195, 217, 233));
        settings.setIcon(new javax.swing.ImageIcon()); // NOI18N
        settings.setText("Settings");
        settings.setBorderPainted(false);
        settings.setContentAreaFilled(false);
        settings.setFocusPainted(false);
        settings.setHideActionText(true);
        settings.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        settings.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        settings.setIconTextGap(20);
        settings.setMargin(new java.awt.Insets(2, 0, 2, 14));
        settings.setMinimumSize(new java.awt.Dimension(0, 35));
        settings.setPreferredSize(new java.awt.Dimension(50, 574));

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
                        .addGroup(sidebarLayout.createSequentialGroup()
                                .addGroup(sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(men, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(settings, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(account, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(sidebarLayout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sidebarLayout.setVerticalGroup(
                sidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(sidebarLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(men, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(home, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(settings, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // Espace flexible
                                .addComponent(account, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE) // Bouton en bas
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
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        sp.openMenu();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        sp.closeMenu();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed
}
