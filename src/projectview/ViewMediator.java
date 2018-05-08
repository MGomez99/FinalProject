package projectview;

import project.MachineModel;

import javax.swing.*;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class ViewMediator extends Observable {
    private MachineModel model;
    private CodeViewPanel codeViewPanel;
    private MemoryViewPanel memoryViewPanel1;
    private MemoryViewPanel memoryViewPanel2;
    private MemoryViewPanel memoryViewPanel3;
    //private ControlPanel controlPanel;
    //private ProcessorViewPanel processorPanel;
    //private MenuBarBuilder menuBuilder;
    private JFrame frame;
    private FilesManager filesManager;
    private Animator animator;

    public void step() {
    }

    public MachineModel getModel() {
        return this.model;
    }

    //GETTERS AND SETTERS
    public void setModel(MachineModel model) {
        this.model = model;
    }

    public JFrame getFrame() {
        return this.frame;
    }


}
