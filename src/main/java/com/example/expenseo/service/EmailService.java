package com.example.expenseo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Expenseo - Your Verification Code");

        message.setText("Welcome to Expenseo!\n\n" +
                "Your email verification code is: " + otp + "\n\n" +
                "This code will expire in 10 minutes. Do not share this code with anyone.");

        mailSender.send(message);
    }
}
