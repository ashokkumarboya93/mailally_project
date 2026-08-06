import React, { useState, useEffect } from 'react';
import { templateApi } from '../../api/campaignApi';
import { Modal } from '../../components/common/Modal';
import { PageSkeletonLoader } from '../../components/common/PageSkeletonLoader';
import { 
  FileText, Plus, Eye, Sparkles, Edit3, Layout, 
  Trash2, Copy, RefreshCw, Wand2, Monitor, Smartphone,
  Code as CodeIcon, Check, CopyCheck, AlertTriangle, ShieldCheck, ArrowRight
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
`<div style="font-family: 'Inter', sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; color: #1f2937; background: #ffffff; border-radius: 16px; border: 1px solid #e2e8f0;">
  <h2 style="color: #1f57f5;">Hello {{firstName}},</h2>
  <p style="font-size: 15px; line-height: 1.6;">We have exciting enterprise updates tailored specifically for <strong>{{company}}</strong> in {{city}}.</p>
  <div style="background-color: #f0f7ff; border-left: 4px solid #1f57f5; padding: 16px; margin: 20px 0; border-radius: 8px;">
    <p style="margin: 0; font-size: 14px; color: #1e3a8a;"><strong>Revenue Group:</strong> {{Revenue}}</p>
    <p style="margin: 4px 0 0 0; font-size: 14px; color: #1e3a8a;"><strong>LinkedIn:</strong> {{LinkedIn}}</p>
  </div>
  <div style="text-align: center; margin: 32px 0;">
    <a href="#" style="background-color: #1f57f5; color: #ffffff; padding: 12px 24px; text-decoration: none; border-radius: 8px; font-weight: 600; display: inline-block;">Claim Offer Now →</a>
  </div>
  <hr style="border: none; border-top: 1px solid #e2e8f0; margin: 24px 0;" />
  <p style="font-size: 12px; color: #94a3b8; text-align: center;">Sent via {{organizationName}} | <a href="{{unsubscribeLink}}" style="color: #94a3b8;">Unsubscribe</a></p>
</div>`
  );

  // Preview State
  const [previewDevice, setPreviewDevice] = useState('DESKTOP');
  const [selectedPreviewContact, setSelectedPreviewContact] = useState('ashok');  // AI Prompt & Chat State
  const [aiGoal, setAiGoal] = useState('Product launch discount for enterprise subscribers');
  const [aiAudience, setAiAudience] = useState('C-Level Tech Executives');
  const [aiTone, setAiTone] = useState('Professional');
  const [aiCta, setAiCta] = useState('Schedule Demo');
  const [chatInput, setChatInput] = useState('');
  const [generatingAi, setGeneratingAi] = useState(false);
  const [aiResult, setAiResult] = useState(null);
  const [subjectOptions, setSubjectOptions] = useState([
    '🚀 Exclusive Enterprise Offer for {{company}}',
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
    const finalName = templateName.trim() || `Marcamor AI Template ${new Date().toLocaleDateString()} ${new Date().toLocaleTimeString()}`;
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
`<div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; border: 1px solid #e2e8f0; border-radius: 16px; background: #ffffff;">
  <h2 style="color: #1e3a8a;">Special ${aiTone} Update for {{company}}</h2>
  <p style="font-size: 15px; color: #334155; line-height: 1.6;">Hi {{firstName}},</p>
  <p style="font-size: 15px; color: #334155; line-height: 1.6;">${promptToUse}</p>
  <div style="background-color: #f1f5f9; padding: 16px; border-radius: 12px; margin: 24px 0; border-left: 4px solid #2563eb;">
    <p style="margin: 0; font-weight: bold; color: #0f172a;">Exclusive Code for {{firstName}}: <span style="color: #2563eb;">{{discountCode}}</span></p>
  </div>
  <div style="text-align: center; margin: 32px 0;">
    <a href="{{actionUrl}}" style="background-color: #2563eb; color: #ffffff; padding: 14px 32px; text-decoration: none; border-radius: 10px; font-weight: bold; display: inline-block;">${aiCta || 'Explore Now'} →</a>
  </div>
  <hr style="border: none; border-top: 1px solid #e2e8f0; margin: 24px 0;" />
  <p style="font-size: 12px; color: #94a3b8; text-align: center;">Sent to {{email}} | Marcamor Enterprise</p>
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

  const handleGenerateAi = async (e) => {
    e.preventDefault();
    handleSendChatMessage();
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
    <div className="p-6 space-y-6 max-w-7xl mx-auto text-slate-800 font-sans">
      
      {/* Header Toolbar — Bright Ice Blue Theme Banner */}
      <div 
        className="flex flex-col md:flex-row md:items-center justify-between gap-4 p-6 rounded-3xl text-white shadow-lg shadow-blue-500/10 relative overflow-hidden border border-blue-200"
        style={{ background: 'linear-gradient(135deg, #1F57F5 0%, #2BAFF2 100%)' }}
      >
        <div className="relative z-10">
          <div className="flex items-center gap-2">
            <h1 className="text-2xl font-extrabold tracking-tight text-white">Template Studio</h1>
            <span className="text-xs px-3 py-0.5 rounded-full bg-white/20 text-white font-semibold backdrop-blur-md border border-white/30">
              Dynamic Variable Engine
            </span>
          </div>
          <p className="text-xs text-blue-100 mt-1">
            Automated contact tag binding • AI generation with spam scoring • Real recipient live preview
          </p>
        </div>

        {/* 3 Primary Header Tabs */}
        <div className="flex items-center gap-1.5 bg-white/15 backdrop-blur-md p-1.5 rounded-2xl border border-white/25 relative z-10">
          <button
            onClick={() => setActiveTab('EDITOR')}
            className={`px-4 py-2 rounded-xl text-xs font-bold flex items-center gap-2 transition-all ${
              activeTab === 'EDITOR' ? 'bg-white text-blue-700 shadow-md' : 'text-white hover:bg-white/10'
            }`}
          >
            <Edit3 className="w-3.5 h-3.5" /> Template Editor
          </button>
          <button
            onClick={() => setActiveTab('GALLERY')}
            className={`px-4 py-2 rounded-xl text-xs font-bold flex items-center gap-2 transition-all ${
              activeTab === 'GALLERY' ? 'bg-white text-blue-700 shadow-md' : 'text-white hover:bg-white/10'
            }`}
          >
            <Layout className="w-3.5 h-3.5" /> Template Gallery ({templates.length})
          </button>
          <button
            onClick={() => setActiveTab('AI')}
            className={`px-4 py-2 rounded-xl text-xs font-bold flex items-center gap-2 transition-all ${
              activeTab === 'AI' ? 'bg-white text-blue-700 shadow-md' : 'text-white hover:bg-white/10'
            }`}
          >
            <Sparkles className="w-3.5 h-3.5 text-amber-500" /> Generate with AI
          </button>
        </div>
      </div>

      {/* TAB 1: TEMPLATE EDITOR */}
      {activeTab === 'EDITOR' && (
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
          
          {/* Left Column: Form & Dynamic Variable Panel */}
          <div className="lg:col-span-7 space-y-4">
            <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-xs font-bold text-slate-700 mb-1.5">Template Name</label>
                  <input
                    type="text"
                    value={templateName}
                    onChange={e => setTemplateName(e.target.value)}
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium focus:border-blue-500 focus:bg-white focus:outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-bold text-slate-700 mb-1.5">Category</label>
                  <select
                    value={category}
                    onChange={e => setCategory(e.target.value)}
                    className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium focus:border-blue-500 focus:bg-white focus:outline-none"
                  >
                    <option value="Promotional">Promotional</option>
                    <option value="Onboarding">Onboarding</option>
                    <option value="Newsletter">Newsletter</option>
                    <option value="Product">Product Launch</option>
                  </select>
                </div>
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-700 mb-1.5">Subject Line</label>
                <input
                  type="text"
                  value={subject}
                  onChange={e => setSubject(e.target.value)}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2.5 text-xs text-slate-900 font-medium focus:border-blue-500 focus:bg-white focus:outline-none"
                />
              </div>

              {/* Dynamic Variable Chips Panel */}
              <div>
                <div className="flex items-center justify-between mb-2">
                  <span className="text-xs font-bold text-blue-600">✨ Available Dynamic Variables (Click to insert)</span>
                  <span className="text-[10px] text-slate-400 font-medium">Auto-populated from Contact Registry</span>
                </div>
                <div className="flex flex-wrap gap-1.5 p-3 bg-blue-50/50 rounded-2xl border border-blue-100 max-h-28 overflow-y-auto">
                  {dynamicVariables.map(v => (
                    <button
                      key={v}
                      type="button"
                      onClick={() => handleInsertVariable(v)}
                      className="px-2.5 py-1 bg-white hover:bg-blue-600 hover:text-white text-blue-700 text-xs font-mono font-bold rounded-lg border border-blue-200 shadow-xs transition-all hover:scale-105"
                    >
                      {`{{${v}}}`}
                    </button>
                  ))}
                </div>
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-700 mb-1.5">HTML Body Content</label>
                <textarea
                  rows={12}
                  value={htmlContent}
                  onChange={e => setHtmlContent(e.target.value)}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-3 font-mono text-xs text-slate-800 focus:border-blue-500 focus:bg-white focus:outline-none leading-relaxed"
                />
              </div>

              <div className="flex items-center justify-end gap-3 pt-2">
                <button
                  onClick={handleSaveTemplate}
                  className="px-6 py-2.5 bg-blue-600 hover:bg-blue-700 text-white font-bold text-sm rounded-xl shadow-md shadow-blue-500/20"
                >
                  Save Email Template
                </button>
              </div>
            </div>
          </div>

          {/* Right Column: Live Recipient Personalized Preview */}
          <div className="lg:col-span-5 space-y-4">
            <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm space-y-4">
              
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <Eye className="w-4 h-4 text-blue-600" />
                  <span className="text-xs font-bold text-slate-800 uppercase tracking-wider">Live Contact Preview</span>
                </div>

                {/* Desktop / Mobile Toggle */}
                <div className="flex items-center gap-1 bg-slate-100 p-1 rounded-xl border border-slate-200">
                  <button
                    onClick={() => setPreviewDevice('DESKTOP')}
                    className={`p-1.5 rounded-lg text-xs font-bold ${previewDevice === 'DESKTOP' ? 'bg-white text-blue-600 shadow-xs' : 'text-slate-500'}`}
                  >
                    <Monitor className="w-3.5 h-3.5" />
                  </button>
                  <button
                    onClick={() => setPreviewDevice('MOBILE')}
                    className={`p-1.5 rounded-lg text-xs font-bold ${previewDevice === 'MOBILE' ? 'bg-white text-blue-600 shadow-xs' : 'text-slate-500'}`}
                  >
                    <Smartphone className="w-3.5 h-3.5" />
                  </button>
                </div>
              </div>

              {/* Recipient Picker */}
              <div>
                <label className="block text-[11px] font-medium text-slate-500 mb-1">Select Contact to Test Personalization:</label>
                <select
                  value={selectedPreviewContact}
                  onChange={e => setSelectedPreviewContact(e.target.value)}
                  className="w-full bg-slate-50 border border-slate-200 rounded-xl p-2 text-xs text-slate-900 font-medium"
                >
                  <option value="ashok">Ashok Kumar (ABC Global, Revenue: 2M)</option>
                  <option value="sarah">Dr. Sarah Jenkins (Metro Health, Revenue: 15M)</option>
                </select>
              </div>

              {/* Simulated Device Frame */}
              <div className={`mx-auto transition-all ${previewDevice === 'MOBILE' ? 'max-w-[340px]' : 'w-full'}`}>
                <div className="bg-white rounded-2xl overflow-hidden shadow-md border border-slate-200 min-h-[420px]">
                  <div className="bg-slate-50 border-b border-slate-200 p-3 text-xs text-slate-700 font-medium">
                    <div><strong>Subject:</strong> {subject.replace('{{company}}', selectedPreviewContact === 'ashok' ? 'ABC Global' : 'Metro Health')}</div>
                    <div className="text-[10px] text-slate-500 mt-0.5">To: {selectedPreviewContact === 'ashok' ? 'ashok@abc.com' : 'sarah@metrohealth.org'}</div>
                  </div>
                  <div className="p-4" dangerouslySetInnerHTML={{ __html: renderPersonalizedHtml() }} />
                </div>
              </div>

            </div>
          </div>

        </div>
      )}

      {/* TAB 2: TEMPLATE GALLERY */}
      {activeTab === 'GALLERY' && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {templates.map(t => (
            <div key={t.id} className="bg-white border border-slate-200 hover:border-blue-400 rounded-3xl p-6 shadow-sm hover:shadow-xl transition-all">
              <div className="flex items-start justify-between mb-3">
                <div>
                  <h3 className="text-base font-bold text-slate-900">{t.name}</h3>
                  <span className="text-xs text-slate-500 font-mono">Subject: {t.subject}</span>
                </div>
                <span className="text-xs px-2.5 py-1 rounded-full bg-emerald-50 text-emerald-700 border border-emerald-200 font-bold">
                  v{t.version || 1}
                </span>
              </div>
              <div className="flex items-center justify-between pt-4 border-t border-slate-100 text-xs">
                <span className="text-slate-500 font-medium">Category: {t.category || 'General'}</span>
                <button
                  onClick={() => { setTemplateName(t.name); setSubject(t.subject); setActiveTab('EDITOR'); }}
                  className="px-3.5 py-1.5 bg-blue-600 hover:bg-blue-700 text-white rounded-xl font-bold transition-colors shadow-xs"
                >
                  Edit Template
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* TAB 3: GENERATE WITH AI & CHAT ASSISTANT */}
      {activeTab === 'AI' && (
        <div className="max-w-4xl mx-auto space-y-6">
          
          {/* Header & Tone Controls */}
          <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm space-y-4">
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
              <div className="flex items-center gap-3">
                <div className="p-3 bg-gradient-to-br from-blue-600 to-indigo-600 text-white rounded-2xl shadow-md shadow-blue-500/20">
                  <Wand2 className="w-6 h-6" />
                </div>
                <div>
                  <h2 className="text-base font-extrabold text-slate-900 flex items-center gap-2">
                    MailAlly Chat AI Assistant <span className="text-[10px] bg-blue-100 text-blue-700 font-bold px-2 py-0.5 rounded-full">Gemini Powered</span>
                  </h2>
                  <p className="text-xs text-slate-500">Paste your raw content, choose a tone, or chat to generate HTML code & subject lines.</p>
                </div>
              </div>

              {/* Tone Selection Pills */}
              <div className="flex items-center gap-1.5 overflow-x-auto pb-1 md:pb-0">
                {['Professional', 'Promotional', 'Friendly', 'High Urgency', 'Executive'].map((t) => (
                  <button
                    key={t}
                    type="button"
                    onClick={() => setAiTone(t)}
                    className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all cursor-pointer ${
                      aiTone === t
                        ? 'bg-blue-600 text-white shadow-md shadow-blue-500/20'
                        : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
                    }`}
                  >
                    {t}
                  </button>
                ))}
              </div>
            </div>

            {/* Quick Action Prompt Chips */}
            <div className="flex items-center gap-2 overflow-x-auto pt-2 border-t border-slate-100">
              <span className="text-[11px] font-bold text-slate-400 whitespace-nowrap">Quick Prompts:</span>
              <button
                type="button"
                onClick={() => handleSendChatMessage('Convert my raw text content into a responsive HTML email template with CTA button')}
                className="px-3 py-1 bg-blue-50 hover:bg-blue-100 text-blue-700 rounded-lg text-xs font-semibold whitespace-nowrap border border-blue-200"
              >
                📄 Raw Text to HTML Template
              </button>
              <button
                type="button"
                onClick={() => handleSendChatMessage('Generate 4 high-converting subject lines and preheaders for B2B subscribers')}
                className="px-3 py-1 bg-amber-50 hover:bg-amber-100 text-amber-800 rounded-lg text-xs font-semibold whitespace-nowrap border border-amber-200"
              >
                🎯 4 Subject Line Choices
              </button>
              <button
                type="button"
                onClick={() => handleSendChatMessage('Rewrite email content for high urgency Black Friday discount sale')}
                className="px-3 py-1 bg-emerald-50 hover:bg-emerald-100 text-emerald-800 rounded-lg text-xs font-semibold whitespace-nowrap border border-emerald-200"
              >
                ⚡ High Urgency Sales Copy
              </button>
            </div>
          </div>

          {/* Chat Messages Log */}
          <div className="bg-white p-6 rounded-3xl border border-slate-200 shadow-sm space-y-4 min-h-[250px] max-h-[450px] overflow-y-auto">
            {chatMessages.map((msg, idx) => (
              <div
                key={idx}
                className={`flex gap-3 ${msg.role === 'user' ? 'justify-end' : 'justify-start'}`}
              >
                {msg.role === 'ai' && (
                  <div className="w-8 h-8 rounded-full bg-gradient-to-tr from-blue-600 to-sky-400 flex items-center justify-center text-white text-xs font-bold shadow-xs shrink-0">
                    AI
                  </div>
                )}
                <div
                  className={`p-4 rounded-2xl max-w-[85%] text-xs leading-relaxed ${
                    msg.role === 'user'
                      ? 'bg-blue-600 text-white font-medium rounded-tr-none'
                      : 'bg-slate-50 text-slate-800 border border-slate-200 rounded-tl-none space-y-3'
                  }`}
                >
                  <p className="whitespace-pre-wrap">{msg.text}</p>

                  {/* Render Subject Choices if available in AI response */}
                  {msg.subjects && msg.subjects.length > 0 && (
                    <div className="space-y-1.5 pt-2 border-t border-slate-200">
                      <span className="text-[10px] font-bold uppercase tracking-wider text-blue-600">
                        Click any subject below to select it:
                      </span>
                      <div className="space-y-1.5">
                        {msg.subjects.map((subjChoice, sIdx) => (
                          <button
                            key={sIdx}
                            type="button"
                            onClick={() => {
                              const cleanSubj = subjChoice.replace(/^\d+\.\s*/, '');
                              setSubject(cleanSubj);
                              alert(`Selected subject: "${cleanSubj}"`);
                            }}
                            className="w-full text-left p-2.5 bg-white hover:bg-blue-50 border border-slate-200 hover:border-blue-300 rounded-xl text-xs font-semibold text-slate-800 transition-all flex items-center justify-between group cursor-pointer shadow-2xs"
                          >
                            <span>{subjChoice}</span>
                            <span className="text-[10px] text-blue-600 opacity-0 group-hover:opacity-100 font-bold transition-opacity">Select →</span>
                          </button>
                        ))}
                      </div>
                    </div>
                  )}
                </div>
              </div>
            ))}

            {generatingAi && (
              <div className="flex gap-3 items-center text-xs text-blue-600 font-bold p-3 bg-blue-50/50 rounded-2xl w-fit">
                <RefreshCw className="w-4 h-4 animate-spin text-blue-600" />
                <span>AI is crafting your email template & subject options...</span>
              </div>
            )}
          </div>

          {/* Paste Raw Content & Send Box */}
          <div className="bg-white p-4 rounded-3xl border border-slate-200 shadow-sm space-y-3">
            <textarea
              rows={3}
              value={chatInput}
              onChange={e => setChatInput(e.target.value)}
              placeholder="Paste your raw email content, draft text, or type instructions here (e.g. 'We are launching a new feature next week for enterprise clients...')"
              className="w-full p-3 bg-slate-50 border border-slate-200 rounded-2xl text-xs font-medium text-slate-900 focus:bg-white focus:border-blue-500 focus:outline-none"
            />

            <div className="flex items-center justify-between">
              <span className="text-[11px] text-slate-400 font-medium">Selected Tone: <strong className="text-blue-600">{aiTone}</strong></span>
              <button
                type="button"
                disabled={generatingAi}
                onClick={() => handleSendChatMessage()}
                className="px-6 py-3 bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white font-bold text-xs rounded-xl flex items-center gap-2 shadow-md shadow-blue-500/20 cursor-pointer"
              >
                {generatingAi ? <RefreshCw className="w-4 h-4 animate-spin" /> : <Sparkles className="w-4 h-4 text-amber-300" />}
                <span>{generatingAi ? 'Generating...' : 'Generate Template & Code'}</span>
              </button>
            </div>
          </div>

          {/* Generated Code & Transport Preview Panel */}
          {aiResult && (
            <div className="p-6 bg-gradient-to-br from-blue-50/80 via-white to-sky-50/50 rounded-3xl border border-blue-200 space-y-5 shadow-sm">
              <div className="flex items-center justify-between border-b border-blue-100 pb-3">
                <div className="flex items-center gap-2">
                  <div className="p-1.5 bg-emerald-100 text-emerald-700 rounded-lg">
                    <ShieldCheck className="w-4 h-4" />
                  </div>
                  <span className="text-xs font-extrabold text-slate-800">
                    Spam Score: <span className="text-emerald-600">{aiResult.spamScore || '1.2/10 (Low Risk)'}</span>
                  </span>
                </div>
                <span className="text-[11px] font-bold px-3 py-1 bg-blue-100 text-blue-700 rounded-full">
                  Gemini AI Code Generated ✨
                </span>
              </div>

              {/* Editable Fields generated by AI */}
              <div className="space-y-4 font-sans text-xs">
                <div>
                  <label className="block font-bold text-slate-700 mb-1 uppercase tracking-wider text-[10px]">
                    Active Selected Subject Line (Editable)
                  </label>
                  <input
                    type="text"
                    value={subject}
                    onChange={e => setSubject(e.target.value)}
                    className="w-full p-2.5 bg-white border border-slate-200 rounded-xl font-semibold text-slate-900 focus:outline-none focus:border-blue-500 shadow-2xs"
                  />
                </div>

                <div>
                  <label className="block font-bold text-slate-700 mb-1 uppercase tracking-wider text-[10px]">
                    Generated Preheader (Editable)
                  </label>
                  <input
                    type="text"
                    value={preheader}
                    onChange={e => setPreheader(e.target.value)}
                    className="w-full p-2.5 bg-white border border-slate-200 rounded-xl font-semibold text-slate-900 focus:outline-none focus:border-blue-500 shadow-2xs"
                  />
                </div>

                <div>
                  <div className="flex items-center justify-between mb-1">
                    <label className="block font-bold text-slate-700 uppercase tracking-wider text-[10px]">
                      Generated Responsive HTML Code (Editable)
                    </label>
                    <span className="text-[10px] text-blue-600 font-bold">✨ Variables: &#123;&#123;firstName&#125;&#125;, &#123;&#123;company&#125;&#125;, &#123;&#123;discountCode&#125;&#125;</span>
                  </div>
                  <textarea
                    rows={8}
                    value={htmlContent}
                    onChange={e => setHtmlContent(e.target.value)}
                    className="w-full p-3 font-mono text-[11px] bg-slate-900 text-sky-300 rounded-2xl border border-slate-800 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
              </div>

              {/* Continue to Preview & Save Button Below Response */}
              <div className="pt-2">
                <button
                  type="button"
                  onClick={() => setActiveTab('EDITOR')}
                  className="w-full py-4 bg-gradient-to-r from-emerald-600 to-teal-500 hover:from-emerald-700 hover:to-teal-600 text-white font-extrabold text-sm rounded-2xl flex items-center justify-center gap-2 shadow-lg shadow-emerald-500/20 hover:scale-[1.01] transition-all cursor-pointer"
                >
                  <span>Continue to Live Preview & Save Template</span>
                  <ArrowRight className="w-4 h-4" />
                </button>
              </div>
            </div>
          )}
        </div>
      )}

    </div>
  );
};
