package pages.PersonalPage;

import CLI.*;

import models.User;

public class Notifications {
    public static void getHelp() {
        CLI.print("", ConsoleColors.BLACK);
        CLI.print("\t\t\t\tNotifications", ConsoleColors.BLACK_BOLD);
        CLI.print("[1] requests", ConsoleColors.YELLOW);
        CLI.print("[2] system messages", ConsoleColors.YELLOW);
        CLI.print("", ConsoleColors.BLACK);
    }

    public static void logic(User user) {
        while (true) {
            getHelp();
            String command = CLI.getCommand("Enter your command:", ConsoleColors.BLACK);
            if (command.equals("back"))
                break;
            switch (command) {
                case "1":
                    getRequests(user);
                    break;
                case "2":
                    getSystemMessages(user);
                    break;
                default:
                    CLI.invalidCommand();
                    break;
            }
        }
    }

    private static void getRequests(User user) {
        while (true) {
            CLI.print("", ConsoleColors.BLACK);
            CLI.print("\t\t\t\trequests", ConsoleColors.BLACK_BOLD);
            CLI.print("[1] requests", ConsoleColors.YELLOW);
            CLI.print("[2] messages", ConsoleColors.YELLOW);
            CLI.print("", ConsoleColors.BLACK);

            String command = CLI.getCommand("Enter your command:", ConsoleColors.BLACK);
            if (command.equals("back"))
                break;
            switch (command) {
                case "1":
                    requestHandling(user);
                    break;
                case "2":
                    getRequestMessages(user);
                    break;
                default:
                    CLI.invalidCommand();
                    break;
            }
        }
    }

    private static void getSystemMessages(User user) {
        CLI.print("", ConsoleColors.BLACK);
        CLI.print("\t\t\t\tsystem messages", ConsoleColors.BLACK_BOLD);
        for (String systemMessage : user.getNotifications())
            CLI.print(systemMessage, ConsoleColors.BLACK);
        CLI.print("", ConsoleColors.RESET);
    }

    private static void requestHandling(User user) {
        CLI.print("", ConsoleColors.BLACK);
        CLI.print("\t\t\t\trequested users", ConsoleColors.BLACK_BOLD);
        for (int counter = 0; counter < user.getRequests().size(); counter++) {
            User currentUser = User.getByID(user.getRequests().get(counter));
            CLI.print(String.format("[%s] %s", counter + 1, currentUser.getUsername()), ConsoleColors.YELLOW);
        }
        CLI.print("", ConsoleColors.BLACK);

        if(user.getRequests().size() == 0)
            return;
        String command = CLI.getCommand("Enter the user number:", ConsoleColors.BLACK);
        if(command.equals("back"))
            return;
        User requester;
        int userNumber;
        try {
            userNumber = Integer.parseInt(command) - 1;
            requester = User.getByID(user.getRequests().get(userNumber));
        } catch (Exception e) {
            CLI.invalidCommand();
            return;
        }
        CLI.print("[1] accept", ConsoleColors.RED);
        CLI.print("[2] reject", ConsoleColors.RED);
        CLI.print("[3] reject/no notif", ConsoleColors.RED);
        command = CLI.getCommand("Enter your command:", ConsoleColors.BLACK);
        if(command.equals("back"))
            return;
        switch (command){
            case "1":
                requester.addToFollowings(user.getID());
                requester.addToRequestNotifications(
                        String.format("user %s accepted your follow request!", user.getUsername()));
                user.addToFollowers(requester.getID());
                user.removeFromRequests(requester.getID());
                break;
            case "2":
                requester.addToRequestNotifications(
                        String.format("user %s rejected your follow request!", user.getUsername()));
                user.removeFromRequests(requester.getID());
                break;
            case"3":
                user.removeFromRequests(requester.getID());
                break;
            default:
                CLI.invalidCommand();
                break;
        }
    }

    private static void getRequestMessages(User user) {
        CLI.print("", ConsoleColors.BLACK);
        CLI.print("\t\t\t\trequest messages", ConsoleColors.BLACK_BOLD);
        for (String requestMessage : user.getRequestNotifications())
            CLI.print(requestMessage, ConsoleColors.BLUE);
        CLI.print("", ConsoleColors.RESET);
    }
}
