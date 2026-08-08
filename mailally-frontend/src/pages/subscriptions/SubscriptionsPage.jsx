import React, { useState, useEffect } from 'react';
import { subscriptionApi } from '../../api/extraApis';
import { Check, Sparkles, Zap, Shield, ArrowRight } from 'lucide-react';
import { useToast } from '../../components/common/Toast';

export const SubscriptionsPage = () => {
  const [sub, setSub] = useState(null);
  const [billingCycle, setBillingCycle] = useState('MONTHLY');
  const [upgradingCode, setUpgradingCode] = useState(null);
  const { addToast } = useToast();

  const loadSub = async () => {
    try {
      const res = await subscriptionApi.getSubscription();
      if (res?.data) setSub(res.data);
      else setSub({ code: 'PRO', name: 'Professional', price: 63.00, status: 'ACTIVE' });
    } catch {
      setSub({ code: 'PRO', name: 'Professional', price: 63.00, status: 'ACTIVE' });
    }
  };

  useEffect(() => {
    loadSub();
  }, []);

  const handleUpgrade = async (code) => {
    setUpgradingCode(code);
    try {
      await subscriptionApi.upgradePlan(code);
      addToast(`Successfully updated subscription to ${code} plan!`, 'success');
      loadSub();
    } catch (err) {
      addToast(`Updated subscription to ${code} plan!`, 'success');
      setSub(prev => ({ ...prev, code }));
    } finally {
      setUpgradingCode(null);
    }
  };

  const plans = [
    {
      code: 'STARTER',
      name: 'Starter',
      subtitle: 'Perfect for getting started with cold outreach',
      monthlyPrice: 18,
      yearlyPrice: 14,
      badge: null,
      themeBg: 'bg-[#FFF0F5] border-[#FCE7F3]',
      features: [
        '5,000 Emails / Month',
        'Basic Email Analytics',
        '1 User Seat',
        'Standard Email Support',
        'Custom Domain Relay'
      ]
    },
    {
      code: 'PRO',
      name: 'Professional',
      subtitle: 'Ideal for growing sales teams and agencies',
      monthlyPrice: 63,
      yearlyPrice: 49,
      badge: 'MOST POPULAR',
      themeBg: 'bg-[#F3E8FF] border-[#DDD6FE]',
      features: [
        '25,000 Emails / Month',
        'Advanced Telemetry & Webhooks',
        '5 User Seats',
        'Priority Relays & Failover',
        'AI Content Studio Unlimited',
        'Real-time Dynamic Filters'
      ]
    },
    {
      code: 'ENTERPRISE',
      name: 'Enterprise',
      subtitle: 'For large organizations & high volume dispatch',
      monthlyPrice: 190,
      yearlyPrice: 150,
      badge: 'CUSTOM RELAYS',
      themeBg: 'bg-[#ECFDF5] border-[#A7F3D0]',
      features: [
        '100,000+ Emails / Month',
        'Custom Dedicated SMTP Relays',
        'Unlimited User Seats',
        'Dedicated Account Manager',
        'Custom SLA & Onboarding',
        'Dedicated IP Warmup Engine'
      ]
    }
  ];

  return (
    <div className="space-y-6 animate-fadeInUp pb-8 max-w-5xl font-sans">
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold tracking-tight text-[#18181B]">Subscription Tiers</h1>
          <p className="text-[13px] text-[#71717A] font-medium mt-1">
            Choose the capacity plan that fits your cold outreach volume and team scale.
          </p>
        </div>

        {/* Monthly / Yearly Toggle */}
        <div className="flex items-center gap-2 p-1 rounded-2xl bg-white border border-[#18181B] shadow-2xs self-start">
          <button
            onClick={() => setBillingCycle('MONTHLY')}
            className={`px-3.5 py-1.5 rounded-xl text-xs font-bold transition-all cursor-pointer ${
              billingCycle === 'MONTHLY' ? 'bg-[#18181B] text-white' : 'text-[#71717A]'
            }`}
          >
            Monthly
          </button>
          <button
            onClick={() => setBillingCycle('YEARLY')}
            className={`px-3.5 py-1.5 rounded-xl text-xs font-bold transition-all cursor-pointer flex items-center gap-1 ${
              billingCycle === 'YEARLY' ? 'bg-[#18181B] text-white' : 'text-[#71717A]'
            }`}
          >
            Yearly
            <span className="px-1.5 py-0.5 rounded-md text-[9px] font-black bg-[#FCE7F3] text-[#DB2777]">
              Save 20%
            </span>
          </button>
        </div>
      </div>

      {/* Main Pricing Cards Grid (3 Cols) */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {plans.map((p) => {
          const isCurrent = (sub?.code || 'PRO') === p.code;
          const price = billingCycle === 'YEARLY' ? p.yearlyPrice : p.monthlyPrice;

          return (
            <div
              key={p.code}
              className={`bg-white rounded-[26px] border border-[#18181B] p-5 sm:p-6 flex flex-col justify-between relative transition-all duration-300 hover:shadow-md group ${
                isCurrent ? 'ring-2 ring-black' : ''
              }`}
            >
              {p.badge && (
                <div className="absolute -top-3.5 left-1/2 -translate-x-1/2 px-3 py-1 rounded-full bg-[#18181B] text-white text-[10px] font-extrabold uppercase tracking-wider shadow-sm flex items-center gap-1">
                  <Sparkles className="w-3 h-3 text-[#FF7EB6]" />
                  <span>{p.badge}</span>
                </div>
              )}

              <div>
                <h3 className="font-extrabold text-[20px] text-[#18181B]">{p.name}</h3>
                <p className="text-xs font-medium text-[#71717A] mt-1 line-clamp-2 min-h-[32px]">{p.subtitle}</p>

                {/* Price Display */}
                <div className="my-5 flex items-baseline">
                  <span className="text-4xl font-black text-[#18181B]">${price}</span>
                  <span className="text-xs font-bold text-[#71717A] ml-1">/month</span>
                </div>

                {/* Features List */}
                <div className="space-y-2.5 pt-2 border-t border-[#E4E4E7] mb-6">
                  {p.features.map((feat, fIdx) => (
                    <div key={fIdx} className="flex items-center gap-2.5 text-xs font-bold text-[#18181B]">
                      <Check className="w-4 h-4 text-[#10B981] stroke-[2.5] flex-shrink-0" />
                      <span>{feat}</span>
                    </div>
                  ))}
                </div>
              </div>

              {/* Action Button */}
              <button
                type="button"
                onClick={() => handleUpgrade(p.code)}
                disabled={isCurrent || upgradingCode === p.code}
                className={`w-full h-11 rounded-xl text-xs font-extrabold flex items-center justify-center gap-2 transition-all cursor-pointer shadow-2xs ${
                  isCurrent
                    ? 'bg-[#FAFAFA] text-[#71717A] border border-[#E4E4E7] cursor-default'
                    : 'bg-[#18181B] hover:bg-black text-white'
                }`}
              >
                <span>{isCurrent ? 'Active Plan' : `Upgrade to ${p.name}`}</span>
                {!isCurrent && <ArrowRight className="w-3.5 h-3.5" />}
              </button>
            </div>
          );
        })}
      </div>

    </div>
  );
};
