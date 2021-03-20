package pages;

import models.User;
import CLI.*;
import pages.Messaging.Messaging;
import pages.PersonalPage.PersonalPage;
import pages.Settings.Settings;

public class MainPage {
    static void getHelp() {
        CLI.print("", ConsoleColors.RESET);
        CLI.print("\t\t\t\tmain page", ConsoleColors.BLACK_BOLD);
        CLI.print("[1] personal page", ConsoleColors.YELLOW);
        CLI.print("[2] timeline", ConsoleColors.YELLOW);
        CLI.print("[3] explorer", ConsoleColors.YELLOW);
        CLI.print("[4] messaging", ConsoleColors.YELLOW);
        CLI.print("[5] settings", ConsoleColors.YELLOW);
        CLI.print("", ConsoleColors.BLACK);
    }

    public static void logic(User user) {
        while (true) {
            getHelp();
            String command = CLI.getCommand("Enter the page you wanna check:", ConsoleColors.RESET);
            switch (command) {
                case "1":
                    PersonalPage.logic(user);
                    break;
                case "2":
                    Timeline.logic(user);
                    break;
                case "3":
                    Explorer.logic(user);
                    break;
                case "4":
                    Messaging.logic(user);
                    break;
                case "5":
                    Settings.logic(user);
                    break;
                default:
                    CLI.invalidCommand();
                    break;
            }
        }
    }
}
