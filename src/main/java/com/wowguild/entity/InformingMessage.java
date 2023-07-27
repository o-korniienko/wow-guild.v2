package com.wowguild.entity;

import javax.persistence.*;

@Entity
public class InformingMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String tag;
    private String message;


    public InformingMessage(long id, String tag, String message, boolean active) {
        this.id = id;
        this.tag = tag;
        this.message = message;
    }

    public InformingMessage() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return "InformingMessage{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
