package com.chess.chessapi.models;

public class Mail {
    private String subject;
    private String to;
    private String content;

    public Mail(String subject, String to, String content) {
        this.subject = subject;
        this.to = to;
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
