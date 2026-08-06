import React, { useState } from 'react';
import { StatusBadge } from '../../components/common/StatusBadge';
import { Shield } from 'lucide-react';

export const UsersPage = () => {
  const [users] = useState([
    { id: 1, email: 'admin@mailally.com', firstName: 'Admin', lastName: 'User', role: 'ADMIN', status: 'ACTIVE' },
    { id: 2, email: 'manager@mailally.com', firstName: 'Sarah', lastName: 'Connor', role: 'MANAGER', status: 'ACTIVE' },
    { id: 3, email: 'member@mailally.com', firstName: 'Dave', lastName: 'Miller', role: 'MEMBER', status: 'ACTIVE' }
  ]);

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
            <Shield className="w-3 h-3 text-[#00DDFF]" />
            <span>Role-Based Access Subsystem</span>
          </div>

          <h1 className="text-2xl sm:text-3xl font-black tracking-tight leading-none text-white" style={{ fontFamily: 'var(--font-heading)' }}>
            Organization <br />
            <span className="text-[#00DDFF]">Team</span>
          </h1>

          <p className="text-[10px] sm:text-[11px] text-blue-50 leading-relaxed font-medium">
            Manage workspace team members, security roles, and user access credentials.
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

      {/* Users Cards Grid */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-5 pt-2">
        {users.map((u) => (
          <div 
            key={u.id} 
            className="bg-white rounded-[22px] p-5 flex flex-col justify-between space-y-4 border transition-all duration-300 hover:-translate-y-1 hover:shadow-lg"
            style={{
              borderColor: 'rgba(37,99,235,0.08)',
              boxShadow: '0 10px 30px -5px rgba(37,99,235,0.04)',
            }}
          >
            <div className="flex items-start justify-between">
              <div className="w-10 h-10 rounded-2xl bg-gradient-to-br from-blue-50 to-blue-100 text-[#2563EB] flex items-center justify-center font-black text-base border border-blue-100 shadow-3xs" style={{ fontFamily: 'var(--font-heading)' }}>
                {u.firstName.charAt(0)}
              </div>
              <StatusBadge status={u.status} />
            </div>

            <div>
              <h3 className="font-black text-base text-[#1E293B]" style={{ fontFamily: 'var(--font-heading)' }}>
                {u.firstName} {u.lastName}
              </h3>
              <p className="text-xs font-mono mt-0.5 text-slate-400 font-bold">
                {u.email}
              </p>
            </div>

            <div className="pt-3 border-t border-slate-100 flex items-center justify-between">
              <span className="text-[9px] font-black uppercase tracking-wider text-slate-400">
                ROLE
              </span>
              <span className="px-2.5 py-0.5 rounded-lg text-[10px] font-black uppercase tracking-wider bg-blue-50 text-[#2563EB] border border-blue-100">
                {u.role}
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
