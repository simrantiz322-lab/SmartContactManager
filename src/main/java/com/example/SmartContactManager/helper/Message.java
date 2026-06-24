package com.example.SmartContactManager.helper;

public class Message {

    private String content;
    private String type;

    // Default Constructor
    public Message() {
    }

    // Parameterized Constructor
    public Message(String content, String type) {
        this.content = content;
        this.type = type;
    }

    // Getter for content
    public String getContent() {
        return content;
    }

    // Setter for content
    public void setContent(String content) {
        this.content = content;
    }

    // Getter for type
    public String getType() {
        return type;
    }

    // Setter for type
    public void setType(String type) {
        this.type = type;
    }

    // toString() Method
    @Override
    public String toString() {
        return "YourClassName{" +
                "content='" + content + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

}
