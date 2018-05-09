package project;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FullAssembler implements Assembler {
	private boolean hasBlank = false;
	private int retval = 0;
	private int lineNumber = 0;
	private int firstBlank = 0;
	private boolean readingCode = true;

	@Override
	public int assemble(String inputFileName, String outputFileName, StringBuilder error) {
		if (error.equals(null)) {
			throw new IllegalArgumentException("Error Buffer cannot be Null");
		}
		File file = new File(inputFileName);
		ArrayList<String> code = new ArrayList<>();
		ArrayList<String> data = new ArrayList<>();
		try {
			Scanner input = new Scanner(file);
			while (input.hasNextLine()) {
				String line = input.nextLine();
				String[] parts = line.trim().split("\\s+");
				lineNumber++;
				if (line.trim().length() == 0 && !hasBlank) {
					hasBlank = true;
					firstBlank = lineNumber;
					continue;
				}
				if (line.trim().length() == 0 && hasBlank) {
					continue;
				}
				if (hasBlank && line.trim().length() != 0) {
					error.append("Illegal blank line found at " + firstBlank + "\n");
					retval = firstBlank;
					hasBlank = false;
				}
				if ((Character.toString(line.charAt(0)).equals(" ") || Character.toString(line.charAt(0)).equals("\t"))) {
					error.append("Line " + lineNumber + " starts with illegal white space" + "\n");
					retval = lineNumber;
				}
				if (line.trim().toUpperCase().equals("DATA") && readingCode) {
					if (!line.trim().equals("DATA")) {
						error.append("DATA must be uppercase at Line " + lineNumber + "\n");
						retval = lineNumber;
					}
					readingCode = false;
					continue;
				}
				if (line.trim().toUpperCase().equals("DATA") && !readingCode) {
					error.append("Illegal DATA tag at Line " + lineNumber + "\n");
					retval = lineNumber;
				}
				if (!InstrMap.toCode.keySet().contains(parts[0].toUpperCase()) && readingCode) {
					error.append("\nError on line " + lineNumber + ": illegal mnemonic" + parts[0]);
					retval = lineNumber;
				}
				if (!InstrMap.toCode.keySet().contains(parts[0]) && readingCode && InstrMap.toCode.keySet().contains(parts[0].toUpperCase())) {
					error.append("\nError on line " + lineNumber + ": mnemonic must be upper case");
					retval = lineNumber;
				}
				if (Assembler.noArgument.contains(parts[0]) && (parts.length != 1 && readingCode)) {
					error.append("\nError on line " + lineNumber + ": this mnemonic cannot take arguments");
					retval = lineNumber;
				}
				if (!Assembler.noArgument.contains(parts[0]) && (parts.length != 2 && readingCode)) {
					if ((parts.length > 2)) {
						error.append("Mnemonic on Line " + lineNumber + " has too many arguments" + "\n");
						retval = lineNumber;
					}
					if ((parts.length < 2)) {
						error.append("Mnemonic on Line " + lineNumber + " has too few arguments" + "\n");
						retval = lineNumber;
					}
				}
				if (!readingCode && parts.length != 2) {
					error.append("Data at Line " + lineNumber + " must have address and value" + "\n");
				}
				if (!readingCode) {
					try {
						Integer.parseInt(parts[0], 16);
					} catch (NumberFormatException e) {
						error.append("\nError on line " + lineNumber +
								": address is not a hex number");
						retval = lineNumber;
					}
					if (parts.length > 1) {
						try {
							Integer.parseInt(parts[1], 16);
						} catch (NumberFormatException e) {
							error.append("\nError on line " + lineNumber +
									": value must be a hex number");
							retval = lineNumber;
						}
					}
				} else {
					if (parts.length > 1) {
						try {
							Integer.parseInt(parts[1], 16);
						} catch (NumberFormatException e) {
							error.append("\nError on line " + lineNumber +
									": argument must be a hex number");
							retval = lineNumber;
						}
					}
				}
				if (readingCode) {
					code.add(line);
				} else {
					data.add(line);
				}
			}
			input.close();
		} catch (FileNotFoundException e) {
			error.append("\nError: Unable to write the assembled program to the output file");
			retval = -1;
		} catch (IOException e) {
			error.append("\nUnexplained IO Exception");
			retval = -1;
		}
		if (retval == 0) {
			new SimpleAssembler().assemble(inputFileName, outputFileName, error);
		}
		if (error.length() != 0 || retval != 0) {
			System.out.println(error);
		}
		return retval;

	}

}