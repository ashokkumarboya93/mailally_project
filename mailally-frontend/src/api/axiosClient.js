import axios from 'axios';

const axiosClient = axios.create({
  baseURL: 'http://localhost:8081/api/v1',
  headers: {
    'Content-Type': 'application/json'
  }
});

// Request Interceptor: Attach Bearer JWT Token
axiosClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('mailally_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response Interceptor: Handle Unauthenticated 401 Session Expiration
axiosClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('mailally_token');
      localStorage.removeItem('mailally_user');
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default axiosClient;
