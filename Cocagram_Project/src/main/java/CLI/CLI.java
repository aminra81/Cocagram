package CLI;

import java.util.Scanner;

public class CLI {
    static public String getCommand(String text, String color) {
        System.out.println(color + text);
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
    static public void print(String text, String color) {
        System.out.println(color + text);
        System.out.println();
    }
}
