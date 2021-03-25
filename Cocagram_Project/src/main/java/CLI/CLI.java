package CLI;

import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class CLI {

    static private final Logger logger = LogManager.getLogger(CLI.class);

    public static String getCommand(String text, String color) {
        System.out.println(color + text);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if(input.equals("exit")) {
            logger.info("program terminated.");
            System.exit(0);
        }
        return input;
    }
    public static void print(String text, String color) {
        System.out.println(color + text);
    }
    public static void invalidCommand() {
        print("Invalid Command", ConsoleColors.RED_BOLD);
        logger.info("user entered an invalid command!");
        print("", ConsoleColors.RESET);
    }
}
