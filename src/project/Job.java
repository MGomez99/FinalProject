package project;

import projectview.States;

public class Job {
	private int startcodeIndex;
	private int codeSize;
	private int startmemoryIndex;
	private int currentIP;
	private int currentAcc;
	private States currentState;

	States getCurrentState() {
		return currentState;
	}

	void setCurrentState(States currentState) {
		this.currentState = currentState;
	}

	int getCurrentAcc() {
		return currentAcc;
	}

	void setCurrentAcc(int currentAccumulator) {
		this.currentAcc = currentAccumulator;
	}

	public int getStartcodeIndex() {
		return startcodeIndex;
	}

	void setStartcodeIndex(int startcodeIndex) {
		this.startcodeIndex = startcodeIndex;
	}

	public int getCodeSize() {
		return codeSize;
	}

	public void setCodeSize(int codeSize) {
		this.codeSize = codeSize;
	}

	public int getStartmemoryIndex() {
		return startmemoryIndex;
	}

	void setStartmemoryIndex(int startmemoryIndex) {
		this.startmemoryIndex = startmemoryIndex;
	}

	int getCurrentIP() {
		return currentIP;
	}

	void setCurrentIP(int currentIP) {
		this.currentIP = currentIP;
	}

	void reset() {
		currentState = States.NOTHING_LOADED;
		currentAcc = 0;
		currentIP = startcodeIndex;
	}
}