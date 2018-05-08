package project;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FullAssembler implements Assembler {

	@Override
	public int assemble(String inputFileName, String outputFileName, StringBuilder error) {
		ArrayList<String> codeLines = new ArrayList<>();
		ArrayList<String> dataLines = new ArrayList<>();
		ArrayList<String> errors = new ArrayList<>();
		ArrayList<Integer> lineNumbers = new ArrayList<>();
		int lineNumber = 0;
		
		try(Scanner input = new Scanner(inputFileName)) {
			boolean beforeData = true;
			String temp = input.nextLine();
			while(beforeData) {
				if(temp.trim().toUpperCase().equals("DATA")) {
					beforeData = false;
					temp = input.nextLine();
					lineNumber += 1;
				}
				if(!temp.trim().equals("DATA")) {
					throw new IllegalDataException();
				}
				if(temp.trim().length() == 0 && input.hasNextLine()) {
					temp = input.nextLine();
					lineNumber += 1;
					throw new IllegalBlankLineException();
				}
				if(temp.charAt(0) == ' ' || temp.charAt(0) == '\t') {
					codeLines.add(temp.trim());
					temp = input.nextLine();
					lineNumber += 1;
					throw new IllegalWhiteSpaceException();
				}
				else {
					codeLines.add(temp);
					temp = input.nextLine();
					lineNumber += 1;
				}
			}
			while (input.hasNextLine()) {
				if(temp.trim().toUpperCase().equals("DATA")) {
					throw new ExtraDataException();
				}
				if(temp.charAt(0) == ' ' || temp.charAt(0) == '\t') {
					dataLines.add(temp.trim());
					temp = input.nextLine();
					lineNumber += 1;
					throw new IllegalWhiteSpaceException();
				}
				if(temp.trim().length() == 0 && input.hasNextLine()) {
					temp = input.nextLine();
					lineNumber += 1;
					throw new IllegalBlankLineException();
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
					throw new IllegalMnemonicException();
				}
				if(InstrMap.toCode.keySet().contains(parts[0].toUpperCase()) && !InstrMap.toCode.keySet().contains(parts[0])) {
					lineNumber += 1;
					throw new MnemonicMustBeUpperCaseException();
				}
				if(Assembler.noArgument.contains(parts[0])) {
					if(parts.length != 1) {
						lineNumber += 1;
						throw new NoArgumentsExcpetion();
					}
				}
				if(!Assembler.noArgument.contains(parts[0])) {
					if(parts.length > 2) {
						lineNumber += 1;
						throw new ToManyArgumentsException();
					}
					if(parts.length < 2) {
						lineNumber += 1;
						throw new MissingArgumentsException();
					}
				}
				try{
				int arg = Integer.parseInt(parts[1], 16);
				} catch(NumberFormatException e) {
					error.append("\nError on line " + (lineNumber) + ": argument is not a hex number");
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
						error.append("\nError on line " + (lineNumber) + ": data has non-numeric memory address");
					}
			}
			SimpleAssembler noError = new SimpleAssembler();
			return noError.assemble(inputFileName, outputFileName, error);
		}
		catch(IllegalDataException e) {
			error.append("\nLine does not have DATA in upper case");
			return lineNumber;
		}
		catch(IllegalBlankLineException e) {
			error.append("\nIllegal blank line in the source file");
			return lineNumber;
		}
		catch(IllegalWhiteSpaceException e) {
			error.append("\nLine starts with illegal white space");
			return lineNumber;
		}
		catch(ExtraDataException e) {
			error.append("\nLine has DATA when a previous line already has DATA");
			return lineNumber;
		}
		catch(IllegalMnemonicException e) {
			error.append("\nError on line " + lineNumber + ": illegal mnemonic");
			return lineNumber;
		}
		catch(MnemonicMustBeUpperCaseException e) {
			error.append("\nError on line " + lineNumber + ": mnemonic must be upper case");
			return lineNumber;
		}
		catch(NoArgumentsExcpetion e) {
			error.append("\nError on line " + lineNumber + ": this mnemonic cannot take arguments");
			return lineNumber;
		}
		catch(ToManyArgumentsException e) {
			error.append("\nError on line " + lineNumber + ": this mnemonic has too many arguments");
			return lineNumber;
		}
		catch(MissingArguementsException e) {
			error.append("\nError on line " + lineNumber + ": this mnemonic is missing an argument");
			return lineNumber;
		}
		catch (FileNotFoundException e) {
			error.append("\nError: Unable to write the assembled program to the output file");
			return -1;
		}
		catch (IOException e) {
			error.append("\nUnexplained IO Exception");
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
