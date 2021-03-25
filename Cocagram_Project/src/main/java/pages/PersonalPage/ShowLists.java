package pages.PersonalPage;
import CLI.*;

import models.Group;
import models.ID;
import models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.EnterPage.EnterPage;
import pages.ShowPage;

import java.util.ArrayList;
import java.util.List;

public class ShowLists {

    static private final Logger logger = LogManager.getLogger(ShowLists.class);

    public static void getHelp(User user) {
        CLI.print("", ConsoleColors.BLACK);
        CLI.print("\t\t\t\tlists", ConsoleColors.BLACK_BOLD);
        CLI.print("[1] followers", ConsoleColors.YELLOW);
        CLI.print("[2] followings", ConsoleColors.YELLOW);
        CLI.print("[3] blocklist", ConsoleColors.YELLOW);
        for (int counter = 0; counter < user.getGroups().size(); counter++)
            CLI.print(String.format("[%s] %s", counter + 4, user.getGroups().get(counter).getGroupName()),
                    ConsoleColors.YELLOW);
        CLI.print(String.format("[%s] new group", user.getGroups().size() + 4), ConsoleColors.YELLOW);
        CLI.print("", ConsoleColors.BLACK);
    }
    public static void logic(User user) {
        while(true) {
            getHelp(user);
            logger.info(String.format("user %s checked list page."));
            String command = CLI.getCommand("Enter your command:", ConsoleColors.BLACK);
            if(command.equals("back"))
                break;
            if(command.equals("1"))
                showList(user, user.getFollowers());
            else if(command.equals("2"))
                showList(user, user.getFollowings());
            else if(command.equals("3"))
                showList(user, user.getBlockList());
            else if(4 <= Integer.parseInt(command) && Integer.parseInt(command) <= 3 + user.getGroups().size())
                showGroup(user, user.getGroups().get(Integer.parseInt(command) - 4));
            else if(Integer.parseInt(command) == 4 + user.getGroups().size())
                makeNewGroup(user);
            else
                CLI.invalidCommand();
        }
    }

    private static void showList(User user, List<ID> userList) {
        List<ID> activeUsers = new ArrayList<>();
        for (ID currentUser : userList)
            if(User.getByID(currentUser).isActive())
                activeUsers.add(currentUser);
        if(activeUsers.size() == 0)
            return;
        for (ID currentUser : activeUsers)
            CLI.print(User.getByID(currentUser).getUsername(), ConsoleColors.BLUE);
        String command = CLI.getCommand("Enter the username you wanna check out:", ConsoleColors.BLACK);
        if(command.equals("back"))
            return;
        User userToBeVisited = null;
        for (ID currentUser : activeUsers)
            if(User.getByID(currentUser).getUsername().equals(command))
                userToBeVisited = User.getByID(currentUser);
        if(userToBeVisited == null) {
            CLI.print("This user doesn't exist in this list.", ConsoleColors.RED_BOLD);
            logger.info(String.format("user %s wants to check profile of a user which doesn't exist.",
                    user.getUsername()));
        }
        else
            ShowPage.logic(user, userToBeVisited);
    }
    private static void showGroup(User user, Group group) {
        CLI.print("", ConsoleColors.BLACK);
        CLI.print(String.format("\t\t\t\t%s", group.getGroupName()), ConsoleColors.BLACK_BOLD);
        while(true) {
            CLI.print("[1] show group users", ConsoleColors.YELLOW);
            CLI.print("[2] delete group", ConsoleColors.YELLOW);
            CLI.print("[3] add user", ConsoleColors.YELLOW);
            CLI.print("[4] remove user", ConsoleColors.YELLOW);
            String command = CLI.getCommand("Enter your command:", ConsoleColors.BLACK);
            if(command.equals("back"))
                break;
            switch (command) {
                case "1":
                    showList(user, group.getUsers());
                    break;
                case "2":
                    user.removeGroup(group);
                    return;
                case "3":
                    String username = CLI.getCommand("Enter the username:", ConsoleColors.BLACK);
                    User currentUser = EnterPage.getUser(username);
                    if(currentUser == null || !currentUser.isActive()) {
                        CLI.print("this username doesn't exist!", ConsoleColors.RED_BOLD);
                        logger.info(String.format("user %s wants to add a user which doesn't exist to group.",
                                user.getUsername()));
                    }
                    else if(!user.getFollowings().contains(currentUser.getID())) {
                        CLI.print("this username isn't in your followings!", ConsoleColors.RED_BOLD);
                        logger.info(String.format("user %s wants to add a user which doesn't exist to group.",
                                user.getUsername()));
                    }
                    else if(group.getUsers().contains(currentUser.getID())) {
                        CLI.print("this username is in your group!", ConsoleColors.RED_BOLD);
                        logger.info(String.format("user %s wants to add a user which doesn't exist to group.",
                                user.getUsername()));
                    }
                    else {
                        group.addUser(currentUser.getID());
                        user.removeGroup(group);
                        user.addGroup(group);
                    }
                    break;
                case "4":
                    username = CLI.getCommand("Enter the username:", ConsoleColors.BLACK);
                    currentUser = EnterPage.getUser(username);
                    if(currentUser == null || !currentUser.isActive()) {
                        CLI.print("this username doesn't exist!", ConsoleColors.RED_BOLD);
                        logger.info(String.format("user %s wants to remove a user which doesn't exist from group.",
                                user.getUsername()));
                    }
                    else if(!group.getUsers().contains(currentUser.getID())) {
                        CLI.print("this username isn't in your group!", ConsoleColors.RED_BOLD);
                        logger.info(String.format("user %s wants to remove a user which doesn't exist from group.",
                                user.getUsername()));
                    }
                    else {
                        group.removeUser(currentUser.getID());
                        user.removeGroup(group);
                        user.addGroup(group);
                    }
                    break;
                default:
                    CLI.invalidCommand();
                    break;
            }
        }
    }

    private static void makeNewGroup(User user) {
        String groupName = CLI.getCommand("Enter the group name:", ConsoleColors.BLACK);
        Group newGroup = new Group(groupName);
        if(user.getGroups().contains(newGroup)) {
            logger.info(String.format("user %s wants to make a group which doesn't exist.", user.getUsername()));
            CLI.print("this group already exists!", ConsoleColors.RED_BOLD);
        }
        else {
            logger.info(String.format("user %s created a group.", user.getUsername()));
            user.addGroup(newGroup);
        }
    }
}
