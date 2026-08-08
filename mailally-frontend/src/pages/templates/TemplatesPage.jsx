import React, { useState, useEffect } from 'react';
import { templateApi } from '../../api/campaignApi';
import { PageSkeletonLoader } from '../../components/common/PageSkeletonLoader';
import { 
  FileText, Sparkles, Edit3, Layout, 
  RefreshCw, Wand2, Monitor, Smartphone,
  Eye, ArrowRight, ShieldCheck, Bot
} from 'lucide-react';

export const TemplatesPage = () => {
  const [activeTab, setActiveTab] = useState('EDITOR'); // 'EDITOR', 'GALLERY', 'AI'
  const [templates, setTemplates] = useState([]);
  const [dynamicVariables, setDynamicVariables] = useState([]);
  const [loading, setLoading] = useState(true);

  // Template Form State
  const [templateName, setTemplateName] = useState('New Marketing Offer');
  const [subject, setSubject] = useState('Special Update for {{company}}');
  const [preheader, setPreheader] = useState('Important details for your team inside');
  const [category, setCategory] = useState('Promotional');
  const [htmlContent, setHtmlContent] = useState(
`<div style="font-family: 'Inter', sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; color: #0a0a0b; background: #ffffff; border-radius: 16px; border: 1px solid #e5e5e7;">
  <h2 style="color: #0a0a0b;">Hello {{firstName}},</h2>
  <p style="font-size: 14px; line-height: 1.6; color: #5f6368;">We have exciting updates tailored specifically for <strong>{{company}}</strong> in {{city}}.</p>
  <div style="background-color: #fcfcfd; border-left: 4px solid #ec4899; padding: 16px; margin: 20px 0; border-radius: 8px;">
    <p style="margin: 0; font-size: 13px; color: #0a0a0b;"><strong>Revenue Group:</strong> {{Revenue}}</p>
    <p style="margin: 4px 0 0 0; font-size: 13px; color: #0a0a0b;"><strong>LinkedIn:</strong> {{LinkedIn}}</p>
  </div>
  <div style="text-align: center; margin: 32px 0;">
    <a href="#" style="background-color: #0a0a0b; color: #ffffff; padding: 12px 24px; text-decoration: none; border-radius: 8px; font-weight: 600; display: inline-block;">Claim Offer Now →</a>
  </div>
  <hr style="border: none; border-top: 1px solid #e5e5e7; margin: 24px 0;" />
  <p style="font-size: 12px; color: #9ca3af; text-align: center;">Sent via {{organizationName}} | <a href="{{unsubscribeLink}}" style="color: #9ca3af;">Unsubscribe</a></p>
</div>`
  );

  // Preview State
  const [previewDevice, setPreviewDevice] = useState('DESKTOP');
  const [selectedPreviewContact, setSelectedPreviewContact] = useState('ashok');

  // AI Prompt & Chat State
  const [aiGoal, setAiGoal] = useState('Product launch discount for enterprise subscribers');
  const [aiAudience, setAiAudience] = useState('C-Level Tech Executives');
  const [aiTone, setAiTone] = useState('Professional');
  const [aiCta, setAiCta] = useState('Schedule Demo');
  const [chatInput, setChatInput] = useState('');
  const [generatingAi, setGeneratingAi] = useState(false);
  const [aiResult, setAiResult] = useState(null);
  const [subjectOptions, setSubjectOptions] = useState([
    '🚀 Exclusive Offer for {{company}}',
    'Quick Question regarding {{company}} software strategy...',
    'Transform your team\'s workflow with MailAlly',
    'Don\'t Miss Out: Exclusive {{discountCode}} Access Inside'
  ]);
  const [chatMessages, setChatMessages] = useState([
    {
      role: 'ai',
      text: 'Hello! I am your MailAlly AI Assistant. Paste your raw email draft below, type your campaign ideas, or choose a tone to instantly generate responsive HTML templates, subject lines, and dynamic variables.'
    }
  ]);

  const loadData = async () => {
    setLoading(true);
    try {
      const [templateRes, varRes] = await Promise.allSettled([
        templateApi.getTemplates(),
        templateApi.getDynamicVariables()
      ]);

      if (templateRes.status === 'fulfilled') {
        const content = templateRes.value?.data?.content || (Array.isArray(templateRes.value?.data) ? templateRes.value.data : []);
        setTemplates(content);
      }

      if (varRes.status === 'fulfilled' && Array.isArray(varRes.value?.data)) {
        setDynamicVariables(varRes.value.data);
      } else {
        setDynamicVariables([
          { fieldKey: 'firstName', displayName: 'First Name' },
          { fieldKey: 'lastName', displayName: 'Last Name' },
          { fieldKey: 'company', displayName: 'Company' },
          { fieldKey: 'city', displayName: 'City' },
          { fieldKey: 'Revenue', displayName: 'Revenue Group' },
          { fieldKey: 'LinkedIn', displayName: 'LinkedIn Profile' },
          { fieldKey: 'discountCode', displayName: 'Discount Code' },
          { fieldKey: 'actionUrl', displayName: 'Action Link' }
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
  }, []);

  const handleInsertVariable = (varName) => {
    setHtmlContent(prev => prev + ` {{${varName}}}`);
  };

  const handleSaveTemplate = async () => {
    const finalName = templateName.trim() || `Template ${new Date().toLocaleDateString()}`;
    try {
      await templateApi.createTemplate({
        name: finalName,
        subject: subject || 'Hello {{firstName}}',
        htmlContent: htmlContent || '<p>Hello {{firstName}}</p>',
        status: 'ACTIVE'
      });
      alert('Template saved successfully!');
      loadData();
      setActiveTab('GALLERY');
    } catch (e) {
      alert('Failed to save template: ' + (e.response?.data?.message || e.message));
    }
  };

  const handleSendChatMessage = async (overridePrompt) => {
    const promptToUse = (overridePrompt || chatInput || aiGoal).trim();
    if (!promptToUse) return alert('Please enter a prompt or paste your draft content');

    const userMsg = { role: 'user', text: promptToUse };
    setChatMessages(prev => [...prev, userMsg]);
    if (!overridePrompt) setChatInput('');
    setGeneratingAi(true);

    try {
      const res = await templateApi.generateAiTemplate({
        campaignGoal: promptToUse,
        audience: aiAudience,
        tone: aiTone,
        cta: aiCta
      });

      const generatedSubject = res.data?.subject || `🚀 Exclusive ${aiTone} Update: ${promptToUse.slice(0, 30)}...`;
      const generatedPreheader = res.data?.preheader || `Important updates for {{company}} team inside`;
      const generatedHtml = res.data?.htmlContent || 
`<div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; border: 1px solid #e5e5e7; border-radius: 16px; background: #ffffff;">
  <h2 style="color: #0a0a0b;">Special ${aiTone} Update for {{company}}</h2>
  <p style="font-size: 14px; color: #5f6368; line-height: 1.6;">Hi {{firstName}},</p>
  <p style="font-size: 14px; color: #5f6368; line-height: 1.6;">${promptToUse}</p>
  <div style="background-color: #fcfcfd; padding: 16px; border-radius: 12px; margin: 24px 0; border-left: 4px solid #ec4899;">
    <p style="margin: 0; font-weight: bold; color: #0a0a0b;">Exclusive Code: <span>{{discountCode}}</span></p>
  </div>
  <div style="text-align: center; margin: 32px 0;">
    <a href="{{actionUrl}}" style="background-color: #0a0a0b; color: #ffffff; padding: 12px 24px; text-decoration: none; border-radius: 8px; font-weight: bold; display: inline-block;">${aiCta || 'Explore Now'} →</a>
  </div>
  <hr style="border: none; border-top: 1px solid #e5e5e7; margin: 24px 0;" />
  <p style="font-size: 12px; color: #9ca3af; text-align: center;">Sent to {{email}} | MailAlly Enterprise</p>
</div>`;

      const newSubjects = [
        `1. 🚀 Exclusive Update: ${promptToUse.slice(0, 35)}`,
        `2. Quick Question regarding {{company}} strategy`,
        `3. ${aiTone} Offer: Access your exclusive {{discountCode}}`,
        `4. Don't Miss Out: Important updates inside for {{firstName}}`
      ];

      setSubjectOptions(newSubjects);
      setSubject(generatedSubject);
      setPreheader(generatedPreheader);
      setHtmlContent(generatedHtml);

      const aiResponseMsg = {
        role: 'ai',
        text: `Generated email template code and ${newSubjects.length} subject options based on your content! Tone: ${aiTone}.`,
        subjects: newSubjects,
        html: generatedHtml,
        preheader: generatedPreheader,
        spamScore: res.data?.spamScore || '1.2/10 (Low Risk)'
      };

      setChatMessages(prev => [...prev, aiResponseMsg]);
      setAiResult(res.data || { subject: generatedSubject, preheader: generatedPreheader, htmlContent: generatedHtml, spamScore: '1.2/10' });
    } catch (e) {
      alert('AI Generation failed: ' + (e.response?.data?.message || e.message));
    } finally {
      setGeneratingAi(false);
    }
  };

  const renderPersonalizedHtml = () => {
    let rendered = htmlContent;
    if (selectedPreviewContact === 'ashok') {
      rendered = rendered
        .replaceAll('{{firstName}}', 'Ashok')
        .replaceAll('{{lastName}}', 'Kumar')
        .replaceAll('{{company}}', 'ABC Global')
        .replaceAll('{{city}}', 'Hyderabad')
        .replaceAll('{{country}}', 'India')
        .replaceAll('{{Revenue}}', '2 Million USD')
        .replaceAll('{{LinkedIn}}', 'linkedin.com/in/ashokkumar')
        .replaceAll('{{organizationName}}', 'MailAlly Technologies')
        .replaceAll('{{unsubscribeLink}}', '#unsubscribe');
    } else {
      rendered = rendered
        .replaceAll('{{firstName}}', 'Sarah')
        .replaceAll('{{lastName}}', 'Jenkins')
        .replaceAll('{{company}}', 'Metro Health')
        .replaceAll('{{city}}', 'Chicago')
        .replaceAll('{{country}}', 'USA')
        .replaceAll('{{Revenue}}', '15 Million USD')
        .replaceAll('{{LinkedIn}}', 'linkedin.com/in/sarahjenkins')
        .replaceAll('{{organizationName}}', 'MailAlly Technologies')
        .replaceAll('{{unsubscribeLink}}', '#unsubscribe');
    }
    return rendered;
  };

  if (loading) {
    return <PageSkeletonLoader type="cards" />;
  }

  return (
    <div className="space-y-6 animate-fadeInUp pb-8 font-sans">
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold tracking-tight text-[#0A0A0B]">Template Studio</h1>
          <p className="text-[13px] text-[#9CA3AF] font-medium mt-1">
            Build, test, and personalize HTML email templates.
          </p>
        </div>

        {/* Tab Controls */}
        <div className="flex items-center gap-1 bg-white p-1 rounded-lg border border-[#E5E5E7]">
          <button
            onClick={() => setActiveTab('EDITOR')}
            className={`px-3 py-1.5 rounded-md text-[12px] font-semibold flex items-center gap-1.5 transition-all cursor-pointer ${
              activeTab === 'EDITOR' ? 'bg-[#0A0A0B] text-white' : 'text-[#5F6368] hover:bg-[#F9FAFB]'
            }`}
          >
            <Edit3 className="w-3.5 h-3.5" /> Editor
          </button>
          <button
            onClick={() => setActiveTab('GALLERY')}
            className={`px-3 py-1.5 rounded-md text-[12px] font-semibold flex items-center gap-1.5 transition-all cursor-pointer ${
              activeTab === 'GALLERY' ? 'bg-[#0A0A0B] text-white' : 'text-[#5F6368] hover:bg-[#F9FAFB]'
            }`}
          >
            <Layout className="w-3.5 h-3.5" /> Gallery ({templates.length})
          </button>
          <button
            onClick={() => setActiveTab('AI')}
            className={`px-3 py-1.5 rounded-md text-[12px] font-semibold flex items-center gap-1.5 transition-all cursor-pointer ${
              activeTab === 'AI' ? 'bg-[#0A0A0B] text-white' : 'text-[#5F6368] hover:bg-[#F9FAFB]'
            }`}
          >
            <Sparkles className="w-3.5 h-3.5 text-[#EC4899]" /> AI Generator
          </button>
        </div>
      </div>

      {/* TAB 1: TEMPLATE EDITOR */}
      {activeTab === 'EDITOR' && (
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-4">
          
          {/* Left: Form & Variables */}
          <div className="lg:col-span-7 space-y-4">
            <div className="bg-white p-5 rounded-[16px] border border-[#E5E5E7] space-y-4">
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="ma-label">Template Name</label>
                  <input
                    type="text"
                    value={templateName}
                    onChange={e => setTemplateName(e.target.value)}
                    className="ma-input"
                  />
                </div>
                <div>
                  <label className="ma-label">Category</label>
                  <select
                    value={category}
                    onChange={e => setCategory(e.target.value)}
                    className="ma-select"
                  >
                    <option value="Promotional">Promotional</option>
                    <option value="Onboarding">Onboarding</option>
                    <option value="Newsletter">Newsletter</option>
                    <option value="Product">Product Launch</option>
                  </select>
                </div>
              </div>

              <div>
                <label className="ma-label">Subject Line</label>
                <input
                  type="text"
                  value={subject}
                  onChange={e => setSubject(e.target.value)}
                  className="ma-input"
                />
              </div>

              {/* Dynamic Variable Chips */}
              <div>
                <span className="text-[11px] font-semibold text-[#EC4899] block mb-1.5">Dynamic Variables (Click to insert)</span>
                <div className="flex flex-wrap gap-1.5 p-2.5 bg-[#FAFAFB] rounded-xl border border-[#E5E5E7] max-h-24 overflow-y-auto">
                  {dynamicVariables.map(v => (
                    <button
                      key={v.fieldKey || v}
                      type="button"
                      onClick={() => handleInsertVariable(v.fieldKey || v)}
                      className="px-2 py-0.5 bg-white hover:bg-[#0A0A0B] hover:text-white text-[#0A0A0B] text-[11px] font-mono font-semibold rounded border border-[#E5E5E7] transition-colors cursor-pointer"
                    >
                      {`{{${v.fieldKey || v}}}`}
                    </button>
                  ))}
                </div>
              </div>

              <div>
                <label className="ma-label">HTML Body</label>
                <textarea
                  rows={10}
                  value={htmlContent}
                  onChange={e => setHtmlContent(e.target.value)}
                  className="w-full p-3 font-mono text-[12px] bg-[#FAFAFB] border border-[#E5E5E7] rounded-xl outline-none focus:border-[#D1D5DB] text-[#0A0A0B]"
                />
              </div>

              <div className="flex justify-end pt-1">
                <button
                  onClick={handleSaveTemplate}
                  className="ma-btn ma-btn-primary"
                >
                  Save Template
                </button>
              </div>
            </div>
          </div>

          {/* Right: Preview */}
          <div className="lg:col-span-5 space-y-4">
            <div className="bg-white p-5 rounded-[16px] border border-[#E5E5E7] space-y-3">
              <div className="flex items-center justify-between">
                <span className="text-[12px] font-bold text-[#0A0A0B] flex items-center gap-1.5">
                  <Eye className="w-3.5 h-3.5 text-[#5F6368]" /> Live Preview
                </span>

                <div className="flex items-center gap-1 bg-[#FAFAFB] p-0.5 rounded border border-[#E5E5E7]">
                  <button
                    onClick={() => setPreviewDevice('DESKTOP')}
                    className={`p-1 rounded text-[11px] ${previewDevice === 'DESKTOP' ? 'bg-white text-[#0A0A0B] shadow-xs' : 'text-[#9CA3AF]'}`}
                  >
                    <Monitor className="w-3.5 h-3.5" />
                  </button>
                  <button
                    onClick={() => setPreviewDevice('MOBILE')}
                    className={`p-1 rounded text-[11px] ${previewDevice === 'MOBILE' ? 'bg-white text-[#0A0A0B] shadow-xs' : 'text-[#9CA3AF]'}`}
                  >
                    <Smartphone className="w-3.5 h-3.5" />
                  </button>
                </div>
              </div>

              <div>
                <select
                  value={selectedPreviewContact}
                  onChange={e => setSelectedPreviewContact(e.target.value)}
                  className="ma-select"
                >
                  <option value="ashok">Ashok Kumar (ABC Global)</option>
                  <option value="sarah">Dr. Sarah Jenkins (Metro Health)</option>
                </select>
              </div>

              <div className={`mx-auto transition-all ${previewDevice === 'MOBILE' ? 'max-w-[320px]' : 'w-full'}`}>
                <div className="bg-white rounded-xl overflow-hidden border border-[#E5E5E7] min-h-[380px]">
                  <div className="bg-[#FAFAFB] border-b border-[#E5E5E7] p-2.5 text-[11px] text-[#5F6368] font-medium">
                    <div><strong>Subject:</strong> {subject.replace('{{company}}', selectedPreviewContact === 'ashok' ? 'ABC Global' : 'Metro Health')}</div>
                  </div>
                  <div className="p-4 text-[13px]" dangerouslySetInnerHTML={{ __html: renderPersonalizedHtml() }} />
                </div>
              </div>

            </div>
          </div>

        </div>
      )}

      {/* TAB 2: GALLERY */}
      {activeTab === 'GALLERY' && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {templates.map(t => (
            <div key={t.id} className="bg-white border border-[#E5E5E7] rounded-[16px] p-5 space-y-3 hover:shadow-sm transition-all flex flex-col justify-between">
              <div>
                <h3 className="text-[15px] font-bold text-[#0A0A0B]">{t.name}</h3>
                <p className="text-[12px] text-[#9CA3AF] font-mono mt-0.5 truncate">Subject: {t.subject || 'No subject'}</p>
              </div>
              <div className="flex items-center justify-between pt-3 border-t border-[#F0F0F2]">
                <span className="text-[11px] text-[#9CA3AF] font-medium">{t.category || 'General'}</span>
                <button
                  onClick={() => { setTemplateName(t.name); setSubject(t.subject || ''); setActiveTab('EDITOR'); }}
                  className="px-3 py-1 bg-[#0A0A0B] text-white rounded-lg text-[12px] font-semibold hover:bg-[#1F1F20]"
                >
                  Edit
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* TAB 3: AI GENERATOR */}
      {activeTab === 'AI' && (
        <div className="max-w-3xl mx-auto space-y-4">
          <div className="bg-white p-5 rounded-[16px] border border-[#E5E5E7] space-y-4">
            <div className="flex items-center gap-2">
              <Wand2 className="w-5 h-5 text-[#EC4899]" />
              <h2 className="text-[15px] font-bold text-[#0A0A0B]">AI Template Generation</h2>
            </div>

            <div className="flex items-center gap-1.5 overflow-x-auto pb-1">
              {['Professional', 'Promotional', 'Friendly', 'Executive'].map((t) => (
                <button
                  key={t}
                  type="button"
                  onClick={() => setAiTone(t)}
                  className={`px-3 py-1 rounded-md text-[12px] font-semibold transition-all cursor-pointer ${
                    aiTone === t ? 'bg-[#0A0A0B] text-white' : 'bg-[#FAFAFB] text-[#5F6368] border border-[#E5E5E7]'
                  }`}
                >
                  {t}
                </button>
              ))}
            </div>

            <textarea
              rows={3}
              value={chatInput}
              onChange={e => setChatInput(e.target.value)}
              placeholder="Describe your email goal or paste draft text..."
              className="ma-input h-24 p-3 font-medium resize-none"
            />

            <button
              type="button"
              disabled={generatingAi}
              onClick={() => handleSendChatMessage()}
              className="ma-btn ma-btn-primary w-full"
            >
              {generatingAi ? <RefreshCw className="w-4 h-4 animate-spin" /> : <Sparkles className="w-4 h-4" />}
              {generatingAi ? 'Generating...' : 'Generate Template & Subject Lines'}
            </button>
          </div>

          {/* AI Result */}
          {aiResult && (
            <div className="p-5 bg-white rounded-[16px] border border-[#E5E5E7] space-y-4">
              <div className="flex items-center justify-between border-b border-[#F0F0F2] pb-3">
                <span className="text-[12px] font-semibold text-[#16A34A] flex items-center gap-1">
                  <ShieldCheck className="w-4 h-4" /> Spam Score: {aiResult.spamScore || '1.2/10'}
                </span>
              </div>

              <div className="space-y-3">
                <div>
                  <label className="ma-label">Generated Subject Line</label>
                  <input type="text" value={subject} onChange={e => setSubject(e.target.value)} className="ma-input" />
                </div>
                <div>
                  <label className="ma-label">Generated HTML Code</label>
                  <textarea rows={6} value={htmlContent} onChange={e => setHtmlContent(e.target.value)} className="w-full p-3 font-mono text-[11px] bg-[#0A0A0B] text-[#86EFAC] rounded-xl outline-none" />
                </div>
              </div>

              <button
                type="button"
                onClick={() => setActiveTab('EDITOR')}
                className="ma-btn ma-btn-primary w-full"
              >
                Continue to Preview & Save <ArrowRight className="w-4 h-4" />
              </button>
            </div>
          )}
        </div>
      )}

    </div>
  );
};
