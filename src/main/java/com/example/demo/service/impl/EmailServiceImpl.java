package com.example.demo.service.impl;

import com.example.demo.entity.EmailToken;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.EmailTokenRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final EmailTokenRepository emailTokenRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Override
    public  void sendVerification(String email, String verifyLink){
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
        }catch (Exception e){
            throw new AppException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    @Override
    public void verify(String token){
        try {
            EmailToken emailToken = emailTokenRepository.findByToken(token)
                    .orElseThrow(() -> new AppException(ErrorCode.TOKEN_NOT_FOUND));
            if (emailToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                throw new AppException(ErrorCode.INVALID_TOKEN);
            }

            User user = new User();
            user.setEmail(emailToken.getEmail());
            user.setUsername(emailToken.getUsername());
            user.setPassword(emailToken.getPassword());
            user.setFullName(emailToken.getFullName());
            Role userRole = roleRepository.findByRoleName("USER")
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)); // Ném ra lỗi nếu không tìm thấy Role
            user.setRole(userRole);
            userRepository.save(user);
        }catch (Exception e){
            throw new AppException(ErrorCode.VERIFY_FAILED);
        }
    }
}
