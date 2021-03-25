package pages.EnterPage;

import CLI.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class EnterPage{

    static private final Logger logger = LogManager.getLogger(EnterPage.class);

    private static void getHelp() {
        CLI.print("type \"sign_in\" to sign in.\n" +
                "type \"sign_up\" to sign up.", ConsoleColors.YELLOW);
        CLI.print("", ConsoleColors.RESET);
    }

    static public void logic() {
        CLI.print("", ConsoleColors.BLACK);
        CLI.print("\t\t\t\tenter page", ConsoleColors.BLACK_BOLD);
        String command = CLI.getCommand("Enter your command:", ConsoleColors.BLACK);
        switch(command) {
            case "-help":
                getHelp();
                logic();
                break;
            case "sign_in":
                SignIn.logic();
                break;
            case "sign_up":
                SignUp.logic();
                break;
            default:
                CLI.invalidCommand();
                logic();
                break;
        }
    }

    public static User getUser(String username) {
        try {
            File dbDirectory = new File("./src/main/resources/DB/Users");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            for (File userFile : dbDirectory.listFiles()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(userFile));
                logger.info(String.format("file %s opened!", userFile.getName()));
                User currentUser = gson.fromJson(bufferedReader, User.class);
                bufferedReader.close();
                logger.info(String.format("file %s closed!", userFile.getName()));
                if(currentUser.getUsername().equals(username))
                    return currentUser;
            }
        } catch (IOException e) {
            logger.warn("an exception occurred while trying to get user with given username file.");
            e.printStackTrace();
        }
        return null;
    }

    public static User getPhoneNumber(String phoneNumber) {
        try {
            File dbDirectory = new File("./src/main/resources/DB/Users");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            for (File userFile : dbDirectory.listFiles()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(userFile));
                logger.info(String.format("file %s opened!", userFile.getName()));
                User currentUser = gson.fromJson(bufferedReader, User.class);
                logger.info(String.format("file %s closed!", userFile.getName()));
                bufferedReader.close();
                if(currentUser.getPhoneNumber().equals(phoneNumber))
                    return currentUser;
            }
        } catch (IOException e) {
            logger.warn("an exception occurred while trying to get user with given phone number file.");
            e.printStackTrace();
        }
        return null;
    }

    public static User getEmail(String email) {
        try {
            File dbDirectory = new File("./src/main/resources/DB/Users");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            for (File userFile : dbDirectory.listFiles()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(userFile));
                logger.info(String.format("file %s opened!", userFile.getName()));
                User currentUser = gson.fromJson(bufferedReader, User.class);
                logger.info(String.format("file %s closed!", userFile.getName()));
                bufferedReader.close();
                if(currentUser.getEmail().equals(email))
                    return currentUser;
            }
        } catch (IOException e) {
            logger.warn("an exception occurred while trying to get user with given email file.");
            e.printStackTrace();
        }
        return null;
    }
}
