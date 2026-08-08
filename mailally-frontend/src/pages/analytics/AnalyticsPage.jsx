import React, { useState, useEffect } from 'react';
import { 
  Megaphone, Gift, Zap, RefreshCw, Lock, Mail, Search, Filter, 
  LayoutList, LayoutGrid, ArrowLeft, ChevronRight, Plus, CheckCircle2
} from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import axiosClient from '../../api/axiosClient';
import { formatPercent, normalizeAnalyticsDetail, normalizeCampaignMetric, numberOrZero } from '../../utils/analyticsFormatters';

export const AnalyticsPage = () => {
  const navigate = useNavigate();
  const [viewMode, setViewMode] = useState('LIST'); // 'LIST' | 'GRID'
  const [selectedCampaignId, setSelectedCampaignId] = useState(null); // null = List view
  const [campaignsList, setCampaignsList] = useState([]);
  const [loadingList, setLoadingList] = useState(true);
  const [loadingDetail, setLoadingDetail] = useState(false);
  const [refreshing, setRefreshing] = useState(false);

  // Filters
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [recipientSearch, setRecipientSearch] = useState('');
  const [recipientStatusFilter, setRecipientStatusFilter] = useState('ALL');

  const [analyticsDetail, setAnalyticsDetail] = useState({
    hasData: false,
    campaignName: 'All Organization Campaigns',
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

  const getIconForCampaign = (index) => {
    const icons = [
      { icon: Megaphone, bg: 'bg-[#FCE7F3] text-[#EC4899]' },
      { icon: Zap, bg: 'bg-[#FFEDD5] text-[#F97316]' },
      { icon: Gift, bg: 'bg-[#F3E8FF] text-[#A855F7]' },
      { icon: RefreshCw, bg: 'bg-[#FFE4E6] text-[#E11D48]' },
      { icon: Lock, bg: 'bg-[#DCFCE7] text-[#22C55E]' },
      { icon: Mail, bg: 'bg-[#DBEAFE] text-[#3B82F6]' },
    ];
    return icons[index % icons.length];
  };

  const fetchCampaignsList = async () => {
    setLoadingList(true);
    try {
      const response = await axiosClient.get('/analytics/campaigns');
      if (response.data && response.data.data) {
        setCampaignsList(response.data.data.map(normalizeCampaignMetric));
      }
    } catch (err) {
      console.error('Error fetching analytics campaigns list:', err);
    } finally {
      setLoadingList(false);
    }
  };

  const fetchCampaignDetail = async (campaignId, showSkeleton = true) => {
    if (showSkeleton) setLoadingDetail(true);
    else setRefreshing(true);
    try {
      const queryParam = campaignId ? `?campaignId=${campaignId}` : '';
      const response = await axiosClient.get(`/analytics/v1-dashboard${queryParam}`);
      if (response.data && response.data.success && response.data.data) {
        setAnalyticsDetail(normalizeAnalyticsDetail(response.data.data));
      }
    } catch (err) {
      console.error('Error fetching campaign detail analytics:', err);
    } finally {
      setLoadingDetail(false);
      setRefreshing(false);
    }
  };

  useEffect(() => {
    fetchCampaignsList();
  }, []);

  useEffect(() => {
    if (selectedCampaignId !== null) {
      fetchCampaignDetail(selectedCampaignId, true);
      const interval = setInterval(() => {
        fetchCampaignDetail(selectedCampaignId, false);
      }, 3000);
      return () => clearInterval(interval);
    }
  }, [selectedCampaignId]);

  const filteredCampaigns = campaignsList.filter(c => {
    const matchesSearch = (c.campaignName || '').toLowerCase().includes(searchQuery.toLowerCase());
    const matchesStatus = statusFilter === 'ALL' || (c.status || '').toUpperCase() === statusFilter.toUpperCase();
    return matchesSearch && matchesStatus;
  });

  const filteredRecipients = (analyticsDetail.recipientActivities || []).filter(r => {
    const matchesSearch = (r.email || '').toLowerCase().includes(recipientSearch.toLowerCase());
    const matchesStatus = recipientStatusFilter === 'ALL' || (r.status || '').toUpperCase() === recipientStatusFilter.toUpperCase();
    return matchesSearch && matchesStatus;
  });

  return (
    <div className="space-y-6 font-sans pb-8 text-[#0A0A0B] animate-fadeInUp">
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold tracking-tight text-[#0A0A0B]">Analytics</h1>
          <p className="text-[13px] text-[#9CA3AF] font-medium mt-1">View and analyze performance of your email campaigns.</p>
        </div>

        <div className="flex items-center gap-2">
          <div className="relative flex-1 sm:w-64">
            <Search className="w-3.5 h-3.5 absolute left-3 top-1/2 -translate-y-1/2 text-[#C0C5CC]" />
            <input
              type="text"
              placeholder="Search campaigns..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full pl-9 pr-3 h-9 text-[12px] font-medium bg-white border border-[#E5E5E7] rounded-lg outline-none focus:border-[#D1D5DB]"
            />
          </div>

          <button
            onClick={() => navigate('/campaigns/wizard')}
            className="ma-btn ma-btn-primary gap-1.5 text-[12px]"
          >
            <Plus className="w-4 h-4" /> New Campaign
          </button>
        </div>
      </div>

      {selectedCampaignId === null ? (
        /* MODE A: ALL CAMPAIGNS LIST & GRID VIEW */
        <div className="bg-white rounded-[16px] border border-[#E5E5E7] p-5 space-y-4">
          <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 border-b border-[#F0F0F2] pb-4">
            <div className="flex items-center gap-2">
              <h2 className="text-[15px] font-bold text-[#0A0A0B]">All Campaigns</h2>
              <span className="px-2.5 py-0.5 rounded-full bg-[#F3F4F6] text-[#5F6368] font-semibold text-[11px]">
                {filteredCampaigns.length} Total
              </span>
            </div>

            <div className="flex items-center gap-3">
              <div className="flex items-center gap-1.5">
                <Filter className="w-3.5 h-3.5 text-[#9CA3AF]" />
                <select
                  value={statusFilter}
                  onChange={(e) => setStatusFilter(e.target.value)}
                  className="bg-[#FAFAFB] border border-[#E5E5E7] text-[#5F6368] text-[12px] font-medium rounded-lg px-2.5 h-8 outline-none cursor-pointer"
                >
                  <option value="ALL">All Status</option>
                  <option value="ACTIVE">Active / Sent</option>
                  <option value="COMPLETED">Completed</option>
                  <option value="DRAFT">Draft</option>
                </select>
              </div>

              <div className="bg-[#FAFAFB] p-1 rounded-lg flex items-center gap-1 border border-[#E5E5E7]">
                <button
                  onClick={() => setViewMode('LIST')}
                  className={`px-3 py-1 rounded-md text-[12px] font-semibold transition-all cursor-pointer ${
                    viewMode === 'LIST' ? 'bg-white text-[#0A0A0B] shadow-xs' : 'text-[#9CA3AF]'
                  }`}
                >
                  <LayoutList className="w-3.5 h-3.5 inline mr-1" /> List
                </button>

                <button
                  onClick={() => setViewMode('GRID')}
                  className={`px-3 py-1 rounded-md text-[12px] font-semibold transition-all cursor-pointer ${
                    viewMode === 'GRID' ? 'bg-white text-[#0A0A0B] shadow-xs' : 'text-[#9CA3AF]'
                  }`}
                >
                  <LayoutGrid className="w-3.5 h-3.5 inline mr-1" /> Grid
                </button>
              </div>
            </div>
          </div>

          {viewMode === 'LIST' ? (
            <div className="overflow-x-auto">
              <table className="w-full text-left border-collapse text-[13px]">
                <thead>
                  <tr className="border-b border-[#E5E5E7] text-[#9CA3AF] font-semibold uppercase tracking-wider text-[10px]">
                    <th className="py-3 px-4">CAMPAIGN</th>
                    <th className="py-3 px-4">STATUS</th>
                    <th className="py-3 px-4 text-center">SENT</th>
                    <th className="py-3 px-4 text-center">OPEN RATE</th>
                    <th className="py-3 px-4 text-center">CLICK RATE</th>
                    <th className="py-3 px-4 text-center">BOUNCE RATE</th>
                    <th className="py-3 px-4 text-right">ACTION</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-[#F0F0F2]">
                  {loadingList ? (
                    <tr>
                      <td colSpan={7} className="py-12 text-center text-[#9CA3AF] font-medium">
                        Loading campaign analytics...
                      </td>
                    </tr>
                  ) : filteredCampaigns.length > 0 ? (
                    filteredCampaigns.map((c, idx) => {
                      const iconStyle = getIconForCampaign(idx);
                      const IconComp = iconStyle.icon;
                      return (
                        <tr key={c.campaignId} className="hover:bg-[#FAFAFB] transition-colors group">
                          <td className="py-3.5 px-4">
                            <div className="flex items-center gap-3">
                              <div className={`p-2 rounded-lg ${iconStyle.bg} flex items-center justify-center shrink-0`}>
                                <IconComp className="w-4 h-4" strokeWidth={1.5} />
                              </div>
                              <div>
                                <span className="font-bold text-[#0A0A0B] text-[14px] block">
                                  {c.campaignName}
                                </span>
                                <span className="text-[10px] text-[#9CA3AF] font-mono">
                                  CMP-{String(c.campaignId).padStart(5, '0')}
                                </span>
                              </div>
                            </div>
                          </td>

                          <td className="py-3.5 px-4">
                            <span className="px-2.5 py-0.5 rounded-full text-[10px] font-semibold uppercase bg-[#DCFCE7] text-[#16A34A]">
                              {c.status}
                            </span>
                          </td>

                          <td className="py-3.5 px-4 text-center">
                            <span className="font-bold text-[#0A0A0B] block">{numberOrZero(c.sentCount).toLocaleString()}</span>
                          </td>

                          <td className="py-3.5 px-4 text-center">
                            <span className="font-bold text-[#0A0A0B] block">{formatPercent(c.openRate, 1)}</span>
                          </td>

                          <td className="py-3.5 px-4 text-center">
                            <span className="font-bold text-[#EC4899] block">{formatPercent(c.clickRate, 1)}</span>
                          </td>

                          <td className="py-3.5 px-4 text-center">
                            <span className="font-bold text-[#E11D48] block">{formatPercent(c.bounceRate, 1)}</span>
                          </td>

                          <td className="py-3.5 px-4 text-right">
                            <button
                              onClick={() => navigate(`/campaigns/${c.campaignId}/analytics`)}
                              className="px-3 py-1 bg-[#0A0A0B] text-white font-semibold text-[12px] rounded-lg hover:bg-[#1F1F20] transition-colors cursor-pointer"
                            >
                              Open <ChevronRight className="w-3.5 h-3.5 inline ml-0.5" />
                            </button>
                          </td>
                        </tr>
                      );
                    })
                  ) : (
                    <tr>
                      <td colSpan={7} className="py-12 text-center text-[#9CA3AF] font-medium">
                        No campaigns found.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          ) : (
            /* GRID VIEW */
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {filteredCampaigns.map((c, idx) => {
                const iconStyle = getIconForCampaign(idx);
                const IconComp = iconStyle.icon;
                return (
                  <div key={c.campaignId} className="bg-white border border-[#E5E5E7] rounded-[16px] p-5 space-y-3 hover:shadow-sm transition-all">
                    <div className="flex items-start justify-between gap-3">
                      <div className="flex items-center gap-2.5">
                        <div className={`p-2 rounded-lg ${iconStyle.bg}`}>
                          <IconComp className="w-4 h-4" strokeWidth={1.5} />
                        </div>
                        <div>
                          <h3 className="font-bold text-[#0A0A0B] text-[14px]">{c.campaignName}</h3>
                          <span className="text-[10px] text-[#9CA3AF] font-mono">CMP-{String(c.campaignId).padStart(5, '0')}</span>
                        </div>
                      </div>
                      <span className="px-2 py-0.5 bg-[#DCFCE7] text-[#16A34A] text-[10px] font-semibold rounded-full uppercase">
                        {c.status}
                      </span>
                    </div>

                    <div className="grid grid-cols-3 gap-2 bg-[#FAFAFB] p-2.5 rounded-xl border border-[#F0F0F2] text-center">
                      <div>
                        <span className="text-[10px] text-[#9CA3AF] font-semibold block uppercase">Sent</span>
                        <span className="text-[12px] font-bold text-[#0A0A0B]">{c.sentCount}</span>
                      </div>
                      <div>
                        <span className="text-[10px] text-[#9CA3AF] font-semibold block uppercase">Open</span>
                        <span className="text-[12px] font-bold text-[#0A0A0B]">{formatPercent(c.openRate, 1)}</span>
                      </div>
                      <div>
                        <span className="text-[10px] text-[#EC4899] font-semibold block uppercase">Click</span>
                        <span className="text-[12px] font-bold text-[#EC4899]">{formatPercent(c.clickRate, 1)}</span>
                      </div>
                    </div>

                    <button
                      onClick={() => navigate(`/campaigns/${c.campaignId}/analytics`)}
                      className="w-full py-2 bg-[#0A0A0B] text-white font-semibold text-[12px] rounded-lg hover:bg-[#1F1F20] transition-colors cursor-pointer flex items-center justify-center gap-1"
                    >
                      View Analytics <ChevronRight className="w-3.5 h-3.5" />
                    </button>
                  </div>
                );
              })}
            </div>
          )}
        </div>
      ) : (
        /* MODE B: DETAILED CAMPAIGN ANALYTICS */
        <div className="space-y-4">
          <div className="flex items-center justify-between bg-white p-4 rounded-[16px] border border-[#E5E5E7]">
            <button
              onClick={() => setSelectedCampaignId(null)}
              className="flex items-center gap-1.5 px-3 py-1.5 text-[12px] font-semibold text-[#5F6368] hover:text-[#0A0A0B] bg-[#FAFAFB] rounded-lg border border-[#E5E5E7]"
            >
              <ArrowLeft className="w-3.5 h-3.5" /> Back to All Campaigns
            </button>

            <div className="flex items-center gap-3">
              <span className="text-[12px] font-medium text-[#5F6368]">
                Viewing: <strong className="text-[#0A0A0B]">{analyticsDetail.campaignName}</strong>
              </span>
              
              <button 
                onClick={() => fetchCampaignDetail(selectedCampaignId, false)}
                className="px-3 py-1.5 bg-[#0A0A0B] text-white rounded-lg text-[12px] font-semibold cursor-pointer"
              >
                <RefreshCw className={`w-3.5 h-3.5 inline mr-1 ${(loadingDetail || refreshing) ? 'animate-spin' : ''}`} />
                Refresh
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
