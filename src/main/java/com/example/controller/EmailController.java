package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.EmailService;
import com.exampledto.EmailDTO;

@RestController
@RequestMapping("/api/email")
public class EmailController {
@Autowired
    private  EmailService emailService;


@PostMapping("/send")
public ResponseEntity<String> sendEmail(@RequestBody EmailDTO emailDTO) {
    try {
        emailService.sendMail(emailDTO);
        return ResponseEntity.ok("Email sent successfully to " + emailDTO.getTo());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Failed to send email: " + e.getMessage());
    }
    
    
}



}
