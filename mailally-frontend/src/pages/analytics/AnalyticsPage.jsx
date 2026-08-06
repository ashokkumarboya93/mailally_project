import React from 'react';
import { StatCard } from '../../components/common/StatCard';
import { TrendingUp, Mail, CheckCircle2, BarChart3, Zap } from 'lucide-react';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';

export const AnalyticsPage = () => {
  const analyticsData = [
    { provider: 'SMTP Engine', sent: 24500, success: 99.2 },
    { provider: 'Brevo Engine', sent: 15200, success: 98.8 },
    { provider: 'AWS SES SDK', sent: 8590, success: 99.6 }
  ];

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

        {/* Left Column Content */}
        <div className="space-y-3.5 max-w-sm relative z-10">
          <div className="inline-flex items-center space-x-2 px-3 py-1 rounded-full bg-white/15 text-white font-black text-[10px] border border-white/25 shadow-3xs backdrop-blur-md">
            <Zap className="w-3 h-3 text-[#00DDFF]" />
            <span>Deliverability Intelligence</span>
          </div>

          <h1 className="text-2xl sm:text-3xl font-black tracking-tight leading-none text-white" style={{ fontFamily: 'var(--font-heading)' }}>
            Analytics <br />
            <span className="text-[#00DDFF]">Hub</span>
          </h1>

          <p className="text-[10px] sm:text-[11px] text-blue-50 leading-relaxed font-medium">
            Real-time delivery rates, open performance metrics, and multi-provider delivery comparisons.
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

        {/* Right SLA Card */}
        <div className="bg-white/95 backdrop-blur-md rounded-2xl p-4 shadow-xl border border-white/20 z-10 text-center w-full sm:w-56">
          <span className="text-[9px] uppercase font-black text-slate-400 block tracking-wider">PLATFORM SLA</span>
          <span className="text-xl font-black text-[#2563EB] block mt-0.5" style={{ fontFamily: 'var(--font-heading)' }}>99.4% Delivered</span>
        </div>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-5">
        <StatCard title="Overall Delivery Rate" value="99.4%" change="+0.4%" isPositive={true} icon={CheckCircle2} description="Successful inbox placement" />
        <StatCard title="Average Open Rate" value="34.2%" change="+2.1%" isPositive={true} icon={Mail} description="Audience engagement level" />
        <StatCard title="Click-Through Rate" value="12.8%" change="+1.2%" isPositive={true} icon={TrendingUp} description="Link interaction conversion" />
      </div>

      {/* Bar Chart Container */}
      <div className="bg-white rounded-[22px] p-6 border shadow-xs space-y-4" style={{ borderColor: 'rgba(37,99,235,0.08)' }}>
        <div className="flex items-center justify-between">
          <div>
            <h3 className="font-black text-lg text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
              Provider Delivery Comparison
            </h3>
            <p className="text-xs text-slate-400 font-medium">Volume dispatched across active email sending engines</p>
          </div>
          <span className="px-3.5 py-1.5 rounded-full text-xs font-black bg-blue-50 text-[#2563EB] border border-blue-100">
            99.4% Delivery SLA
          </span>
        </div>

        <div className="h-64 w-full pt-4">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={analyticsData}>
              <XAxis dataKey="provider" stroke="#64748B" fontSize={11} tickLine={false} />
              <YAxis stroke="#64748B" fontSize={11} tickLine={false} />
              <Tooltip
                contentStyle={{
                  backgroundColor: '#FFFFFF',
                  borderRadius: '16px',
                  border: '1px solid rgba(37,99,235,0.12)',
                  boxShadow: '0 10px 25px rgba(0,0,0,0.08)',
                  color: '#1E293B',
                  fontSize: '12px',
                  fontWeight: 800,
                }}
              />
              <Bar dataKey="sent" fill="#2563EB" radius={[10, 10, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};
