package models.media;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.ID;
import java.io.*;
import java.util.Comparator;
import java.util.List;

public class Message extends Media {
    ID receiver;

    private static final File dbDirectory = new File("./src/main/resources/DB/Messages");

    public static Message getByID(ID id) {
        try {
            File Data = new File(dbDirectory, id + ".json");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Data));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Message message = gson.fromJson(bufferedReader, Message.class);
            bufferedReader.close();
            return message;
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Message(String content, ID writer, ID receiver) {
        super(content, writer);
        this.receiver = receiver;
        this.saveIntoDB();
    }

    public ID getReceiver() { return receiver; }


    public static void sortByDateTime(List<Message> messages) {
        Comparator<Message> byDateTime = Comparator.comparing(Message::getDateTime).reversed();
        messages.sort(byDateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return message.getId().equals(getId());
    }
}
