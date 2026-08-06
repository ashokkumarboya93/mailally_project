import axiosClient from './axiosClient';

export const analyticsApi = {
  getDashboardSummary: async () => (await axiosClient.get('/analytics/dashboard')).data,
  getCampaignAnalytics: async () => (await axiosClient.get('/analytics/campaigns')).data,
  getProviderAnalytics: async () => (await axiosClient.get('/analytics/providers')).data,
  getChartData: async (chartType = 'LINE', metric = 'EMAILS_SENT') => 
    (await axiosClient.get('/analytics/charts', { params: { chartType, metric } })).data
};

export const notificationApi = {
  getNotifications: async (page = 0, size = 10) => (await axiosClient.get('/notifications', { params: { page, size } })).data,
  getUnreadCount: async () => (await axiosClient.get('/notifications/count')).data,
  markAsRead: async (id) => (await axiosClient.patch(`/notifications/${id}/read`)).data,
  markAllAsRead: async () => (await axiosClient.patch('/notifications/read-all')).data
};

export const settingsApi = {
  getSettings: async () => (await axiosClient.get('/settings')).data,
  getCategorySettings: async (category) => (await axiosClient.get(`/settings/category/${category}`)).data,
  updateSetting: async (category, settingKey, settingValue) => 
    (await axiosClient.put('/settings', { category, settingKey, settingValue })).data,
  resetSettings: async () => (await axiosClient.post('/settings/reset')).data
};

export const billingApi = {
  getHistory: async (page = 0, size = 10) => (await axiosClient.get('/billing/history', { params: { page, size } })).data,
  getSummary: async () => (await axiosClient.get('/billing/summary')).data,
  getStatistics: async () => (await axiosClient.get('/billing/statistics')).data,
  createInvoice: async (data) => (await axiosClient.post('/billing/invoice', data)).data,
  recordPayment: async (data) => (await axiosClient.post('/billing/payment', data)).data
};

export const subscriptionApi = {
  getSubscription: async () => (await axiosClient.get('/subscriptions')).data,
  upgradePlan: async (planCode) => (await axiosClient.post('/subscriptions/upgrade', { planCode })).data,
  checkQuota: async (feature = 'EMAILS') => (await axiosClient.get('/subscriptions/quota-check', { params: { feature } })).data
};

export const auditApi = {
  getAuditLogs: async (page = 0, size = 10) => (await axiosClient.get('/audit', { params: { page, size } })).data,
  searchLogs: async (query) => (await axiosClient.get('/audit/search', { params: { query } })).data
};

export const aiApi = {
  generateSubject: async (prompt, tone = 'PROFESSIONAL') => (await axiosClient.post('/ai/generate-subject', { prompt, tone })).data,
  generateContent: async (prompt, tone = 'PROFESSIONAL') => (await axiosClient.post('/ai/generate-content', { prompt, tone })).data,
  rewriteEmail: async (prompt, tone = 'PROFESSIONAL') => (await axiosClient.post('/ai/rewrite', { prompt, tone })).data,
  grammarFix: async (prompt) => (await axiosClient.post('/ai/grammar-fix', { prompt })).data,
  spamScore: async (prompt) => (await axiosClient.post('/ai/spam-score', { prompt })).data,
  campaignIdeas: async (prompt) => (await axiosClient.post('/ai/campaign-ideas', { prompt })).data,
  getUsage: async () => (await axiosClient.get('/ai/usage')).data
};
