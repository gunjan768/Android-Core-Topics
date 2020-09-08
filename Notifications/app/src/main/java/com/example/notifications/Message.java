package com.example.notifications;

public class Message
{
    private CharSequence text, sender;
    private long timeStamp;

    public Message(CharSequence text, CharSequence sender)
    {
        this.text = text;
        this.sender = sender;
        this.timeStamp = System.currentTimeMillis();
    }

    public CharSequence getText() {
        return text;
    }

    public CharSequence getSender() {
        return sender;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
