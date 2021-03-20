package models.media;
import models.ID;
import java.time.LocalDateTime;

public abstract class Media {
    ID id;
    String content;
    ID writer;
    LocalDateTime datetime;
    public Media(String content, ID writer) {
        this.id = new ID(true);
        this.content = content;
        this.writer = writer;
        datetime = LocalDateTime.now();
    }

    public ID getWriter() { return writer; }

    public String getContent() { return content; }

    public ID getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return datetime;
    }
}
