package project;

import projectview.States;

import java.util.Map;
import java.util.TreeMap;

public class MachineModel {
	public static final Map<Integer, Instruction> INSTRUCTIONS = new TreeMap<>();
	private CPU cpu = new CPU();
	private Memory mem = new Memory();
	private HaltCallBack callback;
	private boolean withGUI;
	private Job[] jobs = new Job[2];
	private Job currentJob;

	public MachineModel() {
		this(false, null);
	}

	public MachineModel(boolean GUI, HaltCallBack HCF) {
		this.withGUI = GUI;
		this.callback = HCF;
        //NOP
		INSTRUCTIONS.put(0x0, arg -> {
			cpu.incrementIP();
		});
        //LODI
		INSTRUCTIONS.put(0x1, arg -> {
			cpu.accumulator = arg;
			cpu.incrementIP();
		});
        //LOD
		INSTRUCTIONS.put(0x2, arg -> {
            cpu.accumulator = mem.getData(cpu.memoryBase + arg);
			cpu.incrementIP();
		});
        //LODN
		INSTRUCTIONS.put(0x3, arg -> {
			int arg1 = mem.getData(cpu.memoryBase + arg);
			int arg2 = mem.getData(cpu.memoryBase + arg1);
			cpu.accumulator = arg2;
			cpu.incrementIP();
		});
        //STO
		INSTRUCTIONS.put(0x4, arg -> {
			mem.setData(cpu.memoryBase + arg, cpu.accumulator);
			cpu.incrementIP();
		});
        //STON
		INSTRUCTIONS.put(0x5, arg -> {
            int val = mem.getData(cpu.memoryBase + arg);
            mem.setData(cpu.memoryBase + val, cpu.accumulator);
			cpu.incrementIP();
		});
        //JMPR
		INSTRUCTIONS.put(0x6, arg -> {
			cpu.instructionPointer += arg;
		});
        //JUMP
		INSTRUCTIONS.put(0x7, arg -> {
            cpu.instructionPointer += mem.getData(cpu.memoryBase + arg);
		});

		INSTRUCTIONS.put(0x8, arg -> {
			cpu.instructionPointer = currentJob.getStartcodeIndex() + arg;
		});

		INSTRUCTIONS.put(0x9, arg -> {
			if (cpu.accumulator == 0) {
				cpu.instructionPointer += arg;
			} else {
				cpu.incrementIP();
			}
		});

		INSTRUCTIONS.put(0xA, arg -> {
			if (cpu.accumulator == 0) {
                cpu.instructionPointer += mem.getData(cpu.memoryBase + arg);
			} else {
				cpu.incrementIP();
			}
		});

		INSTRUCTIONS.put(0xB, arg -> {
			if (cpu.accumulator == 0) {
				cpu.instructionPointer = currentJob.getStartcodeIndex() + arg;
			} else {
				cpu.incrementIP(1);
			}
		});

		INSTRUCTIONS.put(0xC, arg -> {
			cpu.accumulator += arg;
			cpu.incrementIP();
		});

		INSTRUCTIONS.put(0xD, arg -> {
			int arg1 = mem.getData(cpu.memoryBase + arg);
			cpu.accumulator += arg1;
			cpu.incrementIP();
		});

		INSTRUCTIONS.put(0xE, arg -> {
			int arg1 = mem.getData(cpu.memoryBase + arg);
			int arg2 = mem.getData(cpu.memoryBase + arg1);
			cpu.accumulator += arg2;
			cpu.incrementIP();
		});

		INSTRUCTIONS.put(0xF, arg -> {
			cpu.accumulator -= arg;
			cpu.incrementIP();
		});

		INSTRUCTIONS.put(0x10, arg -> {
			int arg1 = mem.getData(cpu.memoryBase + arg);
			cpu.accumulator -= arg1;
			cpu.incrementIP();
		});

		INSTRUCTIONS.put(0x11, arg -> {
			int arg1 = mem.getData(cpu.memoryBase + arg);
			int arg2 = mem.getData(cpu.memoryBase + arg1);
			cpu.accumulator -= arg2;
			cpu.incrementIP();
		});

		INSTRUCTIONS.put(0x12, arg -> {
			cpu.accumulator *= arg;
			cpu.incrementIP();
		});

		INSTRUCTIONS.put(0x13, arg -> {
			int arg1 = mem.getData(cpu.memoryBase + arg);
			cpu.accumulator *= arg1;
			cpu.incrementIP();
		});

		INSTRUCTIONS.put(0x14, arg -> {
			int arg1 = mem.getData(cpu.memoryBase + arg);
			int arg2 = mem.getData(cpu.memoryBase + arg1);
			cpu.accumulator *= arg2;
			cpu.incrementIP();
		});

		INSTRUCTIONS.put(0x15, arg -> {
			if (arg == 0) {
				throw new DivideByZeroException("Cannot Divide By 0!");
			} else {
				cpu.accumulator /= arg;
				cpu.incrementIP();
			}
		});

		INSTRUCTIONS.put(0x16, arg -> {
			int arg1 = mem.getData(cpu.memoryBase + arg);
			if (arg1 == 0) {
				throw new DivideByZeroException("Cannot Divide By 0!");
			} else {
				cpu.accumulator /= arg1;
				cpu.incrementIP();
			}
		});

		INSTRUCTIONS.put(0x17, arg -> {

			int arg1 = mem.getData(cpu.memoryBase + arg);
			int arg2 = mem.getData(cpu.memoryBase + arg1);
			if (arg2 == 0) {
				throw new DivideByZeroException("Cannot Divide By 0!");
			} else {
				cpu.accumulator /= arg2;
				cpu.incrementIP();
			}
		});

		INSTRUCTIONS.put(0x18, arg -> {
			if (cpu.accumulator != 0 && arg != 0) {
				cpu.accumulator = 1;
			} else {
				cpu.accumulator = 0;
			}
			cpu.incrementIP();
		});

		INSTRUCTIONS.put(0x19, arg -> {
			int arg1 = mem.getData(cpu.memoryBase + arg);
			if (cpu.accumulator != 0 && arg1 != 0) {
				cpu.accumulator = 1;
			} else {
				cpu.accumulator = 0;
			}
			cpu.incrementIP();
		});

		INSTRUCTIONS.put(0x1A, arg -> {
			if (cpu.accumulator != 0) {
				cpu.accumulator = 0;
			} else {
				cpu.accumulator = 1;
			}
			cpu.incrementIP();
		});

		INSTRUCTIONS.put(0x1B, arg -> {
			int arg1 = mem.getData(cpu.memoryBase + arg);
			if (arg1 < 0) {
				cpu.accumulator = 1;
			} else {
				cpu.accumulator = 0;
			}
			cpu.incrementIP();
		});

		INSTRUCTIONS.put(0x1C, arg -> {
			int arg1 = mem.getData(cpu.memoryBase + arg);
			if (arg1 == 0) {
				cpu.accumulator = 1;
			} else {
				cpu.accumulator = 0;
			}
			cpu.incrementIP();
		});

		INSTRUCTIONS.put(0x1D, arg -> {
			int arg1 = mem.getData(cpu.memoryBase + arg);
			cpu.instructionPointer = currentJob.getStartcodeIndex() + arg1;
		});

		INSTRUCTIONS.put(0x1F, arg -> {
			callback.halt();
		});

		jobs[0] = new Job();
		jobs[1] = new Job();
		currentJob = jobs[0];
		jobs[0].setStartcodeIndex(0);
		jobs[0].setStartmemoryIndex(0);
		jobs[0].setCurrentState(States.NOTHING_LOADED);

		jobs[1].setStartcodeIndex(Memory.CODE_MAX / 4);
		jobs[1].setStartmemoryIndex(Memory.DATA_SIZE / 2);
		jobs[1].setCurrentState(States.NOTHING_LOADED);
	}

	public Job getCurrentJob() {
		return currentJob;
	}

	public void setJob(int i) {
		if (i != 0 && i != 1) {
			throw new IllegalArgumentException();
		}
		currentJob.setCurrentAcc(cpu.accumulator);
		currentJob.setCurrentIP(cpu.instructionPointer);
		currentJob = jobs[i];
		cpu.accumulator = currentJob.getCurrentAcc();
		cpu.instructionPointer = currentJob.getCurrentIP();
		cpu.memoryBase = currentJob.getStartmemoryIndex();
	}

	public States getCurrentState() {
		return currentJob.getCurrentState();
	}

	public void setCurrentState(States currentState) {
		currentJob.setCurrentState(currentState);
	}

	public int[] getData() {
		return mem.getData();
	}

	public int getData(int index) {
		return mem.getData(index);
	}

	public void setData(int index, int value) {
		mem.setData(index, value);
	}

	public int getOp(int i) {
		return mem.getOp(i);
	}

	public int getArg(int i) {
		return mem.getArg(i);
	}

	public void setCode(int index, int op, int arg) {
		mem.setCode(index, op, arg);
	}

	public int[] getCode() {
		return mem.getCode();
	}

	public int getChangedIndex() {
		return mem.getChangedIndex();
	}

	public String getHex(int i) {
		return mem.getHex(i);
	}

	public String getDecimal(int i) {
		return mem.getDecimal(i);
	}

	public int getInstructionPointer() {
		return cpu.instructionPointer;
	}

	public void setInstructionPointer(int i) {
		cpu.instructionPointer = i;
	}

	public int getAccumulator() {
		return cpu.accumulator;
	}

	public void setAccumulator(int i) {
		cpu.accumulator = i;
	}

	public int getMemoryBase() {
		return cpu.memoryBase;
	}

	public void setMemoryBase(int i) {
		cpu.memoryBase = i;
	}

	public Instruction get(int key) {
		return INSTRUCTIONS.get(key);
	}

	public void step() {
		try {
			int ip = getInstructionPointer();
			if (currentJob.getStartcodeIndex() > ip || ip >= currentJob.getStartcodeIndex() + currentJob.getCodeSize()) {
				throw new CodeAccessException();
			}
			get(getOp(ip)).execute(getArg(ip));
		} catch (Exception e) {
			callback.halt();
			throw e;
		}
	}

	public void clearJob() {
		mem.clearData(currentJob.getStartmemoryIndex(), currentJob.getStartmemoryIndex() + Memory.DATA_SIZE / 2);
		mem.clearCode(currentJob.getStartcodeIndex(), currentJob.getStartcodeIndex() + currentJob.getCodeSize());
		currentJob.reset();
		setAccumulator(0);
		setInstructionPointer(currentJob.getStartcodeIndex());
	}

	private class CPU {
		private int accumulator;
		private int instructionPointer;
		private int memoryBase;

		public void incrementIP(int val) {
			instructionPointer += val;
		}

		public void incrementIP() {
			instructionPointer += 1;
		}


	}

}
