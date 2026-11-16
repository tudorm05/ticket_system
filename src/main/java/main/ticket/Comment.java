package main.ticket;

import main.fileio.CommandInput;

public final class Comment {
    private String author;
    private String content;
    private String createdAt;

    public Comment(final CommandInput commandInput) {
        this.author = commandInput.getUsername();
        this.content = commandInput.getComment();
        this.createdAt = commandInput.getTimestamp();
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
