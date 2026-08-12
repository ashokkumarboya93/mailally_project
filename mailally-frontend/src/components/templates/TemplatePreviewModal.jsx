import React, { useState } from 'react';
import { Modal } from '../common/Modal';
import { Monitor, Smartphone, X, Check, Globe } from 'lucide-react';

export const TemplatePreviewModal = ({ isOpen, onClose, template, testContacts = [] }) => {
  const [device, setDevice] = useState('desktop'); // 'desktop' or 'mobile'
  const [selectedContactId, setSelectedContactId] = useState(testContacts[0]?.id || 1);

  if (!template) return null;

  const currentContact = testContacts.find(c => c.id === Number(selectedContactId)) || testContacts[0] || {
    firstName: 'Ashok',
    lastName: 'Kumar',
    email: 'ashok@mailally.com',
    company: 'ABC Global',
    city: 'Hyderabad',
    country: 'India',
    department: 'Marketing',
    designation: 'VP of Growth',
    revenue: '2 Million USD',
    linkedin: 'linkedin.com/in/ashokkumar'
  };

  // Replace dynamic tags for preview rendering
  const renderPreviewHtml = (htmlContent) => {
    if (!htmlContent) return '';
    let rendered = htmlContent;

    const replacements = {
      '{{firstName}}': currentContact.firstName || 'Ashok',
      '{{lastName}}': currentContact.lastName || 'Kumar',
      '{{email}}': currentContact.email || 'ashok@mailally.com',
      '{{phone}}': currentContact.phone || '+91 98765 43210',
      '{{company}}': currentContact.company || 'ABC Global',
      '{{department}}': currentContact.department || 'Marketing',
      '{{designation}}': currentContact.designation || 'VP of Growth',
      '{{city}}': currentContact.city || 'Hyderabad',
      '{{state}}': currentContact.state || 'Telangana',
      '{{country}}': currentContact.country || 'India',
      '{{currentDate}}': new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }),
      '{{currentTime}}': new Date().toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' }),
      '{{campaignName}}': 'Q3 Global Growth Campaign',
      '{{organizationName}}': 'MailAlly Technologies',
      '{{unsubscribeLink}}': 'http://localhost:8081/unsubscribe',
      '{{viewInBrowser}}': 'http://localhost:8081/view-in-browser',
      '{{campaignUrl}}': 'https://mailally.com',
      '{{primaryCtaUrl}}': template.links?.primaryCtaUrl || 'https://mailally.com/welcome',
      '{{secondaryCtaUrl}}': template.links?.secondaryCtaUrl || 'https://mailally.com/learn-more'
    };

    // Replace custom JSON fields if present
    if (currentContact.customFields) {
      try {
        const parsed = JSON.parse(currentContact.customFields);
        Object.keys(parsed).forEach(key => {
          replacements[`{{${key}}}`] = parsed[key];
        });
      } catch (e) {}
    }

    Object.keys(replacements).forEach(key => {
      rendered = rendered.replaceAll(key, replacements[key]);
    });

    return rendered;
  };

  const renderSubject = (subject) => {
    if (!subject) return '';
    let s = subject;
    s = s.replaceAll('{{firstName}}', currentContact.firstName || 'Ashok');
    s = s.replaceAll('{{company}}', currentContact.company || 'ABC Global');
    return s;
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={`Preview Template: ${template.name}`} size="xl">
      <div className="space-y-4 font-sans text-slate-800">
        
        {/* Top Control Bar */}
        <div className="flex flex-wrap items-center justify-between gap-3 p-3 bg-slate-50 border border-slate-200 rounded-2xl">
          
          {/* Device Aspect Ratio Toggles */}
          <div className="flex items-center gap-1 bg-white p-1 rounded-xl border border-slate-200 shadow-xs">
            <button
              type="button"
              onClick={() => setDevice('desktop')}
              className={`flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-bold transition-all cursor-pointer ${
                device === 'desktop' ? 'bg-slate-900 text-white shadow-xs' : 'text-slate-600 hover:text-slate-900 hover:bg-slate-100'
              }`}
            >
              <Monitor className="w-3.5 h-3.5" /> Desktop (600px)
            </button>
            <button
              type="button"
              onClick={() => setDevice('mobile')}
              className={`flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-bold transition-all cursor-pointer ${
                device === 'mobile' ? 'bg-slate-900 text-white shadow-xs' : 'text-slate-600 hover:text-slate-900 hover:bg-slate-100'
              }`}
            >
              <Smartphone className="w-3.5 h-3.5" /> Mobile (375px)
            </button>
          </div>

          {/* Test Recipient Profile Selector */}
          <div className="flex items-center gap-2">
            <span className="text-xs font-bold text-slate-500">Test Profile:</span>
            <select
              value={selectedContactId}
              onChange={(e) => setSelectedContactId(Number(e.target.value))}
              className="h-8 px-3 text-xs font-semibold text-slate-800 bg-white border border-slate-300 rounded-xl outline-none focus:border-blue-500 cursor-pointer shadow-xs"
            >
              {testContacts.map(c => (
                <option key={c.id} value={c.id}>
                  {c.firstName} {c.lastName} ({c.company || c.email})
                </option>
              ))}
            </select>
          </div>
        </div>

        {/* Header Preview Bar */}
        <div className="p-3.5 bg-slate-100/70 border border-slate-200 rounded-2xl space-y-1.5 text-xs">
          <div className="flex items-center justify-between">
            <span className="font-bold text-slate-700">Subject: <span className="text-slate-900 font-semibold">{renderSubject(template.subject)}</span></span>
            <span className="text-[11px] font-bold text-slate-400 uppercase tracking-wider">{template.category || 'General'}</span>
          </div>
          <div className="text-slate-500 text-[11px] font-medium">
            From: <span className="text-slate-700 font-semibold">MailAlly Team &lt;info@mailally.com&gt;</span> &bull; To: <span className="text-slate-700 font-semibold">{currentContact.email}</span>
          </div>
        </div>

        {/* Device Frame Viewport Container */}
        <div className="bg-slate-200/60 p-6 rounded-3xl border border-slate-300 min-h-[420px] max-h-[550px] overflow-y-auto flex justify-center">
          <div
            className={`bg-white transition-all duration-300 shadow-xl overflow-hidden ${
              device === 'mobile'
                ? 'w-[375px] rounded-[36px] border-[8px] border-slate-800 my-2'
                : 'w-[600px] rounded-2xl border border-slate-300'
            }`}
          >
            {device === 'mobile' && (
              <div className="w-full bg-slate-800 h-5 flex items-center justify-center">
                <div className="w-16 h-2 rounded-full bg-slate-600"></div>
              </div>
            )}
            <div
              className="p-6 overflow-y-auto text-slate-900"
              dangerouslySetInnerHTML={{ __html: renderPreviewHtml(template.content || template.htmlContent) }}
            />
          </div>
        </div>

      </div>
    </Modal>
  );
};
