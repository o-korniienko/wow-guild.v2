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
    private Long id;
    private String name;
    private Long encounterID;
    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;
    private Integer difficulty;

}
