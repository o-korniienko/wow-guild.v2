package com.wowguild.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateStatus<T> {

    private String status;
    private T result;
}
