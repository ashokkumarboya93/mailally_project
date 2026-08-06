import React, { useState, useEffect } from 'react';
import { StatCard } from '../../components/common/StatCard';
import { StatusBadge } from '../../components/common/StatusBadge';
import { PageSkeletonLoader } from '../../components/common/PageSkeletonLoader';
import { dashboardApi } from '../../api/dashboardApi';
import { 
  Send, Users, FileText, BarChart2, Activity, 
  Zap, RefreshCw, ArrowRight, Sparkles, CheckCircle2,
  ChevronDown, Trophy, UploadCloud, Sliders, Globe, Smartphone,
  Calendar, Filter, ShieldCheck, Mail, ArrowUpRight, Check, ChevronRight,
  Database, Shield
} from 'lucide-react';
import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';

export const ExecutiveDashboardPage = () => {
  const [kpis, setKpis] = useState(null);
  const [liveStatus, setLiveStatus] = useState(null);
  const [overview, setOverview] = useState(null);
  const [recentActivity, setRecentActivity] = useState([]);
  const [loading, setLoading] = useState(true);

  const mockChartData = [
    { day: 'Mon', sent: 800 },
    { day: 'Tue', sent: 1600 },
    { day: 'Wed', sent: 2100 },
    { day: 'Thu', sent: 1450 },
    { day: 'Fri', sent: 3020 },
    { day: 'Sat', sent: 1200 },
    { day: 'Sun', sent: 2600 }
  ];

  const mockDonutData = [
    { name: 'Completed', value: 65, color: '#2563EB' },
    { name: 'Running', value: 20, color: '#3B82F6' },
    { name: 'Draft', value: 15, color: '#00DDFF' }
  ];

  const mockDeviceData = [
    { name: 'Desktop', value: 58.1, color: '#2563EB' },
    { name: 'Mobile', value: 28.7, color: '#3B82F6' },
    { name: 'Tablet', value: 13.2, color: '#60A5FA' }
  ];

  const mockMultiLineData = [
    { date: 'May 10', sent: 12000, opens: 5000, clicks: 1800 },
    { date: 'May 11', sent: 18000, opens: 8200, clicks: 3100 },
    { date: 'May 12', sent: 15000, opens: 7100, clicks: 2400 },
    { date: 'May 13', sent: 22000, opens: 11000, clicks: 4200 },
    { date: 'May 14', sent: 28000, opens: 14200, clicks: 5800 },
    { date: 'May 15', sent: 24000, opens: 12100, clicks: 4900 }
  ];

  const loadDashboardData = async () => {
    setLoading(true);
    try {
      const [kpiRes, liveRes, overviewRes, activityRes] = await Promise.all([
        dashboardApi.getKpis(),
        dashboardApi.getLiveStatus(),
        dashboardApi.getOverview(),
        dashboardApi.getRecentActivity()
      ]);
      if (kpiRes.data) setKpis(kpiRes.data);
      if (liveRes.data) setLiveStatus(liveRes.data);
      if (overviewRes.data) setOverview(overviewRes.data);
      if (activityRes.data) setRecentActivity(activityRes.data);
    } catch (err) {
      console.error('Failed to load dashboard data:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadDashboardData();
  }, []);

  if (loading) {
    return <PageSkeletonLoader type="cards" />;
  }

  return (
    <div className="space-y-8 animate-fadeInUp font-sans pb-12">
      {/* ═══════════════════════════════════════════════ */}
      {/* 1. HERO DASHBOARD BANNER (100% MATCH TO IMAGE)  */}
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
          onClick={loadDashboardData}
          className="absolute top-4 right-4 w-9 h-9 rounded-xl bg-white/15 hover:bg-white/25 text-white shadow-xs border border-white/20 flex items-center justify-center cursor-pointer hover:rotate-180 transition-transform duration-500 z-20"
          title="Refresh Metrics"
        >
          <RefreshCw className={`w-3.5 h-3.5 ${loading ? 'animate-spin' : ''}`} />
        </button>

        {/* Left Column Content */}
        <div className="space-y-3.5 max-w-sm relative z-10">
          <h1 className="text-2xl sm:text-3xl font-black tracking-tight leading-none text-white" style={{ fontFamily: 'var(--font-heading)' }}>
            Executive <br />
            <span className="text-[#00DDFF]">Dashboard</span>
          </h1>

          <p className="text-[10px] sm:text-[11px] text-blue-50 leading-relaxed font-medium">
            Real-time delivery stats, email dispatch velocity, active engine performance, and recent activity streams.
          </p>

          <div className="pt-1">
            <button className="px-5 py-2.5 rounded-full bg-white hover:bg-blue-50 text-[#2563EB] font-black text-[11px] shadow-md hover:scale-[1.02] transition-all flex items-center space-x-2 cursor-pointer group">
              <span>View Performance Report</span>
              <ArrowRight className="w-3.5 h-3.5 group-hover:translate-x-0.5 transition-transform" />
            </button>
          </div>
        </div>

        {/* Center Outreach Mail Icon Graphic (Custom PNG) */}
        <div className="hidden xl:flex items-center justify-center relative z-10 px-2">
          <img 
            src="/envelope_outreach.png" 
            alt="MailAlly Outreach Campaign Icon" 
            className="w-36 h-auto object-contain max-h-32 select-none pointer-events-none drop-shadow-lg filter brightness-105" 
          />
        </div>

        {/* Right Floating Status Card Container */}
        <div className="flex flex-col space-y-2 relative z-10 w-full sm:w-auto">
          {/* White Card */}
          <div className="bg-white/95 backdrop-blur-md rounded-2xl p-4 shadow-xl border border-white/20 w-full sm:w-64 space-y-3">
            {/* Active Engine */}
            <div className="flex items-center justify-between">
              <div className="space-y-0.5">
                <div className="flex items-center space-x-2">
                  <span className="w-2 h-2 rounded-full bg-[#2563EB]" />
                  <span className="text-[9px] font-black uppercase text-slate-400 tracking-wider">ACTIVE ENGINE</span>
                </div>
                <p className="text-base font-black text-[#1E293B] pl-4" style={{ fontFamily: 'var(--font-heading)' }}>
                  {liveStatus?.activeProvider || 'SMTP'}
                </p>
              </div>
              <div className="w-9 h-9 rounded-full bg-blue-50 text-[#2563EB] border border-blue-100 flex items-center justify-center shadow-3xs">
                <ShieldCheck className="w-4 h-4" />
              </div>
            </div>

            <div className="border-t border-slate-100" />

            {/* Queue Pending */}
            <div className="flex items-center justify-between">
              <div className="space-y-0.5">
                <div className="flex items-center space-x-2">
                  <span className="w-2 h-2 rounded-full bg-emerald-500" />
                  <span className="text-[9px] font-black uppercase text-slate-400 tracking-wider">QUEUE PENDING</span>
                </div>
                <p className="text-base font-black text-[#1E293B] pl-4" style={{ fontFamily: 'var(--font-heading)' }}>
                  {liveStatus?.queuePendingCount || 0} Emails
                </p>
              </div>
              <div className="w-9 h-9 rounded-full bg-emerald-50 text-emerald-600 border border-emerald-100 flex items-center justify-center shadow-3xs">
                <Database className="w-4 h-4" />
              </div>
            </div>
          </div>

          {/* Operational Status Pill Below */}
          <div className="p-2.5 px-4 rounded-xl bg-white/95 backdrop-blur-md border border-white/20 shadow-md flex items-center justify-between text-xs cursor-pointer hover:bg-slate-50 transition-colors w-full sm:w-64">
            <div className="flex items-center space-x-2">
              <CheckCircle2 className="w-3.5 h-3.5 text-emerald-500" />
              <span className="font-extrabold text-[#1E293B] text-[11px]">All Systems Operational</span>
            </div>
            <ChevronRight className="w-3.5 h-3.5 text-slate-400" />
          </div>
        </div>
      </div>

      {/* ═══════════════════════════════════════════════ */}
      {/* 2. 4 KPI CARDS GRID                             */}
      {/* ═══════════════════════════════════════════════ */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard
          title="TOTAL CAMPAIGNS"
          value={kpis?.totalCampaigns ?? 1}
          change="+14%"
          isPositive={true}
          icon={Send}
          description="Active & completed dispatches"
        />
        <StatCard
          title="TOTAL EMAILS SENT"
          value={kpis?.totalEmailsSent ?? 0}
          change="+28%"
          isPositive={true}
          icon={BarChart2}
          description="100% delivered success rate"
        />
        <StatCard
          title="ACTIVE CONTACTS"
          value={kpis?.subscribedContacts ?? 4}
          change="+8%"
          isPositive={true}
          icon={Users}
          description="Subscribed contact list"
        />
        <StatCard
          title="EMAIL TEMPLATES"
          value={kpis?.totalTemplates ?? 2}
          change="+2"
          isPositive={true}
          icon={FileText}
          description="Reusable HTML templates"
        />
      </div>

      {/* ═══════════════════════════════════════════════ */}
      {/* 3. CHARTS GRID (Telemetry & Donut)              */}
      {/* ═══════════════════════════════════════════════ */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Line Chart Card */}
        <div className="lg:col-span-2 claude-card p-6 space-y-4">
          <div className="flex items-center justify-between border-b border-slate-100 pb-3">
            <div>
              <span className="badge-blue mb-1 text-[10px]">TELEMETRY</span>
              <h3 className="font-black text-lg text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
                Email Dispatch Velocity
              </h3>
              <p className="text-xs text-slate-400 font-medium">Weekly email sent & delivered volume telemetry</p>
            </div>

            <div className="flex items-center space-x-2">
              <button className="px-3.5 py-1.5 rounded-xl text-xs font-extrabold bg-blue-50 text-[#2563EB] border border-blue-100 flex items-center gap-1.5 cursor-pointer">
                <span>7 Days</span>
                <ChevronDown className="w-3.5 h-3.5" />
              </button>
            </div>
          </div>

          <div className="h-64 w-full pt-2">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={mockChartData}>
                <defs>
                  <linearGradient id="areaGradient" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0%" stopColor="#2563EB" stopOpacity="0.25" />
                    <stop offset="100%" stopColor="#2563EB" stopOpacity="0.0" />
                  </linearGradient>
                </defs>
                <XAxis dataKey="day" stroke="#94A3B8" fontSize={11} tickLine={false} />
                <YAxis stroke="#94A3B8" fontSize={11} tickLine={false} />
                <Tooltip
                  content={({ active, payload }) => {
                    if (active && payload && payload.length) {
                      return (
                        <div className="bg-white p-3 rounded-2xl border border-blue-100 shadow-xl text-xs space-y-1">
                          <p className="font-bold text-slate-400">{payload[0].payload.day}</p>
                          <p className="font-black text-[#2563EB] text-sm">{payload[0].value.toLocaleString()} Emails</p>
                        </div>
                      );
                    }
                    return null;
                  }}
                />
                <Line 
                  type="monotone" 
                  dataKey="sent" 
                  stroke="#2563EB" 
                  strokeWidth={3.5} 
                  dot={{ r: 5, fill: '#2563EB', stroke: '#FFFFFF', strokeWidth: 2 }} 
                  activeDot={{ r: 7, fill: '#2563EB' }}
                />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Donut Campaign States Card */}
        <div className="claude-card p-6 flex flex-col justify-between space-y-4">
          <div className="border-b border-slate-100 pb-3">
            <span className="badge-cyan mb-1 text-[10px]">DISTRIBUTION</span>
            <h3 className="font-black text-lg text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
              Campaign States
            </h3>
            <p className="text-xs text-slate-400 font-medium">Breakdown by execution state</p>
          </div>

          <div className="h-44 w-full flex items-center justify-center relative">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie data={mockDonutData} innerRadius={55} outerRadius={78} paddingAngle={6} dataKey="value">
                  {mockDonutData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} stroke="#FFFFFF" strokeWidth={3} />
                  ))}
                </Pie>
              </PieChart>
            </ResponsiveContainer>
          </div>

          <div className="space-y-2 pt-2">
            {mockDonutData.map((item) => (
              <div key={item.name} className="flex items-center justify-between p-2.5 px-3.5 rounded-xl bg-blue-50/50 border border-blue-100/60">
                <div className="flex items-center space-x-2.5">
                  <span className="w-2.5 h-2.5 rounded-full" style={{ backgroundColor: item.color }} />
                  <span className="text-xs font-extrabold text-[#1E3A8A]">{item.name}</span>
                </div>
                <span className="text-xs font-black text-[#2563EB]" style={{ fontFamily: 'var(--font-heading)' }}>
                  {item.value}%
                </span>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* ═══════════════════════════════════════════════ */}
      {/* 4. REAL-TIME ACTIVITY STREAM                    */}
      {/* ═══════════════════════════════════════════════ */}
      <div className="claude-card p-6 space-y-5">
        <div className="flex items-center justify-between border-b border-slate-100 pb-4">
          <div>
            <span className="badge-blue mb-1 text-[10px]">LIVE STREAM</span>
            <h3 className="font-black text-xl text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
              Real-Time Activity Stream
            </h3>
            <p className="text-xs text-slate-400 font-medium">System dispatches and automated engine events</p>
          </div>

          <span className="text-xs font-black text-[#2563EB] bg-blue-50 px-3.5 py-1.5 rounded-full border border-blue-100 flex items-center gap-2">
            <span className="w-2 h-2 rounded-full bg-[#2563EB] animate-ping" />
            Live Engine Stream
          </span>
        </div>

        <div className="space-y-3">
          {[
            { title: 'Campaign TODAY TEST', desc: 'Status updated to COMPLETED', status: 'COMPLETED', statusType: 'success', time: '2m ago', icon: CheckCircle2 },
            { title: 'New Contact Added', desc: 'thrisha142003@gmail.com joined mailing list', status: 'SUBSCRIBED', statusType: 'info', time: '5m ago', icon: Users },
            { title: 'New Contact Added', desc: 'ashokkumarboya999@gmail.com joined mailing list', status: 'SUBSCRIBED', statusType: 'info', time: '8m ago', icon: Users },
            { title: 'New Contact Added', desc: 'www.thimmappa51048@gmail.com joined mailing list', status: 'SUBSCRIBED', statusType: 'info', time: '12m ago', icon: Users },
          ].map((act, index) => {
            const IconComponent = act.icon;
            return (
              <div
                key={index}
                className="flex items-center justify-between p-4 rounded-2xl border border-blue-100/70 bg-[#F7FAFF] hover:bg-blue-50/70 transition-all cursor-pointer group"
              >
                <div className="flex items-center space-x-4">
                  <div className={`w-10 h-10 rounded-2xl flex items-center justify-center flex-shrink-0 font-bold ${
                    act.statusType === 'success' 
                      ? 'bg-emerald-50 text-emerald-600 border border-emerald-200/60' 
                      : 'bg-blue-50 text-[#2563EB] border border-blue-200/60'
                  }`}>
                    <IconComponent className="w-5 h-5" />
                  </div>
                  <div>
                    <p className="text-xs font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>{act.title}</p>
                    <p className="text-[11px] text-slate-500 font-medium">{act.desc}</p>
                  </div>
                </div>

                <div className="flex items-center space-x-4">
                  <span className={`px-3 py-1 rounded-xl text-[10px] font-black uppercase tracking-wider ${
                    act.statusType === 'success'
                      ? 'bg-emerald-100/70 text-emerald-700 border border-emerald-200'
                      : 'bg-blue-100/70 text-[#2563EB] border border-blue-200'
                  }`}>
                    ✓ {act.status}
                  </span>
                  <span className="text-[11px] font-bold text-slate-400">{act.time}</span>
                </div>
              </div>
            );
          })}
        </div>

        <div className="pt-2 text-center">
          <button className="text-xs font-black text-[#2563EB] hover:text-[#1D4ED8] inline-flex items-center gap-1.5 transition-colors cursor-pointer">
            <span>View All Activity</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </button>
        </div>
      </div>

      {/* ═══════════════════════════════════════════════ */}
      {/* 5. 3-COLUMN OPERATIONS GRID                     */}
      {/* ═══════════════════════════════════════════════ */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Provider Status Card */}
        <div className="claude-card p-6 flex flex-col justify-between space-y-4">
          <div>
            <h4 className="font-black text-base text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
              Provider Status
            </h4>
            <p className="text-xs text-slate-400 font-medium">All email providers operational</p>

            <div className="space-y-3 pt-4">
              {[
                { name: 'Amazon SES', status: 'Healthy' },
                { name: 'Brevo', status: 'Healthy' },
                { name: 'SMTP', status: 'Healthy' },
                { name: 'MailAlly Engine', status: 'Healthy' }
              ].map((prov, i) => (
                <div key={i} className="flex items-center justify-between p-3 rounded-xl bg-slate-50 border border-slate-100">
                  <span className="text-xs font-bold text-[#1E3A8A]">{prov.name}</span>
                  <span className="text-[11px] font-black text-emerald-600 flex items-center gap-1.5">
                    <span className="w-2 h-2 rounded-full bg-emerald-500" />
                    {prov.status}
                  </span>
                </div>
              ))}
            </div>
          </div>

          <button className="text-xs font-black text-[#2563EB] hover:text-[#1D4ED8] flex items-center gap-1 cursor-pointer pt-2">
            <span>View All Providers</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </button>
        </div>

        {/* Quick Actions Tile Cards */}
        <div className="claude-card p-6 space-y-4">
          <div>
            <h4 className="font-black text-base text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
              Quick Actions
            </h4>
            <p className="text-xs text-slate-400 font-medium">Everything you need, one click away</p>
          </div>

          <div className="grid grid-cols-2 gap-3">
            {[
              { label: 'Create Campaign', icon: Send },
              { label: 'Upload Contacts', icon: UploadCloud },
              { label: 'Create Template', icon: FileText },
              { label: 'Automation Builder', icon: Sliders }
            ].map((qa, idx) => {
              const IconComp = qa.icon;
              return (
                <div
                  key={idx}
                  className="p-4 rounded-2xl bg-[#F7FAFF] border border-blue-100/70 hover:border-blue-300 hover:bg-blue-50/50 hover:shadow-md transition-all cursor-pointer text-center space-y-2 group flex flex-col items-center justify-center"
                >
                  <div className="w-10 h-10 rounded-xl bg-white text-[#2563EB] flex items-center justify-center font-bold shadow-2xs group-hover:scale-110 transition-transform">
                    <IconComp className="w-5 h-5" />
                  </div>
                  <span className="text-xs font-extrabold text-[#1E3A8A] block leading-tight">
                    {qa.label}
                  </span>
                </div>
              );
            })}
          </div>
        </div>

        {/* Top Performing Campaign Card */}
        <div className="claude-card p-6 flex flex-col justify-between space-y-4">
          <div>
            <div className="flex items-center justify-between">
              <div>
                <h4 className="font-black text-base text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
                  Top Performing Campaign
                </h4>
                <p className="text-xs text-slate-400 font-medium mt-0.5">Summer Sale Campaign</p>
              </div>
              <div className="w-10 h-10 rounded-xl bg-purple-50 text-purple-600 flex items-center justify-center font-bold border border-purple-100 shadow-2xs">
                <Trophy className="w-5 h-5" />
              </div>
            </div>

            <div className="grid grid-cols-2 gap-4 pt-6">
              <div className="p-3.5 rounded-2xl bg-blue-50/60 border border-blue-100">
                <span className="text-[10px] font-bold uppercase tracking-wider text-slate-400 block">Emails Sent</span>
                <span className="text-2xl font-black text-[#1E3A8A] block mt-1" style={{ fontFamily: 'var(--font-heading)' }}>
                  24,560
                </span>
              </div>
              <div className="p-3.5 rounded-2xl bg-cyan-50/60 border border-cyan-100">
                <span className="text-[10px] font-bold uppercase tracking-wider text-slate-400 block">Open Rate</span>
                <span className="text-2xl font-black text-[#2563EB] block mt-1" style={{ fontFamily: 'var(--font-heading)' }}>
                  42.7%
                </span>
              </div>
            </div>
          </div>

          <button className="text-xs font-black text-[#2563EB] hover:text-[#1D4ED8] flex items-center gap-1 cursor-pointer pt-2">
            <span>View Campaign Report</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </button>
        </div>
      </div>

      {/* ═══════════════════════════════════════════════ */}
      {/* 6. ANALYTICS OVERVIEW SECTION                   */}
      {/* ═══════════════════════════════════════════════ */}
      <div className="claude-card p-6 space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-slate-100 pb-4">
          <div>
            <h3 className="font-black text-xl text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
              Analytics Overview
            </h3>
            <p className="text-xs text-slate-400 font-medium">Track your email performance and engagement</p>
          </div>

          <div className="flex items-center space-x-3">
            <button className="px-4 py-2 rounded-xl text-xs font-extrabold bg-white border border-slate-200 text-slate-600 flex items-center gap-2 shadow-2xs">
              <Calendar className="w-3.5 h-3.5 text-[#2563EB]" />
              <span>May 10 - May 16, 2025</span>
            </button>
            <button className="px-4 py-2 rounded-xl text-xs font-extrabold bg-white border border-slate-200 text-slate-600 flex items-center gap-2 shadow-2xs">
              <Filter className="w-3.5 h-3.5 text-slate-400" />
              <span>Filter</span>
            </button>
          </div>
        </div>

        {/* 5 Micro KPI Pills */}
        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-3">
          {[
            { label: 'Emails Sent', val: '80,920', trend: '↑ 38.1%', positive: true },
            { label: 'Open Rate', val: '42.7%', trend: '↑ 12.4%', positive: true },
            { label: 'Click Rate', val: '11.3%', trend: '↑ 8.7%', positive: true },
            { label: 'Bounce Rate', val: '2.1%', trend: '↓ 0.4%', positive: true },
            { label: 'Unsubscribe Rate', val: '0.6%', trend: '↑ 0.1%', positive: false }
          ].map((pill, idx) => (
            <div key={idx} className="p-3.5 rounded-2xl bg-[#F7FAFF] border border-blue-100/70 space-y-1">
              <span className="text-[10px] font-bold text-slate-400 uppercase tracking-wider block">{pill.label}</span>
              <div className="flex items-baseline justify-between">
                <span className="text-lg font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
                  {pill.val}
                </span>
                <span className={`text-[10px] font-black ${pill.positive ? 'text-emerald-600' : 'text-rose-600'}`}>
                  {pill.trend}
                </span>
              </div>
            </div>
          ))}
        </div>

        {/* 3 Sub-Analytics Cards */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 pt-2">
          {/* Performance Over Time Mini Multi-Line */}
          <div className="p-5 rounded-2xl bg-white border border-slate-200/80 space-y-3">
            <h5 className="font-extrabold text-xs text-[#1E3A8A] uppercase tracking-wider">Performance Over Time</h5>
            <div className="h-40 w-full pt-1">
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={mockMultiLineData}>
                  <Line type="monotone" dataKey="sent" stroke="#2563EB" strokeWidth={2} dot={false} />
                  <Line type="monotone" dataKey="opens" stroke="#3B82F6" strokeWidth={2} dot={false} />
                  <Line type="monotone" dataKey="clicks" stroke="#60A5FA" strokeWidth={2} dot={false} />
                </LineChart>
              </ResponsiveContainer>
            </div>
          </div>

          {/* Top Locations List */}
          <div className="p-5 rounded-2xl bg-white border border-slate-200/80 space-y-3">
            <h5 className="font-extrabold text-xs text-[#1E3A8A] uppercase tracking-wider">Top Locations</h5>
            <div className="space-y-2 text-xs">
              {[
                { country: 'India', percent: '45.2%' },
                { country: 'USA', percent: '25.6%' },
                { country: 'UK', percent: '8.7%' },
                { country: 'Canada', percent: '6.3%' },
                { country: 'Others', percent: '14.2%' }
              ].map((loc, i) => (
                <div key={i} className="flex items-center justify-between font-bold">
                  <span className="text-slate-600">{loc.country}</span>
                  <span className="text-[#2563EB] font-black">{loc.percent}</span>
                </div>
              ))}
            </div>
          </div>

          {/* Device Breakdown Donut */}
          <div className="p-5 rounded-2xl bg-white border border-slate-200/80 space-y-3 flex flex-col justify-between">
            <h5 className="font-extrabold text-xs text-[#1E3A8A] uppercase tracking-wider">Device Breakdown</h5>
            <div className="h-32 w-full flex items-center justify-center">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie data={mockDeviceData} innerRadius={40} outerRadius={56} paddingAngle={4} dataKey="value">
                    {mockDeviceData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Pie>
                </PieChart>
              </ResponsiveContainer>
            </div>

            <div className="flex items-center justify-around text-xs font-bold pt-1">
              <span className="text-[#2563EB]">Desktop 58.1%</span>
              <span className="text-[#3B82F6]">Mobile 28.7%</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
