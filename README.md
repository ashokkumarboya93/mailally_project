<div align="center">

  # 🚀 MailAlly Enterprise
  ### Next-Generation SaaS Email Marketing & AI Delivery Platform

  [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
  [![React](https://img.shields.io/badge/React-19.0-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://react.dev/)
  [![Vite](https://img.shields.io/badge/Vite-6.x-646CFF?style=for-the-badge&logo=vite&logoColor=white)](https://vitejs.dev/)
  [![Tailwind CSS](https://img.shields.io/badge/Tailwind-3.x-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white)](https://tailwindcss.com/)
  [![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
  [![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)

  <p align="center">
    <b>Enterprise High-Volume Delivery</b> • <b>Real-Time Analytics</b> • <b>AI Subject & Copy Generation</b> • <b>Multi-Tenant Isolation</b>
  </p>

</div>

---

## 📸 Product Showcases & UI Previews

### 📊 Executive Analytics & Campaign Performance Dashboard
> Unified high-performance dashboard featuring real-time email open trends, delivery rates, active subscriber metrics, and campaign KPIs.

![MailAlly Executive Dashboard](images/dashboard_preview.png)

---

### ✍️ AI-Powered Email Campaign Builder & Editor
> Step-by-step campaign creation wizard with real-time responsive mobile/desktop previews, rich text HTML editor, and AI copy generation.

![MailAlly Campaign Builder](images/campaign_builder.png)

---

### 📈 Real-Time Delivery Stream & Audience Segmentation
> Granular subscriber engagement heatmaps, rule-based audience segment filters, bounce rate funnel analytics, and live activity feeds.

![MailAlly Analytics & Segmentation](images/analytics_preview.png)

---

## ⚡ Quick Start (1-Click Run)

To start both the **Spring Boot Backend** (Port `8081`) and **React Frontend** (Port `5173`) simultaneously:

### 💻 Windows Batch:
Double-click [start_app.bat](file:///d:/JDBCSW/MailAlly/mailally-backend/start_app.bat) or run in your terminal:
```cmd
start_app.bat
```

### ⚡ Windows PowerShell:
```powershell
.\start_app.ps1
```

---

## 🏗️ System Architecture & Workflow

```mermaid
flowchart TD
    subgraph ClientLayer ["Client Layer (React 19 + Vite)"]
        UI["Executive Dashboard & Campaign Builder"]
        WebSockets["Real-Time Event Listener"]
    end

    subgraph SecurityLayer ["Security & Multi-Tenant Isolation"]
        JWT["JWT Auth & Security Filter"]
        TenantContext["Multi-Tenant Context Resolver"]
    end

    subgraph ServiceLayer ["Spring Boot Core Services"]
        CampaignSvc["Campaign Engine"]
        AnalyticsSvc["Aggregation & Metrics Engine"]
        AISvc["Gemini / AI Copywriter Engine"]
        AudienceSvc["Segmentation & Contact Svc"]
    end

    subgraph DeliveryLayer ["Pluggable Email Adapters"]
        SMTP["SMTP Brevo Relay"]
        SES["AWS SES Adapter"]
        Brevo["Brevo API Gateway"]
    end

    UI --> JWT
    JWT --> TenantContext
    TenantContext --> ServiceLayer
    CampaignSvc --> DeliveryLayer
    DeliveryLayer --> AnalyticsSvc
    AnalyticsSvc --> WebSockets
    WebSockets --> UI
```

---

## ✨ Key Features & Capabilities

### 🔐 Enterprise Security & Governance
- **Stateless JWT Security**: Secure, stateless authentication with automated refresh token rotation.
- **Role-Based Access Control (RBAC)**: Fine-grained permissions supporting `SUPER_ADMIN`, `ADMIN`, `MARKETER`, and `VIEWER`.
- **Multi-Tenant Isolation**: Strict data segregation per organization at both database and service layers.

### 📧 High-Volume Email Engine
- **Multi-Provider Architecture**: Dynamic switching between **SMTP Relay**, **Brevo API**, and **AWS SES**.
- **Resilient Retry & Webhook Scheduler**: Built-in automated retries and dead-letter queues for unhandled webhook events.
- **Dynamic Personalization**: HTML/Rich-Text editor with dynamic merge tags (`{{firstName}}`, `{{company}}`, `{{unsubscribeUrl}}`).

### 🤖 AI Copywriter & Subject Line Optimizer
- **Smart Generation**: Generate high-converting subject lines and email copy based on target audience demographics.
- **Spam & Deliverability Scoring**: Real-time pre-flight analysis of email content to maximize inbox placement.

### 👥 Smart Audience Segmentation
- **Rule-Based Dynamic Filters**: Filter contacts by open history, link click frequency, geographic location, and custom tags.
- **Bulk Data Pipeline**: High-speed CSV/XLSX contact importer with built-in deduplication and email syntax validation.

---

## 📁 Repository Structure

```text
mailally-enterprise/
├── docs/                           # Architecture Specifications & Diagnostic Reports
│   ├── DEBUG_REPORT.md             # Email Socket Diagnostic & Socket Fixes
│   ├── PROJECT_ANALYSIS_AND_REVIEW.txt # Comprehensive System Review
│   ├── blueprint.md                # Product Requirements & Architecture Blueprint
│   ├── email_engine_analysis_report.md # Multi-channel Engine Specs
│   └── sample_data/                # Sample CSV / XLSX Contacts Datasets
│
├── images/                         # UI Mockups & Screenshots
│   ├── dashboard_preview.png       # Executive Dashboard Screenshot
│   ├── campaign_builder.png        # Email Builder & AI Assistant
│   └── analytics_preview.png       # Analytics & Segmentation Dashboard
│
├── start_app.bat                   # 1-Click Batch Launcher (Backend + Frontend)
├── start_app.ps1                   # 1-Click PowerShell Launcher
├── clean_project.bat               # Build Artifact Cleanup Script
├── push_to_github.bat              # Automated Git Commit & Push Assistant
│
├── mailally-backend/               # Spring Boot 3.x REST API & Domain Engine
│   ├── scripts/                    # Diagnostic & Test Utility Scripts
│   └── src/main/java/com/mailally/ # Domain Modules (Analytics, Email, Security)
│
└── mailally-frontend/              # React 19 + Vite Frontend SPA
    ├── src/                        # Pages, Components, Hooks & State Management
    ├── package.json
    └── vite.config.js
```

---

## 🌐 Endpoints & API Access

| Service | Access URL | Description |
| :--- | :--- | :--- |
| **Frontend Web App** | [http://localhost:5173](http://localhost:5173) | Single Page Application UI |
| **Backend REST API** | [http://localhost:8081](http://localhost:8081) | Core Spring Boot API Server |
| **Swagger API Docs** | [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) | Interactive REST API Documentation |

---

<div align="center">
  <sub>Built with ❤️ by MailAlly Engineering Team. Designed for high deliverability and scale.</sub>
</div>
