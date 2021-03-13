package models.media;

import models.ID;

import java.time.LocalDateTime;

public abstract class Media {
    String content;
    ID writer;
    LocalDateTime datetime;
    public Media(String content, ID writer) {
        this.content = content;
        this.writer = writer;
        datetime = LocalDateTime.now();
    }
}
