import axios from 'axios';
import { API_BASE_URL } from '../config';

const api = axios.create({
  baseURL: API_BASE_URL,
});

// Attach the saved token (if any) to every outgoing request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('acadance_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
