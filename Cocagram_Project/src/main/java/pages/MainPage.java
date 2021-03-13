package pages;

import models.User;
import CLI.*;
import pages.PersonalPage.PersonalPage;

public class MainPage {
    static void getHelp() {
        CLI.print("[1] personal page", ConsoleColors.BLACK);
        CLI.print("[2] timeline", ConsoleColors.BLACK);
        CLI.print("[3] explorer", ConsoleColors.BLACK);
        CLI.print("[4] messaging", ConsoleColors.BLACK);
        CLI.print("[5] settings", ConsoleColors.BLACK);
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
