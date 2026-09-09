import React, { useState, useEffect, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import { schedulerApi, campaignApi } from '../../api/campaignApi';
import { StatusBadge } from '../../components/common/StatusBadge';
import { Modal } from '../../components/common/Modal';
import { AlertModal } from '../../components/common/AlertModal';
import { PageSkeletonLoader } from '../../components/common/PageSkeletonLoader';
import { 
  RefreshCw, Clock, Calendar, Play, Pause, RotateCcw, XCircle, 
  Plus, Search, Filter, CheckCircle2, AlertTriangle, Layers, Send, ChevronRight
} from 'lucide-react';

export const SchedulerPage = () => {
  const navigate = useNavigate();
  const [schedules, setSchedules] = useState([]);
  const [campaigns, setCampaigns] = useState([]);
  const [stats, setStats] = useState({
    totalSchedules: 0,
    activeSchedules: 0,
    upcomingSchedules: 0,
    completedSchedules: 0,
    pausedSchedules: 0,
    failedSchedules: 0
  });
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  // Filters
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');

  // Modals
  const [isScheduleModalOpen, setIsScheduleModalOpen] = useState(false);
  const [rescheduleTarget, setRescheduleTarget] = useState(null); // scheduler object
  const [newRescheduleTime, setNewRescheduleTime] = useState('');
  
  // Schedule Form State
  const [selectedCampaignId, setSelectedCampaignId] = useState('');
  const [scheduleDateTime, setScheduleDateTime] = useState('');

  // Alerts
  const [alertConfig, setAlertConfig] = useState({ isOpen: false, type: 'success', title: '', message: '' });
  const showAlert = (type, message, title = '') => setAlertConfig({ isOpen: true, type, message, title: title || (type === 'success' ? 'Success' : 'Error') });
  const closeAlert = () => setAlertConfig(prev => ({ ...prev, isOpen: false }));

  const loadData = async (isManual = false) => {
    if (isManual) setRefreshing(true);
    else setLoading(true);

    try {
      const [schedRes, statsRes, campRes] = await Promise.allSettled([
        schedulerApi.getSchedules(0, 100),
        schedulerApi.getStats(),
        campaignApi.getCampaigns(0, 100)
      ]);

      if (schedRes.status === 'fulfilled' && schedRes.value?.data?.content) {
        setSchedules(schedRes.value.data.content);
      } else if (schedRes.status === 'fulfilled' && Array.isArray(schedRes.value?.data)) {
        setSchedules(schedRes.value.data);
      } else {
        setSchedules([]);
      }

      if (statsRes.status === 'fulfilled' && statsRes.value?.data) {
        setStats(statsRes.value.data);
      }

      if (campRes.status === 'fulfilled' && campRes.value?.data?.content) {
        setCampaigns(campRes.value.data.content);
      } else if (campRes.status === 'fulfilled' && Array.isArray(campRes.value?.data)) {
        setCampaigns(campRes.value.data);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useEffect(() => {
    loadData();
    // Auto-refresh every 30 seconds for background runner updates
    const interval = setInterval(() => loadData(true), 30000);
    return () => clearInterval(interval);
  }, []);

  // Format relative countdown
  const getNextRunText = (scheduledTimeStr, status) => {
    if (!scheduledTimeStr) return 'Not scheduled';
    if (status === 'COMPLETED') return 'Executed';
    if (status === 'CANCELLED') return 'Cancelled';
    if (status === 'PAUSED') return 'Paused';
    if (status === 'RUNNING') return 'Dispatching now...';

    const target = new Date(scheduledTimeStr);
    const now = new Date();
    const diffMs = target.getTime() - now.getTime();

    if (diffMs < 0) {
      return 'Due for trigger';
    }

    const diffMins = Math.floor(diffMs / (1000 * 60));
    const diffHours = Math.floor(diffMins / 60);
    const diffDays = Math.floor(diffHours / 24);

    if (diffDays > 0) return `In ${diffDays} day${diffDays > 1 ? 's' : ''}`;
    if (diffHours > 0) return `In ${diffHours} hr${diffHours > 1 ? 's' : ''} ${diffMins % 60}m`;
    if (diffMins > 0) return `In ${diffMins} min${diffMins > 1 ? 's' : ''}`;
    return 'In < 1 minute';
  };

  // Actions
  const handleLaunchNow = async (schedId, campaignId) => {
    try {
      await schedulerApi.launchNow(campaignId);
      showAlert('success', 'Immediate campaign dispatch initiated successfully!', 'Dispatched');
      loadData(true);
    } catch (err) {
      showAlert('error', 'Launch failed: ' + (err.response?.data?.message || err.message));
    }
  };

  const handlePause = async (id) => {
    try {
      await schedulerApi.pauseSchedule(id);
      showAlert('success', 'Schedule paused successfully.');
      loadData(true);
    } catch (err) {
      showAlert('error', 'Failed to pause schedule: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleResume = async (id) => {
    try {
      await schedulerApi.resumeSchedule(id);
      showAlert('success', 'Schedule resumed and queued for execution.');
      loadData(true);
    } catch (err) {
      showAlert('error', 'Failed to resume schedule: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleCancel = async (id) => {
    try {
      await schedulerApi.cancelSchedule(id);
      showAlert('success', 'Schedule cancelled successfully.');
      loadData(true);
    } catch (err) {
      showAlert('error', 'Failed to cancel schedule: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleRescheduleSubmit = async (e) => {
    e.preventDefault();
    if (!rescheduleTarget || !newRescheduleTime) return;
    try {
      await schedulerApi.reschedule(rescheduleTarget.id, newRescheduleTime);
      setRescheduleTarget(null);
      setNewRescheduleTime('');
      showAlert('success', 'Campaign rescheduled successfully!');
      loadData(true);
    } catch (err) {
      showAlert('error', 'Failed to reschedule: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleCreateScheduleSubmit = async (e) => {
    e.preventDefault();
    if (!selectedCampaignId || !scheduleDateTime) {
      showAlert('error', 'Please select a campaign and future schedule date & time.');
      return;
    }

    try {
      await schedulerApi.scheduleCampaign({
        campaignId: Number(selectedCampaignId),
        scheduledTime: scheduleDateTime,
        executionType: 'SCHEDULED'
      });
      setIsScheduleModalOpen(false);
      setSelectedCampaignId('');
      setScheduleDateTime('');
      showAlert('success', `Campaign successfully scheduled for ${new Date(scheduleDateTime).toLocaleString()}!`);
      loadData(true);
    } catch (err) {
      showAlert('error', 'Failed to schedule campaign: ' + (err.response?.data?.message || err.message));
    }
  };

  // Filtered schedules
  const filteredSchedules = useMemo(() => {
    return schedules.filter(s => {
      const campName = s.campaignName || (s.campaign && s.campaign.name) || '';
      const matchesSearch = campName.toLowerCase().includes(searchTerm.toLowerCase());
      const matchesStatus = statusFilter === 'ALL' || (s.status && s.status.toUpperCase() === statusFilter);
      return matchesSearch && matchesStatus;
    });
  }, [schedules, searchTerm, statusFilter]);

  if (loading) {
    return <PageSkeletonLoader type="table" />;
  }

  const upcomingCount = schedules.filter(s => s.status === 'SCHEDULED').length;
  const runningCount = schedules.filter(s => s.status === 'RUNNING').length;
  const completedCount = schedules.filter(s => s.status === 'COMPLETED').length;

  return (
    <div className="space-y-6 animate-fadeInUp pb-8 font-sans">
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold tracking-tight text-[#0A0A0B]">Scheduler</h1>
          <p className="text-[13px] text-[#9CA3AF] font-medium mt-1">
            Automated campaign dispatch schedules and background runners.
          </p>
        </div>

        <div className="flex items-center gap-2">
          <button
            onClick={() => loadData(true)}
            className="p-2 rounded-lg border border-[#E5E5E7] bg-white text-[#9CA3AF] hover:text-[#0A0A0B] hover:bg-[#F9FAFB] transition-all cursor-pointer"
            title="Refresh Schedules"
          >
            <RefreshCw className={`w-4 h-4 ${refreshing ? 'animate-spin text-blue-600' : ''}`} />
          </button>

          <button
            onClick={() => setIsScheduleModalOpen(true)}
            className="ma-btn ma-btn-primary gap-1.5 text-[12px]"
          >
            <Plus className="w-4 h-4" /> Schedule Campaign
          </button>
        </div>
      </div>

      {/* KPI Stats Grid */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <div className="bg-white p-4 rounded-[16px] border border-[#E5E5E7] shadow-xs">
          <div className="flex items-center justify-between text-[#9CA3AF]">
            <span className="text-[11px] font-bold uppercase tracking-wider">Total Schedules</span>
            <Clock className="w-4 h-4 text-blue-600" />
          </div>
          <p className="text-2xl font-black text-[#0A0A0B] mt-2">{schedules.length}</p>
          <span className="text-[11px] text-[#9CA3AF] font-medium">All registered jobs</span>
        </div>

        <div className="bg-white p-4 rounded-[16px] border border-[#E5E5E7] shadow-xs">
          <div className="flex items-center justify-between text-[#9CA3AF]">
            <span className="text-[11px] font-bold uppercase tracking-wider">Upcoming & Active</span>
            <Calendar className="w-4 h-4 text-purple-600" />
          </div>
          <p className="text-2xl font-black text-purple-600 mt-2">{upcomingCount + runningCount}</p>
          <span className="text-[11px] text-[#9CA3AF] font-medium">{upcomingCount} queued, {runningCount} active</span>
        </div>

        <div className="bg-white p-4 rounded-[16px] border border-[#E5E5E7] shadow-xs">
          <div className="flex items-center justify-between text-[#9CA3AF]">
            <span className="text-[11px] font-bold uppercase tracking-wider">Completed</span>
            <CheckCircle2 className="w-4 h-4 text-emerald-600" />
          </div>
          <p className="text-2xl font-black text-emerald-600 mt-2">{completedCount}</p>
          <span className="text-[11px] text-[#9CA3AF] font-medium">Dispatched successfully</span>
        </div>

        <div className="bg-white p-4 rounded-[16px] border border-[#E5E5E7] shadow-xs">
          <div className="flex items-center justify-between text-[#9CA3AF]">
            <span className="text-[11px] font-bold uppercase tracking-wider">Runner Status</span>
            <div className="w-2.5 h-2.5 rounded-full bg-emerald-500 animate-ping" />
          </div>
          <p className="text-[15px] font-bold text-[#0A0A0B] mt-2 flex items-center gap-1.5">
            <span className="w-2 h-2 rounded-full bg-emerald-500 inline-block" /> Healthy
          </p>
          <span className="text-[11px] text-[#9CA3AF] font-medium">Cron interval: 30 seconds</span>
        </div>
      </div>

      {/* Controls Bar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 bg-white p-2 rounded-[16px] border border-[#E5E5E7]">
        <div className="flex items-center gap-3 flex-1">
          {/* Search */}
          <div className="relative flex-1 max-w-md">
            <Search className="w-3.5 h-3.5 text-[#C0C5CC] absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              placeholder="Search scheduled campaigns..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-9 pr-3 h-8 text-[12px] font-medium bg-[#FAFAFB] border border-[#E5E5E7] rounded-lg outline-none focus:border-[#D1D5DB]"
            />
          </div>

          {/* Status Filter */}
          <div className="flex items-center gap-1.5 shrink-0">
            <Filter className="w-3.5 h-3.5 text-[#9CA3AF]" />
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="bg-[#FAFAFB] border border-[#E5E5E7] rounded-lg px-2.5 h-8 text-[12px] font-medium text-[#5F6368] outline-none cursor-pointer"
            >
              <option value="ALL">All Statuses</option>
              <option value="SCHEDULED">Scheduled</option>
              <option value="RUNNING">Running</option>
              <option value="COMPLETED">Completed</option>
              <option value="PAUSED">Paused</option>
              <option value="CANCELLED">Cancelled</option>
              <option value="FAILED">Failed</option>
            </select>
          </div>
        </div>
      </div>

      {/* Table */}
      <div className="bg-white rounded-[16px] border border-[#E5E5E7] overflow-hidden shadow-xs">
        <div className="px-5 py-4 border-b border-[#E5E5E7] flex items-center justify-between">
          <div>
            <h3 className="font-bold text-[15px] text-[#0A0A0B]">Scheduled Runners</h3>
            <p className="text-[12px] text-[#9CA3AF] font-medium mt-0.5">Upcoming trigger times & automated background dispatches</p>
          </div>
          <span className="text-[11px] font-semibold text-[#5F6368] bg-[#F3F4F6] px-3 py-1 rounded-full">
            {filteredSchedules.length} Jobs
          </span>
        </div>

        {filteredSchedules.length === 0 ? (
          <div className="py-16 text-center space-y-3">
            <Clock className="w-10 h-10 text-[#9CA3AF] mx-auto opacity-40" />
            <h4 className="text-base font-bold text-[#0A0A0B]">No scheduled jobs found</h4>
            <p className="text-[13px] text-[#9CA3AF] max-w-sm mx-auto font-medium">
              {searchTerm || statusFilter !== 'ALL'
                ? 'No schedules match your filter criteria.'
                : 'Schedule a campaign to automate outreach dispatch at specific dates and times.'}
            </p>
            <button
              onClick={() => setIsScheduleModalOpen(true)}
              className="ma-btn ma-btn-primary text-[12px] mt-2"
            >
              + Schedule Campaign
            </button>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse table-fixed">
              <thead>
                <tr className="bg-[#FAFAFB] border-b border-[#E5E5E7] text-[11px] font-semibold uppercase tracking-wider text-[#9CA3AF]">
                  <th className="py-3 px-5 w-[30%]">Campaign</th>
                  <th className="py-3 px-4 w-[20%]">Scheduled Time</th>
                  <th className="py-3 px-3 w-[12%]">Status</th>
                  <th className="py-3 px-4 w-[16%]">Next Run</th>
                  <th className="py-3 px-5 w-[22%] text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[#F0F0F2] text-[13px]">
                {filteredSchedules.map((j) => {
                  const campName = j.campaignName || (j.campaign && j.campaign.name) || `Campaign #${j.campaignId || j.id}`;
                  const campId = j.campaignId || (j.campaign && j.campaign.id);
                  const scheduledTimeStr = j.scheduledTime || j.scheduledAt;

                  return (
                    <tr key={j.id} className="hover:bg-[#FAFAFB] transition-colors">
                      <td className="py-3.5 px-5 truncate">
                        <div 
                          onClick={() => campId && navigate(`/campaigns/${campId}/analytics`)}
                          className="font-semibold text-[#0A0A0B] hover:text-blue-600 flex items-center gap-1.5 cursor-pointer truncate group"
                        >
                          <span className="truncate">{campName}</span>
                          <ChevronRight className="w-3.5 h-3.5 text-[#9CA3AF] opacity-0 group-hover:opacity-100 transition-opacity shrink-0" />
                        </div>
                        <div className="text-[#9CA3AF] font-mono text-[11px] mt-0.5">
                          Type: {j.executionType || 'SCHEDULED'}
                        </div>
                      </td>

                      <td className="py-3.5 px-4 text-[#5F6368] font-medium">
                        <div className="flex items-center gap-1.5">
                          <Calendar className="w-3.5 h-3.5 text-purple-600 shrink-0" />
                          <span className="text-[12px]">
                            {scheduledTimeStr ? new Date(scheduledTimeStr).toLocaleString() : 'Immediate'}
                          </span>
                        </div>
                      </td>

                      <td className="py-3.5 px-3">
                        <StatusBadge status={j.status || 'SCHEDULED'} />
                      </td>

                      <td className="py-3.5 px-4 font-semibold text-[12px] text-[#0A0A0B]">
                        {getNextRunText(scheduledTimeStr, j.status)}
                      </td>

                      <td className="py-3.5 px-5 text-right">
                        <div className="flex items-center justify-end gap-1.5">
                          {/* Run Now Button */}
                          {j.status !== 'COMPLETED' && j.status !== 'RUNNING' && campId && (
                            <button
                              onClick={() => handleLaunchNow(j.id, campId)}
                              className="px-2 py-1 text-[11px] font-semibold bg-[#0A0A0B] text-white rounded-md flex items-center gap-1 hover:bg-slate-800 cursor-pointer"
                              title="Trigger immediate dispatch"
                            >
                              <Play className="w-3 h-3 fill-current" /> Run Now
                            </button>
                          )}

                          {/* Pause / Resume */}
                          {j.status === 'SCHEDULED' && (
                            <button
                              onClick={() => handlePause(j.id)}
                              className="p-1.5 text-[#5F6368] hover:text-[#0A0A0B] hover:bg-[#F3F4F6] rounded-md cursor-pointer transition-colors"
                              title="Pause Schedule"
                            >
                              <Pause className="w-3.5 h-3.5" />
                            </button>
                          )}

                          {j.status === 'PAUSED' && (
                            <button
                              onClick={() => handleResume(j.id)}
                              className="p-1.5 text-emerald-600 hover:bg-emerald-50 rounded-md cursor-pointer transition-colors"
                              title="Resume Schedule"
                            >
                              <Play className="w-3.5 h-3.5" />
                            </button>
                          )}

                          {/* Reschedule */}
                          {j.status !== 'COMPLETED' && (
                            <button
                              onClick={() => {
                                setRescheduleTarget(j);
                                setNewRescheduleTime(scheduledTimeStr ? scheduledTimeStr.slice(0, 16) : '');
                              }}
                              className="p-1.5 text-purple-600 hover:bg-purple-50 rounded-md cursor-pointer transition-colors"
                              title="Reschedule Date & Time"
                            >
                              <RotateCcw className="w-3.5 h-3.5" />
                            </button>
                          )}

                          {/* Cancel */}
                          {j.status !== 'COMPLETED' && j.status !== 'CANCELLED' && (
                            <button
                              onClick={() => handleCancel(j.id)}
                              className="p-1.5 text-[#9CA3AF] hover:text-[#E11D48] hover:bg-rose-50 rounded-md cursor-pointer transition-colors"
                              title="Cancel Schedule"
                            >
                              <XCircle className="w-3.5 h-3.5" />
                            </button>
                          )}
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {/* Schedule Campaign Modal */}
      <Modal isOpen={isScheduleModalOpen} onClose={() => setIsScheduleModalOpen(false)} title="Schedule Campaign Dispatch">
        <form onSubmit={handleCreateScheduleSubmit} className="space-y-4">
          <div>
            <label className="ma-label">Select Campaign *</label>
            <select
              required
              value={selectedCampaignId}
              onChange={e => setSelectedCampaignId(e.target.value)}
              className="ma-select"
            >
              <option value="">Choose Campaign...</option>
              {campaigns.map(c => (
                <option key={c.id} value={c.id}>
                  {c.name} ({c.status || 'DRAFT'} - {c.totalRecipients || 0} contacts)
                </option>
              ))}
            </select>
            <p className="text-[11px] text-[#9CA3AF] mt-1">
              Select an existing campaign to schedule for automated dispatch.
            </p>
          </div>

          <div className="p-3 bg-purple-50/50 rounded-xl border border-purple-200 space-y-1.5">
            <label className="ma-label flex items-center gap-1.5 text-purple-900 font-bold text-[12px]">
              <Calendar className="w-3.5 h-3.5 text-purple-600" />
              Target Date & Time *
            </label>
            <input
              type="datetime-local"
              required
              value={scheduleDateTime}
              onChange={e => setScheduleDateTime(e.target.value)}
              min={new Date().toISOString().slice(0, 16)}
              className="ma-input bg-white border-purple-300 focus:border-purple-600"
            />
          </div>

          <button
            type="submit"
            className="ma-btn ma-btn-primary w-full gap-1.5"
          >
            <Clock className="w-4 h-4" /> Confirm Schedule
          </button>
        </form>
      </Modal>

      {/* Reschedule Modal */}
      <Modal isOpen={Boolean(rescheduleTarget)} onClose={() => setRescheduleTarget(null)} title="Reschedule Campaign Dispatch">
        <form onSubmit={handleRescheduleSubmit} className="space-y-4">
          <div>
            <p className="text-[13px] text-[#5F6368]">
              Rescheduling: <strong className="text-[#0A0A0B]">{rescheduleTarget?.campaignName || 'Campaign'}</strong>
            </p>
          </div>

          <div className="p-3 bg-purple-50/50 rounded-xl border border-purple-200 space-y-1.5">
            <label className="ma-label flex items-center gap-1.5 text-purple-900 font-bold text-[12px]">
              <Calendar className="w-3.5 h-3.5 text-purple-600" />
              New Launch Date & Time *
            </label>
            <input
              type="datetime-local"
              required
              value={newRescheduleTime}
              onChange={e => setNewRescheduleTime(e.target.value)}
              min={new Date().toISOString().slice(0, 16)}
              className="ma-input bg-white border-purple-300 focus:border-purple-600"
            />
          </div>

          <div className="flex items-center gap-3 pt-2">
            <button
              type="button"
              onClick={() => setRescheduleTarget(null)}
              className="ma-btn ma-btn-secondary flex-1"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="ma-btn ma-btn-primary flex-1 gap-1.5"
            >
              <RotateCcw className="w-3.5 h-3.5" /> Update Schedule
            </button>
          </div>
        </form>
      </Modal>

      {/* Alert Status Modal */}
      <AlertModal
        isOpen={alertConfig.isOpen}
        onClose={closeAlert}
        type={alertConfig.type}
        title={alertConfig.title}
        message={alertConfig.message}
      />

    </div>
  );
};
