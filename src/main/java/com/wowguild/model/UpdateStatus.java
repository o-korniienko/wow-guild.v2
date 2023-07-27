package com.wowguild.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatus<T> {

    private String status;
    private T result;
}
