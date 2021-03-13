package pages.PersonalPage;
import CLI.*;

import CLI.CLI;
import models.User;
import pages.EnterPage.SignUp;

public class EditPage {
    public static void getHelp() {
        CLI.print("Ok here you can change your Coca.", ConsoleColors.BLACK_BOLD);
        CLI.print("[1] firstname", ConsoleColors.RED);
        CLI.print("[2] lastname", ConsoleColors.RED);
        CLI.print("[3] bio", ConsoleColors.RED);
        CLI.print("[4] birthDate", ConsoleColors.RED);
        CLI.print("[5] email", ConsoleColors.RED);
        CLI.print("[6] phone number", ConsoleColors.RED);
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
                    break;
                case "2":
                    user.setLastname(SignUp.getLastname());
                    break;
                case "3":
                    user.setBio(CLI.getCommand("", ConsoleColors.CYAN_BOLD));
                    break;
                case "4":
                    user.setBirthDate(SignUp.getBirthDate());
                    break;
                case "5":
                    user.setEmail(SignUp.getEmail());
                    break;
                case "6":
                    user.setPhoneNumber(SignUp.getPhoneNumber());
                    break;
            }
        }
    }
}
