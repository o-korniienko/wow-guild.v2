package com.wowguild.common.dto.push;

import com.wowguild.common.enums.push.OsType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PushDeviceDto {

    private long id;
    private long userId;
    private OsType osType;
    private String pushToken;
    private String deviceId;
}
