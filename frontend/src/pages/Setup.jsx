import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

const DAYS = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];

export default function Setup() {
  const navigate = useNavigate();
  const [step, setStep] = useState(1);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  // Step 1
  const [requirement, setRequirement] = useState(75);

  // Step 2
  const [workingDays, setWorkingDays] = useState(['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY']);

  // Step 3
  const [subjectInput, setSubjectInput] = useState('');
  const [subjects, setSubjects] = useState([]); // { id, name } once saved to backend

  // Step 4: timetable - { "MONDAY-1": subjectId, ... }
  const [periodsPerDay, setPeriodsPerDay] = useState(6);
  const [timetable, setTimetable] = useState({});

  function toggleDay(day) {
    setWorkingDays((prev) =>
      prev.includes(day) ? prev.filter((d) => d !== day) : [...prev, day]
    );
  }

  async function addSubject() {
    if (!subjectInput.trim()) return;
    try {
      const res = await api.post('/api/subjects', { name: subjectInput.trim() });
      setSubjects((prev) => [...prev, res.data]);
      setSubjectInput('');
    } catch {
      setError('Could not add subject. Try again.');
    }
  }

  function removeSubjectLocally(id) {
    setSubjects((prev) => prev.filter((s) => s.id !== id));
  }

  function setSlot(day, period, subjectId) {
    setTimetable((prev) => ({ ...prev, [`${day}-${period}`]: subjectId || null }));
  }

  async function finishSetup() {
    setError('');
    setSaving(true);
    try {
      // 1. Save attendance requirement
      await api.put('/api/settings', { attendanceRequirement: Number(requirement) });

      // 2. Build and save the full timetable
      const entries = [];
      workingDays.forEach((day) => {
        for (let p = 1; p <= periodsPerDay; p++) {
          const subjectId = timetable[`${day}-${p}`];
          if (subjectId) {
            entries.push({ dayOfWeek: day, periodNumber: p, subjectId: Number(subjectId) });
          }
        }
      });
      await api.post('/api/timetable', { entries });

      navigate('/dashboard');
    } catch (err) {
      setError('Something went wrong saving your setup. You can also finish this later in Settings.');
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="min-h-screen bg-slate-50 p-6">
      <div className="max-w-2xl mx-auto bg-white rounded-2xl shadow-lg p-8">
        <div className="flex gap-2 mb-6">
          {[1, 2, 3, 4].map((s) => (
            <div key={s} className={`h-1.5 flex-1 rounded-full ${s <= step ? 'bg-indigo-600' : 'bg-slate-200'}`} />
          ))}
        </div>

        {step === 1 && (
          <div>
            <h2 className="text-xl font-bold text-slate-800 mb-1">Attendance requirement</h2>
            <p className="text-slate-500 text-sm mb-6">What % do you need to maintain? (Most colleges use 75%.)</p>
            <input
              type="number"
              min="0"
              max="100"
              value={requirement}
              onChange={(e) => setRequirement(e.target.value)}
              className="w-full border border-slate-300 rounded-lg px-3 py-2 mb-6 focus:outline-none focus:ring-2 focus:ring-indigo-400"
            />
            <button onClick={() => setStep(2)} className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-2 rounded-lg">
              Next
            </button>
          </div>
        )}

        {step === 2 && (
          <div>
            <h2 className="text-xl font-bold text-slate-800 mb-1">Working days</h2>
            <p className="text-slate-500 text-sm mb-6">Which days do you have classes?</p>
            <div className="grid grid-cols-2 gap-2 mb-6">
              {DAYS.map((day) => (
                <button
                  key={day}
                  onClick={() => toggleDay(day)}
                  className={`py-2 rounded-lg text-sm font-medium border transition ${
                    workingDays.includes(day)
                      ? 'bg-indigo-600 text-white border-indigo-600'
                      : 'bg-white text-slate-600 border-slate-300'
                  }`}
                >
                  {day.charAt(0) + day.slice(1).toLowerCase()}
                </button>
              ))}
            </div>
            <div className="flex gap-3">
              <button onClick={() => setStep(1)} className="flex-1 border border-slate-300 text-slate-600 font-medium py-2 rounded-lg">
                Back
              </button>
              <button onClick={() => setStep(3)} className="flex-1 bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-2 rounded-lg">
                Next
              </button>
            </div>
          </div>
        )}

        {step === 3 && (
          <div>
            <h2 className="text-xl font-bold text-slate-800 mb-1">Your subjects</h2>
            <p className="text-slate-500 text-sm mb-6">Add each subject you're taking this semester.</p>
            <div className="flex gap-2 mb-4">
              <input
                type="text"
                value={subjectInput}
                onChange={(e) => setSubjectInput(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && addSubject()}
                placeholder="e.g. Java"
                className="flex-1 border border-slate-300 rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-400"
              />
              <button onClick={addSubject} className="bg-indigo-600 hover:bg-indigo-700 text-white font-medium px-4 rounded-lg">
                Add
              </button>
            </div>
            <div className="space-y-2 mb-6 max-h-48 overflow-y-auto">
              {subjects.map((s) => (
                <div key={s.id} className="flex justify-between items-center bg-slate-50 rounded-lg px-3 py-2">
                  <span className="text-slate-700">{s.name}</span>
                  <button onClick={() => removeSubjectLocally(s.id)} className="text-slate-400 hover:text-red-500 text-sm">
                    remove
                  </button>
                </div>
              ))}
              {subjects.length === 0 && <p className="text-slate-400 text-sm">No subjects added yet.</p>}
            </div>
            {error && <p className="text-red-600 text-sm mb-4">{error}</p>}
            <div className="flex gap-3">
              <button onClick={() => setStep(2)} className="flex-1 border border-slate-300 text-slate-600 font-medium py-2 rounded-lg">
                Back
              </button>
              <button
                onClick={() => setStep(4)}
                disabled={subjects.length === 0}
                className="flex-1 bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 text-white font-medium py-2 rounded-lg"
              >
                Next
              </button>
            </div>
          </div>
        )}

        {step === 4 && (
          <div>
            <h2 className="text-xl font-bold text-slate-800 mb-1">Weekly timetable</h2>
            <p className="text-slate-500 text-sm mb-4">
              Assign a subject to each period. Leave blank for a free period/break.
            </p>
            <div className="flex items-center gap-2 mb-4 text-sm">
              <span className="text-slate-600">Periods per day:</span>
              <input
                type="number"
                min="1"
                max="12"
                value={periodsPerDay}
                onChange={(e) => setPeriodsPerDay(Number(e.target.value))}
                className="w-16 border border-slate-300 rounded px-2 py-1"
              />
            </div>

            <div className="overflow-x-auto mb-6">
              <table className="w-full text-sm border-collapse">
                <thead>
                  <tr>
                    <th className="text-left p-2 text-slate-500">Period</th>
                    {workingDays.map((day) => (
                      <th key={day} className="text-left p-2 text-slate-500">{day.slice(0, 3)}</th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  {Array.from({ length: periodsPerDay }, (_, i) => i + 1).map((period) => (
                    <tr key={period} className="border-t border-slate-100">
                      <td className="p-2 font-medium text-slate-600">{period}</td>
                      {workingDays.map((day) => (
                        <td key={day} className="p-1">
                          <select
                            value={timetable[`${day}-${period}`] || ''}
                            onChange={(e) => setSlot(day, period, e.target.value)}
                            className="w-full border border-slate-300 rounded px-1 py-1 text-xs"
                          >
                            <option value="">—</option>
                            {subjects.map((s) => (
                              <option key={s.id} value={s.id}>{s.name}</option>
                            ))}
                          </select>
                        </td>
                      ))}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {error && <p className="text-red-600 text-sm mb-4">{error}</p>}
            <div className="flex gap-3">
              <button onClick={() => setStep(3)} className="flex-1 border border-slate-300 text-slate-600 font-medium py-2 rounded-lg">
                Back
              </button>
              <button
                onClick={finishSetup}
                disabled={saving}
                className="flex-1 bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 text-white font-medium py-2 rounded-lg"
              >
                {saving ? 'Saving...' : 'Finish Setup'}
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
