package com.mailally.email.service.impl;

import com.mailally.email.config.EmailEngineConfig;
import com.mailally.email.provider.EmailProviderFactory;
import com.mailally.email.provider.EmailSendResult;
import com.mailally.email.service.EmailEngineService;
import org.springframework.stereotype.Service;

@Service
public class EmailEngineServiceImpl implements EmailEngineService {

    private final EmailProviderFactory providerFactory;
    private final EmailEngineConfig config;

    public EmailEngineServiceImpl(EmailProviderFactory providerFactory, EmailEngineConfig config) {
        this.providerFactory = providerFactory;
        this.config = config;
    }

    @Override
    public boolean sendEmail(String to, String toName, String subject, String htmlBody) {
        return sendEmail(to, toName, config.getDefaultSenderEmail(), config.getDefaultSenderName(), null, subject, htmlBody);
    }

    @Override
    public boolean sendEmail(String to, String toName, String from, String fromName, String replyTo, String subject, String htmlBody) {
        EmailSendResult result = sendEmailWithResult(to, toName, from, fromName, replyTo, subject, htmlBody);
        return result.isSuccess();
    }

    @Override
    public EmailSendResult sendEmailWithResult(String to, String toName, String from, String fromName, String replyTo, String subject, String htmlBody) {
        String senderEmail = (config.getDefaultSenderEmail() != null && !config.getDefaultSenderEmail().isBlank()) 
                ? config.getDefaultSenderEmail().trim() 
                : "info@marcamor.com";
        String senderName = (fromName != null && !fromName.isBlank()) ? fromName : config.getDefaultSenderName();
        return providerFactory.sendWithFailover(to, toName, senderEmail, senderName, replyTo, subject, htmlBody);
    }
}

