package models.media;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.ID;
import models.User;

import java.io.*;

public class Message extends Media{
    ID id;
    ID receiver;

    private static final File dbDirectory = new File("./src/main/resources/DB/Messages");

    public static Message getByID(ID id)  {
        try {
            File Data = new File(dbDirectory, id + ".json");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Data));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.fromJson(bufferedReader, Message.class);
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

    public Message(String content, ID writer, ID receiver) {
        super(content, writer);
        this.id = new ID(true);
        this.receiver = receiver;
        User.getByID(receiver).addToUnreadMessages(this.id);
        User.getByID(writer).addToMessages(this.id);
        this.saveIntoDB();
    }
}
