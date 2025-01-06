package com.wowguild.arguments;

import com.wowguild.common.entity.push.PushDevice;
import com.wowguild.common.enums.push.OsType;

public class PushDeviceGenerator {

    public static PushDevice getPushDevice(Long id, OsType osType) {
        PushDevice device = new PushDevice();
        device.setId(id);
        device.setDeviceId("some device id");
        device.setPushToken("some firebase token");
        device.setUserId(1);
        device.setOsType(osType);

        return device;
    }
}
