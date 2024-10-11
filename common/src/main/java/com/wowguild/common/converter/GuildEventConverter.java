package com.wowguild.common.converter;

import com.wowguild.common.dto.GuildEventDto;
import com.wowguild.common.entity.GuildEvent;
import org.springframework.stereotype.Service;

@Service
public class GuildEventConverter implements Converter<GuildEvent, GuildEventDto> {

    @Override
    public GuildEvent convertToEntity(GuildEventDto guildEventDto) {
        if (guildEventDto != null) {
            GuildEvent event = new GuildEvent();
            event.setId(guildEventDto.getId());
            event.setDate(guildEventDto.getDate());
            event.setMessage(guildEventDto.getMessage());
            event.setSubject(guildEventDto.getSubject());
            event.setUserIDs(guildEventDto.getUserIDs());

            return event;
        }

        return null;
    }

    @Override
    public GuildEventDto convertToDto(GuildEvent guildEvent) {
        if (guildEvent != null) {
            GuildEventDto event = new GuildEventDto();
            event.setId(guildEvent.getId());
            event.setDate(guildEvent.getDate());
            event.setMessage(guildEvent.getMessage());
            event.setSubject(guildEvent.getSubject());
            event.setUserIDs(guildEvent.getUserIDs());

            return event;
        }
        return null;
    }
}
