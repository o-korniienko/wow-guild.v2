package com.wowguild.common.dto.wow;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatus<T> {

    private String status;
    private T result;
    private int statusCode;
}
