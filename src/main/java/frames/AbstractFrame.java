package frames;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractFrame extends JFrame {

    public AbstractFrame(){
        this.setVisible(true);
        this.setSize(400,200);
        this.setResizable(false);
    }
}
