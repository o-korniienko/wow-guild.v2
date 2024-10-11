package com.wowguild.common.dto.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<DATA> {

    String message;
    int status;
    DATA data;

    public ApiResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
