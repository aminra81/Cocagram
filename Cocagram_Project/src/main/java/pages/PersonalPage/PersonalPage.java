package pages.PersonalPage;

import models.ID;
import models.User;
import CLI.*;
import models.media.Tweet;

import java.util.ArrayList;
import java.util.List;

public class PersonalPage {
    static void getHelp() {
        CLI.print("[1] new tweet", ConsoleColors.BLACK);
        CLI.print("[2] my tweets", ConsoleColors.BLACK);
        CLI.print("[3] edit your page", ConsoleColors.BLACK);
        CLI.print("[4] lists", ConsoleColors.BLACK);
        CLI.print("[5] info", ConsoleColors.BLACK);
        CLI.print("[6] notifications", ConsoleColors.BLACK);
    }

    public static void logic(User user) {
        while (true) {
            getHelp();
            String command = CLI.getCommand("enter your command:", ConsoleColors.BLACK);
            if (command.equals("back"))
                break;
            switch (command) {
                case "1":
                    newTweet(user);
                    break;
                case "2":
                    showTweets(user);
                    break;
                case "3":
                    EditPage.logic(user);
                    break;
                case "4":
                    ShowLists.logic(user);
                    break;
                case "5":
                    getInfo(user);
                    break;
                case "6":
                    Notifications.logic(user);
                    break;
                default:
                    CLI.invalidCommand();
                    break;
            }
        }
    }

    private static void newTweet(User user) {
        String content = CLI.getCommand("write your tweet:", ConsoleColors.BLUE);
        Tweet newTweet = new Tweet(content, user.getID(), null);
        user.addToTweets(newTweet.getId());
        CLI.print("you tweeted successfully!", ConsoleColors.BLUE_BOLD);
    }

    private static void showTweets(User user) {
        List<Tweet> tweets = new ArrayList<>();
        for (ID tweet : user.getTweets()) {
            Tweet curTweet = Tweet.getByID(tweet);
            if (curTweet.getUpPost() == null)
                tweets.add(curTweet);
        }
        Tweet.sortByDateTime(tweets);
        int counter = 1;
        for (Tweet tweet : tweets) {
            if (tweet.getUser().equals(user.getID()))
                CLI.print(String.format("[%s]", counter) + " " + tweet.getContent(),
                        ConsoleColors.RED);
            else
                CLI.print(String.format("[%s]", counter) +
                        String.format(" retweeted %s's tweet: ", User.getByID(tweet.getUser()).getUsername())
                        + tweet.getContent(), ConsoleColors.RED);

            CLI.print(String.format("\u2665: %s", tweet.getLikeNumbers()), ConsoleColors.RED);
            CLI.print("", ConsoleColors.RESET);
            counter++;
        }
    }

    private static void getInfo(User user) {
        CLI.print(String.format("firstname: %s", user.getFirstname()), ConsoleColors.CYAN_BOLD);
        CLI.print(String.format("lastname: %s", user.getLastname()), ConsoleColors.CYAN_BOLD);
        CLI.print(String.format("username: %s", user.getUsername()), ConsoleColors.CYAN_BOLD);
        CLI.print(String.format("bio: %s", user.getBio()), ConsoleColors.CYAN_BOLD);
        try {
            CLI.print(String.format("birthDate: %s", user.getBirthDate().toString()), ConsoleColors.CYAN_BOLD);
        } catch (Exception e) {
            CLI.print("birthDate: No birthdate is given.", ConsoleColors.CYAN_BOLD);
        }
        CLI.print(String.format("email: %s", user.getEmail()), ConsoleColors.CYAN_BOLD);
        CLI.print(String.format("phone number: %s", user.getPhoneNumber()), ConsoleColors.CYAN_BOLD);
        CLI.print(String.format("is active: %s", user.isActive() ? "Y" : "N"), ConsoleColors.CYAN_BOLD);
        CLI.print(String.format("is private: %s", user.isPrivate() ? "Y" : "N"), ConsoleColors.CYAN_BOLD);
        CLI.print(String.format("last seen type: %s", user.getLastSeenType()), ConsoleColors.CYAN_BOLD);
        CLI.print(String.format("Are your data public?: %s", user.isPublicData() ? "Y" : "N"),
                ConsoleColors.CYAN_BOLD);
        CLI.print("", ConsoleColors.RESET);
    }
}
