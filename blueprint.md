---
title: "MailAlly"
subtitle: "Product Blueprint — Enterprise Email Campaign Automation Platform"
date: "July 2026  |  Version 1.0  |  Pre-Development Blueprint"
toc: true
toc-depth: 2
numbersections: true
geometry: "a4paper,margin=0.85in"
fontsize: 10.5pt
linkcolor: NavyBlue
colorlinks: true
---

\newpage

# Foreword — How This Document Was Built

This document is the **Product Blueprint** for MailAlly, an enterprise email campaign automation platform. It is deliberately written **before any code, database schema, or technology stack decision** is finalized. Its purpose is to answer, in full, the questions a product team must answer before an engineering team can build with confidence:

- Who uses this product, and what is each of them allowed to do?
- How does a user move through the product, screen by screen?
- What does every screen actually contain and do?
- What happens on the backend when a user takes an action?
- How do the underlying data concepts relate to one another?
- In what order should the product actually be built?

**Sequencing principle.** MailAlly is being defined in this order, and this order is intentional:

```
Business Requirements
        |
        v
User Experience
        |
        v
Backend Architecture
        |
        v
API Design
        |
        v
Frontend Screens
        |
        v
Implementation
```

A visual UI template (for example, a landing-page design such as "Spydea") is only ever a **visual starting point** — a mood board for tone and layout. It does not define what the product does. The product's requirements, defined in this blueprint, determine what MailAlly does. The template is skinned onto that decision, not the other way around.

**Relationship to prior work.** MailAlly formalizes and extends "Exposys Email Campaign," an existing full-stack email campaign automation platform (Django, Celery, Redis, MySQL) into a defined, enterprise-grade, multi-tenant SaaS product. This blueprint is technology-agnostic by design — it does not prescribe Java, Spring Boot, Django, or any other stack. A separate architecture/tech-stack document should be written *after* this blueprint is approved, and should serve it, not shape it.

**What this document contains.**

1. Who are the users (roles)
2. What permissions each role has
3. The complete navigation of the product
4. Every page in the product, documented individually
5. The backend flow behind the product's core processes
6. The database concept model (relationships, not schema)
7. The order in which the product should be built

\newpage

# 1. Who Are the Users

MailAlly is multi-tenant: many customer **Organizations** (workspaces) run on the same platform, and MailAlly's own operations team manages the platform itself. Roles fall into three groups.

```
Platform-Level Roles      (belong to MailAlly, the company)
   - Super Admin
   - Support Executive
   - Auditor

Organization-Level Roles  (belong to a customer workspace)
   - Organization Owner
   - Organization Admin
   - Marketing Manager
   - Campaign Manager
   - Marketing Executive / Employee
   - Billing Manager
   - Read-Only Viewer

Integration-Level Role    (machine or technical user)
   - Developer / API User
```

## 1.1 Role Directory

| # | Role | Belongs To | One-Line Definition |
|---|------|-----------|----------------------|
| 1 | Super Admin | MailAlly | Owns and operates the entire platform across all customer organizations |
| 2 | Support Executive | MailAlly | Front-line customer support with scoped, logged access into customer workspaces |
| 3 | Auditor | MailAlly / Enterprise Customer | Read-only compliance and security reviewer |
| 4 | Organization Owner | Customer | The person/account that created the workspace; owns billing and top-level control |
| 5 | Organization Admin | Customer | Delegated full administrative control inside one workspace |
| 6 | Marketing Manager | Customer | Owns campaign strategy, audience segmentation, and approvals |
| 7 | Campaign Manager | Customer | Builds, schedules, and launches campaigns day-to-day |
| 8 | Marketing Executive / Employee | Customer | Creates content and manages contacts; limited launch rights |
| 9 | Billing Manager | Customer | Manages subscription plan, payment methods, and invoices |
| 10 | Read-Only Viewer | Customer | Executives/stakeholders who only need to see dashboards and reports |
| 11 | Developer / API User | Customer | Technical integrator connecting MailAlly to other systems via API/webhooks |

## 1.2 Role Profiles

### Super Admin
**Description.** MailAlly's internal, highest-privilege role. Not tied to any single customer organization — operates across the entire platform.
**Responsibilities.** Platform configuration, provider (SES/Brevo/SMTP) and infrastructure oversight, organization lifecycle (approve, suspend, delete), plan/pricing configuration, global announcements, platform-wide analytics, impersonation for support escalations.
**Restrictions.** Cannot bypass audit logging; sensitive actions (impersonation, data export, organization deletion) require a logged reason and are themselves audited.
**Typical Navigation.** A separate "Platform Console" outside any single organization's dashboard: Organizations list, Platform Analytics, System Settings, Provider Health, Billing Oversight, Release Notes.
**Security.** Mandatory 2FA, IP allow-listing recommended, session timeout shorter than customer roles.

### Support Executive
**Description.** MailAlly's customer-facing support staff.
**Responsibilities.** View (not edit) customer workspaces to diagnose issues, restart stuck campaigns, view delivery/bounce logs, respond to support tickets.
**Restrictions.** Cannot access billing card details, cannot delete organizations or campaigns, cannot export contact lists in bulk. Any "view as customer" session is time-boxed and logged.
**Typical Navigation.** Platform Console -> Organizations -> [Selected Org] -> read-only workspace view + Support ticket queue.
**Security.** All impersonation sessions logged with start/end timestamps and viewed screens.

### Auditor
**Description.** Independent reviewer (internal compliance or an enterprise customer's own security team).
**Responsibilities.** Review audit logs, security settings, access history, and data-handling records; verify compliance posture.
**Restrictions.** Strictly read-only across the entire product — cannot create, edit, send, or delete anything.
**Typical Navigation.** Audit Logs, Security Settings (view-only), Team & Roles (view-only), Data Export history.
**Security.** Access itself is logged; exports of audit data are watermarked with requester identity.

### Organization Owner
**Description.** The individual or account that registered the organization; the ultimate authority within that workspace.
**Responsibilities.** Everything an Organization Admin can do, plus: manage billing and subscription plan, transfer or delete the organization, add/remove Organization Admins.
**Restrictions.** None within their own organization; cannot see or affect other organizations.
**Typical Navigation.** Full sidebar access including Billing and Danger Zone (delete/transfer workspace) inside Settings.
**Security.** 2FA strongly recommended and prompted at first login; billing changes require re-authentication.

### Organization Admin
**Description.** Delegated administrator with full operational control but without ownership-level authority (billing/deletion).
**Responsibilities.** Manage users and roles, manage sending domains, manage integrations/API keys, manage all campaigns/templates/contacts, view all analytics.
**Restrictions.** Cannot delete or transfer the organization; cannot downgrade the Owner's role.
**Typical Navigation.** Full sidebar except Danger Zone.
**Security.** 2FA recommended.

### Marketing Manager
**Description.** Owns the campaign calendar and audience strategy for the organization.
**Responsibilities.** Create/approve campaigns, manage segments, manage templates, review analytics and reports, approve sends above a configurable audience-size threshold.
**Restrictions.** Cannot manage billing, cannot manage users/roles, cannot manage sending domains or API keys.
**Typical Navigation.** Dashboard, Contacts, Segments, Templates, Campaigns, Analytics, Reports.

### Campaign Manager
**Description.** Executes the day-to-day work of building and sending campaigns.
**Responsibilities.** Create, edit, schedule, launch, pause, and retry campaigns; select templates and segments; monitor live send progress.
**Restrictions.** Cannot delete templates created by others without permission; cannot access billing, users, or domain settings; large sends (above threshold) require Marketing Manager approval.
**Typical Navigation.** Dashboard, Contacts (view), Templates, Campaigns, Campaign Monitor, Analytics (own campaigns).

### Marketing Executive / Employee
**Description.** General-purpose contributor — content creation and contact hygiene.
**Responsibilities.** Create/edit templates, import and clean contact lists, build segments, draft campaigns (as drafts only, cannot launch without approval, configurable per organization).
**Restrictions.** Cannot launch campaigns to large/production segments by default; cannot access billing, users, API, or settings.
**Typical Navigation.** Dashboard, Contacts, Import, Templates, Campaigns (draft only).

### Billing Manager
**Description.** Manages the commercial relationship with MailAlly.
**Responsibilities.** View/change subscription plan, manage payment methods, download invoices, monitor usage against plan limits (contacts, emails/month, seats).
**Restrictions.** No access to Contacts, Templates, Campaigns, or Analytics content unless separately granted a second role.
**Typical Navigation.** Billing, Subscription, Invoices, Usage.

### Read-Only Viewer
**Description.** Stakeholders (executives, clients of an agency, cross-functional partners) who need visibility without edit rights.
**Responsibilities.** View dashboards, analytics, and reports; export reports they are entitled to see.
**Restrictions.** Cannot create, edit, send, delete, or configure anything anywhere in the product.
**Typical Navigation.** Dashboard, Analytics, Reports (all view-only).

### Developer / API User
**Description.** A technical account (human or machine, represented by an API key) used to integrate MailAlly with external systems.
**Responsibilities.** Manage API keys and webhooks (if granted), trigger transactional/campaign sends via API, read delivery/engagement data programmatically.
**Restrictions.** No access to the visual dashboard by default (API-only); rate-limited; scoped API keys limit which endpoints/data are reachable.
**Typical Navigation.** API Center (keys, webhooks, logs, documentation) only.

\newpage

# 2. What Permissions Does Each Role Have

Permissions are expressed at the module level. Each role below lists what it **can do**; anything not listed is denied by default (MailAlly follows a deny-by-default permission model).

**Super Admin**

- Manage all organizations (approve, suspend, delete)
- Manage platform-wide settings and email providers
- Manage global pricing plans
- View platform-wide analytics
- Impersonate a user for support (logged)
- Manage platform release notes / announcements

**Organization Owner**

- All Organization Admin permissions, plus:
- Manage billing and subscription plan
- Delete or transfer the organization
- Promote/demote Organization Admins

**Organization Admin**

- Create / Edit / Delete Campaigns
- Manage Users and Roles
- Manage Sending Domains
- Manage API Keys and Webhooks
- Manage Templates and Segments
- Manage Contacts (import/export/delete)
- View full Analytics and Reports
- Edit Organization Settings

**Marketing Manager**

- Create / Approve Campaigns
- Manage Segments
- Manage Templates
- View full Analytics and Reports
- Approve high-volume sends
- View Contacts (no delete)

**Campaign Manager**

- Create / Edit / Launch / Pause / Retry Campaigns (below approval threshold)
- Use Templates and Segments (no delete rights on shared assets)
- View Campaign Analytics
- View Contacts (no import/delete)

**Marketing Executive / Employee**

- Create / Edit Templates
- Import / Clean Contacts
- Build Segments
- Create Campaigns as Drafts (cannot launch without approval)
- View own-campaign Analytics

**Billing Manager**

- View / Change Subscription Plan
- Manage Payment Methods
- Download Invoices
- View Usage against Plan Limits

**Read-Only Viewer**

- View Dashboard
- View Analytics
- View Reports (export where permitted)

**Support Executive (Platform)**

- View (read-only) any organization's workspace, scoped and logged
- Restart/unstick a failed campaign batch
- View delivery/bounce/complaint logs
- Respond to support tickets

**Auditor**

- View Audit Logs
- View Security Settings
- View Team & Roles
- View Data Export History

**Developer / API User**

- Manage own API Keys
- Manage Webhooks (if granted by Org Admin)
- Trigger sends via API
- Read delivery/engagement data via API
- View API usage logs

## 2.1 Permission Matrix — Reference

Legend: **F** = Full access, **P** = Partial / scoped access, **V** = View only, blank = No access.

**Table 2.1a — Platform & Ownership Roles**

| Module | Super Admin | Org Owner | Org Admin | Auditor |
|---|---|---|---|---|
| Create / Launch Campaign | | F | F | |
| Delete Campaign | | F | F | |
| Manage Users & Roles | | F | F | V |
| Manage Contacts / Import | | F | F | |
| Manage Templates | | F | F | |
| Manage Segments | | F | F | |
| Billing & Subscription | | F | | |
| Manage Sending Domains | | F | F | V |
| Org / System Settings | F | F | F | V |
| Analytics & Reports | F | F | F | V |
| API Keys & Webhooks | | F | F | V |
| Audit Logs | F | V | V | F |
| Manage Organizations (platform) | F | | | |

**Table 2.1b — Working & Integration Roles**

| Module | Marketing Mgr | Campaign Mgr | Employee | Billing Mgr | Read-Only | Support Exec | Developer |
|---|---|---|---|---|---|---|---|
| Create / Launch Campaign | F | P | P (drafts) | | | | P (API) |
| Delete Campaign | P | | | | | | |
| Manage Users & Roles | | | | | | | |
| Manage Contacts / Import | V | V | F | | | V | P (API) |
| Manage Templates | F | P | F | | | | |
| Manage Segments | F | P | P | | | | |
| Billing & Subscription | | | | F | | | |
| Manage Sending Domains | | | | | | | |
| Org / System Settings | | | | | | | |
| Analytics & Reports | F | P | P | V (usage) | V | V | P (API) |
| API Keys & Webhooks | | | | | | | F |
| Audit Logs | | | | | | | |

\newpage

# 3. Complete Navigation

## 3.1 First-Time User Journey (Linear)

```
Landing Page
     |
     v
Registration  (email + password, or SSO)
     |
     v
Email Verification  (OTP / verification link)
     |
     v
Workspace (Organization) Creation
     |
     v
Guided Onboarding  (invite team, connect sending domain, import first contacts)
     |
     v
Dashboard
     |
     +--> Contacts        --> Import Contacts --> Segments
     |
     +--> Templates        --> Template Builder
     |
     +--> Campaigns         --> Campaign Wizard --> Campaign Monitor
     |
     +--> Analytics         --> Campaign Analytics Detail
     |
     +--> Reports
     |
     +--> Billing           --> Subscription --> Invoices
     |
     +--> Settings          --> Users & Roles / Domains / API Center / Audit Logs
     |
     v
Logout
```

## 3.2 Returning User Journey

```
Landing Page --> Login --> (2FA if enabled) --> Dashboard
```

If the account belongs to more than one organization, a **Workspace Switcher** is shown between Login and Dashboard.

## 3.3 Persistent Application Map (Left Sidebar)

Once inside a workspace, navigation is not strictly linear — every module below is reachable from a persistent sidebar on every page.

```
Dashboard

Contacts
   |-- All Contacts
   |-- Import Contacts
   |-- Segments
   |-- Suppression List

Templates
   |-- All Templates
   |-- Template Builder

Campaigns
   |-- All Campaigns (History)
   |-- Campaign Wizard (New)
   |-- Campaign Monitor (active campaign detail)
   |-- Automation Workflows

Analytics
   |-- Overview
   |-- Campaign Analytics (per campaign)
   |-- Engagement Heatmap

Reports
   |-- Scheduled Reports
   |-- Export Center

Billing
   |-- Subscription
   |-- Invoices
   |-- Usage

Team
   |-- Users
   |-- Roles & Permissions

Developer
   |-- API Center (Keys, Docs)
   |-- Webhooks
   |-- Task / Job Status

Settings
   |-- Organization Profile
   |-- Sending Domains
   |-- Notification Preferences
   |-- Audit Logs
   |-- Danger Zone (Owner only)

Support / Help Center

Profile (account menu, top-right)
   |-- My Profile
   |-- Notifications
   |-- Logout
```

## 3.4 Transition Notes

| Transition | Trigger | Notes |
|---|---|---|
| Landing -> Registration | "Start Free Trial" / "Sign Up" button | Captures email, password, name |
| Registration -> Email Verification | Form submit | Account created in `pending_verification` state; cannot log in until verified |
| Email Verification -> Workspace Creation | Link/OTP confirmed | First-time users only; returning users skip to Dashboard |
| Workspace Creation -> Onboarding | Workspace name + sending purpose submitted | Organization record created; user becomes Organization Owner |
| Onboarding -> Dashboard | "Skip" or steps completed | Onboarding checklist remains visible on Dashboard until complete |
| Dashboard -> Any Module | Sidebar click | Sidebar state persists across the session |
| Campaign Wizard -> Campaign Monitor | "Launch Campaign" confirmed | Redirects to live monitor for the newly launched campaign |
| Any Page -> Logout | Profile menu -> Logout | Clears session/JWT; returns to Landing |

\newpage

# 4. Every Page, Documented

Each page below is documented against a fixed template: Purpose, Primary Users, Key UI Elements (Cards / Tables / Filters / Charts), Buttons & Actions, Navigation, Validation, Permissions, and API Calls. API endpoint names shown are illustrative of intent, not a final contract.

## 4.1 Landing Page

**Purpose.** Public marketing entry point; converts visitors into trial sign-ups.
**Primary Users.** Anonymous visitors.
**Key UI Elements.** Hero section with value proposition; feature highlight cards (Contacts, Campaigns, Analytics, Automation); pricing summary; customer logos/testimonials; FAQ accordion.
**Buttons & Actions.** `Start Free Trial` -> Registration. `Log In` -> Login. `Book a Demo` -> lead-capture form -> CRM webhook.
**Navigation.** Entry point only; links to Login, Registration, Pricing, Docs, Support.
**Validation.** Demo/lead form validates email format and required fields.
**Permissions.** Public, no authentication.

**API Calls.** `POST /api/leads/demo-request`

## 4.2 Login

**Purpose.** Authenticate an existing user into their organization(s).
**Primary Users.** All roles.
**Key UI Elements.** Email/password fields, "Continue with SSO" option, "Remember this device" toggle.
**Buttons & Actions.** `Log In` -> validates credentials -> issues session -> Dashboard (or Workspace Switcher if multiple orgs). `Forgot Password?` -> Forgot Password page.
**Navigation.** From Landing; on success, to Dashboard or Workspace Switcher.
**Validation.** Email format required; password required; account-lock after N failed attempts; 2FA challenge if enabled.
**Permissions.** Public form; access outcome depends on account status (active, suspended, pending-verification).

**API Calls.** `POST /api/auth/login`, `POST /api/auth/refresh`

## 4.3 Registration

**Purpose.** Create a new MailAlly account.
**Primary Users.** Anonymous visitors becoming Organization Owners.
**Key UI Elements.** Name, work email, password, terms-of-service checkbox.
**Buttons & Actions.** `Create Account` -> creates user in `pending_verification` state -> sends verification email -> Email Verification page.
**Navigation.** From Landing; to Email Verification.
**Validation.** Business-email pattern check (optional), password strength meter, duplicate-email check, ToS checkbox required.
**Permissions.** Public.

**API Calls.** `POST /api/auth/register`

## 4.4 Email Verification (OTP / Link)

**Purpose.** Confirm ownership of the registered email address before granting access.
**Primary Users.** Newly registered users.
**Key UI Elements.** OTP input (6-digit) or "click the link we sent" message; countdown/resend timer.
**Buttons & Actions.** `Verify` -> validates OTP -> Workspace Creation. `Resend Code` -> rate-limited resend.
**Navigation.** From Registration; to Workspace Creation (first-time) or Dashboard (already has a workspace, e.g. re-verifying a changed email).
**Validation.** OTP must match and be unexpired (typically 10 minutes); resend rate-limited (e.g. 1 per 60 seconds).
**Permissions.** Authenticated-but-unverified users only.

**API Calls.** `POST /api/auth/verify-otp`, `POST /api/auth/resend-otp`

## 4.5 Forgot Password / Reset Password

**Purpose.** Allow a user to regain access without support intervention.
**Primary Users.** Any role that has forgotten their password.
**Key UI Elements.** Email input (request stage); new-password + confirm fields (reset stage).
**Buttons & Actions.** `Send Reset Link` -> email dispatched. `Reset Password` -> validates token -> updates password -> Login.
**Navigation.** From Login; back to Login on completion.
**Validation.** Reset token single-use and time-boxed; new password must meet strength policy and cannot equal the previous password.
**Permissions.** Public (request stage); token-holder only (reset stage).

**API Calls.** `POST /api/auth/forgot-password`, `POST /api/auth/reset-password`

## 4.6 Workspace (Organization) Creation

**Purpose.** Establish the customer's tenant/workspace.
**Primary Users.** The verifying user, who becomes Organization Owner.
**Key UI Elements.** Organization name, industry dropdown, expected sending volume, timezone.
**Buttons & Actions.** `Create Workspace` -> creates Organization record -> assigns Owner role -> Onboarding checklist.
**Navigation.** From Email Verification; to Onboarding/Dashboard.
**Validation.** Organization name required and unique-enough (slug collision check); sending-volume selection influences default plan suggestion.
**Permissions.** Newly verified user only.

**API Calls.** `POST /api/organizations`

## 4.7 Dashboard

**Purpose.** At-a-glance summary of account health and quick access to core actions.
**Primary Users.** All organization roles (content scoped by role).
**Key UI Elements.**
  - Cards: Total Contacts, Emails Sent (period), Open Rate, Click Rate, Active Campaigns, Plan Usage.
  - Live widget: progress bar for any currently-running campaign (speed, ETA).
  - Onboarding checklist card (until complete): verify domain, import contacts, send first campaign.
  - Recent Activity table (last 10 actions).
**Buttons & Actions.** `New Campaign` -> Campaign Wizard. `Import Contacts` -> Import page. `View All Activity` -> Audit Logs.
**Navigation.** Home base; sidebar reachable from here to every module.
**Validation.** N/A (read-mostly page).
**Permissions.** Read-Only Viewer sees cards/charts only, no action buttons.

**API Calls.** `GET /api/analytics/dashboard`, `GET /api/campaigns?status=running`

## 4.8 Contacts — All Contacts

**Purpose.** Central directory of every contact in the organization.
**Primary Users.** Org Admin, Marketing Manager, Employee (edit); Campaign Manager, Read-Only (view).
**Key UI Elements.**
  - Table: Name, Email, Phone, College/Company, Location, Subscribed, Valid, Date Added.
  - Filters: college/company, location, subscription status, validity, tags, date-added range.
  - Search bar (name/email).
  - Pagination and column sort.
**Buttons & Actions.** `Add Contact` -> inline form. `Import` -> Import Contacts page. `Export` -> CSV download job. `Bulk Action` (delete / unsubscribe / tag) on selected rows.
**Navigation.** From sidebar Contacts; links to Import Contacts, Segments, individual Contact Detail drawer.
**Validation.** Email format enforced on manual add/edit; duplicate-email detection against existing contacts.
**Permissions.** Delete/bulk-action restricted to Org Admin and Marketing Manager (configurable).

**API Calls.** `GET /api/contacts`, `POST /api/contacts`, `PUT /api/contacts/{id}`, `DELETE /api/contacts/{id}`, `POST /api/contacts/export`, `POST /api/contacts/bulk-action`

## 4.9 Import Contacts

**Purpose.** Bulk-ingest contacts from CSV/Excel with automatic column mapping.
**Primary Users.** Org Admin, Marketing Manager, Employee.
**Key UI Elements.**
  - Drag-and-drop upload zone.
  - Auto-detected column-mapping table (source column -> MailAlly field), editable before confirming.
  - Live progress bar with rows processed / valid / invalid counts.
  - Post-import summary card.
**Buttons & Actions.** `Upload File` -> parses headers -> mapping table. `Confirm Mapping & Import` -> background ingestion job starts. `Download Invalid Rows` -> CSV of rejected rows with reasons.
**Navigation.** From Contacts; returns to All Contacts on completion.
**Validation.** File type restricted to `.csv`/`.xlsx`/`.xls`; row-level email format validation; duplicate detection within file and against existing contacts; file size/row-count ceiling per plan.
**Permissions.** Same as Contacts edit rights.

**API Calls.** `POST /api/contacts/upload`, `GET /api/contacts/upload/{id}/progress`, `GET /api/contacts/upload-status/{id}`, `GET /api/contacts/columns`

## 4.10 Segments

**Purpose.** Define reusable, rule-based or manually-curated audience subsets for targeting.
**Primary Users.** Marketing Manager, Campaign Manager, Employee.
**Key UI Elements.**
  - Table: Segment Name, Type (Dynamic/Static), Contact Count, Last Updated.
  - Rule builder (AND/OR conditions on contact fields, tags, engagement history).
  - Preview count as rules are edited.
**Buttons & Actions.** `New Segment` -> rule builder. `Duplicate`, `Delete`, `Use in Campaign` -> Campaign Wizard with segment pre-selected.
**Navigation.** From Contacts sidebar group; feeds into Campaign Wizard audience step.
**Validation.** At least one rule or manual member required to save; dynamic segments recompute count on save.
**Permissions.** Delete restricted to Marketing Manager and Org Admin.

**API Calls.** `GET /api/segments`, `POST /api/segments`, `PUT /api/segments/{id}`, `DELETE /api/segments/{id}`, `GET /api/segments/{id}/preview-count`

## 4.11 Suppression List

**Purpose.** Track and enforce emails that must never receive mail again (hard bounces, complaints, unsubscribes, manual blocks) for deliverability and compliance.
**Primary Users.** Org Admin, Marketing Manager (view/manage); system (auto-populates).
**Key UI Elements.** Table: Email, Reason (bounce, complaint, unsubscribe, manual), Source Campaign, Date Added. Filters by reason and date.
**Buttons & Actions.** `Add Manually` (block an address). `Remove` (Org Admin only, with confirmation — rare/compliance-sensitive). `Export`.
**Navigation.** From Contacts sidebar group.
**Validation.** Manual removal requires typed confirmation; system-added entries (bounce/complaint) cannot be silently deleted without an audit note.
**Permissions.** View: Marketing Manager+. Manage: Org Admin only.

**API Calls.** `GET /api/suppression-list`, `POST /api/suppression-list`, `DELETE /api/suppression-list/{id}`

\newpage

## 4.12 Templates — All Templates

**Purpose.** Library of reusable email templates.
**Primary Users.** Employee, Marketing Manager, Campaign Manager.
**Key UI Elements.** Grid/table of templates with thumbnail preview, Name, Type (marketing, transactional, newsletter, announcement), Last Edited, Used-In-Campaigns count. Filters by type and creator.
**Buttons & Actions.** `New Template` -> Template Builder. `Duplicate`, `Delete`, `Preview`, `Use in Campaign`.
**Navigation.** From sidebar; feeds Campaign Wizard template step.
**Validation.** Cannot delete a template currently attached to a scheduled/running campaign.
**Permissions.** Delete restricted to creator, Marketing Manager, Org Admin.

**API Calls.** `GET /api/templates`, `DELETE /api/templates/{id}`

## 4.13 Template Builder

**Purpose.** Create and edit an email template's subject, HTML body, and plain-text fallback.
**Primary Users.** Employee, Marketing Manager, Campaign Manager.
**Key UI Elements.** WYSIWYG/HTML editor pane, live preview pane, personalization-variable toolbar (`{{name}}`, `{{college}}`, `{{custom_variables.key}}`, etc.), subject-line field, plain-text auto-generation toggle.
**Buttons & Actions.** `Insert Variable` -> inserts token at cursor. `Send Test Email` -> sends rendered sample to the editor's own address. `Save`, `Save & Preview`.
**Navigation.** From Templates list; back to Templates on save.
**Validation.** Subject required; unresolved/unknown variable tokens flagged before save; HTML sanitized against unsafe scripts (sandboxed rendering).
**Permissions.** Same as Templates edit rights.

**API Calls.** `POST /api/templates`, `PUT /api/templates/{id}`, `GET /api/templates/{id}/preview`

## 4.14 Campaign Wizard (New Campaign)

**Purpose.** Guided, step-by-step creation and launch of a campaign.
**Primary Users.** Campaign Manager, Marketing Manager, Employee (draft only).
**Key UI Elements.** Multi-step wizard: (1) Name & Type, (2) Template selection, (3) Audience (segment or filters), (4) Sending provider/domain, (5) Schedule (now / later / recurring), (6) Review & Launch. Estimated-recipient count and estimated-send-duration shown throughout.
**Buttons & Actions.** `Save as Draft` (any step). `Next` / `Back`. `Send Test`. `Launch Campaign` -> confirmation modal -> orchestration starts -> Campaign Monitor. `Schedule` -> creates a scheduled job instead of an immediate one.
**Navigation.** From Campaigns; on launch, redirects to Campaign Monitor for the new campaign.
**Validation.** Template and non-empty audience required to launch; large sends above an org-configured threshold require Marketing Manager/Org Admin approval before `Launch` is enabled; sending-domain must be verified.
**Permissions.** Launch action gated by role and by approval threshold; Employee role limited to `Save as Draft`.

**API Calls.** `POST /api/campaigns`, `PUT /api/campaigns/{id}`, `POST /api/campaigns/{id}/launch`

## 4.15 Campaign Monitor (Live Campaign Detail)

**Purpose.** Real-time view of a sending or completed campaign.
**Primary Users.** Campaign Manager, Marketing Manager, Org Admin.
**Key UI Elements.** Live progress bar (sent / failed / pending), speed (emails/sec), ETA counter, per-batch status table, delivery-status breakdown chart (sent, delivered, opened, clicked, bounced, complained).
**Buttons & Actions.** `Pause`, `Resume`, `Cancel`, `Retry Failed`, `Retry Batch #N`, `Export Logs`.
**Navigation.** From Campaign Wizard on launch, or from Campaigns history list.
**Validation.** `Pause`/`Cancel` disabled once campaign has fully completed; `Retry` only enabled when failed-count > 0.
**Permissions.** Pause/Cancel/Retry restricted to Campaign Manager+.

**API Calls.** `GET /api/campaigns/{id}/progress`, `POST /api/campaigns/{id}/pause`, `POST /api/campaigns/{id}/resume`, `POST /api/campaigns/{id}/cancel`, `POST /api/campaigns/{id}/retry`, `POST /api/campaigns/{id}/retry-batch/{batchNum}`

## 4.16 Campaigns — All Campaigns (History)

**Purpose.** Historical record of every campaign, any status.
**Primary Users.** Campaign Manager, Marketing Manager, Org Admin, Read-Only Viewer.
**Key UI Elements.** Table: Name, Status (draft, scheduled, running, paused, completed, failed, cancelled), Sent, Open Rate, Click Rate, Sent Date. Status filter, date-range filter, search.
**Buttons & Actions.** `New Campaign`, row click -> Campaign Monitor (running) or Campaign Analytics Detail (completed), `Duplicate Campaign`, `Delete Draft`.
**Navigation.** Central hub for Campaign Wizard and Campaign Monitor.
**Validation.** Only `draft` campaigns can be deleted directly; others must be cancelled first.
**Permissions.** Delete restricted to creator, Marketing Manager, Org Admin.

**API Calls.** `GET /api/campaigns`, `DELETE /api/campaigns/{id}`

## 4.17 Automation Workflows

**Purpose.** Define trigger-based, multi-step email sequences (e.g., welcome series, re-engagement) that run without manual launch each time.
**Primary Users.** Marketing Manager, Org Admin.
**Key UI Elements.** Visual workflow canvas (trigger -> wait -> condition -> send template -> branch), workflow list table with status (active/paused/draft) and enrolled-contact count.
**Buttons & Actions.** `New Workflow`, `Activate`/`Pause`, `Duplicate`, `View Enrolled Contacts`.
**Navigation.** From Campaigns sidebar group.
**Validation.** At least one trigger and one send-step required to activate; circular loops flagged.
**Permissions.** Create/edit restricted to Marketing Manager, Org Admin.

**API Calls.** `GET /api/automations`, `POST /api/automations`, `PUT /api/automations/{id}`, `POST /api/automations/{id}/activate`

## 4.18 Analytics — Overview

**Purpose.** Organization-wide engagement analytics across all campaigns.
**Primary Users.** Marketing Manager, Org Admin, Read-Only Viewer.
**Key UI Elements.** KPI cards (Delivery Rate, Open Rate, Click Rate, Bounce Rate, Complaint Rate), trend line chart over time, top-performing campaigns table, engagement-by-location/college heatmap.
**Buttons & Actions.** `Date Range` selector, `Export`, drill-through from any campaign row -> Campaign Analytics Detail.
**Navigation.** From sidebar Analytics.
**Validation.** N/A (read-only).
**Permissions.** Read-Only Viewer has full view access here (no export limits beyond plan tier).

**API Calls.** `GET /api/analytics/dashboard`, `GET /api/analytics/heatmap`

## 4.19 Campaign Analytics Detail

**Purpose.** Deep-dive analytics for a single completed (or in-progress) campaign.
**Primary Users.** Campaign Manager, Marketing Manager, Org Admin, Read-Only Viewer.
**Key UI Elements.** Funnel chart (sent -> delivered -> opened -> clicked), bounce/complaint breakdown, link-click leaderboard, recipient-level activity table, device/client breakdown.
**Buttons & Actions.** `Export Report`, `View Recipient History` (per row) -> event timeline drawer.
**Navigation.** From Campaign Monitor or Campaigns history.
**Validation.** N/A (read-only).
**Permissions.** Standard view rights; recipient-level PII visibility may be restricted further by org policy.

**API Calls.** `GET /api/analytics/campaign/{campaignId}`, `GET /api/analytics/recipient/{campaignContactId}/history`

## 4.20 Reports

**Purpose.** Scheduled and on-demand exportable reports for stakeholders.
**Primary Users.** Marketing Manager, Org Admin, Read-Only Viewer.
**Key UI Elements.** Report templates list (Campaign Summary, Engagement Trend, Deliverability, Contact Growth), scheduled-report table (frequency, recipients), report history with download links.
**Buttons & Actions.** `New Scheduled Report`, `Run Now`, `Download`, `Delete Schedule`.
**Navigation.** From sidebar Reports.
**Validation.** Report recipients must be valid emails; at least one report section selected.
**Permissions.** Create/schedule restricted to Marketing Manager, Org Admin; Read-Only Viewer can only run/download reports they're a recipient of.

**API Calls.** `GET /api/reports`, `POST /api/reports/schedule`, `GET /api/reports/{id}/download`

\newpage

## 4.21 Billing — Subscription

**Purpose.** Manage the organization's plan and understand usage against limits.
**Primary Users.** Billing Manager, Organization Owner.
**Key UI Elements.** Current plan card (contacts limit, emails/month limit, seats), usage progress bars, plan comparison table, `Upgrade`/`Downgrade` options.
**Buttons & Actions.** `Change Plan`, `Add Payment Method`, `Cancel Subscription` (Owner only, confirmation required).
**Navigation.** From sidebar Billing.
**Validation.** Downgrade blocked if current usage exceeds target plan's limits; cancellation requires typed confirmation.
**Permissions.** Owner and Billing Manager only; all other roles have no access to this page.

**API Calls.** `GET /api/billing/subscription`, `PUT /api/billing/subscription`, `POST /api/billing/cancel`

## 4.22 Billing — Invoices

**Purpose.** Historical billing records.
**Primary Users.** Billing Manager, Organization Owner.
**Key UI Elements.** Table: Invoice #, Date, Amount, Status (paid/due/failed), Download link.
**Buttons & Actions.** `Download PDF`, `Update Payment Method` (from a failed invoice).
**Navigation.** From Billing sidebar group.
**Validation.** N/A (read-mostly).
**Permissions.** Owner and Billing Manager only.

**API Calls.** `GET /api/billing/invoices`, `GET /api/billing/invoices/{id}/download`

## 4.23 Team — Users

**Purpose.** Manage who has access to the organization and what role they hold.
**Primary Users.** Org Admin, Organization Owner.
**Key UI Elements.** Table: Name, Email, Role, Status (active/invited/suspended), Last Login. Search and role filter.
**Buttons & Actions.** `Invite User` (email + role) -> invitation email sent. `Edit Role`, `Suspend`, `Remove`, `Resend Invite`.
**Navigation.** From sidebar Team.
**Validation.** Cannot remove the last Organization Owner; invited email must not already belong to the organization.
**Permissions.** Org Admin, Organization Owner only.

**API Calls.** `GET /api/users`, `POST /api/users/invite`, `PUT /api/users/{id}/role`, `DELETE /api/users/{id}`

## 4.24 Team — Roles & Permissions

**Purpose.** View (and, for custom roles, edit) what each role is allowed to do.
**Primary Users.** Org Admin, Organization Owner.
**Key UI Elements.** Role list with permission matrix (as in Section 2), toggle switches per module for custom roles (Enterprise plan feature).
**Buttons & Actions.** `New Custom Role` (Enterprise plans), `Edit Permissions`, `Save Changes`.
**Navigation.** From Team sidebar group.
**Validation.** System roles (Owner, Admin) are not editable; at least one module must remain accessible for any active role.
**Permissions.** Org Admin, Organization Owner only.

**API Calls.** `GET /api/roles`, `PUT /api/roles/{id}`

## 4.25 Developer — API Center

**Purpose.** Manage API keys and view integration documentation.
**Primary Users.** Developer / API User, Org Admin.
**Key UI Elements.** API key table (name, key prefix, scopes, created date, last used), embedded API documentation links, code-sample snippets.
**Buttons & Actions.** `Generate New Key` (choose scopes) -> key shown once. `Revoke Key`.
**Navigation.** From sidebar Developer group.
**Validation.** Key name required; at least one scope selected; revoked keys cannot be un-revoked (must generate a new one).
**Permissions.** Org Admin manages all keys; Developer role manages only its own.

**API Calls.** `GET /api/api-keys`, `POST /api/api-keys`, `DELETE /api/api-keys/{id}`

## 4.26 Developer — Webhooks

**Purpose.** Configure outbound webhooks so external systems receive real-time delivery/engagement events.
**Primary Users.** Developer / API User, Org Admin.
**Key UI Elements.** Webhook table (URL, subscribed events, status, last delivery result), event-type checklist (sent, delivered, opened, clicked, bounced, complained, unsubscribed).
**Buttons & Actions.** `Add Webhook`, `Send Test Event`, `Disable`, `Delete`, `View Delivery Log`.
**Navigation.** From Developer sidebar group.
**Validation.** URL must be a reachable HTTPS endpoint; at least one event type selected.
**Permissions.** Same as API Center.

**API Calls.** `GET /api/webhooks`, `POST /api/webhooks`, `POST /api/webhooks/{id}/test`, `DELETE /api/webhooks/{id}`

## 4.27 Developer — Task / Job Status

**Purpose.** Visibility into background jobs (imports, exports, large sends) for technical users.
**Primary Users.** Developer / API User, Org Admin, Support Executive.
**Key UI Elements.** Table: Job ID, Type, Status (queued, running, completed, failed), Progress %, Started/Completed timestamps.
**Buttons & Actions.** `Retry Job` (failed only), `View Error Detail`.
**Navigation.** From Developer sidebar group; also linked contextually from Import Contacts and Reports.
**Validation.** N/A (read-mostly).
**Permissions.** View: Org Admin, Developer, Support Executive.

**API Calls.** `GET /api/tasks/{taskId}/status`

## 4.28 Settings — Organization Profile

**Purpose.** Core organization metadata and branding.
**Primary Users.** Org Admin, Organization Owner.
**Key UI Elements.** Organization name, logo upload, timezone, default from-name/from-email, unsubscribe-footer template.
**Buttons & Actions.** `Save Changes`.
**Navigation.** From sidebar Settings.
**Validation.** From-email must belong to a verified sending domain; unsubscribe footer cannot be removed entirely (compliance requirement).
**Permissions.** Org Admin, Organization Owner.

**API Calls.** `GET /api/settings`, `PUT /api/settings`

## 4.29 Settings — Sending Domains

**Purpose.** Verify and manage domains used to send campaign email, protecting deliverability.
**Primary Users.** Org Admin, Developer.
**Key UI Elements.** Domain table (domain, verification status, SPF/DKIM/DMARC status), DNS-record instructions panel.
**Buttons & Actions.** `Add Domain`, `Verify Now` (re-checks DNS), `Set as Default`, `Remove Domain`.
**Navigation.** From Settings sidebar group.
**Validation.** Cannot remove a domain currently used as a campaign's active sending domain; verification re-checked against live DNS records.
**Permissions.** Org Admin only.

**API Calls.** `GET /api/domains`, `POST /api/domains`, `POST /api/domains/{id}/verify`, `DELETE /api/domains/{id}`

## 4.30 Settings — Notification Preferences

**Purpose.** Configure which in-product and email notifications each user receives.
**Primary Users.** All authenticated roles (self-scoped).
**Key UI Elements.** Toggle list: campaign completed, campaign failed, weekly summary, billing alerts, team changes.
**Buttons & Actions.** `Save Preferences`.
**Navigation.** From Settings sidebar group, or from Profile menu.
**Validation.** N/A.
**Permissions.** Self only (every role manages their own notification preferences).

**API Calls.** `GET /api/notifications/preferences`, `PUT /api/notifications/preferences`

## 4.31 Settings — Audit Logs

**Purpose.** Immutable record of who did what, when, across the organization.
**Primary Users.** Org Admin, Auditor, Organization Owner.
**Key UI Elements.** Table: Actor, Action, Entity, Timestamp, IP Address. Filters by actor, action type, date range.
**Buttons & Actions.** `Export`.
**Navigation.** From Settings sidebar group.
**Validation.** N/A (immutable, read-only).
**Permissions.** Org Admin, Organization Owner, Auditor.

**API Calls.** `GET /api/audit-logs`, `GET /api/audit-logs/export`

## 4.32 Settings — Danger Zone

**Purpose.** Irreversible organization-level actions.
**Primary Users.** Organization Owner only.
**Key UI Elements.** `Transfer Ownership`, `Delete Organization` cards, each with a clear warning of consequences.
**Buttons & Actions.** `Transfer Ownership` (select new owner, requires their acceptance), `Delete Organization` (typed confirmation of the org name required).
**Navigation.** From Settings sidebar group, visually separated from other settings.
**Validation.** Deletion blocked while an active subscription/outstanding invoice exists, until resolved.
**Permissions.** Organization Owner only.

**API Calls.** `POST /api/organizations/{id}/transfer`, `DELETE /api/organizations/{id}`

## 4.33 Notification Center

**Purpose.** In-app inbox of system notifications.
**Primary Users.** All authenticated roles.
**Key UI Elements.** List of notifications (campaign completed, invite received, billing alert, domain verification result), unread indicator.
**Buttons & Actions.** `Mark as Read`, `Mark All as Read`, click-through to the relevant page.
**Navigation.** From bell icon in the top bar, available on every page.
**Validation.** N/A.
**Permissions.** Self only.

**API Calls.** `GET /api/notifications`, `PUT /api/notifications/{id}/read`

## 4.34 Support / Help Center

**Purpose.** Self-service documentation and support-ticket submission.
**Primary Users.** All authenticated roles.
**Key UI Elements.** Searchable knowledge-base articles, `Contact Support` form, existing-ticket status list.
**Buttons & Actions.** `Submit Ticket`, `View Ticket`.
**Navigation.** From sidebar, always available.
**Validation.** Ticket description required.
**Permissions.** All roles; ticket visibility scoped to the submitting user and Org Admins.

**API Calls.** `GET /api/support/articles`, `POST /api/support/tickets`

## 4.35 Profile / Account Settings

**Purpose.** Manage the logged-in user's own account.
**Primary Users.** All authenticated roles.
**Key UI Elements.** Name, email, password change, 2FA setup, active-sessions list, workspace list (for users in multiple organizations).
**Buttons & Actions.** `Save Changes`, `Enable 2FA`, `Log Out of All Devices`, `Switch Workspace`.
**Navigation.** From top-right account menu, available on every page.
**Validation.** Password change requires current password; email change requires re-verification.
**Permissions.** Self only.

**API Calls.** `GET /api/auth/me`, `PUT /api/users/me`, `POST /api/auth/2fa/enable`

\newpage

# 5. Backend Flow

Pages describe what the user sees. This section describes what the system does. MailAlly has three core backend flows: **Campaign Execution**, **Contact Import**, and **Tracking & Webhook Ingestion**.

## 5.1 Campaign Execution Flow

```
Campaign Creation  (Campaign Wizard submits draft)
        |
        v
Validation          (template valid, audience non-empty, domain verified,
                      approval obtained if above threshold)
        |
        v
Queue                (campaign accepted; contact list resolved from
                      segment/filter into a fixed recipient set;
                      suppressed/unsubscribed/invalid contacts excluded)
        |
        v
Scheduler            (immediate dispatch, or held until scheduled time /
                      recurring trigger fires)
        |
        v
Batching             (recipient set split into fixed-size batches;
                      each batch becomes an independently trackable unit)
        |
        v
Worker(s)            (each batch processed by a worker: render template
                      per-recipient with personalization variables,
                      inject open-tracking pixel and click-tracking links)
        |
        v
Provider Send        (dispatched via the organization's configured
                      provider — SES / SMTP / third-party relay —
                      respecting the provider's rate limit)
        |
        v
Delivery Outcome     (per-recipient result recorded: sent, failed,
                      bounced; failures queued for limited automatic retry)
        |
        v
Tracking             (subsequent opens/clicks/bounces/complaints recorded
                      as they occur, independent of the send step)
        |
        v
Analytics            (per-campaign metrics — delivery/open/click/bounce/
                      complaint rates — recomputed as events arrive)
        |
        v
Reports              (scheduled/on-demand reports read from the
                      analytics layer, not recomputed from raw events)
```

**Stage responsibilities.**

| Stage | Responsibility | Failure Handling |
|---|---|---|
| Validation | Reject incomplete/unapproved campaigns before they consume resources | Returns actionable errors to the Campaign Wizard |
| Queue | Freeze the recipient set at launch time | N/A — resolved once per launch |
| Scheduler | Hold campaigns until the correct dispatch time | Missed schedule triggers alert to Org Admin |
| Batching | Make large sends resumable and independently retryable | A failed batch does not block other batches |
| Worker | Render + personalize + inject tracking per recipient | Rendering failure marks that recipient `failed` with a reason, does not stop the batch |
| Provider Send | Respect provider rate limits; abstract provider differences | Provider errors trigger limited automatic retry, then `failed` |
| Tracking | Capture opens/clicks/bounces/complaints asynchronously | Malformed tracking events are logged, not fatal |
| Analytics | Aggregate events into rates and trends | Recomputation is idempotent and can be safely re-run |

## 5.2 Contact Import Flow

```
File Upload
     |
     v
Column Recognition   (auto-map headers to standard fields: email, name,
                      phone, company/college, location, etc.; unmatched
                      columns retained as custom fields)
     |
     v
Row Validation        (email format check; required-field check)
     |
     v
Deduplication          (within the file, then against existing contacts)
     |
     v
Bulk Insert            (valid rows written in batches, not row-by-row)
     |
     v
Summary                (totals: valid / invalid / duplicate; invalid
                      rows downloadable with reasons)
```

## 5.3 Tracking & Webhook Ingestion Flow

```
Recipient opens email / clicks link / provider reports a bounce
     |
     v
Tracking Endpoint or Provider Webhook receives the event
     |
     v
Event validated and matched to its Campaign + Recipient
     |
     v
Event recorded (append-only event log)
     |
     v
Recipient-level delivery status updated
     |
     v
Campaign-level metrics updated
     |
     v
Organization's configured outbound Webhooks notified (if subscribed)
```

\newpage

# 6. Database — Concept Relationships

This section defines **what relates to what**, not table schemas or SQL. It exists so that any database design that follows respects the same product logic described in Sections 1–5.

## 6.1 Core Relationship Chain

```
Organization
     |
     v
Users  (each User belongs to one or more Organizations, with a Role)
     |
     v
Contacts  (owned by an Organization; optionally tagged to an Import)
     |
     v
Segments  (defined over an Organization's Contacts)
     |
     v
Campaigns  (target a Segment or filter; use one Template)
     |
     v
Templates  (owned by an Organization; used by many Campaigns)
     |
     v
Email Jobs  (one per Campaign-Contact pair — the unit of sending)
     |
     v
Analytics  (aggregated from Email Job outcomes and Events)
```

## 6.2 Entity Reference

| Entity | Relates To | Purpose |
|---|---|---|
| **Organization** | Has many Users, Contacts, Segments, Templates, Campaigns, Domains, API Keys | The tenant boundary — everything else belongs to exactly one Organization |
| **User** | Belongs to one or more Organizations, each with one Role | A person (or, for API Users, a technical identity) who can act within an Organization |
| **Role** | Belongs to an Organization (or is a system default) | Defines the permission set a User has within that Organization |
| **Contact** | Belongs to an Organization; optionally linked to an Import batch; can belong to many Segments | A single recipient in the organization's audience |
| **Import** | Belongs to an Organization; produces many Contacts | Record of a single bulk-upload event |
| **Segment** | Belongs to an Organization; references a set of Contacts (by rule or manual membership) | A reusable, named audience definition |
| **Suppression Entry** | Belongs to an Organization; references a Contact email | An address that must never be sent to again, and why |
| **Template** | Belongs to an Organization; used by many Campaigns | Reusable email content (subject, HTML, plain-text) |
| **Campaign** | Belongs to an Organization; uses one Template; targets one Segment/filter; has many Email Jobs | A single send (or scheduled/recurring send) event |
| **Automation Workflow** | Belongs to an Organization; triggers Campaign-like sends on a schedule/event | A multi-step, trigger-based sequence of Template sends |
| **Email Job** | Belongs to one Campaign and one Contact | The atomic unit of "this Contact, in this Campaign, gets this email" — carries delivery status |
| **Event** | Belongs to one Email Job | An individual occurrence (sent, delivered, opened, clicked, bounced, complained, unsubscribed) with a timestamp |
| **Campaign Metric** | Belongs to one Campaign; derived from its Email Jobs/Events | Pre-aggregated rates (delivery, open, click, bounce, complaint) for fast dashboard reads |
| **Domain** | Belongs to an Organization; used by many Campaigns as the sending identity | A verified sending domain and its authentication (SPF/DKIM/DMARC) status |
| **API Key** | Belongs to an Organization (and optionally to one User) | Credential for programmatic access, with defined scopes |
| **Webhook** | Belongs to an Organization | An external URL subscribed to specific Event types |
| **Subscription / Plan** | Belongs to one Organization | The commercial plan in effect, and its limits |
| **Invoice** | Belongs to one Organization's Subscription | A billing record for one period |
| **Audit Log Entry** | References an Organization, a User (actor), and an affected entity | Immutable record of an action taken |
| **Notification** | Belongs to one User | An in-app/email alert generated by system events |

## 6.3 Notes on Relationship Design

- **Organization is the tenant root.** Every other entity (except platform-level entities like Super Admin actions) traces back to exactly one Organization. This is what makes the platform multi-tenant and keeps one customer's data isolated from another's.
- **Email Job is the hinge between Campaigns and Contacts.** It is a many-to-many resolution: one Campaign has many Email Jobs, one Contact has many Email Jobs (across different Campaigns), and each Email Job belongs to exactly one Campaign + one Contact pair.
- **Events are append-only.** A Campaign Metric is a derived, recomputable summary of Events — it should never be the source of truth, only a cache of it.
- **Segments do not copy Contacts.** A dynamic Segment stores a rule, not a frozen list; a Campaign freezes its own recipient list (via Email Jobs) at launch time, so later Contact/Segment changes never alter a Campaign already in progress or completed.
- **Suppression is organization-wide,** not campaign-specific — once an address is suppressed, it is excluded from every future Campaign and Automation Workflow for that Organization.

\newpage

# 7. Development Order

MailAlly should be built in the order below. Each module is only meaningful once the one before it exists — building out of order creates rework (e.g., a Campaign Wizard is meaningless without Contacts and Templates to feed it).

```
1.  Authentication
2.  Organization
3.  Users
4.  Contacts
5.  Segments
6.  Templates
7.  Campaign Wizard
8.  Email Queue
9.  Scheduler
10. Analytics
11. Reports
12. Billing
13. Team Management
14. Settings
15. API
16. AI
```

## 7.1 Rationale Per Phase

| Order | Module | Why It Comes Here |
|---|---|---|
| 1 | Authentication | Nothing else can exist without a way to identify who is acting |
| 2 | Organization | The tenant boundary must exist before any tenant-scoped data can be created |
| 3 | Users | Roles and team membership must exist before permission-gated features are built |
| 4 | Contacts | The audience is the raw material every later module (segments, campaigns, analytics) depends on |
| 5 | Segments | Cannot target an audience meaningfully until Contacts exist to define rules over |
| 6 | Templates | Content must exist before it can be sent |
| 7 | Campaign Wizard | The first point where Contacts + Segments + Templates come together into a sendable unit |
| 8 | Email Queue | Turns a launched Campaign into individually trackable, resumable units of work |
| 9 | Scheduler | Adds time-based control (immediate, scheduled, recurring) on top of a working queue |
| 10 | Analytics | Requires real sending activity (Email Queue) to have data to aggregate |
| 11 | Reports | A packaging layer on top of Analytics — built after Analytics is trustworthy |
| 12 | Billing | Commercial layer; can be stubbed early but only needs to be real once usage is real |
| 13 | Team Management | Deeper role/permission management, built once there's enough of the product to protect |
| 14 | Settings | Domain verification, notification preferences, audit logs — operational polish once the core loop works |
| 15 | API | Exposes the now-stable core product to external integrators |
| 16 | AI | Enhancement layer (content generation, send-time optimization) built last, on top of a proven core |

## 7.2 Grouping Into Milestones

```
Foundation        : Authentication, Organization, Users
Core Product Loop : Contacts, Segments, Templates, Campaign Wizard,
                     Email Queue, Scheduler
Insight Layer     : Analytics, Reports
Commercial Layer  : Billing, Team Management, Settings
Extensibility     : API
Intelligence      : AI
```

A working, demonstrable product exists as soon as the **Core Product Loop** is complete: a user can log in, import contacts, build a segment, write a template, and send a campaign. Everything after that milestone adds depth, not the core value proposition.

\newpage

# Closing Note

This blueprint intentionally stops at product definition. It does not choose a database engine, a backend framework, or a frontend library — that decision belongs to a separate Technical Architecture Document, written *after* this blueprint is agreed on, and written to serve what is defined here.

```
Business Requirements   <- this document
        |
        v
User Experience          <- this document
        |
        v
Backend Architecture     <- next document
        |
        v
API Design                <- next document
        |
        v
Frontend Screens           <- next document
        |
        v
Implementation
```

Any visual template used later for the landing page or dashboard styling is a skin, applied on top of the structure defined here — it should never be allowed to quietly redefine what a page does, what a role can access, or how data relates. If a template suggests a page or interaction not covered above, that suggestion should be brought back into this document and evaluated on its own merits before it's built.
