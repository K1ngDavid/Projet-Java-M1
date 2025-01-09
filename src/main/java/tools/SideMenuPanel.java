package tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SideMenuPanel {

    private GroupLayout gl;
    private int x = 0;
    private int minWidth = 60;
    private int maxWidth = 200;
    private JPanel side;
    private JPanel main;
    private boolean isEnabled;
    private int speed = 2;
    private int responsiveMinWidth = 600;
    private final JFrame frame;
    private boolean isOpen = false;

    public SideMenuPanel(JFrame frame) {
        this.frame = frame;

        // Listener pour redimensionnement de la fenêtre
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent arg0) {
                x = 0;
                if (isOpen) {
                    openMenu();
                } else {
                    closeMenu();
                }
            }
        });
    }

    // Getters et Setters
    public boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public int getResponsiveMinWidth() {
        return responsiveMinWidth;
    }

    public void setResponsiveMinWidth(int responsiveWidth) {
        this.responsiveMinWidth = responsiveWidth;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = Math.max(speed, 1); // Évite un speed de 0
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int min) {
        this.minWidth = min;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int max) {
        this.maxWidth = max;
    }

    public JPanel getSide() {
        return side;
    }

    public void setSide(JPanel side) {
        this.side = side;
    }

    public JPanel getMain() {
        return main;
    }

    public void setMain(JPanel main) {
        this.main = main;
    }

    public boolean setMainAnimation() {
        return isEnabled;
    }

    public void setMainAnimation(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    // Méthode principale pour animer le menu
    public void onSideMenu() {
        if (side == null || main == null) {
            throw new IllegalStateException("Side or Main panel is not set. Use setSide() and setMain() before toggling the menu.");
        }

        if (x == maxWidth) {
            closeMenu();
        } else if (x == 0) {
            openMenu();
        }
    }

    public void closeMenu() {
        if (getIsOpen()) {
            new Timer(5, e -> {
                if (x > 0) {
                    x -= speed;
                    updateMenuSize(x);
                } else {
                    x = 0;
                    ((Timer) e.getSource()).stop();
                    isOpen = false;
                }
            }).start();
        }
    }

    public void openMenu() {
        if (!getIsOpen()) {
            new Timer(5, e -> {
                if (x < maxWidth) {
                    x += speed;
                    updateMenuSize(x);
                } else {
                    x = maxWidth;
                    ((Timer) e.getSource()).stop();
                    isOpen = true;
                }
            }).start();
        }
    }

    private void updateMenuSize(int currentWidth) {
        try {
            side.setSize(new Dimension(minWidth + currentWidth, main.getHeight()));

            if (side instanceof Container) {
                for (Component child : ((Container) side).getComponents()) {
                    child.setSize(new Dimension(maxWidth + minWidth, child.getHeight()));
                }
            }

            if (frame.getWidth() >= responsiveMinWidth && isEnabled) {
                main.setLocation(minWidth + currentWidth, main.getY());
            }
        } catch (Exception e) {
            Logger.getLogger(SideMenuPanel.class.getName()).log(Level.SEVERE, "Error updating menu size", e);
        }
    }

    private void setGLSize(int size) {
        gl = new GroupLayout(main.getParent());
        main.getParent().setLayout(gl);
        gl.setHorizontalGroup(
                gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl.createSequentialGroup()
                                .addComponent(side, GroupLayout.PREFERRED_SIZE, size, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(main, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0))
        );
        gl.setVerticalGroup(
                gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(side, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                        .addComponent(main, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }
}
