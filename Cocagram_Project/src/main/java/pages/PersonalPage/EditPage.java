package pages.PersonalPage;
import CLI.*;

import CLI.CLI;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.EnterPage.SignUp;

public class EditPage {

    static private final Logger logger = LogManager.getLogger(EditPage.class);

    public static void getHelp() {
        CLI.print("", ConsoleColors.BLACK);
        CLI.print("Ok here you can change your Coca.", ConsoleColors.BLACK_BOLD);
        CLI.print("[1] firstname", ConsoleColors.YELLOW);
        CLI.print("[2] lastname", ConsoleColors.YELLOW);
        CLI.print("[3] bio", ConsoleColors.YELLOW);
        CLI.print("[4] birthDate", ConsoleColors.YELLOW);
        CLI.print("[5] email", ConsoleColors.YELLOW);
        CLI.print("[6] phone number", ConsoleColors.YELLOW);
        CLI.print("", ConsoleColors.BLACK);
    }
    public static void logic(User user) {
        while (true) {
            getHelp();
            String command = CLI.getCommand("Enter your command:", ConsoleColors.BLACK);
            if(command.equals("back"))
                break;
            switch (command) {
                case "1":
                    user.setFirstname(SignUp.getFirstname());
                    logger.info(String.format("user %s changed his/her first name.", user.getUsername()));
                    break;
                case "2":
                    user.setLastname(SignUp.getLastname());
                    logger.info(String.format("user %s changed his/her last name.", user.getUsername()));
                    break;
                case "3":
                    user.setBio(CLI.getCommand("", ConsoleColors.CYAN_BOLD));
                    logger.info(String.format("user %s changed his/her bio.", user.getUsername()));
                    break;
                case "4":
                    user.setBirthDate(SignUp.getBirthDate());
                    logger.info(String.format("user %s changed his/her birthdate.", user.getUsername()));
                    break;
                case "5":
                    user.setEmail(SignUp.getEmail());
                    logger.info(String.format("user %s changed his/her email.", user.getUsername()));
                    break;
                case "6":
                    user.setPhoneNumber(SignUp.getPhoneNumber());
                    logger.info(String.format("user %s changed his/her phone number.", user.getUsername()));
                    break;
                default:
                    CLI.invalidCommand();
                    break;
            }
        }
    }
}
