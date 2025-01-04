package frames;

import tools.SideMenuPanel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingUtilities.invokeLater;

public class HomeForm extends JFrame{
    private JPanel pnlRoot;
    private JPanel mainPanel1;
    private JPanel sidebar;
    private JButton home;
    private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel1;
    private JLabel jLabel13;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JPanel jPanel1;
    private JButton men;
    private JButton settings;
    private SideMenuPanel sp;


    public HomeForm(){
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
        settings = new JButton();
        home = new JButton();
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
                                        .addComponent(settings, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
        );

        pnlRoot.setBackground(new java.awt.Color(210, 231, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setText("Home");

        jLabel2.setText("Mes catégories");
        jLabel3.setText("Mes commandes");
        jLabel3.setBorder(BorderFactory.createLineBorder(Color.black));
        jLabel6.setText("Mes voitures");
        jLabel6.setBorder(BorderFactory.createLineBorder(Color.black));
        jLabel7.setText("Mes dépenses");

        JLabel labeltest = new JLabel("test");
        labeltest.setBorder(BorderFactory.createLineBorder(Color.black));

        GroupLayout mainPanelLayout = new GroupLayout(pnlRoot);
        pnlRoot.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createSequentialGroup()
                        .addContainerGap(20, 100) // Gap gauche (20 pixels minimum)
                        .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(jLabel1) // Titre centré
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // Espace flexible entre colonnes
                                        .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                )
                        )
                        .addContainerGap(20, 150) // Gap droit (20 pixels minimum)
        );



        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23) // Espace initial en haut
                .addComponent(jLabel1) // Titre
                    .addContainerGap(5,20)

                    .addGap(18, 18, 18) // Espace entre le titre et le premier groupe

                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2) // Composant gauche
                    .addComponent(jLabel7) // Composant droit
                )

                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // Espace flexible

                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6) // Composant gauche
                        .addComponent(jLabel3)) // Composant droit
                .addGap(20, 20, 20) // Espace en bas (fixe)
                .addContainerGap(5,50)
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

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menActionPerformed
        // TODO add your handling code here:
        sp.onSideMenu();
    }//GEN-LAST:event_menActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        sp.openMenu();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        sp.closeMenu();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed
}
