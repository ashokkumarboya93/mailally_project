import axiosClient from './axiosClient';

export const dashboardApi = {
  getOverview: async () => (await axiosClient.get('/dashboard/overview')).data,
  getKpis: async () => (await axiosClient.get('/dashboard/kpis')).data,
  getCharts: async () => (await axiosClient.get('/dashboard/charts')).data,
  getRecentActivity: async () => (await axiosClient.get('/dashboard/recent-activity')).data,
  getSystemHealth: async () => (await axiosClient.get('/dashboard/system-health')).data,
  getLiveStatus: async () => (await axiosClient.get('/dashboard/live-status')).data,
  getCampaignWidgets: async () => (await axiosClient.get('/dashboard/campaigns')).data,
  getContactWidgets: async () => (await axiosClient.get('/dashboard/contacts')).data,
  getQuickActions: async () => (await axiosClient.get('/dashboard/quick-actions')).data,
  search: async (query) => (await axiosClient.get('/dashboard/search', { params: { q: query } })).data
};
