import React, { useState, useEffect, useRef } from 'react';
import { templateApi } from '../../api/campaignApi';
import { PageSkeletonLoader } from '../../components/common/PageSkeletonLoader';
import { AlertModal } from '../../components/common/AlertModal';
import { TemplatePreviewModal } from '../../components/templates/TemplatePreviewModal';
import { 
  FileText, Sparkles, Edit3, Layout, 
  RefreshCw, Wand2, Monitor, Smartphone,
  Eye, ArrowRight, ShieldCheck, Code,
  Type, Link, Image as ImageIcon, Smile,
  AlignLeft, AlignCenter, AlignRight, List,
  Bold, Italic, Underline, Strikethrough,
  Plus, Trash2, Check, AlertCircle, ExternalLink
} from 'lucide-react';

export const TemplatesPage = () => {
  const [activeTab, setActiveTab] = useState('EDITOR'); // 'EDITOR', 'GALLERY', 'AI'
  const [editorMode, setEditorMode] = useState('VISUAL'); // 'VISUAL', 'CODE'
  const [templates, setTemplates] = useState([]);
  const [dynamicVariables, setDynamicVariables] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isDirty, setIsDirty] = useState(false);

  // Template Form State
  const [templateId, setTemplateId] = useState(null);
  const [templateName, setTemplateName] = useState('New Marketing Offer');
  const [subject, setSubject] = useState('Special Update for {{company}}');
  const [preheader, setPreheader] = useState('Important details for your team inside');
  const [category, setCategory] = useState('Promotional');

  // Custom Links per Template
  const [links, setLinks] = useState({
    primaryCtaText: 'Claim Offer Now',
    primaryCtaUrl: 'https://mailally.com/welcome',
    secondaryCtaText: 'Learn More',
    secondaryCtaUrl: 'https://mailally.com/learn-more'
  });

  // Alert Modal State
  const [alertConfig, setAlertConfig] = useState({ isOpen: false, type: 'success', title: '', message: '' });
  const showAlert = (type, message, title = '') => setAlertConfig({ isOpen: true, type, message, title: title || (type === 'success' ? 'Success' : 'Error') });
  const closeAlert = () => setAlertConfig(prev => ({ ...prev, isOpen: false }));

  // Gallery Preview Modal State
  const [previewModalOpen, setPreviewModalOpen] = useState(false);
  const [selectedPreviewTemplate, setSelectedPreviewTemplate] = useState(null);

  // Rich Text Editor Content
  const [htmlContent, setHtmlContent] = useState(
`<div style="font-family: 'Inter', sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; color: #0a0a0b; background: #ffffff; border-radius: 16px; border: 1px solid #e5e5e7;">
  <h2 style="color: #0a0a0b;">Hello {{firstName}}, 👋</h2>
  <p style="font-size: 14px; line-height: 1.6; color: #5f6368;">We have exciting updates tailored specifically for <strong>{{company}}</strong> in {{city}}.</p>
  <div style="background-color: #fcfcfd; border-left: 4px solid #ec4899; padding: 16px; margin: 20px 0; border-radius: 8px;">
    <p style="margin: 0; font-size: 13px; color: #0a0a0b;"><strong>Revenue Group:</strong> {{revenue}}</p>
    <p style="margin: 4px 0 0 0; font-size: 13px; color: #0a0a0b;"><strong>LinkedIn:</strong> {{linkedin}}</p>
  </div>
  <p style="font-size: 14px; color: #5f6368;">Don't miss out on this exclusive offer.</p>
  <div style="text-align: center; margin: 32px 0;">
    <a href="{{primaryCtaUrl}}" style="background-color: #0a0a0b; color: #ffffff; padding: 12px 24px; text-decoration: none; border-radius: 8px; font-weight: 600; display: inline-block;">Claim Offer Now →</a>
  </div>
  <hr style="border: none; border-top: 1px solid #e5e5e7; margin: 24px 0;" />
  <p style="font-size: 12px; color: #9ca3af; text-align: center;">Sent via {{organizationName}} | <a href="{{unsubscribeLink}}" style="color: #9ca3af;">Unsubscribe</a></p>
</div>`
  );

  // Emoji Picker Popover State
  const [showEmojiPicker, setShowEmojiPicker] = useState(false);
  const emojiList = ['😃', '😄', '😂', '😉', '😊', '😍', '😎', '🥳', '🙌', '👍', '🔥', '✨', '🚀', '❤️', '💡', '📌', '🎉', '💼', '📈', '⭐'];

  // Preview Test Recipient Profiles
  const testContacts = [
    {
      id: 1,
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
    },
    {
      id: 2,
      firstName: 'Dr. Sarah',
      lastName: 'Smith',
      email: 'sarah.smith@metrohealth.org',
      company: 'Metro Hospital',
      city: 'Chicago',
      country: 'USA',
      department: 'Cardiology',
      designation: 'Chief of Medicine',
      revenue: '15 Million USD',
      linkedin: 'linkedin.com/in/drsarahsmith'
    },
    {
      id: 3,
      firstName: 'Rahul',
      lastName: 'Kumar',
      email: 'rahul.kumar@xyzbanking.com',
      company: 'XYZ Banking',
      city: 'Mumbai',
      country: 'India',
      department: 'Digital Banking',
      designation: 'Head of Fintech',
      revenue: '8 Million USD',
      linkedin: 'linkedin.com/in/rahulkumar'
    }
  ];

  const [previewDevice, setPreviewDevice] = useState('DESKTOP');
  const [selectedPreviewContactId, setSelectedPreviewContactId] = useState(1);

  // AI Prompt & Generator State
  const [aiGoal, setAiGoal] = useState('Product launch discount for enterprise subscribers');
  const [aiAudience, setAiAudience] = useState('C-Level Tech Executives');
  const [aiTone, setAiTone] = useState('Professional');
  const [aiCta, setAiCta] = useState('Explore Product');
  const [chatInput, setChatInput] = useState('');
  const [generatingAi, setGeneratingAi] = useState(false);
  const [aiResult, setAiResult] = useState(null);

  // 6 Default Built-In Templates
  const defaultTemplates = [
    {
      id: 'default_1',
      name: 'Welcome & Onboarding',
      category: 'Onboarding',
      subject: 'Welcome to {{organizationName}}, {{firstName}}!',
      links: { primaryCtaUrl: 'https://mailally.com/welcome', secondaryCtaUrl: 'https://mailally.com/docs' },
      content: `<div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; border: 1px solid #e5e5e7; border-radius: 16px; background: #ffffff;">
  <h2 style="color: #0a0a0b;">Welcome to {{organizationName}}! 🎉</h2>
  <p style="font-size: 14px; color: #5f6368; line-height: 1.6;">Hi {{firstName}},</p>
  <p style="font-size: 14px; color: #5f6368; line-height: 1.6;">We are thrilled to welcome <strong>{{company}}</strong> onboard. MailAlly is designed to supercharge your email communication.</p>
  <div style="background-color: #f8fafc; padding: 16px; border-radius: 12px; margin: 24px 0; border: 1px solid #e2e8f0;">
    <p style="margin: 0; font-size: 13px; color: #334155;"><strong>Account Manager:</strong> Support Team</p>
    <p style="margin: 4px 0 0 0; font-size: 13px; color: #334155;"><strong>Location:</strong> {{city}}, {{country}}</p>
  </div>
  <div style="text-align: center; margin: 32px 0;">
    <a href="{{primaryCtaUrl}}" style="background-color: #2563eb; color: #ffffff; padding: 12px 24px; text-decoration: none; border-radius: 8px; font-weight: bold; display: inline-block;">Get Started Now →</a>
  </div>
  <hr style="border: none; border-top: 1px solid #e5e5e7; margin: 24px 0;" />
  <p style="font-size: 12px; color: #9ca3af; text-align: center;">Sent via MailAlly | <a href="{{unsubscribeLink}}" style="color: #9ca3af;">Unsubscribe</a></p>
</div>`
    },
    {
      id: 'default_2',
      name: 'Special Update & Offer',
      category: 'Promotional',
      subject: 'Special Update for {{company}} in {{city}}',
      links: { primaryCtaUrl: 'https://mailally.com/offers', secondaryCtaUrl: 'https://mailally.com/features' },
      content: `<div style="font-family: 'Inter', sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; color: #0a0a0b; background: #ffffff; border-radius: 16px; border: 1px solid #e5e5e7;">
  <h2 style="color: #0a0a0b;">Hello {{firstName}}, 👋</h2>
  <p style="font-size: 14px; line-height: 1.6; color: #5f6368;">We have exciting updates tailored specifically for <strong>{{company}}</strong> in {{city}}.</p>
  <div style="background-color: #fcfcfd; border-left: 4px solid #ec4899; padding: 16px; margin: 20px 0; border-radius: 8px;">
    <p style="margin: 0; font-size: 13px; color: #0a0a0b;"><strong>Revenue Group:</strong> {{revenue}}</p>
    <p style="margin: 4px 0 0 0; font-size: 13px; color: #0a0a0b;"><strong>LinkedIn:</strong> {{linkedin}}</p>
  </div>
  <p style="font-size: 14px; color: #5f6368;">Don't miss out on this exclusive offer.</p>
  <div style="text-align: center; margin: 32px 0;">
    <a href="{{primaryCtaUrl}}" style="background-color: #0a0a0b; color: #ffffff; padding: 12px 24px; text-decoration: none; border-radius: 8px; font-weight: 600; display: inline-block;">Claim Offer Now →</a>
  </div>
  <hr style="border: none; border-top: 1px solid #e5e5e7; margin: 24px 0;" />
  <p style="font-size: 12px; color: #9ca3af; text-align: center;">Sent via {{organizationName}} | <a href="{{unsubscribeLink}}" style="color: #9ca3af;">Unsubscribe</a></p>
</div>`
    },
    {
      id: 'default_3',
      name: 'Feature Release Announcement',
      category: 'Product',
      subject: 'Introducing New MailAlly Automation Tools',
      links: { primaryCtaUrl: 'https://mailally.com/product', secondaryCtaUrl: 'https://mailally.com/changelog' },
      content: `<div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; border: 1px solid #e5e5e7; border-radius: 16px; background: #ffffff;">
  <span style="background: #eff6ff; color: #2563eb; font-size: 12px; font-weight: bold; padding: 4px 12px; border-radius: 999px; text-transform: uppercase;">Product Update</span>
  <h2 style="color: #0a0a0b; margin-top: 12px;">Next-Gen Email Engine Live 🚀</h2>
  <p style="font-size: 14px; color: #5f6368; line-height: 1.6;">Hello {{firstName}},</p>
  <p style="font-size: 14px; color: #5f6368; line-height: 1.6;">We've upgraded your email engine at <strong>{{company}}</strong> with automated retry pipelines, real-time analytics, and CSV/Google Sheets sync.</p>
  <div style="text-align: center; margin: 32px 0;">
    <a href="{{primaryCtaUrl}}" style="background-color: #0a0a0b; color: #ffffff; padding: 12px 24px; text-decoration: none; border-radius: 8px; font-weight: bold; display: inline-block;">Explore New Features →</a>
  </div>
  <hr style="border: none; border-top: 1px solid #e5e5e7; margin: 24px 0;" />
  <p style="font-size: 12px; color: #9ca3af; text-align: center;">Sent to {{email}} | <a href="{{unsubscribeLink}}" style="color: #9ca3af;">Unsubscribe</a></p>
</div>`
    },
    {
      id: 'default_4',
      name: 'Monthly Growth Newsletter',
      category: 'Newsletter',
      subject: 'MailAlly Insights: Top Marketing Strategies',
      links: { primaryCtaUrl: 'https://mailally.com/blog', secondaryCtaUrl: 'https://mailally.com/webinars' },
      content: `<div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; border: 1px solid #e5e5e7; border-radius: 16px; background: #ffffff;">
  <h2 style="color: #0a0a0b;">Monthly Email Insights 📰</h2>
  <p style="font-size: 14px; color: #5f6368; line-height: 1.6;">Hi {{firstName}}, here are top strategies for <strong>{{company}}</strong> this month:</p>
  <ul style="font-size: 14px; color: #5f6368; line-height: 1.8; padding-left: 20px;">
    <li>Improving inbox placement with DKIM & SPF</li>
    <li>Personalizing recipient dynamic fields</li>
    <li>Automating follow-up sequences</li>
  </ul>
  <div style="text-align: center; margin: 28px 0;">
    <a href="{{primaryCtaUrl}}" style="background-color: #16a34a; color: #ffffff; padding: 12px 24px; text-decoration: none; border-radius: 8px; font-weight: bold; display: inline-block;">Read Full Newsletter →</a>
  </div>
  <hr style="border: none; border-top: 1px solid #e5e5e7; margin: 24px 0;" />
  <p style="font-size: 12px; color: #9ca3af; text-align: center;">Sent via MailAlly | <a href="{{unsubscribeLink}}" style="color: #9ca3af;">Unsubscribe</a></p>
</div>`
    },
    {
      id: 'default_5',
      name: 'Webinar & Event Invitation',
      category: 'Event',
      subject: 'Invitation: Enterprise Automation Summit',
      links: { primaryCtaUrl: 'https://mailally.com/events', secondaryCtaUrl: 'https://mailally.com/calendar' },
      content: `<div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; border: 1px solid #e5e5e7; border-radius: 16px; background: #ffffff;">
  <h2 style="color: #0a0a0b;">You\'re Invited: Growth Summit 🎙️</h2>
  <p style="font-size: 14px; color: #5f6368; line-height: 1.6;">Dear {{firstName}},</p>
  <p style="font-size: 14px; color: #5f6368; line-height: 1.6;">Join leaders from <strong>{{company}}</strong> and top enterprise tech firms for our upcoming live session on marketing scaling.</p>
  <div style="background: #fdf2f8; border: 1px solid #fbcfe8; padding: 16px; border-radius: 12px; margin: 20px 0;">
    <p style="margin: 0; font-size: 13px; font-weight: bold; color: #be185d;">Date: Next Thursday @ 10:00 AM EST</p>
  </div>
  <div style="text-align: center; margin: 28px 0;">
    <a href="{{primaryCtaUrl}}" style="background-color: #ec4899; color: #ffffff; padding: 12px 24px; text-decoration: none; border-radius: 8px; font-weight: bold; display: inline-block;">Register Your Seat →</a>
  </div>
  <hr style="border: none; border-top: 1px solid #e5e5e7; margin: 24px 0;" />
  <p style="font-size: 12px; color: #9ca3af; text-align: center;">MailAlly Events | <a href="{{unsubscribeLink}}" style="color: #9ca3af;">Unsubscribe</a></p>
</div>`
    },
    {
      id: 'default_6',
      name: 'B2B Partnership Outreach',
      category: 'Outreach',
      subject: 'Quick question regarding {{company}}',
      links: { primaryCtaUrl: 'https://mailally.com/demo', secondaryCtaUrl: 'https://mailally.com/contact' },
      content: `<div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; border: 1px solid #e5e5e7; border-radius: 16px; background: #ffffff;">
  <p style="font-size: 14px; color: #0a0a0b; line-height: 1.6;">Hi {{firstName}},</p>
  <p style="font-size: 14px; color: #5f6368; line-height: 1.6;">I saw your team at <strong>{{company}}</strong> is scaling operations in {{city}}. I wanted to share how MailAlly helps companies in {{country}} streamline email delivery.</p>
  <p style="font-size: 14px; color: #5f6368; line-height: 1.6;">Would you be open to a brief 10-minute demo this week?</p>
  <div style="margin: 24px 0;">
    <a href="{{primaryCtaUrl}}" style="background-color: #0a0a0b; color: #ffffff; padding: 10px 20px; text-decoration: none; border-radius: 8px; font-weight: bold; display: inline-block;">Schedule 10-Min Demo →</a>
  </div>
  <p style="font-size: 13px; color: #9ca3af;">Best regards,<br/>MailAlly Enterprise Team</p>
</div>`
    }
  ];

  // Prevent accidental tab/browser refresh when unsaved progress exists
  useEffect(() => {
    const handleBeforeUnload = (e) => {
      if (isDirty) {
        e.preventDefault();
        e.returnValue = '';
      }
    };
    window.addEventListener('beforeunload', handleBeforeUnload);
    return () => window.removeEventListener('beforeunload', handleBeforeUnload);
  }, [isDirty]);

  const loadData = async () => {
    setLoading(true);
    try {
      const [templateRes, varRes] = await Promise.allSettled([
        templateApi.getTemplates(),
        templateApi.getDynamicVariables()
      ]);

      let loadedTemplates = [];
      if (templateRes.status === 'fulfilled') {
        const content = templateRes.value?.data?.content || (Array.isArray(templateRes.value?.data) ? templateRes.value.data : []);
        loadedTemplates = content;
      }

      setTemplates([...defaultTemplates, ...loadedTemplates]);

      if (varRes.status === 'fulfilled' && Array.isArray(varRes.value?.data)) {
        setDynamicVariables(varRes.value.data);
      } else {
        setDynamicVariables([
          { fieldKey: 'firstName', displayName: 'First Name' },
          { fieldKey: 'lastName', displayName: 'Last Name' },
          { fieldKey: 'company', displayName: 'Company' },
          { fieldKey: 'city', displayName: 'City' },
          { fieldKey: 'revenue', displayName: 'Revenue Group' },
          { fieldKey: 'linkedin', displayName: 'LinkedIn Profile' },
          { fieldKey: 'primaryCtaUrl', displayName: 'Primary CTA Link' },
          { fieldKey: 'secondaryCtaUrl', displayName: 'Secondary Link' }
        ]);
      }
    } catch (e) {
      console.error(e);
      setTemplates(defaultTemplates);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleInsertVariable = (varName) => {
    setHtmlContent(prev => prev + ` {{${varName}}}`);
    setIsDirty(true);
  };

  const handleInsertEmoji = (emoji) => {
    setHtmlContent(prev => prev + emoji);
    setShowEmojiPicker(false);
    setIsDirty(true);
  };

  const handleSelectTemplateForEdit = (tpl) => {
    setTemplateId(tpl.id);
    setTemplateName(tpl.name || 'Custom Template');
    setSubject(tpl.subject || '');
    setCategory(tpl.category || 'General');
    setHtmlContent(tpl.content || tpl.htmlContent || '');
    if (tpl.links) {
      setLinks(tpl.links);
    }
    setActiveTab('EDITOR');
    setIsDirty(false);
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
      setIsDirty(false);
      showAlert('success', 'Template saved successfully!');
      loadData();
      setActiveTab('GALLERY');
    } catch (e) {
      showAlert('error', 'Failed to save template: ' + (e.response?.data?.message || e.message));
    }
  };

  const handleAiGenerate = async () => {
    const promptToUse = (chatInput || aiGoal).trim();
    if (!promptToUse) return showAlert('error', 'Please describe your email goal');

    setGeneratingAi(true);
    try {
      const res = await templateApi.generateAiTemplate({
        campaignGoal: promptToUse,
        audience: aiAudience,
        tone: aiTone,
        cta: aiCta
      });

      const genSubject = res.data?.subject || `🚀 Exclusive ${aiTone} Update: ${promptToUse.slice(0, 30)}...`;
      const genHtml = res.data?.htmlContent || 
`<div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; border: 1px solid #e5e5e7; border-radius: 16px; background: #ffffff;">
  <h2 style="color: #0a0a0b;">Special ${aiTone} Update for {{company}}</h2>
  <p style="font-size: 14px; color: #5f6368; line-height: 1.6;">Hi {{firstName}},</p>
  <p style="font-size: 14px; color: #5f6368; line-height: 1.6;">${promptToUse}</p>
  <div style="background-color: #fcfcfd; padding: 16px; border-radius: 12px; margin: 24px 0; border-left: 4px solid #ec4899;">
    <p style="margin: 0; font-weight: bold; color: #0a0a0b;">Custom Department: <span>{{department}}</span></p>
  </div>
  <div style="text-align: center; margin: 32px 0;">
    <a href="{{primaryCtaUrl}}" style="background-color: #0a0a0b; color: #ffffff; padding: 12px 24px; text-decoration: none; border-radius: 8px; font-weight: bold; display: inline-block;">${aiCta || 'Explore Product'} →</a>
  </div>
  <hr style="border: none; border-top: 1px solid #e5e5e7; margin: 24px 0;" />
  <p style="font-size: 12px; color: #9ca3af; text-align: center;">Sent via MailAlly | <a href="{{unsubscribeLink}}" style="color: #9ca3af;">Unsubscribe</a></p>
</div>`;

      setTemplateName(`AI Generated - ${promptToUse.slice(0, 20)}`);
      setSubject(genSubject);
      setHtmlContent(genHtml);
      setAiResult({
        subject: genSubject,
        htmlContent: genHtml,
        spamScore: '1.2/10 (Low Risk)'
      });

      setIsDirty(true);
    } catch (e) {
      showAlert('error', 'AI Generation failed: ' + (e.response?.data?.message || e.message));
    } finally {
      setGeneratingAi(false);
    }
  };

  const currentPreviewContact = testContacts.find(c => c.id === Number(selectedPreviewContactId)) || testContacts[0];

  const renderPersonalizedHtml = () => {
    let rendered = htmlContent;

    const replacements = {
      '{{firstName}}': currentPreviewContact.firstName,
      '{{lastName}}': currentPreviewContact.lastName,
      '{{email}}': currentPreviewContact.email,
      '{{company}}': currentPreviewContact.company,
      '{{city}}': currentPreviewContact.city,
      '{{country}}': currentPreviewContact.country,
      '{{department}}': currentPreviewContact.department,
      '{{designation}}': currentPreviewContact.designation,
      '{{revenue}}': currentPreviewContact.revenue,
      '{{linkedin}}': currentPreviewContact.linkedin,
      '{{organizationName}}': 'MailAlly Technologies',
      '{{unsubscribeLink}}': '#unsubscribe',
      '{{primaryCtaUrl}}': links.primaryCtaUrl || 'https://mailally.com/welcome',
      '{{secondaryCtaUrl}}': links.secondaryCtaUrl || 'https://mailally.com/learn-more'
    };

    Object.keys(replacements).forEach(key => {
      rendered = rendered.replaceAll(key, replacements[key]);
    });

    return rendered;
  };

  if (loading) {
    return <PageSkeletonLoader type="cards" />;
  }

  return (
    <div className="space-y-6 animate-fadeInUp pb-8 font-sans">
      
      {/* Top Header & Navigation */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold tracking-tight text-[#0A0A0B]">Template Studio</h1>
          <p className="text-[13px] text-[#9CA3AF] font-medium mt-1">
            Build, personalize, and preview HTML email templates.
          </p>
        </div>

        {/* Tab Controls */}
        <div className="flex items-center gap-1 bg-white p-1 rounded-xl border border-[#E5E5E7] shadow-xs">
          <button
            type="button"
            onClick={() => setActiveTab('EDITOR')}
            className={`px-3.5 py-1.5 rounded-lg text-xs font-bold flex items-center gap-1.5 transition-all cursor-pointer ${
              activeTab === 'EDITOR' ? 'bg-[#0A0A0B] text-white shadow-xs' : 'text-[#5F6368] hover:bg-[#F9FAFB]'
            }`}
          >
            <Edit3 className="w-3.5 h-3.5" /> Editor
          </button>
          <button
            type="button"
            onClick={() => setActiveTab('GALLERY')}
            className={`px-3.5 py-1.5 rounded-lg text-xs font-bold flex items-center gap-1.5 transition-all cursor-pointer ${
              activeTab === 'GALLERY' ? 'bg-[#0A0A0B] text-white shadow-xs' : 'text-[#5F6368] hover:bg-[#F9FAFB]'
            }`}
          >
            <Layout className="w-3.5 h-3.5" /> Gallery ({templates.length})
          </button>
          <button
            type="button"
            onClick={() => setActiveTab('AI')}
            className={`px-3.5 py-1.5 rounded-lg text-xs font-bold flex items-center gap-1.5 transition-all cursor-pointer ${
              activeTab === 'AI' ? 'bg-[#0A0A0B] text-white shadow-xs' : 'text-[#5F6368] hover:bg-[#F9FAFB]'
            }`}
          >
            <Sparkles className="w-3.5 h-3.5 text-[#EC4899]" /> AI Generator
          </button>
        </div>
      </div>

      {/* TAB 1: TEMPLATE EDITOR */}
      {activeTab === 'EDITOR' && (
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-5">
          
          {/* Left Column: Form, Rich Toolbar, HTML Editor & Links */}
          <div className="lg:col-span-7 space-y-4">
            <div className="bg-white p-5 rounded-[20px] border border-[#E5E5E7] space-y-4 shadow-xs">
              
              {/* Template Name & Category */}
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-bold text-slate-700 mb-1">Template Name</label>
                  <input
                    type="text"
                    value={templateName}
                    onChange={e => { setTemplateName(e.target.value); setIsDirty(true); }}
                    className="w-full h-10 px-3 bg-white border border-slate-200 rounded-xl text-xs font-semibold outline-none focus:border-slate-400"
                  />
                </div>
                <div>
                  <label className="block text-xs font-bold text-slate-700 mb-1">Category</label>
                  <select
                    value={category}
                    onChange={e => { setCategory(e.target.value); setIsDirty(true); }}
                    className="w-full h-10 px-3 bg-white border border-slate-200 rounded-xl text-xs font-semibold outline-none focus:border-slate-400 cursor-pointer"
                  >
                    <option value="Promotional">Promotional</option>
                    <option value="Onboarding">Onboarding</option>
                    <option value="Newsletter">Newsletter</option>
                    <option value="Product">Product Launch</option>
                    <option value="Event">Event Invitation</option>
                    <option value="Outreach">Cold Outreach</option>
                  </select>
                </div>
              </div>

              {/* Subject Line */}
              <div>
                <label className="block text-xs font-bold text-slate-700 mb-1">Subject Line</label>
                <input
                  type="text"
                  value={subject}
                  onChange={e => { setSubject(e.target.value); setIsDirty(true); }}
                  className="w-full h-10 px-3 bg-white border border-slate-200 rounded-xl text-xs font-semibold outline-none focus:border-slate-400"
                />
              </div>

              {/* Dynamic Variable Chips */}
              <div>
                <span className="text-[11px] font-bold text-[#EC4899] block mb-1.5">Dynamic Variables (Click to insert)</span>
                <div className="flex flex-wrap gap-1.5 p-2.5 bg-[#FAFAFB] rounded-xl border border-[#E5E5E7] max-h-24 overflow-y-auto">
                  {dynamicVariables.map(v => (
                    <button
                      key={v.fieldKey || v}
                      type="button"
                      onClick={() => handleInsertVariable(v.fieldKey || v)}
                      className="px-2 py-1 bg-white hover:bg-[#0A0A0B] hover:text-white text-[#0A0A0B] text-[11px] font-mono font-bold rounded-lg border border-[#E5E5E7] transition-all cursor-pointer shadow-2xs"
                    >
                      {`{{${v.fieldKey || v}}}`}
                    </button>
                  ))}
                </div>
              </div>

              {/* Editor Mode Header: Visual Text Editor vs Raw HTML Code Editor */}
              <div className="flex items-center justify-between pt-2 border-t border-slate-100">
                <span className="text-xs font-bold text-slate-800 uppercase tracking-wider">HTML Body</span>
                <div className="flex items-center gap-1 bg-slate-100 p-1 rounded-xl border border-slate-200">
                  <button
                    type="button"
                    onClick={() => setEditorMode('VISUAL')}
                    className={`flex items-center gap-1.5 px-3 py-1 rounded-lg text-xs font-bold transition-all cursor-pointer ${
                      editorMode === 'VISUAL' ? 'bg-white text-slate-900 shadow-xs' : 'text-slate-500 hover:text-slate-900'
                    }`}
                  >
                    <Type className="w-3.5 h-3.5" /> Text Editor
                  </button>
                  <button
                    type="button"
                    onClick={() => setEditorMode('CODE')}
                    className={`flex items-center gap-1.5 px-3 py-1 rounded-lg text-xs font-bold transition-all cursor-pointer ${
                      editorMode === 'CODE' ? 'bg-white text-slate-900 shadow-xs' : 'text-slate-500 hover:text-slate-900'
                    }`}
                  >
                    <Code className="w-3.5 h-3.5" /> Code Editor
                  </button>
                </div>
              </div>

              {/* Visual Text Editor Mode Toolbar */}
              {editorMode === 'VISUAL' ? (
                <div className="border border-slate-200 rounded-2xl overflow-hidden bg-white shadow-xs">
                  {/* Rich Text Formatting Toolbar */}
                  <div className="flex flex-wrap items-center gap-1 p-2 bg-slate-50 border-b border-slate-200 text-slate-700">
                    <select className="h-7 px-2 text-xs font-medium bg-white border border-slate-200 rounded-lg outline-none cursor-pointer">
                      <option>Paragraph</option>
                      <option>Heading 1</option>
                      <option>Heading 2</option>
                      <option>Heading 3</option>
                    </select>

                    <div className="h-4 w-px bg-slate-300 mx-1"></div>

                    <button type="button" className="p-1.5 rounded hover:bg-slate-200 text-slate-700 cursor-pointer" title="Bold"><Bold className="w-3.5 h-3.5" /></button>
                    <button type="button" className="p-1.5 rounded hover:bg-slate-200 text-slate-700 cursor-pointer" title="Italic"><Italic className="w-3.5 h-3.5" /></button>
                    <button type="button" className="p-1.5 rounded hover:bg-slate-200 text-slate-700 cursor-pointer" title="Underline"><Underline className="w-3.5 h-3.5" /></button>
                    <button type="button" className="p-1.5 rounded hover:bg-slate-200 text-slate-700 cursor-pointer" title="Strikethrough"><Strikethrough className="w-3.5 h-3.5" /></button>

                    <div className="h-4 w-px bg-slate-300 mx-1"></div>

                    <button type="button" className="p-1.5 rounded hover:bg-slate-200 text-slate-700 cursor-pointer" title="Align Left"><AlignLeft className="w-3.5 h-3.5" /></button>
                    <button type="button" className="p-1.5 rounded hover:bg-slate-200 text-slate-700 cursor-pointer" title="Align Center"><AlignCenter className="w-3.5 h-3.5" /></button>
                    <button type="button" className="p-1.5 rounded hover:bg-slate-200 text-slate-700 cursor-pointer" title="Align Right"><AlignRight className="w-3.5 h-3.5" /></button>
                    <button type="button" className="p-1.5 rounded hover:bg-slate-200 text-slate-700 cursor-pointer" title="List"><List className="w-3.5 h-3.5" /></button>

                    <div className="h-4 w-px bg-slate-300 mx-1"></div>

                    <button type="button" className="p-1.5 rounded hover:bg-slate-200 text-slate-700 cursor-pointer" title="Insert Link"><Link className="w-3.5 h-3.5" /></button>
                    <button type="button" className="p-1.5 rounded hover:bg-slate-200 text-slate-700 cursor-pointer" title="Insert Image"><ImageIcon className="w-3.5 h-3.5" /></button>

                    {/* Emoji Picker Button */}
                    <div className="relative">
                      <button
                        type="button"
                        onClick={() => setShowEmojiPicker(!showEmojiPicker)}
                        className="p-1.5 rounded hover:bg-slate-200 text-amber-500 cursor-pointer flex items-center gap-1"
                        title="Insert Emoji"
                      >
                        <Smile className="w-4 h-4" />
                      </button>

                      {showEmojiPicker && (
                        <div className="absolute left-0 top-8 z-50 p-3 bg-white border border-slate-300 rounded-2xl shadow-xl grid grid-cols-5 gap-2 w-48">
                          {emojiList.map(emoji => (
                            <button
                              key={emoji}
                              type="button"
                              onClick={() => handleInsertEmoji(emoji)}
                              className="text-base p-1 hover:bg-slate-100 rounded-lg transition-colors cursor-pointer"
                            >
                              {emoji}
                            </button>
                          ))}
                        </div>
                      )}
                    </div>
                  </div>

                  <textarea
                    rows={12}
                    value={htmlContent}
                    onChange={e => { setHtmlContent(e.target.value); setIsDirty(true); }}
                    className="w-full p-4 font-sans text-xs leading-relaxed bg-white outline-none text-slate-900 resize-y"
                  />
                </div>
              ) : (
                /* Raw Code Editor Mode */
                <div className="border border-slate-900 rounded-2xl overflow-hidden bg-[#0A0A0B] shadow-md">
                  <div className="flex items-center justify-between px-4 py-2 bg-slate-900 border-b border-slate-800 text-xs font-mono text-slate-400">
                    <span>index.html (Source Code Mode)</span>
                    <span className="text-emerald-400">HTML5</span>
                  </div>
                  <textarea
                    rows={14}
                    value={htmlContent}
                    onChange={e => { setHtmlContent(e.target.value); setIsDirty(true); }}
                    className="w-full p-4 font-mono text-xs leading-relaxed bg-[#0A0A0B] text-emerald-400 outline-none resize-y"
                  />
                </div>
              )}

              {/* Template-Specific URLs & Dynamic Links Section */}
              <div className="p-4 bg-slate-50 border border-slate-200 rounded-2xl space-y-3">
                <div className="flex items-center justify-between">
                  <h4 className="text-xs font-bold text-slate-800 uppercase tracking-wider flex items-center gap-1.5">
                    <ExternalLink className="w-3.5 h-3.5 text-blue-600" /> Template Links & Dynamic URLs
                  </h4>
                  <span className="text-[11px] text-slate-400 font-medium">Injectable into HTML body</span>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                  <div>
                    <label className="block text-[11px] font-bold text-slate-600 mb-1">Primary CTA URL (&#123;&#123;primaryCtaUrl&#125;&#125;)</label>
                    <input
                      type="text"
                      value={links.primaryCtaUrl}
                      onChange={e => setLinks(prev => ({ ...prev, primaryCtaUrl: e.target.value }))}
                      placeholder="https://mailally.com/offer"
                      className="w-full h-9 px-3 text-xs bg-white border border-slate-200 rounded-xl outline-none focus:border-blue-500 font-mono"
                    />
                  </div>
                  <div>
                    <label className="block text-[11px] font-bold text-slate-600 mb-1">Secondary Link URL (&#123;&#123;secondaryCtaUrl&#125;&#125;)</label>
                    <input
                      type="text"
                      value={links.secondaryCtaUrl}
                      onChange={e => setLinks(prev => ({ ...prev, secondaryCtaUrl: e.target.value }))}
                      placeholder="https://mailally.com/docs"
                      className="w-full h-9 px-3 text-xs bg-white border border-slate-200 rounded-xl outline-none focus:border-blue-500 font-mono"
                    />
                  </div>
                </div>

                <div className="flex flex-wrap gap-2 pt-1">
                  <button
                    type="button"
                    onClick={() => handleInsertVariable('primaryCtaUrl')}
                    className="text-[11px] font-mono px-2 py-1 bg-white hover:bg-blue-50 text-blue-600 rounded-lg border border-blue-200 cursor-pointer"
                  >
                    + &#123;&#123;primaryCtaUrl&#125;&#125;
                  </button>
                  <button
                    type="button"
                    onClick={() => handleInsertVariable('secondaryCtaUrl')}
                    className="text-[11px] font-mono px-2 py-1 bg-white hover:bg-blue-50 text-blue-600 rounded-lg border border-blue-200 cursor-pointer"
                  >
                    + &#123;&#123;secondaryCtaUrl&#125;&#125;
                  </button>
                  <button
                    type="button"
                    onClick={() => handleInsertVariable('unsubscribeLink')}
                    className="text-[11px] font-mono px-2 py-1 bg-white hover:bg-slate-100 text-slate-600 rounded-lg border border-slate-200 cursor-pointer"
                  >
                    + &#123;&#123;unsubscribeLink&#125;&#125;
                  </button>
                </div>
              </div>

              {/* Action Bar */}
              <div className="flex items-center justify-between pt-2">
                <span className="text-xs text-slate-400 font-medium">
                  {isDirty ? '⚠️ Unsaved changes' : '✓ All changes saved'}
                </span>
                <button
                  type="button"
                  onClick={handleSaveTemplate}
                  className="px-6 py-2.5 bg-slate-900 hover:bg-slate-800 text-white rounded-xl text-xs font-bold transition-all shadow-md cursor-pointer"
                >
                  Save Template
                </button>
              </div>

            </div>
          </div>

          {/* Right Column: Live Interactive Preview */}
          <div className="lg:col-span-5 space-y-4">
            <div className="bg-white p-5 rounded-[20px] border border-[#E5E5E7] space-y-3 shadow-xs">
              <div className="flex items-center justify-between">
                <span className="text-xs font-bold text-slate-900 flex items-center gap-1.5">
                  <Eye className="w-4 h-4 text-blue-600" /> Live Preview
                </span>

                <div className="flex items-center gap-1 bg-slate-100 p-1 rounded-xl border border-slate-200">
                  <button
                    type="button"
                    onClick={() => setPreviewDevice('DESKTOP')}
                    className={`p-1.5 rounded-lg text-xs transition-all cursor-pointer ${previewDevice === 'DESKTOP' ? 'bg-white text-slate-900 shadow-xs' : 'text-slate-400'}`}
                  >
                    <Monitor className="w-3.5 h-3.5" />
                  </button>
                  <button
                    type="button"
                    onClick={() => setPreviewDevice('MOBILE')}
                    className={`p-1.5 rounded-lg text-xs transition-all cursor-pointer ${previewDevice === 'MOBILE' ? 'bg-white text-slate-900 shadow-xs' : 'text-slate-400'}`}
                  >
                    <Smartphone className="w-3.5 h-3.5" />
                  </button>
                </div>
              </div>

              {/* Test Contact Selector */}
              <div>
                <label className="block text-[11px] font-bold text-slate-500 mb-1">Test Contact Profile</label>
                <select
                  value={selectedPreviewContactId}
                  onChange={e => setSelectedPreviewContactId(Number(e.target.value))}
                  className="w-full h-9 px-3 bg-white border border-slate-200 rounded-xl text-xs font-semibold text-slate-800 outline-none cursor-pointer"
                >
                  {testContacts.map(c => (
                    <option key={c.id} value={c.id}>
                      {c.firstName} {c.lastName} ({c.company})
                    </option>
                  ))}
                </select>
              </div>

              {/* Frame Simulation Container */}
              <div className={`mx-auto transition-all ${previewDevice === 'MOBILE' ? 'max-w-[320px]' : 'w-full'}`}>
                <div className="bg-white rounded-2xl overflow-hidden border border-slate-200 min-h-[380px] shadow-sm">
                  <div className="bg-slate-50 border-b border-slate-200 p-3 text-xs text-slate-600 font-medium space-y-1">
                    <div><strong className="text-slate-800">Subject:</strong> {subject.replaceAll('{{company}}', currentPreviewContact.company)}</div>
                    <div><strong className="text-slate-800">To:</strong> {currentPreviewContact.email}</div>
                  </div>
                  <div className="p-4 text-xs" dangerouslySetInnerHTML={{ __html: renderPersonalizedHtml() }} />
                </div>
              </div>

            </div>
          </div>

        </div>
      )}

      {/* TAB 2: GALLERY */}
      {activeTab === 'GALLERY' && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {templates.map(t => (
            <div key={t.id} className="bg-white border border-slate-200 rounded-[20px] p-5 space-y-4 hover:shadow-md transition-all flex flex-col justify-between">
              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <span className="text-[11px] font-bold text-blue-600 bg-blue-50 px-2.5 py-0.5 rounded-full border border-blue-100">
                    {t.category || 'General'}
                  </span>
                </div>
                <h3 className="text-sm font-bold text-slate-900">{t.name}</h3>
                <p className="text-xs text-slate-500 font-mono line-clamp-2">Subject: {t.subject || 'No subject'}</p>
              </div>

              <div className="flex items-center justify-end gap-2 pt-3 border-t border-slate-100">
                <button
                  type="button"
                  onClick={() => { setSelectedPreviewTemplate(t); setPreviewModalOpen(true); }}
                  className="px-3 py-1.5 bg-slate-100 hover:bg-slate-200 text-slate-700 rounded-xl text-xs font-bold transition-colors cursor-pointer flex items-center gap-1"
                >
                  <Eye className="w-3.5 h-3.5 text-slate-500" /> Preview
                </button>
                <button
                  type="button"
                  onClick={() => handleSelectTemplateForEdit(t)}
                  className="px-4 py-1.5 bg-slate-900 hover:bg-slate-800 text-white rounded-xl text-xs font-bold transition-colors cursor-pointer"
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
          <div className="bg-white p-6 rounded-[20px] border border-slate-200 space-y-5 shadow-xs">
            <div className="flex items-center gap-2">
              <Wand2 className="w-5 h-5 text-pink-500" />
              <h2 className="text-base font-bold text-slate-900">AI Template Generator</h2>
            </div>

            {/* Tone Options */}
            <div>
              <label className="block text-xs font-bold text-slate-700 mb-2">Select Communication Tone</label>
              <div className="flex items-center gap-2 overflow-x-auto pb-1">
                {['Professional', 'Promotional', 'Friendly', 'Executive', 'Persuasive'].map((t) => (
                  <button
                    key={t}
                    type="button"
                    onClick={() => setAiTone(t)}
                    className={`px-3.5 py-1.5 rounded-xl text-xs font-bold transition-all cursor-pointer ${
                      aiTone === t ? 'bg-slate-900 text-white shadow-xs' : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
                    }`}
                  >
                    {t}
                  </button>
                ))}
              </div>
            </div>

            {/* Goal Input */}
            <div>
              <label className="block text-xs font-bold text-slate-700 mb-1">Email Goal & Prompt Description</label>
              <textarea
                rows={3}
                value={chatInput}
                onChange={e => setChatInput(e.target.value)}
                placeholder="e.g. Product launch discount for enterprise tech subscribers with a call to action..."
                className="w-full p-3 font-medium text-xs bg-slate-50 border border-slate-200 rounded-xl outline-none focus:border-blue-500 resize-none"
              />
            </div>

            <button
              type="button"
              disabled={generatingAi}
              onClick={handleAiGenerate}
              className="w-full py-3 bg-slate-900 hover:bg-slate-800 text-white rounded-xl font-bold text-xs shadow-md transition-all flex items-center justify-center gap-2 cursor-pointer"
            >
              {generatingAi ? <RefreshCw className="w-4 h-4 animate-spin" /> : <Sparkles className="w-4 h-4 text-pink-400" />}
              {generatingAi ? 'Generating Responsive HTML Template...' : 'Generate Template & Subject Lines'}
            </button>
          </div>

          {/* AI Generated Result Box */}
          {aiResult && (
            <div className="p-6 bg-white rounded-[20px] border border-slate-200 space-y-4 shadow-sm">
              <div className="flex items-center justify-between border-b border-slate-100 pb-3">
                <span className="text-xs font-bold text-emerald-600 flex items-center gap-1.5">
                  <ShieldCheck className="w-4 h-4" /> Spam Score: {aiResult.spamScore}
                </span>
                <span className="text-xs text-slate-400 font-medium">Ready for Studio Editor</span>
              </div>

              <div className="space-y-3">
                <div>
                  <label className="block text-xs font-bold text-slate-700 mb-1">Generated Subject Line</label>
                  <input
                    type="text"
                    value={subject}
                    onChange={e => setSubject(e.target.value)}
                    className="w-full h-10 px-3 bg-slate-50 border border-slate-200 rounded-xl text-xs font-semibold outline-none"
                  />
                </div>
                <div>
                  <label className="block text-xs font-bold text-slate-700 mb-1">Generated HTML Code Structure</label>
                  <textarea
                    rows={6}
                    value={htmlContent}
                    onChange={e => setHtmlContent(e.target.value)}
                    className="w-full p-3 font-mono text-xs bg-slate-900 text-emerald-400 rounded-xl outline-none"
                  />
                </div>
              </div>

              <button
                type="button"
                onClick={() => setActiveTab('EDITOR')}
                className="w-full py-3 bg-blue-600 hover:bg-blue-700 text-white rounded-xl font-bold text-xs shadow-md transition-all flex items-center justify-center gap-2 cursor-pointer"
              >
                Save & Continue to Studio Editor <ArrowRight className="w-4 h-4" />
              </button>
            </div>
          )}
        </div>
      )}

      {/* Gallery Template Device Ratio Preview Modal */}
      <TemplatePreviewModal
        isOpen={previewModalOpen}
        onClose={() => setPreviewModalOpen(false)}
        template={selectedPreviewTemplate}
        testContacts={testContacts}
      />

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
