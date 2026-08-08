# MailAlly Email Engine: Diagnostic & Architectural Analysis Report

## 1. Executive Summary

During "Test 03" (Subject: *Update Mail on MailAlly Test !!*), a critical mismatch occurred between the user interface metrics and actual delivery results:
- **UI Metrics**: 149 Recipients, 0 Delivered, 149 Failed.
- **Actual Status**: All 149 emails were delivered to the recipients, but sending was extremely slow and the console registered socket timeout errors.

This report diagnoses the root cause of this discrepancy, outlines how the current system processes bulk emails under the hood, verifies the state of third-party message brokers, and offers a clear path toward building an accurate, high-performance delivery system.

---

## 2. Current Email Engine Architecture

The email engine in MailAlly is composed of:
1. **[EmailServiceImpl.java](file:///d:/JDBCSW/MailAlly/mailally-backend/mailally-backend/src/main/java/com/mailally/email/service/impl/EmailServiceImpl.java)**: Coordinates campaign triggering and queries progress.
2. **[CampaignAsyncExecutor.java](file:///d:/JDBCSW/MailAlly/mailally-backend/mailally-backend/src/main/java/com/mailally/email/service/impl/CampaignAsyncExecutor.java)**: Houses the `@Async("emailTaskExecutor")` worker thread which loops through the target lists.
3. **[EmailProviderFactory.java](file:///d:/JDBCSW/MailAlly/mailally-backend/mailally-backend/src/main/java/com/mailally/email/provider/EmailProviderFactory.java)**: Dynamically routes emails to active or fallback providers.
4. **[SmtpEmailProvider.java](file:///d:/JDBCSW/MailAlly/mailally-backend/mailally-backend/src/main/java/com/mailally/email/provider/SmtpEmailProvider.java)**: Sends emails via SMTP using Spring's `JavaMailSender` over Brevo SMTP Relay.

### Asynchronous Thread Pool Configuration
The background dispatching runs on a Spring-managed thread pool (`ThreadPoolTaskExecutor`) named `emailTaskExecutor` defined in **[AsyncConfig.java](file:///d:/JDBCSW/MailAlly/mailally-backend/mailally-backend/src/main/java/com/mailally/config/AsyncConfig.java)**:
- **Core Pool Size**: 5 (can run 5 campaigns concurrently)
- **Max Pool Size**: 20 (burst capacity)
- **Queue Capacity**: 500 (stores campaigns waiting for threads)

---

## 3. Investigation of Redis & Kafka Integration

A deep audit of the project dependencies in **[pom.xml](file:///d:/JDBCSW/MailAlly/mailally-backend/mailally-backend/pom.xml)** confirms the following status:
- **Redis Integration**: ❌ **NOT INTEGRATED**. There are no Redis dependencies (such as `spring-boot-starter-data-redis` or `lettuce`/`jedis`) nor configurations.
- **Kafka Integration**: ❌ **NOT INTEGRATED**. There are no Kafka dependencies (such as `spring-kafka`) or consumer/producer configurations.

---

## 4. Root Cause Analysis: The SMTP Timeout Race Condition

The core bug causing emails to show as `FAILED` in the UI while successfully arriving in the inbox is a **classic network race condition** between the Java client and the Brevo SMTP server.

### Technical Breakdown

1. **Timeout Settings**:
   In `application.properties`, the SMTP write and read timeouts are set to **5 seconds**:
   ```properties
   spring.mail.properties.mail.smtp.connectiontimeout=5000
   spring.mail.properties.mail.smtp.timeout=5000
   spring.mail.properties.mail.smtp.writetimeout=5000
   ```

2. **The SMTP Handshake & Data Transfer**:
   When `SmtpEmailProvider.send(...)` is called:
   - A TCP connection is opened to `smtp-relay.brevo.com:587`.
   - TLS is initialized, authentication details are exchanged, and the message content is transmitted.
   - The Java client then blocks, waiting for the Brevo SMTP relay to respond with `250 OK` (indicating the message has been accepted and queued on the relay).

---

## 5. Modernization Recommendations

1. **Increase SMTP Read/Write Timeouts** from 5000ms to 15000ms.
2. **Parallelize Asynchronous Sending** using Virtual Threads.
3. **HTTP API Integration** for high-volume delivery.
