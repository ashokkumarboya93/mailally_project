import React, { useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { campaignApi, segmentApi, templateApi } from '../../api/campaignApi';
import { Check, ArrowRight, Send, Layers, Rocket } from 'lucide-react';

export const CampaignWizardPage = () => {
  const navigate = useNavigate();
  const [step, setStep] = useState(1);
  const [formData, setFormData] = useState({
    name: '',
    type: 'REGULAR',
    senderName: 'MailAlly Marketing',
    senderEmail: 'marketing@mailally.com',
    replyTo: '',
    subject: '',
    templateId: '',
    segmentId: '',
    batchSize: 500
  });
  const [templates, setTemplates] = useState([]);
  const [segments, setSegments] = useState([]);
  const [previewContacts, setPreviewContacts] = useState([]);
  const [loadingOptions, setLoadingOptions] = useState(true);
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const unwrapPage = (response) => response?.data?.content || response?.data || response?.content || [];
  const unwrapData = (response) => response?.data || response;

  const selectedTemplate = useMemo(
    () => templates.find((template) => String(template.id) === String(formData.templateId)),
    [templates, formData.templateId]
  );
  const selectedSegment = useMemo(
    () => segments.find((segment) => String(segment.id) === String(formData.segmentId)),
    [segments, formData.segmentId]
  );

  useEffect(() => {
    const loadOptions = async () => {
      setLoadingOptions(true);
      setError('');
      try {
        const [templateResponse, segmentResponse] = await Promise.all([
          templateApi.getTemplates(0, 50),
          segmentApi.getSegments(0, 50)
        ]);

        let loadedTemplates = unwrapPage(templateResponse);
        const loadedSegments = unwrapPage(segmentResponse);

        if (loadedTemplates.length === 0) {
          const createdTemplateResponse = await templateApi.createTemplate({
            name: 'Default Campaign Template',
            subject: 'A quick update from MailAlly',
            htmlContent: '<h1>Hello {{firstName}}</h1><p>Here is your latest update from MailAlly.</p>',
            textContent: 'Hello {{firstName}}, here is your latest update from MailAlly.',
            status: 'ACTIVE'
          });
          const createdTemplate = unwrapData(createdTemplateResponse);
          loadedTemplates = createdTemplate ? [createdTemplate] : [];
        }

        setTemplates(loadedTemplates);
        setSegments(loadedSegments);

        setFormData((prev) => {
          const nextTemplate = prev.templateId || loadedTemplates[0]?.id || '';
          const template = loadedTemplates.find((item) => String(item.id) === String(nextTemplate));
          return {
            ...prev,
            templateId: nextTemplate,
            segmentId: prev.segmentId || loadedSegments[0]?.id || '',
            subject: prev.subject || template?.subject || ''
          };
        });
      } catch (err) {
        setError(err.response?.data?.message || 'Unable to load campaign templates and audiences.');
      } finally {
        setLoadingOptions(false);
      }
    };

    loadOptions();
  }, []);

  useEffect(() => {
    const loadPreview = async () => {
      if (!formData.segmentId) {
        setPreviewContacts([]);
        return;
      }
      try {
        const response = await segmentApi.previewContacts(formData.segmentId);
        setPreviewContacts(unwrapData(response) || []);
      } catch {
        setPreviewContacts([]);
      }
    };

    loadPreview();
  }, [formData.segmentId]);

  const validateStep = () => {
    if (step === 1 && (!formData.name.trim() || !formData.senderName.trim() || !formData.senderEmail.trim() || !formData.subject.trim())) {
      return 'Campaign name, subject, sender name, and sender email are required.';
    }
    if (step === 2 && !formData.segmentId && segments.length > 0) {
      return 'Select an audience segment before continuing.';
    }
    if (step === 3 && !formData.templateId) {
      return 'Select an email template before continuing.';
    }
    return '';
  };

  const handleNext = () => {
    const validationMessage = validateStep();
    if (validationMessage) {
      setError(validationMessage);
      return;
    }
    setError('');
    setStep((prev) => Math.min(prev + 1, 4));
  };
  const handleBack = () => setStep((prev) => Math.max(prev - 1, 1));

  const handleSubmit = async () => {
    setSubmitting(true);
    setError('');
    try {
      let segmentId = formData.segmentId;
      if (!segmentId) {
        const segmentResponse = await segmentApi.createSegment({
          name: 'All Active Subscribers',
          description: 'Auto-created audience for campaign launch',
          type: 'DYNAMIC',
          rulesJson: '{}'
        });
        segmentId = unwrapData(segmentResponse)?.id;
      }

      if (!segmentId) {
        throw new Error('No audience segment is available. Create contacts and an audience segment before launching.');
      }

      const payload = {
        name: formData.name.trim(),
        subject: formData.subject.trim(),
        senderName: formData.senderName.trim(),
        senderEmail: formData.senderEmail.trim(),
        replyTo: formData.replyTo?.trim() || formData.senderEmail.trim(),
        templateId: Number(formData.templateId),
        segmentId: Number(segmentId),
        batchSize: Number(formData.batchSize) || 500
      };

      const createResponse = await campaignApi.createCampaign(payload);
      const createdCampaign = unwrapData(createResponse);
      await campaignApi.launchCampaign(createdCampaign.id, payload.batchSize);
      alert('Campaign launched successfully!');
      navigate('/campaigns');
    } catch (err) {
      setError('Failed to launch campaign: ' + (err.response?.data?.message || err.message));
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="max-w-4xl mx-auto space-y-8 font-sans pb-12 animate-fadeInUp">
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
            <Rocket className="w-3 h-3 text-[#00DDFF]" />
            <span>Campaign Creation Wizard</span>
          </div>

          <h1 className="text-2xl sm:text-3xl font-black tracking-tight leading-none text-white" style={{ fontFamily: 'var(--font-heading)' }}>
            Campaign <br />
            <span className="text-[#00DDFF]">Launch Studio</span>
          </h1>

          <p className="text-[10px] sm:text-[11px] text-blue-50 leading-relaxed font-medium">
            Configure details, select audience segment, template, and review dispatch.
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

      {error && (
        <div className="p-4 rounded-2xl text-xs font-black bg-rose-50 text-rose-600 border border-rose-100 shadow-3xs">
          {error}
        </div>
      )}

      {/* Stepper Progress Card */}
      <div className="bg-white rounded-[22px] p-5 border flex items-center justify-between shadow-xs" style={{ borderColor: 'rgba(37,99,235,0.08)' }}>
        {[1, 2, 3, 4].map((s) => (
          <div key={s} className="flex items-center space-x-2.5">
            <div
              className={`w-8 h-8 rounded-xl flex items-center justify-center text-xs font-black transition-all duration-300 border ${step >= s ? 'bg-[#2563EB] text-white border-transparent shadow-md shadow-blue-500/20' : 'bg-slate-100 text-slate-400 border-slate-200/60'}`}
            >
              {step > s ? <Check className="w-4 h-4 text-white stroke-[3]" /> : s}
            </div>
            <span
              className={`text-xs font-black hidden sm:inline ${step === s ? 'text-[#1E3A8A]' : 'text-slate-400'}`}
              style={{ fontFamily: 'var(--font-heading)' }}
            >
              {s === 1 ? 'Setup' : s === 2 ? 'Audience' : s === 3 ? 'Template' : 'Review'}
            </span>
          </div>
        ))}
      </div>

      {/* Step Content Card */}
      <div className="bg-white rounded-[22px] p-6.5 space-y-5 border shadow-xs" style={{ borderColor: 'rgba(37,99,235,0.08)' }}>
        {step === 1 && (
          <div className="space-y-4">
            <h3 className="font-black text-base text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>Step 1: Campaign Setup</h3>
            <div>
              <label className="block text-xs font-black mb-1.5 uppercase tracking-wider text-slate-400">Campaign Name *</label>
              <input
                type="text"
                required
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                placeholder="e.g. Q4 Black Friday Promo"
                className="w-full p-3 rounded-xl border border-slate-200 text-xs font-semibold focus:outline-none focus:border-[#2563EB] bg-white"
              />
            </div>
            <div>
              <label className="block text-xs font-black mb-1.5 uppercase tracking-wider text-slate-400">Subject Line *</label>
              <input
                type="text"
                required
                value={formData.subject}
                onChange={(e) => setFormData({ ...formData, subject: e.target.value })}
                placeholder="e.g. Your weekly MailAlly update"
                className="w-full p-3 rounded-xl border border-slate-200 text-xs font-semibold focus:outline-none focus:border-[#2563EB] bg-white"
              />
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-black mb-1.5 uppercase tracking-wider text-slate-400">Sender Name *</label>
                <input
                  type="text"
                  value={formData.senderName}
                  onChange={(e) => setFormData({ ...formData, senderName: e.target.value })}
                  className="w-full p-3 rounded-xl border border-slate-200 text-xs font-semibold focus:outline-none focus:border-[#2563EB] bg-white"
                />
              </div>
              <div>
                <label className="block text-xs font-black mb-1.5 uppercase tracking-wider text-slate-400">Sender Email *</label>
                <input
                  type="email"
                  value={formData.senderEmail}
                  onChange={(e) => setFormData({ ...formData, senderEmail: e.target.value })}
                  className="w-full p-3 rounded-xl border border-slate-200 text-xs font-semibold focus:outline-none focus:border-[#2563EB] bg-white"
                />
              </div>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-xs font-black mb-1.5 uppercase tracking-wider text-slate-400">Reply-To Email</label>
                <input
                  type="email"
                  value={formData.replyTo}
                  onChange={(e) => setFormData({ ...formData, replyTo: e.target.value })}
                  placeholder={formData.senderEmail}
                  className="w-full p-3 rounded-xl border border-slate-200 text-xs font-semibold focus:outline-none focus:border-[#2563EB] bg-white"
                />
              </div>
              <div>
                <label className="block text-xs font-black mb-1.5 uppercase tracking-wider text-slate-400">Batch Size *</label>
                <input
                  type="number"
                  min="1"
                  max="5000"
                  value={formData.batchSize}
                  onChange={(e) => setFormData({ ...formData, batchSize: e.target.value })}
                  className="w-full p-3 rounded-xl border border-slate-200 text-xs font-semibold focus:outline-none focus:border-[#2563EB] bg-white"
                />
              </div>
            </div>
          </div>
        )}

        {step === 2 && (
          <div className="space-y-4">
            <h3 className="font-black text-base text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>Step 2: Audience Selection</h3>
            <p className="text-xs text-slate-400 font-medium">Select the target segment and review the contacts that will receive this campaign.</p>
            {loadingOptions ? (
              <div className="p-4 rounded-xl text-xs font-semibold bg-blue-50/50 text-[#2563EB]">Loading audiences...</div>
            ) : segments.length > 0 ? (
              <div className="grid grid-cols-1 gap-3">
                {segments.map((segment) => {
                  const isSelected = String(formData.segmentId) === String(segment.id);
                  return (
                    <button
                      key={segment.id}
                      type="button"
                      onClick={() => setFormData({ ...formData, segmentId: segment.id })}
                      className={`text-left p-4 rounded-2xl transition-all duration-200 cursor-pointer border ${isSelected ? 'border-2 border-[#2563EB] bg-blue-50/40 shadow-xs' : 'border-slate-200 bg-white hover:bg-slate-50/60'}`}
                    >
                      <span className="text-xs font-black block text-[#1E293B]" style={{ fontFamily: 'var(--font-heading)' }}>{segment.name}</span>
                      <span className="text-[11px] text-slate-400 font-semibold block mt-0.5">
                        {segment.contactCount ?? 0} contacts · {segment.type || 'SEGMENT'} · {segment.status || 'ACTIVE'}
                      </span>
                    </button>
                  );
                })}
              </div>
            ) : (
              <div className="p-4 rounded-2xl text-xs font-semibold bg-amber-50 text-amber-700 border border-amber-200">
                No audience segment exists. The launch step will create an “All Active Subscribers” segment automatically.
              </div>
            )}

            <div className="p-4 rounded-2xl space-y-2 bg-[#F7FAFF] border border-blue-100/60">
              <div className="flex items-center justify-between">
                <span className="text-xs font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>Contact Preview</span>
                <span className="text-[11px] text-slate-400 font-bold">{previewContacts.length} loaded</span>
              </div>
              <div className="max-h-48 overflow-y-auto space-y-1.5 pt-1">
                {previewContacts.slice(0, 8).map((contact) => (
                  <div key={contact.id || contact.email} className="flex items-center justify-between text-[11px] text-slate-600 font-semibold">
                    <span>{[contact.firstName, contact.lastName].filter(Boolean).join(' ') || 'Contact'}</span>
                    <span className="font-mono text-slate-400">{contact.email}</span>
                  </div>
                ))}
                {previewContacts.length === 0 && (
                  <p className="text-[11px] text-slate-400 italic">No contacts preview available for this audience.</p>
                )}
              </div>
            </div>
          </div>
        )}

        {step === 3 && (
          <div className="space-y-4">
            <h3 className="font-black text-base text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>Step 3: Select Template</h3>
            {loadingOptions ? (
              <div className="p-4 rounded-xl text-xs font-semibold bg-blue-50/50 text-[#2563EB]">Loading templates...</div>
            ) : templates.length > 0 ? (
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                {templates.map((template) => {
                  const isSelected = String(formData.templateId) === String(template.id);
                  return (
                    <button
                      key={template.id}
                      type="button"
                      onClick={() => setFormData({ ...formData, templateId: template.id, subject: formData.subject || template.subject || '' })}
                      className={`text-left p-4 rounded-2xl transition-all duration-200 cursor-pointer border ${isSelected ? 'border-2 border-[#2563EB] bg-blue-50/40 shadow-xs' : 'border-slate-200 bg-white hover:bg-slate-50/60'}`}
                    >
                      <span className="text-xs font-black block text-[#1E293B]" style={{ fontFamily: 'var(--font-heading)' }}>{template.name}</span>
                      <span className="text-[11px] block truncate text-slate-400 font-medium mt-0.5">{template.subject || 'No subject'}</span>
                      <span className="text-[10px] text-slate-400 font-mono block mt-1">Template #{template.id} · v{template.version || 1}</span>
                    </button>
                  );
                })}
              </div>
            ) : (
              <div className="p-4 rounded-2xl text-xs font-semibold bg-rose-50 text-rose-600 border border-rose-200">
                No email templates found. Create a template before launching a campaign.
              </div>
            )}
          </div>
        )}

        {step === 4 && (
          <div className="space-y-4">
            <h3 className="font-black text-base text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>Step 4: Review & Finalize</h3>
            <div className="p-5 rounded-2xl space-y-2.5 text-xs bg-blue-50/40 border border-blue-100 text-[#1E293B] font-semibold">
              <p><strong className="text-[#2563EB]">Campaign Name:</strong> {formData.name || 'Untitled Campaign'}</p>
              <p><strong className="text-[#2563EB]">Subject:</strong> {formData.subject || selectedTemplate?.subject || 'No subject'}</p>
              <p><strong className="text-[#2563EB]">Sender:</strong> {formData.senderName} ({formData.senderEmail})</p>
              <p><strong className="text-[#2563EB]">Target Segment:</strong> {selectedSegment?.name || 'All Active Subscribers'} ({selectedSegment?.contactCount ?? previewContacts.length} contacts)</p>
              <p><strong className="text-[#2563EB]">Template:</strong> {selectedTemplate?.name || `Template ID ${formData.templateId}`}</p>
              <p><strong className="text-[#2563EB]">Batch Size:</strong> {formData.batchSize}</p>
            </div>
          </div>
        )}

        {/* Wizard Controls */}
        <div className="flex items-center justify-between pt-4 border-t border-slate-100">
          <button
            onClick={handleBack}
            disabled={step === 1}
            className="px-5 py-2.5 rounded-full bg-slate-100 text-slate-600 hover:bg-slate-200 text-xs font-black transition-all cursor-pointer disabled:opacity-40 disabled:cursor-not-allowed"
          >
            Back
          </button>
          {step < 4 ? (
            <button
              onClick={handleNext}
              className="px-6 py-2.5 rounded-full bg-[#2563EB] hover:bg-[#1D4ED8] text-white text-xs font-black transition-all cursor-pointer shadow-md shadow-blue-500/20 flex items-center space-x-2"
            >
              <span>Next</span>
              <ArrowRight className="w-4 h-4" />
            </button>
          ) : (
            <button
              onClick={handleSubmit}
              disabled={submitting}
              className="px-6 py-2.5 text-white font-black rounded-full text-xs shadow-md shadow-blue-500/25 bg-[#2563EB] hover:bg-[#1D4ED8] flex items-center space-x-2 transition-all cursor-pointer disabled:opacity-50"
            >
              <Send className="w-4 h-4" />
              <span>{submitting ? 'Creating...' : 'Create & Launch'}</span>
            </button>
          )}
        </div>
      </div>
    </div>
  );
};
