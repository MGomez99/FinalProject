package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Loader {
    public static String load(MachineModel model, File file, int codeOffset, int memoryOffset){
        int codeSize = 0;
        if(model == null || file == null){ return null;}

        try(Scanner input = new Scanner(file)){
            boolean incode = true;
            while(input.hasNextLine()){
                String line1 = input.nextLine();
                String line2 = input.nextLine();
                Scanner parser = new Scanner(line1 + " " + line2);
                if(incode && parser.nextInt() == -1){
                    incode = false;
                }
                int nextint = parser.nextInt();
                if(incode && nextint != -1){
                    int arg = parser.nextInt();
                    model.setCode(codeOffset+codeSize, nextint, arg); //@TODO This might be wrong
                    codeSize++;
                }
                if(!incode){
                    int address = parser.nextInt();
                    int value = parser.nextInt();
                    model.setData(memoryOffset + address, value);
                    //Write the address and value to memory using model.setData(address+memoryOffset, value). The memory location MUST be offset
                    //Control-F that ^^^^^ Up to (iii) case

                }
            }
        }catch(FileNotFoundException e){

        }
    }
}
