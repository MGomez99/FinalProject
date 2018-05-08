package projectview;

import project.MachineModel;

import javax.swing.*;
import java.util.Observer;

public class MemoryViewPanel implements Observer {
    JTextField[] dataHex;
    JTextField[] dataDecimal;
    int lower = -1;
    int upper = -1;
    int previousColor = -1;
    private MachineModel model;
    private JScrollPane scroller;

    public MemoryViewPanel(ViewMediator gui, MachineModel mdl, int lwr, int upr) {
        this.model = mdl;
        this.lower = lwr;
        this.upper = upr;
        gui.addObserver(this);
    }
}
