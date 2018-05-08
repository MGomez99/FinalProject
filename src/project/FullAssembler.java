package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FullAssembler implements Assembler {

	@Override
	public int assemble(String inputFileName, String outputFileName, StringBuilder error) {
        if (error == null) {
            throw new IllegalArgumentException("Coding error: the error buffer is null");
        }
		ArrayList<String> codeLines = new ArrayList<>();
		ArrayList<String> dataLines = new ArrayList<>();
		int lineNumber = 0;

        try (Scanner input = new Scanner(new File(inputFileName))) {
			boolean beforeData = true;
			String temp = input.nextLine();
			while(beforeData) {
				if(temp.trim().toUpperCase().equals("DATA")) {
					beforeData = false;
					temp = input.nextLine();
					lineNumber += 1;
				}
				if(!temp.trim().equals("DATA")) {
					error.append("\nLine does not have DATA in upper case");
				}
				if(temp.trim().length() == 0 && input.hasNextLine()) {
					temp = input.nextLine();
					lineNumber += 1;
					error.append("\nIllegal blank line in the source file");
				}
				if(temp.charAt(0) == ' ' || temp.charAt(0) == '\t') {
					codeLines.add(temp.trim());
					temp = input.nextLine();
					lineNumber += 1;
					error.append("\nLine starts with illegal white space");
				}
				else {
					codeLines.add(temp);
					temp = input.nextLine();
					lineNumber += 1;
				}
			}
			while (input.hasNextLine()) {
				if(temp.trim().toUpperCase().equals("DATA")) {
					error.append("\nLine" + lineNumber + "has DATA when a previous line already has DATA");
				}
				if(temp.charAt(0) == ' ' || temp.charAt(0) == '\t') {
					dataLines.add(temp.trim());
					temp = input.nextLine();
					lineNumber += 1;
					error.append("\nLine starts with illegal white space");
				}
				if(temp.trim().length() == 0 && input.hasNextLine()) {
					temp = input.nextLine();
					lineNumber += 1;
					error.append("\nIllegal blank line in the source file");
				}
				else {
					dataLines.add(temp);
					temp = input.nextLine();
					lineNumber += 1;
				}
			}
				
			lineNumber = 0;
				
			for(String e : codeLines) {
				String[] parts = e.trim().split("\\s+");
				if(!InstrMap.toCode.keySet().contains(parts[0])) {
					lineNumber += 1;
					error.append("\nError on line " + lineNumber + ": illegal mnemonic");
				}
				if(InstrMap.toCode.keySet().contains(parts[0].toUpperCase()) && !InstrMap.toCode.keySet().contains(parts[0])) {
					lineNumber += 1;
					error.append("\nError on line " + lineNumber + ": mnemonic must be upper case");
				}
				if(Assembler.noArgument.contains(parts[0])) {
					if(parts.length != 1) {
						lineNumber += 1;
						error.append("\nError on line " + lineNumber + ": this mnemonic cannot take arguments");
					}
				}
				if(!Assembler.noArgument.contains(parts[0])) {
					if(parts.length > 2) {
						lineNumber += 1;
						error.append("\nError on line " + lineNumber + ": this mnemonic has too many arguments");
					}
					if(parts.length < 2) {
						lineNumber += 1;
						error.append("\nError on line " + lineNumber + ": this mnemonic is missing an argument");
					}
				}
				try{
                    int arg = Integer.parseInt(parts[1], 16);
				} catch(NumberFormatException b) {
					error.append("\nError on line " + (lineNumber) + ": argument is not a hex number");
				}
			}
			lineNumber += 1; //This is to account for the "DATA" LINE

            for (String s : dataLines) {
                String[] parts = s.trim().split("\\s+");

                try {
                    int address = Integer.parseInt(parts[0], 16);
                    int value = Integer.parseInt(parts[1], 16);
                } catch (NumberFormatException e) {
                    error.append("\nError on line " + (lineNumber) + ": data has non-numeric memory address");
                }
                lineNumber++;
			}
            if (error.length() == 0) {

                SimpleAssembler noError = new SimpleAssembler();
                return noError.assemble(inputFileName, outputFileName, error);
            } else {
                System.out.println(error);
                return -1;
            }

		}
		catch (FileNotFoundException e) {
            error.append("\nUnable to open the source file)");
			return -1;
		}

    }
	
    public static void main(String[] args) {
        StringBuilder error = new StringBuilder();
        System.out.println("Enter the name of the file without extension: ");
        try (Scanner keyboard = new Scanner(System.in)) {
            String filename = keyboard.nextLine();
            int i = new FullAssembler().assemble(filename + ".pasm",
                    filename + ".pexe", error);
            System.out.println("result = " + i);
        }
    }

}
