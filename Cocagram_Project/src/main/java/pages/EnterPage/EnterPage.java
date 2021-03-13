package pages.EnterPage;

import CLI.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class EnterPage{
    static void getHelp() {
        CLI.print("type \"sign_in\" to sign in.\n" +
                "type \"sign_up\" to sign up.", ConsoleColors.RESET);
    }

    static public void logic() {
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
                User currentUser = gson.fromJson(bufferedReader, User.class);
                if(currentUser.getUsername().equals(username))
                    return currentUser;
            }
        } catch (IOException e) {
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
                User currentUser = gson.fromJson(bufferedReader, User.class);
                if(currentUser.getPhoneNumber().equals(phoneNumber))
                    return currentUser;
            }
        } catch (IOException e) {
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
                User currentUser = gson.fromJson(bufferedReader, User.class);
                if(currentUser.getEmail().equals(email))
                    return currentUser;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
