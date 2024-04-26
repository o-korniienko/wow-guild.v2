package com.wowguild.tool.parser;

import org.springframework.stereotype.Service;

@Service
public interface Parser<Model> {

    Model parseTo(String json);
}
