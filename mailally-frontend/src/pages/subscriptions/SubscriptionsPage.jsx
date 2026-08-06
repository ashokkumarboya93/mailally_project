import React, { useState, useEffect } from 'react';
import { subscriptionApi } from '../../api/extraApis';
import { Check, Sparkles, Zap } from 'lucide-react';

export const SubscriptionsPage = () => {
  const [sub, setSub] = useState(null);

  const loadSub = async () => {
    try {
      const res = await subscriptionApi.getSubscription();
      if (res.data) setSub(res.data);
    } catch {
      setSub({
        code: 'PRO',
        name: 'Professional Plan',
        price: 99.00,
        maxContacts: 25000,
        maxEmailsPerMonth: 150000,
        maxUsers: 15,
        status: 'ACTIVE'
      });
    }
  };

  useEffect(() => {
    loadSub();
  }, []);

  const handleUpgrade = async (code) => {
    try {
      await subscriptionApi.upgradePlan(code);
      alert(`Upgraded subscription to ${code} tier!`);
      loadSub();
    } catch (err) {
      alert('Upgrade failed: ' + (err.response?.data?.message || err.message));
    }
  };

  const plans = [
    { code: 'FREE', name: 'Free Tier', price: 0, contacts: '1,000', emails: '5,000/mo', users: '2 Users' },
    { code: 'STARTER', name: 'Starter Plan', price: 29, contacts: '5,000', emails: '25,000/mo', users: '5 Users' },
    { code: 'PRO', name: 'Professional Plan', price: 99, contacts: '25,000', emails: '150,000/mo', users: '15 Users', popular: true },
    { code: 'BUSINESS', name: 'Business Plan', price: 299, contacts: '100,000', emails: '750,000/mo', users: '50 Users' },
    { code: 'ENTERPRISE', name: 'Enterprise Plan', price: 999, contacts: '1,000,000', emails: '10,000,000/mo', users: '500 Users' }
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
            <span>Subscription Allocation</span>
          </div>

          <h1 className="text-2xl sm:text-3xl font-black tracking-tight leading-none text-white" style={{ fontFamily: 'var(--font-heading)' }}>
            Subscription <br />
            <span className="text-[#00DDFF]">Plans</span>
          </h1>

          <p className="text-[10px] sm:text-[11px] text-blue-50 leading-relaxed font-medium">
            Manage plan tier limits, feature allocations, and enterprise capacity upgrades.
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

      {/* Pricing Cards Grid */}
      <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-5 gap-5 pt-2">
        {plans.map((p) => {
          const isCurrent = sub?.code === p.code;
          return (
            <div
              key={p.code}
              className="bg-white rounded-[22px] p-5 flex flex-col justify-between relative transition-all duration-300 hover:-translate-y-1 hover:shadow-lg border"
              style={{
                borderColor: isCurrent ? '#2563EB' : 'rgba(37,99,235,0.08)',
                borderWidth: isCurrent ? '2px' : '1px',
                boxShadow: isCurrent ? '0 10px 30px -5px rgba(37, 99, 235, 0.18)' : '0 8px 20px -6px rgba(37,99,235,0.03)',
              }}
            >
              {p.popular && (
                <span
                  className="absolute -top-3 left-1/2 transform -translate-x-1/2 text-white text-[9px] font-black uppercase tracking-wider px-3 py-1 rounded-full shadow-md flex items-center space-x-1 bg-[#2563EB]"
                >
                  <Sparkles className="w-2.5 h-2.5" />
                  <span>Popular</span>
                </span>
              )}
              <div>
                <h3 className="font-black text-sm mt-1 text-[#1E293B]" style={{ fontFamily: 'var(--font-heading)' }}>
                  {p.name}
                </h3>
                <div className="my-3">
                  <span className="text-2xl font-black text-[#2563EB]" style={{ fontFamily: 'var(--font-heading)' }}>
                    ${p.price}
                  </span>
                  <span className="text-xs text-slate-400 font-bold ml-1">/mo</span>
                </div>
                <ul className="space-y-2.5 text-xs mb-5 text-slate-600 font-medium">
                  <li className="flex items-center font-bold">
                    <Check className="w-3.5 h-3.5 mr-1.5 text-[#2563EB] flex-shrink-0" />
                    {p.contacts} Contacts
                  </li>
                  <li className="flex items-center font-bold">
                    <Check className="w-3.5 h-3.5 mr-1.5 text-[#2563EB] flex-shrink-0" />
                    {p.emails} Emails
                  </li>
                  <li className="flex items-center font-bold">
                    <Check className="w-3.5 h-3.5 mr-1.5 text-[#2563EB] flex-shrink-0" />
                    {p.users}
                  </li>
                </ul>
              </div>
              <button
                onClick={() => handleUpgrade(p.code)}
                disabled={isCurrent}
                className={isCurrent ? 'w-full py-2.5 rounded-xl text-xs font-black bg-blue-50 text-[#2563EB] border border-blue-150 cursor-default opacity-80' : 'w-full py-2.5 rounded-xl text-xs font-black text-white bg-[#2563EB] hover:bg-[#1D4ED8] transition-all cursor-pointer shadow-md shadow-blue-500/20'}
              >
                {isCurrent ? 'Current Plan' : 'Select Plan'}
              </button>
            </div>
          );
        })}
      </div>
    </div>
  );
};
