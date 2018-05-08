package project;

import java.util.ArrayList;
import java.util.Scanner;

public class FullAssembler implements Assembler {

	@Override
	public int assemble(String inputFileName, String outputFileName, StringBuilder error) {
		// TODO Auto-generated method stub
		ArrayList<String> codeLines = new ArrayList<>();
		ArrayList<String> dataLines = new ArrayList<>();
		
		try(Scanner input = new Scanner(inputFileName)) {
			boolean beforeData = true;
			String temp = input.nextLine();
			int lineNumber = 0;
			while(beforeData) {
				if(temp.trim().toUpperCase().equals("DATA")) {
					beforeData = false;
					temp = input.nextLine();
					lineNumber += 1;
				}
				if(!temp.trim().equals("DATA")) {
					throw new Exception();
				}
				if(temp.trim().length() == 0) {
					temp = input.nextLine();
					lineNumber += 1;
					throw new Exception();
				}
				if(temp.charAt(0) == ' ' || temp.charAt(0) == '\t') {
					codeLines.add(temp.trim());
					temp = input.nextLine();
					lineNumber += 1;
					throw new Exception();
				}
				else {
					codeLines.add(temp);
					temp = input.nextLine();
					lineNumber += 1;
				}
			}
			while (input.hasNextLine()) {
				if(temp.trim().toUpperCase().equals("DATA")) {
					throw new Exception();
				}
				if(temp.charAt(0) == ' ' || temp.charAt(0) == '\t') {
					dataLines.add(temp.trim());
					temp = input.nextLine();
					lineNumber += 1;
					throw new Exception();
				}
				if(temp.trim().length() == 0 && input.hasNextLine()) {
					temp = input.nextLine();
					lineNumber += 1;
					throw new Exception();
				}
				else {
					dataLines.add(temp);
					temp = input.nextLine();
					lineNumber += 1;
				}
				
				lineNumber = 0;
				
				for(String e : codeLines) {
					String[] parts = e.trim().split("\\s+");
					if(!InstrMap.toCode.keySet().contains(parts[0])) {
						lineNumber += 1;
						throw new Exception();
					}
					if(InstrMap.toCode.keySet().contains(parts[0].toUpperCase()) && !InstrMap.toCode.keySet().contains(parts[0])) {
						lineNumber += 1;
						throw new Exception();
					}
					if(Assembler.noArgument.contains(parts[0])) {
						if(parts.length != 1) {
							lineNumber += 1;
							throw new Exception();
						}
					}
					if(!Assembler.noArgument.contains(parts[0])) {
						if(parts.length != 2) {
							lineNumber += 1;
							throw new Exception();
						}
					}
					try{
						int arg = Integer.parseInt(parts[1], 16);
					} catch(NumberFormatException e) {
						error.append("\nError on line " + (i+1) + ": argument is not a hex number");
					}
				}
				lineNumber += 1; //This is to account for the "DATA" LINE
				for(String e: dataLines) {
					String[] parts = e.trim().split("\\s+");
					if(parts.length != 2) {
						lineNumber += 1;
						throw new Exception();
					}
					try {
						int address = Integer.parseInt(parts[0], 16);
						int value = Integer.parseInt(parts[1], 16);
					} catch (NumberFormatException e){
						error.append("\nError on line " + (offset+i+1) + ": data has non-numeric memory address");
					}
				}
			}
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
