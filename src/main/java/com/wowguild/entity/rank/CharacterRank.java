package com.wowguild.entity.rank;

import jakarta.persistence.*;
import java.util.List;

@Entity
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boss getBoss() {
        return boss;
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    public long getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(long maxAmount) {
        this.maxAmount = maxAmount;
    }

    public long getAverage() {
        return average;
    }

    public void setAverage(long average) {
        this.average = average;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public void setRanks(List<Rank> ranks) {
        this.ranks = ranks;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    @Override
    public String toString() {
        return "CharacterRank{" +
                "id=" + id +
                ", boss=" + boss +
                ", maxAmount=" + maxAmount +
                ", average=" + average +
                ", totalKills=" + totalKills +
                ", metric='" + metric + '\'' +
                ", ranks=" + ranks +
                '}';
    }
}
