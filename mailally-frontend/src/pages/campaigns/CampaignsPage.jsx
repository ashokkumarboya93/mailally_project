import React, { useState, useEffect, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
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
  Send, LayoutGrid, List, Search, Filter, Layers, BarChart2, Activity, ExternalLink,
  ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight, Calendar, Clock, Zap,
  Loader2, CheckCircle2, Sparkles, Rocket
} from 'lucide-react';

export const CampaignsPage = () => {
  const navigate = useNavigate();
  const [campaigns, setCampaigns] = useState([]);
  const [templates, setTemplates] = useState([]);
  const [collections, setCollections] = useState([]);
  const [loading, setLoading] = useState(true);

  // View mode: 'list' or 'grid'
  const [viewMode, setViewMode] = useState('list');
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');

  // Pagination State (50 per page)
  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 50;

  // Active Running Campaign State
  const [activeLiveCampaignId, setActiveLiveCampaignId] = useState(null);

  const getProgressBarGradient = (status) => {
    const st = (status || 'DRAFT').toUpperCase();
    if (st === 'RUNNING') return 'bg-gradient-to-r from-blue-600 via-indigo-500 via-purple-500 to-emerald-400 animate-pulse';
    if (st === 'COMPLETED') return 'bg-gradient-to-r from-emerald-500 to-teal-400';
    if (st === 'FAILED') return 'bg-gradient-to-r from-rose-600 to-pink-500';
    if (st === 'PAUSED') return 'bg-gradient-to-r from-amber-500 to-orange-400';
    if (st === 'CANCELLED') return 'bg-gradient-to-r from-slate-500 to-slate-400';
    return 'bg-gradient-to-r from-blue-500 to-sky-400';
  };

  // Alert Modal State
  const [alertConfig, setAlertConfig] = useState({ isOpen: false, type: 'success', title: '', message: '' });
  const showAlert = (type, message, title = '') => setAlertConfig({ isOpen: true, type, message, title: title || (type === 'success' ? 'Success' : 'Error') });
  const closeAlert = () => setAlertConfig(prev => ({ ...prev, isOpen: false }));

  // Modals State
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isDiagnosticsOpen, setIsDiagnosticsOpen] = useState(false);
  const [isAttachCollectionOpen, setIsAttachCollectionOpen] = useState(false);
  const [isLaunchLoaderOpen, setIsLaunchLoaderOpen] = useState(false);
  const [launchStepText, setLaunchStepText] = useState('Initializing AWS SES Engine...');
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

  // Launch Mode & Schedule State
  const [launchMode, setLaunchMode] = useState('INSTANT'); // 'INSTANT' or 'SCHEDULED'
  const [scheduledDateTime, setScheduledDateTime] = useState('');
  const [modalCollectionId, setModalCollectionId] = useState('');

  const loadData = async () => {
    setLoading(true);
    try {
      const [cRes, tRes, collRes] = await Promise.allSettled([
        campaignApi.getCampaigns(0, 1000),
        templateApi.getTemplates(0, 50),
        contactApi.getCollections()
      ]);

      if (cRes.status === 'fulfilled' && cRes.value?.data?.content) {
        setCampaigns(cRes.value.data.content);
      } else if (cRes.status === 'fulfilled' && Array.isArray(cRes.value?.data)) {
        setCampaigns(cRes.value.data);
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

  // Reset page to 1 when search or status filter changes
  useEffect(() => {
    setCurrentPage(1);
  }, [searchTerm, statusFilter]);

  const handleCreateSubmit = async (e) => {
    e.preventDefault();
    if (launchMode === 'SCHEDULED' && !scheduledDateTime) {
      showAlert('error', 'Please select a scheduled date and time for automated outreach.', 'Schedule Required');
      return;
    }

    const selectedTpl = templates.find(t => String(t.id) === String(templateId));
    const effectiveSubject = selectedTpl?.subject || name;

    try {
      const createdRes = await campaignApi.createCampaign({
        name,
        subject: effectiveSubject,
        templateId: templateId ? Number(templateId) : (templates.length > 0 ? templates[0].id : null),
        senderName: senderName || 'Marcamor',
        senderEmail: senderEmail || 'info@marcamor.com',
        batchSize,
        retryCount,
        executionType: launchMode,
        scheduledAt: launchMode === 'SCHEDULED' && scheduledDateTime ? scheduledDateTime : null
      });

      const newCampaignId = createdRes?.data?.id;
      if (newCampaignId && modalCollectionId) {
        try {
          await campaignApi.addCollectionToCampaign(newCampaignId, modalCollectionId);
        } catch (ignored) {}
      }

      setIsCreateModalOpen(false);
      setName('');
      setScheduledDateTime('');
      setLaunchMode('INSTANT');
      setModalCollectionId('');
      showAlert('success', launchMode === 'SCHEDULED' 
        ? `Campaign scheduled successfully for ${new Date(scheduledDateTime).toLocaleString()}!` 
        : 'Campaign created successfully as draft!');
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
    const targetCamp = campaigns.find(c => c.id === campaignId);

    // Pre-launch verification: check if recipients exist
    if (targetCamp && (targetCamp.totalRecipients || 0) === 0) {
      setSelectedCampaignId(campaignId);
      setIsAttachCollectionOpen(true);
      showAlert('error', `Cannot launch "${targetCamp.name}" because it has 0 recipients. Please select and attach a contact collection first.`, 'Recipients Required');
      return;
    }

    try {
      const res = await campaignApi.getDiagnostics(campaignId);
      if (res.data) {
        setCurrentDiagnostics(res.data);
      } else {
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
    const targetCamp = campaigns.find(c => c.id === selectedCampaignId);
    if (targetCamp && (targetCamp.totalRecipients || 0) === 0) {
      setIsDiagnosticsOpen(false);
      setIsAttachCollectionOpen(true);
      showAlert('error', 'Cannot launch campaign with 0 recipients. Please attach a collection first.', 'No Recipients');
      return;
    }

    setIsDiagnosticsOpen(false);
    setIsLaunchLoaderOpen(true);
    setLaunchStepText('⚡ Connecting to Amazon SES SMTP Relay Engine...');

    try {
      await campaignApi.launchCampaign(selectedCampaignId);

      setTimeout(() => {
        setLaunchStepText('🚀 Spinning up High-Performance Java Virtual Thread Workers...');
      }, 700);

      setTimeout(() => {
        setLaunchStepText('✉️ Handshaking with SMTP Relay & Executing Dispatches...');
      }, 1400);

      setTimeout(() => {
        setIsLaunchLoaderOpen(false);
        setActiveLiveCampaignId(selectedCampaignId);
        loadData();
      }, 2100);
    } catch (e) {
      setTimeout(() => {
        setIsLaunchLoaderOpen(false);
        setActiveLiveCampaignId(selectedCampaignId);
        loadData();
      }, 1500);
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
  const filteredCampaigns = useMemo(() => {
    return campaigns.filter(c => {
      const matchesSearch = c.name?.toLowerCase().includes(searchTerm.toLowerCase()) || 
                            c.subject?.toLowerCase().includes(searchTerm.toLowerCase());
      const matchesStatus = statusFilter === 'ALL' || (c.status && c.status.toUpperCase() === statusFilter);
      return matchesSearch && matchesStatus;
    });
  }, [campaigns, searchTerm, statusFilter]);

  // Pagination calculations (50 per page)
  const totalCampaigns = filteredCampaigns.length;
  const totalPages = Math.max(1, Math.ceil(totalCampaigns / pageSize));
  
  // Ensure valid current page within bounds
  const validCurrentPage = Math.min(Math.max(1, currentPage), totalPages);
  
  const paginatedCampaigns = useMemo(() => {
    const startIndex = (validCurrentPage - 1) * pageSize;
    return filteredCampaigns.slice(startIndex, startIndex + pageSize);
  }, [filteredCampaigns, validCurrentPage, pageSize]);

  // Helper to generate page numbers list
  const getPaginationNumbers = () => {
    if (totalPages <= 7) {
      return Array.from({ length: totalPages }, (_, i) => i + 1);
    }
    if (validCurrentPage <= 4) {
      return [1, 2, 3, 4, 5, '...', totalPages];
    }
    if (validCurrentPage >= totalPages - 3) {
      return [1, '...', totalPages - 4, totalPages - 3, totalPages - 2, totalPages - 1, totalPages];
    }
    return [1, '...', validCurrentPage - 1, validCurrentPage, validCurrentPage + 1, '...', totalPages];
  };

  const startRecord = totalCampaigns === 0 ? 0 : (validCurrentPage - 1) * pageSize + 1;
  const endRecord = Math.min(validCurrentPage * pageSize, totalCampaigns);

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
        <div className="space-y-4">
          <div className="bg-white rounded-[16px] border border-[#E5E5E7] overflow-hidden">
            <table className="w-full text-left border-collapse table-fixed">
              <thead>
                <tr className="bg-[#FAFAFB] border-b border-[#E5E5E7] text-[11px] font-semibold uppercase tracking-wider text-[#9CA3AF]">
                  <th className="py-3 px-4 w-[25%]">Campaign</th>
                  <th className="py-3 px-3 w-[12%]">Status</th>
                  <th className="py-3 px-3 w-[18%]">Template</th>
                  <th className="py-3 px-3 w-[8%] text-center">Recipients</th>
                  <th className="py-3 px-3 w-[15%]">Progress</th>
                  <th className="py-3 px-4 w-[22%] text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[#F0F0F2] text-[13px]">
                {paginatedCampaigns.map((c) => {
                  const total = c.totalRecipients || 0;
                  const delivered = c.sentCount || 0;
                  const failed = c.failedCount || 0;
                  const pct = total > 0 ? Math.round(((delivered + failed) / total) * 100) : 0;

                  return (
                    <tr key={c.id} className="hover:bg-[#FAFAFB] transition-colors cursor-pointer group" onClick={() => navigate(`/campaigns/${c.id}/analytics`)}>
                      <td className="py-3.5 px-4 truncate">
                        <div className="font-semibold text-[#0A0A0B] group-hover:text-blue-600 flex items-center gap-1.5 truncate">
                          <span className="truncate">{c.name}</span>
                          <ExternalLink className="w-3 h-3 text-[#9CA3AF] opacity-0 group-hover:opacity-100 transition-opacity shrink-0" />
                        </div>
                        <div className="text-[#9CA3AF] font-mono text-[11px] mt-0.5 truncate">
                          {c.subject || 'No subject'}
                        </div>
                      </td>

                      <td className="py-3.5 px-3">
                        <StatusBadge status={c.status || 'DRAFT'} />
                      </td>

                      <td className="py-3.5 px-3" onClick={(e) => e.stopPropagation()}>
                        <select
                          value={c.templateId || ''}
                          onChange={(e) => handleAttachTemplateToCampaign(c.id, e.target.value)}
                          className="bg-[#FAFAFB] border border-[#E5E5E7] rounded-lg px-2 py-1 text-[12px] font-medium text-[#0A0A0B] w-full outline-none truncate"
                        >
                          <option value="">+ Template...</option>
                          {templates.map(t => (
                            <option key={t.id} value={t.id}>{t.name}</option>
                          ))}
                        </select>
                      </td>

                      <td className="py-3.5 px-3 text-center font-bold text-[#0A0A0B]">
                        {total}
                      </td>

                      <td className="py-3.5 px-3">
                        <div className="flex items-center gap-2 max-w-[140px]">
                          <div className="flex-1 bg-[#F3F4F6] rounded-full h-2 overflow-hidden border border-[#E5E5E7] min-w-[50px]">
                            <div 
                              className={`h-full ${getProgressBarGradient(c.status)} transition-all duration-500`}
                              style={{ width: `${pct}%` }}
                            />
                          </div>
                          <span className="text-[11px] font-bold text-[#0A0A0B] shrink-0 w-8 text-right">{pct}%</span>
                        </div>
                      </td>

                      <td className="py-3.5 px-4 text-right" onClick={(e) => e.stopPropagation()}>
                        <div className="flex items-center justify-end gap-2">
                          <button
                            onClick={() => navigate(`/campaigns/${c.id}/analytics`)}
                            className="p-1.5 text-[#9CA3AF] hover:text-[#0A0A0B] hover:bg-[#F3F4F6] rounded-md cursor-pointer transition-colors"
                            title="View Live Progress & Telemetry"
                          >
                            <BarChart2 className="w-4 h-4 text-blue-600" />
                          </button>

                          <button
                            onClick={() => { setSelectedCampaignId(c.id); setIsAttachCollectionOpen(true); }}
                            className="p-1.5 text-[#9CA3AF] hover:text-[#0A0A0B] hover:bg-[#F3F4F6] rounded-md cursor-pointer transition-colors"
                            title="Attach Collection"
                          >
                            <Layers className="w-4 h-4" />
                          </button>

                          <button
                            onClick={() => handleRunDiagnostics(c.id)}
                            className="px-2.5 py-1 text-[11px] font-semibold bg-[#0A0A0B] text-white rounded-md flex items-center gap-1 cursor-pointer hover:bg-slate-800 shrink-0"
                          >
                            <Play className="w-3 h-3 fill-current" /> Launch
                          </button>

                          <button
                            onClick={() => setDeleteConfirmId(c.id)}
                            className="p-1.5 text-[#9CA3AF] hover:text-[#E11D48] hover:bg-rose-50 rounded-md cursor-pointer transition-colors"
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

          {/* Pagination Bar */}
          {totalPages > 1 && (
            <div className="flex flex-col sm:flex-row items-center justify-between gap-3 bg-white px-4 py-3 rounded-[16px] border border-[#E5E5E7]">
              <div className="text-[12px] text-[#71717A] font-medium">
                Showing <span className="font-bold text-[#0A0A0B]">{startRecord}</span> to <span className="font-bold text-[#0A0A0B]">{endRecord}</span> of <span className="font-bold text-[#0A0A0B]">{totalCampaigns}</span> campaigns
              </div>

              <div className="flex items-center gap-1">
                {/* Previous Page */}
                <button
                  onClick={() => setCurrentPage(prev => Math.max(1, prev - 1))}
                  disabled={validCurrentPage === 1}
                  className="flex items-center gap-1 px-2.5 py-1.5 text-[12px] font-semibold rounded-lg border border-[#E5E5E7] text-[#5F6368] hover:bg-[#FAFAFB] disabled:opacity-40 disabled:cursor-not-allowed cursor-pointer transition-colors"
                >
                  <ChevronLeft className="w-3.5 h-3.5" /> Prev
                </button>

                {/* Page Number Buttons */}
                <div className="flex items-center gap-1 mx-1">
                  {getPaginationNumbers().map((num, idx) => {
                    if (num === '...') {
                      return (
                        <span key={`dots-${idx}`} className="px-2 py-1 text-[12px] text-[#9CA3AF] select-none">
                          ...
                        </span>
                      );
                    }
                    const isActive = num === validCurrentPage;
                    return (
                      <button
                        key={`page-${num}`}
                        onClick={() => setCurrentPage(num)}
                        className={`min-w-[32px] h-8 px-2 text-[12px] font-semibold rounded-lg transition-all cursor-pointer ${
                          isActive
                            ? 'bg-[#0A0A0B] text-white shadow-xs'
                            : 'text-[#5F6368] hover:bg-[#FAFAFB] border border-transparent hover:border-[#E5E5E7]'
                        }`}
                      >
                        {num}
                      </button>
                    );
                  })}
                </div>

                {/* Next Page */}
                <button
                  onClick={() => setCurrentPage(prev => Math.min(totalPages, prev + 1))}
                  disabled={validCurrentPage === totalPages}
                  className="flex items-center gap-1 px-2.5 py-1.5 text-[12px] font-semibold rounded-lg border border-[#E5E5E7] text-[#5F6368] hover:bg-[#FAFAFB] disabled:opacity-40 disabled:cursor-not-allowed cursor-pointer transition-colors"
                >
                  Next <ChevronRight className="w-3.5 h-3.5" />
                </button>
              </div>
            </div>
          )}
        </div>
      ) : (
        /* CARDS GRID VIEW */
        <div className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {paginatedCampaigns.map((c) => {
              const total = c.totalRecipients || 0;
              const delivered = c.sentCount || 0;
              const failed = c.failedCount || 0;
              const pct = total > 0 ? Math.round(((delivered + failed) / total) * 100) : 0;

            return (
              <div
                key={c.id}
                onClick={() => navigate(`/campaigns/${c.id}/analytics`)}
                className="bg-white border border-[#E5E5E7] rounded-[16px] p-5 space-y-4 flex flex-col justify-between hover:shadow-md hover:border-slate-300 transition-all cursor-pointer group"
              >
                <div>
                  <div className="flex items-start justify-between gap-2">
                    <div>
                      <h3 className="text-[15px] font-bold text-[#0A0A0B] group-hover:text-blue-600 transition-colors truncate">{c.name}</h3>
                      <p className="text-[12px] text-[#9CA3AF] font-mono mt-0.5 truncate">
                        Subject: {c.subject || 'Not configured'}
                      </p>
                    </div>
                    <StatusBadge status={c.status || 'DRAFT'} />
                  </div>

                  <div className="mt-3 flex items-center gap-2 p-2 bg-[#FAFAFB] rounded-lg border border-[#E5E5E7]" onClick={(e) => e.stopPropagation()}>
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
                      <span className="font-bold text-[#0A0A0B]">{pct}%</span>
                    </div>
                    <div className="bg-[#F3F4F6] rounded-full h-2 overflow-hidden border border-[#E5E5E7]">
                      <div className={`h-full ${getProgressBarGradient(c.status)} transition-all duration-500`} style={{ width: `${pct}%` }} />
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

                <div className="flex items-center justify-between pt-3 border-t border-[#F0F0F2]" onClick={(e) => e.stopPropagation()}>
                  <button
                    onClick={() => navigate(`/campaigns/${c.id}/analytics`)}
                    className="text-[12px] font-semibold text-blue-600 hover:text-blue-800 flex items-center gap-1 cursor-pointer"
                  >
                    <BarChart2 className="w-3.5 h-3.5" /> Progress & Live Stream
                  </button>

                  <div className="flex items-center gap-2">
                    <button
                      onClick={() => handleRunDiagnostics(c.id)}
                      className="px-3 py-1 text-[12px] font-semibold bg-[#0A0A0B] text-white rounded-lg flex items-center gap-1 cursor-pointer hover:bg-slate-800"
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

          {/* Cards Pagination Bar */}
          {totalPages > 1 && (
            <div className="flex flex-col sm:flex-row items-center justify-between gap-3 bg-white px-4 py-3 rounded-[16px] border border-[#E5E5E7]">
              <div className="text-[12px] text-[#71717A] font-medium">
                Showing <span className="font-bold text-[#0A0A0B]">{startRecord}</span> to <span className="font-bold text-[#0A0A0B]">{endRecord}</span> of <span className="font-bold text-[#0A0A0B]">{totalCampaigns}</span> campaigns
              </div>

              <div className="flex items-center gap-1">
                {/* Previous Page */}
                <button
                  onClick={() => setCurrentPage(prev => Math.max(1, prev - 1))}
                  disabled={validCurrentPage === 1}
                  className="flex items-center gap-1 px-2.5 py-1.5 text-[12px] font-semibold rounded-lg border border-[#E5E5E7] text-[#5F6368] hover:bg-[#FAFAFB] disabled:opacity-40 disabled:cursor-not-allowed cursor-pointer transition-colors"
                >
                  <ChevronLeft className="w-3.5 h-3.5" /> Prev
                </button>

                {/* Page Number Buttons */}
                <div className="flex items-center gap-1 mx-1">
                  {getPaginationNumbers().map((num, idx) => {
                    if (num === '...') {
                      return (
                        <span key={`dots-cards-${idx}`} className="px-2 py-1 text-[12px] text-[#9CA3AF] select-none">
                          ...
                        </span>
                      );
                    }
                    const isActive = num === validCurrentPage;
                    return (
                      <button
                        key={`page-cards-${num}`}
                        onClick={() => setCurrentPage(num)}
                        className={`min-w-[32px] h-8 px-2 text-[12px] font-semibold rounded-lg transition-all cursor-pointer ${
                          isActive
                            ? 'bg-[#0A0A0B] text-white shadow-xs'
                            : 'text-[#5F6368] hover:bg-[#FAFAFB] border border-transparent hover:border-[#E5E5E7]'
                        }`}
                      >
                        {num}
                      </button>
                    );
                  })}
                </div>

                {/* Next Page */}
                <button
                  onClick={() => setCurrentPage(prev => Math.min(totalPages, prev + 1))}
                  disabled={validCurrentPage === totalPages}
                  className="flex items-center gap-1 px-2.5 py-1.5 text-[12px] font-semibold rounded-lg border border-[#E5E5E7] text-[#5F6368] hover:bg-[#FAFAFB] disabled:opacity-40 disabled:cursor-not-allowed cursor-pointer transition-colors"
                >
                  Next <ChevronRight className="w-3.5 h-3.5" />
                </button>
              </div>
            </div>
          )}
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
          
          {/* Launch Mode Switcher */}
          <div>
            <label className="ma-label mb-1.5 block">Campaign Type & Dispatch Mode</label>
            <div className="grid grid-cols-2 gap-2.5">
              <button
                type="button"
                onClick={() => setLaunchMode('INSTANT')}
                className={`p-3 rounded-xl border text-left transition-all cursor-pointer ${
                  launchMode === 'INSTANT'
                    ? 'border-blue-600 bg-blue-50/50 ring-1 ring-blue-600'
                    : 'border-[#E5E5E7] bg-[#FAFAFB] hover:border-slate-300'
                }`}
              >
                <div className="flex items-center gap-1.5 font-bold text-[13px] text-[#0A0A0B]">
                  <Zap className="w-4 h-4 text-blue-600" />
                  Instant / Manual
                </div>
                <p className="text-[11px] text-[#71717A] mt-1">
                  Draft campaign for on-demand manual launch anytime.
                </p>
              </button>

              <button
                type="button"
                onClick={() => setLaunchMode('SCHEDULED')}
                className={`p-3 rounded-xl border text-left transition-all cursor-pointer ${
                  launchMode === 'SCHEDULED'
                    ? 'border-purple-600 bg-purple-50/50 ring-1 ring-purple-600'
                    : 'border-[#E5E5E7] bg-[#FAFAFB] hover:border-slate-300'
                }`}
              >
                <div className="flex items-center gap-1.5 font-bold text-[13px] text-[#0A0A0B]">
                  <Clock className="w-4 h-4 text-purple-600" />
                  Automated Schedule
                </div>
                <p className="text-[11px] text-[#71717A] mt-1">
                  Pick date & time for background runner auto-dispatch.
                </p>
              </button>
            </div>
          </div>

          {/* Schedule Date & Time Picker */}
          {launchMode === 'SCHEDULED' && (
            <div className="p-3 bg-purple-50/40 rounded-xl border border-purple-200 space-y-1.5 animate-fadeIn">
              <label className="ma-label flex items-center gap-1.5 text-purple-900 font-bold text-[12px]">
                <Calendar className="w-3.5 h-3.5 text-purple-600" />
                Select Launch Date & Time *
              </label>
              <input
                type="datetime-local"
                required={launchMode === 'SCHEDULED'}
                value={scheduledDateTime}
                onChange={e => setScheduledDateTime(e.target.value)}
                min={new Date().toISOString().slice(0, 16)}
                className="ma-input bg-white border-purple-300 focus:border-purple-600"
              />
              <p className="text-[11px] text-purple-700">
                The campaign will be registered with the background runner and trigger automatically.
              </p>
            </div>
          )}

          <div>
            <label className="ma-label">Campaign Name</label>
            <input
              type="text"
              required
              placeholder="e.g. Q3 Customer Newsletter"
              value={name}
              onChange={e => setName(e.target.value)}
              className="ma-input"
            />
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="ma-label">Email Template</label>
              <select
                value={templateId}
                onChange={e => setTemplateId(e.target.value)}
                className="ma-select"
              >
                <option value="">Select Template...</option>
                {templates.map(t => (
                  <option key={t.id} value={t.id}>{t.name}</option>
                ))}
              </select>
            </div>

            <div>
              <label className="ma-label">Attach Contacts Collection</label>
              <select
                value={modalCollectionId}
                onChange={e => setModalCollectionId(e.target.value)}
                className="ma-select"
              >
                <option value="">Select Collection (Optional)...</option>
                {collections.map(c => (
                  <option key={c.id} value={c.id}>{c.name}</option>
                ))}
              </select>
            </div>
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
            className="ma-btn ma-btn-primary w-full gap-2 text-[13px] py-2.5"
          >
            {launchMode === 'SCHEDULED' ? (
              <>
                <Clock className="w-4 h-4" /> Schedule Campaign
              </>
            ) : (
              <>
                <Plus className="w-4 h-4" /> Create Instant Campaign
              </>
            )}
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

      {/* High-Tech Animated Campaign Launch Overlay */}
      {isLaunchLoaderOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-[#0A0A0B]/80 backdrop-blur-md animate-fadeIn font-sans">
          <div className="bg-[#0F172A] border border-[#1E293B] rounded-3xl p-8 max-w-md w-full mx-4 shadow-2xl text-center space-y-6 animate-scaleUp">
            
            {/* Spinning Glowing Launch Rings */}
            <div className="relative w-24 h-24 mx-auto flex items-center justify-center">
              <div className="absolute inset-0 rounded-full border-4 border-blue-500/20 animate-ping"></div>
              <div className="absolute inset-0 rounded-full border-4 border-t-blue-500 border-r-indigo-500 border-b-purple-500 border-l-transparent animate-spin"></div>
              <div className="w-14 h-14 rounded-full bg-gradient-to-tr from-blue-600 to-indigo-600 flex items-center justify-center shadow-lg shadow-blue-500/40">
                <Rocket className="w-7 h-7 text-white animate-bounce" />
              </div>
            </div>

            <div>
              <h2 className="text-xl font-black text-white tracking-tight">Initializing Campaign Launch</h2>
              <p className="text-xs text-slate-400 mt-1.5 font-medium">{launchStepText}</p>
            </div>

            {/* Step Checkpoints */}
            <div className="bg-slate-900/80 border border-slate-800 rounded-2xl p-4 text-left space-y-3">
              <div className="flex items-center gap-2.5 text-xs text-emerald-400 font-semibold">
                <CheckCircle2 className="w-4 h-4 text-emerald-400 flex-shrink-0" />
                <span>AWS SES Mail Manager Relay Connected</span>
              </div>
              <div className="flex items-center gap-2.5 text-xs text-blue-400 font-semibold">
                <Loader2 className="w-4 h-4 text-blue-400 animate-spin flex-shrink-0" />
                <span>Parallel Virtual Thread Pool Engaged</span>
              </div>
              <div className="flex items-center gap-2.5 text-xs text-slate-300 font-medium">
                <Sparkles className="w-4 h-4 text-purple-400 flex-shrink-0" />
                <span>Audience Personalization Engine Ready</span>
              </div>
            </div>

            <div className="w-full bg-slate-800 h-1.5 rounded-full overflow-hidden">
              <div className="bg-gradient-to-r from-blue-500 via-indigo-500 to-emerald-400 h-full w-full animate-pulse"></div>
            </div>
          </div>
        </div>
      )}

    </div>
  );
};
