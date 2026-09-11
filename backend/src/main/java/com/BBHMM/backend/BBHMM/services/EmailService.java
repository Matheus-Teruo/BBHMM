package com.BBHMM.backend.BBHMM.services;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.BBHMM.backend.BBHMM.config.props.EmailProperties;
import com.BBHMM.backend.BBHMM.models.User;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
@EnableConfigurationProperties(EmailProperties.class)
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final EmailProperties props;

    @Async
    public void sendValidationEmail(User user, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(props.senderEmail());
            helper.setTo(Objects.requireNonNull(user.getEmail()));
            helper.setSubject("Confirmação de Conta | BBHMM");

            Context context = new Context();
            context.setVariable("userName", user.getFullname());
            context.setVariable("token", token);

            String htmlContent = templateEngine.process("email-templates/validation-email", context);

            helper.setText(Objects.requireNonNull(htmlContent), true); 

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            log.error("Falha ao enviar e-mail: " + e.getMessage());
        }
    }

    @Async
    public void sendPasswordResetEmail(User user, String resetToken) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(props.senderEmail());
            helper.setTo(Objects.requireNonNull(user.getEmail()));
            helper.setSubject("Solicitação de Troca de Senha | BBHMM");

            Context context = new Context();
            context.setVariable("userName", user.getFullname());

            String token = URLEncoder.encode(resetToken, StandardCharsets.UTF_8);
            
            String resetLink = props.redirectUrl() + "/auth/user/reset-password?token=" + token; 
            context.setVariable("resetLink", resetLink);

            String htmlContent = templateEngine.process("email-templates/reset-password-email", context);

            helper.setText(Objects.requireNonNull(htmlContent), true);

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            log.error("Falha ao enviar e-mail de troca de senha: " + e.getMessage());
        }
    }
}
