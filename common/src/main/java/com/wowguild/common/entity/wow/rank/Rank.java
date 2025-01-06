package com.wowguild.common.entity.wow.rank;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(exclude = {"id", "amount", "killIlvl", "metric"})
@Data
@Entity
public class Rank {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long amount;
    private int killIlvl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private String reportCode;
    private String fightID;
    private String metric;

}
