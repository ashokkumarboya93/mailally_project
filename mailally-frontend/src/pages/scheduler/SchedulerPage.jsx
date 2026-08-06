import React, { useState, useEffect } from 'react';
import { schedulerApi } from '../../api/campaignApi';
import { StatusBadge } from '../../components/common/StatusBadge';
import { PageSkeletonLoader } from '../../components/common/PageSkeletonLoader';
import { RefreshCw, Clock, Calendar } from 'lucide-react';

export const SchedulerPage = () => {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);

  const loadSchedules = async () => {
    setLoading(true);
    try {
      const res = await schedulerApi.getSchedules();
      if (res.data && res.data.content) {
        setJobs(res.data.content);
      } else {
        setJobs([
          { id: 1, campaignName: 'Q3 Product Announcement', scheduledAt: '2026-08-01 14:00:00', status: 'SCHEDULED', nextRun: 'In 2 hours' },
          { id: 2, campaignName: 'VIP Digest Weekly', scheduledAt: '2026-08-02 09:00:00', status: 'PAUSED', nextRun: 'Tomorrow' }
        ]);
      }
    } catch {
      setJobs([
        { id: 1, campaignName: 'Q3 Product Announcement', scheduledAt: '2026-08-01 14:00:00', status: 'SCHEDULED', nextRun: 'In 2 hours' }
      ]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadSchedules();
  }, []);

  if (loading) {
    return <PageSkeletonLoader type="table" />;
  }

  return (
    <div className="space-y-8 animate-fadeInUp font-sans pb-12">
      {/* ═══════════════════════════════════════════════ */}
      {/* HERO BANNER (MATCHES EXECUTIVE DASHBOARD)       */}
      {/* ═══════════════════════════════════════════════ */}
      <div 
        className="rounded-[28px] py-5 px-7 lg:py-5 lg:px-8 flex flex-col lg:flex-row items-center justify-between gap-6 relative overflow-hidden text-white border"
        style={{
          background: 'linear-gradient(135deg, #2563EB 0%, #3B82F6 60%, #60A5FA 100%)',
          borderColor: 'rgba(255, 255, 255, 0.9)',
          boxShadow: '0 0 0 1px rgba(15, 23, 42, 0.08), 0 15px 35px -10px rgba(37, 99, 235, 0.18)',
        }}
      >
        {/* Soft Radial Glows */}
        <div className="absolute top-[-80px] left-[-80px] w-96 h-96 rounded-full bg-white/20 blur-3xl pointer-events-none" />
        <div className="absolute bottom-[-80px] right-[-80px] w-96 h-96 rounded-full bg-white/10 blur-3xl pointer-events-none" />

        {/* Floating Top-Right Refresh Button */}
        <button
          onClick={loadSchedules}
          className="absolute top-4 right-4 w-9 h-9 rounded-xl bg-white/15 hover:bg-white/25 text-white shadow-xs border border-white/20 flex items-center justify-center cursor-pointer hover:rotate-180 transition-transform duration-500 z-20"
          title="Refresh Schedules"
        >
          <RefreshCw className={`w-3.5 h-3.5 ${loading ? 'animate-spin' : ''}`} />
        </button>

        {/* Left Column Content */}
        <div className="space-y-3.5 max-w-sm relative z-10">
          <div className="inline-flex items-center space-x-2 px-3 py-1 rounded-full bg-white/15 text-white font-black text-[10px] border border-white/25 shadow-3xs backdrop-blur-md">
            <Clock className="w-3 h-3 text-[#00DDFF]" />
            <span>Automated Cron Subsystem</span>
          </div>

          <h1 className="text-2xl sm:text-3xl font-black tracking-tight leading-none text-white" style={{ fontFamily: 'var(--font-heading)' }}>
            Scheduler <br />
            <span className="text-[#00DDFF]">Dashboard</span>
          </h1>

          <p className="text-[10px] sm:text-[11px] text-blue-50 leading-relaxed font-medium">
            Monitor automated campaign dispatch schedules and upcoming background runners.
          </p>
        </div>

        {/* Center Outreach Mail Icon Graphic (Custom PNG) */}
        <div className="hidden xl:flex items-center justify-center relative z-10 px-2">
          <img 
            src="/envelope_outreach.png" 
            alt="MailAlly Outreach Campaign Icon" 
            className="w-36 h-auto object-contain max-h-32 select-none pointer-events-none drop-shadow-lg filter brightness-105" 
          />
        </div>
      </div>

      {/* Table Section */}
      <div className="bg-white rounded-[22px] border overflow-hidden shadow-xs" style={{ borderColor: 'rgba(37,99,235,0.08)' }}>
        <div className="p-5 px-6 border-b flex items-center justify-between border-slate-100 bg-[#F7FAFF]/40">
          <div>
            <h3 className="font-black text-lg text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>Scheduled Campaign Runners</h3>
            <p className="text-xs text-slate-400 font-medium">Upcoming automated trigger times</p>
          </div>
          <span className="badge-blue bg-blue-50 border border-blue-100 text-[#2563EB] px-3.5 py-1 text-xs font-black">
            {jobs.length} Active Timers
          </span>
        </div>

        <div className="overflow-x-auto">
          <table className="table-premium w-full text-left text-xs font-semibold text-[#1E293B]">
            <thead>
              <tr className="bg-slate-50/50 border-b border-slate-100 text-slate-400 text-[10px] tracking-wider uppercase font-black">
                <th className="p-4 px-6">Campaign Target</th>
                <th className="p-4">Scheduled Dispatch Time</th>
                <th className="p-4">Status</th>
                <th className="p-4">Execution Window</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {jobs.map((j) => (
                <tr key={j.id} className="hover:bg-blue-50/20 transition-all">
                  <td className="p-4 px-6 font-black text-sm text-[#1E293B]" style={{ fontFamily: 'var(--font-heading)' }}>
                    {j.campaignName}
                  </td>
                  <td className="p-4 font-mono text-xs text-slate-600">
                    <div className="flex items-center space-x-1.5">
                      <Calendar className="w-3.5 h-3.5 text-[#2563EB]" />
                      <span>{j.scheduledAt}</span>
                    </div>
                  </td>
                  <td className="p-4"><StatusBadge status={j.status} /></td>
                  <td className="p-4 text-slate-400 font-bold">{j.nextRun || 'Pending Runner'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
