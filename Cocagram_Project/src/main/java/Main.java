import CLI.CLI;
import CLI.ConsoleColors;
import pages.EnterPage.EnterPage;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Main {
    static private final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {

        logger.info("program started!");

        CLI.print("Welcom to Cocagram.\n" +
                "this is the social media you'are looking for.\n" +
                "Have a Coca-cola before entering the app ;)", ConsoleColors.RED);
        CLI.print("", ConsoleColors.RESET);
        EnterPage.logic();
    }
}
