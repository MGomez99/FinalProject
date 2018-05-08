package projectview;

import project.Loader;
import project.MachineModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.util.Observable;
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

    public static void main(String[] args) {//TODO Try test
        ViewMediator view = new ViewMediator();
        MachineModel model = new MachineModel();
        MemoryViewPanel panel = new MemoryViewPanel(view, model, 0, 500);
        JFrame frame = new JFrame("TEST");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 700);
        frame.setLocationRelativeTo(null);
        frame.add(panel.createMemoryDisplay());
        frame.setVisible(true);
        System.out.println(Loader.load(model, new File("large.pexe"), 0, 0));
        panel.update(view, null);
    }

    public JComponent createMemoryDisplay() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        Border border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                "Data Memory View [" + lower + "-" + upper + "]",
                TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
        panel.setBorder(border);
        JPanel innerPanel = new JPanel();
        innerPanel.setBorder(border);
        JPanel numPanel = new JPanel();
        numPanel.setLayout(new GridLayout(0, 1));
        JPanel decimalPanel = new JPanel();
        decimalPanel.setLayout(new GridLayout(0, 1));
        JPanel hexPanel = new JPanel();
        hexPanel.setLayout(new GridLayout(0, 1));

        innerPanel.add(numPanel, BorderLayout.LINE_START);
        innerPanel.add(decimalPanel, BorderLayout.CENTER);
        innerPanel.add(hexPanel, BorderLayout.LINE_END);

        dataHex = new JTextField[upper - lower];
        dataDecimal = new JTextField[upper - lower];

        for (int i = lower; i < upper; i++) {
            numPanel.add(new JLabel(i + ": ", JLabel.RIGHT));
            dataDecimal[i - lower] = new JTextField(10);
            dataHex[i - lower] = new JTextField(10);
            decimalPanel.add(dataDecimal[i - lower]);
            hexPanel.add(dataHex[i - lower]);
        }
        scroller = new JScrollPane(innerPanel);
        panel.add(scroller);
        return panel;

    }

    @Override
    public void update(Observable arg0, Object arg1) {
        for (int i = lower; i < upper; i++) {
            dataDecimal[i - lower].setText("" + model.getData(i));
            dataHex[i - lower].setText(Integer.toHexString(model.getData(i)));
        }
        if (arg1 != null && arg1.equals("Clear")) {
            if (lower <= previousColor && previousColor < upper) {
                dataDecimal[previousColor - lower].setBackground(Color.WHITE);
                dataHex[previousColor - lower].setBackground(Color.WHITE);
                previousColor = -1;
            }
        } else {
            if (previousColor >= lower && previousColor < upper) {
                dataDecimal[previousColor - lower].setBackground(Color.WHITE);
                dataHex[previousColor - lower].setBackground(Color.WHITE);
            }
            previousColor = model.getChangedIndex();
            if (previousColor >= lower && previousColor < upper) {
                dataDecimal[previousColor - lower].setBackground(Color.YELLOW);
                dataHex[previousColor - lower].setBackground(Color.YELLOW);
            }
        }
        if (scroller != null && model != null) {
            JScrollBar bar = scroller.getVerticalScrollBar();
            if (model.getChangedIndex() >= lower &&
                    model.getChangedIndex() < upper &&
                    // the following just checks createMemoryDisplay has run
                    dataDecimal != null) {
                Rectangle bounds = dataDecimal[model.getChangedIndex() - lower].getBounds();
                bar.setValue(Math.max(0, bounds.y - 15 * bounds.height));
            }
        }
    }
}
