package project;

public class SimpleAssembler implements Assembler {

    private boolean readingCode = true;

    private String makeOutputCode(String[] parts) {
        if (parts.length == 1) {
            return InstrMap.toCode.get(parts[0]) + "\n" + 0;
        } else {
            return InstrMap.toCode.get(parts[0]) + "\n" + Integer.parseInt(parts[1], 16);
        }
    }

    private String makeOutputData(String[] parts) {
        return Integer.parseInt(parts[0], 16) + "\n" + Integer.parseInt(parts[1], 16);
    }

    @Override
    public int assemble(String inputFileName, String outputFileName, StringBuilder error) {

        return 0;
    }

}
