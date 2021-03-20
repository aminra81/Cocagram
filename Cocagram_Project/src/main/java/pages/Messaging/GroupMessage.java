package pages.Messaging;

import CLI.*;

import models.Group;
import models.ID;
import models.User;
import pages.EnterPage.EnterPage;

import java.util.ArrayList;
import java.util.List;

public class GroupMessage {
    public static void getHelp() {
        CLI.print("", ConsoleColors.BLACK);
        CLI.print("[1] send to users", ConsoleColors.YELLOW);
        CLI.print("[2] send to groups", ConsoleColors.YELLOW);
        CLI.print("[3] send to all followings", ConsoleColors.YELLOW);
        CLI.print("", ConsoleColors.BLACK);
    }

    public static void sendGroupMessage(User user, String content) {
        getHelp();
        List<User> receivers = new ArrayList<>();
        String command = CLI.getCommand("Enter your command:", ConsoleColors.BLACK);
        switch (command) {
            case "1":
                while (true) {
                    String username = CLI.getCommand("Enter the username (or press Enter to break):",
                            ConsoleColors.BLUE);
                    if (username.equals(""))
                        break;
                    User curUser = EnterPage.getUser(username);
                    if (curUser == null)
                        CLI.print("this user doesn't exist!", ConsoleColors.RED_BOLD);
                    else if (!receivers.contains(curUser))
                        receivers.add(curUser);
                }
                break;
            case "2":
                while (true) {
                    String groupName = CLI.getCommand("Enter the group name (or press Enter to break):",
                            ConsoleColors.BLUE);
                    if(groupName.equals(""))
                        break;
                    if(!user.getGroups().contains(new Group(groupName)))
                        CLI.print("this group doesn't exist!", ConsoleColors.RED_BOLD);
                    else
                        for (Group group : user.getGroups())
                            if(group.getGroupName().equals(groupName))
                                for (ID userID : group.getUsers()) {
                                    User curUser = User.getByID(userID);
                                    if(!receivers.contains(curUser))
                                        receivers.add(curUser);
                                }
                }
                break;
            case "3":
                for (ID userID : user.getFollowings())
                    receivers.add(User.getByID(userID));
                break;
            default:
                CLI.invalidCommand();
                return;
        }
        for (User receiver : receivers)
            Messaging.sendMessage(user, receiver, content);
    }
}
