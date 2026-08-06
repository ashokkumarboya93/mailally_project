import React, { useState } from 'react';
import { aiApi } from '../../api/extraApis';
import { Sparkles, Send, Wand2, Bot } from 'lucide-react';

export const AiAssistantPage = () => {
  const [prompt, setPrompt] = useState('');
  const [mode, setMode] = useState('SUBJECT');
  const [response, setResponse] = useState('');
  const [loading, setLoading] = useState(false);

  const handleGenerate = async (e) => {
    e.preventDefault();
    if (!prompt.trim()) return;
    setLoading(true);
    setResponse('');
    try {
      let res;
      if (mode === 'SUBJECT') res = await aiApi.generateSubject(prompt);
      else if (mode === 'CONTENT') res = await aiApi.generateContent(prompt);
      else if (mode === 'REWRITE') res = await aiApi.rewriteEmail(prompt);
      else if (mode === 'GRAMMAR') res = await aiApi.grammarFix(prompt);
      else if (mode === 'SPAM') res = await aiApi.spamScore(prompt);
      else res = await aiApi.campaignIdeas(prompt);

      if (res.data) setResponse(res.data.generatedContent);
    } catch {
      setResponse('Error executing AI prompt. Check backend connection.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-8 animate-fadeInUp font-sans max-w-5xl mx-auto pb-12">
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
            <Wand2 className="w-3 h-3 text-[#00DDFF]" />
            <span>Generative AI Engine</span>
          </div>

          <h1 className="text-2xl sm:text-3xl font-black tracking-tight leading-none text-white" style={{ fontFamily: 'var(--font-heading)' }}>
            AI Content <br />
            <span className="text-[#00DDFF]">Studio</span>
          </h1>

          <p className="text-[10px] sm:text-[11px] text-blue-50 leading-relaxed font-medium">
            Generate high-converting subject lines, HTML body copy, spam score evaluations, and campaign strategies.
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

      {/* Mode Switcher Tabs */}
      <div className="flex space-x-2 pb-2 overflow-x-auto">
        {[
          { key: 'SUBJECT', label: 'Subject Lines' },
          { key: 'CONTENT', label: 'HTML Body' },
          { key: 'REWRITE', label: 'Rewrite' },
          { key: 'GRAMMAR', label: 'Grammar' },
          { key: 'SPAM', label: 'Spam Score' },
          { key: 'IDEAS', label: 'Campaign Ideas' }
        ].map((item) => (
          <button
            key={item.key}
            onClick={() => setMode(item.key)}
            className={`px-4.5 py-2.5 rounded-xl text-xs font-black transition-all whitespace-nowrap cursor-pointer ${mode === item.key ? 'bg-[#2563EB] text-white shadow-md shadow-blue-500/20' : 'bg-white text-slate-600 border border-slate-200 hover:bg-slate-50'}`}
          >
            {item.label}
          </button>
        ))}
      </div>

      {/* Form Card Container */}
      <div className="bg-white rounded-[22px] p-6 border space-y-4 shadow-xs" style={{ borderColor: 'rgba(37,99,235,0.08)' }}>
        <form onSubmit={handleGenerate} className="space-y-4">
          <div>
            <label className="block text-xs font-black mb-1.5 uppercase tracking-wider text-slate-400">
              Input Prompt / Campaign Context *
            </label>
            <textarea
              rows={4}
              value={prompt}
              onChange={(e) => setPrompt(e.target.value)}
              placeholder="Describe your campaign topic..."
              className="w-full p-3 rounded-xl border border-slate-200 text-xs font-semibold focus:outline-none focus:border-[#2563EB] bg-white placeholder-slate-400/80"
            />
          </div>
          <button
            type="submit"
            disabled={loading}
            className="px-6 py-3 rounded-xl text-xs font-black text-white bg-[#2563EB] hover:bg-[#1D4ED8] transition-all cursor-pointer shadow-md shadow-blue-500/25 flex items-center space-x-2"
          >
            <Sparkles className="w-4 h-4" />
            <span>{loading ? 'Generating Copy...' : 'Execute AI Prompt'}</span>
          </button>
        </form>

        {response && (
          <div className="p-4.5 rounded-xl animate-fadeInUp bg-blue-50/50 border border-blue-100/80 space-y-2">
            <div className="flex items-center space-x-2 text-xs font-black text-[#2563EB]">
              <Bot className="w-4 h-4" />
              <span>AI Model Output</span>
            </div>
            <pre className="text-xs font-sans whitespace-pre-wrap leading-relaxed text-[#1E293B] font-medium">
              {response}
            </pre>
          </div>
        )}
      </div>
    </div>
  );
};
