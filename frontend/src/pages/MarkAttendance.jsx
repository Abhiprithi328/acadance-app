import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

export default function MarkAttendance() {
  const navigate = useNavigate();
  const [classes, setClasses] = useState([]);
  const [marks, setMarks] = useState({}); // subjectId -> "PRESENT" | "ABSENT"
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const today = new Date().toISOString().split('T')[0];

  useEffect(() => {
    loadClasses();
  }, []);

  async function loadClasses() {
    setLoading(true);
    try {
      const res = await api.get('/api/attendance/classes');
      setClasses(res.data);
      const initial = {};
      res.data.forEach((c) => { if (c.status) initial[c.subjectId] = c.status; });
      setMarks(initial);
    } catch {
      setError('Could not load today\'s classes.');
    } finally {
      setLoading(false);
    }
  }

  function setMark(subjectId, status) {
    setMarks((prev) => ({ ...prev, [subjectId]: status }));
  }

  async function saveAttendance() {
    setSaving(true);
    setError('');
    try {
      const entries = Object.entries(marks).map(([subjectId, status]) => ({
        subjectId: Number(subjectId),
        status,
      }));
      await api.post('/api/attendance/mark', { date: today, entries });
      navigate('/dashboard');
    } catch {
      setError('Could not save attendance. Try again.');
    } finally {
      setSaving(false);
    }
  }

  if (loading) {
    return <div className="min-h-screen bg-slate-50 flex items-center justify-center text-slate-500">Loading...</div>;
  }

  return (
    <div className="min-h-screen bg-slate-50 p-6">
      <div className="max-w-lg mx-auto bg-white rounded-2xl shadow-lg p-8">
        <h1 className="text-2xl font-bold text-slate-800 mb-1">Mark Attendance</h1>
        <p className="text-slate-500 text-sm mb-6">{today}</p>

        {classes.length === 0 ? (
          <p className="text-slate-400">No classes today — nothing to mark (could be a holiday or a day off).</p>
        ) : (
          <div className="space-y-3 mb-6">
            {classes.map((c) => (
              <div key={c.subjectId} className="flex justify-between items-center bg-slate-50 rounded-lg px-4 py-3">
                <span className="text-slate-700 font-medium">{c.subjectName}</span>
                <div className="flex gap-2">
                  <button
                    onClick={() => setMark(c.subjectId, 'PRESENT')}
                    className={`px-3 py-1 rounded-lg text-sm font-medium border ${
                      marks[c.subjectId] === 'PRESENT'
                        ? 'bg-green-600 text-white border-green-600'
                        : 'bg-white text-slate-600 border-slate-300'
                    }`}
                  >
                    Present
                  </button>
                  <button
                    onClick={() => setMark(c.subjectId, 'ABSENT')}
                    className={`px-3 py-1 rounded-lg text-sm font-medium border ${
                      marks[c.subjectId] === 'ABSENT'
                        ? 'bg-red-600 text-white border-red-600'
                        : 'bg-white text-slate-600 border-slate-300'
                    }`}
                  >
                    Absent
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}

        {error && <p className="text-red-600 text-sm mb-4">{error}</p>}

        <div className="flex gap-3">
          <button onClick={() => navigate('/dashboard')} className="flex-1 border border-slate-300 text-slate-600 font-medium py-2 rounded-lg">
            Cancel
          </button>
          <button
            onClick={saveAttendance}
            disabled={saving || classes.length === 0}
            className="flex-1 bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 text-white font-medium py-2 rounded-lg"
          >
            {saving ? 'Saving...' : 'Save Attendance'}
          </button>
        </div>
      </div>
    </div>
  );
}
