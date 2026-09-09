import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { 
  ArrowLeft, CheckCircle2, RefreshCw, Play, Pause, XCircle, Activity,
  AlertTriangle, ShieldCheck, Cpu, X, AlertOctagon, BarChart2, Zap
} from 'lucide-react';
import { campaignApi } from '../../api/campaignApi';
import axiosClient from '../../api/axiosClient';
import { AlertModal } from '../../components/common/AlertModal';
import { StatusBadge } from '../../components/common/StatusBadge';
import { formatPercent, normalizeAnalyticsDetail, numberOrZero } from '../../utils/analyticsFormatters';

export const CampaignAnalyticsPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [recipientSearch, setRecipientSearch] = useState('');
  const [recipientStatusFilter, setRecipientStatusFilter] = useState('ALL');

  // Campaign Entity & Live Progress State
  const [campaign, setCampaign] = useState(null);
  const [liveProgress, setLiveProgress] = useState({
    status: 'RUNNING',
    progressPercentage: 0,
    totalRecipients: 0,
    queuedCount: 0,
    sentCount: 0,
    deliveredCount: 0,
    failedCount: 0,
    authFailures: 0,
    connectionFailures: 0,
    invalidRecipientFailures: 0,
    templateFailures: 0,
    providerErrors: 0,
    retryCount: 0,
    emailsPerMinute: 0,
    activeProvider: 'AWS SES',
    workers: [],
    recentActivity: []
  });

  // Diagnostics & Failure Modal State
  const [showFailureModal, setShowFailureModal] = useState(false);
  const [failureDetails, setFailureDetails] = useState([]);
  const [loadingFailures, setLoadingFailures] = useState(false);
  const [alertConfig, setAlertConfig] = useState({ isOpen: false, type: 'error', title: '', message: '' });

  // Analytics Detail State
  const [analyticsDetail, setAnalyticsDetail] = useState({
    hasData: false,
    campaignName: `Campaign #${id}`,
    campaignStatus: 'ACTIVE',
    healthScore: 0.0,
    healthRating: 'NO_DATA',
    healthSummary: 'No campaign engagement events recorded yet.',
    benchmarks: [],
    campaignSummary: { totalRecipients: 0, sent: 0, delivered: 0, failed: 0, queued: 0 },
    deliveryFunnel: { queued: 0, sent: 0, delivered: 0, opened: 0, clicked: 0, sentPct: 0.0, deliveredPct: 0.0, openPct: 0.0, clickPct: 0.0 },
    kpis: { deliveryRate: 0.0, openRate: 0.0, clickRate: 0.0, bounceRate: 0.0 },
    recipientActivities: [],
    liveActivityFeed: []
  });

  const fetchData = async (showSkeleton = true) => {
    if (showSkeleton) setLoading(true);
    else setRefreshing(true);

    try {
      const [campRes, liveRes, analyticsRes] = await Promise.allSettled([
        campaignApi.getCampaignById(id),
        campaignApi.getLiveProgress(id),
        axiosClient.get(`/analytics/v1-dashboard?campaignId=${id}`)
      ]);

      if (campRes.status === 'fulfilled' && campRes.value) {
        const campData = campRes.value.data || campRes.value;
        setCampaign(campData);
      }

      if (liveRes.status === 'fulfilled' && liveRes.value?.data) {
        setLiveProgress(liveRes.value.data);
      }

      if (analyticsRes.status === 'fulfilled' && analyticsRes.value?.data?.data) {
        setAnalyticsDetail(normalizeAnalyticsDetail(analyticsRes.value.data.data, `Campaign #${id}`));
      }
    } catch (err) {
      console.error('Error fetching campaign analytics & telemetry:', err);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  const fetchFailures = async () => {
    setLoadingFailures(true);
    try {
      const res = await campaignApi.getCampaignFailures(id);
      if (res?.data) {
        setFailureDetails(res.data);
      }
    } catch (e) {
      console.error('Failed to load failure details:', e);
    } finally {
      setLoadingFailures(false);
    }
  };

  useEffect(() => {
    fetchData(true);
    const interval = setInterval(() => {
      fetchData(false);
    }, 2500);
    return () => clearInterval(interval);
  }, [id]);

  const handleControlAction = async (action) => {
    try {
      await campaignApi.controlCampaign(id, action);
      fetchData(false);
    } catch (e) {
      setAlertConfig({
        isOpen: true,
        type: 'error',
        title: 'Action Failed',
        message: 'Control action failed: ' + (e.response?.data?.message || e.message)
      });
    }
  };

  const getProgressBarGradient = (status) => {
    const st = (status || 'RUNNING').toUpperCase();
    if (st === 'RUNNING') return 'bg-gradient-to-r from-blue-600 via-indigo-500 via-purple-500 to-emerald-400 animate-pulse';
    if (st === 'COMPLETED') return 'bg-gradient-to-r from-emerald-500 to-teal-400';
    if (st === 'FAILED') return 'bg-gradient-to-r from-rose-600 to-pink-500';
    if (st === 'PAUSED') return 'bg-gradient-to-r from-amber-500 to-orange-400';
    if (st === 'CANCELLED') return 'bg-gradient-to-r from-slate-600 to-slate-400';
    return 'bg-gradient-to-r from-blue-500 to-sky-400';
  };

  const filteredRecipients = (analyticsDetail.recipientActivities || []).filter(r => {
    const matchesSearch = (r.email || '').toLowerCase().includes(recipientSearch.toLowerCase());
    const matchesStatus = recipientStatusFilter === 'ALL' || (r.status || '').toUpperCase() === recipientStatusFilter.toUpperCase();
    return matchesSearch && matchesStatus;
  });

  const totalRecipients = liveProgress.totalRecipients || campaign?.totalRecipients || analyticsDetail.campaignSummary.totalRecipients || 0;
  const sentCount = liveProgress.sentCount || campaign?.sentCount || analyticsDetail.campaignSummary.sent || 0;
  const failedCount = liveProgress.failedCount || campaign?.failedCount || analyticsDetail.campaignSummary.failed || 0;
  const currentStatus = liveProgress.status || campaign?.status || 'RUNNING';
  const progressPct = liveProgress.progressPercentage || (totalRecipients > 0 ? Math.min(100, Math.round(((sentCount + failedCount) / totalRecipients) * 100)) : 0);

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center h-80 space-y-4 font-sans">
        <div className="w-10 h-10 rounded-full border-3 border-[#0A0A0B] border-t-transparent animate-spin" />
        <p className="text-[13px] text-[#9CA3AF] font-semibold">Loading campaign telemetry command center...</p>
      </div>
    );
  }

  return (
    <div className="space-y-6 font-sans pb-10 animate-fadeInUp">
      
      {/* Navigation & Header */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 bg-white p-4 rounded-[20px] border border-[#E5E5E7] shadow-xs">
        <div className="flex items-center gap-3">
          <button
            onClick={() => navigate('/campaigns')}
            className="inline-flex items-center gap-1.5 px-3 py-1.5 text-[12px] font-semibold text-[#5F6368] hover:text-[#0A0A0B] bg-[#FAFAFB] hover:bg-[#F3F4F6] rounded-xl transition-colors border border-[#E5E5E7] cursor-pointer"
          >
            <ArrowLeft className="w-3.5 h-3.5" />
            <span>Campaigns</span>
          </button>

          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-lg font-black text-[#0A0A0B]">{campaign?.name || analyticsDetail.campaignName}</h1>
              <StatusBadge status={currentStatus} />
            </div>
            <p className="text-[11px] text-[#9CA3AF] font-mono">Subject: {campaign?.subject || 'N/A'} • Provider: {liveProgress.activeProvider}</p>
          </div>
        </div>

        {/* Live Control Toolbar */}
        <div className="flex items-center gap-2">
          {failedCount > 0 && (
            <button
              onClick={() => { setShowFailureModal(true); fetchFailures(); }}
              className="px-3 py-1.5 bg-rose-600 hover:bg-rose-700 text-white font-bold text-[12px] rounded-xl flex items-center gap-1.5 transition-colors cursor-pointer"
            >
              <AlertOctagon className="w-3.5 h-3.5" /> Failures ({failedCount})
            </button>
          )}

          {currentStatus === 'PAUSED' ? (
            <button
              onClick={() => handleControlAction('RESUME')}
              className="px-3.5 py-1.5 bg-emerald-600 hover:bg-emerald-700 text-white font-bold text-[12px] rounded-xl flex items-center gap-1.5 transition-colors cursor-pointer"
            >
              <Play className="w-3.5 h-3.5 fill-current" /> Resume
            </button>
          ) : currentStatus === 'RUNNING' ? (
            <button
              onClick={() => handleControlAction('PAUSE')}
              className="px-3.5 py-1.5 bg-amber-600 hover:bg-amber-700 text-white font-bold text-[12px] rounded-xl flex items-center gap-1.5 transition-colors cursor-pointer"
            >
              <Pause className="w-3.5 h-3.5 fill-current" /> Pause
            </button>
          ) : null}

          {currentStatus === 'RUNNING' || currentStatus === 'PAUSED' ? (
            <button
              onClick={() => handleControlAction('CANCEL')}
              className="px-3 py-1.5 bg-rose-50 hover:bg-rose-100 text-rose-700 border border-rose-200 font-bold text-[12px] rounded-xl flex items-center gap-1.5 transition-colors cursor-pointer"
            >
              <XCircle className="w-3.5 h-3.5" /> Cancel
            </button>
          ) : null}

          <button 
            onClick={() => fetchData(false)}
            className="px-3 py-1.5 bg-[#0A0A0B] text-white rounded-xl transition-colors flex items-center gap-1.5 text-[12px] font-semibold cursor-pointer"
            title="Refresh Telemetry"
          >
            <RefreshCw className={`w-3.5 h-3.5 ${refreshing ? 'animate-spin' : ''}`} />
            <span>Refresh</span>
          </button>
        </div>
      </div>

      {/* DYNAMIC PROGRESS BAR SECTION */}
      <div className="bg-white rounded-[20px] p-6 border border-[#E5E5E7] space-y-3 shadow-xs">
        <div className="flex items-center justify-between text-[13px]">
          <div className="flex items-center gap-2">
            <Zap className="w-4 h-4 text-blue-600 animate-pulse" />
            <span className="font-extrabold text-[#0A0A0B]">Live Execution Progress</span>
            <span className="text-[11px] px-2 py-0.5 rounded-full bg-blue-50 text-blue-700 font-bold border border-blue-100">
              {currentStatus}
            </span>
          </div>
          <div className="flex items-center gap-2">
            <span className="text-[12px] text-[#9CA3AF] font-semibold">{sentCount + failedCount} of {totalRecipients} processed</span>
            <span className="font-black text-blue-600 text-base">{progressPct}%</span>
          </div>
        </div>

        <div className="w-full bg-[#F3F4F6] rounded-full h-4 p-0.5 border border-[#E5E5E7] overflow-hidden">
          <div
            className={`h-full rounded-full transition-all duration-700 shadow-xs ${getProgressBarGradient(currentStatus)}`}
            style={{ width: `${progressPct}%` }}
          />
        </div>

        <div className="flex items-center justify-between text-[11px] text-[#9CA3AF] font-semibold pt-1">
          <span>Speed: {liveProgress.emailsPerMinute || 0} emails / min</span>
          <span>{totalRecipients - sentCount - failedCount} remaining</span>
        </div>
      </div>

      {/* MAIN TELEMETRY COUNTERS */}
      <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3">
        <div className="bg-white p-4 rounded-[16px] border border-[#E5E5E7] text-center shadow-xs">
          <span className="text-[11px] text-[#9CA3AF] font-semibold uppercase tracking-wider block">Total Recipients</span>
          <span className="text-xl font-black text-[#0A0A0B] mt-1 block">{totalRecipients}</span>
        </div>
        <div className="bg-sky-50/60 p-4 rounded-[16px] border border-sky-100 text-center shadow-xs">
          <span className="text-[11px] text-sky-700 font-semibold uppercase tracking-wider block">Sent / Accepted</span>
          <span className="text-xl font-black text-sky-700 mt-1 block">{sentCount}</span>
        </div>
        <div className="bg-emerald-50/60 p-4 rounded-[16px] border border-emerald-100 text-center shadow-xs">
          <span className="text-[11px] text-emerald-700 font-semibold uppercase tracking-wider block">Delivered</span>
          <span className="text-xl font-black text-emerald-700 mt-1 block">{liveProgress.deliveredCount || analyticsDetail.deliveryFunnel.delivered || 0}</span>
        </div>
        <div className="bg-rose-50/60 p-4 rounded-[16px] border border-rose-100 text-center shadow-xs cursor-pointer" onClick={() => { setShowFailureModal(true); fetchFailures(); }}>
          <span className="text-[11px] text-rose-700 font-semibold uppercase tracking-wider block">Failed</span>
          <span className="text-xl font-black text-rose-700 mt-1 block">{failedCount}</span>
        </div>
        <div className="bg-amber-50/60 p-4 rounded-[16px] border border-amber-100 text-center shadow-xs">
          <span className="text-[11px] text-amber-700 font-semibold uppercase tracking-wider block">Queued</span>
          <span className="text-xl font-black text-amber-700 mt-1 block">{liveProgress.queuedCount || Math.max(0, totalRecipients - sentCount - failedCount)}</span>
        </div>
        <div className="bg-purple-50/60 p-4 rounded-[16px] border border-purple-100 text-center shadow-xs">
          <span className="text-[11px] text-purple-700 font-semibold uppercase tracking-wider block">Dispatch Rate</span>
          <span className="text-xl font-black text-purple-700 mt-1 block">{liveProgress.emailsPerMinute || 0} /m</span>
        </div>
      </div>

      {/* ENTERPRISE DELIVERY DIAGNOSTICS */}
      <div className="bg-[#0A0A0B] text-white p-5 rounded-[20px] space-y-3 shadow-md border border-slate-800">
        <div className="flex items-center justify-between text-[12px] border-b border-slate-800 pb-2">
          <span className="font-bold tracking-wider text-slate-200 flex items-center gap-2">
            <ShieldCheck className="w-4 h-4 text-emerald-400" /> Delivery Failure Diagnostics & Error Analysis
          </span>
          <span className="text-slate-400 font-mono text-[11px]">Provider: {liveProgress.activeProvider}</span>
        </div>
        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3 pt-1 text-center">
          <div className="bg-slate-900/90 p-3 rounded-xl border border-slate-800">
            <span className="text-[10px] text-slate-400 block font-semibold">Auth Failures</span>
            <span className="text-base font-black text-rose-400 mt-0.5 block">{liveProgress.authFailures || 0}</span>
          </div>
          <div className="bg-slate-900/90 p-3 rounded-xl border border-slate-800">
            <span className="text-[10px] text-slate-400 block font-semibold">Conn Failures</span>
            <span className="text-base font-black text-amber-400 mt-0.5 block">{liveProgress.connectionFailures || 0}</span>
          </div>
          <div className="bg-slate-900/90 p-3 rounded-xl border border-slate-800">
            <span className="text-[10px] text-slate-400 block font-semibold">Invalid Recipient</span>
            <span className="text-base font-black text-purple-400 mt-0.5 block">{liveProgress.invalidRecipientFailures || 0}</span>
          </div>
          <div className="bg-slate-900/90 p-3 rounded-xl border border-slate-800">
            <span className="text-[10px] text-slate-400 block font-semibold">Template Errors</span>
            <span className="text-base font-black text-orange-400 mt-0.5 block">{liveProgress.templateFailures || 0}</span>
          </div>
          <div className="bg-slate-900/90 p-3 rounded-xl border border-slate-800">
            <span className="text-[10px] text-slate-400 block font-semibold">Provider Errors</span>
            <span className="text-base font-black text-rose-400 mt-0.5 block">{liveProgress.providerErrors || 0}</span>
          </div>
          <div className="bg-slate-900/90 p-3 rounded-xl border border-slate-800">
            <span className="text-[10px] text-slate-400 block font-semibold">Total Retries</span>
            <span className="text-base font-black text-sky-400 mt-0.5 block">{liveProgress.retryCount || 0}</span>
          </div>
        </div>
      </div>

      {/* FUNNEL & KEY METRICS */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="bg-white rounded-[20px] p-5 border border-[#E5E5E7] space-y-4 shadow-xs">
          <h3 className="font-bold text-[#5F6368] text-[11px] uppercase tracking-wider">Delivery Funnel</h3>
          <div className="space-y-3 pt-1">
            {[
              { label: 'Queued', val: liveProgress.queuedCount || analyticsDetail.deliveryFunnel.queued, pct: 100, color: 'bg-slate-300' },
              { label: 'Sent', val: sentCount, pct: totalRecipients > 0 ? (sentCount / totalRecipients) * 100 : 0, color: 'bg-sky-500' },
              { label: 'Delivered', val: liveProgress.deliveredCount || analyticsDetail.deliveryFunnel.delivered, pct: analyticsDetail.deliveryFunnel.deliveredPct, color: 'bg-emerald-500' },
              { label: 'Opened', val: analyticsDetail.deliveryFunnel.opened, pct: analyticsDetail.deliveryFunnel.openPct, color: 'bg-pink-500' },
              { label: 'Clicked', val: analyticsDetail.deliveryFunnel.clicked, pct: analyticsDetail.deliveryFunnel.clickPct, color: 'bg-purple-500' },
            ].map((item) => (
              <div key={item.label} className="space-y-1">
                <div className="flex justify-between text-[12px] font-medium text-[#5F6368]">
                  <span>{item.label}</span>
                  <span className="font-bold text-[#0A0A0B]">{numberOrZero(item.val)} ({formatPercent(item.pct)})</span>
                </div>
                <div className="w-full h-2 bg-[#F3F4F6] rounded-full overflow-hidden">
                  <div className={`h-full ${item.color} rounded-full transition-all duration-500`} style={{ width: `${Math.max(numberOrZero(item.pct), 2)}%` }} />
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="bg-white rounded-[20px] p-5 border border-[#E5E5E7] space-y-4 shadow-xs">
          <h3 className="font-bold text-[#5F6368] text-[11px] uppercase tracking-wider">Key Campaign Rates</h3>
          <div className="grid grid-cols-2 gap-3 pt-1">
            <div className="bg-[#FAFAFB] p-4 rounded-2xl border border-[#F0F0F2]">
              <span className="text-[10px] font-bold text-[#9CA3AF] uppercase tracking-wider block">DELIVERY RATE</span>
              <span className="text-2xl font-black text-emerald-600 block mt-1">{formatPercent(analyticsDetail.kpis.deliveryRate)}</span>
            </div>
            <div className="bg-[#FAFAFB] p-4 rounded-2xl border border-[#F0F0F2]">
              <span className="text-[10px] font-bold text-[#9CA3AF] uppercase tracking-wider block">OPEN RATE</span>
              <span className="text-2xl font-black text-[#0A0A0B] block mt-1">{formatPercent(analyticsDetail.kpis.openRate)}</span>
            </div>
            <div className="bg-[#FAFAFB] p-4 rounded-2xl border border-[#F0F0F2]">
              <span className="text-[10px] font-bold text-[#9CA3AF] uppercase tracking-wider block">CLICK RATE</span>
              <span className="text-2xl font-black text-pink-600 block mt-1">{formatPercent(analyticsDetail.kpis.clickRate)}</span>
            </div>
            <div className="bg-[#FAFAFB] p-4 rounded-2xl border border-[#F0F0F2]">
              <span className="text-[10px] font-bold text-[#9CA3AF] uppercase tracking-wider block">BOUNCE RATE</span>
              <span className="text-2xl font-black text-rose-600 block mt-1">{formatPercent(analyticsDetail.kpis.bounceRate)}</span>
            </div>
          </div>
        </div>
      </div>

      {/* RECIPIENT TABLE & LIVE STREAM */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
        <div className="lg:col-span-2 bg-white rounded-[20px] p-5 border border-[#E5E5E7] space-y-4 shadow-xs">
          <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-3">
            <h3 className="font-bold text-[#5F6368] text-[11px] uppercase tracking-wider">Recipient Activity Log</h3>
            <div className="flex items-center gap-2">
              <input
                type="text"
                placeholder="Search email..."
                value={recipientSearch}
                onChange={(e) => setRecipientSearch(e.target.value)}
                className="px-3 h-8 bg-[#FAFAFB] border border-[#E5E5E7] rounded-xl text-[12px] font-medium outline-none"
              />
              <select
                value={recipientStatusFilter}
                onChange={(e) => setRecipientStatusFilter(e.target.value)}
                className="bg-[#FAFAFB] border border-[#E5E5E7] text-[#5F6368] text-[12px] font-semibold rounded-xl px-2.5 h-8 outline-none cursor-pointer"
              >
                <option value="ALL">All Statuses</option>
                <option value="SENT">Sent</option>
                <option value="DELIVERED">Delivered</option>
                <option value="OPENED">Opened</option>
                <option value="CLICKED">Clicked</option>
                <option value="FAILED">Failed</option>
                <option value="BOUNCED">Bounced</option>
              </select>
            </div>
          </div>

          <div className="overflow-x-auto max-h-96 overflow-y-auto">
            <table className="w-full text-left border-collapse text-[13px]">
              <thead>
                <tr className="border-b border-[#E5E5E7] text-[#9CA3AF] font-bold uppercase tracking-wider text-[10px] bg-[#FAFAFB] sticky top-0">
                  <th className="py-2.5 px-3">Recipient Email</th>
                  <th className="py-2.5 px-3">Status</th>
                  <th className="py-2.5 px-3">Sent Time</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[#F0F0F2]">
                {filteredRecipients.length > 0 ? (
                  filteredRecipients.map((rec) => (
                    <tr key={rec.recipientId || rec.email} className="hover:bg-[#FAFAFB] transition-colors">
                      <td className="py-3 px-3 font-semibold text-[#0A0A0B]">{rec.email || 'Unknown'}</td>
                      <td className="py-3 px-3">
                        <span className={`px-2.5 py-0.5 rounded-full text-[10px] font-extrabold uppercase ${
                          rec.status === 'CLICKED' ? 'bg-purple-100 text-purple-800' :
                          rec.status === 'OPENED' ? 'bg-pink-100 text-pink-800' :
                          rec.status === 'DELIVERED' ? 'bg-emerald-100 text-emerald-800' :
                          rec.status === 'SENT' ? 'bg-sky-100 text-sky-800' :
                          'bg-rose-100 text-rose-800'
                        }`}>
                          {rec.status || 'UNKNOWN'}
                        </span>
                      </td>
                      <td className="py-3 px-3 text-[#9CA3AF] text-[12px] font-mono">{rec.sentAt || '—'}</td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={3} className="py-8 text-center text-[#9CA3AF] font-medium">
                      No recipient log activity recorded yet.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>

        {/* Live Activity Telemetry Feed */}
        <div className="bg-white rounded-[20px] p-5 border border-[#E5E5E7] flex flex-col justify-between shadow-xs">
          <div>
            <div className="flex items-center justify-between">
              <h3 className="font-bold text-[#5F6368] text-[11px] uppercase tracking-wider flex items-center gap-2">
                <Activity className="w-4 h-4 text-emerald-600" /> Live Event Stream
              </h3>
              <span className="w-2.5 h-2.5 rounded-full bg-emerald-500 animate-ping" />
            </div>
            <p className="text-[12px] text-[#9CA3AF] font-medium mt-0.5">Real-time WebSocket / SSE telemetry</p>
          </div>

          <div className="space-y-2 overflow-y-auto max-h-80 my-3 pr-1">
            {(liveProgress.recentActivity && liveProgress.recentActivity.length > 0
              ? liveProgress.recentActivity
              : analyticsDetail.liveActivityFeed || []
            ).length > 0 ? (
              (liveProgress.recentActivity || analyticsDetail.liveActivityFeed).map((feed, idx) => (
                <div key={idx} className="bg-[#FAFAFB] p-2.5 rounded-xl border border-[#F0F0F2] text-[11px] font-mono text-slate-700 truncate">
                  {typeof feed === 'string' ? feed : `${feed.eventType}: ${feed.recipientEmail || feed.email || ''}`}
                </div>
              ))
            ) : (
              <div className="py-8 text-center text-[#9CA3AF] text-[12px] font-medium">
                Awaiting batch telemetry events...
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Failure Details Modal */}
      {showFailureModal && (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-xs flex items-center justify-center p-4 z-50 animate-fadeIn font-sans">
          <div className="bg-white rounded-3xl p-6 max-w-4xl w-full max-h-[85vh] flex flex-col shadow-2xl space-y-4 border border-slate-200">
            <div className="flex items-center justify-between border-b border-slate-100 pb-3">
              <div className="flex items-center gap-2.5">
                <AlertOctagon className="w-5 h-5 text-rose-600" />
                <h3 className="text-lg font-black text-slate-900">Campaign Failure Diagnostics</h3>
                <span className="text-xs px-2.5 py-0.5 rounded-full bg-rose-100 text-rose-700 font-bold">
                  {failureDetails.length} Failures
                </span>
              </div>
              <button
                onClick={() => setShowFailureModal(false)}
                className="p-1.5 text-slate-400 hover:text-slate-700 rounded-xl bg-slate-100 cursor-pointer"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            <div className="flex-1 overflow-y-auto space-y-2 pr-1">
              {loadingFailures ? (
                <div className="text-center py-8 text-slate-500 font-medium text-xs flex items-center justify-center gap-2">
                  <RefreshCw className="w-4 h-4 animate-spin text-blue-600" /> Loading failure traces...
                </div>
              ) : failureDetails.length > 0 ? (
                <table className="w-full text-left text-xs border-collapse">
                  <thead>
                    <tr className="bg-slate-50 text-slate-500 border-b border-slate-200 font-semibold">
                      <th className="p-3">Recipient</th>
                      <th className="p-3">Status</th>
                      <th className="p-3">Diagnostic Trace / Failure Reason</th>
                      <th className="p-3">Failed At</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-100">
                    {failureDetails.map(item => (
                      <tr key={item.recipientId || item.recipientEmail} className="hover:bg-rose-50/30 transition-colors">
                        <td className="p-3 font-semibold text-slate-900">
                          <div>{item.recipientName}</div>
                          <div className="text-[11px] text-slate-500 font-mono">{item.recipientEmail}</div>
                        </td>
                        <td className="p-3">
                          <span className="px-2 py-0.5 rounded-md bg-rose-100 text-rose-800 font-bold text-[10px]">
                            {item.status}
                          </span>
                        </td>
                        <td className="p-3 font-mono text-[11px] text-rose-700 bg-rose-50/50 rounded-lg max-w-md break-words">
                          {item.failureReason || 'No detailed reason captured.'}
                        </td>
                        <td className="p-3 text-slate-500 font-mono text-[11px]">
                          {item.failedAt ? new Date(item.failedAt).toLocaleTimeString() : 'N/A'}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              ) : (
                <div className="text-center py-8 text-slate-400 italic text-xs">
                  No failed recipient records found for this campaign.
                </div>
              )}
            </div>

            <div className="flex items-center justify-between border-t border-slate-100 pt-3">
              <span className="text-xs text-slate-400">All exceptions and SMTP diagnostic traces are persisted.</span>
              <button
                onClick={() => setShowFailureModal(false)}
                className="px-5 py-2 bg-slate-900 hover:bg-slate-800 text-white font-bold text-xs rounded-xl transition-colors cursor-pointer"
              >
                Close Diagnostics
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Alert Status Modal */}
      <AlertModal
        isOpen={alertConfig.isOpen}
        onClose={() => setAlertConfig(prev => ({ ...prev, isOpen: false }))}
        type={alertConfig.type}
        title={alertConfig.title}
        message={alertConfig.message}
      />
    </div>
  );
};
