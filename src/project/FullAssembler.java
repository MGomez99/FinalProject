package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FullAssembler implements Assembler {


    public static void main(String[] args) {
        System.out.println(InstrMap.toCode.keySet());
    }

    @Override
    public int assemble(String inputFileName, String outputFileName, StringBuilder error) {
        int retval = -1;
        int lineNumber = 0;
        ArrayList<String> file = new ArrayList<>();
        ArrayList<String> code = new ArrayList<>();
        ArrayList<String> data = new ArrayList<>();
        try (Scanner input = new Scanner(new File(inputFileName))) {
            while (input.hasNextLine()) {
                String line = input.nextLine();
                file.add(line);
            }
        } catch (FileNotFoundException e) {
            error.append("\nUnable to open the source file)");
            return -1;
        }
        //the list 'file' now has the contents of the file; this breaks up the text into 2 array lists
        boolean inData = false;
        for (String line : file) {
            if (line.trim().equals("DATA")) {//go to data
                inData = true;
            } else if (!inData) {
                code.add(line);
            } else {
                data.add(line);
            }
        }
        boolean hasBlank = false;
        int firstBlank = 0;

        //Iterate over code
        for (String line : code) {
            lineNumber += 1;
            if (line.trim().length() == 0 && !hasBlank) {//Theres a blank line
                hasBlank = true;
                firstBlank = lineNumber;
            } else if (line.trim().length() != 0 && hasBlank) {//NON BLANK LINE AFTER BLANK LINE

                error.append("\nError on line " + firstBlank +
                        ": Illegal blank line in the source file");
                retval = firstBlank;
                hasBlank = false;
            } else if (line.trim().length() != 0) {//NON BLANK LINES
                if (line.charAt(0) == ' ' || line.charAt(0) == '\t') {
                    error.append("\nError on line " + lineNumber +
                            "\nLine starts with illegal white space");
                    retval = lineNumber;
                }
                if (line.trim().toUpperCase().equals("DATA")) {
                    if (!line.trim().equals("DATA")) {
                        error.append("\"\\nError on line \" + lineNumber +" +
                                "\nLine does not have DATA in upper case");
                        retval = lineNumber;
                    }
                }
                String[] parts = line.trim().split("\\s+");

                if (!InstrMap.toCode.keySet().contains(parts[0])) {
                    error.append("\nError on line " + lineNumber + ": illegal mnemonic" + parts[0]);
                    retval = lineNumber;
                }
                //does it contain the mnemonic @HERE
                if (InstrMap.toCode.keySet().contains(parts[0].toUpperCase())) {
                    if (!InstrMap.toCode.keySet().contains(parts[0])) {
                        error.append("\nError on line " + lineNumber + ": mnemonic must be upper case");
                        retval = lineNumber;
                    }
                    //there is a correct mnemonic
                    else {
                        if (Assembler.noArgument.contains(parts[0])) {
                            if (parts.length != 1) {
                                error.append("\nError on line " + lineNumber + ": this mnemonic cannot take arguments");
                                retval = lineNumber;
                            }
                        }
                        if (!Assembler.noArgument.contains(parts[0])) {
                            if (parts.length > 2) {
                                error.append("\nError on line " + lineNumber + ": this mnemonic has too many arguments");
                                retval = lineNumber;
                            }
                            if (parts.length < 2) {
                                error.append("\nError on line " + lineNumber + ": this mnemonic is missing an argument");
                                retval = lineNumber;
                            }
                            //has argument, check if it is a number in hex
                            else {
                                try {
                                    int arg = Integer.parseInt(parts[1], 16);
                                } catch (NumberFormatException e) {
                                    error.append("\nError on line " + lineNumber +
                                            ": argument is not a hex number");
                                    retval = lineNumber;

                                }
                            }
                        }
                    }
                }
            }
        }
        //Done with checking code
        lineNumber++; //Accounts for the "DATA" line

        //Iterate over data
        for (String line : data) {
            lineNumber += 1;
            if (line.trim().length() == 0 && !hasBlank) {//Theres a blank line
                hasBlank = true;
                firstBlank = lineNumber;
            } else if (line.trim().length() != 0 && hasBlank) {//NON BLANK LINE AFTER BLANK LINE
                hasBlank = false;
                error.append("\nError on line " + firstBlank +
                        ": Illegal blank line in the source file");
                retval = firstBlank;
            } else if (line.trim().length() != 0) {//NON BLANK LINES
                String[] parts = line.trim().split("\\s+");
                if (line.charAt(0) == ' ' || line.charAt(0) == '\t') {
                    error.append("\nError on line " + lineNumber +
                            ": Line starts with illegal white space");
                    retval = lineNumber;
                }
                if (line.trim().toUpperCase().equals("DATA")) {
                    error.append("\nError on line " + lineNumber +
                            ": There can only be one data segment");
                    retval = lineNumber;
                }
                if (parts.length != 2) {
                    error.append("\nError on line " + lineNumber +
                            ": Line in the data segment is too short");//@HERE2
                    retval = lineNumber;
                }
                try {
                    int address = Integer.parseInt(parts[0], 16);
                    int value = Integer.parseInt(parts[1], 16);
                } catch (NumberFormatException e) {
                    error.append("\nError on line " + lineNumber +
                            ": data has non-numeric memory address");
                    retval = lineNumber;
                }


            }

        }
        if (error.length() == 0) {
            SimpleAssembler noError = new SimpleAssembler();
            return noError.assemble(inputFileName, outputFileName, error);
        } else {
            System.out.println(error);
            return retval;
        }

    }
}
