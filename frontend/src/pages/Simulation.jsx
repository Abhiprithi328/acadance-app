import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function Simulation() {
  const navigate = useNavigate();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // hypothetical additions: subjectId -> { present: n, absent: n }
  const [hypothetical, setHypothetical] = useState({});

  useEffect(() => {
    loadStats();
  }, []);

  async function loadStats() {
    try {
      const res = await api.get('/api/attendance/stats');
      setStats(res.data);
    } catch {
      setError('Could not load your current stats.');
    } finally {
      setLoading(false);
    }
  }

  function adjust(subjectId, field, delta) {
    setHypothetical((prev) => {
      const current = prev[subjectId] || { present: 0, absent: 0 };
      const updated = { ...current, [field]: Math.max(0, current[field] + delta) };
      return { ...prev, [subjectId]: updated };
    });
  }

  // Pure client-side math - simulation NEVER calls the backend or saves anything
  function simulate(subject) {
    const h = hypothetical[subject.subjectId] || { present: 0, absent: 0 };
    const conducted = subject.conducted + h.present + h.absent;
    const attended = subject.attended + h.present;
    const percentage = conducted === 0 ? 100 : Math.round((attended / conducted) * 10000) / 100;
    return { conducted, attended, percentage };
  }

  function simulateOverall() {
    let conducted = 0;
    let attended = 0;
    stats.subjectStats.forEach((s) => {
      const sim = simulate(s);
      conducted += sim.conducted;
      attended += sim.attended;
    });
    return conducted === 0 ? 100 : Math.round((attended / conducted) * 10000) / 100;
  }

  if (loading) return <div className="min-h-screen bg-slate-50 flex items-center justify-center text-slate-500">Loading...</div>;

  return (
    <div className="min-h-screen bg-slate-50 p-6">
      <div className="max-w-lg mx-auto space-y-6">
        <div className="flex justify-between items-center">
          <h1 className="text-2xl font-bold text-slate-800">Simulation Mode</h1>
          <button onClick={() => navigate('/dashboard')} className="text-sm text-indigo-600">← Back</button>
        </div>
        <p className="text-slate-500 text-sm -mt-4">
          Add hypothetical future classes below. This never changes your real attendance — it's just a "what if."
        </p>

        {error && <p className="text-red-600 text-sm">{error}</p>}

        {stats && (
          <>
            <div className="bg-white rounded-2xl shadow-lg p-6 text-center">
              <p className="text-slate-500 text-sm mb-1">Current → Simulated Overall</p>
              <div className="flex items-center justify-center gap-3">
                <span className="text-3xl font-bold text-slate-400">{stats.overallPercentage}%</span>
                <span className="text-slate-300">→</span>
                <span className="text-3xl font-bold text-indigo-600">{simulateOverall()}%</span>
              </div>
            </div>

            <div className="space-y-3">
              {stats.subjectStats.map((s) => {
                const sim = simulate(s);
                const h = hypothetical[s.subjectId] || { present: 0, absent: 0 };
                return (
                  <div key={s.subjectId} className="bg-white rounded-2xl shadow-lg p-4">
                    <div className="flex justify-between items-center mb-3">
                      <span className="font-medium text-slate-800">{s.subjectName}</span>
                      <span className="text-sm text-slate-400">
                        {s.percentage}% → <span className="font-semibold text-indigo-600">{sim.percentage}%</span>
                      </span>
                    </div>
                    <div className="flex justify-between text-sm">
                      <div className="flex items-center gap-2">
                        <span className="text-slate-500">Present:</span>
                        <button onClick={() => adjust(s.subjectId, 'present', -1)} className="w-6 h-6 rounded bg-slate-100">−</button>
                        <span className="w-4 text-center">{h.present}</span>
                        <button onClick={() => adjust(s.subjectId, 'present', 1)} className="w-6 h-6 rounded bg-green-100 text-green-700">+</button>
                      </div>
                      <div className="flex items-center gap-2">
                        <span className="text-slate-500">Absent:</span>
                        <button onClick={() => adjust(s.subjectId, 'absent', -1)} className="w-6 h-6 rounded bg-slate-100">−</button>
                        <span className="w-4 text-center">{h.absent}</span>
                        <button onClick={() => adjust(s.subjectId, 'absent', 1)} className="w-6 h-6 rounded bg-red-100 text-red-700">+</button>
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          </>
        )}

        <button
          onClick={() => setHypothetical({})}
          className="w-full border border-slate-300 text-slate-600 font-medium py-2 rounded-lg"
        >
          Reset simulation
        </button>
      </div>
    </div>
  );
}
