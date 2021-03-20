package pages.EnterPage;

import CLI.*;
import models.User;
import pages.MainPage;


public class SignIn {
    public static void logic() {
        String username = CLI.getCommand("Enter your username:", ConsoleColors.BLACK);
        User user = EnterPage.getUser(username);
        if(user == null) {
            CLI.print("this username doesn't exist. get a Coca and try again.", ConsoleColors.RED_BOLD);
            logic();
        }
        String password = CLI.getCommand("Enter your password:", ConsoleColors.BLACK);
        if(!password.equals(user.getPassword())) {
            CLI.print("password isn't correct. you've drunk a lot of Coca.", ConsoleColors.RED_BOLD);
            logic();
        }
        user.setLastSeen(null);
        user.setActive(true);
        CLI.print("\t\t\t\tWelcome back " + user.getUsername() +"\n", ConsoleColors.BLUE_BOLD);
        MainPage.logic(user);
    }
}
