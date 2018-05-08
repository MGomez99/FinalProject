package projectview;

import javax.swing.*;

public class Animator {
    static final int TICK = 500;
    boolean autoStepOn = false;
    Timer timer;
    ViewMediator view;

    public Animator(ViewMediator view) {
        this.view = view;
    }

    public boolean isAutoStepOn() {
        return autoStepOn;
    }

    public void setAutoStepOn(boolean autoStepOn) {
        this.autoStepOn = autoStepOn;
    }

    public void toggleAutoStep() {
        autoStepOn = !autoStepOn;
    }

    public void setPeriod(int period) {
        timer.setDelay(period);
    }

    public void start() {
        timer = new Timer(TICK, e -> {
            if (autoStepOn) view.step();
        });
        timer.start();
    }


}
