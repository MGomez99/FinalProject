package projectview;

import javafx.beans.Observable;
import project.MachineModel;

import javax.swing.JFrame;

public class ViewMediator extends Observable {
    private MachineModel model;
    private JFrame frame;

    public void step() { }

    //GETTERS AND SETTERS
    public void setModel(MachineModel model){
        this.model = model;
    }
    public MachineModel getModel() {
        return this.model;
    }
    public JFrame gtFrame() {
        return this.frame;
    }

}
