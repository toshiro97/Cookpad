package com.example.cookpad.model;

public class ChatMessenger {
    private String messenger;
    private boolean isSend;

    public ChatMessenger() {
    }

    public ChatMessenger(String messenger) {
        this.messenger = messenger;
        this.isSend = false;
    }

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
}
