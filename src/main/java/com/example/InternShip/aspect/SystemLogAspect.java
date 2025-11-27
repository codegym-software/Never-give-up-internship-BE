package com.example.InternShip.aspect;

import com.example.InternShip.annotation.LogActivity;
import com.example.InternShip.entity.Log;
import com.example.InternShip.entity.User;
import com.example.InternShip.repository.LogRepository;
import com.example.InternShip.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class SystemLogAspect {

    private final LogRepository logRepository;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @AfterReturning(pointcut = "@annotation(logActivity)", returning = "result")
    public void logActivity(JoinPoint joinPoint, LogActivity logActivity, Object result) {
        try {
            // 1. Lấy User hiện tại
            User currentUser = null;
            try {
                currentUser = authService.getUserLogin();
            } catch (Exception e) {
                // Bỏ qua nếu không có user (tác vụ hệ thống)
            }
            if (currentUser == null) return;

            // 2. Tạo Log
            Log newLog = new Log();
            newLog.setAction(currentUser);
            newLog.setActionAt(LocalDateTime.now());
            newLog.setActionType(logActivity.action());
            newLog.setAffected(logActivity.affected());

            // 3. Xử lý dữ liệu thay đổi (JSON)
            String dataJson = getPayloadJson(joinPoint);

            // Nếu JSON rỗng (hoặc chỉ có {}), dùng description mặc định
            if (dataJson == null || dataJson.equals("{}") || dataJson.isEmpty()) {
                newLog.setDataChange(logActivity.description());
            } else {
                newLog.setDataChange(dataJson);
            }

            logRepository.save(newLog);

        } catch (Exception e) {
            log.error("Lỗi ghi log: ", e);
        }
    }

    /**
     * Hàm thông minh: Lấy tên tham số và giá trị, bỏ qua File, convert sang JSON
     */
    private String getPayloadJson(JoinPoint joinPoint) {
        try {
            CodeSignature signature = (CodeSignature) joinPoint.getSignature();
            String[] paramNames = signature.getParameterNames(); // Tên tham số (vd: id, request)
            Object[] args = joinPoint.getArgs();                 // Giá trị tham số

            Map<String, Object> logData = new HashMap<>();

            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                String paramName = paramNames[i];

                // Bỏ qua các đối tượng hệ thống không cần log
                if (arg instanceof MultipartFile || arg instanceof MultipartFile[]
                        || arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
                    continue;
                }

                // Nếu tham số là DTO (Request Body), ta thường muốn log nội dung nó trực tiếp
                // chứ không muốn bọc trong tên biến.
                // Ví dụ: muốn { "name": "A" } thay vì { "request": { "name": "A" } }
                // Logic: Nếu chỉ có 1 tham số và nó là Object phức tạp -> Log thẳng Object đó.
                if (args.length == 1 && !isPrimitiveOrWrapper(arg)) {
                    return objectMapper.writeValueAsString(arg);
                }

                // Nếu có nhiều tham số (VD: id, request), ta đưa vào Map
                logData.put(paramName, arg);
            }

            return objectMapper.writeValueAsString(logData);

        } catch (Exception e) {
            return "";
        }
    }

    // Helper: Kiểm tra kiểu dữ liệu đơn giản
    private boolean isPrimitiveOrWrapper(Object obj) {
        Class<?> clazz = obj.getClass();
        return clazz.isPrimitive() || clazz == Integer.class || clazz == Long.class
                || clazz == Double.class || clazz == String.class || clazz == Boolean.class;
    }
}