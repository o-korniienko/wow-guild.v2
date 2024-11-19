package com.wowguild.common.converter;

import com.wowguild.common.dto.SimpleChatMessageDto;
import com.wowguild.common.entity.SimpleChatMessage;
import org.springframework.stereotype.Service;

@Service
public class SimpleChatMessageConverter implements Converter<SimpleChatMessage, SimpleChatMessageDto> {
    @Override
    public SimpleChatMessage convertToEntity(SimpleChatMessageDto simpleChatMessageDto) {
        if (simpleChatMessageDto != null) {
            SimpleChatMessage message = new SimpleChatMessage();
            message.setId(simpleChatMessageDto.getId());
            message.setMessage(simpleChatMessageDto.getMessage());
            message.setDate(simpleChatMessageDto.getDate());
            message.setUserId(simpleChatMessageDto.getUserId());
            return message;
        }

        return null;
    }

    @Override
    public SimpleChatMessageDto convertToDto(SimpleChatMessage simpleChatMessage) {
        if (simpleChatMessage != null) {
            SimpleChatMessageDto message = new SimpleChatMessageDto();
            message.setId(simpleChatMessage.getId());
            message.setMessage(simpleChatMessage.getMessage());
            message.setDate(simpleChatMessage.getDate());
            message.setUserId(simpleChatMessage.getUserId());
            return message;
        }
        return null;
    }
}
