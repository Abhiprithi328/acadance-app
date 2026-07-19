import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Signup from './pages/Signup';
import Login from './pages/Login';
import Setup from './pages/Setup';
import Dashboard from './pages/Dashboard';
import MarkAttendance from './pages/MarkAttendance';
import Settings from './pages/Settings';
import Simulation from './pages/Simulation';

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Navigate to="/signup" replace />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/login" element={<Login />} />
          <Route
            path="/setup"
            element={
              <ProtectedRoute>
                <Setup />
              </ProtectedRoute>
            }
          />
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/mark-attendance"
            element={
              <ProtectedRoute>
                <MarkAttendance />
              </ProtectedRoute>
            }
          />
          <Route
            path="/settings"
            element={
              <ProtectedRoute>
                <Settings />
              </ProtectedRoute>
            }
          />
          <Route
            path="/simulation"
            element={
              <ProtectedRoute>
                <Simulation />
              </ProtectedRoute>
            }
          />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
