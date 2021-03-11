package models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private ID id;
    private String firstname;
    private String lastname;
    private String username;
    private String bio;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
    private String password;
    private boolean publicData;
    private boolean isActive;
    private LocalDateTime lastSeen;
    private String lastSeenType;
    private List<ID> followings;
    private List<ID> followers;
    private List<ID> blockList;
    private List<ID> tweets;
    private List<ID> retweets;
    private List<ID> likedTweets;
    private List<ID> messages;
    private List<ID> unreadMessages;
    private List<ID> requests;
    private List<String> notifications;
    private List<List<ID>> groups;
    private boolean isPrivate;

    private static final File dbDirectory = new File("./src/main/resources/DB/Users");

    public static User getByID(ID id)  {
        try {
            File Data = new File(dbDirectory, id + ".json");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Data));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            User user = gson.fromJson(bufferedReader, User.class);
            return user;
        }
        catch (IOException e) {
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
    public User(String username, String firstname, String lastname, String bio, LocalDate birthDate, String email, String phoneNumber, String password, boolean publicData, String lastSeenType) {
        //get from user.
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bio = bio;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.publicData = publicData;
        this.lastSeenType = lastSeenType;

        //fill them by default.
        this.id = new ID(true);
        this.isActive = true;
        this.isPrivate = false;
        this.followers = new ArrayList<>();
        this.followings = new ArrayList<>();
        this.blockList = new ArrayList<>();
        this.lastSeen = LocalDateTime.now();
        this.groups = new ArrayList<>();

        this.tweets = new ArrayList<>();
        this.likedTweets = new ArrayList<>();
        this.retweets = new ArrayList<>();

        this.messages = new ArrayList<>();
        this.unreadMessages = new ArrayList<>();

        this.requests = new ArrayList<>();
        this.notifications = new ArrayList<>();

        this.saveIntoDB();
    }

    public void addToLikedTweets(ID post) {
        this.likedTweets.add(post);
        this.saveIntoDB();
    }

    public void addToTweets(ID tweet) {
        this.tweets.add(tweet);
        this.saveIntoDB();
    }

    public void addToMessages(ID message) {
        this.messages.add(message);
        this.saveIntoDB();
    }

    public void addToUnreadMessages(ID message) {
        this.unreadMessages.add(message);
        this.saveIntoDB();
    }

    public ID getID() {
        return this.id;
    }
}
