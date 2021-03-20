package pages;

import CLI.*;
import models.User;
import models.media.Message;
import models.media.Tweet;
import pages.Messaging.Messaging;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TweetWalk {
    public static void getHelp() {
        CLI.print("", ConsoleColors.BLACK);
        CLI.print("\t\t\t\tTweet Walker", ConsoleColors.BLACK_BOLD);
        CLI.print("[<] prev", ConsoleColors.YELLOW);
        CLI.print("[>] next", ConsoleColors.YELLOW);
        CLI.print("[1] add to saved messages", ConsoleColors.YELLOW);
        CLI.print("[2] retweet", ConsoleColors.YELLOW);
        CLI.print("[3] forward", ConsoleColors.YELLOW);
        CLI.print("[4] check user profile", ConsoleColors.YELLOW);
        CLI.print("[5] add comment", ConsoleColors.YELLOW);
        CLI.print("[6] check comments", ConsoleColors.YELLOW);
        CLI.print("[7] like/dislike tweet", ConsoleColors.YELLOW);
        CLI.print("", ConsoleColors.RESET);
    }

    public static List<Tweet> validation(User user, List<Tweet> tweets, boolean firstLayer) {
        List<Tweet> validatedTweets = new ArrayList<>();
        for (Tweet tweet : tweets) {
            User writer = User.getByID(tweet.getWriter());
            if (!writer.isActive())
                continue;
            if (writer.equals(user)) {
                if (!firstLayer)
                    validatedTweets.add(tweet);
                continue;
            }
            if (user.getMutedUsers().contains(writer.getID()))
                continue;
            if (writer.isPrivate() && !user.getFollowings().contains(writer.getID()))
                continue;
            if (!validatedTweets.contains(tweet))
                validatedTweets.add(tweet);
        }
        return validatedTweets;
    }

    public static void showTweets(User user, List<Tweet> tweets, boolean myTweets) {
        if (tweets.size() == 0) {
            CLI.print("No tweet/comments here", ConsoleColors.RED_BOLD);
            CLI.print("", ConsoleColors.RESET);
            return;
        }
        int counter = 0;
        while (true) {
            getHelp();
            Tweet tweet = tweets.get(counter);
            showTweet(user, tweets.get(counter), myTweets);
            String command = CLI.getCommand("Enter your command:", ConsoleColors.BLACK);
            if (command.equals("back"))
                return;
            switch (command) {
                case "<":
                    if (counter == 0)
                        CLI.invalidCommand();
                    else
                        counter--;
                    break;
                case ">":
                    if (counter == tweets.size() - 1)
                        CLI.invalidCommand();
                    else
                        counter++;
                    break;
                case "1":
                    Messaging.addToSavedMessages(user, new Message(tweet.getContent(),
                            tweet.getWriter(), null));
                    CLI.print("added to saved messages", ConsoleColors.GREEN_BOLD);
                    break;
                case "2":
                    if (tweet.getUpPost() != null)
                        CLI.print("you can't retweet a comment!", ConsoleColors.RED_BOLD);
                    else if (user.getTweets().contains(tweet.getId()))
                        CLI.print("you've (re)tweeted this one before!", ConsoleColors.RED_BOLD);
                    else {
                        user.addToTweets(tweet.getId());
                        CLI.print("you retweeted this tweet!", ConsoleColors.GREEN_BOLD);
                    }
                    break;
                case "3":
                    Messaging.forward(user, tweet);
                    break;
                case "4":
                    if (tweet.getWriter().equals(user.getID()))
                        CLI.print("check your info at personal page!", ConsoleColors.RED_BOLD);
                    else
                        ShowPage.logic(user, User.getByID(tweet.getWriter()));
                    break;
                case "5":
                    String content = CLI.getCommand("write your comment:", ConsoleColors.BLUE);
                    Tweet curComment = new Tweet(content, user.getID(), tweet.getId());
                    user.addToTweets(curComment.getId());
                    tweet.addComment(curComment.getId());
                    CLI.print("you commented successfully!", ConsoleColors.GREEN_BOLD);
                    CLI.print("", ConsoleColors.BLACK);
                    break;
                case "6":
                    showTweets(user, validation(user, tweet.getComments(), false), myTweets);
                    break;
                case "7":
                    if (user.getLikedTweets().contains(tweet.getId()))
                        dislike(user, tweet);
                    else {
                        if (tweet.getWriter().equals(user.getID()))
                            CLI.print("don't open a coca for yourself.", ConsoleColors.RED_BOLD);
                        else
                            like(user, tweet);
                    }
                    break;
                default:
                    CLI.invalidCommand();
                    break;
            }
        }
    }

    private static void showTweet(User user, Tweet tweet, boolean myTweets) {
        if (!myTweets || tweet.getUpPost() != null || user.getID().equals(tweet.getWriter()))
            CLI.print(String.format("%s:", User.getByID(tweet.getWriter()).getUsername()) + " " +
                    tweet.getContent(), ConsoleColors.BLUE);
        else
            CLI.print(String.format("retweeted from %s:", User.getByID(tweet.getWriter()).getUsername()),
                    ConsoleColors.BLUE);
        if (user.getLikedTweets().contains(tweet.getId()))
            CLI.print(String.format("\u2665: %s", tweet.getLikeNumbers()), ConsoleColors.RED);
        else
            CLI.print(String.format("\u2661: %s", tweet.getLikeNumbers()), ConsoleColors.RED);

        CLI.print(tweet.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), ConsoleColors.GREEN);
        CLI.print("", ConsoleColors.BLACK);
    }

    private static void like(User user, Tweet tweet) {
        tweet.addLike(user.getID());
        user.addToLikedTweets(tweet.getId());
    }

    private static void dislike(User user, Tweet tweet) {
        tweet.removeLike(user.getID());
        user.removeFromLikedTweets(tweet.getId());
    }
}
