package com.wowguild.entity.rank;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String zoneName;
    private String expansionName;
    @Column(unique=true)
    private long canonicalId;

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


    public long getCanonicalId() {
        return canonicalId;
    }

    public void setCanonicalId(long canonicalId) {
        this.canonicalId = canonicalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zone zone = (Zone) o;
        return id == zone.id && canonicalId == zone.getCanonicalId() && Objects.equals(zoneName, zone.zoneName) && Objects.equals(expansionName, zone.expansionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, canonicalId, zoneName, expansionName);
    }

    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", zoneName='" + zoneName + '\'' +
                ", expansionName='" + expansionName + '\'' +
                ", canocicalId=" + canonicalId +
                '}';
    }
}
