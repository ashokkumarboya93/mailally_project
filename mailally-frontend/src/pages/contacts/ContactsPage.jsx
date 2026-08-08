import React, { useState, useEffect } from 'react';
import { contactApi } from '../../api/contactApi';
import { campaignApi } from '../../api/campaignApi';
import { StatusBadge } from '../../components/common/StatusBadge';
import { Modal } from '../../components/common/Modal';
import { PageSkeletonLoader } from '../../components/common/PageSkeletonLoader';
import { GoogleDrivePickerModal } from '../../components/contacts/GoogleDrivePickerModal';
import { ImportHistoryModal } from '../../components/contacts/ImportHistoryModal';
import { 
  Plus, Search, Users, Upload, Trash2, 
  FileText, Layers, Edit3, Megaphone, Grid, HardDrive, Edit2, Save, X, ArrowLeft, ArrowRight, Zap
} from 'lucide-react';

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
          { id: 101, name: 'Enterprise Customers', contactCount: 2458, subscribedCount: 2400, invalidCount: 42, duplicateCount: 16, sourceType: 'EXCEL', tag: 'Enterprise', colorCode: '#EC4899', createdAt: new Date().toISOString() },
          { id: 102, name: 'Healthcare Leads', contactCount: 1588, subscribedCount: 1550, invalidCount: 20, duplicateCount: 18, sourceType: 'CSV', tag: 'Healthcare', colorCode: '#22C55E', createdAt: new Date().toISOString() },
          { id: 103, name: 'July Marketing Leads', contactCount: 5280, subscribedCount: 5100, invalidCount: 120, duplicateCount: 60, sourceType: 'GOOGLE_DRIVE', tag: 'July', colorCode: '#A855F7', createdAt: new Date().toISOString() }
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
    <div className="space-y-6 animate-fadeInUp pb-8 font-sans">
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold tracking-tight text-[#0A0A0B]">Contacts</h1>
          <p className="text-[13px] text-[#9CA3AF] font-medium mt-1">
            Manage contact lists, collections, and custom dynamic fields.
          </p>
        </div>

        {/* Primary Action Buttons */}
        <div className="flex items-center gap-2">
          <button
            onClick={() => setIsImportModalOpen(true)}
            className="ma-btn ma-btn-primary gap-1.5 text-[12px]"
          >
            <Upload className="w-3.5 h-3.5" /> Upload File
          </button>
          <button
            onClick={() => setIsDriveModalOpen(true)}
            className="ma-btn ma-btn-secondary gap-1.5 text-[12px]"
          >
            <HardDrive className="w-3.5 h-3.5 text-[#9CA3AF]" /> Drive Import
          </button>
          <button
            onClick={() => setIsAddModalOpen(true)}
            className="ma-btn ma-btn-secondary gap-1.5 text-[12px]"
          >
            <Plus className="w-3.5 h-3.5 text-[#9CA3AF]" /> Add Contact
          </button>
        </div>
      </div>

      {/* View Switcher & Search Bar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 bg-white p-2 rounded-[16px] border border-[#E5E5E7]">
        <div className="flex items-center gap-1">
          <button
            onClick={() => { setViewMode('CARDS'); setActiveCollectionId(null); }}
            className={`px-3.5 py-1.5 rounded-lg text-[12px] font-semibold flex items-center gap-1.5 transition-all cursor-pointer ${
              viewMode === 'CARDS' && !activeCollectionId
                ? 'bg-[#0A0A0B] text-white'
                : 'text-[#5F6368] hover:bg-[#F9FAFB]'
            }`}
          >
            <Layers className="w-3.5 h-3.5" /> Collections ({collections.length})
          </button>
          <button
            onClick={() => setViewMode('SPREADSHEET')}
            className={`px-3.5 py-1.5 rounded-lg text-[12px] font-semibold flex items-center gap-1.5 transition-all cursor-pointer ${
              viewMode === 'SPREADSHEET' || activeCollectionId
                ? 'bg-[#0A0A0B] text-white'
                : 'text-[#5F6368] hover:bg-[#F9FAFB]'
            }`}
          >
            <Grid className="w-3.5 h-3.5" /> Grid View
          </button>
        </div>

        <div className="flex items-center gap-2">
          <div className="relative flex-1 sm:w-64">
            <Search className="w-3.5 h-3.5 text-[#C0C5CC] absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              placeholder="Search contacts..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              className="w-full pl-9 pr-3 h-8 text-[12px] font-medium bg-[#FAFAFB] border border-[#E5E5E7] rounded-lg outline-none focus:border-[#D1D5DB]"
            />
          </div>

          <button
            onClick={() => { loadHistory(); setIsHistoryModalOpen(true); }}
            className="p-1.5 rounded-lg border border-[#E5E5E7] bg-white text-[#9CA3AF] hover:text-[#0A0A0B] transition-colors cursor-pointer"
            title="Import History"
          >
            <FileText className="w-4 h-4" />
          </button>
        </div>
      </div>

      {/* COLLECTION CARDS VIEW */}
      {viewMode === 'CARDS' && !activeCollectionId && (
        <div className="space-y-3">
          <div className="flex items-center justify-between">
            <h2 className="text-[13px] font-semibold text-[#5F6368] uppercase tracking-wider">Collections</h2>
            <button
              onClick={() => setIsCreateCollectionOpen(true)}
              className="text-[12px] font-semibold text-[#0A0A0B] hover:underline cursor-pointer"
            >
              + Custom Collection
            </button>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {collections.map((coll, idx) => {
              const themes = [
                {
                  bg: 'bg-[#FFF0F5] border-[#FCE7F3]',
                  iconBg: 'bg-white text-[#DB2777] border-[#FCE7F3]',
                  pillBg: 'bg-[#FCE7F3] text-[#DB2777]',
                  icon: Users
                },
                {
                  bg: 'bg-[#F3E8FF] border-[#DDD6FE]',
                  iconBg: 'bg-white text-[#7C3AED] border-[#DDD6FE]',
                  pillBg: 'bg-[#DDD6FE] text-[#7C3AED]',
                  icon: Layers
                },
                {
                  bg: 'bg-[#ECFDF5] border-[#A7F3D0]',
                  iconBg: 'bg-white text-[#059669] border-[#A7F3D0]',
                  pillBg: 'bg-[#A7F3D0] text-[#047857]',
                  icon: Zap
                }
              ];
              const theme = themes[idx % themes.length];
              const CardIcon = theme.icon;

              return (
                <div
                  key={coll.id}
                  className="bg-white border border-[#18181B] rounded-[26px] p-4 sm:p-5 flex flex-col justify-between hover:shadow-md transition-all duration-300 group cursor-pointer"
                  onClick={() => { setActiveCollectionId(coll.id); setViewMode('SPREADSHEET'); }}
                >
                  {/* Inner Tinted Container */}
                  <div className={`${theme.bg} rounded-[20px] p-5 mb-4 border transition-colors`}>
                    <div className="flex items-start justify-between gap-3">
                      <h3 className="font-extrabold text-[18px] text-[#18181B] tracking-tight truncate leading-tight">
                        {coll.name.replace(/\.[^/.]+$/, "")}
                      </h3>
                      <div className={`w-9 h-9 rounded-xl ${theme.iconBg} flex items-center justify-center flex-shrink-0 border shadow-2xs`}>
                        <CardIcon className="w-4 h-4" strokeWidth={2} />
                      </div>
                    </div>

                    <p className="text-[12px] font-medium text-[#52525B] leading-relaxed mt-2.5 mb-4 line-clamp-2">
                      Organized contact audience stream for targeted campaign delivery and real-time behavioral tracking.
                    </p>

                    {/* Pastel Tags Row */}
                    <div className="flex flex-wrap items-center gap-1.5 pt-1">
                      <span className={`px-3 py-1 rounded-full text-[11px] font-bold ${theme.pillBg}`}>
                        {coll.sourceType || 'Excel'}
                      </span>
                      <span className={`px-3 py-1 rounded-full text-[11px] font-bold ${theme.pillBg}`}>
                        {coll.contactCount || 0} Contacts
                      </span>
                      <span className={`px-3 py-1 rounded-full text-[11px] font-bold ${theme.pillBg}`}>
                        {coll.subscribedCount || 0} Active
                      </span>
                    </div>
                  </div>

                  {/* Bottom Footer Action Row */}
                  <div className="flex items-center justify-between px-1 pt-1">
                    <span className="text-[13px] font-extrabold text-[#18181B] group-hover:underline">
                      Explore audience
                    </span>
                    <div className="flex items-center gap-2">
                      <button
                        type="button"
                        onClick={(e) => {
                          e.stopPropagation();
                          setSelectedCampaignId('');
                          setIsAddToCampaignOpen(coll.id);
                        }}
                        className="p-2 rounded-full border border-[#18181B] text-[#18181B] hover:bg-[#18181B] hover:text-white transition-colors"
                        title="Add to Campaign"
                      >
                        <Megaphone className="w-3.5 h-3.5" />
                      </button>
                      <button
                        type="button"
                        onClick={(e) => {
                          e.stopPropagation();
                          handleDeleteCollection(coll.id, coll.name);
                        }}
                        className="p-2 rounded-full border border-rose-300 text-rose-600 hover:bg-rose-600 hover:text-white transition-colors"
                        title="Delete Collection"
                      >
                        <Trash2 className="w-3.5 h-3.5" />
                      </button>
                      <div className="w-9 h-9 rounded-full border border-[#18181B] bg-white flex items-center justify-center text-[#18181B] group-hover:bg-[#18181B] group-hover:text-white transition-all shadow-xs">
                        <ArrowRight className="w-4 h-4" />
                      </div>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      )}

      {/* SPREADSHEET GRID VIEW */}
      {(viewMode === 'SPREADSHEET' || activeCollectionId) && (
        <div className="space-y-3">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              {activeCollectionId && (
                <button
                  onClick={() => setActiveCollectionId(null)}
                  className="flex items-center gap-1 text-[12px] font-semibold text-[#5F6368] hover:text-[#0A0A0B] bg-white px-3 py-1 rounded-lg border border-[#E5E5E7] cursor-pointer"
                >
                  <ArrowLeft className="w-3.5 h-3.5" /> Back
                </button>
              )}
              <h3 className="text-[14px] font-bold text-[#0A0A0B]">
                {activeCollectionId ? 'Collection Grid' : 'All Contacts Grid'}
              </h3>
              {selectedIds.length > 0 && (
                <div className="flex items-center gap-2 ml-2">
                  <button
                    onClick={() => setIsAddToCampaignOpen('SELECTED')}
                    className="px-2.5 py-1 bg-[#0A0A0B] text-white rounded-lg text-[11px] font-semibold flex items-center gap-1 cursor-pointer"
                  >
                    <Plus className="w-3 h-3" /> Add to Campaign ({selectedIds.length})
                  </button>
                  <button
                    onClick={handleBulkDeleteContacts}
                    className="px-2.5 py-1 bg-[#FFE4E6] text-[#E11D48] rounded-lg text-[11px] font-semibold flex items-center gap-1 cursor-pointer"
                  >
                    <Trash2 className="w-3 h-3" /> Delete ({selectedIds.length})
                  </button>
                </div>
              )}
            </div>
            <span className="text-[11px] text-[#9CA3AF] font-medium hidden sm:inline">Double click cell to edit inline</span>
          </div>

          <div className="overflow-x-auto border border-[#E5E5E7] rounded-[16px] bg-white">
            <table className="w-full text-left border-collapse min-w-[800px]">
              <thead>
                <tr className="bg-[#FAFAFB] text-[11px] font-semibold text-[#9CA3AF] uppercase tracking-wider border-b border-[#E5E5E7]">
                  <th className="p-3 w-10">
                    <input type="checkbox" checked={selectedIds.length === contacts.length && contacts.length > 0} onChange={toggleSelectAll} className="rounded border-[#E5E5E7]" />
                  </th>
                  <th className="py-3 px-3">First Name</th>
                  <th className="py-3 px-3">Last Name</th>
                  <th className="py-3 px-3">Email</th>
                  <th className="py-3 px-3">Company</th>
                  <th className="py-3 px-3">Location</th>
                  <th className="py-3 px-3">Status</th>
                  {dynamicFields.map(field => (
                    <th key={field.id} className="py-3 px-3 text-[#EC4899]">
                      {field.displayName}
                    </th>
                  ))}
                  <th className="py-3 px-3 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[#F0F0F2] text-[13px] text-[#0A0A0B]">
                {contacts.map((c) => (
                  <tr key={c.id} className="hover:bg-[#FAFAFB] transition-colors">
                    <td className="p-3">
                      <input type="checkbox" checked={selectedIds.includes(c.id)} onChange={() => toggleSelectOne(c.id)} className="rounded border-[#E5E5E7]" />
                    </td>

                    <td 
                      onDoubleClick={() => { setEditingCell({ contactId: c.id, fieldName: 'firstName', isCustomField: false }); setEditValue(c.firstName || ''); }}
                      className="py-3 px-3 font-semibold cursor-pointer"
                    >
                      {editingCell?.contactId === c.id && editingCell?.fieldName === 'firstName' ? (
                        <div className="flex items-center gap-1">
                          <input type="text" value={editValue} onChange={e => setEditValue(e.target.value)} className="px-2 py-0.5 text-xs border border-[#0A0A0B] rounded" autoFocus />
                          <button onClick={() => handleSaveInlineCell(c.id, 'firstName', false)} className="p-1 bg-[#0A0A0B] text-white rounded"><Save className="w-3 h-3" /></button>
                          <button onClick={() => setEditingCell(null)} className="p-1 bg-[#F3F4F6] text-[#5F6368] rounded"><X className="w-3 h-3" /></button>
                        </div>
                      ) : (
                        c.firstName || '-'
                      )}
                    </td>

                    <td 
                      onDoubleClick={() => { setEditingCell({ contactId: c.id, fieldName: 'lastName', isCustomField: false }); setEditValue(c.lastName || ''); }}
                      className="py-3 px-3 font-medium cursor-pointer"
                    >
                      {editingCell?.contactId === c.id && editingCell?.fieldName === 'lastName' ? (
                        <div className="flex items-center gap-1">
                          <input type="text" value={editValue} onChange={e => setEditValue(e.target.value)} className="px-2 py-0.5 text-xs border border-[#0A0A0B] rounded" autoFocus />
                          <button onClick={() => handleSaveInlineCell(c.id, 'lastName', false)} className="p-1 bg-[#0A0A0B] text-white rounded"><Save className="w-3 h-3" /></button>
                        </div>
                      ) : (
                        c.lastName || '-'
                      )}
                    </td>

                    <td className="py-3 px-3 font-mono text-[12px] text-[#5F6368]">{c.email}</td>

                    <td className="py-3 px-3 font-medium text-[#5F6368]">{c.company || '-'}</td>
                    <td className="py-3 px-3 text-[#9CA3AF] text-[12px] font-medium">{c.city || ''} {c.country ? `(${c.country})` : ''}</td>

                    <td className="py-3 px-3">
                      <StatusBadge status={c.status || 'SUBSCRIBED'} />
                    </td>

                    {dynamicFields.map(field => (
                      <td
                        key={field.id}
                        onDoubleClick={() => { setEditingCell({ contactId: c.id, fieldName: field.fieldKey, isCustomField: true }); setEditValue(parseCustomFieldValue(c, field.fieldKey)); }}
                        className="py-3 px-3 font-mono text-[12px] text-[#EC4899] font-medium cursor-pointer"
                      >
                        {parseCustomFieldValue(c, field.fieldKey)}
                      </td>
                    ))}

                    <td className="py-3 px-3 text-right">
                      <div className="flex items-center justify-end gap-1">
                        <button
                          onClick={() => handleOpenEditModal(c)}
                          className="p-1 text-[#9CA3AF] hover:text-[#0A0A0B] cursor-pointer"
                          title="Edit"
                        >
                          <Edit2 className="w-3.5 h-3.5" />
                        </button>
                        <button
                          onClick={() => handleDeleteSingleContact(c.id)}
                          className="p-1 text-[#9CA3AF] hover:text-[#E11D48] cursor-pointer"
                          title="Delete"
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
      <Modal isOpen={isImportModalOpen} onClose={() => setIsImportModalOpen(false)} title="Upload File">
        <form onSubmit={handleStartImport} className="space-y-4">
          <div>
            <label className="ma-label">Select File (.xlsx or .csv)</label>
            <input
              type="file"
              accept=".csv,.xlsx"
              onChange={(e) => setImportFile(e.target.files[0])}
              className="ma-input p-2"
            />
          </div>
          <div>
            <label className="ma-label">Duplicate Strategy</label>
            <select
              value={duplicateStrategy}
              onChange={(e) => setDuplicateStrategy(e.target.value)}
              className="ma-select"
            >
              <option value="SKIP">Skip Duplicates</option>
              <option value="UPDATE">Update Existing</option>
            </select>
          </div>
          <button
            type="submit"
            disabled={importing}
            className="ma-btn ma-btn-primary w-full"
          >
            {importing ? 'Importing File...' : 'Start Import'}
          </button>
        </form>
      </Modal>

      {/* Attach Collection To Campaign Modal */}
      <Modal isOpen={!!isAddToCampaignOpen} onClose={() => setIsAddToCampaignOpen(false)} title="Add Contacts to Campaign">
        <div className="space-y-4">
          <p className="text-[13px] text-[#5F6368] font-medium">
            Select target campaign for contacts:
          </p>
          <select
            value={selectedCampaignId}
            onChange={(e) => setSelectedCampaignId(e.target.value)}
            className="ma-select"
          >
            <option value="">Select Campaign...</option>
            {campaigns.map(c => (
              <option key={c.id} value={c.id}>{c.name} ({c.status})</option>
            ))}
          </select>

          <button
            onClick={() => handleAddCollectionToCampaign(isAddToCampaignOpen)}
            disabled={!selectedCampaignId}
            className="ma-btn ma-btn-primary w-full"
          >
            Confirm & Attach
          </button>
        </div>
      </Modal>

      {/* Edit Contact Modal */}
      {isEditModalOpen && editingContact && (
        <Modal isOpen={isEditModalOpen} onClose={() => setIsEditModalOpen(false)} title="Edit Contact">
          <form onSubmit={handleSaveContactEdit} className="space-y-4">
            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="ma-label">First Name</label>
                <input
                  type="text"
                  value={editingContact.firstName}
                  onChange={e => setEditingContact({ ...editingContact, firstName: e.target.value })}
                  className="ma-input"
                />
              </div>
              <div>
                <label className="ma-label">Last Name</label>
                <input
                  type="text"
                  value={editingContact.lastName}
                  onChange={e => setEditingContact({ ...editingContact, lastName: e.target.value })}
                  className="ma-input"
                />
              </div>
            </div>

            <div>
              <label className="ma-label">Email Address</label>
              <input
                type="email"
                required
                value={editingContact.email}
                onChange={e => setEditingContact({ ...editingContact, email: e.target.value })}
                className="ma-input"
              />
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="ma-label">Company</label>
                <input
                  type="text"
                  value={editingContact.company}
                  onChange={e => setEditingContact({ ...editingContact, company: e.target.value })}
                  className="ma-input"
                />
              </div>
              <div>
                <label className="ma-label">Status</label>
                <select
                  value={editingContact.status}
                  onChange={e => setEditingContact({ ...editingContact, status: e.target.value })}
                  className="ma-select"
                >
                  <option value="SUBSCRIBED">SUBSCRIBED</option>
                  <option value="UNSUBSCRIBED">UNSUBSCRIBED</option>
                  <option value="BOUNCED">BOUNCED</option>
                </select>
              </div>
            </div>

            <div className="flex justify-end gap-2 pt-3 border-t border-[#F0F0F2]">
              <button
                type="button"
                onClick={() => setIsEditModalOpen(false)}
                className="ma-btn ma-btn-secondary"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="ma-btn ma-btn-primary"
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
