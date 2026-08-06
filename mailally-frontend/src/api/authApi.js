import axiosClient from './axiosClient';

export const authApi = {
  login: async (email, password) => {
    const response = await axiosClient.post('/auth/login', { email, password });
    return response.data;
  },
  register: async (userData) => {
    const response = await axiosClient.post('/auth/register', userData);
    return response.data;
  },
  getProfile: async () => {
    const response = await axiosClient.get('/auth/me');
    return response.data;
  }
};
