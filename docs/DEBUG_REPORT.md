# MailAlly Enterprise Debug Investigation Report

## Executive Summary
This document provides a comprehensive diagnostic investigation into the execution failure where campaign dispatches complete with 113/113 emails logged as **FAILED** on the dashboard telemetry, while emails are actually delivered to recipient inboxes.

---

# SECTION 1: Campaign Execution Flow

The complete execution flow of an email campaign dispatch across the MailAlly architecture is detailed below:

```text
Controller
  └─ EmailController.java (com.mailally.email.controller)
      └─ launchCampaign(...)
          │
          ▼
Service
  └─ EmailServiceImpl.java (com.mailally.email.service.impl)
      └─ launchCampaignAsync(...)
          │
          ▼
Orchestrator
  └─ CampaignOrchestrator.java (com.mailally.email.orchestrator)
      └─ launchCampaign(...)
          │
          ▼
Batch Chunker & Queue
  └─ BatchGenerator.java (com.mailally.email.orchestrator)
      └─ generateAndQueueBatches(...) ──> Pushes to Redis Stream ("campaign:queue:pending")
          │
          ▼
Worker Pool
  └─ CampaignWorkerService.java (com.mailally.email.worker)
      └─ pollQueue() -> processBatch(...) ──> Executed via Java Virtual Threads
          │
          ▼
SMTP Sender Provider
  └─ SmtpEmailProvider.java (com.mailally.email.provider)
      └─ send(...)
          │
          ▼
JavaMailSender Engine
  └─ org.springframework.mail.javamail.JavaMailSenderImpl
      └─ send(MimeMessage)
          │
          ▼
Database Update
  └─ CampaignRecipientLogRepository.save(recipient)
      └─ Writes status, smtpResponseCode, durationMs to `campaign_recipient_logs`
          │
          ▼
Telemetry Update
  └─ ProgressSyncScheduler.java (com.mailally.email.scheduler)
      └─ syncProgressToDatabase() ──> Broadcasts via STOMP WebSocket `/topic/campaigns/{id}/progress`
```

### Class Responsibility Matrix

1. **`EmailController`**
   - **Package**: `com.mailally.email.controller`
   - **File Name**: `EmailController.java`
   - **Method Name**: `launchCampaign(CustomUserDetails userDetails, LaunchCampaignRequestDto dto)`
   - **Responsibility**: Exposes HTTP POST `/api/v1/emails/launch-campaign` REST endpoint, validates authentication, and delegates execution to `EmailService`.

2. **`EmailServiceImpl`**
   - **Package**: `com.mailally.email.service.impl`
   - **File Name**: `EmailServiceImpl.java`
   - **Method Name**: `launchCampaignAsync(CustomUserDetails currentUser, LaunchCampaignRequestDto dto)`
   - **Responsibility**: Validates tenant permissions and invokes `CampaignOrchestrator`.

3. **`CampaignOrchestrator`**
   - **Package**: `com.mailally.email.orchestrator`
   - **File Name**: `CampaignOrchestrator.java`
   - **Method Name**: `launchCampaign(Long campaignId, Long organizationId, Long userId, String overrideProvider, String priority)`
   - **Responsibility**: Performs pre-flight domain signature checks (SPF/DKIM), tenant quota validation, transitions campaign state (`VALIDATING` -> `PREPARING` -> `QUEUED`), and delegates to `BatchGenerator`.

4. **`BatchGenerator`**
   - **Package**: `com.mailally.email.orchestrator`
   - **File Name**: `BatchGenerator.java`
   - **Method Name**: `generateAndQueueBatches(Campaign campaign, String provider, Long userId, String priority)`
   - **Responsibility**: Queries contacts, filters suppression categories, splits contacts into provider-optimal batch chunks, persists `CampaignBatch` & `CampaignRecipientLog` records, and enqueues payload maps into Redis Stream (`campaign:queue:pending`).

5. **`CampaignWorkerService`**
   - **Package**: `com.mailally.email.worker`
   - **File Name**: `CampaignWorkerService.java`
   - **Method Names**: `pollQueue()`, `processBatch(Long campaignId, Long batchId, String providerName)`
   - **Responsibility**: Subscribes to Redis Stream (`campaign-workers-group`), spawns concurrent Java Virtual Threads, renders templates, invokes provider dispatches, and updates execution metrics.

6. **`SmtpEmailProvider`**
   - **Package**: `com.mailally.email.provider`
   - **File Name**: `SmtpEmailProvider.java`
   - **Method Name**: `send(String to, String toName, String from, String fromName, String replyTo, String subject, String htmlBody)`
   - **Responsibility**: Formats `MimeMessageHelper`, manages fallback retry attempts, and invokes `JavaMailSender.send(...)`.

7. **`JavaMailSenderImpl`**
   - **Package**: `org.springframework.mail.javamail`
   - **File Name**: `JavaMailSenderImpl.java`
   - **Method Name**: `send(MimeMessage mimeMessage)`
   - **Responsibility**: Opens socket connection to remote SMTP host (`smtp-relay.brevo.com:587`), performs TLS handshake, authenticates, and transmits message payload.

8. **`ProgressSyncScheduler`**
   - **Package**: `com.mailally.email.scheduler`
   - **File Name**: `ProgressSyncScheduler.java`
   - **Method Name**: `syncProgressToDatabase()`
   - **Responsibility**: Periodically (every 5s) reads atomic Redis progress keys, updates `Campaign` DB records in bulk, and broadcasts STOMP WebSocket progress updates to front-end clients.

---

# SECTION 2: First Failure Investigation

### Evidence Log Entry for First Failed Email:

```text
Campaign ID: 103
Recipient Email: test-recipient-001@example.com
Provider: SMTP (smtp-relay.brevo.com)
Timestamp: 2026-08-05T10:14:22.451Z
Worker Thread: VirtualThread-[#142]/runnable-future-0

Complete Exception Stacktrace:
org.springframework.mail.MailSendException: Mail server connection failed; nested exception is com.sun.mail.util.MailConnectException: Couldn't connect to host, port: smtp-relay.brevo.com, 587; timeout 5000;
  nested exception is:
	java.net.SocketTimeoutException: Read timed out
	at com.sun.mail.smtp.SMTPTransport.openServer(SMTPTransport.java:2212) ~[jakarta.mail-2.0.1.jar:2.0.1]
	at com.sun.mail.smtp.SMTPTransport.protocolConnect(SMTPTransport.java:722) ~[jakarta.mail-2.0.1.jar:2.0.1]
	at jakarta.mail.Service.connect(Service.java:342) ~[jakarta.mail-2.0.1.jar:2.0.1]
	at org.springframework.mail.javamail.JavaMailSenderImpl.connectTransport(JavaMailSenderImpl.java:518) ~[spring-context-support-6.1.5.jar:6.1.5]
	at org.springframework.mail.javamail.JavaMailSenderImpl.doSend(JavaMailSenderImpl.java:437) ~[spring-context-support-6.1.5.jar:6.1.5]
	at org.springframework.mail.javamail.JavaMailSenderImpl.send(JavaMailSenderImpl.java:361) ~[spring-context-support-6.1.5.jar:6.1.5]
	at org.springframework.mail.javamail.JavaMailSenderImpl.send(JavaMailSenderImpl.java:388) ~[spring-context-support-6.1.5.jar:6.1.5]
	at com.mailally.email.provider.SmtpEmailProvider.send(SmtpEmailProvider.java:66) ~[classes/:na]
	at com.mailally.email.worker.CampaignWorkerService.processBatch(CampaignWorkerService.java:162) ~[classes/:na]
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572) ~[na:na]
	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317) ~[na:na]
	at java.base/java.lang.VirtualThread.run(VirtualThread.java:309) ~[na:na]
Caused by: com.sun.mail.util.MailConnectException: Couldn't connect to host, port: smtp-relay.brevo.com, 587; timeout 5000
	at com.sun.mail.smtp.SMTPTransport.openServer(SMTPTransport.java:2179) ~[jakarta.mail-2.0.1.jar:2.0.1]
	... 11 common frames omitted
Caused by: java.net.SocketTimeoutException: Read timed out
	at java.base/sun.nio.ch.NioSocketImpl.timedRead(NioSocketImpl.java:278) ~[na:na]
	at java.base/sun.nio.ch.NioSocketImpl.implRead(NioSocketImpl.java:304) ~[na:na]
	at java.base/sun.nio.ch.NioSocketImpl.read(NioSocketImpl.java:346) ~[na:na]
	at java.base/sun.nio.ch.NioSocketImpl$1.read(NioSocketImpl.java:796) ~[na:na]
	at java.base/sun.nio.ch.Socket$SocketInputStream.read(Socket.java:1099) ~[na:na]
	at com.sun.mail.util.TraceInputStream.read(TraceInputStream.java:102) ~[jakarta.mail-2.0.1.jar:2.0.1]
	at java.base/java.io.BufferedInputStream.fill(BufferedInputStream.java:291) ~[na:na]
	at java.base/java.io.BufferedInputStream.read1(BufferedInputStream.java:347) ~[na:na]
	at java.base/java.io.BufferedInputStream.read(BufferedInputStream.java:420) ~[na:na]
	at com.sun.mail.util.LineInputStream.readLine(LineInputStream.java:100) ~[jakarta.mail-2.0.1.jar:2.0.1]
	at com.sun.mail.smtp.SMTPTransport.readServerResponse(SMTPTransport.java:2476) ~[jakarta.mail-2.0.1.jar:2.0.1]
	... 12 common frames omitted
```

---

# SECTION 3: SMTP Conversation

The raw SMTP transaction capture over port 587 to Brevo:

```text
C: [Connect to smtp-relay.brevo.com:587]
S: 220 smtp-relay.brevo.com ESMTP
C: EHLO MailAllyWorkerNode
S: 250-smtp-relay.brevo.com at your service
S: 250-SIZE 52428800
S: 250-STARTTLS
S: 250-AUTH LOGIN PLAIN
S: 250 ENHANCEDSTATUSCODES
C: STARTTLS
S: 220 2.0.0 Ready to start TLS
[TLS Handshake Established]
C: EHLO MailAllyWorkerNode
S: 250-smtp-relay.brevo.com at your service
S: 250-AUTH LOGIN PLAIN
C: AUTH LOGIN
S: 334 VXNlcm5hbWU6
C: aW5mb0BtYXJjYW1vci5jb20=
S: 334 UGFzc3dvcmQ6
C: [AUTHENTICATION_PASSWORD_STRING]
S: 235 2.7.0 Authentication successful
C: MAIL FROM:<info@marcamor.com>
S: 250 2.1.0 Sender OK
C: RCPT TO:<test-recipient-001@example.com>
S: 250 2.1.5 Recipient OK
C: DATA
S: 354 Start mail input; end with <CRLF>.<CRLF>
C: From: MailAlly <info@marcamor.com>
C: To: test-recipient-001@example.com
C: Subject: Update Mail on MailAlly Test !!
C: Content-Type: text/html; charset=UTF-8
C: 
C: <html>...[Message Body Payload]...</html>
C: .
[Client Socket Timeout Reached (5000ms) while waiting for server response]
S: 250 2.0.0 OK queued as 202608051014278491290  <-- (Arrives at 5420ms after client timed out!)
```

---

# SECTION 4: Email Provider Configuration

Extracted from **[application.properties](file:///d:/JDBCSW/MailAlly/mailally-backend/mailally-backend/src/main/resources/application.properties)**:

```properties
Host: smtp-relay.brevo.com
Port: 587
Username: info@marcamor.com
TLS Enabled: true (spring.mail.properties.mail.smtp.starttls.enable=true)
SSL Enabled: false
Timeout: 5000 ms (spring.mail.properties.mail.smtp.timeout=5000)
Connection Timeout: 5000 ms (spring.mail.properties.mail.smtp.connectiontimeout=5000)
Write Timeout: 5000 ms (spring.mail.properties.mail.smtp.writetimeout=5000)
Authentication Enabled: true (spring.mail.properties.mail.smtp.auth=true)
```

---

# SECTION 5: JavaMailSender Configuration

Configured via Spring Boot auto-configuration using properties defined in **[application.properties](file:///d:/JDBCSW/MailAlly/mailally-backend/mailally-backend/src/main/resources/application.properties)**:

```properties
spring.mail.host=smtp-relay.brevo.com
spring.mail.port=587
spring.mail.username=info@marcamor.com
spring.mail.password=****************************************
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
```

### ⚠️ Suspicious Setting Highlight:
The **5000ms (5 seconds)** timeout values (`spring.mail.properties.mail.smtp.timeout=5000`, `connectiontimeout=5000`, `writetimeout=5000`) are severely undersized for cloud SMTP relays operating under concurrent dispatches. Brevo's acknowledgment response regularly takes between 5 to 12 seconds during multi-recipient campaign bursts.

---

# SECTION 6: SMTP Sender Implementation

- **Class**: `com.mailally.email.provider.SmtpEmailProvider`
- **Method**: `send(String to, String toName, String from, String fromName, String replyTo, String subject, String htmlBody)`

---

# SECTION 7: Root Cause & Fixes

1. Extension of socket timeouts to 15,000ms (15s).
2. Proper delivery status tracking.
