# 🚀 MailAlly Enterprise - SaaS Email Marketing Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19.0-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://react.dev/)
[![Vite](https://img.shields.io/badge/Vite-6.x-646CFF?style=for-the-badge&logo=vite&logoColor=white)](https://vitejs.dev/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind-3.x-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white)](https://tailwindcss.com/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)

**MailAlly Enterprise** is a high-performance, enterprise-grade SaaS Email Marketing Platform designed to deliver high-volume email campaigns, real-time analytics, automated audience segmentation, multi-tenant organization isolation, and AI-powered content generation.

---

## ⚡ Quick Start (1-Click Run)

To start both the Spring Boot Backend (Port `8081`) and React Frontend (Port `5173`) with one click:

### Windows Batch:
Double-click [start_app.bat](file:///d:/JDBCSW/MailAlly/mailally-backend/start_app.bat) or run in terminal:
```cmd
start_app.bat
```

### Windows PowerShell:
```powershell
.\start_app.ps1
```

---

## ✨ Features & Core Modules

MailAlly is built as an end-to-end multi-tenant platform with 18 fully integrated modules:

### 🔑 Authentication & Security
- **JWT Authentication**: Stateless, secure token-based authentication with refresh token support.
- **RBAC (Role-Based Access Control)**: Granular permissions for `SUPER_ADMIN`, `ADMIN`, `MARKETER`, and `VIEWER`.
- **Multi-Tenant Isolation**: Strict database-level and service-level data partitioning per organization.

### 📊 Dashboard & Real-Time Analytics
- **Executive Dashboard**: Unified overview of campaigns, open rates, delivery performance, and active subscribers.
- **Advanced Analytics**: Detailed metrics on opens, clicks, unsubscribes, bounces, and interactive visual charts (powered by Recharts).

### 📧 Campaign & Email Engine
- **Multi-Channel Email Engine**: Pluggable provider support including SMTP, AWS SES, and Brevo adapters.
- **Campaign Wizard**: Step-by-step creation flow for standard, recurring, and automated drip campaigns.
- **Dynamic Template Editor**: HTML and rich text template management with variable insertion (`{{firstName}}`, `{{company}}`).

### 👥 Audience Management & AI Copywriting
- **Contact Management**: Bulk CSV import/export, tagging, and contact metadata tracking.
- **Smart Segmentation**: Real-time rule-based segment filtering based on engagement history and attributes.
- **AI Copywriter**: Provider-agnostic AI integration (Mock / OpenAI / Claude) for generating subject lines, body content, spam score analysis, and campaign ideas.

---

## 📁 Clean Repository Structure

```text
mailally-enterprise/
├── docs/                           # Project Documentation & Architecture Reports
│   ├── DEBUG_REPORT.md             # Email Delivery & Socket Diagnostic Report
│   ├── PROJECT_ANALYSIS_AND_REVIEW.txt # Architectural Review
│   ├── blueprint.md                # Enterprise Product Specification
│   ├── email_engine_analysis_report.md # Engine Analysis & Timeout Fixes
│   └── sample_data/                # Sample CSV / XLSX Contacts Datasets
│
├── start_app.bat                   # 1-Click Batch Launcher (Backend + Frontend)
├── start_app.ps1                   # 1-Click PowerShell Launcher
├── clean_project.bat               # Cleanup script for temporary build artifacts
├── push_to_github.bat              # One-step Git commit and push script
│
├── mailally-backend/               # Spring Boot Backend API
│   ├── scripts/                    # Diagnostic & Test Utility Scripts
│   └── src/main/java/com/mailally/ # Java Domain Modules
│
└── mailally-frontend/              # React 19 Frontend Web App
    ├── src/                        # SPA Components & Pages
    ├── package.json
    └── vite.config.js
```

---

## 🔑 Default Credentials & Access

- **Frontend Application**: [http://localhost:5173](http://localhost:5173)
- **Backend REST API**: [http://localhost:8081](http://localhost:8081)
- **Swagger Documentation**: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
