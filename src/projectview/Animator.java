package projectview;

import javax.swing.*;

class Animator {
	private static final int TICK = 500;
	private boolean autoStepOn = false;
	private Timer timer;
	private ViewMediator view;

	Animator(ViewMediator view) {
		this.view = view;
	}

	boolean isAutoStepOn() {
		return autoStepOn;
	}

	void setAutoStepOn(boolean autoStepOn) {
		this.autoStepOn = autoStepOn;
	}

	void toggleAutoStep() {
		autoStepOn = !autoStepOn;
	}

	void setPeriod(int period) {
		timer.setDelay(period);
	}

	void start() {
		timer = new Timer(TICK, e -> {
			if (autoStepOn) view.step();
		});
		timer.start();
	}


}
