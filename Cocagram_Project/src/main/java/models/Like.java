package models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.media.Tweet;

import java.io.*;

public class Like {
    ID id;
    ID user;
    ID tweet;

    private static final File dbDirectory = new File("./src/main/resources/DB/Likes");

    public static Like getByID(ID id) {
        try {
            File Data = new File(dbDirectory, id + ".json");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Data));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.fromJson(bufferedReader, Like.class);
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

    public Like(ID user, ID tweet) {
        this.id = new ID(true);
        this.user = user;
        this.tweet = tweet;
        Tweet.getByID(tweet).addLike(this.id);
        User.getByID(user).addToLikedTweets(tweet);
        this.saveIntoDB();
    }
}
