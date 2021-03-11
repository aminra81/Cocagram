package models.media;

import models.ID;

public abstract class Media {
    String content;
    ID writer;
    public Media(String content, ID writer) {
        this.content = content;
        this.writer = writer;
    }
}
