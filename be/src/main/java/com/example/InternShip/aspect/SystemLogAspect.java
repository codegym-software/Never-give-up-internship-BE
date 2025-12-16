package com.example.InternShip.aspect;

import com.example.InternShip.annotation.LogActivity;
import com.example.InternShip.entity.Log;
import com.example.InternShip.entity.User;
import com.example.InternShip.repository.LogRepository;
import com.example.InternShip.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class SystemLogAspect {

    private final LogRepository logRepository;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager entityManager; // Dùng để query dữ liệu động

    // Dùng @Around để bao trọn quá trình (Trước và Sau khi chạy hàm)
    @Around("@annotation(logActivity)")
    public Object logActivity(ProceedingJoinPoint joinPoint, LogActivity logActivity) throws Throwable {

        //Lấy dữ liệu cũ (Pre-processing)
        String oldDataJson = null;
        Object entityId = null;

        // Chỉ tìm dữ liệu cũ nếu là Sửa hoặc Xóa và có khai báo entityType
        if ((logActivity.action() == Log.Action.MODIFY || logActivity.action() == Log.Action.DELETE)
                && logActivity.entityType() != Void.class) {

            try {
                // Giả định: ID thường là tham số đầu tiên hoặc tham số kiểu Integer/Long đầu tiên
                entityId = findIdInArgs(joinPoint.getArgs());

                if (entityId != null) {
                    // Query DB lấy entity hiện tại
                    Object oldEntity = entityManager.find(logActivity.entityType(), entityId);
                    if (oldEntity != null) {
                        // Detach để tránh Hibernate tự động update nếu có thay đổi ngầm
                        entityManager.detach(oldEntity);
                        oldDataJson = convertToJson(oldEntity);
                    }
                }
            } catch (Exception e) {
                log.warn("Không thể lấy dữ liệu cũ cho Log: {}", e.getMessage());
            }
        }

        //Thực thi hàm chính
        Object result = joinPoint.proceed();

        //Lấy dữ liệu mới và lưu log
        try {
            saveLog(logActivity, oldDataJson, result, joinPoint.getArgs(), entityId);
        } catch (Exception e) {
            log.error("Lỗi khi lưu Log: ", e);
        }

        return result;
    }

    private void saveLog(LogActivity logActivity, String oldDataJson, Object result, Object[] args, Object entityId) {
        User currentUser = null;
        try {
            currentUser = authService.getUserLogin();
        } catch (Exception e) { /* Bỏ qua */ }
        if (currentUser == null) return;

        Log log = new Log();
        log.setAction(currentUser);
        log.setActionAt(LocalDateTime.now());
        log.setActionType(logActivity.action());
        log.setAffected(logActivity.affected());
        log.setDescription(logActivity.description());

        // Set Data Old
        log.setDataOld(oldDataJson);

        // Set Data New
        String newDataJson = null;

        if (logActivity.action() == Log.Action.DELETE) {
            newDataJson = null; // Xóa thì không có dữ liệu mới
        } else if (logActivity.action() == Log.Action.CREATE) {
            // Tạo mới: Dữ liệu mới chính là Result trả về (nếu có) hoặc tham số DTO truyền vào
            newDataJson = (result != null) ? convertToJson(result) : getDtoFromJson(args);
        } else if (logActivity.action() == Log.Action.MODIFY) {
            //Nếu hàm trả về Entity mới -> dùng Result
            //Query lại DB (chậm hơn chút nhưng chính xác nhất)
            if (result != null) {
                newDataJson = convertToJson(result);
            } else if (entityId != null && logActivity.entityType() != Void.class) {
                Object newEntity = entityManager.find(logActivity.entityType(), entityId);
                newDataJson = convertToJson(newEntity);
            }
        }

        log.setDataNew(newDataJson);
        logRepository.save(log);
    }

    private Object findIdInArgs(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Integer || arg instanceof Long) {
                return arg; // Lấy Integer/Long đầu tiên làm ID
            }
        }
        return null;
    }

    private String convertToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            return null;
        }
    }

    // Lấy tham số DTO (bỏ qua ID và File)
    private String getDtoFromJson(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof MultipartFile || arg instanceof Integer || arg instanceof Long) continue;
            return convertToJson(arg);
        }
        return null;
    }
}