package com.example.InternShip.service.impl;

import com.example.InternShip.entity.PendingUser;
import com.example.InternShip.entity.User;
import com.example.InternShip.entity.enums.Role;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.PendingUserRepository;
import com.example.InternShip.repository.UserRepository;
import com.example.InternShip.service.PendingUserService;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PendingUserServiceImpl implements PendingUserService {
    private final PendingUserRepository pendingUserRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromMail;

    public void verify(String token) {
        try {
            PendingUser pendingUser = pendingUserRepository.findByToken(token)
                    .orElseThrow(() -> new RuntimeException(ErrorCode.VERIFICATION_CODE_NOT_EXISTED.getMessage()));
            if (pendingUser.getExpiryDate().isBefore(LocalDateTime.now())) {
                throw new RuntimeException(ErrorCode.VERIFICATION_CODE_INVALID.getMessage());
            }

            User user = modelMapper.map(pendingUser, User.class);
            user.setRole(Role.VISITOR);
            userRepository.save(user);
            pendingUserRepository.delete(pendingUser);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(ErrorCode.VERIFICATION_FAILED.getMessage());
        }
    }

    public void verify_ForgetPassword(String token) {
        try {
            PendingUser pendingUser = pendingUserRepository.findByToken(token)
                    .orElseThrow(() -> new RuntimeException(ErrorCode.VERIFICATION_CODE_NOT_EXISTED.getMessage()));
            if (pendingUser.getExpiryDate().isBefore(LocalDateTime.now())) {
                throw new RuntimeException(ErrorCode.VERIFICATION_CODE_INVALID.getMessage());
            }

            User user = userRepository.findByUsernameOrEmail(pendingUser.getEmail())
                    .orElseThrow(() -> new RuntimeException(ErrorCode.USER_NOT_EXISTED.getMessage()));
            user.setPassword(pendingUser.getPassword());
            userRepository.save(user);
            pendingUserRepository.delete(pendingUser);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(ErrorCode.VERIFICATION_FAILED.getMessage());
        }
    }

    public void sendVerification(String email, String verifyLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(new InternetAddress(fromMail));
            helper.setTo(email);
            helper.setSubject("Xác thực email của bạn");

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
            String formattedNow = now.format(formatter);

            String emailContent = """
                        <html>
                            <body>
                                <h2>Xin chào!</h2>
                                <p>Liên kết xác thực sẽ hết hạn sau 20 phút tính từ %s</p>
                                <p>Vui lòng click liên kết bên dưới để xác thực email của bạn:</p>
                                <p><a href='%s'>Xác thực ngay</a></p>
                            </body>
                        </html>
                    """.formatted(formattedNow, verifyLink);
            helper.setText(emailContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(ErrorCode.VERIFICATION_CODE_SEND_FAILED.getMessage());
        }
    }
}
