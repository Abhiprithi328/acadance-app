import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';

export default function Settings() {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [requirement, setRequirement] = useState(user?.attendanceRequirement || 75);
  const [subjects, setSubjects] = useState([]);
  const [newSubject, setNewSubject] = useState('');
  const [saved, setSaved] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    loadSubjects();
  }, []);

  async function loadSubjects() {
    try {
      const res = await api.get('/api/subjects');
      setSubjects(res.data);
    } catch {
      setError('Could not load subjects.');
    }
  }

  async function saveRequirement() {
    setError('');
    try {
      await api.put('/api/settings', { attendanceRequirement: Number(requirement) });
      setSaved(true);
      setTimeout(() => setSaved(false), 2000);
    } catch {
      setError('Could not save. Try again.');
    }
  }

  async function addSubject() {
    if (!newSubject.trim()) return;
    try {
      const res = await api.post('/api/subjects', { name: newSubject.trim() });
      setSubjects((prev) => [...prev, res.data]);
      setNewSubject('');
    } catch {
      setError('Could not add subject.');
    }
  }

  async function deleteSubject(id) {
    try {
      await api.delete(`/api/subjects/${id}`);
      setSubjects((prev) => prev.filter((s) => s.id !== id));
    } catch {
      setError('Could not delete — it may still be used in your timetable.');
    }
  }

  return (
    <div className="min-h-screen bg-slate-50 p-6">
      <div className="max-w-lg mx-auto space-y-6">
        <div className="flex justify-between items-center">
          <h1 className="text-2xl font-bold text-slate-800">Settings</h1>
          <button onClick={() => navigate('/dashboard')} className="text-sm text-indigo-600">← Back</button>
        </div>

        <div className="bg-white rounded-2xl shadow-lg p-6">
          <h2 className="font-semibold text-slate-800 mb-3">Attendance requirement</h2>
          <div className="flex gap-2">
            <input
              type="number"
              min="0"
              max="100"
              value={requirement}
              onChange={(e) => setRequirement(e.target.value)}
              className="flex-1 border border-slate-300 rounded-lg px-3 py-2"
            />
            <button onClick={saveRequirement} className="bg-indigo-600 hover:bg-indigo-700 text-white font-medium px-4 rounded-lg">
              Save
            </button>
          </div>
          {saved && <p className="text-green-600 text-sm mt-2">Saved!</p>}
        </div>

        <div className="bg-white rounded-2xl shadow-lg p-6">
          <h2 className="font-semibold text-slate-800 mb-3">Subjects</h2>
          <div className="flex gap-2 mb-4">
            <input
              type="text"
              value={newSubject}
              onChange={(e) => setNewSubject(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && addSubject()}
              placeholder="Add a subject"
              className="flex-1 border border-slate-300 rounded-lg px-3 py-2"
            />
            <button onClick={addSubject} className="bg-indigo-600 hover:bg-indigo-700 text-white font-medium px-4 rounded-lg">
              Add
            </button>
          </div>
          <ul className="space-y-2">
            {subjects.map((s) => (
              <li key={s.id} className="flex justify-between items-center bg-slate-50 rounded-lg px-3 py-2">
                <span className="text-slate-700">{s.name}</span>
                <button onClick={() => deleteSubject(s.id)} className="text-slate-400 hover:text-red-500 text-sm">
                  delete
                </button>
              </li>
            ))}
          </ul>
          <p className="text-xs text-slate-400 mt-3">
            To change your weekly timetable, go through Setup again from the dashboard (timetable editor coming soon here too).
          </p>
        </div>

        {error && <p className="text-red-600 text-sm">{error}</p>}

        <button onClick={logout} className="w-full border border-red-200 text-red-600 font-medium py-2 rounded-lg">
          Log out
        </button>
      </div>
    </div>
  );
}
