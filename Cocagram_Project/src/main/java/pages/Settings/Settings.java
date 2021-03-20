package pages.Settings;
import CLI.*;
import models.User;
import pages.EnterPage.EnterPage;
import pages.EnterPage.SignUp;

import java.time.LocalDateTime;

public class Settings{
    static void getHelp() {
        CLI.print("", ConsoleColors.RESET);
        CLI.print("\t\t\t\tsettings", ConsoleColors.BLACK_BOLD);
        CLI.print("[1] privacy settings", ConsoleColors.YELLOW);
        CLI.print("[2] log out", ConsoleColors.YELLOW);
        CLI.print("[3] delete account", ConsoleColors.YELLOW);
        CLI.print("", ConsoleColors.BLACK);
    }
    public static void logic(User user) {
        while(true) {
            getHelp();
            String command = CLI.getCommand("Enter your command:", ConsoleColors.BLACK);
            if(command.equals("back"))
                return;
            switch (command) {
                case "1":
                    privacyLogic(user);
                    break;
                case "2":
                    logout(user);
                    break;
                case "3":
                    //delete
                    break;
                default:
                    CLI.invalidCommand();
                    break;
            }
        }
    }

    static void getHelpPrivacy() {
        CLI.print("", ConsoleColors.RESET);
        CLI.print("\t\t\t\tprivacy settings", ConsoleColors.BLACK_BOLD);
        CLI.print("[1] public/private account", ConsoleColors.YELLOW);
        CLI.print("[2] last seen settings", ConsoleColors.YELLOW);
        CLI.print("[3] deactivate", ConsoleColors.YELLOW);
        CLI.print("[4] change password", ConsoleColors.YELLOW);
        CLI.print("", ConsoleColors.BLACK);
    }
    public static void logout(User user) {
        user.setLastSeen(LocalDateTime.now());
        EnterPage.logic();
    }
    public static void privacyLogic(User user) {
        while(true) {
            getHelpPrivacy();
            String command = CLI.getCommand("Enter your command", ConsoleColors.BLACK);
            if(command.equals("back"))
                return;
            switch (command) {
                case "1":
                    if(user.isPrivate()) {
                        String input = CLI.getCommand("your account is private. do you want to make it public? (Y/N)",
                                ConsoleColors.BLACK);
                        if(input.equals("Y"))
                            user.setPrivate(false);
                    }
                    else {
                        String input = CLI.getCommand("your account is public. do you want to make it private? (Y/N)",
                                ConsoleColors.BLACK);
                        if(input.equals("Y"))
                            user.setPrivate(true);
                    }
                    break;
                case "2":
                    String lastSeenType = CLI.getCommand("Enter your last seen type (Everyone, Followings, Nobody):",
                            ConsoleColors.BLACK);
                    user.setLastSeenType(lastSeenType);
                    break;
                case "3":
                    user.setActive(false);
                    logout(user);
                    break;
                case "4":
                    String password = SignUp.getPassword();
                    user.setPassword(password);
                    logout(user);
                    break;
                default:
                    CLI.invalidCommand();
            }
        }
    }
}
