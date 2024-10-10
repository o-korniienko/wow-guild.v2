package com.wowguild.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RankDto {
    private long id;
    private long amount;
    private int killIlvl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private String reportCode;
    private String fightID;
    private String metric;
}
