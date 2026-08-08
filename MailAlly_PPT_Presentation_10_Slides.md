# 📊 MailAlly Enterprise SaaS Platform — PowerPoint Presentation Master Content
> **Comprehensive 10-Slide Presentation Content Document**
> 
> *Note: This document contains deep, exhaustive technical, architectural, operational, and business matter for all 10 slides. You can easily extract, summarize, or paste bullet points directly into PowerPoint presentation slides.*

---

## 📌 Presentation Structure Overview

- **Slide 1**: Executive Summary & Platform Vision
- **Slide 2**: High-Level Architecture & Technology Stack
- **Slide 3**: Multi-Tenant Organization & Security Model (RBAC & Auth)
- **Slide 4**: High-Volume Email Dispatch Engine & Virtual Thread Worker Architecture
- **Slide 5**: Campaign Management Lifecycle & Automation Engine
- **Slide 6**: Audience Management, CSV Processing & Smart Segmentation
- **Slide 7**: AI Copywriter Engine & Dynamic Template Studio
- **Slide 8**: Executive Dashboard, Real-Time Telemetry & Analytics
- **Slide 9**: Multi-Tier Subscription, Usage Metering & Billing Governance
- **Slide 10**: Deployment Architecture, Reliability, Debugging & Future Roadmap

---

\newpage

# Slide 1: Executive Summary & Platform Vision

## 🎯 1. Executive Summary & Vision Statement
- **Product Name**: MailAlly Enterprise SaaS Email Marketing & Campaign Automation Platform
- **Core Vision**: To provide an enterprise-grade, multi-tenant SaaS email marketing platform capable of delivering high-volume campaigns, real-time analytics, automated audience segmentation, AI-powered content generation, and strict organization-level data isolation with high deliverability.
- **Target Audience**: Digital Marketing Agencies, SaaS Companies, Enterprise Marketing Teams, E-commerce Platforms, and Customer Success Teams requiring scalable campaign delivery.

---

## 🚨 2. The Problem Statement
- **Deliverability & Latency Issues**: Legacy systems struggle with connection timeouts, slow bulk dispatches, and lack of real-time progress visibility.
- **Data Isolation & Security Gaps**: Standard marketing tools often lack robust multi-tenant data boundaries, fine-grained Role-Based Access Control (RBAC), and enterprise-wide audit logging.
- **High Operational Costs**: Traditional email tools charge high markups on email sending infrastructure and lack flexible multi-provider failover options (SMTP, AWS SES, Brevo).
- **Manual Content & Segmentation Bottlenecks**: Marketers spend excessive hours manually writing copy, segmenting contacts, and testing email subject lines for spam filters.

---

##💡 3. Key Solution & Core Capabilities
- **Multi-Tenant SaaS Architecture**: Built from the ground up with workspace isolation, enabling organizations to manage their own campaigns, contacts, templates, and team members independently.
- **Pluggable Multi-Channel Email Engine**: Seamless integration across generic SMTP relays, AWS SES API, and Brevo HTTP APIs with automatic retry logic and connection pooling.
- **High-Throughput Concurrent Worker Infrastructure**: Powered by Java 21 Virtual Threads (Project Loom) and Redis Stream queues, achieving massive parallel dispatch with minimal memory footprint.
- **AI-Driven Campaign Acceleration**: Integrated AI Copywriter for subject line generation, email body drafting, campaign strategy ideation, and real-time spam score estimation.
- **Real-Time Telemetry & WebSocket Progress Tracking**: End-to-end progress reporting pushing live stats to front-end dashboards via STOMP WebSockets and Redis atomic meters.

---

## 🌟 4. Primary Differentiators & Value Proposition
- **Enterprise-Grade RBAC**: 11 distinct user roles across Platform Level, Organization Level, and Integration Level.
- **Zero Lock-In Provider Strategy**: Switch between AWS SES, Brevo, and custom SMTP relays without changing campaign setup.
- **Pre-Flight Deliverability Guardrails**: Built-in verification checking SPF/DKIM compliance, suppressed emails, and tenant quota limits prior to campaign release.
- **Modern Responsive UX**: Built with React 19, Vite, Tailwind CSS, and Recharts for an intuitive visual experience.

---

\newpage

# Slide 2: High-Level Architecture & Technology Stack

## 🏗️ 1. Architecture Overview
MailAlly follows a modern, decoupled, multi-tier micro-services ready architecture. It separates presentation layers, application processing, queue management, background asynchronous workers, and persistence databases.

```text
+-----------------------------------------------------------------------+
|                       React 19 Frontend (Vite)                       |
|           Tailwind CSS | Recharts | Axios JWT Interceptors            |
+-----------------------------------------------------------------------+
                                   | HTTP REST / WebSocket STOMP
                                   v
+-----------------------------------------------------------------------+
|                    Spring Boot 3.x API Gateway                        |
|        Spring Security | JWT Authorization | REST Controllers         |
+-----------------------------------------------------------------------+
                                   |
         +-------------------------+-------------------------+
         |                                                   |
         v                                                   v
+------------------------------------+   +----------------------------------+
|      Spring Data JPA / MySQL 8     |   |    Redis Stream Queue & Pub/Sub  |
|  (Orgs, Users, Campaigns, Logs)    |   |    (Pending Tasks & Atomic Stats) |
+------------------------------------+   +----------------------------------+
                                                             |
                                                             v
                                         +----------------------------------+
                                         | Virtual Thread Campaign Workers  |
                                         | (Java 21 Executors.newVirtual..) |
                                         +----------------------------------+
                                                             |
                                                             v
                                         +----------------------------------+
                                         | Multi-Provider Email Adapters    |
                                         | (SMTP Relay / AWS SES / Brevo)   |
                                         +----------------------------------+
```

---

## 💻 2. Full Technology Stack Matrix

| Architectural Layer | Technology / Framework | Key Purpose / Responsibilities |
| :--- | :--- | :--- |
| **Language & Runtime** | Java 21 LTS | Next-generation performance, Pattern Matching, Virtual Threads |
| **Backend Framework** | Spring Boot 3.x | Core framework, dependency injection, REST APIs, WebSockets |
| **Security Layer** | Spring Security + JWT | Stateless JWT authentication, BCrypt encryption, RBAC filters |
| **Data Access Layer** | Spring Data JPA / Hibernate | ORM mapping, transactional bounds, repository abstraction |
| **Database Engine** | MySQL 8.0 | Relational database storing multi-tenant orgs, users, campaigns |
| **Queue & Cache** | Redis 7.x (Streams / PubSub)| Distributed job queue, rate limiting counters, WebSocket sync |
| **Concurrency Model**| Java Virtual Threads (Loom) | Lightweight non-blocking worker threads for concurrent sending |
| **Frontend UI Framework**| React 19 + Vite 6.x | High-speed SPA client application rendering and routing |
| **Styling & Components**| Tailwind CSS + Lucide Icons| Modern responsive visual design system and icon library |
| **State & HTTP Client**| React Context API + Axios | Global auth state management and API request interceptors |
| **Data Visualization**| Recharts | Interactive graphs for open/click metrics and campaign progress |
| **API Protocol** | RESTful JSON & STOMP / WS | Synchronous CRUD operations and live streaming progress updates |

---

## ⚙️ 3. Component & Directory Architecture
- `mailally-backend`:
  - `com.mailally.auth`: JWT validation, login, registration, password recovery.
  - `com.mailally.organization`: Workspace management and tenant isolation rules.
  - `com.mailally.campaign`: Campaign creation, wizard, lifecycle state manager.
  - `com.mailally.email`: Multi-provider adapters, batch generator, virtual thread workers.
  - `com.mailally.contact`: CSV import engine, tag engine, suppression logic.
  - `com.mailally.segment`: Smart rule engine processing demographic/behavior filters.
  - `com.mailally.ai`: LLM integration service for subject/body generation and spam scoring.
  - `com.mailally.analytics`: Aggregation engine for open, click, bounce metrics.
  - `com.mailally.billing`: Subscription plan management, limit counters, invoices.
  - `com.mailally.audit`: Compliance logger for user actions.
- `mailally-frontend`:
  - `src/api`: Centralized API service modules with automatic JWT token refresh.
  - `src/components`: UI layouts, tables, modals, chart widgets, notifications.
  - `src/context`: Auth provider, theme context, global notifications.
  - `src/pages`: SaaS app views (Dashboard, Campaigns, Contacts, Segments, AI Copywriter, Analytics, Billing, Settings).

---

\newpage

# Slide 3: Multi-Tenant Organization & Security Model (RBAC & Auth)

## 🏢 1. Multi-Tenant Organization Isolation Model
MailAlly ensures complete data privacy and logical isolation between enterprise customer accounts:
- **Tenant Context Scoping**: Every database record (`Campaign`, `Contact`, `Segment`, `Template`, `AuditLog`) contains an `organization_id` foreign key.
- **Service-Level Filtering**: Every JPA query automatically applies tenant-scoping checks derived from the authenticated user's JWT security context (`CustomUserDetails`).
- **Data Protection**: Prevents cross-tenant data leaks and enforces strict boundary isolation at both API controller and persistence repository layers.

---

## 🔐 2. Authentication & JWT Token Architecture
- **Stateless Authentication**: Eliminates server-side session overhead using signed JSON Web Tokens (JWT).
- **Token Dual-Structure**:
  - **Access Token**: Short-lived (e.g., 15-60 mins) containing user claims, role, organization ID, and permissions.
  - **Refresh Token**: Long-lived (e.g., 7 days) stored securely to issue new access tokens without requiring re-login.
- **Password Hashing**: Industry-standard **BCrypt** password hashing algorithm with configurable salt strength.
- **Password Reset Flow**: Secure token generation via `/api/v1/auth/forgot-password` and `/api/v1/auth/reset-password`.

---

## 👥 3. Granular Role-Based Access Control (RBAC) Matrix

| User Role | Scope Level | Key Permissions & System Capabilities |
| :--- | :--- | :--- |
| **Super Admin** | Platform Level | Global system configuration, tenant lifecycle management, provider health, global analytics |
| **Support Executive** | Platform Level | Scoped, logged read-only support access into customer workspaces for issue diagnosis |
| **Auditor** | Platform / Org | Read-only access to audit trail logs, compliance histories, and security settings |
| **Organization Owner** | Organization | Full workspace ownership, billing & subscription management, admin management, workspace deletion |
| **Organization Admin** | Organization | Full operational control, user management, sending domain setup, API keys, campaigns |
| **Marketing Manager** | Organization | Strategy management, campaign approval, audience segment creation, analytics review |
| **Campaign Manager** | Organization | Day-to-day creation, editing, scheduling, launching, and monitoring of campaigns |
| **Marketing Executive** | Organization | Content drafting, template building, contact list cleaning; restricted from direct launches |
| **Billing Manager** | Organization | Management of subscription plans, payment methods, invoice downloads, usage meter tracking |
| **Read-Only Viewer** | Organization | Executive stakeholder access; view-only rights to dashboards, charts, and campaign reports |
| **Developer / API User**| Organization | Programmatic access via API keys to trigger transactional emails and query webhooks |

---

## 🛡️ 4. Enterprise Security Features
- **Comprehensive Audit Trail (`AuditLog`)**: Logs critical actions (e.g., campaign launched, user role changed, API key generated) with IP address, timestamp, actor ID, and organization ID.
- **CORS Configuration**: Restricts API calls to authorized frontend domains.
- **Request Validation**: Annotation-driven DTO validation (`@Valid`, `@NotNull`, `@Email`) catching bad payloads at the controller layer.

---

\newpage

# Slide 4: High-Volume Email Dispatch Engine & Virtual Thread Worker Architecture

## 🚀 1. Asynchronous Email Dispatch Architecture Flow
The MailAlly Email Engine is designed to handle high-volume dispatches asynchronously without blocking HTTP request threads.

```text
[1. HTTP Request] ---> EmailController.launchCampaign()
                            |
[2. Async Delegation] ---> EmailServiceImpl.launchCampaignAsync()
                            |
[3. Pre-Flight Validation]-> CampaignOrchestrator.launchCampaign()
                            | (Domain Checks, Quota Checks, State -> QUEUED)
                            v
[4. Chunking & DB Init] -> BatchGenerator.generateAndQueueBatches()
                            | (Creates CampaignBatch & Recipient Logs)
                            v
[5. Queue Ingestion] ----> Redis Stream ("campaign:queue:pending")
                            |
                            v
[6. Parallel Processing]-> CampaignWorkerService (Virtual Threads)
                            | (Renders Template, Calls Provider)
                            v
[7. Provider Dispatch] --> SmtpEmailProvider / AwsSesProvider / BrevoProvider
                            |
[8. DB & WS Sync] -------> ProgressSyncScheduler (Updates DB & pushes WebSocket)
```

---

## ⚡ 2. Java 21 Virtual Threads (Project Loom) Integration
- **Concurrency Bottleneck Solved**: Traditional platform threads are 1-to-1 tied to OS threads, limiting concurrent outbound SMTP socket connections.
- **Virtual Thread Worker Pool**: Utilizes `Executors.newVirtualThreadPerTaskExecutor()`, allowing tens of thousands of simultaneous worker tasks to run concurrently.
- **Non-Blocking I/O**: While one thread waits for remote SMTP network socket read responses, the JVM context switches to process another recipient batch instantly.

---

## 📬 3. Pluggable Multi-Provider Architecture
MailAlly abstracts email sending behind an `EmailProvider` interface:
- **`SmtpEmailProvider`**: Custom SMTP relay engine supporting TLS, authentication, and custom headers.
- **`AwsSesEmailProvider`**: High-volume cloud sending via AWS SES V2 SDK.
- **`BrevoEmailProvider`**: HTTP REST API adapter for Brevo (Sendinblue) transactional/marketing emails.
- **Dynamic Provider Selection**: Allows fallback switching if a primary provider experiences downtime or rate limits.

---

## 🔍 4. SMTP Reliability, Connection Timeout & Self-Healing Fix
During debug diagnostics, a crucial architectural bug was investigated and resolved:
- **Symptom**: Telemetry reported 113/113 emails as FAILED on the dashboard, while recipients actually received the emails in their inboxes.
- **Root Cause Analysis**: The remote SMTP server (`smtp-relay.brevo.com`) accepted the email payload and sent `250 OK`, but took **5,420ms** to return the final network response line. JavaMailSender had a hardcoded read timeout of **5,000ms**, triggering a `SocketTimeoutException` on the client side just **420ms before** the server confirmation arrived.
- **Resolution Implemented**:
  1. Adjusted Socket Read Timeout (`mail.smtp.timeout`) from 5,000ms to **15,000ms**.
  2. Implemented socket connection pooling (`mail.smtp.connectiontimeout = 10000ms`).
  3. Added idempotent response handling in `CampaignWorkerService` to verify delivery state before marking a batch record as failed.

---

## 🔄 5. Real-Time Telemetry & STOMP WebSocket Broadcasting
- **Redis Atomic Meters**: `CampaignWorkerService` increments Redis atomic keys (`campaign:{id}:sent`, `campaign:{id}:failed`) instantly upon each email completion.
- **ProgressSyncScheduler**: Runs a background poll every 5 seconds, flushing Redis counters to MySQL DB in bulk and broadcasting live STOMP JSON payloads over `/topic/campaigns/{id}/progress`.

---

\newpage

# Slide 5: Campaign Management Lifecycle & Automation Engine

## 🔄 1. Campaign State Machine & Lifecycle
Every campaign follows a strictly monitored state machine to ensure execution safety and data integrity:

```text
  [ DRAFT ]  <--->  [ SCHEDULED ]
      |                  |
      +--------+---------+
               |
               v
        [ VALIDATING ]  (Pre-flight SPF/DKIM/Quota checks)
               |
               v
        [ PREPARING ]   (Generating batches & recipient logs)
               |
               v
          [ QUEUED ]    (Pushed to Redis Stream)
               |
               v
        [ PROCESSING ]  (Virtual Threads dispatching)
               |
      +--------+--------+
      |                 |
      v                 v
[ COMPLETED ]      [ FAILED / PAUSED ]
                        |
                        v
                 (Resume / Retry)
```

---

## ✈️ 2. Campaign Setup Wizard Workflow (4-Step UI Flow)
1. **Step 1: Campaign Details**: Name, Subject Line, Preview Text, Sender Name, Sender Email.
2. **Step 2: Audience Selection**: Choose target Contact Lists or Dynamic Smart Segments; choose exclusion segments.
3. **Step 3: Content Design**: Select email template from gallery, edit content via Rich Text / HTML editor, insert personal placeholders.
4. **Step 4: Review & Schedule**: Verify campaign parameters, run pre-flight deliverability check, send immediate release or schedule for future timestamp.

---

## 🔍 3. Pre-Flight Deliverability Guardrails Engine
Before any campaign enters the queue, `CampaignOrchestrator` runs an automated inspection:
- **Domain Signature Verification**: Verifies SPF (Sender Policy Framework), DKIM (DomainKeys Identified Mail), and DMARC record status for sending domains.
- **Suppression List Filtering**: Automatically filters out unsubscribed, bounced, or complained email addresses.
- **Quota Governance**: Verifies if the organization's monthly email sending limit has sufficient capacity.

---

## 🎯 4. Personalization & Merge Tags Engine
- **Dynamic Merging**: Evaluates email templates at runtime for each individual recipient.
- **Supported Merge Tags**:
  - `{{firstName}}` / `{{lastName}}`: Recipient personal names.
  - `{{email}}`: Recipient email address.
  - `{{company}}`: Custom metadata company attribute.
  - `{{unsubscribeUrl}}`: Automatically generated secure 1-click unsubscribe link.
- **Fallback Values**: Supports default fallback strings (e.g., `{{firstName | "Valued Customer"}}`).

---

## 🎛️ 5. Live Campaign Control & Telemetry Panel
- **Real-Time Monitoring**: Marketers can view live dispatch progress bars (Percentage, Sent, Failed, Remaining).
- **Execution Controls**:
  - **Pause Campaign**: Instantly halts queue consuming for the specific campaign ID.
  - **Resume Campaign**: Re-engages Redis stream worker processing.
  - **Retry Failed**: Re-enqueues only recipient logs marked with `FAILED` status due to temporary network timeouts.

---

\newpage

# Slide 6: Audience Management, CSV Processing & Smart Segmentation

## 👥 1. Contact Management & Data Lifecycle
MailAlly stores rich contact metadata to enable targeted marketing campaigns:
- **Contact Fields**: Email Address, First Name, Last Name, Phone Number, Company, Opt-in Status (`SUBSCRIBED`, `UNSUBSCRIBED`, `BOUNCED`, `COMPLAINED`), Created At, Last Engaged At.
- **Contact Tagging**: Flexible multi-tag association (e.g., `VIP`, `Lead-2026`, `Newsletter`, `Churned`) for quick categorizing.
- **Contact Activity History**: Log of all campaign interactions per recipient (Sent, Opened, Clicked, Bounced).

---

## ⚡ 2. High-Throughput Bulk CSV Import & Export Engine
- **Asynchronous Chunked Parsing**: Uses Spring Batch / Jackson CSV parsing to process large CSV files (100,000+ contacts) without UI freezing.
- **Field Mapping Interface**: Interactive UI allows users to map custom CSV column headers to MailAlly contact fields dynamically.
- **Validation & Duplicate Handling**:
  - Checks email format regex validity.
  - Automatically deduplicates records against existing database contacts per organization.
  - Generates detailed import summary reports (Total Processed, Inserted, Updated, Invalid Skipped).

---

## 🧠 3. Rule-Based Dynamic Smart Segmentation Engine
Unlike static contact lists, **Smart Segments** automatically update target audiences in real time using logical rule combinations:

```text
                   +----------------------------------+
                   |     Smart Segment Ruleset        |
                   +----------------------------------+
                                    |
          +-------------------------+-------------------------+
          | AND                                               | AND
          v                                                   v
+------------------------------------+             +----------------------------------+
| Demographic Rule:                  |             | Behavioral Engagement Rule:      |
| Country EQUALS "United States"     |             | Email Opens GREATER THAN 3       |
| AND Tag CONTAINS "SaaS-Customer"   |             | In Last 30 Days                   |
+------------------------------------+             +----------------------------------+
```

---

## 🛠️ 4. Supported Segmentation Rules & Operators

| Category | Available Field Attributes | Supported Filter Operators |
| :--- | :--- | :--- |
| **Demographics** | First Name, Last Name, Email Domain, Company, Created Date | `EQUALS`, `NOT_EQUALS`, `CONTAINS`, `STARTS_WITH`, `IS_EMPTY` |
| **Contact Metadata**| Tags, Custom Properties, Opt-In Status | `IN_LIST`, `NOT_IN_LIST`, `HAS_TAG`, `DOES_NOT_HAVE_TAG` |
| **Engagement** | Open Count, Click Count, Last Opened Date, Last Clicked Date| `GREATER_THAN`, `LESS_THAN`, `BETWEEN`, `IN_LAST_X_DAYS` |
| **Campaigns** | Received Specific Campaign, Clicked Link in Campaign | `RECEIVED_CAMPAIGN`, `CLICKED_LINK`, `DID_NOT_OPEN` |

---

## 🚫 5. Suppression & Compliance Management
- **Automatic Unsubscribe Enforcement**: Every email dispatch checks the central `suppression_list` table.
- **One-Click Unsubscribe Header**: Includes RFC-8058 `List-Unsubscribe` headers to comply with Google & Yahoo 2024 deliverability guidelines.
- **GDPR & CAN-SPAM Ready**: Supports complete contact erasure ("Right to be Forgotten") and full audit tracking of consent history.

---

\newpage

# Slide 7: AI Copywriter Engine & Dynamic Template Studio

## 🤖 1. Provider-Agnostic AI Integration Architecture
MailAlly features a built-in **AI Copywriter Engine** (`AiService`) designed to accelerate campaign creation:

```text
[ Frontend Request: "Write cold email for HR software" ]
                           |
                           v
              +--------------------------+
              |     AiController.java    |
              +--------------------------+
                           |
                           v
              +--------------------------+
              |   AiCopywriterService    |
              +--------------------------+
                           |
         +-----------------+-----------------+
         | (If Key Configured)              | (Fallback / Default)
         v                                   v
+------------------+               +-------------------+
| OpenAI / Claude  |               | MockAiProvider    |
| API Adapter      |               | Rule Engine       |
+------------------+               +-------------------+
```

---

## ✨ 2. AI Copywriter Feature Capabilities
1. **AI Subject Line Generator**: Generates 5 compelling, click-optimized subject lines based on target topic, audience tone, and industry context.
2. **AI Email Content Drafter**: Writes complete HTML/rich-text email body copy tailored to selected tones (*Professional, Urgency, Friendly, Persuasive, Promotional*).
3. **AI Spam Score Estimator**: Analyzes subject lines and body copy for spam trigger words (e.g., *"FREE Money", "100% Guaranteed", excessive caps*), producing a score (0-100) and actionable improvement suggestions.
4. **Campaign Idea Generator**: Suggests target segment ideas and campaign themes based on seasonal events or business goals.

---

## 🎨 3. Dynamic Email Template Studio
- **Dual Editing Modes**:
  - **Rich Text / Visual Editor**: Intuitive WYSIWYG editor for non-technical marketers.
  - **HTML Code Editor**: Raw HTML code editor with syntax highlighting for custom developer templates.
- **Template Variable Insertion**: Built-in toolbar buttons to quickly insert merge tags (`{{firstName}}`, `{{company}}`, `{{unsubscribeUrl}}`).
- **Template Gallery & Categorization**: Save templates into organization categories (*Welcome Series, Monthly Newsletter, Promotional, Re-engagement, Transactional*).

---

## 📱 4. Responsive Preview & Multi-Device Rendering
- **Real-Time Live Preview**: Instant visual preview directly inside the template editor.
- **Device Viewport Toggle**: Switch between Desktop (768px+), Tablet (600px), and Mobile (320px) viewport frames.
- **Test Email Dispatcher**: Send immediate single test emails to internal team inbox addresses to verify inbox layout formatting before launching.

---

\newpage

# Slide 8: Executive Dashboard, Real-Time Telemetry & Analytics

## 📊 1. Executive Overview Dashboard
The Executive Dashboard provides C-level executives and marketing leads with an aggregated high-level view of platform performance:
- **Key Performance Indicators (KPI Cards)**:
  - **Total Campaigns**: Count of active, completed, and scheduled campaigns.
  - **Total Emails Sent**: Lifetime and monthly email dispatch counters.
  - **Average Open Rate (%)**: Aggregated percentage of unique email opens.
  - **Average Click Rate (%)**: Aggregated percentage of recipient link clicks.
  - **Deliverability Rate (%)**: Percentage of successfully delivered emails vs. bounces.
- **Recent Campaign Activity Feed**: Status table of recently launched campaigns with live progress badges.

---

## 📈 2. Interactive Telemetry Visualizations (Recharts)
The frontend integrates **Recharts** to render dynamic data visual charts:
- **Send Volume vs. Engagement Trend (Area Chart)**: Daily email sending volumes plotted against open and click rates over selectable date ranges (7 Days, 30 Days, 90 Days).
- **Engagement Distribution (Pie Chart)**: Visual breakdown of Delivered, Unopened, Clicked, Bounced, and Unsubscribed recipients.
- **Hourly Open Rate Heatmap (Bar Chart)**: Identifies optimal send-time windows based on when subscribers engage most.

---

## 🔬 3. Deep Campaign Analytics & Recipient Telemetry
Detailed analytics page for individual campaign inspection:

| Metric Category | Data Tracked | Business Value / Insight |
| :--- | :--- | :--- |
| **Delivery Stats** | Sent, Delivered, Soft Bounces, Hard Bounces | Measures server deliverability & inbox acceptance |
| **Engagement Stats** | Unique Opens, Total Opens, Unique Clicks, Total Clicks | Evaluates subject line quality & content interest |
| **Conversion Metrics**| Click-Through-Rate (CTR), Click-to-Open-Rate (CTOR) | Measures copy effectiveness and call-to-action relevance |
| **Compliance Metrics**| Unsubscribe Count, Spam Complaint Count | Ensures list hygiene and domain reputation protection |

---

## 🧾 4. Recipient Audit Logs (`campaign_recipient_logs`)
Every single recipient email transaction stores exhaustive audit telemetry:
- `recipient_email`: Target address.
- `status`: `SENT`, `FAILED`, `BOUNCED`, `OPENED`, `CLICKED`.
- `smtp_response_code`: Raw server response code (e.g., `250 2.0.0 OK queued`).
- `delivery_duration_ms`: Time taken in milliseconds for provider connection and dispatch.
- `opened_at` / `clicked_at`: Exact ISO timestamps of subscriber engagement.

---

\newpage

# Slide 9: Multi-Tier Subscription, Usage Metering & Billing Governance

## 💳 1. Multi-Tier Subscription Architecture
MailAlly incorporates a flexible SaaS billing system with 5 distinct subscription tiers:

```text
+-----------------+-----------------+-----------------+-----------------+-----------------+
|      FREE       |     STARTER     |       PRO       |    BUSINESS     |   ENTERPRISE    |
| 1,000 emails/mo | 25,000 emails/mo|100,000 emails/mo|500,000 emails/mo| Unlimited       |
| 500 contacts    | 5,000 contacts  | 25,000 contacts |100,000 contacts | Custom          |
| 1 User Seat     | 3 User Seats    | 10 User Seats   | 25 User Seats   | Unlimited Seats |
| SMTP Only       | SMTP + Brevo    | All Providers   | All + AI Copilot| Dedicated Infra |
+-----------------+-----------------+-----------------+-----------------+-----------------+
```

---

## 📏 2. Real-Time Usage Metering & Quota Enforcement
- **Atomic Usage Tracking**: Tracks usage dynamically in Redis and MySQL.
- **Quota Enforcers**:
  - **Email Send Limit**: Checked by `CampaignOrchestrator` before starting a campaign.
  - **Contact Storage Limit**: Checked during CSV imports or single contact creation.
  - **User Seat Limit**: Checked when sending team member invitations.
- **Grace Period & Warning Alerts**: System generates warning notifications when an organization reaches **80%** and **95%** of its monthly tier allowance.

---

## 🚪 3. Dynamic Feature Gating System
- **Annotation-Based Enforcement**: Services use annotations/guards (e.g., `@RequiresFeature("AI_COPYWRITER")`) to grant or restrict access.
- **Feature Matrix**:
  - `FREE` & `STARTER`: Standard templates, Basic CSV Import, SMTP Provider.
  - `PRO`: Smart Segments, AWS SES Adapter, Recharts Advanced Analytics.
  - `BUSINESS` & `ENTERPRISE`: Full AI Copywriter, Custom Webhooks, Priority Queueing, Multi-Domain Support.

---

## 🧾 4. Invoice History & Commercial Management
- **Billing Manager Dashboard**: Dedicated UI for viewing subscription tier details, renewal dates, and payment status.
- **Transaction Log**: Itemized billing history storing past invoices, amounts paid, currency, and download PDF receipts.
- **Payment Processor Integration**: Ready for Stripe / Razorpay Webhook integration to handle recurring monthly subscriptions and failed payment handling.

---

\newpage

# Slide 10: Deployment Architecture, Reliability, Debugging & Future Roadmap

## ⚙️ 1. Deployment Architecture & Operations
- **Environment Configuration**: Centralized `application.properties` and environment variable overrides (`SPRING_DATASOURCE_URL`, `REDIS_HOST`, `SMTP_PASSWORD`).
- **Build & Launch Scripts**:
  - `mvnw clean package`: Builds executable Spring Boot JAR.
  - `start_app.bat` / `start_app.ps1`: One-click startup scripts for backend and frontend services.
  - `push_to_github.bat`: Automated repository staging and version control push script.
- **Containerization Ready**: Fully compatible with Docker containers and Kubernetes orchestrations.

---

## 🛠️ 2. Logging, Monitoring & Operational Diagnostics
- **Spring Boot Actuator**: Health endpoints (`/actuator/health`, `/actuator/metrics`) exposing system metrics, DB connection pool health, and disk space.
- **Structured Error Handling**: `GlobalExceptionHandler` converts uncaught backend exceptions into clean, standard REST JSON error payloads (`timestamp`, `status`, `error`, `message`, `path`).
- **Comprehensive Debug Reporting**: Diagnostic scripts (`gate1-smtp-test-v5.ps1`) to simulate socket latency, verify TLS handshakes, and debug provider transport logs.

---

## 🗺️ 3. Strategic Product Roadmap & Future Scope

```text
[ Phase 1: Core Foundation ] ---> [ Phase 2: AI & Deliverability ] ---> [ Phase 3: Advanced Enterprise ]
- Multi-Tenant Engine           - AI Copywriter Copilot               - Visual Drag & Drop Workflow Builder
- Virtual Thread Worker Pool    - Dynamic Smart Segments              - A/B Subject & Content Testing
- SMTP / AWS SES / Brevo        - STOMP WebSocket Telemetry           - Send-Time Optimization Engine
- React 19 / Tailwind UI        - Socket Timeout Self-Healing         - Custom Webhooks & Zapier App
```

- **A/B Testing Engine**: Automatic split-testing of subject lines and email copy, auto-routing remaining batches to the winning variant.
- **Visual Drag-and-Drop Workflow Builder**: Multi-step automated drip sequences with delay timers and conditional branching.
- **Predictive Send-Time Optimization (STO)**: Machine learning model analyzing past open times per contact to deliver emails at their peak engagement hour.

---

## 🏁 Summary Conclusion
MailAlly Enterprise stands as a robust, high-performance, and secure SaaS platform designed for modern email campaign management. By leveraging **Java 21 Virtual Threads**, **Redis Distributed Queues**, **Multi-Tenant Isolation**, and **AI Integration**, it achieves exceptional deliverability, scalability, and operational efficiency.
