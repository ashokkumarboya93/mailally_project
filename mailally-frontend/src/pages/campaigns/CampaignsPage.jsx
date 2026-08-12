import React, { useState, useEffect } from 'react';
import { campaignApi, templateApi } from '../../api/campaignApi';
import { contactApi } from '../../api/contactApi';
import { StatusBadge } from '../../components/common/StatusBadge';
import { Modal } from '../../components/common/Modal';
import { AlertModal } from '../../components/common/AlertModal';
import { PageSkeletonLoader } from '../../components/common/PageSkeletonLoader';
import { LiveSendingDashboard } from '../../components/campaigns/LiveSendingDashboard';
import { CampaignDiagnosticsModal } from '../../components/campaigns/CampaignDiagnosticsModal';
import { 
  Play, Plus, Trash2, 
  Send, LayoutGrid, List, Search, Filter, Layers
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

  // Active Running Campaign State
  const [activeLiveCampaignId, setActiveLiveCampaignId] = useState(null);

  // Alert Modal State
  const [alertConfig, setAlertConfig] = useState({ isOpen: false, type: 'success', title: '', message: '' });
  const showAlert = (type, message, title = '') => setAlertConfig({ isOpen: true, type, message, title: title || (type === 'success' ? 'Success' : 'Error') });
  const closeAlert = () => setAlertConfig(prev => ({ ...prev, isOpen: false }));

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
      showAlert('error', 'Failed to create campaign: ' + (e.response?.data?.message || e.message));
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
      showAlert('error', 'Failed to attach template: ' + (e.response?.data?.message || e.message));
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
      showAlert('error', 'Failed to attach collection: ' + (e.response?.data?.message || e.message));
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
      showAlert('error', 'Failed to delete campaign: ' + (e.response?.data?.message || e.message));
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
    <div className="space-y-6 animate-fadeInUp pb-8 font-sans">
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold tracking-tight text-[#0A0A0B]">Campaigns</h1>
          <p className="text-[13px] text-[#9CA3AF] font-medium mt-1">
            Create, manage & execute bulk outreach campaigns.
          </p>
        </div>

        <button
          onClick={() => setIsCreateModalOpen(true)}
          className="ma-btn ma-btn-primary gap-1.5 text-[12px]"
        >
          <Plus className="w-4 h-4" /> Create Campaign
        </button>
      </div>

      {/* LIVE SENDING DASHBOARD */}
      {activeLiveCampaignId && (
        <LiveSendingDashboard
          campaignId={activeLiveCampaignId}
          onClose={() => setActiveLiveCampaignId(null)}
          onFinished={() => loadData()}
        />
      )}

      {/* Controls Bar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 bg-white p-2 rounded-[16px] border border-[#E5E5E7]">
        <div className="flex items-center gap-3 flex-1">
          {/* Search Input */}
          <div className="relative flex-1 max-w-md">
            <Search className="w-3.5 h-3.5 text-[#C0C5CC] absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              placeholder="Search campaigns..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-9 pr-3 h-8 text-[12px] font-medium bg-[#FAFAFB] border border-[#E5E5E7] rounded-lg outline-none focus:border-[#D1D5DB]"
            />
          </div>

          {/* Status Filter */}
          <div className="flex items-center gap-1.5 shrink-0">
            <Filter className="w-3.5 h-3.5 text-[#9CA3AF]" />
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="bg-[#FAFAFB] border border-[#E5E5E7] rounded-lg px-2.5 h-8 text-[12px] font-medium text-[#5F6368] outline-none cursor-pointer"
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

        {/* View Mode Switcher */}
        <div className="flex items-center bg-[#FAFAFB] p-1 rounded-lg shrink-0 border border-[#E5E5E7]">
          <button
            onClick={() => setViewMode('list')}
            className={`px-3 py-1 text-[12px] font-semibold rounded-md transition-all cursor-pointer ${
              viewMode === 'list' ? 'bg-white text-[#0A0A0B] shadow-xs' : 'text-[#9CA3AF]'
            }`}
          >
            <List className="w-3.5 h-3.5 inline mr-1" /> List
          </button>
          <button
            onClick={() => setViewMode('grid')}
            className={`px-3 py-1 text-[12px] font-semibold rounded-md transition-all cursor-pointer ${
              viewMode === 'grid' ? 'bg-white text-[#0A0A0B] shadow-xs' : 'text-[#9CA3AF]'
            }`}
          >
            <LayoutGrid className="w-3.5 h-3.5 inline mr-1" /> Cards
          </button>
        </div>
      </div>

      {/* Campaign List / Cards */}
      {filteredCampaigns.length === 0 ? (
        <div className="py-16 text-center border border-dashed border-[#E5E5E7] rounded-[16px] bg-white space-y-3">
          <Send className="w-10 h-10 text-[#9CA3AF] mx-auto opacity-50" />
          <h3 className="text-base font-bold text-[#0A0A0B]">No campaigns found</h3>
          <p className="text-[13px] text-[#9CA3AF] max-w-sm mx-auto font-medium">
            {searchTerm || statusFilter !== 'ALL' 
              ? 'No campaigns match your search criteria.'
              : 'Create your first campaign to start outreach.'}
          </p>
          <button
            onClick={() => { setSearchTerm(''); setStatusFilter('ALL'); setIsCreateModalOpen(true); }}
            className="ma-btn ma-btn-primary text-[12px] mt-2"
          >
            + Create Campaign
          </button>
        </div>
      ) : viewMode === 'list' ? (
        /* LIST VIEW */
        <div className="bg-white rounded-[16px] border border-[#E5E5E7] overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="bg-[#FAFAFB] border-b border-[#E5E5E7] text-[11px] font-semibold uppercase tracking-wider text-[#9CA3AF]">
                  <th className="py-3 px-5">Campaign</th>
                  <th className="py-3 px-4">Status</th>
                  <th className="py-3 px-4">Template</th>
                  <th className="py-3 px-4 text-center">Recipients</th>
                  <th className="py-3 px-4 text-center">Delivered</th>
                  <th className="py-3 px-4 text-center">Failed</th>
                  <th className="py-3 px-4">Progress</th>
                  <th className="py-3 px-5 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[#F0F0F2] text-[13px]">
                {filteredCampaigns.map((c) => {
                  const total = c.totalRecipients || 0;
                  const delivered = c.sentCount || 0;
                  const failed = c.failedCount || 0;
                  const pct = total > 0 ? Math.round(((delivered + failed) / total) * 100) : 0;

                  return (
                    <tr key={c.id} className="hover:bg-[#FAFAFB] transition-colors">
                      <td className="py-3.5 px-5">
                        <div className="font-semibold text-[#0A0A0B]">
                          {c.name}
                        </div>
                        <div className="text-[#9CA3AF] font-mono text-[11px] mt-0.5 truncate max-w-xs">
                          {c.subject || 'No subject'}
                        </div>
                      </td>

                      <td className="py-3.5 px-4 whitespace-nowrap">
                        <StatusBadge status={c.status || 'DRAFT'} />
                      </td>

                      <td className="py-3.5 px-4 min-w-[180px]">
                        <select
                          value={c.templateId || ''}
                          onChange={(e) => handleAttachTemplateToCampaign(c.id, e.target.value)}
                          className="bg-[#FAFAFB] border border-[#E5E5E7] rounded-lg px-2 py-1 text-[12px] font-medium text-[#0A0A0B] w-full outline-none"
                        >
                          <option value="">+ Template...</option>
                          {templates.map(t => (
                            <option key={t.id} value={t.id}>{t.name}</option>
                          ))}
                        </select>
                      </td>

                      <td className="py-3.5 px-4 text-center font-bold text-[#0A0A0B] whitespace-nowrap">
                        {total}
                      </td>

                      <td className="py-3.5 px-4 text-center font-bold text-[#16A34A] whitespace-nowrap">
                        {delivered}
                      </td>

                      <td className="py-3.5 px-4 text-center font-bold text-[#E11D48] whitespace-nowrap">
                        {failed}
                      </td>

                      <td className="py-3.5 px-4 min-w-[120px]">
                        <div className="flex items-center gap-2">
                          <div className="flex-1 bg-[#F3F4F6] rounded-full h-1.5 overflow-hidden">
                            <div 
                              className="h-full bg-[#0A0A0B] transition-all duration-300"
                              style={{ width: `${pct}%` }}
                            />
                          </div>
                          <span className="text-[11px] font-semibold text-[#9CA3AF]">{pct}%</span>
                        </div>
                      </td>

                      <td className="py-3.5 px-5 text-right whitespace-nowrap">
                        <div className="flex items-center justify-end gap-2">
                          <button
                            onClick={() => { setSelectedCampaignId(c.id); setIsAttachCollectionOpen(true); }}
                            className="p-1 text-[#9CA3AF] hover:text-[#0A0A0B] cursor-pointer"
                            title="Attach Collection"
                          >
                            <Layers className="w-4 h-4" />
                          </button>

                          <button
                            onClick={() => handleRunDiagnostics(c.id)}
                            className="px-2.5 py-1 text-[11px] font-semibold bg-[#0A0A0B] text-white rounded-md flex items-center gap-1 cursor-pointer"
                          >
                            <Play className="w-3 h-3 fill-current" /> Launch
                          </button>

                          <button
                            onClick={() => setDeleteConfirmId(c.id)}
                            className="p-1 text-[#9CA3AF] hover:text-[#E11D48] cursor-pointer"
                            title="Delete"
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
        /* CARDS GRID VIEW */
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {filteredCampaigns.map((c) => {
            const total = c.totalRecipients || 0;
            const delivered = c.sentCount || 0;
            const failed = c.failedCount || 0;
            const pct = total > 0 ? Math.round(((delivered + failed) / total) * 100) : 0;

            return (
              <div
                key={c.id}
                className="bg-white border border-[#E5E5E7] rounded-[16px] p-5 space-y-4 flex flex-col justify-between hover:shadow-sm transition-all"
              >
                <div>
                  <div className="flex items-start justify-between gap-2">
                    <div>
                      <h3 className="text-[15px] font-bold text-[#0A0A0B] truncate">{c.name}</h3>
                      <p className="text-[12px] text-[#9CA3AF] font-mono mt-0.5 truncate">
                        Subject: {c.subject || 'Not configured'}
                      </p>
                    </div>
                    <StatusBadge status={c.status || 'DRAFT'} />
                  </div>

                  <div className="mt-3 flex items-center gap-2 p-2 bg-[#FAFAFB] rounded-lg border border-[#E5E5E7]">
                    <span className="text-[11px] font-semibold text-[#9CA3AF] shrink-0">Template:</span>
                    <select
                      value={c.templateId || ''}
                      onChange={(e) => handleAttachTemplateToCampaign(c.id, e.target.value)}
                      className="bg-white border border-[#E5E5E7] rounded px-2 py-0.5 text-[11px] font-medium text-[#0A0A0B] w-full outline-none cursor-pointer"
                    >
                      <option value="">+ Template...</option>
                      {templates.map(t => (
                        <option key={t.id} value={t.id}>{t.name}</option>
                      ))}
                    </select>
                  </div>

                  <div className="mt-3 space-y-1">
                    <div className="flex items-center justify-between text-[11px] font-semibold text-[#9CA3AF]">
                      <span>Progress</span>
                      <span>{pct}%</span>
                    </div>
                    <div className="bg-[#F3F4F6] rounded-full h-1.5 overflow-hidden">
                      <div className="h-full bg-[#0A0A0B] transition-all duration-300" style={{ width: `${pct}%` }} />
                    </div>
                  </div>

                  <div className="grid grid-cols-3 gap-2 mt-3 p-2 bg-[#FAFAFB] rounded-lg text-center">
                    <div>
                      <span className="text-[10px] text-[#9CA3AF] font-semibold block">Total</span>
                      <span className="text-[12px] font-bold text-[#0A0A0B]">{total}</span>
                    </div>
                    <div>
                      <span className="text-[10px] text-[#16A34A] font-semibold block">Sent</span>
                      <span className="text-[12px] font-bold text-[#16A34A]">{delivered}</span>
                    </div>
                    <div>
                      <span className="text-[10px] text-[#E11D48] font-semibold block">Failed</span>
                      <span className="text-[12px] font-bold text-[#E11D48]">{failed}</span>
                    </div>
                  </div>
                </div>

                <div className="flex items-center justify-between pt-3 border-t border-[#F0F0F2]">
                  <button
                    onClick={() => { setSelectedCampaignId(c.id); setIsAttachCollectionOpen(true); }}
                    className="text-[12px] font-semibold text-[#5F6368] hover:text-[#0A0A0B] flex items-center gap-1 cursor-pointer"
                  >
                    <Layers className="w-3.5 h-3.5" /> Collection
                  </button>

                  <div className="flex items-center gap-2">
                    <button
                      onClick={() => handleRunDiagnostics(c.id)}
                      className="px-3 py-1 text-[12px] font-semibold bg-[#0A0A0B] text-white rounded-lg flex items-center gap-1 cursor-pointer"
                    >
                      <Play className="w-3 h-3 fill-current" /> Launch
                    </button>
                    <button
                      onClick={() => setDeleteConfirmId(c.id)}
                      className="p-1 text-[#9CA3AF] hover:text-[#E11D48] cursor-pointer"
                    >
                      <Trash2 className="w-3.5 h-3.5" />
                    </button>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}

      {/* Diagnostics Modal */}
      <CampaignDiagnosticsModal
        isOpen={isDiagnosticsOpen}
        onClose={() => setIsDiagnosticsOpen(false)}
        diagnostics={currentDiagnostics}
        templates={templates}
        onAttachTemplate={(campId, tId) => handleAttachTemplateToCampaign(campId, tId)}
        onConfirmLaunch={handleConfirmLaunch}
      />

      {/* Create Campaign Modal */}
      <Modal isOpen={isCreateModalOpen} onClose={() => setIsCreateModalOpen(false)} title="Create New Campaign">
        <form onSubmit={handleCreateSubmit} className="space-y-4">
          <div>
            <label className="ma-label">Campaign Name</label>
            <input
              type="text"
              required
              placeholder="e.g. Black Friday Sale"
              value={name}
              onChange={e => setName(e.target.value)}
              className="ma-input"
            />
          </div>

          <div>
            <label className="ma-label">Email Subject Line</label>
            <input
              type="text"
              required
              placeholder="e.g. Special Offer inside"
              value={subject}
              onChange={e => setSubject(e.target.value)}
              className="ma-input"
            />
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="ma-label">Sender Name</label>
              <input
                type="text"
                required
                value={senderName}
                onChange={e => setSenderName(e.target.value)}
                className="ma-input"
              />
            </div>
            <div>
              <label className="ma-label">Sender Email</label>
              <input
                type="email"
                required
                value={senderEmail}
                onChange={e => setSenderEmail(e.target.value)}
                className="ma-input"
              />
            </div>
          </div>

          <button
            type="submit"
            className="ma-btn ma-btn-primary w-full"
          >
            Create Campaign
          </button>
        </form>
      </Modal>

      {/* Attach Collection Modal */}
      <Modal isOpen={isAttachCollectionOpen} onClose={() => setIsAttachCollectionOpen(false)} title="Attach Collection">
        <div className="space-y-4">
          <label className="ma-label">Choose Collection:</label>
          <select
            value={selectedCollectionId}
            onChange={e => setSelectedCollectionId(e.target.value)}
            className="ma-select"
          >
            <option value="">Select Collection...</option>
            {collections.map(coll => (
              <option key={coll.id} value={coll.id}>{coll.name}</option>
            ))}
          </select>

          <button
            onClick={handleAttachCollection}
            disabled={!selectedCollectionId}
            className="ma-btn ma-btn-primary w-full"
          >
            Attach Recipients
          </button>
        </div>
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal isOpen={Boolean(deleteConfirmId)} onClose={() => setDeleteConfirmId(null)} title="Delete Campaign">
        <div className="space-y-4 text-center">
          <p className="text-[13px] text-[#5F6368]">
            Are you sure you want to delete this campaign?
          </p>

          <div className="flex items-center gap-3 pt-2">
            <button
              onClick={() => setDeleteConfirmId(null)}
              className="ma-btn ma-btn-secondary flex-1"
            >
              Cancel
            </button>
            <button
              onClick={handleDeleteCampaign}
              disabled={deleting}
              className="ma-btn ma-btn-danger flex-1"
            >
              {deleting ? 'Deleting...' : 'Delete Campaign'}
            </button>
          </div>
        </div>
      </Modal>

      {/* Alert Status Modal */}
      <AlertModal
        isOpen={alertConfig.isOpen}
        onClose={closeAlert}
        type={alertConfig.type}
        title={alertConfig.title}
        message={alertConfig.message}
      />

    </div>
  );
};
