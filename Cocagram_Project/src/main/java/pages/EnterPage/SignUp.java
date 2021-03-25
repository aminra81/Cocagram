package pages.EnterPage;

import CLI.*;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

public class SignUp {

    static private final Logger logger = LogManager.getLogger(SignUp.class);

    public static void logic() {
        String username = getUsername();

        String firstname = getFirstname();

        String lastname = getLastname();

        String bio = CLI.getCommand("Enter a Coca bio:", ConsoleColors.BLACK);

        LocalDate birthDate = getBirthDate();

        String email = getEmail();

        String phoneNumber = getPhoneNumber();

        String password = getPassword();

        boolean publicData = (CLI.getCommand("Do you want your data to be public? (birthdate, phone number, email) (Y/N):",
                ConsoleColors.BLACK).equals("Y"));

        String lastSeenType = CLI.getCommand("Enter your last seen type (Everyone, Followings, Nobody):",
                ConsoleColors.BLACK);

        new User(username, firstname, lastname, bio, birthDate, email, phoneNumber, password, publicData, lastSeenType);

        logger.info(String.format("user %s registered.", username));

        CLI.print("Your account is registered now. don't forget about Coca.", ConsoleColors.GREEN_BOLD);
        EnterPage.logic();
    }

    public static String getUsername() {
        String username = CLI.getCommand("Enter your username:", ConsoleColors.BLACK);
        if (username.equals("")) {
            CLI.print("you have to fill this field!", ConsoleColors.RED_BOLD);
            return getUsername();
        }
        if (EnterPage.getUser(username) != null) {
            CLI.print("This username exists. don't use other Cocas!", ConsoleColors.RED_BOLD);
            return getUsername();
        }
        return username;
    }

    public static String getFirstname() {
        String firstname = CLI.getCommand("Enter your firstname:", ConsoleColors.BLACK);
        if (firstname.equals("")) {
            CLI.print("you have to fill this field!", ConsoleColors.RED_BOLD);
            return getFirstname();
        }
        return firstname;
    }

    public static String getLastname() {
        String lastname = CLI.getCommand("Enter your lastname:", ConsoleColors.BLACK);
        if (lastname.equals("")) {
            CLI.print("you have to fill this field!", ConsoleColors.RED_BOLD);
            return getLastname();
        }
        return lastname;
    }

    public static String getPassword() {
        String password = CLI.getCommand("Enter your Password:", ConsoleColors.BLACK);
        if (password.equals("")) {
            CLI.print("you have to fill this field!", ConsoleColors.RED_BOLD);
            return getPassword();
        }
        return password;
    }

    public static LocalDate getBirthDate() {
        String date = CLI.getCommand("Enter your birthdate:", ConsoleColors.BLACK);
        if (date.equals(""))
            return null;
        else
            try {
                return LocalDate.parse(date);
            } catch (Exception e) {
                CLI.print("this date isn't valid.", ConsoleColors.RED_BOLD);
                return getBirthDate();
            }
    }

    public static String getEmail() {
        String email = CLI.getCommand("Enter your email:", ConsoleColors.BLACK);
        if (email.equals("")) {
            CLI.print("you have to fill this field!", ConsoleColors.RED_BOLD);
            return getEmail();
        }
        if (EnterPage.getEmail(email) != null) {
            CLI.print("This email is already registered.", ConsoleColors.RED_BOLD);
            return getEmail();
        }
        return email;
    }

    public static String getPhoneNumber() {
        String phoneNumber = CLI.getCommand("Enter your phone number:", ConsoleColors.BLACK);
        if (!phoneNumber.equals("") && EnterPage.getPhoneNumber(phoneNumber) != null) {
            CLI.print("This phone number is already registered.", ConsoleColors.RED_BOLD);
            return getPhoneNumber();
        }
        return phoneNumber;
    }
}
