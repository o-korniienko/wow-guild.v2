package com.wowguild.common.entity.wow.rank;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(exclude = {"id", "zone"})
@Data
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

}
