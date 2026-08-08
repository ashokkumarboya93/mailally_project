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
    { name: 'Completed', value: 65, color: '#22C55E' },
    { name: 'Running', value: 20, color: '#3B82F6' },
    { name: 'Draft', value: 15, color: '#E5E7EB' }
  ];

  const mockDeviceData = [
    { name: 'Desktop', value: 58.1, color: '#0A0A0B' },
    { name: 'Mobile', value: 28.7, color: '#EC4899' },
    { name: 'Tablet', value: 13.2, color: '#E5E7EB' }
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
    <div className="space-y-6 animate-fadeInUp pb-8">

      {/* ═══════════════════════════════════════════════ */}
      {/* 1. WELCOME HEADER                              */}
      {/* ═══════════════════════════════════════════════ */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold tracking-tight text-[#0A0A0B]">
            Dashboard
          </h1>
          <p className="text-[13px] text-[#9CA3AF] font-medium mt-1">
            Real-time delivery metrics, engine performance, and activity.
          </p>
        </div>

        <div className="flex items-center gap-2.5">
          {/* System Status Pill */}
          <div className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-[#DCFCE7] text-[#16A34A]">
            <span className="w-1.5 h-1.5 rounded-full bg-[#22C55E] animate-pulse" />
            <span className="text-[11px] font-semibold">All Systems Operational</span>
          </div>

          {/* Refresh */}
          <button
            onClick={loadDashboardData}
            className="p-2 rounded-lg border border-[#E5E5E7] bg-white text-[#9CA3AF] hover:text-[#0A0A0B] hover:bg-[#F9FAFB] transition-all cursor-pointer"
            title="Refresh"
          >
            <RefreshCw className={`w-4 h-4 ${loading ? 'animate-spin' : ''}`} />
          </button>
        </div>
      </div>

      {/* ═══════════════════════════════════════════════ */}
      {/* 2. KPI CARDS                                   */}
      {/* ═══════════════════════════════════════════════ */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard
          title="Total Campaigns"
          value={kpis?.totalCampaigns ?? 1}
          change="+14%"
          isPositive={true}
          icon={Send}
          description="Active & completed"
          accentColor="pink"
        />
        <StatCard
          title="Emails Sent"
          value={kpis?.totalEmailsSent ?? 0}
          change="+28%"
          isPositive={true}
          icon={Mail}
          description="100% delivery rate"
          accentColor="blue"
        />
        <StatCard
          title="Active Contacts"
          value={kpis?.subscribedContacts ?? 4}
          change="+8%"
          isPositive={true}
          icon={Users}
          description="Subscribed contacts"
          accentColor="green"
        />
        <StatCard
          title="Templates"
          value={kpis?.totalTemplates ?? 2}
          change="+2"
          isPositive={true}
          icon={FileText}
          description="Reusable HTML templates"
          accentColor="purple"
        />
      </div>

      {/* ═══════════════════════════════════════════════ */}
      {/* 3. CHARTS GRID (Line + Donut)                  */}
      {/* ═══════════════════════════════════════════════ */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
        {/* Line Chart */}
        <div className="lg:col-span-2 bg-white rounded-[16px] border border-[#E5E5E7] p-5 space-y-4">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="font-bold text-[15px] text-[#0A0A0B]">Email Dispatch Velocity</h3>
              <p className="text-[12px] text-[#9CA3AF] font-medium mt-0.5">Weekly volume</p>
            </div>
            <button className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-[12px] font-medium bg-[#F3F4F6] text-[#5F6368] border border-[#E5E5E7] cursor-pointer hover:bg-[#E5E7EB] transition-colors">
              7 Days
              <ChevronDown className="w-3 h-3" />
            </button>
          </div>

          <div className="h-60 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={mockChartData}>
                <defs>
                  <linearGradient id="areaGradient" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0%" stopColor="#EC4899" stopOpacity="0.15" />
                    <stop offset="100%" stopColor="#EC4899" stopOpacity="0.0" />
                  </linearGradient>
                </defs>
                <XAxis
                  dataKey="day"
                  stroke="#E5E5E7"
                  tick={{ fontSize: 11, fill: '#9CA3AF', fontWeight: 500 }}
                  tickLine={false}
                  axisLine={false}
                />
                <YAxis
                  stroke="#E5E5E7"
                  tick={{ fontSize: 11, fill: '#9CA3AF', fontWeight: 500 }}
                  tickLine={false}
                  axisLine={false}
                  width={40}
                />
                <Tooltip
                  content={({ active, payload }) => {
                    if (active && payload && payload.length) {
                      return (
                        <div className="bg-[#0A0A0B] text-white px-3 py-2 rounded-lg text-xs shadow-lg">
                          <p className="font-medium text-[#9CA3AF]">{payload[0].payload.day}</p>
                          <p className="font-bold text-sm">{payload[0].value.toLocaleString()} Emails</p>
                        </div>
                      );
                    }
                    return null;
                  }}
                />
                <Line
                  type="monotone"
                  dataKey="sent"
                  stroke="#0A0A0B"
                  strokeWidth={2}
                  dot={{ r: 3, fill: '#0A0A0B', stroke: '#FFFFFF', strokeWidth: 2 }}
                  activeDot={{ r: 5, fill: '#EC4899', stroke: '#FFFFFF', strokeWidth: 2 }}
                />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Donut - Campaign States */}
        <div className="bg-white rounded-[16px] border border-[#E5E5E7] p-5 flex flex-col justify-between">
          <div>
            <h3 className="font-bold text-[15px] text-[#0A0A0B]">Campaign States</h3>
            <p className="text-[12px] text-[#9CA3AF] font-medium mt-0.5">By execution status</p>
          </div>

          <div className="h-40 w-full flex items-center justify-center my-4">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie data={mockDonutData} innerRadius={50} outerRadius={68} paddingAngle={4} dataKey="value">
                  {mockDonutData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} stroke="#FFFFFF" strokeWidth={3} />
                  ))}
                </Pie>
              </PieChart>
            </ResponsiveContainer>
          </div>

          <div className="space-y-2">
            {mockDonutData.map((item) => (
              <div key={item.name} className="flex items-center justify-between py-1.5 px-3 rounded-lg bg-[#FAFAFB]">
                <div className="flex items-center gap-2">
                  <span className="w-2 h-2 rounded-full" style={{ backgroundColor: item.color }} />
                  <span className="text-[12px] font-medium text-[#5F6368]">{item.name}</span>
                </div>
                <span className="text-[12px] font-bold text-[#0A0A0B]">{item.value}%</span>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* ═══════════════════════════════════════════════ */}
      {/* 4. REAL-TIME ACTIVITY STREAM                    */}
      {/* ═══════════════════════════════════════════════ */}
      <div className="bg-white rounded-[16px] border border-[#E5E5E7] p-5 space-y-4">
        <div className="flex items-center justify-between">
          <div>
            <h3 className="font-bold text-[15px] text-[#0A0A0B]">Activity Stream</h3>
            <p className="text-[12px] text-[#9CA3AF] font-medium mt-0.5">Recent dispatches and events</p>
          </div>
          <div className="flex items-center gap-1.5 px-2.5 py-1 rounded-full bg-[#DCFCE7] text-[#16A34A]">
            <span className="w-1.5 h-1.5 rounded-full bg-[#22C55E] animate-pulse" />
            <span className="text-[10px] font-semibold">Live</span>
          </div>
        </div>

        <div className="space-y-1">
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
                className="flex items-center justify-between p-3.5 rounded-xl hover:bg-[#FAFAFB] transition-colors cursor-pointer group"
              >
                <div className="flex items-center gap-3">
                  <div className={`w-8 h-8 rounded-lg flex items-center justify-center flex-shrink-0 ${
                    act.statusType === 'success'
                      ? 'bg-[#DCFCE7] text-[#16A34A]'
                      : 'bg-[#F3E8FF] text-[#A855F7]'
                  }`}>
                    <IconComponent className="w-4 h-4" strokeWidth={1.5} />
                  </div>
                  <div>
                    <p className="text-[13px] font-semibold text-[#0A0A0B]">{act.title}</p>
                    <p className="text-[12px] text-[#9CA3AF] font-medium">{act.desc}</p>
                  </div>
                </div>

                <div className="flex items-center gap-3">
                  <StatusBadge status={act.status} />
                  <span className="text-[11px] font-medium text-[#C0C5CC]">{act.time}</span>
                </div>
              </div>
            );
          })}
        </div>

        <div className="pt-1 text-center border-t border-[#F0F0F2]">
          <button className="text-[12px] font-semibold text-[#5F6368] hover:text-[#0A0A0B] inline-flex items-center gap-1 transition-colors cursor-pointer mt-3">
            View All Activity
            <ArrowRight className="w-3.5 h-3.5" />
          </button>
        </div>
      </div>

      {/* ═══════════════════════════════════════════════ */}
      {/* 5. 3-COLUMN OPERATIONS GRID                     */}
      {/* ═══════════════════════════════════════════════ */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
        {/* Provider Status */}
        <div className="bg-white rounded-[16px] border border-[#E5E5E7] p-5 flex flex-col justify-between">
          <div>
            <h3 className="font-bold text-[15px] text-[#0A0A0B]">Provider Status</h3>
            <p className="text-[12px] text-[#9CA3AF] font-medium mt-0.5">Email delivery engines</p>

            <div className="space-y-2 mt-4">
              {[
                { name: 'Amazon SES', status: 'Healthy' },
                { name: 'Brevo', status: 'Healthy' },
                { name: 'SMTP', status: 'Healthy' },
                { name: 'MailAlly Engine', status: 'Healthy' }
              ].map((prov, i) => (
                <div key={i} className="flex items-center justify-between p-2.5 rounded-lg bg-[#FAFAFB]">
                  <span className="text-[13px] font-medium text-[#0A0A0B]">{prov.name}</span>
                  <span className="text-[11px] font-semibold text-[#16A34A] flex items-center gap-1.5">
                    <span className="w-1.5 h-1.5 rounded-full bg-[#22C55E]" />
                    {prov.status}
                  </span>
                </div>
              ))}
            </div>
          </div>

          <button className="text-[12px] font-semibold text-[#5F6368] hover:text-[#0A0A0B] flex items-center gap-1 cursor-pointer mt-4 transition-colors">
            View All Providers
            <ArrowRight className="w-3.5 h-3.5" />
          </button>
        </div>

        {/* Quick Actions */}
        <div className="bg-white rounded-[16px] border border-[#E5E5E7] p-5">
          <h3 className="font-bold text-[15px] text-[#0A0A0B]">Quick Actions</h3>
          <p className="text-[12px] text-[#9CA3AF] font-medium mt-0.5 mb-4">One-click operations</p>

          <div className="grid grid-cols-2 gap-2.5">
            {[
              { label: 'Create Campaign', icon: Send, accent: 'bg-[#FCE7F3] text-[#EC4899]' },
              { label: 'Upload Contacts', icon: UploadCloud, accent: 'bg-[#DBEAFE] text-[#3B82F6]' },
              { label: 'New Template', icon: FileText, accent: 'bg-[#F3E8FF] text-[#A855F7]' },
              { label: 'Automation', icon: Sliders, accent: 'bg-[#DCFCE7] text-[#22C55E]' }
            ].map((qa, idx) => {
              const IconComp = qa.icon;
              return (
                <div
                  key={idx}
                  className="p-3.5 rounded-xl bg-[#FAFAFB] border border-[#E5E5E7] hover:border-[#D1D5DB] hover:bg-white hover:shadow-sm transition-all cursor-pointer flex flex-col items-center gap-2 group"
                >
                  <div className={`w-9 h-9 rounded-lg ${qa.accent} flex items-center justify-center transition-transform group-hover:scale-105`}>
                    <IconComp className="w-4 h-4" strokeWidth={1.5} />
                  </div>
                  <span className="text-[11px] font-semibold text-[#5F6368] text-center leading-tight">
                    {qa.label}
                  </span>
                </div>
              );
            })}
          </div>
        </div>

        {/* Top Performing Campaign */}
        <div className="bg-white rounded-[16px] border border-[#E5E5E7] p-5 flex flex-col justify-between">
          <div>
            <div className="flex items-center justify-between">
              <div>
                <h3 className="font-bold text-[15px] text-[#0A0A0B]">Top Campaign</h3>
                <p className="text-[12px] text-[#9CA3AF] font-medium mt-0.5">Summer Sale Campaign</p>
              </div>
              <div className="w-9 h-9 rounded-lg bg-[#FFEDD5] text-[#F97316] flex items-center justify-center">
                <Trophy className="w-4 h-4" strokeWidth={1.5} />
              </div>
            </div>

            <div className="grid grid-cols-2 gap-3 mt-5">
              <div className="p-3 rounded-xl bg-[#FAFAFB]">
                <span className="text-[10px] font-semibold uppercase tracking-wider text-[#9CA3AF] block">Emails Sent</span>
                <span className="text-xl font-extrabold text-[#0A0A0B] block mt-1">24,560</span>
              </div>
              <div className="p-3 rounded-xl bg-[#FAFAFB]">
                <span className="text-[10px] font-semibold uppercase tracking-wider text-[#9CA3AF] block">Open Rate</span>
                <span className="text-xl font-extrabold text-[#0A0A0B] block mt-1">42.7%</span>
              </div>
            </div>
          </div>

          <button className="text-[12px] font-semibold text-[#5F6368] hover:text-[#0A0A0B] flex items-center gap-1 cursor-pointer mt-4 transition-colors">
            View Report
            <ArrowRight className="w-3.5 h-3.5" />
          </button>
        </div>
      </div>

      {/* ═══════════════════════════════════════════════ */}
      {/* 6. ANALYTICS OVERVIEW                           */}
      {/* ═══════════════════════════════════════════════ */}
      <div className="bg-white rounded-[16px] border border-[#E5E5E7] p-5 space-y-5">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
          <div>
            <h3 className="font-bold text-[15px] text-[#0A0A0B]">Analytics Overview</h3>
            <p className="text-[12px] text-[#9CA3AF] font-medium mt-0.5">Track performance and engagement</p>
          </div>
          <div className="flex items-center gap-2">
            <button className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-[12px] font-medium bg-white border border-[#E5E5E7] text-[#5F6368] hover:bg-[#F9FAFB] transition-colors cursor-pointer">
              <Calendar className="w-3.5 h-3.5 text-[#9CA3AF]" />
              May 10 - May 16
            </button>
            <button className="flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-[12px] font-medium bg-white border border-[#E5E5E7] text-[#5F6368] hover:bg-[#F9FAFB] transition-colors cursor-pointer">
              <Filter className="w-3.5 h-3.5 text-[#9CA3AF]" />
              Filter
            </button>
          </div>
        </div>

        {/* KPI Pills */}
        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-3">
          {[
            { label: 'Emails Sent', val: '80,920', trend: '↑ 38.1%', positive: true },
            { label: 'Open Rate', val: '42.7%', trend: '↑ 12.4%', positive: true },
            { label: 'Click Rate', val: '11.3%', trend: '↑ 8.7%', positive: true },
            { label: 'Bounce Rate', val: '2.1%', trend: '↓ 0.4%', positive: true },
            { label: 'Unsubscribe', val: '0.6%', trend: '↑ 0.1%', positive: false }
          ].map((pill, idx) => (
            <div key={idx} className="p-3 rounded-xl bg-[#FAFAFB] border border-[#F0F0F2]">
              <span className="text-[10px] font-semibold text-[#9CA3AF] uppercase tracking-wider block">{pill.label}</span>
              <div className="flex items-baseline justify-between mt-1.5">
                <span className="text-lg font-extrabold text-[#0A0A0B]">{pill.val}</span>
                <span className={`text-[10px] font-semibold ${pill.positive ? 'text-[#16A34A]' : 'text-[#E11D48]'}`}>
                  {pill.trend}
                </span>
              </div>
            </div>
          ))}
        </div>

        {/* 3 Mini Cards */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-4 pt-1">
          {/* Performance Over Time */}
          <div className="p-4 rounded-xl border border-[#E5E5E7] space-y-3">
            <h5 className="text-[12px] font-semibold text-[#5F6368] uppercase tracking-wider">Performance Over Time</h5>
            <div className="h-36 w-full">
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={mockMultiLineData}>
                  <Line type="monotone" dataKey="sent" stroke="#0A0A0B" strokeWidth={1.5} dot={false} />
                  <Line type="monotone" dataKey="opens" stroke="#EC4899" strokeWidth={1.5} dot={false} />
                  <Line type="monotone" dataKey="clicks" stroke="#C0C5CC" strokeWidth={1.5} dot={false} />
                </LineChart>
              </ResponsiveContainer>
            </div>
          </div>

          {/* Top Locations */}
          <div className="p-4 rounded-xl border border-[#E5E5E7] space-y-3">
            <h5 className="text-[12px] font-semibold text-[#5F6368] uppercase tracking-wider">Top Locations</h5>
            <div className="space-y-2.5">
              {[
                { country: 'India', percent: '45.2%' },
                { country: 'USA', percent: '25.6%' },
                { country: 'UK', percent: '8.7%' },
                { country: 'Canada', percent: '6.3%' },
                { country: 'Others', percent: '14.2%' }
              ].map((loc, i) => (
                <div key={i} className="flex items-center justify-between">
                  <span className="text-[13px] font-medium text-[#5F6368]">{loc.country}</span>
                  <span className="text-[13px] font-bold text-[#0A0A0B]">{loc.percent}</span>
                </div>
              ))}
            </div>
          </div>

          {/* Device Breakdown */}
          <div className="p-4 rounded-xl border border-[#E5E5E7] flex flex-col justify-between">
            <h5 className="text-[12px] font-semibold text-[#5F6368] uppercase tracking-wider">Device Breakdown</h5>
            <div className="h-28 w-full flex items-center justify-center my-2">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie data={mockDeviceData} innerRadius={35} outerRadius={50} paddingAngle={3} dataKey="value">
                    {mockDeviceData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} stroke="#FFFFFF" strokeWidth={2} />
                    ))}
                  </Pie>
                </PieChart>
              </ResponsiveContainer>
            </div>
            <div className="flex items-center justify-between text-[11px] font-medium text-[#5F6368]">
              <span className="flex items-center gap-1"><span className="w-2 h-2 rounded-full bg-[#0A0A0B]" />Desktop 58%</span>
              <span className="flex items-center gap-1"><span className="w-2 h-2 rounded-full bg-[#EC4899]" />Mobile 29%</span>
              <span className="flex items-center gap-1"><span className="w-2 h-2 rounded-full bg-[#E5E7EB]" />Tablet 13%</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
