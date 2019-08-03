package com.elasdka2.zar3tyseller.Model;

public class Chat {
    public String from;
    public String to;
    public String message;
    public boolean isseen;

    public Chat() {
    }

    public Chat(String from, String to, String message, boolean isseen) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.isseen = isseen;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
