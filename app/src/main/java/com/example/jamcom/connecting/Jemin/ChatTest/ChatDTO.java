package com.example.jamcom.connecting.Jemin.ChatTest;

public class ChatDTO {

    private int userID;
    private String userName;
    private String message;
    private String dateTime;

    public ChatDTO() {}
    public ChatDTO(int userID, String userName, String message, String dateTime) {
        this.userID = userID;
        this.userName = userName;
        this.message = message;
        this.dateTime = dateTime;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public String getDateTime() {
        return dateTime;
    }
}