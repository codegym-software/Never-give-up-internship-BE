package com.example.demo.exception;

import lombok.Getter;


@Getter
public enum ErrorCode {
    DUPLICATE_DATA(2001, "Thông tin bị trùng lặp"),
    EMAIL_NOT_EMPTY(2001, "Email không được để trống"),
    NAME_NOT_EMPTY(2001, "Họ tên không được để trống"),
    GENDER_NOT_EMPTY(2001, "Giới tính không được để trống"),
    EMAIL_INVALID(2001, "Email không hợp lệ"),
    INVALID_GENDER(2001, "Giới tính chỉ được là Nam hoặc Nữ")
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    private int code;
    private String message;
}
