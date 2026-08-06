import React, { useState, useEffect } from 'react';
import { campaignApi, templateApi } from '../../api/campaignApi';
import { contactApi } from '../../api/contactApi';
import { StatusBadge } from '../../components/common/StatusBadge';
import { Modal } from '../../components/common/Modal';
import { PageSkeletonLoader } from '../../components/common/PageSkeletonLoader';
import { LiveSendingDashboard } from '../../components/campaigns/LiveSendingDashboard';
import { CampaignDiagnosticsModal } from '../../components/campaigns/CampaignDiagnosticsModal';
import { 
  Play, Plus, Eye, BarChart2, CheckCircle, Pause, Trash2, 
  RefreshCw, AlertTriangle, Users, Layers, ShieldCheck, Mail, Send,
  LayoutGrid, List, Search, Filter, MoreVertical, Calendar, Clock
} from 'lucide-react';

export const CampaignsPage = () => {
  const [campaigns, setCampaigns] = useState([]);
  const [templates, setTemplates] = useState([]);
  const [collections, setCollections] = useState([]);
  const [loading, setLoading] = useState(true);

  // View mode: 'list' or 'grid'
  const [viewMode, setViewMode] = useState('list');
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');

  // Active Running Campaign State (Only shown when user clicks Launch or View Live Stream)
  const [activeLiveCampaignId, setActiveLiveCampaignId] = useState(null);

  // Modals State
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isDiagnosticsOpen, setIsDiagnosticsOpen] = useState(false);
  const [isAttachCollectionOpen, setIsAttachCollectionOpen] = useState(false);
  const [deleteConfirmId, setDeleteConfirmId] = useState(null);
  const [deleting, setDeleting] = useState(false);

  // Diagnostics State
  const [currentDiagnostics, setCurrentDiagnostics] = useState(null);
  const [selectedCampaignId, setSelectedCampaignId] = useState(null);

  // Form State
  const [name, setName] = useState('');
  const [subject, setSubject] = useState('');
  const [senderName, setSenderName] = useState('Marcamor');
  const [senderEmail, setSenderEmail] = useState('info@marcamor.com');
  const [templateId, setTemplateId] = useState('');
  const [batchSize, setBatchSize] = useState(100);
  const [retryCount, setRetryCount] = useState(3);
  const [selectedCollectionId, setSelectedCollectionId] = useState('');

  const loadData = async () => {
    setLoading(true);
    try {
      const [cRes, tRes, collRes] = await Promise.allSettled([
        campaignApi.getCampaigns(0, 100),
        templateApi.getTemplates(0, 50),
        contactApi.getCollections()
      ]);

      if (cRes.status === 'fulfilled' && cRes.value?.data?.content) {
        setCampaigns(cRes.value.data.content);
      } else {
        setCampaigns([]);
      }

      if (tRes.status === 'fulfilled') {
        let loadedTemplates = tRes.value?.data?.content || (Array.isArray(tRes.value?.data) ? tRes.value.data : []);
        if (loadedTemplates.length === 0) {
          try {
            const defaultT = await templateApi.createTemplate({
              name: 'Marcamor Corporate Announcement',
              subject: 'Hello {{firstName}} - Important Marcamor Update',
              htmlContent: `<div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; border: 1px solid #e2e8f0; border-radius: 16px; background: #ffffff;">
  <h2 style="color: #1e3a8a;">Hello {{firstName}},</h2>
  <p style="font-size: 15px; color: #334155; line-height: 1.6;">We have important updates for team {{company}} in {{city}}.</p>
  <div style="background-color: #f1f5f9; padding: 16px; border-radius: 12px; margin: 24px 0; border-left: 4px solid #2563eb;">
    <p style="margin: 0; font-weight: bold; color: #0f172a;">Department: {{department}}</p>
  </div>
  <p style="font-size: 14px; color: #64748b;">Best regards,<br/>Marcamor Leadership Team</p>
</div>`,
              status: 'ACTIVE'
            });
            if (defaultT?.data) loadedTemplates = [defaultT.data];
          } catch (err) {
            console.error(err);
          }
        }
        setTemplates(loadedTemplates);
      }

      if (collRes.status === 'fulfilled' && Array.isArray(collRes.value?.data)) {
        setCollections(collRes.value.data);
      }

    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleCreateSubmit = async (e) => {
    e.preventDefault();
    try {
      await campaignApi.createCampaign({
        name,
        subject,
        templateId: templateId ? Number(templateId) : (templates.length > 0 ? templates[0].id : null),
        senderName: senderName || 'Marcamor',
        senderEmail: senderEmail || 'info@marcamor.com',
        batchSize,
        retryCount
      });
      setIsCreateModalOpen(false);
      setName('');
      setSubject('');
      loadData();
    } catch (e) {
      alert('Failed to create campaign: ' + (e.response?.data?.message || e.message));
    }
  };

  const handleAttachTemplateToCampaign = async (campaignId, templateId) => {
    if (!templateId) return;
    try {
      await campaignApi.attachTemplate(campaignId, Number(templateId));
      await loadData();
      
      const diagRes = await campaignApi.getDiagnostics(campaignId);
      if (diagRes.data) {
        setCurrentDiagnostics(diagRes.data);
      } else {
        setCurrentDiagnostics(prev => prev ? {
          ...prev,
          templateExists: true,
          isReady: prev.totalRecipients > 0
        } : null);
      }
    } catch (e) {
      alert('Failed to attach template: ' + (e.response?.data?.message || e.message));
    }
  };

  const handleRunDiagnostics = async (campaignId) => {
    setSelectedCampaignId(campaignId);
    try {
      const res = await campaignApi.getDiagnostics(campaignId);
      if (res.data) {
        setCurrentDiagnostics(res.data);
      } else {
        const targetCamp = campaigns.find(c => c.id === campaignId);
        setCurrentDiagnostics({
          campaignId,
          isReady: targetCamp ? (targetCamp.totalRecipients > 0 && targetCamp.templateId != null) : true,
          totalRecipients: targetCamp?.totalRecipients || 0,
          templateExists: targetCamp?.templateId != null,
          providerHealthy: true,
          activeProvider: 'Brevo API (Sendinblue)',
          estimatedDurationMinutes: Math.ceil((targetCamp?.totalRecipients || 100) / 100),
          estimatedCost: '$0.00'
        });
      }
      setIsDiagnosticsOpen(true);
    } catch (e) {
      console.error(e);
    }
  };

  const handleConfirmLaunch = async () => {
    if (!selectedCampaignId) return;
    try {
      await campaignApi.launchCampaign(selectedCampaignId);
      setIsDiagnosticsOpen(false);
      setActiveLiveCampaignId(selectedCampaignId);
      loadData();
    } catch (e) {
      setIsDiagnosticsOpen(false);
      setActiveLiveCampaignId(selectedCampaignId);
      loadData();
    }
  };

  const handleAttachCollection = async () => {
    if (!selectedCampaignId || !selectedCollectionId) return;
    try {
      await campaignApi.addCollectionToCampaign(selectedCampaignId, selectedCollectionId);
      setIsAttachCollectionOpen(false);
      loadData();
    } catch (e) {
      alert('Failed to attach collection: ' + (e.response?.data?.message || e.message));
    }
  };

  const handleDeleteCampaign = async () => {
    if (!deleteConfirmId) return;
    setDeleting(true);
    try {
      await campaignApi.deleteCampaign(deleteConfirmId);
      setDeleteConfirmId(null);
      await loadData();
    } catch (e) {
      alert('Failed to delete campaign: ' + (e.response?.data?.message || e.message));
    } finally {
      setDeleting(false);
    }
  };

  // Filtered campaigns list
  const filteredCampaigns = campaigns.filter(c => {
    const matchesSearch = c.name?.toLowerCase().includes(searchTerm.toLowerCase()) || 
                          c.subject?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus = statusFilter === 'ALL' || (c.status && c.status.toUpperCase() === statusFilter);
    return matchesSearch && matchesStatus;
  });

  if (loading) {
    return <PageSkeletonLoader type="cards" />;
  }

  return (
    <div className="p-6 space-y-6 max-w-7xl mx-auto text-slate-800 font-sans">
      
      {/* Header Toolbar — Bright Ice Blue Banner */}
      <div 
        className="flex flex-col md:flex-row md:items-center justify-between gap-4 p-6 rounded-3xl text-white shadow-lg shadow-blue-500/10 relative overflow-hidden border border-blue-200"
        style={{ background: 'linear-gradient(135deg, #1F57F5 0%, #2BAFF2 100%)' }}
      >
        <div className="relative z-10">
          <div className="flex items-center gap-2">
            <h1 className="text-2xl font-extrabold tracking-tight text-white">Campaign Management</h1>
            <span className="text-xs px-3 py-0.5 rounded-full bg-white/20 text-white font-semibold backdrop-blur-md border border-white/30">
              {campaigns.length} Total
            </span>
          </div>
          <p className="text-xs text-blue-100 mt-1">
            Create, manage & execute bulk email campaigns • Real-time dispatch telemetry • Multi-provider failover
          </p>
        </div>

        <button
          onClick={() => setIsCreateModalOpen(true)}
          className="px-5 py-2.5 bg-white text-blue-700 hover:bg-blue-50 font-bold text-sm rounded-xl flex items-center gap-2 shadow-md hover:scale-[1.02] transition-all relative z-10 cursor-pointer"
        >
          <Plus className="w-4 h-4 text-blue-600" /> Create New Campaign
        </button>
      </div>

      {/* LIVE SENDING DASHBOARD — ONLY DISPLAYED WHEN USER LAUNCHES OR SELECTS LIVE STREAM */}
      {activeLiveCampaignId && (
        <LiveSendingDashboard
          campaignId={activeLiveCampaignId}
          onClose={() => setActiveLiveCampaignId(null)}
          onFinished={() => loadData()}
        />
      )}

      {/* Controls Bar: Search, Status Filter & View Toggle */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 bg-white p-4 rounded-2xl border border-slate-200 shadow-sm">
        <div className="flex items-center gap-3 flex-1">
          {/* Search Input */}
          <div className="relative flex-1 max-w-md">
            <Search className="w-4 h-4 text-slate-400 absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              placeholder="Search campaigns by name or subject..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full bg-slate-50 border border-slate-200 rounded-xl pl-9 pr-4 py-2 text-xs font-medium text-slate-800 focus:outline-none focus:border-blue-500 focus:bg-white transition-all"
            />
          </div>

          {/* Status Filter */}
          <div className="flex items-center gap-1.5 shrink-0">
            <Filter className="w-3.5 h-3.5 text-slate-400" />
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="bg-slate-50 border border-slate-200 rounded-xl px-3 py-2 text-xs font-semibold text-slate-700 focus:outline-none focus:border-blue-500 cursor-pointer"
            >
              <option value="ALL">All Statuses</option>
              <option value="DRAFT">Draft</option>
              <option value="RUNNING">Running</option>
              <option value="COMPLETED">Completed</option>
              <option value="CANCELLED">Cancelled</option>
              <option value="FAILED">Failed</option>
            </select>
          </div>
        </div>

        {/* View Mode Switcher: List vs Grid */}
        <div className="flex items-center bg-slate-100 p-1 rounded-xl shrink-0 self-end sm:self-auto border border-slate-200">
          <button
            onClick={() => setViewMode('list')}
            className={`flex items-center gap-1.5 px-3 py-1.5 text-xs font-bold rounded-lg transition-all ${
              viewMode === 'list'
                ? 'bg-white text-blue-600 shadow-sm'
                : 'text-slate-500 hover:text-slate-800'
            }`}
            title="Switch to List View"
          >
            <List className="w-3.5 h-3.5" /> List View
          </button>
          <button
            onClick={() => setViewMode('grid')}
            className={`flex items-center gap-1.5 px-3 py-1.5 text-xs font-bold rounded-lg transition-all ${
              viewMode === 'grid'
                ? 'bg-white text-blue-600 shadow-sm'
                : 'text-slate-500 hover:text-slate-800'
            }`}
            title="Switch to Cards Grid View"
          >
            <LayoutGrid className="w-3.5 h-3.5" /> Cards View
          </button>
        </div>
      </div>

      {/* Campaign List / Cards */}
      {filteredCampaigns.length === 0 ? (
        <div className="p-12 text-center border-2 border-dashed border-slate-200 rounded-3xl bg-white space-y-3">
          <Send className="w-12 h-12 text-blue-500 mx-auto opacity-70" />
          <h3 className="text-base font-bold text-slate-800">No campaigns found</h3>
          <p className="text-xs text-slate-500 max-w-sm mx-auto font-medium">
            {searchTerm || statusFilter !== 'ALL' 
              ? 'No campaigns match your search criteria. Try clearing your filters.'
              : 'Click "+ Create New Campaign" above to build your first email campaign.'}
          </p>
          <button
            onClick={() => { setSearchTerm(''); setStatusFilter('ALL'); setIsCreateModalOpen(true); }}
            className="px-5 py-2 bg-blue-600 hover:bg-blue-700 text-white font-bold text-xs rounded-xl shadow-md transition-all cursor-pointer"
          >
            + Create New Campaign
          </button>
        </div>
      ) : viewMode === 'list' ? (
        /* ================= LIST VIEW FORMAT ================= */
        <div className="bg-white rounded-3xl border border-slate-200 shadow-sm overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="bg-slate-50 border-b border-slate-200 text-[11px] font-extrabold uppercase tracking-wider text-slate-500">
                  <th className="py-3.5 px-5">Campaign & Subject</th>
                  <th className="py-3.5 px-4">Status</th>
                  <th className="py-3.5 px-4">Template</th>
                  <th className="py-3.5 px-4 text-center">Recipients</th>
                  <th className="py-3.5 px-4 text-center">Delivered</th>
                  <th className="py-3.5 px-4 text-center">Failed</th>
                  <th className="py-3.5 px-4">Progress</th>
                  <th className="py-3.5 px-5 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 text-xs">
                {filteredCampaigns.map((c) => {
                  const total = c.totalRecipients || 0;
                  const delivered = c.sentCount || 0;
                  const failed = c.failedCount || 0;
                  const pct = total > 0 ? Math.round(((delivered + failed) / total) * 100) : 0;

                  return (
                    <tr 
                      key={c.id} 
                      className={`hover:bg-slate-50/80 transition-colors ${
                        activeLiveCampaignId === c.id ? 'bg-blue-50/50' : ''
                      }`}
                    >
                      {/* Campaign Name & Subject */}
                      <td className="py-4 px-5">
                        <div className="font-bold text-slate-900 text-sm hover:text-blue-600 transition-colors">
                          {c.name}
                        </div>
                        <div className="text-slate-500 font-mono text-[11px] mt-0.5 truncate max-w-xs">
                          {c.subject || 'No subject configured'}
                        </div>
                        <div className="text-[10px] text-slate-400 mt-0.5">
                          From: {c.senderName || 'Marcamor'} ({c.senderEmail || 'info@marcamor.com'})
                        </div>
                      </td>

                      {/* Status */}
                      <td className="py-4 px-4 whitespace-nowrap">
                        <StatusBadge status={c.status || 'DRAFT'} />
                      </td>

                      {/* Template Selector */}
                      <td className="py-4 px-4 min-w-[200px]">
                        <select
                          value={c.templateId || ''}
                          onChange={(e) => handleAttachTemplateToCampaign(c.id, e.target.value)}
                          className="bg-slate-50 hover:bg-white border border-slate-200 rounded-lg px-2 py-1 text-xs font-semibold text-slate-800 focus:outline-none focus:border-blue-500 w-full cursor-pointer transition-colors"
                        >
                          <option value="">+ Attach Template...</option>
                          {templates.map(t => (
                            <option key={t.id} value={t.id}>{t.name}</option>
                          ))}
                        </select>
                      </td>

                      {/* Recipients */}
                      <td className="py-4 px-4 text-center font-bold text-slate-800 whitespace-nowrap">
                        {total}
                      </td>

                      {/* Delivered */}
                      <td className="py-4 px-4 text-center font-bold text-emerald-600 whitespace-nowrap">
                        {delivered}
                      </td>

                      {/* Failed */}
                      <td className="py-4 px-4 text-center font-bold text-rose-600 whitespace-nowrap">
                        {failed}
                      </td>

                      {/* Progress Bar */}
                      <td className="py-4 px-4 min-w-[120px]">
                        <div className="flex items-center gap-2">
                          <div className="flex-1 bg-slate-100 rounded-full h-2 overflow-hidden border border-slate-200">
                            <div 
                              className={`h-full transition-all duration-500 ${
                                c.status === 'COMPLETED' ? 'bg-emerald-500' :
                                c.status === 'RUNNING' ? 'bg-blue-500 animate-pulse' :
                                c.status === 'FAILED' ? 'bg-rose-500' : 'bg-slate-400'
                              }`}
                              style={{ width: `${pct}%` }}
                            />
                          </div>
                          <span className="text-[10px] font-bold text-slate-500 shrink-0">{pct}%</span>
                        </div>
                      </td>

                      {/* Actions */}
                      <td className="py-4 px-5 text-right whitespace-nowrap">
                        <div className="flex items-center justify-end gap-1.5">
                          {c.status === 'RUNNING' && (
                            <button
                              onClick={() => setActiveLiveCampaignId(c.id)}
                              className="px-2.5 py-1 text-[11px] font-bold text-blue-600 bg-blue-50 hover:bg-blue-100 rounded-lg border border-blue-200 transition-colors"
                              title="View Live Stream"
                            >
                              Live
                            </button>
                          )}

                          <button
                            onClick={() => { setSelectedCampaignId(c.id); setIsAttachCollectionOpen(true); }}
                            className="p-1.5 text-slate-500 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors"
                            title="Attach Collection Cards"
                          >
                            <Layers className="w-4 h-4" />
                          </button>

                          <button
                            onClick={() => handleRunDiagnostics(c.id)}
                            className="px-3 py-1.5 text-xs font-bold bg-emerald-600 hover:bg-emerald-700 text-white rounded-lg flex items-center gap-1 shadow-sm transition-all cursor-pointer"
                            title="Launch Campaign"
                          >
                            <Play className="w-3.5 h-3.5 fill-current" /> Launch
                          </button>

                          <button
                            onClick={() => setDeleteConfirmId(c.id)}
                            className="p-1.5 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-lg transition-colors cursor-pointer"
                            title="Delete Campaign"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      ) : (
        /* ================= CARDS GRID VIEW ================= */
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {filteredCampaigns.map((c) => {
            const total = c.totalRecipients || 0;
            const delivered = c.sentCount || 0;
            const failed = c.failedCount || 0;
            const pct = total > 0 ? Math.round(((delivered + failed) / total) * 100) : 0;

            return (
              <div
                key={c.id}
                className={`bg-white border rounded-3xl p-5 space-y-4 transition-all shadow-sm flex flex-col justify-between ${
                  activeLiveCampaignId === c.id
                    ? 'border-blue-500 ring-2 ring-blue-100 shadow-md'
                    : 'border-slate-200 hover:border-slate-300 hover:shadow-md'
                }`}
              >
                <div>
                  {/* Top Row: Title & Delete Button */}
                  <div className="flex items-start justify-between gap-2">
                    <div>
                      <h3 className="text-base font-bold text-slate-900 line-clamp-1">{c.name}</h3>
                      <p className="text-xs text-slate-500 font-mono mt-0.5 line-clamp-1">
                        Subject: {c.subject || 'Not configured'}
                      </p>
                    </div>
                    <div className="flex items-center gap-1.5 shrink-0">
                      <StatusBadge status={c.status || 'DRAFT'} />
                      <button
                        onClick={() => setDeleteConfirmId(c.id)}
                        className="p-1 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-lg transition-colors cursor-pointer"
                        title="Delete Campaign"
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  </div>

                  {/* Attached Template Selector */}
                  <div className="mt-3 flex items-center gap-2 p-2 bg-slate-50 rounded-xl border border-slate-100">
                    <span className="text-[11px] font-bold text-slate-600 shrink-0">Template:</span>
                    <select
                      value={c.templateId || ''}
                      onChange={(e) => handleAttachTemplateToCampaign(c.id, e.target.value)}
                      className="bg-white border border-slate-200 rounded-lg px-2 py-1 text-xs font-semibold text-slate-800 focus:outline-none focus:border-blue-500 w-full cursor-pointer truncate"
                    >
                      <option value="">+ Attach Template...</option>
                      {templates.map(t => (
                        <option key={t.id} value={t.id}>{t.name}</option>
                      ))}
                    </select>
                  </div>

                  {/* Progress Bar */}
                  <div className="mt-3 space-y-1">
                    <div className="flex items-center justify-between text-[11px] font-bold text-slate-500">
                      <span>Dispatch Progress</span>
                      <span>{pct}%</span>
                    </div>
                    <div className="bg-slate-100 rounded-full h-2 overflow-hidden border border-slate-200">
                      <div 
                        className={`h-full transition-all duration-500 ${
                          c.status === 'COMPLETED' ? 'bg-emerald-500' :
                          c.status === 'RUNNING' ? 'bg-blue-500 animate-pulse' :
                          c.status === 'FAILED' ? 'bg-rose-500' : 'bg-slate-400'
                        }`}
                        style={{ width: `${pct}%` }}
                      />
                    </div>
                  </div>

                  {/* Metrics Cards */}
                  <div className="grid grid-cols-3 gap-2 mt-3 p-2.5 bg-slate-50 rounded-xl border border-slate-100 text-center">
                    <div>
                      <span className="text-[10px] text-slate-500 font-medium block">Recipients</span>
                      <span className="text-xs font-bold text-slate-900">{total}</span>
                    </div>
                    <div>
                      <span className="text-[10px] text-emerald-600 font-medium block">Delivered</span>
                      <span className="text-xs font-bold text-emerald-600">{delivered}</span>
                    </div>
                    <div>
                      <span className="text-[10px] text-rose-600 font-medium block">Failed</span>
                      <span className="text-xs font-bold text-rose-600">{failed}</span>
                    </div>
                  </div>
                </div>

                {/* Card Actions Footer */}
                <div className="flex items-center justify-between pt-3 border-t border-slate-100">
                  <button
                    onClick={() => { setSelectedCampaignId(c.id); setIsAttachCollectionOpen(true); }}
                    className="text-xs font-bold text-blue-600 hover:text-blue-700 flex items-center gap-1 cursor-pointer"
                  >
                    <Layers className="w-3.5 h-3.5" /> Attach Collection
                  </button>

                  <div className="flex items-center gap-2">
                    {c.status === 'RUNNING' && (
                      <button
                        onClick={() => setActiveLiveCampaignId(c.id)}
                        className="px-3 py-1.5 text-xs font-bold text-blue-600 hover:text-blue-700 bg-blue-50 hover:bg-blue-100 rounded-xl border border-blue-200 transition-colors"
                      >
                        Live
                      </button>
                    )}
                    <button
                      onClick={() => handleRunDiagnostics(c.id)}
                      className="px-3.5 py-1.5 text-xs font-bold bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl flex items-center gap-1 shadow-sm transition-all hover:scale-105 cursor-pointer"
                    >
                      <Play className="w-3.5 h-3.5 fill-current" /> Launch
                    </button>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}

      {/* Pre-Launch Diagnostics Modal */}
      <CampaignDiagnosticsModal
        isOpen={isDiagnosticsOpen}
        onClose={() => setIsDiagnosticsOpen(false)}
        diagnostics={currentDiagnostics}
        templates={templates}
        onAttachTemplate={(campId, tId) => handleAttachTemplateToCampaign(campId, tId)}
        onConfirmLaunch={handleConfirmLaunch}
      />

      {/* Create Campaign Modal */}
      <Modal isOpen={isCreateModalOpen} onClose={() => setIsCreateModalOpen(false)} title="Create New Email Campaign">
        <form onSubmit={handleCreateSubmit} className="space-y-4">
          <div>
            <label className="block text-xs font-bold text-slate-700 mb-1.5">Campaign Name</label>
            <input
              type="text"
              required
              placeholder="e.g. Black Friday 2026 Enterprise"
              value={name}
              onChange={e => setName(e.target.value)}
              className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-700 mb-1.5">Email Subject Line</label>
            <input
              type="text"
              required
              placeholder="e.g. Special Offer for {{company}}"
              value={subject}
              onChange={e => setSubject(e.target.value)}
              className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 mb-1.5">Sender Name</label>
              <input
                type="text"
                required
                placeholder="e.g. Marcamor Enterprise"
                value={senderName}
                onChange={e => setSenderName(e.target.value)}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium"
              />
            </div>
            <div>
              <label className="block text-xs font-bold text-slate-700 mb-1.5">Sender Email ID</label>
              <input
                type="email"
                required
                placeholder="e.g. info@marcamor.com"
                value={senderEmail}
                onChange={e => setSenderEmail(e.target.value)}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 mb-1.5">Batch Size</label>
              <input
                type="number"
                value={batchSize}
                onChange={e => setBatchSize(Number(e.target.value))}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium"
              />
            </div>
            <div>
              <label className="block text-xs font-bold text-slate-700 mb-1.5">Retry Count</label>
              <input
                type="number"
                value={retryCount}
                onChange={e => setRetryCount(Number(e.target.value))}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium"
              />
            </div>
          </div>

          <button
            type="submit"
            className="w-full py-3 bg-blue-600 hover:bg-blue-700 text-white font-bold text-sm rounded-xl shadow-md cursor-pointer"
          >
            Save Campaign
          </button>
        </form>
      </Modal>

      {/* Attach Collection Modal */}
      <Modal isOpen={isAttachCollectionOpen} onClose={() => setIsAttachCollectionOpen(false)} title="Attach Collection to Campaign">
        <div className="space-y-4">
          <label className="block text-xs font-bold text-slate-700">Choose Contact Collection Card:</label>
          <select
            value={selectedCollectionId}
            onChange={e => setSelectedCollectionId(e.target.value)}
            className="w-full bg-slate-50 border border-slate-200 rounded-xl p-3 text-xs text-slate-900 font-medium"
          >
            <option value="">Select Collection...</option>
            {collections.map(coll => (
              <option key={coll.id} value={coll.id}>{coll.name}</option>
            ))}
          </select>

          <button
            onClick={handleAttachCollection}
            disabled={!selectedCollectionId}
            className="w-full py-3 bg-blue-600 hover:bg-blue-700 disabled:opacity-50 text-white font-bold text-sm rounded-xl shadow-md cursor-pointer"
          >
            Attach Collection Recipients
          </button>
        </div>
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal isOpen={Boolean(deleteConfirmId)} onClose={() => setDeleteConfirmId(null)} title="Delete Campaign">
        <div className="space-y-4 text-center p-2">
          <div className="w-12 h-12 bg-rose-100 text-rose-600 rounded-2xl flex items-center justify-center mx-auto">
            <Trash2 className="w-6 h-6" />
          </div>
          <h3 className="text-base font-bold text-slate-900">Are you sure you want to delete this campaign?</h3>
          <p className="text-xs text-slate-500 max-w-sm mx-auto">
            This action will soft-delete the campaign. Past delivery logs will be preserved, but the campaign will be removed from your active list.
          </p>

          <div className="flex items-center gap-3 pt-3">
            <button
              onClick={() => setDeleteConfirmId(null)}
              className="flex-1 py-2.5 bg-slate-100 hover:bg-slate-200 text-slate-700 font-bold text-xs rounded-xl transition-colors cursor-pointer"
            >
              Cancel
            </button>
            <button
              onClick={handleDeleteCampaign}
              disabled={deleting}
              className="flex-1 py-2.5 bg-rose-600 hover:bg-rose-700 disabled:opacity-50 text-white font-bold text-xs rounded-xl shadow-md transition-all cursor-pointer"
            >
              {deleting ? 'Deleting...' : 'Yes, Delete Campaign'}
            </button>
          </div>
        </div>
      </Modal>

    </div>
  );
};
