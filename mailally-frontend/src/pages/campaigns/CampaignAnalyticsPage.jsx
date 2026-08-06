import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { campaignApi } from '../../api/campaignApi';
import {
  ArrowLeft, CheckCircle2, XCircle, Mail, TrendingUp,
  Clock, Zap, BarChart3, Users, RefreshCw, Send
} from 'lucide-react';
import {
  PieChart, Pie, Cell, ResponsiveContainer, Tooltip,
  BarChart, Bar, XAxis, YAxis, CartesianGrid
} from 'recharts';

export const CampaignAnalyticsPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [progress, setProgress] = useState(null);
  const [stats, setStats] = useState(null);
  const [logs, setLogs] = useState([]);
  const [logsPage, setLogsPage] = useState(0);
  const [totalLogPages, setTotalLogPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  const loadData = async (showLoading = true) => {
    if (showLoading) setLoading(true);
    else setRefreshing(true);
    try {
      const [progressRes, statsRes, logsRes] = await Promise.all([
        campaignApi.getCampaignProgress(id),
        campaignApi.getDeliveryStats(id),
        campaignApi.getCampaignLogs(id, { page: logsPage, size: 10 }),
      ]);

      if (progressRes?.data) setProgress(progressRes.data);
      if (statsRes?.data) setStats(statsRes.data);
      if (logsRes?.data) {
        setLogs(logsRes.data.content || []);
        setTotalLogPages(logsRes.data.totalPages || 0);
      }
    } catch (err) {
      console.error('Failed to load analytics:', err);
      // Set demo data for development
      setProgress({
        campaignId: id,
        campaignName: 'Campaign #' + id,
        campaignStatus: 'COMPLETED',
        totalRecipients: 0,
        sentCount: 0,
        failedCount: 0,
        pendingCount: 0,
        progressPercentage: 0,
      });
      setStats({
        totalSent: 0, totalDelivered: 0, totalBounced: 0, totalFailed: 0,
        totalOpened: 0, totalClicked: 0,
        deliveryRate: 0, bounceRate: 0, openRate: 0, clickRate: 0,
      });
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useEffect(() => { loadData(); }, [id, logsPage]);

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div
          className="w-10 h-10 rounded-full border-3 border-t-transparent animate-spin"
          style={{ borderColor: 'var(--claude-accent)', borderTopColor: 'transparent' }}
        />
      </div>
    );
  }

  const donutData = [
    { name: 'Delivered', value: stats?.totalDelivered || 0, color: '#10B981' },
    { name: 'Failed', value: stats?.totalFailed || 0, color: '#EF4444' },
    { name: 'Bounced', value: stats?.totalBounced || 0, color: '#F59E0B' },
  ].filter(d => d.value > 0);

  // If no data, show placeholder
  if (donutData.length === 0) {
    donutData.push({ name: 'No Data', value: 1, color: 'var(--claude-border)' });
  }

  const statusBarData = [
    { label: 'Sent', count: progress?.sentCount || 0, color: '#10B981' },
    { label: 'Failed', count: progress?.failedCount || 0, color: '#EF4444' },
    { label: 'Pending', count: progress?.pendingCount || 0, color: '#F59E0B' },
  ];

  const statusColors = {
    COMPLETED: '#10B981',
    RUNNING: '#3B82F6',
    FAILED: '#EF4444',
    CANCELLED: '#F59E0B',
    DRAFT: '#6B7280',
    SCHEDULED: '#8B5CF6',
  };

  const campaignStatus = progress?.campaignStatus || 'DRAFT';
  const statusColor = statusColors[campaignStatus] || '#6B7280';

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

        {/* Floating Back Button */}
        <button
          onClick={() => navigate('/campaigns')}
          className="absolute top-4 left-4 w-9 h-9 rounded-xl bg-white/15 hover:bg-white/25 text-white shadow-xs border border-white/20 flex items-center justify-center cursor-pointer transition-transform z-20"
          title="Back to Campaigns"
        >
          <ArrowLeft className="w-4 h-4 text-white" />
        </button>

        {/* Floating Refresh Button */}
        <button
          onClick={() => loadData(false)}
          className="absolute top-4 right-4 w-9 h-9 rounded-xl bg-white/15 hover:bg-white/25 text-white shadow-xs border border-white/20 flex items-center justify-center cursor-pointer hover:rotate-180 transition-transform duration-500 z-20"
          title="Refresh Data"
        >
          <RefreshCw className={`w-3.5 h-3.5 ${refreshing ? 'animate-spin' : ''}`} />
        </button>

        {/* Left Column Content */}
        <div className="space-y-3.5 max-w-sm relative z-10 pt-4 sm:pt-0">
          <div className="inline-flex items-center space-x-2 px-3 py-1 rounded-full bg-white/15 text-white font-black text-[10px] border border-white/25 shadow-3xs backdrop-blur-md">
            <BarChart3 className="w-3 h-3 text-[#00DDFF]" />
            <span>Telemetry & Logs</span>
          </div>

          <h1 className="text-2xl sm:text-3xl font-black tracking-tight leading-none text-white" style={{ fontFamily: 'var(--font-heading)' }}>
            Campaign <br />
            <span className="text-[#00DDFF]">Analytics</span>
          </h1>

          <p className="text-[10px] sm:text-[11px] text-blue-50 leading-relaxed font-medium">
            {progress?.campaignName || `Campaign #${id}`} · Status: <span className="uppercase font-black text-[#00DDFF]">{campaignStatus}</span>
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

      {/* Overview Progress Bar */}
      <div className="claude-card rounded-3xl p-5">
        <div className="flex items-center justify-between mb-3">
          <span className="text-xs font-bold" style={{ color: 'var(--claude-text)' }}>
            Overall Progress
          </span>
          <span className="text-sm font-black tabular-nums" style={{ color: statusColor }}>
            {Math.round(progress?.progressPercentage || 0)}%
          </span>
        </div>
        <div className="w-full h-3 rounded-full overflow-hidden" style={{ backgroundColor: 'var(--claude-surface-elevated)' }}>
          <div
            className="h-full rounded-full transition-all duration-700 ease-out"
            style={{
              width: `${progress?.progressPercentage || 0}%`,
              backgroundColor: statusColor,
              boxShadow: `0 0 12px ${statusColor}44`,
            }}
          />
        </div>
        <div className="flex items-center justify-between mt-2">
          <span className="text-[10px]" style={{ color: 'var(--claude-text-muted)' }}>
            {progress?.sentCount || 0} sent of {progress?.totalRecipients || 0} recipients
          </span>
          <span className="text-[10px]" style={{ color: 'var(--claude-text-muted)' }}>
            {progress?.failedCount || 0} failed
          </span>
        </div>
      </div>

      {/* Stats Cards Row */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
        <AnalyticsStatCard
          title="Delivery Rate"
          value={`${(stats?.deliveryRate || 0).toFixed(1)}%`}
          icon={CheckCircle2}
          color="#10B981"
          subtitle={`${stats?.totalDelivered || 0} delivered`}
        />
        <AnalyticsStatCard
          title="Bounce Rate"
          value={`${(stats?.bounceRate || 0).toFixed(1)}%`}
          icon={XCircle}
          color="#EF4444"
          subtitle={`${stats?.totalFailed || 0} failed`}
        />
        <AnalyticsStatCard
          title="Open Rate"
          value={`${(stats?.openRate || 0).toFixed(1)}%`}
          icon={Mail}
          color="#3B82F6"
          subtitle={`${stats?.totalOpened || 0} opened`}
        />
        <AnalyticsStatCard
          title="Click Rate"
          value={`${(stats?.clickRate || 0).toFixed(1)}%`}
          icon={TrendingUp}
          color="#8B5CF6"
          subtitle={`${stats?.totalClicked || 0} clicked`}
        />
      </div>

      {/* Charts Row */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
        {/* Delivery Donut Chart */}
        <div className="claude-card rounded-3xl p-5">
          <h3 className="font-bold text-sm mb-4" style={{ color: 'var(--claude-text)' }}>
            Delivery Breakdown
          </h3>
          <div className="h-56 flex items-center justify-center">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={donutData}
                  cx="50%"
                  cy="50%"
                  innerRadius={60}
                  outerRadius={85}
                  paddingAngle={3}
                  dataKey="value"
                  stroke="none"
                >
                  {donutData.map((entry, index) => (
                    <Cell key={index} fill={entry.color} />
                  ))}
                </Pie>
                <Tooltip
                  contentStyle={{
                    backgroundColor: 'var(--claude-surface)',
                    borderRadius: '12px',
                    border: '1px solid var(--claude-border)',
                    fontSize: '12px',
                    color: 'var(--claude-text)',
                  }}
                />
              </PieChart>
            </ResponsiveContainer>
          </div>
          <div className="flex items-center justify-center gap-4 mt-2">
            {donutData.filter(d => d.name !== 'No Data').map((d, i) => (
              <div key={i} className="flex items-center gap-1.5">
                <div className="w-2.5 h-2.5 rounded-full" style={{ backgroundColor: d.color }} />
                <span className="text-[10px] font-semibold" style={{ color: 'var(--claude-text-muted)' }}>
                  {d.name}: {d.value}
                </span>
              </div>
            ))}
          </div>
        </div>

        {/* Status Bar Chart */}
        <div className="claude-card rounded-3xl p-5">
          <h3 className="font-bold text-sm mb-4" style={{ color: 'var(--claude-text)' }}>
            Email Status Distribution
          </h3>
          <div className="h-56">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={statusBarData}>
                <CartesianGrid strokeDasharray="3 3" stroke="var(--claude-border)" />
                <XAxis dataKey="label" stroke="var(--claude-text-muted)" fontSize={11} tickLine={false} />
                <YAxis stroke="var(--claude-text-muted)" fontSize={11} tickLine={false} />
                <Tooltip
                  contentStyle={{
                    backgroundColor: 'var(--claude-surface)',
                    borderRadius: '12px',
                    border: '1px solid var(--claude-border)',
                    fontSize: '12px',
                    color: 'var(--claude-text)',
                  }}
                />
                <Bar dataKey="count" radius={[8, 8, 0, 0]}>
                  {statusBarData.map((entry, index) => (
                    <Cell key={index} fill={entry.color} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-4">
        <SummaryCard icon={Users} label="Total Recipients" value={progress?.totalRecipients || 0} color="#8B5CF6" />
        <SummaryCard icon={Send} label="Emails Sent" value={progress?.sentCount || 0} color="#10B981" />
        <SummaryCard icon={XCircle} label="Emails Failed" value={progress?.failedCount || 0} color="#EF4444" />
        <SummaryCard icon={Clock} label="Pending" value={progress?.pendingCount || 0} color="#F59E0B" />
      </div>

      {/* Email Logs Table */}
      <div className="claude-card rounded-3xl overflow-hidden">
        <div className="px-6 py-4" style={{ borderBottom: '1px solid var(--claude-border)' }}>
          <h3 className="font-bold text-sm" style={{ color: 'var(--claude-text)' }}>
            Recipient Delivery Log
          </h3>
          <p className="text-[10px]" style={{ color: 'var(--claude-text-muted)' }}>
            Individual email delivery status for this campaign
          </p>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs" style={{ color: 'var(--claude-text-secondary)' }}>
            <thead
              className="uppercase text-[10px] font-bold"
              style={{
                backgroundColor: 'var(--claude-surface-elevated)',
                color: 'var(--claude-text-muted)',
                borderBottom: '1px solid var(--claude-border)',
              }}
            >
              <tr>
                <th className="px-6 py-3">Recipient</th>
                <th className="px-6 py-3">Subject</th>
                <th className="px-6 py-3">Provider</th>
                <th className="px-6 py-3">Status</th>
                <th className="px-6 py-3">Sent At</th>
              </tr>
            </thead>
            <tbody>
              {logs.length === 0 ? (
                <tr>
                  <td colSpan={5} className="px-6 py-8 text-center" style={{ color: 'var(--claude-text-muted)' }}>
                    No email logs found for this campaign
                  </td>
                </tr>
              ) : (
                logs.map((log, i) => (
                  <tr
                    key={log.id || i}
                    className="transition-colors duration-150"
                    style={{ borderBottom: '1px solid var(--claude-border)' }}
                    onMouseEnter={e => e.currentTarget.style.backgroundColor = 'var(--claude-surface-elevated)'}
                    onMouseLeave={e => e.currentTarget.style.backgroundColor = 'transparent'}
                  >
                    <td className="px-6 py-3">
                      <div>
                        <span className="font-semibold" style={{ color: 'var(--claude-text)' }}>
                          {log.recipientName || '—'}
                        </span>
                        <br />
                        <span className="text-[10px]" style={{ color: 'var(--claude-text-muted)' }}>
                          {log.recipientEmail}
                        </span>
                      </div>
                    </td>
                    <td className="px-6 py-3 max-w-[200px] truncate">{log.subject}</td>
                    <td className="px-6 py-3">
                      <span
                        className="px-2 py-0.5 rounded-md text-[10px] font-bold"
                        style={{
                          backgroundColor: 'var(--claude-surface-elevated)',
                          color: 'var(--claude-text)',
                        }}
                      >
                        {log.provider || '—'}
                      </span>
                    </td>
                    <td className="px-6 py-3">
                      <span
                        className="px-2 py-0.5 rounded-full text-[10px] font-bold"
                        style={{
                          backgroundColor: log.status === 'SENT' ? '#10B98115' : '#EF444415',
                          color: log.status === 'SENT' ? '#10B981' : '#EF4444',
                        }}
                      >
                        {log.status}
                      </span>
                    </td>
                    <td className="px-6 py-3 text-[10px]" style={{ color: 'var(--claude-text-muted)' }}>
                      {log.sentAt ? new Date(log.sentAt).toLocaleString() : log.failedAt ? new Date(log.failedAt).toLocaleString() : '—'}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        {/* Pagination */}
        {totalLogPages > 1 && (
          <div className="px-6 py-3 flex items-center justify-between" style={{ borderTop: '1px solid var(--claude-border)' }}>
            <span className="text-[10px]" style={{ color: 'var(--claude-text-muted)' }}>
              Page {logsPage + 1} of {totalLogPages}
            </span>
            <div className="flex items-center gap-2">
              <button
                disabled={logsPage === 0}
                onClick={() => setLogsPage(p => Math.max(0, p - 1))}
                className="px-3 py-1 rounded-lg text-[10px] font-semibold transition-all duration-200 disabled:opacity-40"
                style={{
                  backgroundColor: 'var(--claude-surface-elevated)',
                  color: 'var(--claude-text)',
                  border: '1px solid var(--claude-border)',
                }}
              >
                Previous
              </button>
              <button
                disabled={logsPage >= totalLogPages - 1}
                onClick={() => setLogsPage(p => p + 1)}
                className="px-3 py-1 rounded-lg text-[10px] font-semibold transition-all duration-200 disabled:opacity-40"
                style={{
                  backgroundColor: 'var(--claude-surface-elevated)',
                  color: 'var(--claude-text)',
                  border: '1px solid var(--claude-border)',
                }}
              >
                Next
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

/** Analytics stat card component */
const AnalyticsStatCard = ({ title, value, icon: Icon, color, subtitle }) => (
  <div
    className="claude-card rounded-2xl p-4 transition-all duration-300"
    style={{ borderLeft: `3px solid ${color}` }}
    onMouseEnter={e => {
      e.currentTarget.style.transform = 'translateY(-2px)';
      e.currentTarget.style.boxShadow = `0 8px 24px ${color}15`;
    }}
    onMouseLeave={e => {
      e.currentTarget.style.transform = 'translateY(0)';
      e.currentTarget.style.boxShadow = 'none';
    }}
  >
    <div className="flex items-center justify-between mb-2">
      <span className="text-[10px] font-semibold uppercase tracking-wider" style={{ color: 'var(--claude-text-muted)' }}>
        {title}
      </span>
      <div
        className="w-7 h-7 rounded-lg flex items-center justify-center"
        style={{ backgroundColor: `${color}12` }}
      >
        <Icon className="w-3.5 h-3.5" style={{ color }} />
      </div>
    </div>
    <div className="text-xl font-black" style={{ color: 'var(--claude-text)' }}>
      {value}
    </div>
    <span className="text-[10px]" style={{ color: 'var(--claude-text-muted)' }}>{subtitle}</span>
  </div>
);

/** Summary count card */
const SummaryCard = ({ icon: Icon, label, value, color }) => (
  <div
    className="claude-card rounded-2xl p-4 flex items-center gap-3"
  >
    <div
      className="w-10 h-10 rounded-xl flex items-center justify-center flex-shrink-0"
      style={{ backgroundColor: `${color}12`, border: `1px solid ${color}20` }}
    >
      <Icon className="w-5 h-5" style={{ color }} />
    </div>
    <div>
      <div className="text-lg font-black tabular-nums" style={{ color: 'var(--claude-text)' }}>
        {(value || 0).toLocaleString()}
      </div>
      <span className="text-[10px] font-semibold" style={{ color: 'var(--claude-text-muted)' }}>
        {label}
      </span>
    </div>
  </div>
);
