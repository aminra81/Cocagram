package models.media;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.ID;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Tweet extends Media {

    static private final Logger logger = LogManager.getLogger(Tweet.class);
    private static final File dbDirectory = new File("./src/main/resources/DB/Tweets");

    private ID upPost;
    private List<ID> likes, comments;

    public static Tweet getByID(ID id) {
        try {
            File Data = new File(dbDirectory, id + ".json");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Data));
            logger.info(String.format("file %s opened.", Data.getName()));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Tweet tweet = gson.fromJson(bufferedReader, Tweet.class);
            bufferedReader.close();
            logger.info(String.format("file %s closed.", Data.getName()));
            return tweet;
        } catch (Exception e) {
            logger.warn(String.format("Exception occurred while trying to get tweet %s", id));
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
            logger.info(String.format("file %s opened.", Data.getName()));
            FileWriter writer = new FileWriter(Data);
            writer.write(gson.toJson(this));
            writer.close();
            logger.info(String.format("file %s closed.", Data.getName()));
        } catch (IOException e) {
            logger.warn(String.format("Exception occurred while trying to save tweet %s", this.getId()));
            e.printStackTrace();
        }
    }

    public Tweet(String content, ID writer, ID upPost) {
        super(content, writer);
        this.upPost = upPost;
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.saveIntoDB();
    }

    public void addLike(ID user) {
        this.likes.add(user);
        this.saveIntoDB();
    }

    public void removeLike(ID user) {
        this.likes.remove(user);
        this.saveIntoDB();
    }

    public ID getUpPost() {
        return upPost;
    }

    public int getLikeNumbers() {
        return likes.size();
    }

    public static void sortByDateTime(List<Tweet> tweets) {
        Comparator<Tweet> byDateTime = Comparator.comparing(Tweet::getDateTime).reversed();
        tweets.sort(byDateTime);
    }

    public static void sortByLikeNumbers(List<Tweet> tweets) {
        Comparator<Tweet> byDateTime = Comparator.comparing(Tweet::getLikeNumbers).reversed();
        tweets.sort(byDateTime);
    }

    public void addComment(ID comment) {
        this.comments.add(comment);
        this.saveIntoDB();
    }

    public List<Tweet> getComments() {
        List<Tweet> curComments = new ArrayList<>();
        for (ID comment : comments)
            curComments.add(getByID(comment));
        return curComments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tweet tweet = (Tweet) o;
        return tweet.getId().equals(getId());
    }
}
