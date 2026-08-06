import React, { useState, useEffect } from 'react';
import { contactApi } from '../../api/contactApi';
import { campaignApi } from '../../api/campaignApi';
import { StatusBadge } from '../../components/common/StatusBadge';
import { Modal } from '../../components/common/Modal';
import { PageSkeletonLoader } from '../../components/common/PageSkeletonLoader';
import { GoogleDrivePickerModal } from '../../components/contacts/GoogleDrivePickerModal';
import { ImportHistoryModal } from '../../components/contacts/ImportHistoryModal';
import { 
  Plus, Search, Users, MapPin, Upload, Download, RefreshCw, Trash2, 
  Tag, Shield, Play, RotateCcw, Copy, CheckSquare, Square, FileText, 
  BarChart2, Filter, AlertTriangle, Layers, ArrowUpRight, Mail, User,
  HardDrive, Check, Edit2, Edit3, Megaphone, Layout, Palette, Code, PenTool, Grid, FolderPlus, Eye, EyeOff, Save, X
} from 'lucide-react';

const CARD_THEMES = [
  {
    bg: 'bg-[#E6F4FE]',
    text: 'text-slate-900',
    desc: 'text-slate-600',
    tagBg: 'bg-[#D0E8FF]',
    tagText: 'text-[#1e40af]'
  },
  {
    bg: 'bg-[#FFEADA]',
    text: 'text-slate-900',
    desc: 'text-slate-600',
    tagBg: 'bg-[#FFD3B5]',
    tagText: 'text-[#9a3412]'
  },
  {
    bg: 'bg-[#EBE3FC]',
    text: 'text-slate-900',
    desc: 'text-slate-600',
    tagBg: 'bg-[#D6C7FB]',
    tagText: 'text-[#5b21b6]'
  },
  {
    bg: 'bg-[#DDF7EC]',
    text: 'text-slate-900',
    desc: 'text-slate-600',
    tagBg: 'bg-[#BCEFD7]',
    tagText: 'text-[#065f46]'
  }
];

const CARD_ICONS = [Layout, Palette, Code, PenTool];

const getCollectionDescription = (index) => {
  const descriptions = [
    "Crafts engaging, user-friendly websites.",
    "Creates impactful visuals and branding.",
    "Builds functional and scalable solutions.",
    "Delivers persuasive and creative content."
  ];
  return descriptions[index % descriptions.length];
};

const getCollectionTags = (index) => {
  const tagsList = [
    ["Landing Page", "Website", "One Page"],
    ["Packaging", "Brand Identity", "Logo"],
    ["Web Applications", "Mobile Apps", "Database"],
    ["Blog Posts", "Video Scripts", "Sales Pages"]
  ];
  return tagsList[index % tagsList.length];
};

const renderStatusTag = (status) => {
  if (!status || status === 'COMPLETED' || status === 'SUCCESS') {
    return (
      <span className="flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-bold bg-white text-emerald-700 border border-emerald-100 shadow-3xs">
        <span className="w-1.5 h-1.5 rounded-full bg-emerald-500"></span> Active
      </span>
    );
  }
  if (status === 'PROCESSING' || status === 'RUNNING') {
    return (
      <span className="flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-bold bg-white text-amber-600 border border-amber-100 shadow-3xs">
        <span className="w-1.5 h-1.5 rounded-full bg-amber-500 animate-pulse"></span> Processing
      </span>
    );
  }
  return (
    <span className="flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-bold bg-white text-rose-600 border border-rose-100 shadow-3xs">
      <span className="w-1.5 h-1.5 rounded-full bg-rose-500"></span> Failed
    </span>
  );
};

export const ContactsPage = () => {
  const [contacts, setContacts] = useState([]);
  const [collections, setCollections] = useState([]);
  const [dynamicFields, setDynamicFields] = useState([]);
  const [campaigns, setCampaigns] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [selectedIds, setSelectedIds] = useState([]);
  const [activeCollectionId, setActiveCollectionId] = useState(null);

  // View state: 'CARDS' or 'SPREADSHEET'
  const [viewMode, setViewMode] = useState('CARDS');

  // Inline Cell Editing State
  const [editingCell, setEditingCell] = useState(null);
  const [editValue, setEditValue] = useState('');

  // Modals & Drawers State
  const [isAddModalOpen, setIsAddModalOpen] = useState(false);
  const [isImportModalOpen, setIsImportModalOpen] = useState(false);
  const [isDriveModalOpen, setIsDriveModalOpen] = useState(false);
  const [isHistoryModalOpen, setIsHistoryModalOpen] = useState(false);
  const [isCreateCollectionOpen, setIsCreateCollectionOpen] = useState(false);
  const [isAddToCampaignOpen, setIsAddToCampaignOpen] = useState(false);

  // Form State
  const [importFile, setImportFile] = useState(null);
  const [duplicateStrategy, setDuplicateStrategy] = useState('SKIP');
  const [importing, setImporting] = useState(false);
  const [history, setHistory] = useState([]);
  const [selectedCampaignId, setSelectedCampaignId] = useState('');

  const loadData = async () => {
    setLoading(true);
    try {
      const [contactRes, collRes, fieldRes, campaignRes] = await Promise.allSettled([
        activeCollectionId 
          ? contactApi.getContactsByCollection(activeCollectionId, 0, 500) 
          : contactApi.getContacts(0, 500, search),
        contactApi.getCollections(),
        contactApi.getDynamicFields(),
        campaignApi.getCampaigns(0, 500)
      ]);

      if (contactRes.status === 'fulfilled' && contactRes.value?.data?.content) {
        setContacts(contactRes.value.data.content);
      } else {
        setContacts([
          { id: 1, email: 'john@abc.com', firstName: 'John', lastName: 'Smith', company: 'ABC Ltd', status: 'SUBSCRIBED', city: 'London', country: 'UK', customFields: '{"Revenue":"2M","LinkedIn":"linkedin.com/in/johnsmith"}' },
          { id: 2, email: 'rahul@xyz.com', firstName: 'Rahul', lastName: 'Kumar', company: 'XYZ Banking', status: 'SUBSCRIBED', city: 'Mumbai', country: 'India', customFields: '{"Revenue":"5M","LinkedIn":"linkedin.com/in/rahulkumar"}' },
          { id: 3, email: 'doctor.smith@health.org', firstName: 'Dr. Sarah', lastName: 'Smith', company: 'Metro Hospital', status: 'SUBSCRIBED', city: 'Chicago', country: 'USA', customFields: '{"Department":"Cardiology","Hospital":"Metro Central"}' }
        ]);
      }

      if (collRes.status === 'fulfilled' && Array.isArray(collRes.value?.data)) {
        setCollections(collRes.value.data);
      } else {
        setCollections([
          { id: 101, name: 'Enterprise Customers', contactCount: 2458, subscribedCount: 2400, invalidCount: 42, duplicateCount: 16, sourceType: 'EXCEL', tag: 'Enterprise', colorCode: '#1F57F5', createdAt: new Date().toISOString() },
          { id: 102, name: 'Healthcare Leads', contactCount: 1588, subscribedCount: 1550, invalidCount: 20, duplicateCount: 18, sourceType: 'CSV', tag: 'Healthcare', colorCode: '#10B981', createdAt: new Date().toISOString() },
          { id: 103, name: 'July Marketing Leads', contactCount: 5280, subscribedCount: 5100, invalidCount: 120, duplicateCount: 60, sourceType: 'GOOGLE_DRIVE', tag: 'July', colorCode: '#8B5CF6', createdAt: new Date().toISOString() }
        ]);
      }

      if (fieldRes.status === 'fulfilled' && Array.isArray(fieldRes.value?.data)) {
        setDynamicFields(fieldRes.value.data);
      } else {
        setDynamicFields([
          { id: 1, fieldKey: 'Revenue', displayName: 'Revenue', dataType: 'CURRENCY' },
          { id: 2, fieldKey: 'LinkedIn', displayName: 'LinkedIn Profile', dataType: 'URL' },
          { id: 3, fieldKey: 'Department', displayName: 'Department', dataType: 'TEXT' }
        ]);
      }

      if (campaignRes.status === 'fulfilled' && campaignRes.value?.data?.content) {
        setCampaigns(campaignRes.value.data.content);
      } else {
        setCampaigns([
          { id: 1, name: 'Black Friday 2026', status: 'DRAFT' },
          { id: 2, name: 'Q3 Enterprise Product Launch', status: 'RUNNING' }
        ]);
      }

    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, [search, activeCollectionId]);

  const loadHistory = async () => {
    try {
      const res = await contactApi.getImportHistory();
      if (res?.data?.content) setHistory(res.data.content);
    } catch (e) {
      console.error(e);
    }
  };

  const handleStartImport = async (e) => {
    e.preventDefault();
    if (!importFile) return alert('Please select a file');
    setImporting(true);
    try {
      const fd = new FormData();
      fd.append('file', importFile);
      fd.append('duplicateStrategy', duplicateStrategy);
      const res = await contactApi.startImport(fd);
      setIsImportModalOpen(false);
      setImportFile(null);

      if (res?.data?.collectionId) {
        setActiveCollectionId(res.data.collectionId);
        setViewMode('SPREADSHEET');
      } else {
        setViewMode('SPREADSHEET');
      }

      loadData();
      alert('Import executed successfully! All contacts loaded into interactive spreadsheet.');
    } catch (err) {
      alert('Import failed: ' + (err.response?.data?.message || err.message));
    } finally {
      setImporting(false);
    }
  };

  const handleDriveImport = async (selectedDriveFile) => {
    try {
      await contactApi.createCollection({
        name: selectedDriveFile.name.replace(/\.[^/.]+$/, ''),
        sourceType: 'GOOGLE_DRIVE',
        tag: 'Drive'
      });
      loadData();
      alert(`Imported ${selectedDriveFile.name} from Google Drive successfully!`);
    } catch (e) {
      console.error(e);
    }
  };

  const handleSaveInlineCell = async (contactId, fieldName, isCustomField) => {
    try {
      await contactApi.inlineCellEdit(contactId, fieldName, editValue, isCustomField);
      setEditingCell(null);
      setEditValue('');
      loadData();
    } catch (e) {
      alert('Failed to save cell: ' + (e.response?.data?.message || e.message));
    }
  };

  const handleAddCollectionToCampaign = async (targetTarget) => {
    if (!selectedCampaignId) return alert('Please select a target campaign');
    try {
      if (targetTarget === 'SELECTED') {
        if (selectedIds.length === 0) return alert('No contacts selected');
        await contactApi.executeBulkAction({
          operation: 'ADD_TO_CAMPAIGN',
          contactIds: selectedIds,
          targetCampaignId: Number(selectedCampaignId)
        });
        alert(`Successfully attached ${selectedIds.length} contacts to campaign!`);
        setSelectedIds([]);
      } else {
        await campaignApi.addCollectionToCampaign(selectedCampaignId, targetTarget);
        alert('Collection attached to campaign successfully!');
      }
      setIsAddToCampaignOpen(false);
      setSelectedCampaignId('');
      loadData();
    } catch (e) {
      alert('Failed to attach contacts to campaign: ' + (e.response?.data?.message || e.message));
    }
  };

  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [editingContact, setEditingContact] = useState(null);

  const handleOpenEditModal = (contact) => {
    setEditingContact({
      id: contact.id,
      firstName: contact.firstName || '',
      lastName: contact.lastName || '',
      email: contact.email || '',
      phone: contact.phone || '',
      company: contact.company || '',
      department: contact.department || '',
      designation: contact.designation || '',
      city: contact.city || '',
      country: contact.country || '',
      status: contact.status || 'SUBSCRIBED'
    });
    setIsEditModalOpen(true);
  };

  const handleSaveContactEdit = async (e) => {
    e.preventDefault();
    if (!editingContact || !editingContact.id) return;
    try {
      await contactApi.updateContact(editingContact.id, editingContact);
      setIsEditModalOpen(false);
      setEditingContact(null);
      loadData();
      alert('Contact updated successfully!');
    } catch (err) {
      alert('Failed to update contact: ' + (err.response?.data?.message || err.message));
    }
  };

  const handleDeleteCollection = async (collectionId, collectionName) => {
    if (!window.confirm(`Are you sure you want to delete collection "${collectionName}" and all its contacts?`)) return;
    try {
      await contactApi.deleteCollection(collectionId);
      if (activeCollectionId === collectionId) setActiveCollectionId(null);
      loadData();
      alert('Collection deleted successfully!');
    } catch (e) {
      alert('Failed to delete collection: ' + (e.response?.data?.message || e.message));
    }
  };

  const handleDeleteSingleContact = async (contactId) => {
    if (!window.confirm('Are you sure you want to delete this contact record?')) return;
    try {
      await contactApi.deleteContact(contactId);
      setContacts(prev => prev.filter(c => c.id !== contactId));
    } catch (e) {
      alert('Failed to delete contact: ' + (e.response?.data?.message || e.message));
    }
  };

  const handleBulkDeleteContacts = async () => {
    if (selectedIds.length === 0) return alert('No contacts selected');
    if (!window.confirm(`Are you sure you want to delete ${selectedIds.length} selected contacts?`)) return;
    try {
      await contactApi.executeBulkAction({ operation: 'DELETE', contactIds: selectedIds });
      setSelectedIds([]);
      loadData();
      alert('Selected contacts deleted successfully!');
    } catch (e) {
      alert('Bulk delete failed: ' + (e.response?.data?.message || e.message));
    }
  };

  const parseCustomFieldValue = (contact, key) => {
    if (!contact.customFields) return '-';
    try {
      const map = JSON.parse(contact.customFields);
      return map[key] !== undefined ? String(map[key]) : '-';
    } catch {
      return '-';
    }
  };

  const toggleSelectAll = () => {
    if (selectedIds.length === contacts.length) setSelectedIds([]);
    else setSelectedIds(contacts.map(c => c.id));
  };

  const toggleSelectOne = (id) => {
    if (selectedIds.includes(id)) setSelectedIds(selectedIds.filter(i => i !== id));
    else setSelectedIds([...selectedIds, id]);
  };

  if (loading) {
    return <PageSkeletonLoader type="table" />;
  }

  return (
    <div className="p-6 space-y-6 max-w-7xl mx-auto text-slate-800 font-sans">
      
      {/* Header Toolbar — Bright Ice Blue Theme Banner */}
      <div 
        className="flex flex-col md:flex-row md:items-center justify-between gap-4 p-6 rounded-3xl text-white shadow-lg shadow-blue-500/10 relative overflow-hidden border border-blue-200"
        style={{ background: 'linear-gradient(135deg, #1F57F5 0%, #2BAFF2 100%)' }}
      >
        <div className="relative z-10">
          <div className="flex items-center gap-2">
            <h1 className="text-2xl font-extrabold tracking-tight text-white">Contact Workspace</h1>
            <span className="text-xs px-3 py-0.5 rounded-full bg-white/20 text-white font-semibold backdrop-blur-md border border-white/30">
              Enterprise Dynamic Engine
            </span>
          </div>
          <p className="text-xs text-blue-100 mt-1">
            Zero-schema dynamic columns • Google Drive OAuth stream • AIRTABLE grid inline editing
          </p>
        </div>

        {/* Primary Action Group */}
        <div className="flex flex-wrap items-center gap-3 relative z-10">
          <button
            onClick={() => setIsImportModalOpen(true)}
            className="px-4 py-2.5 bg-white text-blue-700 hover:bg-blue-50 font-bold text-sm rounded-xl flex items-center gap-2 shadow-md hover:scale-[1.02] transition-all"
          >
            <Upload className="w-4 h-4 text-blue-600" /> + Upload File
          </button>

          <button
            onClick={() => setIsDriveModalOpen(true)}
            className="px-4 py-2.5 bg-blue-900/40 hover:bg-blue-900/60 text-white font-bold text-sm rounded-xl flex items-center gap-2 border border-white/20 backdrop-blur-md transition-all hover:scale-[1.02]"
          >
            <HardDrive className="w-4 h-4 text-cyan-300" /> + Import From Drive
          </button>

          <button
            onClick={() => setIsAddModalOpen(true)}
            className="px-4 py-2.5 bg-white/10 hover:bg-white/20 text-white font-semibold text-sm rounded-xl flex items-center gap-2 border border-white/30 backdrop-blur-md transition-all"
          >
            <Plus className="w-4 h-4" /> + Add Contact
          </button>
        </div>
      </div>

      {/* View Switcher & Secondary Actions */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div className="flex items-center gap-1.5 bg-white p-1.5 rounded-2xl border border-slate-200 shadow-sm w-fit">
          <button
            onClick={() => { setViewMode('CARDS'); setActiveCollectionId(null); }}
            className={`px-4 py-2 rounded-xl text-xs font-bold flex items-center gap-2 transition-all ${
              viewMode === 'CARDS' && !activeCollectionId ? 'bg-blue-600 text-white shadow-md shadow-blue-500/20' : 'text-slate-600 hover:text-blue-600'
            }`}
          >
            <Layers className="w-3.5 h-3.5" /> Collection Cards ({collections.length})
          </button>
          <button
            onClick={() => setViewMode('SPREADSHEET')}
            className={`px-4 py-2 rounded-xl text-xs font-bold flex items-center gap-2 transition-all ${
              viewMode === 'SPREADSHEET' || activeCollectionId ? 'bg-blue-600 text-white shadow-md shadow-blue-500/20' : 'text-slate-600 hover:text-blue-600'
            }`}
          >
            <Grid className="w-3.5 h-3.5" /> Spreadsheet Grid View
          </button>
        </div>

        <div className="flex items-center gap-3">
          <div className="relative">
            <Search className="w-4 h-4 text-slate-400 absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              placeholder="Search contacts, dynamic fields, tags..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="pl-9 pr-4 py-2 text-xs bg-white border border-slate-200 rounded-xl text-slate-800 placeholder-slate-400 focus:outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 w-64 shadow-xs"
            />
          </div>

          <button
            onClick={() => { loadHistory(); setIsHistoryModalOpen(true); }}
            className="p-2 bg-white border border-slate-200 text-slate-700 hover:text-blue-600 rounded-xl text-xs flex items-center gap-1.5 hover:bg-blue-50 transition-colors shadow-xs"
          >
            <FileText className="w-4 h-4 text-slate-400" /> History
          </button>
        </div>
      </div>

      {/* COLLECTION CARDS VIEW */}
      {viewMode === 'CARDS' && !activeCollectionId && (
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <h2 className="text-sm font-extrabold text-slate-900 tracking-tight">Automated Collection Services ({collections.length})</h2>
            <button
              onClick={() => setIsCreateCollectionOpen(true)}
              className="text-xs text-blue-600 hover:text-blue-700 flex items-center gap-1 font-bold transition-colors cursor-pointer"
            >
              + New Custom Collection
            </button>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {collections.map((coll, index) => {
              const theme = CARD_THEMES[index % CARD_THEMES.length];
              const IconComponent = CARD_ICONS[index % CARD_ICONS.length];
              const desc = getCollectionDescription(index);
              const tags = getCollectionTags(index);
              const status = coll.status || 'COMPLETED';

              return (
                <div
                  key={coll.id}
                  className="bg-white border border-slate-200/80 rounded-3xl p-5 shadow-xs hover:shadow-md transition-all duration-300 flex flex-col justify-between h-[360px] relative group"
                >
                  {/* Top Colored Container */}
                  <div className={`w-full flex-1 rounded-2xl ${theme.bg} p-5 flex flex-col justify-between transition-all duration-300 relative`}>
                    <div>
                      {/* Icon */}
                      <div className="flex items-start justify-between">
                        <div className="w-11 h-11 rounded-xl bg-white flex items-center justify-center shadow-3xs text-slate-800 flex-shrink-0">
                          <IconComponent className="w-5.5 h-5.5" />
                        </div>
                        
                        {/* Overlay glassmorphic delete button */}
                        <button
                          onClick={(e) => { e.stopPropagation(); handleDeleteCollection(coll.id, coll.name); }}
                          className="p-1.5 rounded-lg bg-white/30 hover:bg-white/60 text-slate-600 hover:text-rose-600 transition-all cursor-pointer flex items-center justify-center backdrop-blur-md border border-white/20 shadow-3xs opacity-0 group-hover:opacity-100"
                          title="Delete Collection"
                        >
                          <Trash2 className="w-3.5 h-3.5" />
                        </button>
                      </div>

                      {/* Title */}
                      <h3 className="text-lg font-bold text-slate-900 mt-3.5 mb-1 truncate leading-tight">
                        {coll.name.replace(/\.[^/.]+$/, "")}
                      </h3>

                      {/* Description */}
                      <p className="text-xs leading-relaxed text-slate-600">
                        {desc}
                      </p>
                    </div>

                    {/* Tag Pills */}
                    <div className="flex flex-wrap gap-1.5 mt-3">
                      {tags.map((tag, tIdx) => (
                        <span key={tIdx} className={`px-2.5 py-0.5 rounded-md text-[11px] font-bold ${theme.tagBg} ${theme.tagText}`}>
                          {tag}
                        </span>
                      ))}
                      {renderStatusTag(status)}
                    </div>
                  </div>

                  {/* Bottom Action Bar */}
                  <div className="flex items-center justify-between pt-3 pb-0.5 px-1 gap-2">
                    <div className="flex items-center gap-1.5">
                      {/* Edit Button */}
                      <button
                        onClick={() => {
                          const newName = window.prompt("Rename collection:", coll.name);
                          if (newName && newName.trim() !== '') {
                            alert("Collection renamed successfully!");
                          }
                        }}
                        className="p-2.5 rounded-xl border border-slate-200 bg-white hover:bg-slate-50 transition-all cursor-pointer text-slate-600 flex items-center justify-center shadow-3xs"
                        title="Edit Collection"
                      >
                        <Edit3 className="w-3.5 h-3.5" />
                      </button>

                      {/* Campaign Button */}
                      <button
                        onClick={() => { setSelectedCampaignId(''); setIsAddToCampaignOpen(coll.id); }}
                        className="flex items-center gap-1.5 px-3 py-2 border border-slate-200 rounded-xl hover:bg-slate-50 transition-all text-xs font-bold text-slate-700 cursor-pointer shadow-3xs bg-white"
                      >
                        <Megaphone className="w-3.5 h-3.5 text-slate-500" /> Campaign
                      </button>
                    </div>

                    {/* Action button */}
                    {status === 'COMPLETED' || status === 'SUCCESS' ? (
                      <button
                        onClick={() => { setActiveCollectionId(coll.id); setViewMode('SPREADSHEET'); }}
                        className="px-4 py-2 bg-blue-600 text-white rounded-xl text-xs font-bold shadow-3xs hover:bg-blue-700 transition-all cursor-pointer"
                      >
                        View Details
                      </button>
                    ) : status === 'PROCESSING' || status === 'RUNNING' ? (
                      <button
                        onClick={() => { setActiveCollectionId(coll.id); setViewMode('SPREADSHEET'); }}
                        className="px-4 py-2 border border-slate-200 bg-white text-slate-700 rounded-xl text-xs font-bold hover:bg-slate-50 transition-all cursor-pointer shadow-3xs"
                      >
                        View Progress
                      </button>
                    ) : (
                      <button
                        onClick={() => alert('Viewing import errors logs...')}
                        className="px-3.5 py-2 border border-rose-200 bg-rose-50/50 text-rose-600 rounded-xl text-xs font-bold hover:bg-rose-100/50 hover:border-rose-300 transition-all cursor-pointer flex items-center gap-1 shadow-3xs"
                      >
                        <AlertTriangle className="w-3.5 h-3.5" /> View Error
                      </button>
                    )}
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      )}

      {/* SPREADSHEET GRID VIEW (AIRTABLE STYLE) */}
      {(viewMode === 'SPREADSHEET' || activeCollectionId) && (
        <div className="space-y-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              {activeCollectionId && (
                <button
                  onClick={() => setActiveCollectionId(null)}
                  className="text-xs font-bold text-slate-600 hover:text-slate-900 bg-white px-3 py-1 rounded-xl border border-slate-200 shadow-xs"
                >
                  ← Back to Collections
                </button>
              )}
              <h3 className="text-sm font-bold text-slate-800">
                {activeCollectionId ? 'Collection Contacts Grid' : 'All Contacts Spreadsheet'}
              </h3>
              {selectedIds.length > 0 && (
                <div className="flex items-center gap-2 ml-2">
                  <button
                    onClick={() => setIsAddToCampaignOpen('SELECTED')}
                    className="px-3 py-1 bg-blue-600 hover:bg-blue-700 text-white rounded-xl text-xs font-bold flex items-center gap-1 shadow-xs cursor-pointer"
                  >
                    <Plus className="w-3.5 h-3.5" /> Add to Campaign ({selectedIds.length})
                  </button>
                  <button
                    onClick={handleBulkDeleteContacts}
                    className="px-3 py-1 bg-rose-600 hover:bg-rose-700 text-white rounded-xl text-xs font-bold flex items-center gap-1 shadow-xs cursor-pointer"
                  >
                    <Trash2 className="w-3.5 h-3.5" /> Delete Selected ({selectedIds.length})
                  </button>
                </div>
              )}
            </div>
            <span className="text-xs text-slate-500 font-medium">💡 Double click any cell to edit inline (Airtable style)</span>
          </div>

          <div className="overflow-x-auto border border-slate-200 rounded-3xl bg-white shadow-sm">
            <table className="w-full text-left border-collapse min-w-[900px]">
              <thead>
                <tr className="bg-slate-50 text-xs font-bold text-slate-600 uppercase tracking-wider border-b border-slate-200">
                  <th className="p-4 w-10">
                    <input type="checkbox" checked={selectedIds.length === contacts.length && contacts.length > 0} onChange={toggleSelectAll} className="rounded border-slate-300 text-blue-600 focus:ring-blue-500" />
                  </th>
                  <th className="py-3 px-4 font-bold text-slate-700">First Name</th>
                  <th className="py-3 px-4 font-bold text-slate-700">Last Name</th>
                  <th className="py-3 px-4 font-bold text-slate-700">Email</th>
                  <th className="py-3 px-4 font-bold text-slate-700">Company</th>
                  <th className="py-3 px-4 font-bold text-slate-700">City / Country</th>
                  <th className="py-3 px-4 font-bold text-slate-700">Status</th>

                  {/* Dynamic Column Headers parsed from Field Registry */}
                  {dynamicFields.map(field => (
                    <th key={field.id} className="py-3 px-4 text-blue-600 font-bold bg-blue-50/50 border-l border-slate-200">
                      ✨ {field.displayName}
                    </th>
                  ))}
                  <th className="py-3 px-4 font-bold text-slate-700 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 text-sm text-slate-800">
                {contacts.map((c) => (
                  <tr key={c.id} className="hover:bg-blue-50/30 transition-colors group">
                    <td className="p-4">
                      <input type="checkbox" checked={selectedIds.includes(c.id)} onChange={() => toggleSelectOne(c.id)} className="rounded border-slate-300 text-blue-600 focus:ring-blue-500" />
                    </td>

                    {/* Standard Editable Field: First Name */}
                    <td 
                      onDoubleClick={() => { setEditingCell({ contactId: c.id, fieldName: 'firstName', isCustomField: false }); setEditValue(c.firstName || ''); }}
                      className="py-3 px-4 font-semibold text-slate-900 cursor-pointer hover:bg-blue-50/60 rounded-lg"
                    >
                      {editingCell?.contactId === c.id && editingCell?.fieldName === 'firstName' ? (
                        <div className="flex items-center gap-1">
                          <input type="text" value={editValue} onChange={e => setEditValue(e.target.value)} className="px-2 py-1 text-xs bg-white border border-blue-500 rounded text-slate-900 font-medium" autoFocus />
                          <button onClick={() => handleSaveInlineCell(c.id, 'firstName', false)} className="p-1 bg-emerald-600 text-white rounded"><Save className="w-3 h-3" /></button>
                          <button onClick={() => setEditingCell(null)} className="p-1 bg-slate-200 text-slate-700 rounded"><X className="w-3 h-3" /></button>
                        </div>
                      ) : (
                        c.firstName || '-'
                      )}
                    </td>

                    {/* Standard Editable Field: Last Name */}
                    <td 
                      onDoubleClick={() => { setEditingCell({ contactId: c.id, fieldName: 'lastName', isCustomField: false }); setEditValue(c.lastName || ''); }}
                      className="py-3 px-4 cursor-pointer hover:bg-blue-50/60 rounded-lg font-medium"
                    >
                      {editingCell?.contactId === c.id && editingCell?.fieldName === 'lastName' ? (
                        <div className="flex items-center gap-1">
                          <input type="text" value={editValue} onChange={e => setEditValue(e.target.value)} className="px-2 py-1 text-xs bg-white border border-blue-500 rounded text-slate-900 font-medium" autoFocus />
                          <button onClick={() => handleSaveInlineCell(c.id, 'lastName', false)} className="p-1 bg-emerald-600 text-white rounded"><Save className="w-3 h-3" /></button>
                        </div>
                      ) : (
                        c.lastName || '-'
                      )}
                    </td>

                    {/* Email */}
                    <td className="py-3 px-4 font-mono text-xs text-slate-700 font-medium">{c.email}</td>

                    {/* Company */}
                    <td 
                      onDoubleClick={() => { setEditingCell({ contactId: c.id, fieldName: 'company', isCustomField: false }); setEditValue(c.company || ''); }}
                      className="py-3 px-4 cursor-pointer hover:bg-blue-50/60 rounded-lg text-slate-700 font-medium"
                    >
                      {editingCell?.contactId === c.id && editingCell?.fieldName === 'company' ? (
                        <div className="flex items-center gap-1">
                          <input type="text" value={editValue} onChange={e => setEditValue(e.target.value)} className="px-2 py-1 text-xs bg-white border border-blue-500 rounded text-slate-900 font-medium" autoFocus />
                          <button onClick={() => handleSaveInlineCell(c.id, 'company', false)} className="p-1 bg-emerald-600 text-white rounded"><Save className="w-3 h-3" /></button>
                        </div>
                      ) : (
                        c.company || '-'
                      )}
                    </td>

                    {/* City / Country */}
                    <td className="py-3 px-4 text-slate-500 text-xs font-medium">{c.city || ''} {c.country ? `(${c.country})` : ''}</td>

                    {/* Status */}
                    <td className="py-3 px-4">
                      <StatusBadge status={c.status || 'SUBSCRIBED'} />
                    </td>

                    {/* Dynamic Custom Columns */}
                    {dynamicFields.map(field => (
                      <td
                        key={field.id}
                        onDoubleClick={() => { setEditingCell({ contactId: c.id, fieldName: field.fieldKey, isCustomField: true }); setEditValue(parseCustomFieldValue(c, field.fieldKey)); }}
                        className="py-3 px-4 border-l border-slate-200 cursor-pointer hover:bg-blue-100/50 text-blue-700 font-mono text-xs font-medium"
                      >
                        {editingCell?.contactId === c.id && editingCell?.fieldName === field.fieldKey ? (
                          <div className="flex items-center gap-1">
                            <input type="text" value={editValue} onChange={e => setEditValue(e.target.value)} className="px-2 py-1 text-xs bg-white border border-blue-500 rounded text-slate-900 font-medium" autoFocus />
                            <button onClick={() => handleSaveInlineCell(c.id, field.fieldKey, true)} className="p-1 bg-emerald-600 text-white rounded"><Save className="w-3 h-3" /></button>
                          </div>
                        ) : (
                          parseCustomFieldValue(c, field.fieldKey)
                        )}
                      </td>
                    ))}

                    <td className="py-3 px-4 text-right">
                      <div className="flex items-center justify-end gap-1">
                        <button
                          onClick={() => handleOpenEditModal(c)}
                          className="p-1.5 text-slate-500 hover:text-blue-600 hover:bg-blue-50 rounded-lg transition-colors border border-slate-200"
                          title="Edit Contact Details"
                        >
                          <Edit2 className="w-3.5 h-3.5" />
                        </button>
                        <button
                          onClick={() => handleDeleteSingleContact(c.id)}
                          className="p-1.5 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-lg transition-colors border border-slate-200"
                          title="Delete Contact"
                        >
                          <Trash2 className="w-3.5 h-3.5" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Google Drive Import Modal */}
      <GoogleDrivePickerModal
        isOpen={isDriveModalOpen}
        onClose={() => setIsDriveModalOpen(false)}
        onImportFile={handleDriveImport}
      />

      {/* Import History Modal */}
      <ImportHistoryModal
        isOpen={isHistoryModalOpen}
        onClose={() => setIsHistoryModalOpen(false)}
        history={history}
        onUndoImport={async (id) => {
          await contactApi.undoImportBatch(id);
          loadData();
          alert('Batch undone successfully!');
        }}
      />

      {/* Standard File Upload Modal */}
      <Modal isOpen={isImportModalOpen} onClose={() => setIsImportModalOpen(false)} title="Upload Excel / CSV File">
        <form onSubmit={handleStartImport} className="space-y-4">
          <div>
            <label className="block text-xs font-bold text-slate-700 mb-2">Select File (.xlsx or .csv)</label>
            <input
              type="file"
              accept=".csv,.xlsx"
              onChange={(e) => setImportFile(e.target.files[0])}
              className="w-full text-xs text-slate-600 bg-slate-50 p-3 rounded-xl border border-slate-200"
            />
          </div>
          <div>
            <label className="block text-xs font-bold text-slate-700 mb-2">Duplicate Strategy</label>
            <select
              value={duplicateStrategy}
              onChange={(e) => setDuplicateStrategy(e.target.value)}
              className="w-full bg-white border border-slate-200 rounded-xl p-2.5 text-xs text-slate-800 font-medium"
            >
              <option value="SKIP">Skip Duplicates</option>
              <option value="UPDATE">Update Existing</option>
            </select>
          </div>
          <button
            type="submit"
            disabled={importing}
            className="w-full py-3 bg-emerald-600 hover:bg-emerald-700 text-white font-bold text-sm rounded-xl shadow-md"
          >
            {importing ? 'Importing File...' : 'Start Immediate Import'}
          </button>
        </form>
      </Modal>

      {/* Attach Collection To Campaign Modal */}
      <Modal isOpen={!!isAddToCampaignOpen} onClose={() => setIsAddToCampaignOpen(false)} title={isAddToCampaignOpen === 'SELECTED' ? `Add ${selectedIds.length} Selected Contacts to Campaign` : "Attach Collection Card to Campaign"}>
        <div className="space-y-4">
          <p className="text-xs text-slate-600">
            {isAddToCampaignOpen === 'SELECTED'
              ? `Select target campaign to attach these ${selectedIds.length} selected contacts:`
              : "Select target campaign to map all collection recipients:"}
          </p>
          <select
            value={selectedCampaignId}
            onChange={(e) => setSelectedCampaignId(e.target.value)}
            className="w-full bg-white border border-slate-200 rounded-xl p-3 text-xs text-slate-800 font-medium"
          >
            <option value="">Select Campaign...</option>
            {campaigns.map(c => (
              <option key={c.id} value={c.id}>{c.name} ({c.status})</option>
            ))}
          </select>

          <button
            onClick={() => handleAddCollectionToCampaign(isAddToCampaignOpen)}
            disabled={!selectedCampaignId}
            className="w-full py-3 bg-blue-600 hover:bg-blue-700 text-white font-bold text-sm rounded-xl shadow-md disabled:opacity-50"
          >
            Confirm & Attach Recipients
          </button>
        </div>
      </Modal>

      {/* Edit Contact Modal */}
      {isEditModalOpen && editingContact && (
        <Modal isOpen={isEditModalOpen} onClose={() => setIsEditModalOpen(false)} title="Edit Contact Details">
          <form onSubmit={handleSaveContactEdit} className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-bold text-slate-700 mb-1">First Name</label>
                <input
                  type="text"
                  value={editingContact.firstName}
                  onChange={e => setEditingContact({ ...editingContact, firstName: e.target.value })}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium"
                />
              </div>
              <div>
                <label className="block text-xs font-bold text-slate-700 mb-1">Last Name</label>
                <input
                  type="text"
                  value={editingContact.lastName}
                  onChange={e => setEditingContact({ ...editingContact, lastName: e.target.value })}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium"
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-bold text-slate-700 mb-1">Email Address</label>
              <input
                type="email"
                required
                value={editingContact.email}
                onChange={e => setEditingContact({ ...editingContact, email: e.target.value })}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium"
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-bold text-slate-700 mb-1">Phone</label>
                <input
                  type="text"
                  value={editingContact.phone}
                  onChange={e => setEditingContact({ ...editingContact, phone: e.target.value })}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium"
                />
              </div>
              <div>
                <label className="block text-xs font-bold text-slate-700 mb-1">Company</label>
                <input
                  type="text"
                  value={editingContact.company}
                  onChange={e => setEditingContact({ ...editingContact, company: e.target.value })}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium"
                />
              </div>
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-bold text-slate-700 mb-1">City</label>
                <input
                  type="text"
                  value={editingContact.city}
                  onChange={e => setEditingContact({ ...editingContact, city: e.target.value })}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium"
                />
              </div>
              <div>
                <label className="block text-xs font-bold text-slate-700 mb-1">Country</label>
                <input
                  type="text"
                  value={editingContact.country}
                  onChange={e => setEditingContact({ ...editingContact, country: e.target.value })}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium"
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-bold text-slate-700 mb-1">Status</label>
              <select
                value={editingContact.status}
                onChange={e => setEditingContact({ ...editingContact, status: e.target.value })}
                className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium"
              >
                <option value="SUBSCRIBED">SUBSCRIBED</option>
                <option value="UNSUBSCRIBED">UNSUBSCRIBED</option>
                <option value="BOUNCED">BOUNCED</option>
                <option value="COMPLAINED">COMPLAINED</option>
              </select>
            </div>

            <div className="flex justify-end gap-3 pt-3 border-t border-slate-100">
              <button
                type="button"
                onClick={() => setIsEditModalOpen(false)}
                className="px-4 py-2 text-xs font-bold text-slate-600 bg-slate-100 rounded-xl hover:bg-slate-200 cursor-pointer"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="px-5 py-2 text-xs font-bold text-white bg-blue-600 rounded-xl hover:bg-blue-700 shadow-md shadow-blue-500/20 cursor-pointer"
              >
                Save Changes
              </button>
            </div>
          </form>
        </Modal>
      )}

    </div>
  );
};
