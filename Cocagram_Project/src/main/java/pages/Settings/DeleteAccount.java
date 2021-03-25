package pages.Settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Group;
import models.ID;
import models.User;
import CLI.*;
import models.media.Message;
import models.media.Tweet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pages.EnterPage.EnterPage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeleteAccount {

    static private final Logger logger = LogManager.getLogger(DeleteAccount.class);

    public static void logic(User user) {
        String command = CLI.getCommand("Do you really want to delete your account? (Y/N)",
                ConsoleColors.RED_BOLD);
        if (command.equals("N"))
            return;
        logger.info(String.format("user %s deleted his/her account.", user.getUsername()));
        deleteMessages(user.getID());
        deleteTweets(user.getID());
        deleteUser(user.getID());
        EnterPage.logic();
    }

    public static void deleteMessages(ID userID) {
        try {
            File dbDirectory = new File("./src/main/resources/DB/Messages");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            for (File file : dbDirectory.listFiles()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                logger.info(String.format("file %s opened.", file.getName()));
                Message currentMessage = gson.fromJson(bufferedReader, Message.class);
                bufferedReader.close();
                logger.info(String.format("file %s closed.", file.getName()));
                if ((currentMessage.getReceiver() != null && currentMessage.getReceiver().equals(userID)) ||
                        (currentMessage.getWriter() != null && currentMessage.getWriter().equals(userID))) {
                    logger.info(String.format("file %s deleted.", file.getName()));
                    file.delete();
                }
            }
        } catch (IOException e) {
            logger.warn("an exception occurred while deleting messages.");
            e.printStackTrace();
        }
    }

    public static void deleteTweets(ID userID) {
        try {
            List<String> toBeDeletedTweets = new ArrayList<>();
            File dbDirectory = new File("./src/main/resources/DB/Tweets");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            for (File file : dbDirectory.listFiles()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                logger.info(String.format("file %s opened.", file.getName()));
                Tweet currentTweet = gson.fromJson(bufferedReader, Tweet.class);
                bufferedReader.close();
                logger.info(String.format("file %s closed.", file.getName()));
                if (isDependentTweet(currentTweet, userID))
                    toBeDeletedTweets.add(currentTweet.getId() + ".json");
            }
            for (File file : dbDirectory.listFiles())
                if (toBeDeletedTweets.contains(file.getName())) {
                    logger.info(String.format("file %s deleted.", file.getName()));
                    file.delete();
                }
        } catch (IOException e) {
            logger.warn("an exception occurred while deleting tweets.");
            e.printStackTrace();
        }
    }

    private static boolean isDependentTweet(Tweet tweet, ID userID) {
        if (tweet.getWriter().equals(userID))
            return true;
        if (tweet.getUpPost() == null)
            return false;
        return isDependentTweet(Tweet.getByID(tweet.getUpPost()), userID);
    }

    public static void deleteUser(ID userID) {
        try {
            File dbDirectory = new File("./src/main/resources/DB/Users");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            //modify other users files.
            for (File file : dbDirectory.listFiles()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                logger.info(String.format("file %s opened.", file.getName()));
                User currentUser = gson.fromJson(bufferedReader, User.class);
                bufferedReader.close();
                logger.info(String.format("file %s closed.", file.getName()));
                if (currentUser.getID().equals(userID))
                    continue;
                removeUserDetails(currentUser, userID);
            }
            //delete the user file.
            for (File file : dbDirectory.listFiles())
                if (file.getName().equals(userID + ".json")) {
                    logger.info(String.format("file %s deleted.", file.getName()));
                    file.delete();
                }

        } catch (IOException e) {
            logger.warn("an exception occurred while deleting user.");
            e.printStackTrace();
        }
    }

    private static void removeUserDetails(User user, ID toBeDeletedUser) {
        //followings
        if (user.getFollowings().contains(toBeDeletedUser))
            user.removeFromFollowings(toBeDeletedUser);
        //followers
        if (user.getFollowers().contains(toBeDeletedUser))
            user.removeFromFollowers(toBeDeletedUser);
        //blocklist
        if (user.getBlockList().contains(toBeDeletedUser))
            user.removeFromBlocklist(toBeDeletedUser);
        //tweets
        List<ID> toBeDeletedTweets = new ArrayList<>();
        for (ID tweetID : user.getTweets())
            if (Tweet.getByID(tweetID) == null)
                toBeDeletedTweets.add(tweetID);
        for (ID tweetID : toBeDeletedTweets)
            user.removeFromTweets(tweetID);
        toBeDeletedTweets.clear();
        //request notifications
        List<String> toBeDeletedNotifications = new ArrayList<>();
        for (String requestNotification : user.getRequestNotifications()) {
            String acceptedName = String.format("user %s accepted your follow request!",
                    User.getByID(toBeDeletedUser).getUsername());
            String rejectedName = String.format("user %s rejected your follow request!",
                    User.getByID(toBeDeletedUser).getUsername());
            if (requestNotification.equals(acceptedName) || requestNotification.equals(rejectedName))
                toBeDeletedNotifications.add(requestNotification);
        }
        for (String requestNotification : toBeDeletedNotifications)
            user.removeFromRequestNotifications(requestNotification);
        toBeDeletedNotifications.clear();
        //liked tweets
        for (ID tweetID : user.getLikedTweets())
            if (Tweet.getByID(tweetID) == null)
                toBeDeletedTweets.add(tweetID);
        for (ID tweetID : toBeDeletedTweets)
            user.removeFromLikedTweets(tweetID);
        toBeDeletedTweets.clear();
        //messages
        List<ID> toBeDeletedMessages = new ArrayList<>();
        for (ID messageID : user.getMessages())
            if (Message.getByID(messageID) == null)
                toBeDeletedMessages.add(messageID);
        for (ID messageID : toBeDeletedMessages)
            user.removeFromMessages(messageID);
        toBeDeletedMessages.clear();
        //unread messages
        for (ID messageID : user.getUnreadMessages())
            if (Message.getByID(messageID) == null)
                toBeDeletedMessages.add(messageID);
        for (ID messageID : toBeDeletedMessages)
            user.removeFromUnreadMessages(messageID);
        toBeDeletedMessages.clear();
        //requests
        if (user.getRequests().contains(toBeDeletedUser))
            user.removeFromRequests(toBeDeletedUser);
        //notifications
        for (String notification : user.getNotifications()) {
            String followName = String.format("user %s followed you!",
                    User.getByID(toBeDeletedUser).getUsername());
            String unfollowName = String.format("user %s unfollowed you!",
                    User.getByID(toBeDeletedUser).getUsername());
            if (notification.equals(followName) || notification.equals(unfollowName))
                toBeDeletedNotifications.add(notification);
        }
        for (String notification : toBeDeletedNotifications)
            user.removeFromNotifications(notification);
        toBeDeletedNotifications.clear();
        //groups
        List<Group> modifiedGroups = new ArrayList<>();
        for (Group group : user.getGroups())
            if (group.getUsers().contains(toBeDeletedUser))
                modifiedGroups.add(group);
        for (Group group : modifiedGroups) {
            group.removeUser(toBeDeletedUser);
            user.removeGroup(group);
            user.addGroup(group);
        }
        //saved messages
        for (ID messageID : user.getSavedMessages())
            if (Message.getByID(messageID) == null)
                toBeDeletedMessages.add(messageID);
        for (ID messageID : toBeDeletedMessages)
            user.removeFromSavedMessages(messageID);
        toBeDeletedMessages.clear();
        //muted users
        if (user.getMutedUsers().contains(toBeDeletedUser))
            user.removeFromMutedUsers(toBeDeletedUser);
    }
}
