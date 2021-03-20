package pages;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.User;
import CLI.*;
import models.media.Tweet;
import pages.EnterPage.EnterPage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Explorer{
    static void getHelp() {
        CLI.print("", ConsoleColors.RESET);
        CLI.print("\t\t\t\texplorer", ConsoleColors.BLACK_BOLD);
        CLI.print("[1] search user", ConsoleColors.YELLOW);
        CLI.print("[2] explore tweets", ConsoleColors.YELLOW);
        CLI.print("", ConsoleColors.BLACK);
    }
    public static void logic(User user) {
        while (true) {
            getHelp();
            String command = CLI.getCommand("Enter your command:", ConsoleColors.BLACK);
            if(command.equals("back"))
                break;
            switch (command) {
                case "1":
                    User curUser = EnterPage.getUser(CLI.getCommand("Enter the username:", ConsoleColors.BLUE));
                    if(curUser == null)
                        CLI.print("this username doesn't exist!", ConsoleColors.RED_BOLD);
                    else
                        ShowPage.logic(user, curUser);
                    break;
                case "2":
                    TweetWalk.showTweets(user, TweetWalk.validation(user, getTweets(), true), false);
                    break;
                default:
                    CLI.invalidCommand();
                    break;
            }
        }
    }
    private static List<Tweet> getTweets() {
        List<Tweet> tweets = new ArrayList<>();
        try {
            File dbDirectory = new File("./src/main/resources/DB/Tweets");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            for (File userFile : dbDirectory.listFiles()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(userFile));
                Tweet currentTweet = gson.fromJson(bufferedReader, Tweet.class);
                User writer = User.getByID(currentTweet.getWriter());
                if(!writer.isPrivate())
                    tweets.add(currentTweet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tweets;
    }
}
