package projectview;

import project.CodeAccessException;
import project.DivideByZeroException;
import project.MachineModel;
import project.Memory;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

@SuppressWarnings({"Duplicates"})
class ViewMediator extends Observable {
    private MachineModel model;
    private JFrame frame;
    private FilesManager filesManager;
    private Animator animator;

    void step() {
        if (model.getCurrentState() != States.PROGRAM_HALTED && model.getCurrentState() != States.NOTHING_LOADED){
            try {
                model.step();
            }catch (CodeAccessException e) { // import project.CodeAccessException at the start of the class
                JOptionPane.showMessageDialog(
                        frame,
                        "Illegal access to code from line " + model.getInstructionPointer() + "\n"
                                + "Exception message: " + e.getMessage(),
                        "Run time error",
                        JOptionPane.OK_OPTION);
            }catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Illegal access to data from line " + model.getInstructionPointer() + "\n"
                                + "Exception message: " + e.getMessage(),
                        "Run time error",
                        JOptionPane.OK_OPTION);
            }catch (NullPointerException e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "NullPointerException " + model.getInstructionPointer() + "\n"
                                + "Exception message: " + e.getMessage(),
                        "Run time error",
                        JOptionPane.OK_OPTION);
            }catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Program error " + model.getInstructionPointer() + "\n"
                                + "Exception message: " + e.getMessage(),
                        "Run time error",
                        JOptionPane.OK_OPTION);
            }catch( DivideByZeroException e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "DividdeByZeroException " + model.getInstructionPointer() + "\n"
                                + "Exception message: " + e.getMessage(),
                        "Run time error",
                        JOptionPane.OK_OPTION);
            }

            setChanged();
            notifyObservers();
        }
    }
    void execute() {
        while (model.getCurrentState() != States.PROGRAM_HALTED && model.getCurrentState() != States.NOTHING_LOADED){
            try {
                model.step();
            }catch (CodeAccessException e) { // import project.CodeAccessException at the start of the class
                JOptionPane.showMessageDialog(
                        frame,
                        "Illegal access to code from line " + model.getInstructionPointer() + "\n"
                                + "Exception message: " + e.getMessage(),
                        "Run time error",
                        JOptionPane.OK_OPTION);
            }catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Illegal access to data from line " + model.getInstructionPointer() + "\n"
                                + "Exception message: " + e.getMessage(),
                        "Run time error",
                        JOptionPane.OK_OPTION);
            }catch (NullPointerException e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "NullPointerException " + model.getInstructionPointer() + "\n"
                                + "Exception message: " + e.getMessage(),
                        "Run time error",
                        JOptionPane.OK_OPTION);
            }catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Program error " + model.getInstructionPointer() + "\n"
                                + "Exception message: " + e.getMessage(),
                        "Run time error",
                        JOptionPane.OK_OPTION);
            }catch( DivideByZeroException e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "DividdeByZeroException " + model.getInstructionPointer() + "\n"
                                + "Exception message: " + e.getMessage(),
                        "Run time error",
                        JOptionPane.OK_OPTION);
            }
        }
        setChanged();
        notifyObservers();
    }

    MachineModel getModel() {
        return this.model;
    }

    //GETTERS AND SETTERS
    private void setModel(MachineModel model) {
        this.model = model;
    }

    JFrame getFrame() {
        return this.frame;
    }

    // CORRECTED LATER IN THE EVENING
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ViewMediator mediator = new ViewMediator();
                MachineModel model = new MachineModel(
                        true,
                        () -> mediator.setCurrentState(States.PROGRAM_HALTED)
                );
                mediator.setModel(model);
                mediator.createAndShowGUI();
            }
        });
    }

    private void createAndShowGUI(){
        this.animator = new Animator(this);
        this.filesManager = new FilesManager(this);
        filesManager.initialize();
        CodeViewPanel codeViewPanel = new CodeViewPanel(this, model);
        MemoryViewPanel memoryViewPanel1 = new MemoryViewPanel(this, model, 0, 240);
        MemoryViewPanel memoryViewPanel2 = new MemoryViewPanel(this, model, 240, Memory.DATA_SIZE / 2);
        MemoryViewPanel memoryViewPanel3 = new MemoryViewPanel(this, model, Memory.DATA_SIZE / 2, Memory.DATA_SIZE);
        ControlPanel controlPanel = new ControlPanel(this);
        ProcessorViewPanel processorPanel = new ProcessorViewPanel(this, model);
        MenuBarBuilder menuBuilder = new MenuBarBuilder(this);
        this.frame = new JFrame("Simulator");

        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout(1,1));
        content.setBackground(Color.BLACK);
        frame.setSize(1200, 600);
        JPanel center = new JPanel();
        center.setLayout(new GridLayout(1,3));
        frame.add(codeViewPanel.createCodeDisplay(), BorderLayout.LINE_START);
        center.add(memoryViewPanel1.createMemoryDisplay());
        center.add(memoryViewPanel2.createMemoryDisplay());
        center.add(memoryViewPanel3.createMemoryDisplay());
        frame.add(center, BorderLayout.CENTER);
        frame.add(controlPanel.createControlDisplay(), BorderLayout.PAGE_END);
        frame.add(processorPanel.createProcessorDisplay(), BorderLayout.PAGE_START);

        JMenuBar bar = new JMenuBar();
        frame.setJMenuBar(bar);
        bar.add(menuBuilder.createFileMenu());
        bar.add(menuBuilder.createExecuteMenu());
        bar.add(menuBuilder.createJobsMenu());

        //return HERE for the other GUI components.
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(WindowListenerFactory.
                windowClosingFactory(e -> exit()));
        model.setCurrentState(States.NOTHING_LOADED);
        model.getCurrentState().enter();
        setChanged();
        notifyObservers();
        animator.start();
        // return HERE for other setup details
        frame.setVisible(true);


    }

    States getCurrentState(){ return model.getCurrentState();}

    private void setCurrentState(States currentState) {
        if(currentState == States.PROGRAM_HALTED){
            animator.setAutoStepOn(false);
        }
        model.setCurrentState(currentState);
        //3 NOTIFY LINES
        model.getCurrentState().enter();
        setChanged();
        notifyObservers();
    }

    public void exit() { // method executed when user exits the program
        int decision = JOptionPane.showConfirmDialog(
                frame, "Do you really wish to exit?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (decision == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    void clearJob(){
        model.clearJob();
        model.setCurrentState(States.NOTHING_LOADED);
        model.getCurrentState().enter();
        setChanged();
        notifyObservers("Clear");
    }
    void toggleAutoStep(){
        animator.toggleAutoStep();
        if(animator.isAutoStepOn()){
            model.setCurrentState(States.AUTO_STEPPING);
        }
        else{
            model.setCurrentState(States.PROGRAM_LOADED_NOT_AUTOSTEPPING);
            model.getCurrentState().enter();
            setChanged();
            notifyObservers();
        }
    }
    void reload(){
        animator.setAutoStepOn(false);
        clearJob();
        filesManager.finalLoad_ReloadStep(model.getCurrentJob());
    }
    void assembleFile(){ filesManager.assembleFile(); }
    void loadFile() {filesManager.loadFile(model.getCurrentJob());}
    void setPeriod(int value){ animator.setPeriod(value);}
    void setJob(int i){
        model.setJob(i);
        if(model.getCurrentState() != null){
            model.getCurrentState().enter();
            setChanged();
            notifyObservers();
        }
    }
    void makeReady(String string){
        animator.setAutoStepOn(false);
        model.setCurrentState(States.PROGRAM_LOADED_NOT_AUTOSTEPPING);
        model.getCurrentState().enter();
        setChanged();
        notifyObservers(string);
    }






}
