package com.wowguild.common.entity.rank;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String zoneName;
    private String expansionName;
    @Column(unique = true)
    private long canonicalId;

}
