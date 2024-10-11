package com.wowguild.push_api.service;

import com.wowguild.common.entity.push.PushDevice;
import com.wowguild.common.entity.security.User;
import com.wowguild.common.enums.push.OsType;
import com.wowguild.common.model.push.DeviceModel;
import com.wowguild.common.service.UserService;
import com.wowguild.common.service.impl.PushDeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeviceService {

    private final UserService userService;
    private final PushDeviceService pushDeviceService;

    public Mono<ServerResponse> processRegistration(ServerRequest serverRequest) {
        Mono<Authentication> authUserMono = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication);

        Mono<DeviceModel> requestBodyMono = serverRequest.bodyToMono(DeviceModel.class);

        return authUserMono.zipWith(requestBodyMono)
                .flatMap(tuple -> {
                    Authentication auth = tuple.getT1();
                    DeviceModel deviceRequest = tuple.getT2();
                    String username = auth.getName();
                    User user = userService.findByUsername(username);

                    if (register(deviceRequest, user)) {
                        return ServerResponse.ok()
                                .contentType(MediaType.TEXT_PLAIN)
                                .body(BodyInserters.fromValue("Device registered"));
                    } else {
                        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.TEXT_PLAIN)
                                .body(BodyInserters.fromValue("Could not register device"));
                    }


                })
                .switchIfEmpty(ServerResponse.badRequest().build());
    }

    private boolean register(DeviceModel deviceModel, User user) {
        try {
            PushDevice pushDevice = pushDeviceService.findBiUserId(user.getId());
            if (pushDevice == null) {
                pushDevice = new PushDevice();
                pushDevice.setDeviceId(deviceModel.getDeviceId());
                pushDevice.setOsType(OsType.valueOf(deviceModel.getOsType()));
                pushDevice.setPushToken(deviceModel.getToken());
                pushDevice.setUserId(user.getId());

                pushDeviceService.save(pushDevice);
            } else {
                if (pushDevice.getDeviceId().equals(deviceModel.getDeviceId())) {
                    pushDevice.setPushToken(deviceModel.getToken());

                    pushDeviceService.save(pushDevice);
                } else {
                    pushDevice = new PushDevice();
                    pushDevice.setDeviceId(deviceModel.getDeviceId());
                    pushDevice.setOsType(OsType.valueOf(deviceModel.getOsType()));
                    pushDevice.setPushToken(deviceModel.getToken());
                    pushDevice.setUserId(user.getId());

                    pushDeviceService.save(pushDevice);
                }
            }
        } catch (Exception e) {
            log.error("Could not register push device. Error: {}", e.getMessage());
            return false;
        }

        return true;
    }
}
