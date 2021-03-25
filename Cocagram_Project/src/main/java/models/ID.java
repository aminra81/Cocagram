package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ID {

    static private final Logger logger = LogManager.getLogger(ID.class);
    private static final File idFile = new File("./src/main/resources/DB/lastID.txt");
    int idNum;

    public ID(boolean toBeGen) {
        Scanner myReader;
        int lastID = 0;
        try {
            myReader = new Scanner(idFile);
            logger.info("file lastID.txt opened.");
            lastID = myReader.nextInt() + 1;
            myReader.close();
        } catch (FileNotFoundException e) {
            logger.error("lastID.txt not found");
            e.printStackTrace();
        }

        PrintStream myPrinter;
        try {
            myPrinter = new PrintStream(idFile);
            myPrinter.print(lastID);
            myPrinter.close();
            logger.info("file lastID.txt closed.");
        } catch (FileNotFoundException e) {
            logger.error("lastID.txt not found!");
            e.printStackTrace();
        }
        this.idNum = lastID;
    }

    @Override
    public String toString() {
        return String.valueOf(idNum);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ID id = (ID) o;
        return id.idNum == idNum;
    }
}
