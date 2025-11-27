package com.example.InternShip.aspect;

import com.example.InternShip.annotation.LogActivity;
import com.example.InternShip.entity.Log;
import com.example.InternShip.entity.User;
import com.example.InternShip.repository.LogRepository;
import com.example.InternShip.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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
    @Transactional
    public void logActivity(JoinPoint joinPoint, LogActivity logActivity, Object result) {
        try {
            // 1. Lấy User hiện tại
            User currentUser = null;
            try {
                currentUser = authService.getUserLogin();
            } catch (Exception e) {
                // Bỏ qua nếu không có user
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
            String description = logActivity.description();

            // --- LOGIC MỚI Ở ĐÂY ---
            if (dataJson != null && !dataJson.equals("{}") && !dataJson.isEmpty()) {
                // Nếu có JSON: Lưu cả Description + JSON để dễ đọc
                // Ví dụ: "Tạo mới chương trình | Data: {"name": "Java", ...}"
                newLog.setDataChange(description + " | Chi tiết: " + dataJson);
            } else {
                // Nếu không có JSON: Chỉ lưu Description
                newLog.setDataChange(description);
            }
            // -----------------------

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
            String[] paramNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();

            Map<String, Object> logData = new HashMap<>();

            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                String paramName = paramNames[i];

                if (arg instanceof MultipartFile || arg instanceof MultipartFile[]
                        || arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
                    continue;
                }

                // Nếu chỉ có 1 tham số là DTO phức tạp -> Log thẳng object đó cho gọn
                if (args.length == 1 && !isPrimitiveOrWrapper(arg)) {
                    return objectMapper.writeValueAsString(arg);
                }

                logData.put(paramName, arg);
            }

            return objectMapper.writeValueAsString(logData);

        } catch (Exception e) {
            return "";
        }
    }

    private boolean isPrimitiveOrWrapper(Object obj) {
        Class<?> clazz = obj.getClass();
        return clazz.isPrimitive() || clazz == Integer.class || clazz == Long.class
                || clazz == Double.class || clazz == String.class || clazz == Boolean.class;
    }
}