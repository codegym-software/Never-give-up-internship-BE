package com.example.InternShip.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //INVALID
    ROLE_INVALID("Vai trò không hợp lệ."),
    TOKEN_INVALID("Token không hợp lệ"),
    USERNAME_INVALID("Tên đăng nhập không hợp lệ"),
    EMAIL_INVALID("Email không hợp lệ"),
    PASSWORD_INVALID("Mật khấu không hợp lệ"),
    FULL_NAME_INVALID("Họ tên không hợp lệ"),
    PHONE_INVALID("Số điện thoại không hợp lệ"),
    VERIFICATION_CODE_INVALID("Mã xác thực không hợp lệ"),
    STATUS_INVALID("trạng thái không hợp lệ "),
    MAJOR_INVALID ("Chuyên ngành không hợp lệ"),
    UNIVERSITY_INVALID ("Trường học không hợp lệ"),

    //EXISTED
    USERNAME_EXISTED("Tên đăng nhập đã tồn tại."),
    EMAIL_EXISTED("Email đã tồn tại."),
    //NOT_EXISTED
    USER_NOT_EXISTED("Người dùng không tồn tại."),
    VERIFICATION_CODE_NOT_EXISTED("Mã xác thực không tồn tại."),
    INTERN_NOT_EXISTED("Thực tập sinh không tồn tại."),
    UNIVERSITY_NOT_EXISTED ("Trường học không tồn tại"),
    MAJOR_NOT_EXISTED ("Ngành học không tồn tại"),
    //FAILED
    VERIFICATION_FAILED("Xác thực thất bại"),
    VERIFICATION_CODE_SEND_FAILED("Gửi mã xác thực thất bại"),
    //UNAUTHENTICATED
    UNAUTHENTICATED("Đăng nhập thất bại"),
    USER_INACTIVE("Tài khoản đã bị vô hiệu hóa");
    ;
    private final String message;
}
