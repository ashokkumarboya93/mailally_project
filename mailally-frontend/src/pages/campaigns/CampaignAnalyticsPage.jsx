import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { 
  ArrowLeft, CheckCircle2, RefreshCw
} from 'lucide-react';
import axiosClient from '../../api/axiosClient';
import { formatPercent, normalizeAnalyticsDetail, numberOrZero } from '../../utils/analyticsFormatters';

export const CampaignAnalyticsPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [recipientSearch, setRecipientSearch] = useState('');
  const [recipientStatusFilter, setRecipientStatusFilter] = useState('ALL');

  const [analyticsDetail, setAnalyticsDetail] = useState({
    hasData: false,
    campaignName: `Campaign #${id}`,
    campaignStatus: 'ACTIVE',
    healthScore: 0.0,
    healthRating: 'NO_DATA',
    healthSummary: 'No campaign engagement events recorded yet.',
    benchmarks: [],
    campaignSummary: { totalRecipients: 0, sent: 0, delivered: 0, failed: 0, queued: 0, sending: 0 },
    deliveryFunnel: { queued: 0, sent: 0, delivered: 0, opened: 0, clicked: 0, sentPct: 0.0, deliveredPct: 0.0, openPct: 0.0, clickPct: 0.0 },
    kpis: { deliveryRate: 0.0, openRate: 0.0, clickRate: 0.0, bounceRate: 0.0, complaintRate: 0.0, unsubscribeRate: 0.0 },
    recipientActivities: [],
    liveActivityFeed: []
  });

  const fetchCampaignDetail = async (showSkeleton = true) => {
    if (showSkeleton) setLoading(true);
    else setRefreshing(true);
    try {
      const response = await axiosClient.get(`/analytics/v1-dashboard?campaignId=${id}`);
      if (response.data && response.data.success && response.data.data) {
        setAnalyticsDetail(normalizeAnalyticsDetail(response.data.data, `Campaign #${id}`));
      }
    } catch (err) {
      console.error('Error fetching campaign detail analytics:', err);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  useEffect(() => {
    fetchCampaignDetail(true);
    const interval = setInterval(() => {
      fetchCampaignDetail(false);
    }, 3000);
    return () => clearInterval(interval);
  }, [id]);

  const filteredRecipients = (analyticsDetail.recipientActivities || []).filter(r => {
    const matchesSearch = (r.email || '').toLowerCase().includes(recipientSearch.toLowerCase());
    const matchesStatus = recipientStatusFilter === 'ALL' || (r.status || '').toUpperCase() === recipientStatusFilter.toUpperCase();
    return matchesSearch && matchesStatus;
  });

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center h-64 space-y-3">
        <div className="w-8 h-8 rounded-full border-2 border-[#0A0A0B] border-t-transparent animate-spin" />
        <p className="text-[12px] text-[#9CA3AF] font-medium">Loading campaign telemetry...</p>
      </div>
    );
  }

  return (
    <div className="space-y-6 font-sans pb-8 animate-fadeInUp">
      
      {/* Top Controls */}
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 bg-white p-4 rounded-[16px] border border-[#E5E5E7]">
        <button
          onClick={() => navigate('/analytics')}
          className="inline-flex items-center gap-1.5 px-3 py-1.5 text-[12px] font-semibold text-[#5F6368] hover:text-[#0A0A0B] bg-[#FAFAFB] hover:bg-[#F3F4F6] rounded-lg transition-colors border border-[#E5E5E7] cursor-pointer"
        >
          <ArrowLeft className="w-3.5 h-3.5" />
          <span>Back to All Campaigns</span>
        </button>

        <div className="flex items-center gap-3">
          <div className="text-right">
            <h1 className="text-[14px] font-bold text-[#0A0A0B]">{analyticsDetail.campaignName}</h1>
            <span className="text-[10px] text-[#9CA3AF] font-mono">Campaign ID: {id}</span>
          </div>

          <button 
            onClick={() => fetchCampaignDetail(false)}
            className="px-3 py-1.5 bg-[#0A0A0B] text-white rounded-lg transition-colors flex items-center gap-1.5 text-[12px] font-semibold cursor-pointer"
            title="Refresh Telemetry"
          >
            <RefreshCw className={`w-3.5 h-3.5 ${refreshing ? 'animate-spin' : ''}`} />
            <span>Refresh</span>
          </button>
        </div>
      </div>

      {/* SECTION 1: HEALTH SCORE & BENCHMARKS */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-white rounded-[16px] p-5 border border-[#E5E5E7] flex flex-col justify-between">
          <div className="flex items-center justify-between">
            <h3 className="font-semibold text-[#5F6368] text-[11px] uppercase tracking-wider flex items-center gap-1.5">
              <CheckCircle2 className="w-3.5 h-3.5 text-[#22C55E]" />
              Campaign Health
            </h3>
            <span className="px-2.5 py-0.5 rounded-full text-[10px] font-semibold uppercase bg-[#DCFCE7] text-[#16A34A]">
              {analyticsDetail.healthRating}
            </span>
          </div>

          <div className="flex items-center gap-5 my-4">
            <div className="relative w-20 h-20 flex items-center justify-center shrink-0">
              <svg className="w-full h-full transform -rotate-90" viewBox="0 0 36 36">
                <path className="text-[#F3F4F6]" strokeWidth="3.5" stroke="currentColor" fill="none" d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831" />
                <path className="text-[#0A0A0B] transition-all duration-1000 ease-out" strokeDasharray={`${analyticsDetail.healthScore}, 100`} strokeWidth="3.5" strokeLinecap="round" stroke="currentColor" fill="none" d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831" />
              </svg>
              <div className="absolute inset-0 flex flex-col items-center justify-center text-center">
                <span className="text-xl font-extrabold text-[#0A0A0B] leading-none">{numberOrZero(analyticsDetail.healthScore)}</span>
                <span className="text-[9px] text-[#9CA3AF] font-semibold">/ 100</span>
              </div>
            </div>
            <p className="text-[12px] text-[#5F6368] font-medium leading-relaxed">{analyticsDetail.healthSummary}</p>
          </div>
        </div>

        <div className="md:col-span-2 bg-white rounded-[16px] p-5 border border-[#E5E5E7] flex flex-col justify-between">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="font-semibold text-[#5F6368] text-[11px] uppercase tracking-wider">Industry Benchmarks</h3>
              <p className="text-[12px] text-[#9CA3AF] font-medium">Variance against vertical averages</p>
            </div>
            <span className="px-2.5 py-0.5 rounded-full bg-[#FAFAFB] border border-[#E5E5E7] text-[#5F6368] font-semibold text-[10px]">Standard</span>
          </div>

          <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 mt-4">
            {(analyticsDetail.benchmarks || []).map((bm) => (
              <div key={bm.metricName} className="bg-[#FAFAFB] rounded-xl p-3 border border-[#F0F0F2] space-y-1">
                <span className="text-[10px] font-semibold text-[#9CA3AF] uppercase block tracking-wider">{bm.metricName}</span>
                <div className="flex items-baseline gap-1">
                  <span className="text-base font-extrabold text-[#0A0A0B]">{formatPercent(bm.actualRate)}</span>
                  <span className="text-[10px] text-[#9CA3AF]">vs {formatPercent(bm.benchmarkRate)}</span>
                </div>
                <div className={`inline-flex items-center text-[10px] font-semibold px-1.5 py-0.5 rounded-md ${bm.favorable ? 'bg-[#DCFCE7] text-[#16A34A]' : 'bg-[#FFE4E6] text-[#E11D48]'}`}>
                  {bm.variancePct >= 0 ? `+${formatPercent(bm.variancePct)}` : formatPercent(bm.variancePct)}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* SECTION 2: FUNNEL & KEY KPIS */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="bg-white rounded-[16px] p-5 border border-[#E5E5E7] space-y-4">
          <h3 className="font-semibold text-[#5F6368] text-[11px] uppercase tracking-wider">Delivery Funnel</h3>
          <div className="space-y-3 pt-1">
            {[
              { label: 'Queued', val: analyticsDetail.deliveryFunnel.queued, pct: 100, color: 'bg-[#E5E7EB]' },
              { label: 'Sent', val: analyticsDetail.deliveryFunnel.sent, pct: analyticsDetail.deliveryFunnel.sentPct, color: 'bg-[#60A5FA]' },
              { label: 'Delivered', val: analyticsDetail.deliveryFunnel.delivered, pct: analyticsDetail.deliveryFunnel.deliveredPct, color: 'bg-[#22C55E]' },
              { label: 'Opened', val: analyticsDetail.deliveryFunnel.opened, pct: analyticsDetail.deliveryFunnel.openPct, color: 'bg-[#EC4899]' },
              { label: 'Clicked', val: analyticsDetail.deliveryFunnel.clicked, pct: analyticsDetail.deliveryFunnel.clickPct, color: 'bg-[#C084FC]' },
            ].map((item) => (
              <div key={item.label} className="space-y-1">
                <div className="flex justify-between text-[12px] font-medium text-[#5F6368]">
                  <span>{item.label}</span>
                  <span className="font-semibold text-[#0A0A0B]">{numberOrZero(item.val)} ({formatPercent(item.pct)})</span>
                </div>
                <div className="w-full h-2 bg-[#F3F4F6] rounded-full overflow-hidden">
                  <div className={`h-full ${item.color} rounded-full transition-all duration-500`} style={{ width: `${Math.max(numberOrZero(item.pct), 3)}%` }} />
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="bg-white rounded-[16px] p-5 border border-[#E5E5E7] space-y-4">
          <h3 className="font-semibold text-[#5F6368] text-[11px] uppercase tracking-wider">Key Metrics</h3>
          <div className="grid grid-cols-2 gap-3 pt-1">
            <div className="bg-[#FAFAFB] p-3.5 rounded-xl border border-[#F0F0F2]">
              <span className="text-[10px] font-semibold text-[#9CA3AF] uppercase tracking-wider block">DELIVERY RATE</span>
              <span className="text-xl font-extrabold text-[#16A34A] block mt-1">{formatPercent(analyticsDetail.kpis.deliveryRate)}</span>
            </div>
            <div className="bg-[#FAFAFB] p-3.5 rounded-xl border border-[#F0F0F2]">
              <span className="text-[10px] font-semibold text-[#9CA3AF] uppercase tracking-wider block">OPEN RATE</span>
              <span className="text-xl font-extrabold text-[#0A0A0B] block mt-1">{formatPercent(analyticsDetail.kpis.openRate)}</span>
            </div>
            <div className="bg-[#FAFAFB] p-3.5 rounded-xl border border-[#F0F0F2]">
              <span className="text-[10px] font-semibold text-[#9CA3AF] uppercase tracking-wider block">CLICK RATE</span>
              <span className="text-xl font-extrabold text-[#EC4899] block mt-1">{formatPercent(analyticsDetail.kpis.clickRate)}</span>
            </div>
            <div className="bg-[#FAFAFB] p-3.5 rounded-xl border border-[#F0F0F2]">
              <span className="text-[10px] font-semibold text-[#9CA3AF] uppercase tracking-wider block">BOUNCE RATE</span>
              <span className="text-xl font-extrabold text-[#E11D48] block mt-1">{formatPercent(analyticsDetail.kpis.bounceRate)}</span>
            </div>
          </div>
        </div>
      </div>

      {/* SECTION 3: RECIPIENT TABLE & LIVE STREAM */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
        <div className="lg:col-span-2 bg-white rounded-[16px] p-5 border border-[#E5E5E7] space-y-4">
          <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-3">
            <h3 className="font-semibold text-[#5F6368] text-[11px] uppercase tracking-wider">Recipient Activity</h3>
            <div className="flex items-center gap-2">
              <input
                type="text"
                placeholder="Search email..."
                value={recipientSearch}
                onChange={(e) => setRecipientSearch(e.target.value)}
                className="px-3 h-8 bg-[#FAFAFB] border border-[#E5E5E7] rounded-lg text-[12px] font-medium outline-none"
              />
              <select
                value={recipientStatusFilter}
                onChange={(e) => setRecipientStatusFilter(e.target.value)}
                className="bg-[#FAFAFB] border border-[#E5E5E7] text-[#5F6368] text-[12px] font-semibold rounded-lg px-2.5 h-8 outline-none"
              >
                <option value="ALL">All Statuses</option>
                <option value="CLICKED">Clicked</option>
                <option value="OPENED">Opened</option>
                <option value="DELIVERED">Delivered</option>
                <option value="BOUNCED">Bounced</option>
              </select>
            </div>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse text-[13px]">
              <thead>
                <tr className="border-b border-[#E5E5E7] text-[#9CA3AF] font-semibold uppercase tracking-wider text-[10px]">
                  <th className="py-2.5 px-3">Recipient Email</th>
                  <th className="py-2.5 px-3">Status</th>
                  <th className="py-2.5 px-3">Sent Time</th>
                  <th className="py-2.5 px-3">Bounced Time</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[#F0F0F2]">
                {filteredRecipients.length > 0 ? (
                  filteredRecipients.map((rec) => (
                    <tr key={rec.recipientId || rec.email} className="hover:bg-[#FAFAFB] transition-colors">
                      <td className="py-3 px-3 font-semibold text-[#0A0A0B]">{rec.email || 'Unknown'}</td>
                      <td className="py-3 px-3">
                        <span className={`px-2 py-0.5 rounded-full text-[10px] font-semibold uppercase ${
                          rec.status === 'CLICKED' ? 'bg-[#F3E8FF] text-[#7C3AED]' :
                          rec.status === 'OPENED' ? 'bg-[#FCE7F3] text-[#EC4899]' :
                          rec.status === 'DELIVERED' ? 'bg-[#DCFCE7] text-[#16A34A]' :
                          'bg-[#FFE4E6] text-[#E11D48]'
                        }`}>
                          {rec.status || 'UNKNOWN'}
                        </span>
                      </td>
                      <td className="py-3 px-3 text-[#9CA3AF] text-[12px] font-medium">{rec.sentAt || '—'}</td>
                      <td className="py-3 px-3 text-[#9CA3AF] text-[12px] font-medium">{rec.bouncedAt || '—'}</td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan={4} className="py-8 text-center text-[#9CA3AF] font-medium">
                      No matching recipient activity logs.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>

        <div className="bg-white rounded-[16px] p-5 border border-[#E5E5E7] flex flex-col justify-between">
          <div>
            <div className="flex items-center justify-between">
              <h3 className="font-semibold text-[#5F6368] text-[11px] uppercase tracking-wider">Live Telemetry</h3>
              <span className="w-2 h-2 rounded-full bg-[#22C55E] animate-pulse" />
            </div>
            <p className="text-[12px] text-[#9CA3AF] font-medium mt-0.5">Webhook event stream</p>
          </div>

          <div className="space-y-2 overflow-y-auto max-h-72 my-3">
            {(analyticsDetail.liveActivityFeed || []).length > 0 ? (
              analyticsDetail.liveActivityFeed.map((feed, idx) => (
                <div key={idx} className="bg-[#FAFAFB] p-2.5 rounded-lg border border-[#F0F0F2] space-y-0.5">
                  <div className="flex items-center justify-between text-[10px]">
                    <span className="font-semibold uppercase text-[#0A0A0B]">{feed.eventType}</span>
                    <span className="text-[#9CA3AF]">{feed.timestamp}</span>
                  </div>
                  <p className="text-[12px] text-[#5F6368] font-medium truncate">{feed.recipientEmail || feed.email || 'Unknown'}</p>
                </div>
              ))
            ) : (
              <div className="py-8 text-center text-[#9CA3AF] text-[12px] font-medium">
                Awaiting live telemetry events...
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};
