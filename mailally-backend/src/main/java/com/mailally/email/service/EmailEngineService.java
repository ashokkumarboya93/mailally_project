package com.mailally.email.service;

import com.mailally.email.provider.EmailSendResult;

public interface EmailEngineService {
    boolean sendEmail(String to, String toName, String subject, String htmlBody);
    boolean sendEmail(String to, String toName, String from, String fromName, String replyTo, String subject, String htmlBody);
    EmailSendResult sendEmailWithResult(String to, String toName, String from, String fromName, String replyTo, String subject, String htmlBody);
}

