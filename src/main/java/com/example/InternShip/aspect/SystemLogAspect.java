package com.example.InternShip.aspect;

import com.example.InternShip.annotation.LogActivity;
import com.example.InternShip.entity.Log;
import com.example.InternShip.entity.User;
import com.example.InternShip.repository.LogRepository;
import com.example.InternShip.service.AuthService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class SystemLogAspect {

    private final LogRepository logRepository;
    private final AuthService authService;

    @PersistenceContext
    private EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    @Around("@annotation(logActivity)")
    public Object logActivity(ProceedingJoinPoint joinPoint, LogActivity logActivity) throws Throwable {

        //1. PRE-PROCESSING: LẤY OLD DATA
        String oldDataJson = null;
        Object entityId = null;

        if ((logActivity.action() == Log.Action.MODIFY || logActivity.action() == Log.Action.DELETE)
                && logActivity.entityType() != Void.class) {
            try {
                entityId = findIdInArgs(joinPoint.getArgs());

                // Fallback ID User (nếu sửa Profile)
                if (entityId == null && logActivity.entityType() == User.class) {
                    try {
                        User u = authService.getUserLogin();
                        if (u != null) entityId = u.getId();
                    } catch (Exception e) {}
                }

                if (entityId != null) {
                    Object oldEntity = entityManager.find(logActivity.entityType(), entityId);
                    if (oldEntity != null) {
                        Object realEntity = unproxy(oldEntity);
                        entityManager.detach(realEntity);
                        oldDataJson = convertToJsonSafe(realEntity);
                    }
                }
            } catch (Exception e) {
                log.warn("Không thể lấy dataOld: {}", e.getMessage());
            }
        }

        //2. EXECUTION
        Object result = joinPoint.proceed();

        //3. POST-PROCESSING
        try {
            saveLog(logActivity, oldDataJson, result, joinPoint, entityId);
        } catch (Exception e) {
            log.error("Lỗi lưu Log: ", e);
        }

        return result;
    }

    private void saveLog(LogActivity logActivity, String oldDataJson, Object result, ProceedingJoinPoint joinPoint, Object entityId) {
        User currentUser = null;
        try { currentUser = authService.getUserLogin(); } catch (Exception e) {}
        if (currentUser == null) return;

        Log log = new Log();
        log.setAction(currentUser);
        log.setActionAt(LocalDateTime.now());
        log.setActionType(logActivity.action());
        log.setAffected(logActivity.affected());

        String payload = getPayloadJson(joinPoint);
        log.setDescription(logActivity.description());

        String finalNewData = null;

        if (logActivity.action() == Log.Action.DELETE) {
            finalNewData = null;
        }
        else if (logActivity.action() == Log.Action.CREATE) {
            finalNewData = (result != null) ? convertToJsonSafe(result) : payload;
        }
        else if (logActivity.action() == Log.Action.MODIFY) {
            String fullNewJson = null;

            if (result != null && logActivity.entityType().isInstance(result)) {
                fullNewJson = convertToJsonSafe(unproxy(result));
            }

            if (fullNewJson == null && entityId != null && logActivity.entityType() != Void.class) {
                Object newEntity = entityManager.find(logActivity.entityType(), entityId);
                fullNewJson = convertToJsonSafe(unproxy(newEntity));
            }

            // Diff Logic
            if (oldDataJson != null && fullNewJson != null) {
                finalNewData = getJsonDiff(oldDataJson, fullNewJson);
            } else {
                finalNewData = fullNewJson;
            }
        }

        log.setDataOld(oldDataJson);
        log.setDataNew(finalNewData);
        logRepository.save(log);
    }

    private String convertToJsonSafe(Object object) {
        if (object == null) return null;
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            try {
                Map<String, Object> safeMap = new HashMap<>();
                for (Field field : object.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    if (value == null) continue; // Bỏ qua null

                    if (isPrimitiveOrWrapper(value) || value instanceof String
                            || value instanceof Enum) {
                        safeMap.put(field.getName(), value);
                    }
                    else if (value instanceof LocalDateTime) {
                        safeMap.put(field.getName(), ((LocalDateTime) value).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    }
                    else if (value instanceof LocalDate) {
                        safeMap.put(field.getName(), value.toString());
                    }
                    else if (!(value instanceof Collection) && !(value instanceof Map)) {
                        safeMap.put(field.getName(), getEntitySummary(value));
                    }
                }
                return objectMapper.writeValueAsString(safeMap);
            } catch (Exception ex2) {
                return "{}";
            }
        }
    }

    private Map<String, Object> getEntitySummary(Object entity) {
        Map<String, Object> summary = new HashMap<>();
        try {
            Class<?> clazz = entity.getClass();

            //1. Lấy ID
            try {
                Method getId = clazz.getMethod("getId");
                Object id = getId.invoke(entity);
                if (id != null) summary.put("id", id);
            } catch (Exception e) {}

            //2. Quét TỰ ĐỘNG các trường có thể là Tên
            String[] potentialNameGetters = {
                    "getName", "getFullName", "getTitle", "getEmail", "getUsername", "getCode", "getSlug"
            };

            for (String getter : potentialNameGetters) {
                try {
                    Method method = clazz.getMethod(getter);
                    Object val = method.invoke(entity);
                    if (val != null && val instanceof String && !((String)val).isEmpty()) {
                        // Biến đổi tên getter thành tên field (getName -> name)
                        String fieldName = Character.toLowerCase(getter.charAt(3)) + getter.substring(4);
                        summary.put(fieldName, val);

                        // Nếu tìm thấy FullName hoặc Name rồi thì thôi, không cần lấy hết cho đỡ rối
                        if (fieldName.equals("fullName") || fieldName.equals("name")) break;
                    }
                } catch (NoSuchMethodException e) { /* Bỏ qua */ }
            }

            //3. Xử lý trường hợp Wrapper (Ví dụ Mentor bọc User bên trong)
            // Nếu summary chỉ có mỗi ID, thử đào sâu thêm 1 cấp tìm User
            if (summary.size() == 1 && summary.containsKey("id")) {
                try {
                    Method getUser = clazz.getMethod("getUser");
                    Object userObj = getUser.invoke(entity);
                    if (userObj != null) {
                        // Đệ quy lấy thông tin User bên trong
                        Map<String, Object> userSummary = getEntitySummary(userObj);
                        userSummary.remove("id"); // Bỏ ID user nếu không cần thiết
                        summary.putAll(userSummary);
                    }
                } catch (Exception e) {}
            }

        } catch (Exception e) {}
        return summary;
    }

    private String getJsonDiff(String oldJson, String newJson) {
        try {
            JsonNode oldNode = objectMapper.readTree(oldJson);
            JsonNode newNode = objectMapper.readTree(newJson);
            ObjectNode diffNode = objectMapper.createObjectNode();
            Iterator<String> fieldNames = newNode.fieldNames();

            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode newValue = newNode.get(fieldName);
                JsonNode oldValue = oldNode.get(fieldName);

                if (fieldName.equals("updatedAt") || fieldName.equals("createdAt") || fieldName.equals("password")) continue;

                // So sánh (Deep Equals của Jackson sẽ lo phần mảng/object)
                if (oldValue == null || !oldValue.equals(newValue)) {
                    diffNode.set(fieldName, newValue);
                }
            }
            if (diffNode.isEmpty()) return "{\"message\": \"Không có thay đổi quan trọng\"}";
            return objectMapper.writeValueAsString(diffNode);
        } catch (Exception e) { return newJson; }
    }

    private Object unproxy(Object entity) {
        if (entity == null) return null;
        if (entity instanceof HibernateProxy) {
            return ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
        }
        return entity;
    }

    private Object findIdInArgs(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Integer || arg instanceof Long) return arg;
        }
        for (Object arg : args) {
            if (arg == null || isPrimitiveOrWrapper(arg) || arg instanceof MultipartFile
                    || arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) continue;
            try {
                Method getId = arg.getClass().getMethod("getId");
                Object id = getId.invoke(arg);
                if (isValidId(id)) return id;
            } catch (Exception e) {}
            try {
                for (Field field : arg.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if ((field.getName().equals("id") || field.getName().endsWith("Id")) && isValidId(field.get(arg))) {
                        return field.get(arg);
                    }
                }
            } catch (Exception e) {}
        }
        return null;
    }

    private boolean isValidId(Object id) {
        return (id instanceof Integer && (Integer) id > 0) || (id instanceof Long && (Long) id > 0);
    }

    private String getPayloadJson(ProceedingJoinPoint joinPoint) {
        try {
            CodeSignature signature = (CodeSignature) joinPoint.getSignature();
            String[] paramNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof MultipartFile || args[i] instanceof HttpServletRequest || args[i] instanceof HttpServletResponse) continue;
                if (args.length == 1 && !isPrimitiveOrWrapper(args[i])) return convertToJsonSafe(args[i]);
                map.put(paramNames[i], args[i]);
            }
            return convertToJsonSafe(map);
        } catch (Exception e) { return ""; }
    }

    private boolean isPrimitiveOrWrapper(Object obj) {
        if (obj == null) return false;
        Class<?> clazz = obj.getClass();
        return clazz.isPrimitive() || clazz == Integer.class || clazz == Long.class
                || clazz == Double.class || clazz == String.class || clazz == Boolean.class;
    }
}