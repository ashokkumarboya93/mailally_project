import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { MainLayout } from '../components/layout/MainLayout';
import { LoginPage } from '../pages/auth/LoginPage';
import { RegisterPage } from '../pages/auth/RegisterPage';
import { AgencyLandingPage } from '../pages/AgencyLandingPage';
import { ExecutiveDashboardPage } from '../pages/dashboard/ExecutiveDashboardPage';
import { ContactsPage } from '../pages/contacts/ContactsPage';
import { TemplatesPage } from '../pages/templates/TemplatesPage';
import { CampaignsPage } from '../pages/campaigns/CampaignsPage';
import { CampaignWizardPage } from '../pages/campaigns/CampaignWizardPage';
import { CampaignAnalyticsPage } from '../pages/campaigns/CampaignAnalyticsPage';
import { SchedulerPage } from '../pages/scheduler/SchedulerPage';
import { AnalyticsPage } from '../pages/analytics/AnalyticsPage';
import { NotificationsPage } from '../pages/notifications/NotificationsPage';
import { SettingsPage } from '../pages/settings/SettingsPage';
import { BillingPage } from '../pages/billing/BillingPage';
import { SubscriptionsPage } from '../pages/subscriptions/SubscriptionsPage';
import { AuditLogsPage } from '../pages/audit/AuditLogsPage';
import { AiAssistantPage } from '../pages/ai/AiAssistantPage';
import { UsersPage } from '../pages/users/UsersPage';
import { NotFoundPage } from '../pages/NotFoundPage';

const ProtectedRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  return children;
};

export const AppRoutes = () => {
  const { isAuthenticated } = useAuth();

  return (
    <Routes>
      {/* Public Showcase & Auth Routes */}
      <Route path="/" element={isAuthenticated ? <Navigate to="/dashboard" replace /> : <AgencyLandingPage />} />
      <Route path="/landing" element={<AgencyLandingPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />

      {/* Protected App Routes */}
      <Route
        path="/*"
        element={
          <ProtectedRoute>
            <MainLayout />
          </ProtectedRoute>
        }
      >
        <Route index element={<Navigate to="/dashboard" replace />} />
        <Route path="dashboard" element={<ExecutiveDashboardPage />} />
        <Route path="contacts" element={<ContactsPage />} />
        <Route path="templates" element={<TemplatesPage />} />
        <Route path="campaigns" element={<CampaignsPage />} />
        <Route path="campaigns/wizard" element={<CampaignWizardPage />} />
        <Route path="campaigns/:id/analytics" element={<CampaignAnalyticsPage />} />
        <Route path="scheduler" element={<SchedulerPage />} />
        <Route path="analytics" element={<AnalyticsPage />} />
        <Route path="notifications" element={<NotificationsPage />} />
        <Route path="settings" element={<SettingsPage />} />
        <Route path="billing" element={<BillingPage />} />
        <Route path="subscriptions" element={<SubscriptionsPage />} />
        <Route path="audit" element={<AuditLogsPage />} />
        <Route path="ai" element={<AiAssistantPage />} />
        <Route path="users" element={<UsersPage />} />
      </Route>

      {/* 404 Route */}
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
};
