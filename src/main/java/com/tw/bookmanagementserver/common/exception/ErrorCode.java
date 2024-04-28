package com.tw.bookmanagementserver.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND(404, "not found"),
    INTERNAL_SERVER_ERROR(500, "internal server error");

    private final int code;
    private final String message;
}
