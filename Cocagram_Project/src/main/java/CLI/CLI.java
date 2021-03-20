package CLI;


import java.util.Scanner;

public class CLI {
    public static String getCommand(String text, String color) {
        System.out.println(color + text);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if(input.equals("exit"))
            System.exit(0);
        return input;
    }
    public static void print(String text, String color) {
        System.out.println(color + text);
    }
    public static void invalidCommand() {
        print("Invalid Command", ConsoleColors.RED_BOLD);
        print("", ConsoleColors.RESET);
    }
}
