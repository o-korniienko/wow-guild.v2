package com.wowguild.common.entity.wow.rank;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class CharacterRank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Boss boss;
    private Long maxAmount;
    private Long average;
    private Integer totalKills;
    private String metric;
    @OneToMany
    @JoinColumn(name = "character_rank_id")
    private List<Rank> ranks;
}
