package com.BBHMM.backend.BBHMM.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendValidationEmail(String to, String token) {
        try {
            // 1. Criar o objeto MimeMessage (necessário para HTML)
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("noreply@minhaapp.com"); // Seu e-mail verificado (SendGrid)
            helper.setTo(to);
            helper.setSubject("Confirmação de Conta | Minha App");

            // 2. Criar o Contexto do Thymeleaf (o "model" para o template)
            Context context = new Context();
            context.setVariable("userName", userName);
            
            // Cria o URL de confirmação com o token
            String confirmationUrl = "http://localhost:8080/api/auth/confirm?token=" + token;
            context.setVariable("confirmationUrl", confirmationUrl);

            // 3. Processar (renderizar) o template
            // O Spring busca o arquivo em templates/email-templates/validation-email.html
            String htmlContent = templateEngine.process("email-templates/validation-email", context);

            // 4. Inserir o conteúdo HTML no e-mail (o "true" indica que é HTML)
            helper.setText(htmlContent, true); 

            // 5. Enviar
            mailSender.send(mimeMessage);
            System.out.println("E-mail HTML de validação enviado para: " + to);

        } catch (Exception e) {
            System.err.println("Falha ao enviar e-mail: " + e.getMessage());
        }
    }

    @Async
    public void sendPasswordResetEmail(String to, String userName, String resetToken) {
        try {
            // Usa MimeMessageHelper para suportar HTML
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("noreply@minhaapp.com");
            helper.setTo(to);
            helper.setSubject("Solicitação de Troca de Senha | Minha App");

            // Cria o contexto de dados para o template
            Context context = new Context();
            context.setVariable("userName", userName);
            
            // Simula o URL que levará o usuário para o frontend
            String resetLink = "http://localhost:3000/reset-password?token=" + resetToken; 
            context.setVariable("resetLink", resetLink);

            // Processa o template HTML
            String htmlContent = templateEngine.process("email-templates/reset-password-email", context);

            // Define o conteúdo como HTML (o 'true' é fundamental)
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            System.out.println("E-mail de troca de senha enviado para: " + to);

        } catch (Exception e) {
            System.err.println("Falha ao enviar e-mail de troca de senha: " + e.getMessage());
        }
    }
}
