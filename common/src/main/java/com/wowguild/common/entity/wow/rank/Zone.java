package com.wowguild.common.entity.wow.rank;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String zoneName;
    private String expansionName;
    @Column(unique = true)
    private Long canonicalId;

}
