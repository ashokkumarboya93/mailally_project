# 🚀 MailAlly Enterprise - SaaS Email Marketing Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19.0-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://react.dev/)
[![Vite](https://img.shields.io/badge/Vite-6.x-646CFF?style=for-the-badge&logo=vite&logoColor=white)](https://vitejs.dev/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind-3.x-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white)](https://tailwindcss.com/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green.style=for-the-badge)](#license)

**MailAlly Enterprise** is a high-performance, enterprise-grade SaaS Email Marketing Platform designed to deliver high-volume email campaigns, real-time analytics, automated audience segmentation, multi-tenant organization isolation, and AI-powered content generation.

---

## ✨ Features & Core Modules

MailAlly is built as an end-to-end multi-tenant platform with 18 fully integrated modules:

### 🔑 Authentication & Security
- **JWT Authentication**: Stateless, secure token-based authentication with refresh token support.
- **RBAC (Role-Based Access Control)**: Granular permissions for `SUPER_ADMIN`, `ADMIN`, `MARKETER`, and `VIEWER`.
- **Multi-Tenant Organization Isolation**: Strict database-level and service-level data partitioning per organization.

### 📊 Dashboard & Real-Time Analytics
- **Executive Dashboard**: Unified overview of campaigns, open rates, delivery performance, and active subscribers.
- **Advanced Analytics**: Detailed metrics on opens, clicks, unsubscribes, bounces, and interactive visual charts (powered by Recharts).

### 📧 Campaign & Email Engine
- **Multi-Channel Email Engine**: Pluggable provider support including SMTP, AWS SES, and Brevo adapters.
- **Campaign Wizard**: Step-by-step creation flow for standard, recurring, and automated drip campaigns.
- **Dynamic Template Editor**: HTML and rich text template management with variable insertion (`{{firstName}}`, `{{company}}`).

### 👥 Audience Management
- **Contact Management**: Bulk CSV import/export, tagging, and contact metadata tracking.
- **Smart Segmentation**: Real-time rule-based segment filtering based on engagement history and attributes.

### 🤖 AI Engine & Utilities
- **AI Copywriter**: Provider-agnostic AI integration (Mock / OpenAI / Claude) for generating subject lines, body content, spam score analysis, and campaign ideas.
- **Notification Center**: Real-time system notifications for campaign completions, threshold alerts, and billing events.
- **Subscription & Billing**: Plan tiers (`FREE`, `STARTER`, `PRO`, `BUSINESS`, `ENTERPRISE`), usage limits, and transaction history.
- **Audit Logging**: Comprehensive activity logs for compliance and enterprise tracking.

---

## 🛠️ Technology Stack

| Layer | Technology |
| :--- | :--- |
| **Backend Framework** | Spring Boot 3.x / Java 21 |
| **Security** | Spring Security + JWT |
| **Persistence** | Spring Data JPA / Hibernate |
| **Database** | MySQL 8.0 |
| **Frontend Framework** | React 19 + Vite |
| **Styling & UI** | Tailwind CSS + Lucide Icons |
| **State Management** | React Context API |
| **HTTP Client** | Axios (with JWT Interceptors) |
| **Charts & Graphs** | Recharts |

---

## 📁 Repository Structure

```
mailally-enterprise/
├── mailally-backend/               # Spring Boot Backend API
│   ├── src/main/java/com/mailally/
│   │   ├── ai/                     # AI Copywriter Module
│   │   ├── analytics/              # Analytics Module
│   │   ├── audit/                  # Audit Log Module
│   │   ├── auth/                   # Authentication Module
│   │   ├── billing/                # Billing & Invoices Module
│   │   ├── campaign/               # Campaign Management
│   │   ├── contact/                # Contact Management
│   │   ├── dashboard/              # Executive Dashboard
│   │   ├── email/                  # Multi-Provider Email Engine
│   │   ├── notification/           # Notification Center
│   │   ├── organization/           # Multi-Tenant Isolation
│   │   ├── scheduler/              # Automated Campaign Scheduler
│   │   ├── segment/                # Audience Segmentation
│   │   ├── settings/               # System & Org Settings
│   │   ├── subscription/           # Tier Limits & Subscriptions
│   │   ├── template/               # Email Templates
│   │   └── user/                   # User Administration
│   └── src/main/resources/
│       ├── application.properties   # Configuration file
│       └── *.sql                   # DDL Schemas
│
└── mailally-frontend/              # React 19 Frontend Web App
    ├── src/
    │   ├── api/                    # API clients (Axios)
    │   ├── components/             # Reusable UI Components & Layouts
    │   ├── context/                # Auth & Theme Contexts
    │   ├── pages/                  # SaaS Application Pages
    │   └── index.css               # Global Tailwind CSS Styles
    ├── package.json
    └── vite.config.js
```

---

## ⚡ Quick Start Guide

### Prerequisites
- **Java 21** or higher installed
- **Node.js 18+** and **npm** installed
- **MySQL 8.0** database running locally on port `3306`

---

### 1️⃣ Database Setup

Create a MySQL database named `mailally`:

```sql
CREATE DATABASE mailally;
```

Update your database credentials in `mailally-backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mailally?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

---

### 2️⃣ Running the Backend (Spring Boot)

#### Option A: Via Eclipse / IntelliJ IDE
1. Import `mailally-backend` as an **Existing Maven Project**.
2. Run `com.mailally.MailallyBackendApplication.java` as a **Java Application**.

#### Option B: Via Terminal
```bash
cd mailally-backend
./mvnw spring-boot:run
```

> Backend will start at: **`http://localhost:8081`**

---

### 3️⃣ Running the Frontend (React + Vite)

Open a new terminal window:

```bash
cd mailally-frontend
npm install
npm run dev
```

> Frontend will start at: **`http://localhost:5173`**

---

## 🔑 Default Credentials

To get started quickly, register a new account on the frontend or use pre-configured test credentials after registration:

- **URL**: `http://localhost:5173/login`
- **Register**: `http://localhost:5173/register`

---

## 📡 REST API Documentation

Key backend REST endpoints available under `/api/v1`:

| Module | Endpoint | Method | Description |
| :--- | :--- | :--- | :--- |
| **Auth** | `/api/v1/auth/register` | `POST` | Register a new organization & admin |
| **Auth** | `/api/v1/auth/login` | `POST` | Authenticate & receive JWT token |
| **Dashboard**| `/api/v1/dashboard/overview` | `GET` | Fetch executive dashboard stats |
| **Campaigns** | `/api/v1/campaigns` | `GET / POST` | Manage email campaigns |
| **Email Engine**| `/api/v1/emails/send` | `POST` | Send single/bulk emails |
| **Contacts** | `/api/v1/contacts` | `GET / POST` | Manage contacts and audience |
| **Segments** | `/api/v1/segments` | `GET / POST` | Audience segmentation rules |
| **AI Module** | `/api/v1/ai/generate` | `POST` | Generate AI email subject & body |
| **Analytics** | `/api/v1/analytics/overview`| `GET` | Performance analytics metrics |

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<p center="align">
Made with ❤️ for Enterprise Email Marketing
</p>
