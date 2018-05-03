package projectview;

import java.util.Observable;
import project.MachineModel;

import javax.swing.*;

@SuppressWarnings("deprecation")
public class ViewMediator extends Observable {
    private MachineModel model;
    private JFrame frame;

    public void step() {
    }

    public MachineModel getModel() {
        return this.model;
    }

    //GETTERS AND SETTERS
    public void setModel(MachineModel model) {
        this.model = model;
    }

    public JFrame gtFrame() {
        return this.frame;
    }


}
