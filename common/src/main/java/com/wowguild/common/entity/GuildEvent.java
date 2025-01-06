package com.wowguild.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
public class GuildEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String subject;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    @ElementCollection
    @CollectionTable(name = "subscriber_id", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "user_id")
    private Set<Long> userIDs;
}
