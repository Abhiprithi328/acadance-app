import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';

const RISK_STYLES = {
  SAFE: { badge: 'bg-green-100 text-green-700', dot: '🟢' },
  WARNING: { badge: 'bg-amber-100 text-amber-700', dot: '🟡' },
  CRITICAL: { badge: 'bg-red-100 text-red-700', dot: '🔴' },
};

export default function Dashboard() {
  const { user, logout } = useAuth();
  const [stats, setStats] = useState(null);
  const [todayClasses, setTodayClasses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadData();
  }, []);

  async function loadData() {
    setLoading(true);
    setError('');
    try {
      const [statsRes, classesRes] = await Promise.all([
        api.get('/api/attendance/stats'),
        api.get('/api/attendance/classes'),
      ]);
      setStats(statsRes.data);
      setTodayClasses(classesRes.data);
    } catch (err) {
      setError('Could not load your dashboard. Try refreshing.');
    } finally {
      setLoading(false);
    }
  }

  if (loading) {
    return <div className="min-h-screen bg-slate-50 flex items-center justify-center text-slate-500">Loading your dashboard...</div>;
  }

  return (
    <div className="min-h-screen bg-slate-50 p-4 md:p-6">
      <div className="max-w-3xl mx-auto space-y-6">

        <div className="flex justify-between items-center">
          <h1 className="text-2xl font-bold text-slate-800">Hey, {user?.name} 👋</h1>
          <button onClick={logout} className="text-sm text-slate-500 hover:text-red-600">Log out</button>
        </div>

        {error && <p className="text-red-600 text-sm">{error}</p>}

        {stats && (
          <div className="bg-white rounded-2xl shadow-lg p-6 text-center">
            <p className="text-slate-500 text-sm mb-1">Overall Attendance</p>
            <p className="text-5xl font-bold text-slate-800 mb-2">{stats.overallPercentage}%</p>
            <span className={`inline-flex items-center gap-1 px-3 py-1 rounded-full text-sm font-medium ${RISK_STYLES[stats.riskLevel].badge}`}>
              {RISK_STYLES[stats.riskLevel].dot} {stats.riskLevel === 'SAFE' ? 'Safe' : stats.riskLevel === 'WARNING' ? 'Warning' : 'Critical'}
            </span>
            <p className="text-slate-400 text-xs mt-3">
              {stats.totalAttended} attended out of {stats.totalConducted} classes conducted
            </p>
          </div>
        )}

        <div className="grid grid-cols-3 gap-3">
          <Link to="/mark-attendance" className="bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-3 rounded-xl text-center text-sm">
            Mark Attendance
          </Link>
          <Link to="/simulation" className="bg-violet-600 hover:bg-violet-700 text-white font-medium py-3 rounded-xl text-center text-sm">
            Simulate
          </Link>
          <Link to="/settings" className="bg-white border border-slate-200 hover:bg-slate-50 text-slate-700 font-medium py-3 rounded-xl text-center text-sm">
            Settings
          </Link>
        </div>

        <div className="bg-white rounded-2xl shadow-lg p-6">
          <h2 className="font-semibold text-slate-800 mb-3">Today's Classes</h2>
          {todayClasses.length === 0 ? (
            <p className="text-slate-400 text-sm">No classes scheduled today (or it's a holiday).</p>
          ) : (
            <ul className="space-y-2">
              {todayClasses.map((c) => (
                <li key={`${c.subjectId}-${c.periodNumber}`} className="flex justify-between items-center bg-slate-50 rounded-lg px-3 py-2 text-sm">
                  <span className="text-slate-700">Period {c.periodNumber} — {c.subjectName}</span>
                  {c.status ? (
                    <span className={c.status === 'PRESENT' ? 'text-green-600 font-medium' : 'text-red-600 font-medium'}>
                      {c.status === 'PRESENT' ? 'Present' : 'Absent'}
                    </span>
                  ) : (
                    <span className="text-slate-400">Not marked</span>
                  )}
                </li>
              ))}
            </ul>
          )}
        </div>

        {stats && stats.subjectStats.length > 0 && (
          <div className="bg-white rounded-2xl shadow-lg p-6">
            <h2 className="font-semibold text-slate-800 mb-3">Subject-wise Attendance</h2>
            <ul className="space-y-2">
              {stats.subjectStats.map((s) => (
                <li key={s.subjectId} className="flex justify-between items-center bg-slate-50 rounded-lg px-3 py-2 text-sm">
                  <span className="text-slate-700">{RISK_STYLES[s.riskLevel].dot} {s.subjectName}</span>
                  <span className="font-medium text-slate-800">{s.percentage}%</span>
                </li>
              ))}
            </ul>
          </div>
        )}
      </div>
    </div>
  );
}
