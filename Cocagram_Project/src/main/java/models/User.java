package models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

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
    private List<String> requestNotifications;
    private List<ID> likedTweets;
    private List<ID> messages;
    private List<ID> unreadMessages;
    private List<ID> requests;
    private List<String> notifications;
    private boolean isPrivate;
    private List<Group> groups;
    private List<ID> savedMessages;
    private List<ID> mutedUsers;

    static private final Logger logger = LogManager.getLogger(User.class);
    private static final File dbDirectory = new File("./src/main/resources/DB/Users");

    public static User getByID(ID id) {
        try {
            File Data = new File(dbDirectory, id + ".json");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Data));
            logger.info(String.format("file %s opened.", Data.getName()));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            User user = gson.fromJson(bufferedReader, User.class);
            bufferedReader.close();
            logger.info(String.format("file %s closed.", Data.getName()));
            return user;
        } catch (IOException e) {
            logger.error(String.format("Exception occurred while trying to get user %s", id));
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
        } catch (Exception e) {
            logger.error(String.format("Exception occurred while trying to save user %s", this.getId()));
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
        this.requestNotifications = new ArrayList<>();

        this.messages = new ArrayList<>();
        this.unreadMessages = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.savedMessages = new ArrayList<>();
        this.mutedUsers = new ArrayList<>();

        this.saveIntoDB();
    }

    public ID getId() { return id; }

    public void addToLikedTweets(ID tweet) {
        this.likedTweets.add(tweet);
        this.saveIntoDB();
    }

    public void removeFromTweets(ID tweet) {
        this.tweets.remove(tweet);
        this.saveIntoDB();
    }

    public void removeFromLikedTweets(ID tweet) {
        this.likedTweets.remove(tweet);
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

    public String getPassword() { return password; }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<ID> getTweets() {
        return tweets;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getBio() {
        return bio;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public boolean isPublicData() {
        return publicData;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getLastSeenType() {
        return lastSeenType;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
        saveIntoDB();
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
        saveIntoDB();
    }

    public void setBio(String bio) {
        this.bio = bio;
        saveIntoDB();
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        saveIntoDB();
    }

    public void setEmail(String email) {
        this.email = email;
        saveIntoDB();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        saveIntoDB();
    }

    public List<String> getNotifications() { return notifications; }

    public List<String> getRequestNotifications() { return requestNotifications; }

    public List<ID> getRequests() { return requests; }

    public void addToFollowings(ID user) {
        followings.add(user);
        saveIntoDB();
    }

    public void removeFromFollowings(ID user) {
        followings.remove(user);
        saveIntoDB();
    }

    public void addToFollowers(ID user) {
        followers.add(user);
        saveIntoDB();
    }

    public void removeFromFollowers(ID user) {
        followers.remove(user);
        saveIntoDB();
    }

    public void addToBlocklist(ID user) {
        blockList.add(user);
        saveIntoDB();
    }

    public void removeFromBlocklist(ID user) {
        blockList.remove(user);
        saveIntoDB();
    }

    public void addToRequestNotifications(String content) {
        requestNotifications.add(content);
        if(requestNotifications.size() > 10)
            requestNotifications.remove(0);
        saveIntoDB();
    }

    public void addToNotifications(String content) {
        notifications.add(content);
        if(notifications.size() > 10)
            notifications.remove(0);
        saveIntoDB();
    }

    public void addToRequests(ID requester) {
        requests.add(requester);
        saveIntoDB();
    }

    public void removeFromRequests(ID requester) {
        requests.remove(requester);
        saveIntoDB();
    }

    public List<Group> getGroups() { return groups; }

    public List<ID> getFollowings() {
        return followings;
    }

    public List<ID> getFollowers() {
        return followers;
    }

    public List<ID> getBlockList() {
        return blockList;
    }

    public void removeGroup(Group group) {
        groups.remove(group);
        saveIntoDB();
    }

    public void addGroup(Group group) {
        groups.add(group);
        saveIntoDB();
    }

    public List<ID> getLikedTweets() { return likedTweets; }

    public List<ID> getMutedUsers() { return mutedUsers; }

    public void addToMutedUsers(ID user) {
        mutedUsers.add(user);
        this.saveIntoDB();
    }

    public void removeFromMutedUsers(ID user) {
        mutedUsers.remove(user);
        this.saveIntoDB();
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
        this.saveIntoDB();
    }

    public LocalDateTime getLastSeen() { return lastSeen; }

    public void addToSavedMessages(ID message) {
        savedMessages.add(message);
        this.saveIntoDB();
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
        this.saveIntoDB();
    }

    public List<ID> getSavedMessages() { return savedMessages; }

    public List<ID> getUnreadMessages() { return unreadMessages; }

    public List<ID> getMessages() { return messages; }

    public void removeFromMessages(ID message) {
        messages.remove(message);
        this.saveIntoDB();
    }

    public void removeFromUnreadMessages(ID message) {
        unreadMessages.remove(message);
        this.saveIntoDB();
    }

    public void removeFromSavedMessages(ID message) {
        savedMessages.remove(message);
        this.saveIntoDB();
    }

    public void setPassword(String password) {
        this.password = password;
        this.saveIntoDB();
    }

    public void setLastSeenType(String lastSeenType) {
        this.lastSeenType = lastSeenType;
        this.saveIntoDB();
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
        this.saveIntoDB();
    }

    public void removeFromRequestNotifications(String requestNotification) {
        requestNotifications.remove(requestNotification);
        this.saveIntoDB();
    }

    public void removeFromNotifications(String notification) {
        notifications.remove(notification);
        this.saveIntoDB();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return user.getID().equals(getID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
