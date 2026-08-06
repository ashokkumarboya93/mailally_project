import React, { useState } from 'react';
import { settingsApi } from '../../api/extraApis';
import { Save, Settings as SettingsIcon } from 'lucide-react';

export const SettingsPage = () => {
  const [activeTab, setActiveTab] = useState('GENERAL');
  const [orgName, setOrgName] = useState('Acme Corporation');
  const [timezone, setTimezone] = useState('UTC');
  const [saving, setSaving] = useState(false);

  const handleSave = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await settingsApi.updateSetting('GENERAL', 'organization_name', orgName);
      alert('Settings updated successfully!');
    } catch (err) {
      alert('Failed to update settings: ' + (err.response?.data?.message || err.message));
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="space-y-8 animate-fadeInUp font-sans max-w-5xl mx-auto pb-12">
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
            <SettingsIcon className="w-3 h-3 text-[#00DDFF]" />
            <span>Workspace Configurations</span>
          </div>

          <h1 className="text-2xl sm:text-3xl font-black tracking-tight leading-none text-white" style={{ fontFamily: 'var(--font-heading)' }}>
            Organization <br />
            <span className="text-[#00DDFF]">Settings</span>
          </h1>

          <p className="text-[10px] sm:text-[11px] text-blue-50 leading-relaxed font-medium">
            Configure workspace identity, sending domains, API integration keys, and security controls.
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

      {/* Tabs */}
      <div className="flex space-x-2 pb-2 overflow-x-auto">
        {['GENERAL', 'BRAND', 'SECURITY', 'EMAIL', 'AI'].map((tab) => (
          <button
            key={tab}
            onClick={() => setActiveTab(tab)}
            className={`px-4.5 py-2.5 rounded-xl text-xs font-black transition-all cursor-pointer ${activeTab === tab ? 'bg-[#2563EB] text-white shadow-md shadow-blue-500/20' : 'bg-white text-slate-600 border border-slate-200 hover:bg-slate-50'}`}
          >
            {tab}
          </button>
        ))}
      </div>

      {/* Settings Form Card */}
      <div className="bg-white rounded-[22px] p-6 border space-y-4 shadow-xs" style={{ borderColor: 'rgba(37,99,235,0.08)' }}>
        <form onSubmit={handleSave} className="space-y-4 font-sans">
          <div>
            <label className="block text-xs font-black mb-1.5 uppercase tracking-wider text-slate-400">
              Organization Name
            </label>
            <input
              type="text"
              value={orgName}
              onChange={(e) => setOrgName(e.target.value)}
              className="w-full p-3 rounded-xl border border-slate-200 text-xs font-semibold focus:outline-none focus:border-[#2563EB] bg-white"
            />
          </div>

          <div>
            <label className="block text-xs font-black mb-1.5 uppercase tracking-wider text-slate-400">
              Default Dispatch Timezone
            </label>
            <select
              value={timezone}
              onChange={(e) => setTimezone(e.target.value)}
              className="w-full p-3 rounded-xl border border-slate-200 text-xs font-semibold focus:outline-none focus:border-[#2563EB] bg-white"
            >
              <option value="UTC">UTC (Coordinated Universal Time)</option>
              <option value="EST">EST (Eastern Standard Time)</option>
              <option value="PST">PST (Pacific Standard Time)</option>
              <option value="IST">IST (Indian Standard Time)</option>
            </select>
          </div>

          <button
            type="submit"
            disabled={saving}
            className="px-6 py-3 rounded-xl text-xs font-black text-white bg-[#2563EB] hover:bg-[#1D4ED8] transition-all cursor-pointer shadow-md shadow-blue-500/25 flex items-center space-x-2"
          >
            <Save className="w-4 h-4" />
            <span>{saving ? 'Saving Settings...' : 'Save Configuration'}</span>
          </button>
        </form>
      </div>
    </div>
  );
};
