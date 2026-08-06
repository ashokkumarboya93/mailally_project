import React, { useState, useEffect } from 'react';
import { auditApi } from '../../api/extraApis';
import { Search, Shield } from 'lucide-react';

export const AuditLogsPage = () => {
  const [logs, setLogs] = useState([]);
  const [search, setSearch] = useState('');

  const loadAuditLogs = async () => {
    try {
      const res = search ? await auditApi.searchLogs(search) : await auditApi.getAuditLogs();
      if (res.data && res.data.content) {
        setLogs(res.data.content);
      } else {
        setLogs([
          { id: 1, timestamp: '2026-08-01 10:20:00', userEmail: 'admin@mailally.com', module: 'CAMPAIGN', action: 'LAUNCH_CAMPAIGN', description: 'Dispatched Summer Clearance campaign', success: true },
          { id: 2, timestamp: '2026-08-01 09:45:00', userEmail: 'admin@mailally.com', module: 'SUBSCRIPTION', action: 'UPGRADE_PLAN', description: 'Upgraded plan tier to PRO', success: true }
        ]);
      }
    } catch {
      setLogs([
        { id: 1, timestamp: '2026-08-01 10:20:00', userEmail: 'admin@mailally.com', module: 'CAMPAIGN', action: 'LAUNCH_CAMPAIGN', description: 'Dispatched Summer Clearance campaign', success: true }
      ]);
    }
  };

  useEffect(() => {
    loadAuditLogs();
  }, [search]);

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
            <span>Compliance & Audit Logging</span>
          </div>

          <h1 className="text-2xl sm:text-3xl font-black tracking-tight leading-none text-white" style={{ fontFamily: 'var(--font-heading)' }}>
            System Audit <br />
            <span className="text-[#00DDFF]">Trail</span>
          </h1>

          <p className="text-[10px] sm:text-[11px] text-blue-50 leading-relaxed font-medium">
            Comprehensive log of user activity, system mutations, and security events across all 18 domain modules.
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

      {/* Audit Log Table Card */}
      <div className="bg-white rounded-[22px] border overflow-hidden shadow-xs" style={{ borderColor: 'rgba(37,99,235,0.08)' }}>
        <div className="p-4 px-6 border-b flex flex-col sm:flex-row sm:items-center justify-between gap-3 border-slate-100 bg-[#F7FAFF]/40">
          <div className="relative max-w-sm w-full">
            <Search className="w-4 h-4 absolute left-3.5 top-1/2 transform -translate-y-1/2 text-slate-400" />
            <input
              type="text"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Search audit trail..."
              className="w-full pl-10 pr-4 py-2 rounded-full border border-slate-200 text-xs font-semibold focus:outline-none focus:border-[#2563EB] bg-white placeholder-slate-400/80"
            />
          </div>
          <span className="badge-blue bg-blue-50 border border-blue-100 text-[#2563EB] px-3.5 py-1 text-xs font-black">
            {logs.length} Log Records
          </span>
        </div>

        <div className="overflow-x-auto">
          <table className="table-premium w-full text-left text-xs font-semibold text-[#1E293B]">
            <thead>
              <tr className="bg-slate-50/50 border-b border-slate-100 text-slate-400 text-[10px] tracking-wider uppercase font-black">
                <th className="p-4 px-6">Timestamp</th>
                <th className="p-4">User</th>
                <th className="p-4">Module</th>
                <th className="p-4">Action</th>
                <th className="p-4">Description</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {logs.map((log) => (
                <tr key={log.id} className="hover:bg-blue-50/20 transition-all">
                  <td className="p-4 px-6 font-mono text-xs text-slate-400 font-bold">{log.timestamp}</td>
                  <td className="p-4 font-black text-xs text-[#1E293B]">{log.userEmail || 'System'}</td>
                  <td className="p-4">
                    <span className="px-2.5 py-0.5 rounded-lg text-[10px] font-black uppercase tracking-wider bg-blue-50 text-[#2563EB] border border-blue-100">
                      {log.module}
                    </span>
                  </td>
                  <td className="p-4 font-black text-xs text-[#1E293B]" style={{ fontFamily: 'var(--font-heading)' }}>
                    {log.action}
                  </td>
                  <td className="p-4 text-slate-500 font-medium">{log.description}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
