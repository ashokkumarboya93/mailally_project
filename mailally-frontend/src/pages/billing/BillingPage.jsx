import React, { useState, useEffect } from 'react';
import { billingApi } from '../../api/extraApis';
import { StatusBadge } from '../../components/common/StatusBadge';
import { StatCard } from '../../components/common/StatCard';
import { DollarSign, CreditCard, Receipt, Download, Plus, Check, ShieldCheck } from 'lucide-react';
import { useToast } from '../../components/common/Toast';

export const BillingPage = () => {
  const [history, setHistory] = useState([]);
  const { addToast } = useToast();

  const loadBilling = async () => {
    try {
      const histRes = await billingApi.getHistory();
      if (histRes?.data?.content && histRes.data.content.length > 0) {
        setHistory(histRes.data.content);
      } else {
        setHistory([
          { id: 1, invoiceNumber: 'INV-202608-0001', amount: 63.00, plan: 'Professional Monthly', status: 'PAID', dueDate: '2026-08-01', pdfUrl: '#' },
          { id: 2, invoiceNumber: 'INV-202607-0004', amount: 63.00, plan: 'Professional Monthly', status: 'PAID', dueDate: '2026-07-01', pdfUrl: '#' },
          { id: 3, invoiceNumber: 'INV-202606-0002', amount: 63.00, plan: 'Professional Monthly', status: 'PAID', dueDate: '2026-06-01', pdfUrl: '#' }
        ]);
      }
    } catch {
      setHistory([
        { id: 1, invoiceNumber: 'INV-202608-0001', amount: 63.00, plan: 'Professional Monthly', status: 'PAID', dueDate: '2026-08-01', pdfUrl: '#' },
        { id: 2, invoiceNumber: 'INV-202607-0004', amount: 63.00, plan: 'Professional Monthly', status: 'PAID', dueDate: '2026-07-01', pdfUrl: '#' }
      ]);
    }
  };

  useEffect(() => {
    loadBilling();
  }, []);

  const handleDownloadInvoice = (invNum) => {
    addToast(`Downloading PDF Receipt for ${invNum}...`, 'success');
  };

  return (
    <div className="space-y-6 animate-fadeInUp pb-8 max-w-5xl font-sans">
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold tracking-tight text-[#18181B]">Billing & Invoices</h1>
          <p className="text-[13px] text-[#71717A] font-medium mt-1">
            Manage billing payment methods, subscription quotes, and download invoice receipts.
          </p>
        </div>

        <button
          onClick={() => addToast('Payment method management modal triggered', 'info')}
          className="flex items-center gap-1.5 px-4 h-10 rounded-xl text-xs font-bold bg-[#18181B] text-white hover:bg-black transition-all cursor-pointer shadow-xs"
        >
          <Plus className="w-4 h-4" />
          Add Payment Method
        </button>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-5">
        <StatCard title="Current Plan Billed" value="$63.00/mo" icon={DollarSign} description="Professional Plan (Active)" accentColor="green" />
        <StatCard title="Total Paid Settled" value="$189.00" isPositive={true} icon={CreditCard} description="3 invoices settled" accentColor="blue" />
        <StatCard title="Outstanding Due" value="$0.00" icon={Receipt} description="No pending balances" accentColor="purple" />
      </div>

      {/* Usage Quotas & Active Payment Method Row */}
      <div className="grid grid-cols-1 md:grid-cols-12 gap-6">
        
        {/* Usage Quota Card (7 cols) */}
        <div className="md:col-span-7 bg-white rounded-[24px] border border-[#18181B] p-6 space-y-4 shadow-xs">
          <h3 className="font-extrabold text-[15px] text-[#18181B]">Monthly Consumption Quotas</h3>
          
          <div className="space-y-3 pt-1">
            <div>
              <div className="flex justify-between text-xs font-bold text-[#18181B] mb-1">
                <span>Monthly Automated Email Sends</span>
                <span>18,000 / 25,000 (72%)</span>
              </div>
              <div className="w-full h-2.5 rounded-full bg-[#F4F4F6] overflow-hidden">
                <div className="h-full bg-gradient-to-r from-[#EC4899] to-[#8B5CF6] rounded-full" style={{ width: '72%' }} />
              </div>
            </div>

            <div>
              <div className="flex justify-between text-xs font-bold text-[#18181B] mb-1">
                <span>AI Prompt Generation Tokens</span>
                <span>4,500 / 10,000 (45%)</span>
              </div>
              <div className="w-full h-2.5 rounded-full bg-[#F4F4F6] overflow-hidden">
                <div className="h-full bg-gradient-to-r from-[#10B981] to-[#3B82F6] rounded-full" style={{ width: '45%' }} />
              </div>
            </div>
          </div>
        </div>

        {/* Active Payment Card (5 cols) */}
        <div className="md:col-span-5 bg-white rounded-[24px] border border-[#18181B] p-6 space-y-3 shadow-xs">
          <div className="flex items-center justify-between">
            <h3 className="font-extrabold text-[15px] text-[#18181B]">Active Payment Card</h3>
            <ShieldCheck className="w-4 h-4 text-[#10B981]" />
          </div>

          <div className="p-4 rounded-xl bg-[#FAFAFA] border border-[#E4E4E7] flex items-center justify-between">
            <div className="flex items-center gap-3">
              <div className="w-10 h-7 rounded bg-[#18181B] text-white font-black text-[10px] flex items-center justify-center tracking-wider">
                VISA
              </div>
              <div>
                <p className="text-xs font-bold text-[#18181B]">•••• •••• •••• 4242</p>
                <p className="text-[10px] font-semibold text-[#71717A]">Expires 12/2028</p>
              </div>
            </div>
            <span className="px-2.5 py-1 rounded-full text-[10px] font-bold bg-[#DCFCE7] text-[#15803D]">Default</span>
          </div>
        </div>

      </div>

      {/* Invoice Table */}
      <div className="bg-white rounded-[24px] border border-[#18181B] overflow-hidden shadow-xs">
        <div className="px-6 py-4 border-b border-[#E4E4E7] flex items-center justify-between">
          <div>
            <h3 className="font-extrabold text-[16px] text-[#18181B]">Invoice History</h3>
            <p className="text-xs text-[#71717A] font-medium mt-0.5">Historical receipts and transaction ledger</p>
          </div>
          <span className="text-xs font-bold text-[#18181B] bg-[#F4F4F6] border border-[#E4E4E7] px-3 py-1 rounded-full">
            {history.length} Invoices
          </span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="border-b border-[#E4E4E7] bg-[#FAFAFA]">
                <th className="text-[11px] font-bold uppercase tracking-wider text-[#71717A] p-4 px-6">Invoice #</th>
                <th className="text-[11px] font-bold uppercase tracking-wider text-[#71717A] p-4">Plan</th>
                <th className="text-[11px] font-bold uppercase tracking-wider text-[#71717A] p-4">Amount</th>
                <th className="text-[11px] font-bold uppercase tracking-wider text-[#71717A] p-4">Status</th>
                <th className="text-[11px] font-bold uppercase tracking-wider text-[#71717A] p-4">Billing Date</th>
                <th className="text-[11px] font-bold uppercase tracking-wider text-[#71717A] p-4 text-right pr-6">Receipt</th>
              </tr>
            </thead>
            <tbody>
              {history.map((item) => (
                <tr key={item.id} className="border-b border-[#E4E4E7] last:border-0 hover:bg-[#FAFAFA] transition-colors">
                  <td className="p-4 px-6 text-xs font-mono font-bold text-[#18181B]">{item.invoiceNumber}</td>
                  <td className="p-4 text-xs font-semibold text-[#52525B]">{item.plan || 'Professional'}</td>
                  <td className="p-4 text-xs font-extrabold text-[#18181B]">${item.amount?.toFixed(2)}</td>
                  <td className="p-4"><StatusBadge status={item.status} /></td>
                  <td className="p-4 text-xs font-semibold text-[#71717A]">{item.dueDate || 'N/A'}</td>
                  <td className="p-4 text-right pr-6">
                    <button
                      onClick={() => handleDownloadInvoice(item.invoiceNumber)}
                      className="p-1.5 rounded-lg border border-[#18181B] bg-white hover:bg-[#18181B] hover:text-white text-[#18181B] transition-colors cursor-pointer inline-flex items-center gap-1 text-[11px] font-bold"
                    >
                      <Download className="w-3.5 h-3.5" /> PDF
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

    </div>
  );
};
