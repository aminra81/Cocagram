package pages.Messaging;

import models.ID;
import models.User;
import CLI.*;
import models.media.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.EnterPage.EnterPage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalMessage {

    static private final Logger logger = LogManager.getLogger(PersonalMessage.class);

    public static void getHelp() {
        CLI.print("", ConsoleColors.BLACK);
        CLI.print("\t\t\t\tpersonal messages", ConsoleColors.BLACK_BOLD);
        CLI.print("", ConsoleColors.BLACK);
    }

    public static void logic(User user) {
        while (true) {
            getHelp();
            logger.info(String.format("user %s checked personal messages page.", user.getUsername()));
            showPersonalMessages(user);
            String username = CLI.getCommand("Enter the username you wanna check your chats together: (or type \"back\")",
                    ConsoleColors.BLACK);
            if (username.equals("back"))
                return;
            User curUser = EnterPage.getUser(username);
            if (curUser == null) {
                CLI.print("this user doesn't exist!", ConsoleColors.RED_BOLD);
                logger.info(String.format("user %s wants to send an invalid message.", user.getUsername()));
                continue;
            }
            showMessages(user, curUser);
        }
    }

    public static void showPersonalMessages(User user) {
        Map<User, Integer> unreadCount = new HashMap<>();
        for (ID message : user.getUnreadMessages()) {
            Message curMessage = Message.getByID(message);
            User sender = User.getByID(curMessage.getWriter());
            if(!sender.isActive())
                continue;
            if (unreadCount.containsKey(sender))
                unreadCount.put(sender, unreadCount.get(sender) + 1);
            else
                unreadCount.put(sender, 1);
        }

        for (User sender : unreadCount.keySet())
            CLI.print(String.format("%s [%s]", sender.getUsername(), unreadCount.get(sender)), ConsoleColors.BLUE);


        List<User> otherSenders = new ArrayList<>();
        for (ID message : user.getMessages()) {
            Message curMessage = Message.getByID(message);
            User sender;
            if (!User.getByID(curMessage.getWriter()).equals(user))
                sender = User.getByID(curMessage.getWriter());
            else
                sender = User.getByID(curMessage.getReceiver());
            if(!sender.isActive())
                continue;
            if (!otherSenders.contains(sender) && !unreadCount.containsKey(sender))
                otherSenders.add(sender);
        }
        for (User sender : otherSenders)
            CLI.print(sender.getUsername(), ConsoleColors.BLUE);
    }

    public static void getHelpPrivateChat() {
        CLI.print("[1] send a message", ConsoleColors.YELLOW);
        CLI.print("[2] save a message", ConsoleColors.YELLOW);
        CLI.print("", ConsoleColors.BLACK);
    }

    public static void showMessages(User user, User curUser) {
        if (curUser.equals(user)) {
            CLI.print("you can't send message to yourself here!", ConsoleColors.RED_BOLD);
            logger.info(String.format("user %s wants to send invalid message.", user.getUsername()));
            return;
        }
        while (true) {
            CLI.print(String.format("\t\t\t\tyour chat with %s", curUser.getUsername()), ConsoleColors.BLACK_BOLD);
            //delete unread messages.
            List<ID> userUnreadMessages = user.getUnreadMessages();
            List<ID> curUnreadMessages = new ArrayList<>();
            for (ID message : userUnreadMessages)
                if (Message.getByID(message).getWriter().equals(curUser.getID()))
                    curUnreadMessages.add(message);
            for (ID message : curUnreadMessages) {
                user.removeFromUnreadMessages(message);
                user.addToMessages(message);
            }
            //show messages
            List<Message> messages = new ArrayList<>();
            for (ID message : user.getMessages())
                if (Message.getByID(message).getWriter().equals(curUser.getID()) ||
                        Message.getByID(message).getReceiver().equals(curUser.getID()))
                    messages.add(Message.getByID(message));
            Message.sortByDateTime(messages);
            for (int counter = 0; counter < messages.size(); counter++) {
                Message curMessage = messages.get(counter);
                if (curMessage.getWriter().equals(user.getID()))
                    CLI.print(String.format("[%s] you: %s", counter + 1, curMessage.getContent()), ConsoleColors.RED);
                else
                    CLI.print(String.format("[%s] %s: %s", counter + 1, curUser.getUsername(),
                            curMessage.getContent()), ConsoleColors.BLUE);

                CLI.print(curMessage.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        ConsoleColors.GREEN);
                CLI.print("", ConsoleColors.BLACK);
            }
            getHelpPrivateChat();
            String command = CLI.getCommand("Enter your command:", ConsoleColors.BLACK);
            if (command.equals("back"))
                break;
            if (command.equals("1"))
                Messaging.sendMessage(user, curUser, CLI.getCommand("write your message:", ConsoleColors.BLUE));
            else if (command.equals("2")) {
                try {
                    int id = Integer.parseInt(CLI.getCommand("Enter the message number:", ConsoleColors.BLACK)) - 1;
                    Messaging.addToSavedMessages(user, messages.get(id));
                    CLI.print("added to saved messages!", ConsoleColors.GREEN_BOLD);
                } catch (Exception e) {
                    CLI.invalidCommand();
                }

            } else
                CLI.invalidCommand();
        }
    }
}
