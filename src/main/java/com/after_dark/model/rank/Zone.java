package com.after_dark.model.rank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String zoneName;
    private String expansionName;
    private int maxLevel;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getExpansionName() {
        return expansionName;
    }

    public void setExpansionName(String expansionName) {
        this.expansionName = expansionName;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zone zone = (Zone) o;
        return id == zone.id && maxLevel == zone.maxLevel && Objects.equals(zoneName, zone.zoneName) && Objects.equals(expansionName, zone.expansionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, zoneName, expansionName, maxLevel);
    }

    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", zoneName='" + zoneName + '\'' +
                ", expansionName='" + expansionName + '\'' +
                ", maxLevel=" + maxLevel +
                '}';
    }
}
