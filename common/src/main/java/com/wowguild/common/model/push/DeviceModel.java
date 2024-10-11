package com.wowguild.common.model.push;

import lombok.Data;

@Data
public class DeviceModel {

    private String token;
    private String deviceId;
    private String osType;
}
