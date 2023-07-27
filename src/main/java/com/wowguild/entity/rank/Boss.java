package com.wowguild.entity.rank;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Boss {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private long encounterID;
    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;
    private int difficulty;

    public Zone getZone() {
        return zone;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getEncounterID() {
        return encounterID;
    }

    public void setEncounterID(long encounterID) {
        this.encounterID = encounterID;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boss boss = (Boss) o;
        return encounterID == boss.encounterID && difficulty == boss.difficulty && Objects.equals(name, boss.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, encounterID, difficulty);
    }

    @Override
    public String toString() {
        return "Boss{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", encounterID=" + encounterID +
                ", zone='" + zone + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }
}
