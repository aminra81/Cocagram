package pages.Messaging;

import CLI.*;
import models.ID;
import models.User;
import models.media.Message;
import models.media.Tweet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Messaging {

    static private final Logger logger = LogManager.getLogger(Messaging.class);

    static void getHelp() {
        CLI.print("", ConsoleColors.RESET);
        CLI.print("\t\t\t\tmessaging", ConsoleColors.BLACK_BOLD);
        CLI.print("[1] saved messages", ConsoleColors.YELLOW);
        CLI.print("[2] personal messaging", ConsoleColors.YELLOW);
        CLI.print("[3] group messaging", ConsoleColors.YELLOW);
        CLI.print("", ConsoleColors.BLACK);
    }

    public static void logic(User user) {
        while (true) {
            getHelp();
            logger.info(String.format("user %s checked messaging page.", user.getUsername()));
            String command = CLI.getCommand("Enter your command:", ConsoleColors.BLACK);
            if (command.equals("back"))
                break;
            switch (command) {
                case "1":
                    savedMessagesHandling(user);
                    break;
                case "2":
                    PersonalMessage.logic(user);
                    break;
                case "3":
                    String content = CLI.getCommand("Enter your message: ", ConsoleColors.BLUE);
                    GroupMessage.sendGroupMessage(user, content);
                    break;
                default:
                    CLI.invalidCommand();
                    break;
            }
        }
    }

    public static void addToSavedMessages(User user, Message message) {
        user.addToSavedMessages(message.getId());
    }

    public static String getForwardedContent(Tweet tweet) {
        return String.format("%s tweeted: %s", User.getByID(tweet.getWriter()).getUsername(),
                tweet.getContent());
    }

    public static void forward(User user, Tweet tweet) {
        GroupMessage.sendGroupMessage(user, getForwardedContent(tweet));
    }

    public static void sendMessage(User writer, User receiver, String content) {
        if(writer.equals(receiver)) {
            CLI.print("you can't send a message to yourself here!", ConsoleColors.RED_BOLD);
            logger.info(String.format("user %s wants to send an invalid message.", writer.getUsername()));
            return;
        }
        if(!receiver.isActive()) {
            CLI.print("this user isn't active!", ConsoleColors.RED_BOLD);
            logger.info(String.format("user %s wants to send an invalid message.", writer.getUsername()));
            return;
        }
        if (!writer.getFollowings().contains(receiver.getID()) && !receiver.getFollowings().contains(writer.getID())) {
            CLI.print(String.format("neither you follow %s nor %s follows you so you can't send message to him/her",
                    receiver.getUsername(), receiver.getUsername()), ConsoleColors.RED_BOLD);
            logger.info(String.format("user %s wants to send an invalid message.", writer.getUsername()));
            CLI.print("", ConsoleColors.BLACK);
            return;
        }

        if (writer.getBlockList().contains(receiver.getID())) {
            CLI.print(String.format("you blocked user %s so you can't send message to him/her!", writer.getUsername()),
                    ConsoleColors.RED_BOLD);
            logger.info(String.format("user %s wants to send an invalid message.", writer.getUsername()));
            CLI.print("", ConsoleColors.BLACK);
            return;
        }
        if (receiver.getBlockList().contains(writer.getID())) {
            CLI.print(String.format("user %s blocked you so you can't send message to him/her!", writer.getUsername())
                    , ConsoleColors.RED_BOLD);
            logger.info(String.format("user %s wants to send an invalid message.", writer.getUsername()));
            CLI.print("", ConsoleColors.BLACK);
            return;
        }
        Message message = new Message(content, writer.getID(), receiver.getID());
        receiver.addToUnreadMessages(message.getId());
        writer.addToMessages(message.getId());
        logger.info(String.format("user %s sent a message to user %s", writer.getUsername(), receiver.getUsername()));
    }

    public static void getHelpSavedMessages() {
        CLI.print("", ConsoleColors.RESET);
        CLI.print("\t\t\t\tsaved messages", ConsoleColors.BLACK_BOLD);
        CLI.print("[1] add a message", ConsoleColors.YELLOW);
        CLI.print("", ConsoleColors.BLACK);
    }

    public static void savedMessagesHandling(User user) {
        while (true) {
            getHelpSavedMessages();
            logger.info(String.format("user %s checked saved messages.", user.getUsername()));
            List<ID> savedMessagesID = user.getSavedMessages();
            List<Message> savedMessages = new ArrayList<>();
            for (ID messageID : savedMessagesID)
                savedMessages.add(Message.getByID(messageID));
            Message.sortByDateTime(savedMessages);
            for (Message message : savedMessages) {
                if (message.getReceiver() == null)
                    CLI.print(String.format("(tweet) %s: %s", User.getByID(message.getWriter()).getUsername(),
                            message.getContent()), ConsoleColors.BLUE);
                else
                    CLI.print(String.format("(message) %s: %s", User.getByID(message.getWriter()).getUsername(),
                            message.getContent()), ConsoleColors.BLUE);

                CLI.print(message.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        ConsoleColors.GREEN);
                CLI.print("", ConsoleColors.BLACK);
            }
            String command = CLI.getCommand("Enter your command", ConsoleColors.BLACK);
            if (command.equals("back"))
                break;
            else if (command.equals("1"))
                addNewSavedMessage(user);
            else
                CLI.invalidCommand();
        }
    }

    public static void addNewSavedMessage(User user) {
        String content = CLI.getCommand("Write your message:", ConsoleColors.BLUE);
        Message curMessage = new Message(content, user.getID(), user.getID());
        user.addToSavedMessages(curMessage.getId());
    }
}
