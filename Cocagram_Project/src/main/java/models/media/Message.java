package models.media;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.ID;
import java.io.*;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Message extends Media {
    ID receiver;

    static private final Logger logger = LogManager.getLogger(Message.class);
    private static final File dbDirectory = new File("./src/main/resources/DB/Messages");

    public static Message getByID(ID id) {
        try {
            File Data = new File(dbDirectory, id + ".json");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Data));
            logger.info(String.format("file %s opened.", Data.getName()));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Message message = gson.fromJson(bufferedReader, Message.class);
            bufferedReader.close();
            logger.info(String.format("file %s closed.", Data.getName()));
            return message;
        } catch (Exception e) {
            logger.warn(String.format("Exception occurred while trying to get message %s", id));
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
            logger.error(String.format("Exception occurred while trying to save message %s", this.getId()));
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

    @Override
    public int hashCode() { return Objects.hash(id); }
}
