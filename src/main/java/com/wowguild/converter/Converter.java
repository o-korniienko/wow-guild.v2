package com.wowguild.converter;

import org.springframework.stereotype.Service;

@Service
public interface Converter<Entity, Dto> {

    Entity convertToEntity(Dto dto);

    Dto convertToDto(Entity entity);
}