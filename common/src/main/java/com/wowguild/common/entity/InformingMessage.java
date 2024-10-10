package com.wowguild.common.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
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
}
