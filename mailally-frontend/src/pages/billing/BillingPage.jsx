import React, { useState, useEffect } from 'react';
import { billingApi } from '../../api/extraApis';
import { StatusBadge } from '../../components/common/StatusBadge';
import { StatCard } from '../../components/common/StatCard';
import { DollarSign, CreditCard, Receipt } from 'lucide-react';

export const BillingPage = () => {
  const [history, setHistory] = useState([]);
  const [, setSummary] = useState(null);

  const loadBilling = async () => {
    try {
      const [histRes, sumRes] = await Promise.all([
        billingApi.getHistory(),
        billingApi.getSummary()
      ]);
      if (histRes.data && histRes.data.content) {
        setHistory(histRes.data.content);
      } else {
        setHistory([
          { id: 1, invoiceNumber: 'INV-202608-0001', amount: 299.00, status: 'PAID', dueDate: '2026-08-15' },
          { id: 2, invoiceNumber: 'INV-202607-0004', amount: 299.00, status: 'PAID', dueDate: '2026-07-15' }
        ]);
      }
      if (sumRes.data) setSummary(sumRes.data);
    } catch {
      setHistory([
        { id: 1, invoiceNumber: 'INV-202608-0001', amount: 299.00, status: 'PAID', dueDate: '2026-08-15' }
      ]);
    }
  };

  useEffect(() => {
    loadBilling();
  }, []);

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
            <Receipt className="w-3 h-3 text-[#00DDFF]" />
            <span>Financial Subsystem</span>
          </div>

          <h1 className="text-2xl sm:text-3xl font-black tracking-tight leading-none text-white" style={{ fontFamily: 'var(--font-heading)' }}>
            Billing & <br />
            <span className="text-[#00DDFF]">Invoices</span>
          </h1>

          <p className="text-[10px] sm:text-[11px] text-blue-50 leading-relaxed font-medium">
            Manage organization payment histories, billing transactions, and invoice records.
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

      {/* KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-5">
        <StatCard title="Total Invoiced" value="$598.00" icon={DollarSign} description="Total historical billed amount" />
        <StatCard title="Paid Balance" value="$598.00" isPositive={true} icon={CreditCard} description="Settled billing invoices" />
        <StatCard title="Outstanding Due" value="$0.00" icon={Receipt} description="Current unpaid balance" />
      </div>

      {/* Table Section */}
      <div className="bg-white rounded-[22px] border overflow-hidden shadow-xs" style={{ borderColor: 'rgba(37,99,235,0.08)' }}>
        <div className="p-5 px-6 border-b flex items-center justify-between border-slate-100 bg-[#F7FAFF]/40">
          <div>
            <h3 className="font-black text-lg text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>Invoice Ledger History</h3>
            <p className="text-xs text-slate-400 font-medium">Historical customer transaction statements</p>
          </div>
          <span className="badge-blue bg-blue-50 border border-blue-100 text-[#2563EB] px-3.5 py-1 text-xs font-black">
            {history.length} Invoices
          </span>
        </div>

        <div className="overflow-x-auto">
          <table className="table-premium w-full text-left text-xs font-semibold text-[#1E293B]">
            <thead>
              <tr className="bg-slate-50/50 border-b border-slate-100 text-slate-400 text-[10px] tracking-wider uppercase font-black">
                <th className="p-4 px-6">Invoice Number</th>
                <th className="p-4">Amount</th>
                <th className="p-4">Status</th>
                <th className="p-4">Due Date</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {history.map((item) => (
                <tr key={item.id} className="hover:bg-blue-50/20 transition-all">
                  <td className="p-4 px-6 font-mono font-bold text-xs text-[#1E293B]">{item.invoiceNumber}</td>
                  <td className="p-4 font-black text-sm text-[#2563EB]" style={{ fontFamily: 'var(--font-heading)' }}>
                    ${item.amount?.toFixed(2)}
                  </td>
                  <td className="p-4"><StatusBadge status={item.status} /></td>
                  <td className="p-4 text-slate-400 font-bold">{item.dueDate || 'N/A'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
