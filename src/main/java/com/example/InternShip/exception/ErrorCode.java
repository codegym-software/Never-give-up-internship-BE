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
    TIME_APPLY_INVALID("Đã quá hạn nộp đơn thực tập"),
    STATUS_APPLICATION_INVALID("Trạng thái cập nhật không hợp lệ cho: "),
    ACTION_INVALID("Hành động không hợp lệ"),


    //EXISTED
    USERNAME_EXISTED("Tên đăng nhập đã tồn tại."),
    EMAIL_EXISTED("Email đã tồn tại."),
    APPLICATION_EXISTED("Hồ sơ đã tồn tại"),
    //NOT_EXISTED
    USER_NOT_EXISTED("Người dùng không tồn tại."),
    VERIFICATION_CODE_NOT_EXISTED("Mã xác thực không tồn tại."),
    INTERN_NOT_EXISTED("Thực tập sinh không tồn tại."),
    UNIVERSITY_NOT_EXISTED ("Trường học không tồn tại"),
    MAJOR_NOT_EXISTED ("Ngành học không tồn tại"),
    INTERNSHIPTERM_NOT_EXISTED ("Kì thực tập không tồn tại"),
    INTERNSHIP_APPLICATION_NOT_EXISTED("Đơn đăng ký không tồn tại"),

    //NOT_NULL
    UNIVERSITY_NOT_NULL ("Trường học không được để trống"),
    MAJOR_NOT_NULL ("Ngành học không được để trống"),
    STATUS_NOT_BLANK("Trạng thái không được để trống"),
    //FAILED
    VERIFICATION_FAILED("Xác thực thất bại"),
    VERIFICATION_CODE_SEND_FAILED("Gửi mã xác thực thất bại"),
    SUBMIT_FAILED("Gửi thất bại"),
    WITHDRAWAL_FAILED("Rút đơn thất bại"),

    //UNAUTHENTICATED
    UNAUTHENTICATED("Đăng nhập thất bại"),
    USER_INACTIVE("Tài khoản đã bị vô hiệu hóa"), 
    UNAUTHORIZED_ACTION("Hành động không được phép"),
    
    ;
    private final String message;
}
