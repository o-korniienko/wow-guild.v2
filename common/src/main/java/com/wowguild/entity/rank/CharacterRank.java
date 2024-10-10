package com.wowguild.entity.rank;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class CharacterRank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private Boss boss;
    private long maxAmount;
    private long average;
    private int totalKills;
    private String metric;
    @OneToMany
    @JoinColumn(name = "character_rank_id")
    private List<Rank> ranks;
}
