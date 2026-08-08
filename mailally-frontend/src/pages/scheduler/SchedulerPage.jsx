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
    <div className="space-y-6 animate-fadeInUp pb-8">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold tracking-tight text-[#0A0A0B]">Scheduler</h1>
          <p className="text-[13px] text-[#9CA3AF] font-medium mt-1">
            Automated campaign dispatch schedules and background runners.
          </p>
        </div>
        <button
          onClick={loadSchedules}
          className="p-2 rounded-lg border border-[#E5E5E7] bg-white text-[#9CA3AF] hover:text-[#0A0A0B] hover:bg-[#F9FAFB] transition-all cursor-pointer"
          title="Refresh"
        >
          <RefreshCw className={`w-4 h-4 ${loading ? 'animate-spin' : ''}`} />
        </button>
      </div>

      {/* Table */}
      <div className="bg-white rounded-[16px] border border-[#E5E5E7] overflow-hidden">
        <div className="px-5 py-4 border-b border-[#E5E5E7] flex items-center justify-between">
          <div>
            <h3 className="font-bold text-[15px] text-[#0A0A0B]">Scheduled Runners</h3>
            <p className="text-[12px] text-[#9CA3AF] font-medium mt-0.5">Upcoming trigger times</p>
          </div>
          <span className="text-[11px] font-semibold text-[#5F6368] bg-[#F3F4F6] px-3 py-1 rounded-full">
            {jobs.length} Active
          </span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="border-b border-[#F0F0F2]">
                <th className="text-[11px] font-semibold uppercase tracking-[0.05em] text-[#9CA3AF] p-4 px-5">Campaign</th>
                <th className="text-[11px] font-semibold uppercase tracking-[0.05em] text-[#9CA3AF] p-4">Scheduled Time</th>
                <th className="text-[11px] font-semibold uppercase tracking-[0.05em] text-[#9CA3AF] p-4">Status</th>
                <th className="text-[11px] font-semibold uppercase tracking-[0.05em] text-[#9CA3AF] p-4">Next Run</th>
              </tr>
            </thead>
            <tbody>
              {jobs.map((j) => (
                <tr key={j.id} className="border-b border-[#F0F0F2] last:border-0 hover:bg-[#FAFAFB] transition-colors">
                  <td className="p-4 px-5 text-[13px] font-semibold text-[#0A0A0B]">
                    {j.campaignName}
                  </td>
                  <td className="p-4 text-[13px] font-medium text-[#5F6368]">
                    <div className="flex items-center gap-1.5">
                      <Calendar className="w-3.5 h-3.5 text-[#9CA3AF]" />
                      <span>{j.scheduledAt}</span>
                    </div>
                  </td>
                  <td className="p-4"><StatusBadge status={j.status} /></td>
                  <td className="p-4 text-[13px] font-medium text-[#9CA3AF]">{j.nextRun || 'Pending'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
