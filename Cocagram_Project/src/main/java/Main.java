import CLI.CLI;
import CLI.ConsoleColors;
import pages.EnterPage.EnterPage;

public class Main {
    public static void main(String[] args) {
        CLI.print("Welcom to Cocagram.\n" +
                "this is the social media you'are looking for.\n" +
                "Have a Coca-cola before entering the app ;)", ConsoleColors.RED);
        CLI.print("", ConsoleColors.RESET);
        EnterPage.logic();
    }
}
