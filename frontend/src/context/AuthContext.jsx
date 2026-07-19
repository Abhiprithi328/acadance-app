import { createContext, useContext, useState } from 'react';
import api from '../services/api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('acadance_user');
    return saved ? JSON.parse(saved) : null;
  });

  function saveSession(authResponse) {
    localStorage.setItem('acadance_token', authResponse.token);
    localStorage.setItem('acadance_user', JSON.stringify({
      id: authResponse.userId,
      name: authResponse.name,
      attendanceRequirement: authResponse.attendanceRequirement,
    }));
    setUser({
      id: authResponse.userId,
      name: authResponse.name,
      attendanceRequirement: authResponse.attendanceRequirement,
    });
  }

  async function signup(name, password) {
    const res = await api.post('/api/auth/signup', { name, password });
    saveSession(res.data);
  }

  async function login(name, password) {
    const res = await api.post('/api/auth/login', { name, password });
    saveSession(res.data);
  }

  function logout() {
    localStorage.removeItem('acadance_token');
    localStorage.removeItem('acadance_user');
    setUser(null);
  }

  return (
    <AuthContext.Provider value={{ user, signup, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
