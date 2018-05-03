package project;

import projectview.States;

import java.util.Map;
import java.util.TreeMap;

public class MachineModel {
    public final Map<Integer, Instruction> INSTRUCTIONS = new TreeMap();
    public Job[] jobs = new Job[2]; //IS THIS PRIVATE?
    private CPU cpu = new CPU();
    private Memory memory = new Memory();
    private HaltCallback callback;
    private boolean withGUI;
    private Job currentJob;

    public MachineModel() {
        this(false, null);
    }

    public MachineModel(boolean withGUI, HaltCallback callback) {
        this.withGUI = withGUI;
        this.callback = callback;

        //INSTRUCTION_MAP entry for "ADDI"
        INSTRUCTIONS.put(0xC, arg -> {
            cpu.accumulator += arg;
            cpu.incrementIP(1);
        });

        //INSTRUCTION_MAP entry for "ADD"
        INSTRUCTIONS.put(0xD, arg -> {
            int arg1 = memory.getData(cpu.memoryBase + arg);
            cpu.accumulator += arg1;
            cpu.incrementIP(1);
        });

        //INSTRUCTION_MAP entry for "ADDN"
        INSTRUCTIONS.put(0xE, arg -> {
            int arg1 = memory.getData(cpu.memoryBase + arg);
            int arg2 = memory.getData(cpu.memoryBase + arg1);
            cpu.accumulator += arg2;
            cpu.incrementIP(1);
        });

        //Thomas's code starts here
        //NOP
        INSTRUCTIONS.put(0, arg -> {
            cpu.incrementIP(1);
        });

        //LODI
        INSTRUCTIONS.put(1, arg -> {
            cpu.accumulator = arg;
            cpu.incrementIP(1);
        });

        //LOD
        INSTRUCTIONS.put(2, arg -> {
            int arg1 = memory.getData(cpu.memoryBase + arg);
            cpu.accumulator = arg1;
            cpu.incrementIP(1);
        });

        //LODN
        INSTRUCTIONS.put(3, arg -> {
            int val = memory.getData(cpu.memoryBase + arg);
            cpu.accumulator = memory.getData(cpu.memoryBase + val);
            cpu.incrementIP(1);
        });

        //STO
        INSTRUCTIONS.put(4, arg -> {
            memory.setData(cpu.memoryBase += arg, cpu.accumulator);
            cpu.incrementIP(1);
        });

        //STON
        INSTRUCTIONS.put(5, arg -> {
            int val = memory.getData(cpu.memoryBase + arg);
            memory.setData(cpu.memoryBase + val, cpu.accumulator);
            cpu.incrementIP(1);
        });

        //JMPR
        INSTRUCTIONS.put(6, arg -> {
            cpu.instructionPointer += arg;
        });

        //JUMP
        INSTRUCTIONS.put(7, arg -> {
            cpu.instructionPointer += memory.getData(cpu.memoryBase + arg);
        });

        //JUMPI
        INSTRUCTIONS.put(8, arg -> {
            cpu.instructionPointer = arg;
        });

        //JUMPZR
        INSTRUCTIONS.put(9, arg -> {
            if (cpu.accumulator == 0) {
                cpu.instructionPointer += arg;
            } else {
                cpu.incrementIP(1);
            }
        });

        //JMPZ
        INSTRUCTIONS.put(0xA, arg -> {
            if (cpu.accumulator == 0) {
                cpu.instructionPointer += memory.getData(cpu.memoryBase + arg);
            } else {
                cpu.incrementIP(1);
            }
        });

        //JMPZI
        INSTRUCTIONS.put(0xB, arg -> {
            if (cpu.accumulator == 0) {
                cpu.instructionPointer = arg;
            } else {
                cpu.incrementIP(1);
            }
        });

        //SUBI
        INSTRUCTIONS.put(0xF, arg -> {
            cpu.accumulator -= arg;
            cpu.incrementIP(1);
        });

        //SUB
        INSTRUCTIONS.put(0x10, arg -> {
            int arg1 = memory.getData(cpu.memoryBase + arg);
            cpu.accumulator -= arg1;
            cpu.incrementIP(1);
        });

        //SUBN
        INSTRUCTIONS.put(0x11, arg -> {
            int arg1 = memory.getData(cpu.memoryBase + arg);
            int arg2 = memory.getData(cpu.memoryBase + arg1);
            cpu.accumulator -= arg2;
            cpu.incrementIP(1);
        });

        //MULI
        INSTRUCTIONS.put(0x12, arg -> {
            cpu.accumulator *= arg;
            cpu.incrementIP(1);
        });

        //MUL
        INSTRUCTIONS.put(0x13, arg -> {
            int arg1 = memory.getData(cpu.memoryBase + arg);
            cpu.accumulator *= arg1;
            cpu.incrementIP(1);
        });

        //MULN
        INSTRUCTIONS.put(0x14, arg -> {
            int arg1 = memory.getData(cpu.memoryBase + arg);
            int arg2 = memory.getData(cpu.memoryBase + arg1);
            cpu.accumulator *= arg2;
            cpu.incrementIP(1);
        });

        //DIVI
        INSTRUCTIONS.put(0x15, arg -> {
            if (arg == 0) {
                throw new DivideByZeroException();
            }
            cpu.accumulator /= arg;
            cpu.incrementIP(1);
        });

        //DIV
        INSTRUCTIONS.put(0x16, arg -> {
            int arg1 = memory.getData(cpu.memoryBase + arg);
            if (arg1 == 0) {
                throw new DivideByZeroException();
            }
            cpu.accumulator /= arg1;
            cpu.incrementIP(1);
        });

        //DIVN
        INSTRUCTIONS.put(0x17, arg -> {
            int arg1 = memory.getData(cpu.memoryBase + arg);
            int arg2 = memory.getData(cpu.memoryBase + arg1);
            if (arg2 == 0) {
                throw new DivideByZeroException();
            }
            cpu.accumulator /= arg2;
            cpu.incrementIP(1);
        });

        //ANDI
        INSTRUCTIONS.put(0x18, arg -> {
            if (cpu.accumulator != 0 && arg != 0) {
                cpu.accumulator = 1;
            } else {
                cpu.accumulator = 0;
            }
            cpu.incrementIP(1);
        });

        //AND
        INSTRUCTIONS.put(0x19, arg -> {
            int arg1 = memory.getData(cpu.memoryBase + arg);
            if (cpu.accumulator != 0 && arg1 != 0) {
                cpu.accumulator = 1;
            } else {
                cpu.accumulator = 0;
            }
            cpu.incrementIP(1);
        });

        //NOT
        INSTRUCTIONS.put(0x1A, arg -> {
            if (cpu.accumulator != 0) {
                cpu.accumulator = 0;
            } else {
                cpu.accumulator = 1;
            }
            cpu.incrementIP(1);
        });

        //CMPL
        INSTRUCTIONS.put(0x1B, arg -> {
            if (getData(cpu.memoryBase + arg) < 0) {
                cpu.accumulator = 1;
            } else {
                cpu.accumulator = 0;
            }
            cpu.incrementIP(1);
        });

        //CMPZ
        INSTRUCTIONS.put(0x1C, arg -> {
            if (getData(cpu.memoryBase + arg) == 0) {
                cpu.accumulator = 1;
            } else {
                cpu.accumulator = 0;
            }
            cpu.incrementIP(1);
        });

        //HALT
        INSTRUCTIONS.put(0x1F, arg -> {
            callback.halt();
        });
        jobs[0] = new Job();
        jobs[1] = new Job();
        currentJob = jobs[0];
        jobs[0].setStartcodeIndex(0);
        jobs[0].setStartmemoryIndex(0);
        jobs[1].setStartcodeIndex(Memory.CODE_MAX / 4);
        jobs[1].setStartmemoryIndex(Memory.DATA_SIZE / 4);
        jobs[0].setCurrentState(States.NOTHING_LOADED);
        jobs[1].setCurrentState(States.NOTHING_LOADED);

    }

    public int getAccumulator() {
        return cpu.accumulator;
    }

    public void setAccumulator(int accumulator) {
        this.cpu.accumulator = accumulator;
    }

    public int getInstructionPointer() {
        return cpu.instructionPointer;
    }

    public void setInstructionPointer(int instructionPointer) {
        this.cpu.instructionPointer = instructionPointer;
    }

    public int[] getData() {
        return memory.getDataArray();
    }

    public int getData(int index) {
        return memory.getData(index);
    }

    public void setData(int index, int value) {
        memory.setData(index, value);
    }

    public Instruction get(int index) {
        return INSTRUCTIONS.get(index);
    }

    public Job getCurrentJob() {
        return this.currentJob;
    }

    public void setJob(int i) {
        if (i != 0 || i != 1) {
            throw new IllegalArgumentException();
        }
        currentJob = jobs[i];
        cpu.accumulator = currentJob.getCurrentAcc();
        cpu.instructionPointer = currentJob.getCurrentIP();
        cpu.memoryBase = currentJob.getStartmemoryIndex();
    }

    public void clearJob() {

    }

    public void setCurrentAcc() {
        currentJob.setCurrentAcc(cpu.accumulator);
    }

    public void setCurrentIP() {
        currentJob.setCurrentIP(cpu.instructionPointer);
    }

    private class CPU {
        private int accumulator, instructionPointer, memoryBase;

        public void incrementIP(int val) {
            instructionPointer += val;
        }
    }


}
