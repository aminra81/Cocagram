package pages;

import CLI.*;

import models.User;
import pages.Messaging.Messaging;

import java.time.format.DateTimeFormatter;

public class ShowPage {
    private static void getHelp() {
        CLI.print("", ConsoleColors.RESET);
        CLI.print("[1] send message", ConsoleColors.YELLOW);
        CLI.print("[2] block/unblock", ConsoleColors.YELLOW);
        CLI.print("[3] report", ConsoleColors.YELLOW);
        CLI.print("[4] mute/unmute", ConsoleColors.YELLOW);
        CLI.print("[5] follow/unfollow", ConsoleColors.YELLOW);
        CLI.print("", ConsoleColors.BLACK);
    }

    public static void logic(User user, User userToBeVisited) {
        if (!userToBeVisited.isActive()) {
            CLI.print("this user isn't active!", ConsoleColors.RED_BOLD);
            MainPage.logic(user);
        }
        while (true) {
            getInfo(user, userToBeVisited);
            CLI.print("", ConsoleColors.RESET);
            getHelp();
            String command = CLI.getCommand("Enter your command", ConsoleColors.BLACK);
            if (command.equals("back"))
                MainPage.logic(user);
            switch (command) {
                case "1":
                    String content = CLI.getCommand("Enter your message:", ConsoleColors.BLUE);
                    Messaging.sendMessage(user, userToBeVisited, content);
                    break;
                case "2":
                    blockHandling(user, userToBeVisited);
                    break;
                case "3":
                    CLI.print("you reported this user", ConsoleColors.GREEN_BOLD);
                    break;
                case "4":
                    muteHandling(user, userToBeVisited);
                    break;
                case "5":
                    followHandling(user, userToBeVisited);
                    break;
                default:
                    CLI.invalidCommand();
                    break;
            }
        }
    }

    public static void getInfo(User user, User userToBeVisited) {
        CLI.print(String.format("\t\t\t\t%s", userToBeVisited.getUsername()), ConsoleColors.GREEN_BOLD);
        CLI.print("", ConsoleColors.RESET);
        CLI.print("firstname: " + userToBeVisited.getFirstname(), ConsoleColors.CYAN_BOLD);
        CLI.print("lastname: " + userToBeVisited.getLastname(), ConsoleColors.CYAN_BOLD);
        if (userToBeVisited.isPublicData()) {
            if (userToBeVisited.getBirthDate() == null)
                CLI.print("Birthdate: ", ConsoleColors.CYAN_BOLD);
            else
                CLI.print("Birthdate: " + userToBeVisited.getBirthDate().toString(), ConsoleColors.CYAN_BOLD);
            CLI.print("email: " + userToBeVisited.getEmail(), ConsoleColors.CYAN_BOLD);
            CLI.print("phone number: " + userToBeVisited.getPhoneNumber(), ConsoleColors.CYAN_BOLD);
        }
        CLI.print("last seen: " + getLastSeen(user, userToBeVisited), ConsoleColors.CYAN_BOLD);
        if (user.getBlockList().contains(userToBeVisited.getID()))
            CLI.print("you blocked this user.", ConsoleColors.CYAN_BOLD);
        if (user.getMutedUsers().contains(userToBeVisited.getID()))
            CLI.print("you muted this user.", ConsoleColors.CYAN_BOLD);
        if (user.getFollowings().contains(userToBeVisited.getID()))
            CLI.print("you follow this user.", ConsoleColors.CYAN_BOLD);
        else if (userToBeVisited.getRequests().contains(user.getID()))
            CLI.print("you requested this user.", ConsoleColors.CYAN_BOLD);
        else
            CLI.print("you don't follow this user.", ConsoleColors.CYAN_BOLD);
    }

    public static String getLastSeen(User user, User userToBeVisited) {
        if (userToBeVisited.getLastSeenType().equals("Nobody"))
            return "recently";
        if (userToBeVisited.getLastSeenType().equals("Followings") &&
                !userToBeVisited.getFollowings().contains(user.getID()))
            return "recently";
        if (userToBeVisited.getLastSeen() == null)
            return "online";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return userToBeVisited.getLastSeen().format(formatter);
    }

    public static void muteHandling(User user, User userToBeVisited) {
        if (user.getMutedUsers().contains(userToBeVisited.getID())) {
            user.removeFromMutedUsers(userToBeVisited.getID());
            CLI.print("you unmuted this user!", ConsoleColors.GREEN_BOLD);
        } else {
            user.addToMutedUsers(userToBeVisited.getID());
            CLI.print("you muted this user!", ConsoleColors.GREEN_BOLD);
        }
    }

    public static void blockHandling(User user, User userToBeVisited) {
        if (user.getBlockList().contains(userToBeVisited.getID())) {
            user.removeFromBlocklist(userToBeVisited.getID());
            CLI.print("you unblocked this user!", ConsoleColors.GREEN_BOLD);
        } else {
            user.addToBlocklist(userToBeVisited.getID());
            CLI.print("you blocked this user!", ConsoleColors.GREEN_BOLD);
        }
    }

    public static void followHandling(User user, User userToBeVisited) {
        if (user.getFollowings().contains(userToBeVisited.getID())) {
            user.removeFromFollowings(userToBeVisited.getID());
            userToBeVisited.removeFromFollowers(user.getID());
            userToBeVisited.addToNotifications(String.format("user %s unfollowed you!", user.getUsername()));
        } else if (userToBeVisited.getRequests().contains(user.getID()))
            userToBeVisited.removeFromRequests(user.getID());
        else if (userToBeVisited.isPrivate())
            userToBeVisited.addToRequests(user.getID());
        else {
            user.addToFollowings(userToBeVisited.getID());
            userToBeVisited.addToFollowers(user.getID());
            userToBeVisited.addToNotifications(String.format("user %s followed you!", user.getUsername()));
        }
    }
}