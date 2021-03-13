package models.media;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.ID;
import models.User;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Tweet extends Media {

    private static final File dbDirectory = new File("./src/main/resources/DB/Tweets");

    private ID id, upPost;
    private List<ID> likes;

    public static Tweet getByID(ID id) {
        try {
            File Data = new File(dbDirectory, id + ".json");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Data));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.fromJson(bufferedReader, Tweet.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveIntoDB() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            File Data = new File(dbDirectory, this.id + ".json");
            if (!Data.exists())
                Data.createNewFile();
            FileWriter writer = new FileWriter(Data);
            writer.write(gson.toJson(this));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Tweet(String content, ID writer, ID upPost) {
        super(content, writer);
        this.id = new ID(true);
        this.upPost = upPost;
        this.likes = new ArrayList<>();
        User.getByID(writer).addToTweets(this.id);
        this.saveIntoDB();
    }

    public void addLike(ID like) {
        this.likes.add(like);
        this.saveIntoDB();
    }

    public ID getId() {
        return id;
    }

    public ID getUpPost() {
        return upPost;
    }

    public LocalDateTime getDateTime() {
        return datetime;
    }

    public int getLikeNumbers() {
        return likes.size();
    }

    public static List<Tweet> sortByDateTime(List<Tweet> tweets) {
        Comparator<Tweet> byDateTime = Comparator.comparing(Tweet::getDateTime).reversed();
        tweets.sort(byDateTime);
        return tweets;
    }

    public static List<Tweet> sortByLikeNumbers(List<Tweet> tweets) {
        Comparator<Tweet> byDateTime = Comparator.comparing(Tweet::getLikeNumbers).reversed();
        tweets.sort(byDateTime);
        return tweets;
    }

    public String getContent() {
        return content;
    }

    public ID getUser() {
        return writer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tweet tweet = (Tweet) o;
        return tweet.getId().equals(getId());
    }
}
