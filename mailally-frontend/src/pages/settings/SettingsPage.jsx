import React, { useState, useEffect } from 'react';
import { settingsApi } from '../../api/extraApis';
import { useToast } from '../../components/common/Toast';
import { Save, Building, Shield, Mail, Sparkles, Check, RefreshCw } from 'lucide-react';

export const SettingsPage = () => {
  const [activeTab, setActiveTab] = useState('GENERAL');
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const { addToast } = useToast();

  // Settings State Map
  const [settings, setSettings] = useState({
    GENERAL: {
      organization_name: 'Acme Corporation',
      timezone: 'UTC',
      reply_to: 'support@acme.com',
    },
    BRAND: {
      brand_color: '#EC4899',
      brand_footer: '© 2026 Acme Corp. All rights reserved.',
      logo_url: 'https://acme.com/logo.png',
    },
    SECURITY: {
      enforce_2fa: 'true',
      session_timeout_mins: '60',
      ip_whitelist: '192.168.1.1, 10.0.0.1',
    },
    EMAIL: {
      daily_send_limit: '50000',
      smtp_fallback_enabled: 'true',
      unsubscribe_header: 'enabled',
    },
    AI: {
      default_ai_tone: 'PROFESSIONAL',
      ai_creativity_level: 'HIGH',
      max_tokens_per_gen: '1000',
    }
  });

  useEffect(() => {
    const fetchSettings = async () => {
      setLoading(true);
      try {
        const res = await settingsApi.getSettings();
        if (res && typeof res === 'object') {
          // If backend returns map or array, merge into state
          if (Array.isArray(res)) {
            const mapped = { ...settings };
            res.forEach(item => {
              if (item.category && mapped[item.category]) {
                mapped[item.category][item.settingKey] = item.settingValue;
              }
            });
            setSettings(mapped);
          }
        }
      } catch (err) {
        console.warn('Backend settings fetch fallback to default:', err);
      } finally {
        setLoading(false);
      }
    };
    fetchSettings();
  }, []);

  const handleInputChange = (category, key, value) => {
    setSettings(prev => ({
      ...prev,
      [category]: {
        ...prev[category],
        [key]: value
      }
    }));
  };

  const handleSave = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      const currentCategorySettings = settings[activeTab] || {};
      const savePromises = Object.entries(currentCategorySettings).map(([key, val]) =>
        settingsApi.updateSetting(activeTab, key, String(val))
      );
      await Promise.all(savePromises);
      addToast(`Saved ${activeTab.toLowerCase()} settings to database!`, 'success');
    } catch (err) {
      addToast('Failed to save settings: ' + (err.response?.data?.message || err.message), 'error');
    } finally {
      setSaving(false);
    }
  };

  const tabs = [
    { key: 'GENERAL', label: 'General', icon: Building },
    { key: 'BRAND', label: 'Branding', icon: Sparkles },
    { key: 'SECURITY', label: 'Security', icon: Shield },
    { key: 'EMAIL', label: 'Email Relay', icon: Mail },
    { key: 'AI', label: 'AI Studio', icon: Sparkles },
  ];

  return (
    <div className="space-y-6 animate-fadeInUp pb-8 max-w-4xl font-sans">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-extrabold tracking-tight text-[#0A0A0B]">Workspace Settings</h1>
          <p className="text-[13px] text-[#71717A] font-medium mt-1">
            Configure organization preferences, dispatch parameters, security policies, and AI tokens.
          </p>
        </div>
      </div>

      {/* Tabs */}
      <div className="flex gap-2 overflow-x-auto pb-1 border-b border-[#E4E4E7]">
        {tabs.map((t) => {
          const TabIcon = t.icon;
          const isActive = activeTab === t.key;
          return (
            <button
              key={t.key}
              onClick={() => setActiveTab(t.key)}
              className={`px-4 py-2.5 rounded-xl text-xs font-bold flex items-center gap-2 transition-all cursor-pointer whitespace-nowrap ${
                isActive
                  ? 'bg-[#18181B] text-white shadow-xs'
                  : 'bg-white text-[#71717A] border border-[#E4E4E7] hover:bg-[#FAFAFA] hover:text-[#18181B]'
              }`}
            >
              <TabIcon className="w-3.5 h-3.5" />
              <span>{t.label}</span>
            </button>
          );
        })}
      </div>

      {/* Form Content */}
      <div className="bg-white rounded-[24px] border border-[#18181B] p-6 lg:p-8 shadow-xs">
        <form onSubmit={handleSave} className="space-y-6">
          
          {activeTab === 'GENERAL' && (
            <div className="space-y-5">
              <div>
                <label className="block text-xs font-bold text-[#18181B] mb-1.5">
                  Organization Name
                </label>
                <input
                  type="text"
                  value={settings.GENERAL.organization_name}
                  onChange={(e) => handleInputChange('GENERAL', 'organization_name', e.target.value)}
                  className="w-full h-11 px-4 rounded-xl border border-[#18181B] text-xs font-semibold text-[#18181B] outline-none focus:ring-2 focus:ring-black/5"
                  required
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-[#18181B] mb-1.5">
                  Default Dispatch Timezone
                </label>
                <select
                  value={settings.GENERAL.timezone}
                  onChange={(e) => handleInputChange('GENERAL', 'timezone', e.target.value)}
                  className="w-full h-11 px-4 rounded-xl border border-[#18181B] text-xs font-semibold text-[#18181B] outline-none bg-white"
                >
                  <option value="UTC">UTC (Coordinated Universal Time)</option>
                  <option value="EST">EST (Eastern Standard Time)</option>
                  <option value="PST">PST (Pacific Standard Time)</option>
                  <option value="IST">IST (Indian Standard Time)</option>
                </select>
              </div>

              <div>
                <label className="block text-xs font-bold text-[#18181B] mb-1.5">
                  Default Reply-To Address
                </label>
                <input
                  type="email"
                  value={settings.GENERAL.reply_to}
                  onChange={(e) => handleInputChange('GENERAL', 'reply_to', e.target.value)}
                  className="w-full h-11 px-4 rounded-xl border border-[#18181B] text-xs font-semibold text-[#18181B] outline-none"
                  required
                />
              </div>
            </div>
          )}

          {activeTab === 'BRAND' && (
            <div className="space-y-5">
              <div>
                <label className="block text-xs font-bold text-[#18181B] mb-1.5">
                  Primary Accent Color (Hex)
                </label>
                <div className="flex items-center gap-3">
                  <input
                    type="color"
                    value={settings.BRAND.brand_color}
                    onChange={(e) => handleInputChange('BRAND', 'brand_color', e.target.value)}
                    className="w-10 h-10 rounded-lg border border-[#18181B] cursor-pointer"
                  />
                  <input
                    type="text"
                    value={settings.BRAND.brand_color}
                    onChange={(e) => handleInputChange('BRAND', 'brand_color', e.target.value)}
                    className="w-full h-11 px-4 rounded-xl border border-[#18181B] text-xs font-semibold text-[#18181B] outline-none"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-bold text-[#18181B] mb-1.5">
                  Custom Email Footer Copyright HTML
                </label>
                <input
                  type="text"
                  value={settings.BRAND.brand_footer}
                  onChange={(e) => handleInputChange('BRAND', 'brand_footer', e.target.value)}
                  className="w-full h-11 px-4 rounded-xl border border-[#18181B] text-xs font-semibold text-[#18181B] outline-none"
                />
              </div>
            </div>
          )}

          {activeTab === 'SECURITY' && (
            <div className="space-y-5">
              <div className="flex items-center justify-between p-4 rounded-xl border border-[#E4E4E7] bg-[#FAFAFA]">
                <div>
                  <h4 className="text-xs font-bold text-[#18181B]">Enforce Two-Factor Authentication (2FA)</h4>
                  <p className="text-[11px] text-[#71717A] font-medium">Require TOTP authenticator for all organization members</p>
                </div>
                <input
                  type="checkbox"
                  checked={settings.SECURITY.enforce_2fa === 'true'}
                  onChange={(e) => handleInputChange('SECURITY', 'enforce_2fa', e.target.checked ? 'true' : 'false')}
                  className="w-4 h-4 rounded border-[#18181B] accent-[#18181B] cursor-pointer"
                />
              </div>

              <div>
                <label className="block text-xs font-bold text-[#18181B] mb-1.5">
                  Session Timeout (Minutes)
                </label>
                <input
                  type="number"
                  value={settings.SECURITY.session_timeout_mins}
                  onChange={(e) => handleInputChange('SECURITY', 'session_timeout_mins', e.target.value)}
                  className="w-full h-11 px-4 rounded-xl border border-[#18181B] text-xs font-semibold text-[#18181B] outline-none"
                />
              </div>
            </div>
          )}

          {activeTab === 'EMAIL' && (
            <div className="space-y-5">
              <div>
                <label className="block text-xs font-bold text-[#18181B] mb-1.5">
                  Daily Sending Quota Limit
                </label>
                <input
                  type="number"
                  value={settings.EMAIL.daily_send_limit}
                  onChange={(e) => handleInputChange('EMAIL', 'daily_send_limit', e.target.value)}
                  className="w-full h-11 px-4 rounded-xl border border-[#18181B] text-xs font-semibold text-[#18181B] outline-none"
                />
              </div>

              <div className="flex items-center justify-between p-4 rounded-xl border border-[#E4E4E7] bg-[#FAFAFA]">
                <div>
                  <h4 className="text-xs font-bold text-[#18181B]">Automatic SMTP Provider Fallback</h4>
                  <p className="text-[11px] text-[#71717A] font-medium">Reroute emails automatically if primary provider triggers 4xx/5xx bounce</p>
                </div>
                <input
                  type="checkbox"
                  checked={settings.EMAIL.smtp_fallback_enabled === 'true'}
                  onChange={(e) => handleInputChange('EMAIL', 'smtp_fallback_enabled', e.target.checked ? 'true' : 'false')}
                  className="w-4 h-4 rounded border-[#18181B] accent-[#18181B] cursor-pointer"
                />
              </div>
            </div>
          )}

          {activeTab === 'AI' && (
            <div className="space-y-5">
              <div>
                <label className="block text-xs font-bold text-[#18181B] mb-1.5">
                  Default AI Generation Tone
                </label>
                <select
                  value={settings.AI.default_ai_tone}
                  onChange={(e) => handleInputChange('AI', 'default_ai_tone', e.target.value)}
                  className="w-full h-11 px-4 rounded-xl border border-[#18181B] text-xs font-semibold text-[#18181B] outline-none bg-white"
                >
                  <option value="PROFESSIONAL">Professional & Authoritative</option>
                  <option value="PERSUASIVE">Persuasive Cold Outreach</option>
                  <option value="FRIENDLY">Friendly & Casual</option>
                  <option value="URGENT">Urgent Call to Action</option>
                </select>
              </div>

              <div>
                <label className="block text-xs font-bold text-[#18181B] mb-1.5">
                  Max Output Tokens Per Sequence
                </label>
                <input
                  type="number"
                  value={settings.AI.max_tokens_per_gen}
                  onChange={(e) => handleInputChange('AI', 'max_tokens_per_gen', e.target.value)}
                  className="w-full h-11 px-4 rounded-xl border border-[#18181B] text-xs font-semibold text-[#18181B] outline-none"
                />
              </div>
            </div>
          )}

          {/* Submit Action */}
          <div className="pt-4 border-t border-[#E4E4E7] flex justify-end">
            <button
              type="submit"
              disabled={saving}
              className="h-11 px-6 rounded-xl bg-[#18181B] hover:bg-black text-white text-xs font-bold flex items-center gap-2 transition-all cursor-pointer shadow-sm"
            >
              {saving ? (
                <>
                  <RefreshCw className="w-4 h-4 animate-spin text-[#10B981]" />
                  <span>Persisting to DB...</span>
                </>
              ) : (
                <>
                  <Save className="w-4 h-4" />
                  <span>Save Configuration to Database</span>
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
