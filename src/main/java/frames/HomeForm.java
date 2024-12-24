package frames;

import javax.swing.*;
import java.awt.*;

public class HomeForm extends JFrame{
    private JPanel pnlRoot;

    public HomeForm(){
        this.setVisible(true);
        this.setSize(400,200);
        this.setContentPane(pnlRoot);
        this.pnlRoot.setLayout(new BorderLayout());
        this.setResizable(false);
    }
}
