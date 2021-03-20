package pages;
import models.ID;
import models.User;
import models.media.Tweet;

import java.util.ArrayList;
import java.util.List;

public class Timeline {

    public static void logic(User user) {
        List<Tweet> tweets = getTweets(user);
        Tweet.sortByLikeNumbers(tweets);
        TweetWalk.showTweets(user, TweetWalk.validation(user, tweets, true), false);
    }

    private static List<Tweet> getTweets(User user) {
        List<Tweet> tweets = new ArrayList<>();
        for (ID following : user.getFollowings()) {
            for (ID tweet : User.getByID(following).getTweets())
                if(!tweets.contains(Tweet.getByID(tweet)))
                    tweets.add(Tweet.getByID(tweet));
            for (ID tweet : User.getByID(following).getLikedTweets())
                tweets.add(Tweet.getByID(tweet));
        }
        return tweets;
    }
}
