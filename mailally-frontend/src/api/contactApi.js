import axiosClient from './axiosClient';

export const contactApi = {
  getContacts: async (page = 0, size = 50, search = '') => 
    (await axiosClient.get('/contacts', { params: { page, size, search } })).data,
  
  createContact: async (data) => (await axiosClient.post('/contacts', data)).data,
  
  getContactById: async (id) => (await axiosClient.get(`/contacts/${id}`)).data,
  
  updateContact: async (id, data) => (await axiosClient.put(`/contacts/${id}`, data)).data,
  
  deleteContact: async (id) => (await axiosClient.delete(`/contacts/${id}`)).data,
  
  restoreContact: async (id) => (await axiosClient.post(`/contacts/${id}/restore`)).data,
  
  duplicateContact: async (id) => (await axiosClient.post(`/contacts/${id}/duplicate`)).data,
  
  inlineUpdateField: async (id, fieldName, fieldValue) => 
    (await axiosClient.patch(`/contacts/${id}/field`, { fieldName, fieldValue })).data,

  inlineCellEdit: async (id, fieldName, newValue, isCustomField = false) =>
    (await axiosClient.patch(`/contacts/${id}/cell`, { fieldName, newValue, isCustomField })).data,

  getContactHistory: async (id) => (await axiosClient.get(`/contacts/${id}/history`)).data,

  restoreContactHistory: async (id, historyId) => (await axiosClient.post(`/contacts/${id}/history/${historyId}/restore`)).data,
  
  filterContacts: async (params) => (await axiosClient.get('/contacts/filter', { params })).data,
  
  startImport: async (formData) => 
    (await axiosClient.post('/contacts/import/start', formData, { headers: { 'Content-Type': 'multipart/form-data' } })).data,
  
  getImportProgress: async (batchCode) => (await axiosClient.get(`/contacts/import/progress/${batchCode}`)).data,
  
  getImportHistory: async (page = 0, size = 10) => (await axiosClient.get('/contacts/import/history', { params: { page, size } })).data,
  
  getImportBatchDetails: async (batchId) => (await axiosClient.get(`/contacts/import/${batchId}`)).data,
  
  deleteImportBatch: async (batchId) => (await axiosClient.delete(`/contacts/import/${batchId}`)).data,

  undoImportBatch: async (batchId) => (await axiosClient.post(`/contacts/import-batch/${batchId}/undo`)).data,
  
  getContactTimeline: async (id) => (await axiosClient.get(`/contacts/${id}/timeline`)).data,
  
  getDomainStats: async () => (await axiosClient.get('/contacts/stats/domains')).data,

  getCollections: async () => (await axiosClient.get('/contacts/collections')).data,

  createCollection: async (data) => (await axiosClient.post('/contacts/collections', data)).data,

  getContactsByCollection: async (collectionId, page = 0, size = 50) =>
    (await axiosClient.get(`/contacts/collections/${collectionId}/contacts`, { params: { page, size } })).data,

  deleteCollection: async (collectionId) => (await axiosClient.delete(`/contacts/collections/${collectionId}`)).data,

  getDynamicFields: async () => (await axiosClient.get('/contacts/fields')).data,
  
  executeBulkAction: async (data) => (await axiosClient.post('/contacts/bulk', data)).data,
  
  getTags: async () => (await axiosClient.get('/contacts/tags')).data,
  
  getSavedFilters: async () => (await axiosClient.get('/contacts/filters/saved')).data,
  
  createSavedFilter: async (name, filterJson) => (await axiosClient.post('/contacts/filters/saved', { name, filterJson })).data
};
