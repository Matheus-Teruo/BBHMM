package com.BBHMM.backend.BBHMM.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.BBHMM.backend.BBHMM.models.User;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.sender.email}")
    private String email;

    @Value("${spring.mail.sender.frontend.domain.url}")
    private String domain;

    @Async
    public void sendValidationEmail(User user, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(email);
            helper.setTo(user.getEmail());
            helper.setSubject("Confirmação de Conta | BBHMM");

            Context context = new Context();
            context.setVariable("userName", user.getFullname());
            context.setVariable("token", token);

            String htmlContent = templateEngine.process("email-templates/validation-email", context);

            helper.setText(htmlContent, true); 

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            System.err.println("Falha ao enviar e-mail: " + e.getMessage());
        }
    }

    @Async
    public void sendPasswordResetEmail(User user, String resetToken) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(email);
            helper.setTo(user.getEmail());
            helper.setSubject("Solicitação de Troca de Senha | BBHMM");

            Context context = new Context();
            context.setVariable("userName", user.getFullname());
            
            String resetLink = "http://" + domain + "/reset-password?token=" + resetToken; 
            context.setVariable("resetLink", resetLink);

            String htmlContent = templateEngine.process("email-templates/reset-password-email", context);

            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            System.err.println("Falha ao enviar e-mail de troca de senha: " + e.getMessage());
        }
    }
}
