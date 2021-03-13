package pages.PersonalPage;
import CLI.*;

import models.Group;
import models.ID;
import models.User;
import pages.ShowPage;

import java.util.ArrayList;
import java.util.List;

public class ShowLists {
    public static void getHelp(User user) {
        CLI.print("[1] followers", ConsoleColors.RED);
        CLI.print("[2] followings", ConsoleColors.RED);
        CLI.print("[3] blocklist", ConsoleColors.RED);
        for (int counter = 0; counter < user.getGroups().size(); counter++)
            CLI.print(String.format("[%s] %s", counter + 4, user.getGroups().get(counter).getGroupName()),
                    ConsoleColors.RED);
        CLI.print(String.format("[%s] new group", user.getGroups().size() + 4), ConsoleColors.RED);
    }
    public static void logic(User user) {
        while(true) {
            getHelp(user);
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
        String command = CLI.getCommand("Enter the username you wanna check out:", ConsoleColors.BLUE);
        if(command.equals("back"))
            return;
        User userToBeVisited = null;
        for (ID currentUser : activeUsers)
            if(User.getByID(currentUser).getUsername().equals(command))
                userToBeVisited = User.getByID(currentUser);
        if(userToBeVisited == null)
            CLI.print("This user doesn't exist in this list.", ConsoleColors.RED_BOLD);
        else
            ShowPage.logic(user, userToBeVisited);
    }

    private static void showGroup(User user, Group group) {

    }

    private static void makeNewGroup(User user) {

    }
}
