package models;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class ID {
    private static final File idFile = new File("./src/main/resources/DB/lastID.txt");
    int idNum;
    public ID(boolean toBeGen) {
        Scanner myReader;
        int lastID = 0;
        try {
            myReader = new Scanner(idFile);
            lastID = myReader.nextInt() + 1;
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        PrintStream myPrinter;
        try {
            myPrinter = new PrintStream(idFile);
            myPrinter.print(lastID);
            myPrinter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.idNum = lastID;
    }

    public String toString() {
        return String.valueOf(idNum);
    }
}
