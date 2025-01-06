package com.wowguild.common.entity.push;

import com.wowguild.common.enums.push.OsType;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class PushDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    private long userId;
    private OsType osType;
    private String pushToken;
    @Column(unique = true)
    private String deviceId;
 }
