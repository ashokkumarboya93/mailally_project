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
    <div className="max-w-4xl mx-auto space-y-6 font-sans pb-8 animate-fadeInUp">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-extrabold tracking-tight text-[#0A0A0B]">Campaign Launch Studio</h1>
        <p className="text-[13px] text-[#9CA3AF] font-medium mt-1">
          Configure details, select audience segment, template, and review dispatch.
        </p>
      </div>

      {error && (
        <div className="p-3.5 rounded-xl text-[12px] font-semibold bg-[#FFE4E6] text-[#E11D48] border border-[#FECDD3]">
          {error}
        </div>
      )}

      {/* Stepper */}
      <div className="bg-white rounded-[16px] p-4 border border-[#E5E5E7] flex items-center justify-between">
        {[1, 2, 3, 4].map((s) => (
          <div key={s} className="flex items-center gap-2">
            <div
              className={`w-7 h-7 rounded-lg flex items-center justify-center text-[12px] font-bold transition-all duration-200 ${
                step >= s ? 'bg-[#0A0A0B] text-white' : 'bg-[#F3F4F6] text-[#9CA3AF]'
              }`}
            >
              {step > s ? <Check className="w-3.5 h-3.5" /> : s}
            </div>
            <span className={`text-[12px] font-semibold hidden sm:inline ${step === s ? 'text-[#0A0A0B]' : 'text-[#9CA3AF]'}`}>
              {s === 1 ? 'Setup' : s === 2 ? 'Audience' : s === 3 ? 'Template' : 'Review'}
            </span>
          </div>
        ))}
      </div>

      {/* Step Content */}
      <div className="bg-white rounded-[16px] p-6 space-y-5 border border-[#E5E5E7]">
        {step === 1 && (
          <div className="space-y-4">
            <h3 className="font-bold text-[15px] text-[#0A0A0B]">Step 1: Campaign Setup</h3>
            <div>
              <label className="ma-label">Campaign Name *</label>
              <input
                type="text"
                required
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                placeholder="e.g. Q4 Black Friday Promo"
                className="ma-input"
              />
            </div>
            <div>
              <label className="ma-label">Subject Line *</label>
              <input
                type="text"
                required
                value={formData.subject}
                onChange={(e) => setFormData({ ...formData, subject: e.target.value })}
                placeholder="e.g. Your weekly MailAlly update"
                className="ma-input"
              />
            </div>
            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="ma-label">Sender Name *</label>
                <input
                  type="text"
                  value={formData.senderName}
                  onChange={(e) => setFormData({ ...formData, senderName: e.target.value })}
                  className="ma-input"
                />
              </div>
              <div>
                <label className="ma-label">Sender Email *</label>
                <input
                  type="email"
                  value={formData.senderEmail}
                  onChange={(e) => setFormData({ ...formData, senderEmail: e.target.value })}
                  className="ma-input"
                />
              </div>
            </div>
            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="ma-label">Reply-To Email</label>
                <input
                  type="email"
                  value={formData.replyTo}
                  onChange={(e) => setFormData({ ...formData, replyTo: e.target.value })}
                  placeholder={formData.senderEmail}
                  className="ma-input"
                />
              </div>
              <div>
                <label className="ma-label">Batch Size *</label>
                <input
                  type="number"
                  value={formData.batchSize}
                  onChange={(e) => setFormData({ ...formData, batchSize: e.target.value })}
                  className="ma-input"
                />
              </div>
            </div>
          </div>
        )}

        {step === 2 && (
          <div className="space-y-4">
            <h3 className="font-bold text-[15px] text-[#0A0A0B]">Step 2: Target Audience</h3>
            {loadingOptions ? (
              <p className="text-[12px] text-[#9CA3AF]">Loading audience segments...</p>
            ) : segments.length === 0 ? (
              <div className="p-4 rounded-xl bg-[#FAFAFB] border border-[#E5E5E7] text-[12px] text-[#5F6368]">
                No custom audience segments found. MailAlly will auto-target all active subscribers upon launch.
              </div>
            ) : (
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                {segments.map((seg) => (
                  <div
                    key={seg.id}
                    onClick={() => setFormData({ ...formData, segmentId: seg.id })}
                    className={`p-4 rounded-xl border cursor-pointer transition-all ${
                      String(formData.segmentId) === String(seg.id)
                        ? 'border-[#0A0A0B] bg-[#FAFAFB]'
                        : 'border-[#E5E5E7] hover:border-[#D1D5DB]'
                    }`}
                  >
                    <h4 className="font-bold text-[14px] text-[#0A0A0B]">{seg.name}</h4>
                    <p className="text-[12px] text-[#9CA3AF] mt-1">{seg.description || 'Audience segment'}</p>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {step === 3 && (
          <div className="space-y-4">
            <h3 className="font-bold text-[15px] text-[#0A0A0B]">Step 3: Select Template</h3>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              {templates.map((tpl) => (
                <div
                  key={tpl.id}
                  onClick={() => setFormData({ ...formData, templateId: tpl.id, subject: formData.subject || tpl.subject || '' })}
                  className={`p-4 rounded-xl border cursor-pointer transition-all ${
                    String(formData.templateId) === String(tpl.id)
                      ? 'border-[#0A0A0B] bg-[#FAFAFB]'
                      : 'border-[#E5E5E7] hover:border-[#D1D5DB]'
                  }`}
                >
                  <h4 className="font-bold text-[14px] text-[#0A0A0B]">{tpl.name}</h4>
                  <p className="text-[12px] text-[#9CA3AF] mt-1 truncate">{tpl.subject || 'No subject'}</p>
                </div>
              ))}
            </div>
          </div>
        )}

        {step === 4 && (
          <div className="space-y-4">
            <h3 className="font-bold text-[15px] text-[#0A0A0B]">Step 4: Review & Dispatch</h3>
            <div className="p-4 rounded-xl bg-[#FAFAFB] border border-[#E5E5E7] space-y-2 text-[13px]">
              <p><strong>Name:</strong> {formData.name}</p>
              <p><strong>Subject:</strong> {formData.subject}</p>
              <p><strong>Sender:</strong> {formData.senderName} ({formData.senderEmail})</p>
              <p><strong>Segment:</strong> {selectedSegment?.name || 'All Active Subscribers'}</p>
              <p><strong>Template:</strong> {selectedTemplate?.name || 'Attached'}</p>
              <p><strong>Batch Size:</strong> {formData.batchSize}</p>
            </div>
          </div>
        )}

        {/* Buttons */}
        <div className="flex items-center justify-between pt-4 border-t border-[#F0F0F2]">
          {step > 1 ? (
            <button onClick={handleBack} className="ma-btn ma-btn-secondary">
              Back
            </button>
          ) : (
            <div />
          )}

          {step < 4 ? (
            <button onClick={handleNext} className="ma-btn ma-btn-primary">
              Next Step <ArrowRight className="w-3.5 h-3.5" />
            </button>
          ) : (
            <button onClick={handleSubmit} disabled={submitting} className="ma-btn ma-btn-primary">
              {submitting ? 'Launching...' : 'Launch Campaign'}
            </button>
          )}
        </div>
      </div>
    </div>
  );
};
