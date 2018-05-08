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
			while(beforeData) {
				if(temp == "DATA") {
					beforeData = false;
				}
				else {
					codeLines.add(temp);
					temp = input.nextLine();
				}
			}
			while (beforeData == false && temp != "") {
				if(temp == "DATA") {
					temp = input.nextLine();
				}
				else {
					dataLines.add(temp);
					temp = input.nextLine();
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
