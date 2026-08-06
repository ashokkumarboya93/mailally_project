import axiosClient from './axiosClient';

export const campaignApi = {
  getCampaigns: async (page = 0, size = 10) => (await axiosClient.get('/campaigns', { params: { page, size } })).data,
  getCampaignById: async (id) => (await axiosClient.get(`/campaigns/${id}`)).data,
  createCampaign: async (data) => (await axiosClient.post('/campaigns', data)).data,
  addCollectionToCampaign: async (id, collectionId) => (await axiosClient.post(`/campaigns/${id}/add-collection/${collectionId}`)).data,
  getDiagnostics: async (id) => (await axiosClient.get(`/campaigns/${id}/diagnostics`)).data,
  attachTemplate: async (id, templateId) => (await axiosClient.post(`/campaigns/${id}/attach-template/${templateId}`)).data,
  launchCampaign: async (id) => (await axiosClient.post(`/campaigns/${id}/launch`)).data,
  controlCampaign: async (id, action) => (await axiosClient.post(`/campaigns/${id}/control`, null, { params: { action } })).data,
  getLiveProgress: async (id) => (await axiosClient.get(`/campaigns/${id}/live-progress`)).data,
  getCampaignFailures: async (id) => (await axiosClient.get(`/campaigns/${id}/failures`)).data,
  cancelCampaign: async (id) => (await axiosClient.post(`/campaigns/${id}/control`, null, { params: { action: 'CANCEL' } })).data,
  deleteCampaign: async (id) => (await axiosClient.delete(`/campaigns/${id}`)).data,
  getDeliveryStats: async (id) => (await axiosClient.get(`/emails/stats/${id}`)).data,
  getCampaignLogs: async (id, params = {}) => (await axiosClient.get('/emails/logs', { params: { campaignId: id, ...params } })).data,
  retryFailed: async (id) => (await axiosClient.post(`/emails/retry/${id}`)).data,
};

export const templateApi = {
  getTemplates: async (page = 0, size = 50) => (await axiosClient.get('/templates', { params: { page, size } })).data,
  getTemplateById: async (id) => (await axiosClient.get(`/templates/${id}`)).data,
  createTemplate: async (data) => (await axiosClient.post('/templates', data)).data,
  updateTemplate: async (id, data) => (await axiosClient.put(`/templates/${id}`, data)).data,
  deleteTemplate: async (id) => (await axiosClient.delete(`/templates/${id}`)).data,
  getDynamicVariables: async () => (await axiosClient.get('/templates/dynamic-variables')).data,
  generateAiTemplate: async (data) => (await axiosClient.post('/templates/generate-ai', data)).data,
};

export const segmentApi = {
  getSegments: async (page = 0, size = 50) => (await axiosClient.get('/segments', { params: { page, size } })).data,
  createSegment: async (data) => (await axiosClient.post('/segments', data)).data,
  previewContacts: async (id) => (await axiosClient.get(`/segments/${id}/preview`)).data
};

export const schedulerApi = {
  getSchedules: async (page = 0, size = 10) => (await axiosClient.get('/scheduler', { params: { page, size } })).data,
  pauseSchedule: async (id) => (await axiosClient.patch(`/scheduler/${id}/pause`)).data,
  resumeSchedule: async (id) => (await axiosClient.patch(`/scheduler/${id}/resume`)).data
};
