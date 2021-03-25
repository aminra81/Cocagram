package pages.EnterPage;

import CLI.*;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.MainPage;



public class SignIn {

    static private final Logger logger = LogManager.getLogger(SignIn.class);

    public static void logic() {
        String username = CLI.getCommand("Enter your username:", ConsoleColors.BLACK);
        User user = EnterPage.getUser(username);
        if(user == null) {
            CLI.print("this username doesn't exist. get a Coca and try again.", ConsoleColors.RED_BOLD);
            logic();
        }
        else {
            String password = CLI.getCommand("Enter your password:", ConsoleColors.BLACK);
            if(!password.equals(user.getPassword())) {
                CLI.print("password isn't correct. you've drunk a lot of Coca.", ConsoleColors.RED_BOLD);
                logic();
            }
            user.setLastSeen(null);
            user.setActive(true);
            CLI.print("\t\t\t\tWelcome back " + user.getUsername() +"\n", ConsoleColors.BLUE_BOLD);

            logger.info(String.format("user %s signed in.", user.getUsername()));
            MainPage.logic(user);
        }
    }
}
