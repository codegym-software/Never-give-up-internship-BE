package com.example.demo.exception;

import lombok.Getter;


@Getter
public enum ErrorCode {
    //UNIQUE
    DUPLICATE_DATA(400, "Thông tin bị trùng lặp"),
    DUPLICATE_EMAIL(400, "Email đã tồn tại"),
    DUPLICATE_USERNAME(400, "Tên đăng nhập đã tồn tại"),
    //NOT_EMPTY
    EMAIL_NOT_EMPTY(400, "Email không được để trống"),
    NAME_NOT_EMPTY(400, "Họ tên không được để trống"),
    GENDER_NOT_EMPTY(400, "Giới tính không được để trống"),
    PASSWORD_NOT_EMPTY(400, "Mật khẩu không được để trống"),
    USERNAME_NOT_EMPTY(400, "Tên đăng nhập không được để trống"),
    PHONE_NOT_EMPTY(400, "Số điện thoại không được để trống"),
    ADDRESS_NOT_EMPTY(400, "Địa chỉ không được để trống"),
    BIRTHDAY_NOT_EMPTY(400, "Ngày sinh không được để trống"),
    FULL_NAME_NOT_EMPTY(400, "Họ tên không được để trống"),
    //INVALID
    INVALID_EMAIL(400, "Email không hợp lệ"),
    INVALID_TOKEN(400, "Token đã hết hiệu lực"),
    INVALID_PASSWORD(400, "Mật khẩu phải từ 8-20 kí tự"),
    INVALID_USERNAME(400, "Tên đăng nhập phải ít từ 5-50 kí tự"),
    INVALID_GENDER(400, "Giới tính chỉ được là Nam hoặc Nữ"),
    INVALID_LOGIN(400, "Thông tin đăng nhập không hợp lệ"),
    INVALID_PHONE(400, "Số điện thoại phải bắt đầu bằng 0 và có 10 chữ số"),
    INVALID_BIRTHDAY(400,"Ngày sinh phải ở quá khứ"),
    //NOT_FOUND
    USER_NOT_FOUND(404, "Người dùng không tồn tại"),
    ACCOUNT_NOT_FOUND(404, "Tài khoản không tồn tại"),
    TOKEN_NOT_FOUND(404,"Email token không tồn tại"),
    //FAILED
    EMAIL_SEND_FAILED(400, "Gửi email xác thực thất bại"),
    VERIFY_FAILED(400, "Xác thực thất bại"),
    //ACCESS_DENIED
    LOGIN_FAILED(403, "Đăng nhập thất bại"),
    ACCOUNT_DISABLED(403, "Tài khoản đã bị vô hiệu hóa"),
    ROLE_NOT_FOUND(1012,"Không tìm thấy vai trò");


    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    private int code;
    private String message;
}
