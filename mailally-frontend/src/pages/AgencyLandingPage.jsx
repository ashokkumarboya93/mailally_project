import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { 
  Sparkles, ArrowRight, CheckCircle2, Star, ChevronDown, ChevronUp,
  Layout, Palette, Code, Smartphone, Video, Search, Shield, Zap,
  Linkedin, Mail, Send, Globe, Users, Award, Clock, ArrowUpRight, Play, Check,
  TrendingUp, BarChart3, Filter, Cpu, Layers, RefreshCw, Rocket, HelpCircle,
  Lock, Activity, MousePointer, Bell, Sliders, CheckCircle, ChevronRight,
  Maximize2, Eye, FileText, CheckCircle2 as CheckIcon
} from 'lucide-react';
import { CometLogo } from '../components/common/CometLogo';

export const AgencyLandingPage = () => {
  const navigate = useNavigate();
  const [openFaq, setOpenFaq] = useState(0);
  const [isYearly, setIsYearly] = useState(true);
  const [activeTab, setActiveTab] = useState('analytics');

  const toggleFaq = (index) => {
    setOpenFaq(openFaq === index ? null : index);
  };

  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  return (
    <div className="min-h-screen relative font-sans text-[#1E293B] bg-[#F0F7FF] selection:bg-[#1F57F5]/20 selection:text-[#1F57F5]">
      {/* Ice Blue Ambient Radial Glow Blobs */}
      <div className="radial-glow-cyan top-[-100px] left-[-100px] opacity-70" />
      <div className="radial-glow-blue top-[250px] right-[-150px] opacity-60" />
      <div className="radial-glow-cyan top-[1600px] left-[-200px] opacity-50" />
      <div className="radial-glow-blue top-[3200px] right-[-200px] opacity-60" />

      {/* FLOATING GLASS NAVBAR */}
      <div className="sticky top-4 z-50 px-4 sm:px-8 max-w-7xl mx-auto">
        <header className="glass-navbar rounded-2xl px-5 py-3 flex items-center justify-between shadow-sm transition-all duration-300">
          {/* Logo & Brand */}
          <div className="flex items-center space-x-3 cursor-pointer group" onClick={() => navigate('/')}>
            <CometLogo size="md" />
            <div>
              <span className="text-2xl font-black tracking-tight block text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
                MailAlly<span className="text-[#1F57F5]">.</span>
              </span>
              <span className="text-[9px] font-extrabold uppercase tracking-[1.8px] block -mt-1 text-[#2BAFF2]">
                Enterprise SaaS
              </span>
            </div>
          </div>

          {/* Navigation Links */}
          <nav className="hidden lg:flex items-center space-x-8 text-xs font-bold text-[#334155]">
            <a href="#showcase" className="hover:text-[#1F57F5] transition-colors">Showcase</a>
            <a href="#features" className="hover:text-[#1F57F5] transition-colors">Features</a>
            <a href="#workflow" className="hover:text-[#1F57F5] transition-colors">Workflow</a>
            <a href="#pricing" className="hover:text-[#1F57F5] transition-colors">Pricing</a>
            <a href="#integrations" className="hover:text-[#1F57F5] transition-colors">Integrations</a>
            <a href="#faq" className="hover:text-[#1F57F5] transition-colors">FAQ</a>
          </nav>

          {/* Header Action Buttons */}
          <div className="flex items-center space-x-4">
            <Link 
              to="/login" 
              className="text-xs font-extrabold text-[#1E3A8A] hover:text-[#1F57F5] transition-colors hidden sm:block px-3 py-1.5"
            >
              Sign In
            </Link>
            <button
              onClick={() => navigate('/dashboard')}
              className="mailally-btn-primary py-2.5 px-5 text-xs font-extrabold"
            >
              <span>Launch Workspace</span>
              <ArrowRight className="w-3.5 h-3.5" />
            </button>
          </div>
        </header>
      </div>

      {/* HERO SECTION */}
      <section className="px-6 lg:px-12 pt-16 pb-20 max-w-7xl mx-auto relative z-10">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-12 items-center">
          {/* Hero Left Content */}
          <div className="lg:col-span-7 space-y-6">
            {/* Pill Badge */}
            <div className="badge-blue bg-[#1F57F5]/10 text-[#1F57F5] border border-[#1F57F5]/30 inline-flex items-center gap-2">
              <Sparkles className="w-3.5 h-3.5 text-[#2BAFF2]" />
              <span>Enterprise Email Marketing Automation</span>
            </div>

            {/* Headline with Electric Blue & Cyan Gradients */}
            <h1 className="text-4xl sm:text-5xl lg:text-6xl font-black leading-[1.12] tracking-tight text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
              Boost your sales with <span className="blue-gradient-text">smarter cold</span> <span className="cyan-gradient-text">email campaigns</span>
            </h1>

            {/* Supporting Text */}
            <p className="text-base sm:text-lg leading-relaxed text-[#334155] max-w-xl font-medium">
              MailAlly helps you automate outreach, engage prospects, and close more deals with high deliverability, real-time telemetry, and Gemini AI.
            </p>

            {/* Action Buttons */}
            <div className="flex flex-wrap items-center gap-4 pt-2">
              <button 
                onClick={() => navigate('/register')} 
                className="mailally-btn-accent py-4 px-8 text-sm font-extrabold group"
              >
                <span>Start Free Trial</span>
                <ArrowRight className="w-4 h-4 group-hover:translate-x-1 transition-transform" />
              </button>

              <a 
                href="#showcase" 
                className="mailally-btn-secondary py-4 px-8 text-sm font-extrabold"
              >
                <span>Explore Showcase</span>
              </a>
            </div>

            {/* Metrics */}
            <div className="pt-8 border-t border-slate-200/80 flex flex-wrap items-center justify-between gap-6">
              <div className="flex items-center space-x-3">
                <div className="w-10 h-10 rounded-xl bg-blue-50 flex items-center justify-center text-[#1F57F5]">
                  <Send className="w-5 h-5" />
                </div>
                <div>
                  <span className="text-2xl font-black block text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>120+</span>
                  <span className="text-xs text-slate-500 font-medium">Projects Delivered</span>
                </div>
              </div>

              <div className="flex items-center space-x-3">
                <div className="w-10 h-10 rounded-xl bg-cyan-50 flex items-center justify-center text-[#0088AA]">
                  <TrendingUp className="w-5 h-5" />
                </div>
                <div>
                  <span className="text-2xl font-black block text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>99.4%</span>
                  <span className="text-xs text-slate-500 font-medium">Delivery Velocity</span>
                </div>
              </div>

              <div className="flex items-center space-x-3">
                <div className="w-10 h-10 rounded-xl bg-sky-50 flex items-center justify-center text-[#2BAFF2]">
                  <Star className="w-5 h-5 fill-[#2BAFF2]" />
                </div>
                <div>
                  <span className="text-2xl font-black block text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>4.9 ★</span>
                  <span className="text-xs text-slate-500 font-medium">Client Rating</span>
                </div>
              </div>
            </div>

            {/* Avatars */}
            <div className="flex items-center space-x-3 pt-2">
              <div className="flex -space-x-2.5">
                {['https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=80&q=80',
                  'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=80&q=80',
                  'https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=80&q=80'
                ].map((img, i) => (
                  <img key={i} src={img} alt="User avatar" className="w-8 h-8 rounded-full border-2 border-white object-cover shadow-xs" />
                ))}
              </div>
              <span className="text-xs font-bold text-[#334155]">
                <strong className="text-[#1E3A8A]">+2.5K</strong> Active Campaigns Running
              </span>
            </div>
          </div>

          {/* Hero Right Dashboard Preview */}
          <div className="lg:col-span-5 relative">
            <div className="claude-card p-6 border border-slate-200/90 shadow-2xl bg-white rounded-3xl space-y-5 animate-float-slow">
              <div className="flex items-center justify-between pb-2 border-b border-slate-100">
                <div className="flex items-center space-x-3">
                  <div className="w-9 h-9 rounded-xl bg-gradient-to-r from-[#1F57F5] to-[#2BAFF2] text-white flex items-center justify-center font-bold shadow-md shadow-[#1F57F5]/30">
                    <Sparkles className="w-4 h-4" />
                  </div>
                  <div>
                    <h4 className="font-black text-base text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
                      Campaign Telemetry
                    </h4>
                    <p className="text-[10px] text-slate-400 font-medium">Real-Time Enterprise Engine</p>
                  </div>
                </div>

                <div className="flex items-center space-x-2">
                  <span className="px-2.5 py-1 rounded-full text-[10px] font-extrabold bg-emerald-500/10 text-emerald-600 flex items-center gap-1.5 border border-emerald-500/20">
                    <span className="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-ping" />
                    Live
                  </span>
                  <div className="text-[11px] font-bold text-slate-500 bg-slate-100 px-2.5 py-1 rounded-lg border border-slate-200 flex items-center gap-1">
                    <span>Last 7 Days</span>
                    <ChevronDown className="w-3 h-3 text-slate-400" />
                  </div>
                </div>
              </div>

              {/* Stats Row */}
              <div className="grid grid-cols-3 gap-3">
                <div className="p-3 rounded-xl bg-[#F0F7FF] border border-slate-100">
                  <span className="text-[10px] text-slate-400 font-bold uppercase block">Emails Sent</span>
                  <span className="text-base font-black text-[#1E3A8A] block my-0.5" style={{ fontFamily: 'var(--font-heading)' }}>
                    24,560
                  </span>
                  <span className="text-[10px] font-extrabold text-emerald-600 flex items-center gap-0.5">
                    ↑ 26.1%
                  </span>
                </div>

                <div className="p-3 rounded-xl bg-[#F0F7FF] border border-slate-100">
                  <span className="text-[10px] text-slate-400 font-bold uppercase block">Open Rate</span>
                  <span className="text-base font-black text-[#1E3A8A] block my-0.5" style={{ fontFamily: 'var(--font-heading)' }}>
                    42.7%
                  </span>
                  <span className="text-[10px] font-extrabold text-emerald-600 flex items-center gap-0.5">
                    ↑ 12.4%
                  </span>
                </div>

                <div className="p-3 rounded-xl bg-[#F0F7FF] border border-slate-100">
                  <span className="text-[10px] text-slate-400 font-bold uppercase block">Reply Rate</span>
                  <span className="text-base font-black text-[#1E3A8A] block my-0.5" style={{ fontFamily: 'var(--font-heading)' }}>
                    11.3%
                  </span>
                  <span className="text-[10px] font-extrabold text-emerald-600 flex items-center gap-0.5">
                    ↑ 8.7%
                  </span>
                </div>
              </div>

              {/* Chart Mockup */}
              <div className="p-4 rounded-2xl bg-gradient-to-b from-blue-50/60 to-slate-50 border border-blue-100/80 relative overflow-hidden space-y-3">
                <div className="h-32 w-full relative">
                  <svg className="w-full h-full overflow-visible" viewBox="0 0 300 100" preserveAspectRatio="none">
                    <defs>
                      <linearGradient id="blueAreaGrad" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="0%" stopColor="#1F57F5" stopOpacity="0.25" />
                        <stop offset="100%" stopColor="#00DDFF" stopOpacity="0.0" />
                      </linearGradient>
                    </defs>
                    <path
                      d="M 0 70 Q 50 30, 100 50 T 200 35 T 300 15 L 300 100 L 0 100 Z"
                      fill="url(#blueAreaGrad)"
                    />
                    <path
                      d="M 0 70 Q 50 30, 100 50 T 200 35 T 300 15"
                      fill="none"
                      stroke="#1F57F5"
                      strokeWidth="3"
                      strokeLinecap="round"
                    />
                    <circle cx="210" cy="33" r="5" fill="#1F57F5" stroke="#FFFFFF" strokeWidth="2" />
                  </svg>

                  <div className="absolute top-[18%] left-[62%] -translate-x-1/2 bg-[#1F57F5] text-white text-[10px] font-extrabold px-2.5 py-1 rounded-full shadow-md shadow-blue-500/30">
                    12,540
                  </div>
                </div>

                <div className="h-16 flex items-end justify-between gap-1.5 pt-2 border-t border-blue-100/60">
                  {[35, 55, 40, 80, 65, 95, 70, 88, 60, 100, 82, 90].map((height, idx) => (
                    <div
                      key={idx}
                      className="flex-1 bg-gradient-to-t from-blue-300/40 to-[#1F57F5] rounded-t-sm hover:from-cyan-400 hover:to-[#00DDFF] transition-all cursor-pointer"
                      style={{ height: `${height}%` }}
                    />
                  ))}
                </div>
              </div>

              {/* Bottom Row */}
              <div className="flex items-center justify-between pt-1">
                <div className="flex items-center space-x-2">
                  <div className="flex -space-x-2">
                    <img src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=60&q=80" className="w-6 h-6 rounded-full border border-white" alt="Avatar" />
                    <img src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=60&q=80" className="w-6 h-6 rounded-full border border-white" alt="Avatar" />
                    <img src="https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=60&q=80" className="w-6 h-6 rounded-full border border-white" alt="Avatar" />
                  </div>
                  <span className="text-[11px] font-bold text-slate-500">+2.5K</span>
                </div>
                <span className="text-[11px] font-black text-[#1E3A8A]">Active Campaigns</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* LOGOS MARQUEE SECTION */}
      <section className="py-12 border-y border-slate-200/80 bg-white/80 backdrop-blur-md relative overflow-hidden">
        <div className="max-w-7xl mx-auto px-6 mb-6 text-center">
          <p className="text-[11px] font-extrabold uppercase tracking-[2.5px] text-slate-400">
            Trusted by Fast-Growing SaaS Brands & Marketing Teams
          </p>
        </div>

        <div className="relative w-full overflow-hidden">
          <div className="animate-marquee flex items-center justify-around space-x-12 min-w-max">
            {['stripe', 'DigitalOcean', 'intuit quickbooks', 'mailchimp', 'HubSpot', 'zendesk', 'stripe', 'DigitalOcean', 'intuit quickbooks', 'mailchimp', 'HubSpot', 'zendesk'].map((logo, idx) => (
              <span 
                key={idx} 
                className="text-xl font-black tracking-wider text-slate-400 hover:text-[#1E3A8A] transition-colors cursor-pointer opacity-70 hover:opacity-100 px-4"
                style={{ fontFamily: 'var(--font-heading)' }}
              >
                {logo}
              </span>
            ))}
          </div>
        </div>
      </section>

      {/* PRODUCT PAGES SHOWCASE */}
      <section id="showcase" className="px-6 lg:px-12 py-24 max-w-7xl mx-auto space-y-12">
        <div className="text-center space-y-3 max-w-2xl mx-auto">
          <div className="badge-blue bg-[#1F57F5]/10 text-[#1F57F5]">
            <span>Product Showcase</span>
          </div>
          <h2 className="text-3xl sm:text-4xl lg:text-5xl font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
            Experience the MailAlly Dashboard
          </h2>
          <p className="text-sm text-slate-500 font-medium">
            Designed with sleek, bordered browser mockups giving you full control over email outreach.
          </p>
        </div>

        {/* Tab Switcher */}
        <div className="flex justify-center gap-3">
          {[
            { id: 'analytics', label: 'Campaign Telemetry' },
            { id: 'copilot', label: 'AI Copilot Studio' },
            { id: 'automation', label: 'Visual Workflow Builder' }
          ].map(tab => (
            <button
              key={tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`px-6 py-3 rounded-xl text-xs font-black transition-all cursor-pointer ${
                activeTab === tab.id
                  ? 'bg-[#1F57F5] text-white shadow-lg shadow-blue-500/25'
                  : 'bg-white text-slate-600 border border-slate-200 hover:bg-slate-50'
              }`}
            >
              {tab.label}
            </button>
          ))}
        </div>

        {/* Browser Window Frame */}
        <div className="browser-window-frame max-w-5xl mx-auto">
          <div className="px-4 py-3 bg-slate-100 border-b border-slate-200 flex items-center justify-between">
            <div className="flex items-center space-x-2">
              <div className="w-3 h-3 rounded-full bg-rose-500" />
              <div className="w-3 h-3 rounded-full bg-amber-400" />
              <div className="w-3 h-3 rounded-full bg-emerald-500" />
            </div>

            <div className="bg-white px-6 py-1 rounded-lg border border-slate-200 text-[11px] font-mono text-slate-500 flex items-center gap-2 min-w-[320px] justify-center shadow-xs">
              <Lock className="w-3 h-3 text-emerald-500" />
              <span>https://app.mailally.io/{activeTab}</span>
            </div>

            <div className="flex items-center space-x-2 text-slate-400">
              <Maximize2 className="w-3.5 h-3.5" />
            </div>
          </div>

          <div className="p-8 bg-white min-h-[380px] flex items-center justify-center">
            {activeTab === 'analytics' && (
              <div className="w-full space-y-6">
                <div className="flex items-center justify-between border-b border-slate-100 pb-4">
                  <div>
                    <h3 className="font-black text-xl text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
                      Executive Telemetry Overview
                    </h3>
                    <p className="text-xs text-slate-400 font-medium">Aggregated live performance across active dispatches</p>
                  </div>
                  <button className="mailally-btn-accent text-xs py-2 px-4">Export Performance Report</button>
                </div>
                <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                  <div className="p-5 rounded-2xl bg-[#F0F7FF] border border-blue-100">
                    <span className="text-xs text-slate-500 font-bold">Deliverability Velocity</span>
                    <span className="text-3xl font-black block text-[#1F57F5] mt-1" style={{ fontFamily: 'var(--font-heading)' }}>99.8%</span>
                  </div>
                  <div className="p-5 rounded-2xl bg-[#F0F7FF] border border-cyan-100">
                    <span className="text-xs text-slate-500 font-bold">Avg. Open Rate</span>
                    <span className="text-3xl font-black block text-[#0088AA] mt-1" style={{ fontFamily: 'var(--font-heading)' }}>48.2%</span>
                  </div>
                  <div className="p-5 rounded-2xl bg-[#F0F7FF] border border-sky-100">
                    <span className="text-xs text-slate-500 font-bold">Click Conversion</span>
                    <span className="text-3xl font-black block text-[#2BAFF2] mt-1" style={{ fontFamily: 'var(--font-heading)' }}>14.6%</span>
                  </div>
                  <div className="p-5 rounded-2xl bg-[#F0F7FF] border border-slate-100">
                    <span className="text-xs text-slate-500 font-bold">Bounces Shielded</span>
                    <span className="text-3xl font-black block text-[#1E3A8A] mt-1" style={{ fontFamily: 'var(--font-heading)' }}>1,420</span>
                  </div>
                </div>
              </div>
            )}

            {activeTab === 'copilot' && (
              <div className="w-full space-y-4 max-w-2xl mx-auto">
                <div className="flex items-center space-x-2 text-xs font-bold text-[#1F57F5] bg-blue-50 px-3 py-1 rounded-full w-max">
                  <Sparkles className="w-3.5 h-3.5 text-[#2BAFF2]" />
                  <span>Gemini AI Copy Generator</span>
                </div>
                <div className="p-5 rounded-2xl bg-[#F0F7FF] border border-slate-200 text-xs font-mono text-slate-700 leading-relaxed shadow-xs space-y-2">
                  <p className="text-slate-400">// Prompt: Write a personalized SaaS outreach email for VP of Engineering</p>
                  <p className="font-sans font-black text-[#1E3A8A] text-base pt-1">
                    Subject: Accelerating email dispatch velocity for your backend infrastructure
                  </p>
                  <p className="font-sans text-[#334155] leading-relaxed">
                    Hi {"{FirstName}"}, noticed your team is scaling infrastructure. MailAlly connects AWS SES & SMTP routing to prevent bounce downtime...
                  </p>
                </div>
                <button className="mailally-btn-accent py-3 px-6 text-xs font-bold w-full">Regenerate Copy Variant</button>
              </div>
            )}

            {activeTab === 'automation' && (
              <div className="w-full flex flex-wrap items-center justify-center gap-4 py-8">
                <div className="p-5 rounded-2xl bg-blue-50 border border-blue-200 text-center w-44 shadow-xs">
                  <span className="text-[10px] font-black text-[#1F57F5] block uppercase">Trigger Node</span>
                  <span className="text-xs font-black text-[#1E3A8A]">Contact Uploaded</span>
                </div>
                <ChevronRight className="w-6 h-6 text-slate-300" />
                <div className="p-5 rounded-2xl bg-cyan-50 border border-cyan-200 text-center w-44 shadow-xs">
                  <span className="text-[10px] font-black text-[#0088AA] block uppercase">Action Node</span>
                  <span className="text-xs font-black text-[#1E3A8A]">Send Email #1</span>
                </div>
                <ChevronRight className="w-6 h-6 text-slate-300" />
                <div className="p-5 rounded-2xl bg-sky-50 border border-sky-200 text-center w-44 shadow-xs">
                  <span className="text-[10px] font-black text-[#2BAFF2] block uppercase">Condition</span>
                  <span className="text-xs font-black text-[#1E3A8A]">If Opened &gt; 24h</span>
                </div>
              </div>
            )}
          </div>
        </div>
      </section>

      {/* FEATURES BENTO GRID SECTION */}
      <section id="features" className="px-6 lg:px-12 py-24 max-w-7xl mx-auto space-y-12">
        <div className="text-center space-y-3 max-w-2xl mx-auto">
          <div className="badge-blue bg-[#1F57F5]/10 text-[#1F57F5]">
            <span>SaaS Platform</span>
          </div>
          <h2 className="text-3xl sm:text-4xl lg:text-5xl font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
            Powerful features tailored for cold email campaigns
          </h2>
          <p className="text-sm text-slate-500 leading-relaxed font-medium">
            Everything you need to automate email outreach with high deliverability, dynamic segmentation, and AI insights.
          </p>
        </div>

        {/* Bento Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div className="claude-card group space-y-4 hover:border-[#1F57F5]/40">
            <div className="w-12 h-12 rounded-2xl bg-blue-50 text-[#1F57F5] flex items-center justify-center font-bold border border-blue-100 group-hover:scale-110 transition-transform">
              <Send className="w-6 h-6" />
            </div>
            <h3 className="text-lg font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
              Multi-Engine Delivery
            </h3>
            <p className="text-xs text-slate-500 leading-relaxed">
              SMTP, AWS SES, and Brevo fallback routing ensures zero bounce downtime and maximum inbox placement.
            </p>
            <div className="pt-3 border-t border-slate-100 flex items-center justify-between text-[11px] font-bold text-slate-600">
              <span className="flex items-center gap-1 text-emerald-600">● AWS SES Active</span>
              <span className="text-slate-400">Fallback: SMTP</span>
            </div>
          </div>

          <div className="claude-card group space-y-4 hover:border-[#2BAFF2]/40">
            <div className="w-12 h-12 rounded-2xl bg-sky-50 text-[#2BAFF2] flex items-center justify-center font-bold border border-sky-100 group-hover:scale-110 transition-transform">
              <Users className="w-6 h-6" />
            </div>
            <h3 className="text-lg font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
              Dynamic Audience Rules
            </h3>
            <p className="text-xs text-slate-500 leading-relaxed">
              Real-time contact segment matching based on activity, custom properties, and engagement scores.
            </p>
            <div className="pt-3 border-t border-slate-100 flex items-center gap-2">
              <span className="px-2.5 py-0.5 rounded-full text-[10px] font-bold bg-blue-100 text-[#1F57F5]">Opened &gt; 3x</span>
              <span className="px-2.5 py-0.5 rounded-full text-[10px] font-bold bg-cyan-100 text-[#0088AA]">High Intent</span>
            </div>
          </div>

          <div className="claude-card group space-y-4 hover:border-[#00DDFF]/60">
            <div className="w-12 h-12 rounded-2xl bg-cyan-50 text-[#0088AA] flex items-center justify-center font-bold border border-cyan-100 group-hover:scale-110 transition-transform">
              <Sparkles className="w-6 h-6" />
            </div>
            <h3 className="text-lg font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
              AI Copilot Studio
            </h3>
            <p className="text-xs text-slate-500 leading-relaxed">
              Generate high-converting email copy and personal subject lines using Gemini AI models in seconds.
            </p>
            <div className="p-2.5 rounded-xl bg-cyan-50/50 border border-cyan-100 text-[11px] text-[#006688] font-medium italic">
              "Subject: Quick question regarding your Q3 pipeline..."
            </div>
          </div>

          <div className="claude-card group space-y-4 hover:border-amber-400">
            <div className="w-12 h-12 rounded-2xl bg-amber-50 text-amber-600 flex items-center justify-center font-bold border border-amber-100 group-hover:scale-110 transition-transform">
              <Clock className="w-6 h-6" />
            </div>
            <h3 className="text-lg font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
              Cron Scheduler
            </h3>
            <p className="text-xs text-slate-500 leading-relaxed">
              Schedule recurring dispatches and automated drip email sequences tailored to prospect timezones.
            </p>
            <div className="pt-3 border-t border-slate-100 flex items-center justify-between text-[11px] font-bold text-slate-500">
              <span>Smart Timezone Match</span>
              <span className="text-amber-600 font-extrabold">Next: 09:00 AM EST</span>
            </div>
          </div>

          <div className="claude-card group space-y-4 hover:border-emerald-400">
            <div className="w-12 h-12 rounded-2xl bg-emerald-50 text-emerald-600 flex items-center justify-center font-bold border border-emerald-100 group-hover:scale-110 transition-transform">
              <Shield className="w-6 h-6" />
            </div>
            <h3 className="text-lg font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
              Role-Based Isolation
            </h3>
            <p className="text-xs text-slate-500 leading-relaxed">
              Strict multi-tenant database isolation ensuring customer data privacy and role-based permissions.
            </p>
            <div className="pt-3 border-t border-slate-100 flex items-center gap-2 text-[11px] font-bold text-emerald-700">
              <Lock className="w-3.5 h-3.5" />
              <span>SOC2 & GDPR Compliant</span>
            </div>
          </div>

          <div className="claude-card group space-y-4 hover:border-[#1F57F5]/40">
            <div className="w-12 h-12 rounded-2xl bg-blue-50 text-[#1F57F5] flex items-center justify-center font-bold border border-blue-100 group-hover:scale-110 transition-transform">
              <Zap className="w-6 h-6" />
            </div>
            <h3 className="text-lg font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
              Live Telemetry
            </h3>
            <p className="text-xs text-slate-500 leading-relaxed">
              Track open rates, click conversions, and dispatch velocity in real time with live websocket feeds.
            </p>
            <div className="pt-3 border-t border-slate-100 flex items-center justify-between text-[11px] font-bold text-slate-600">
              <span>Dispatch Speed:</span>
              <span className="text-[#1F57F5] font-extrabold">2,400 emails / min</span>
            </div>
          </div>
        </div>
      </section>

      {/* WORKFLOW SECTION */}
      <section id="workflow" className="py-24 bg-white/80 border-y border-slate-200/80 relative">
        <div className="max-w-7xl mx-auto px-6 lg:px-12 space-y-16">
          <div className="text-center space-y-3 max-w-2xl mx-auto">
            <div className="badge-cyan bg-cyan-500/10 text-[#0088AA]">
              <span>5-Step Workflow</span>
            </div>
            <h2 className="text-3xl sm:text-4xl lg:text-5xl font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
              How MailAlly Works
            </h2>
            <p className="text-sm text-slate-500 font-medium">
              Simple steps to launch high-performing cold email campaigns.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-5 gap-4 relative">
            {[
              { num: '01', icon: Send, title: 'Import Contacts', desc: 'Upload your list or connect your CRM in seconds.' },
              { num: '02', icon: Mail, title: 'Create Campaign', desc: 'Draft engaging emails with AI copilot assistance.' },
              { num: '03', icon: Sliders, title: 'Set Rules & Schedule', desc: 'Define audience rules and schedule the best time.' },
              { num: '04', icon: Rocket, title: 'We Deliver', desc: 'Our multi-engine system ensures high deliverability.' },
              { num: '05', icon: BarChart3, title: 'Track & Optimize', desc: 'Analyze performance and improve with insights.' },
            ].map((step, idx) => {
              const Icon = step.icon;
              return (
                <div 
                  key={idx}
                  className="claude-card p-5 space-y-4 hover:border-[#1F57F5] group relative flex flex-col justify-between"
                >
                  <div className="flex items-center justify-between">
                    <div className="w-10 h-10 rounded-xl bg-blue-50 text-[#1F57F5] flex items-center justify-center font-bold border border-blue-100 group-hover:scale-110 transition-transform">
                      <Icon className="w-5 h-5" />
                    </div>
                    <span className="text-2xl font-black text-slate-200 group-hover:text-[#1F57F5]/40 transition-colors" style={{ fontFamily: 'var(--font-heading)' }}>
                      {step.num}
                    </span>
                  </div>

                  <div>
                    <h4 className="font-black text-base text-[#1E3A8A] mb-1" style={{ fontFamily: 'var(--font-heading)' }}>
                      {step.title}
                    </h4>
                    <p className="text-xs text-slate-500 leading-relaxed">
                      {step.desc}
                    </p>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      {/* PRICING SECTION */}
      <section id="pricing" className="px-6 lg:px-12 py-24 max-w-7xl mx-auto space-y-12">
        <div className="text-center space-y-3 max-w-2xl mx-auto">
          <div className="badge-blue bg-[#1F57F5]/10 text-[#1F57F5]">
            <span>Flexible Plans</span>
          </div>
          <h2 className="text-3xl sm:text-4xl lg:text-5xl font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
            Transparent SaaS Pricing
          </h2>
          <p className="text-sm text-slate-500 font-medium">
            Choose the plan that fits your needs. Upgrade or downgrade anytime.
          </p>

          <div className="pt-4 flex items-center justify-center space-x-4">
            <span className={`text-xs font-black ${!isYearly ? 'text-[#1E3A8A]' : 'text-slate-400'}`}>Monthly</span>
            <button 
              onClick={() => setIsYearly(!isYearly)}
              className="w-14 h-7 rounded-full bg-slate-200 p-1 flex items-center transition-colors relative cursor-pointer shadow-inner"
            >
              <div className={`w-5 h-5 rounded-full bg-gradient-to-r from-[#1F57F5] to-[#2BAFF2] shadow-md transform transition-transform ${isYearly ? 'translate-x-7' : 'translate-x-0'}`} />
            </button>
            <span className={`text-xs font-black ${isYearly ? 'text-[#1E3A8A]' : 'text-slate-400'}`}>
              Yearly <span className="ml-1 px-2.5 py-0.5 rounded-full text-[10px] font-extrabold bg-cyan-100 text-[#0088AA]">Save 20%</span>
            </span>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 items-stretch">
          <div className="claude-card p-8 space-y-6 flex flex-col justify-between hover:border-slate-300">
            <div className="space-y-4">
              <span className="text-xs font-black uppercase tracking-wider text-slate-400">Starter Plan</span>
              <h3 className="text-2xl font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>Starter</h3>
              <p className="text-xs text-slate-500">Perfect for getting started with cold outreach.</p>
              <div className="flex items-baseline space-x-1 pt-2">
                <span className="text-4xl font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
                  ${isYearly ? '23' : '29'}
                </span>
                <span className="text-xs text-slate-400 font-bold">/mo</span>
              </div>
              <ul className="space-y-3 pt-4 text-xs text-slate-600 font-medium">
                <li className="flex items-center gap-2.5"><CheckIcon className="w-4 h-4 text-emerald-500 flex-shrink-0" /> 5,000 Emails / Month</li>
                <li className="flex items-center gap-2.5"><CheckIcon className="w-4 h-4 text-emerald-500 flex-shrink-0" /> Basic Campaign Telemetry</li>
                <li className="flex items-center gap-2.5"><CheckIcon className="w-4 h-4 text-emerald-500 flex-shrink-0" /> Standard Email Templates</li>
                <li className="flex items-center gap-2.5"><CheckIcon className="w-4 h-4 text-emerald-500 flex-shrink-0" /> Community Support</li>
              </ul>
            </div>
            <button onClick={() => navigate('/register')} className="mailally-btn-secondary w-full py-3.5 text-xs font-extrabold">Select Starter</button>
          </div>

          <div className="mailally-card-highlight p-8 space-y-6 flex flex-col justify-between relative border-2 border-[#1F57F5]">
            <span className="absolute -top-3 left-1/2 -translate-x-1/2 badge-blue bg-gradient-to-r from-[#1F57F5] to-[#2BAFF2] text-white border-none shadow-md text-[10px]">
              Most Popular
            </span>
            <div className="space-y-4">
              <span className="text-xs font-black uppercase tracking-wider text-[#1F57F5]">Pro Plan</span>
              <h3 className="text-2xl font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>Professional</h3>
              <p className="text-xs text-slate-500">For growing businesses scaling email volume.</p>
              <div className="flex items-baseline space-x-1 pt-2">
                <span className="text-4xl font-black text-[#1F57F5]" style={{ fontFamily: 'var(--font-heading)' }}>
                  ${isYearly ? '79' : '99'}
                </span>
                <span className="text-xs text-slate-400 font-bold">/mo</span>
              </div>
              <ul className="space-y-3 pt-4 text-xs text-slate-800 font-bold">
                <li className="flex items-center gap-2.5"><CheckIcon className="w-4 h-4 text-[#1F57F5] flex-shrink-0" /> 25,000 Emails / Month</li>
                <li className="flex items-center gap-2.5"><CheckIcon className="w-4 h-4 text-[#1F57F5] flex-shrink-0" /> Real-Time Telemetry & SLA</li>
                <li className="flex items-center gap-2.5"><CheckIcon className="w-4 h-4 text-[#1F57F5] flex-shrink-0" /> Gemini AI Copilot Studio</li>
                <li className="flex items-center gap-2.5"><CheckIcon className="w-4 h-4 text-[#1F57F5] flex-shrink-0" /> Priority Support</li>
                <li className="flex items-center gap-2.5"><CheckIcon className="w-4 h-4 text-[#1F57F5] flex-shrink-0" /> Custom Domain Tracking</li>
              </ul>
            </div>
            <button onClick={() => navigate('/register')} className="mailally-btn-accent w-full py-3.5 text-xs font-extrabold">Get Started Pro</button>
          </div>

          <div className="claude-card p-8 space-y-6 flex flex-col justify-between hover:border-slate-300">
            <div className="space-y-4">
              <span className="text-xs font-black uppercase tracking-wider text-slate-400">Enterprise</span>
              <h3 className="text-2xl font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>Enterprise</h3>
              <p className="text-xs text-slate-500">For large teams with custom SLA requirements.</p>
              <div className="flex items-baseline space-x-1 pt-2">
                <span className="text-4xl font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
                  ${isYearly ? '239' : '299'}
                </span>
                <span className="text-xs text-slate-400 font-bold">/mo</span>
              </div>
              <ul className="space-y-3 pt-4 text-xs text-slate-600 font-medium">
                <li className="flex items-center gap-2.5"><CheckIcon className="w-4 h-4 text-emerald-500 flex-shrink-0" /> Unlimited Emails</li>
                <li className="flex items-center gap-2.5"><CheckIcon className="w-4 h-4 text-emerald-500 flex-shrink-0" /> Dedicated IPs</li>
                <li className="flex items-center gap-2.5"><CheckIcon className="w-4 h-4 text-emerald-500 flex-shrink-0" /> Advanced Security & Isolation</li>
                <li className="flex items-center gap-2.5"><CheckIcon className="w-4 h-4 text-emerald-500 flex-shrink-0" /> Dedicated Success Manager</li>
                <li className="flex items-center gap-2.5"><CheckIcon className="w-4 h-4 text-emerald-500 flex-shrink-0" /> Custom Onboarding & SLA</li>
              </ul>
            </div>
            <button onClick={() => navigate('/register')} className="mailally-btn-secondary w-full py-3.5 text-xs font-extrabold">Contact Sales</button>
          </div>
        </div>
      </section>

      {/* INTEGRATIONS SECTION */}
      <section id="integrations" className="py-20 bg-white border-y border-slate-200/80">
        <div className="max-w-7xl mx-auto px-6 lg:px-12 space-y-12 text-center">
          <div className="space-y-3 max-w-xl mx-auto">
            <h2 className="text-3xl font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
              Integrate with the best
            </h2>
            <p className="text-xs text-slate-500 font-medium">
              Seamlessly connect MailAlly with the <span className="text-[#1F57F5] font-bold">tools you use</span> every day.
            </p>
          </div>

          <div className="flex flex-wrap items-center justify-center gap-6">
            {['webflow', 'zapier', 'WWR', 'Discord', 'Google', 'Slack'].map((tool, idx) => (
              <div 
                key={idx}
                className="px-8 py-4 rounded-2xl bg-[#F0F7FF] border border-slate-200/80 text-slate-700 font-extrabold text-sm hover:border-[#1F57F5] hover:text-[#1F57F5] transition-all cursor-pointer shadow-xs hover:-translate-y-1"
              >
                {tool}
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* FAQ SECTION */}
      <section id="faq" className="px-6 lg:px-12 py-24 max-w-4xl mx-auto space-y-10">
        <div className="text-center space-y-3">
          <div className="badge-blue bg-[#1F57F5]/10 text-[#1F57F5]">
            <span>Frequently Asked Questions</span>
          </div>
          <h2 className="text-3xl sm:text-4xl font-black text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
            Got Questions? We Have Answers.
          </h2>
        </div>

        <div className="space-y-4">
          {[
            { q: 'How does MailAlly ensure 99.4% email deliverability?', a: 'MailAlly combines Multi-Engine Fallback routing across AWS SES, SMTP, and Brevo with dynamic domain warmups and real-time SPF/DKIM authentication checks.' },
            { q: 'Can I integrate my existing CRM with MailAlly?', a: 'Yes! We offer 1-click integrations for HubSpot, Salesforce, Webflow, and custom Webhooks for custom pipelines.' },
            { q: 'How does the AI Copilot help draft cold emails?', a: 'Our Gemini AI model analyzes high-performing templates and custom prospect properties to generate personalized headlines and body copy.' },
            { q: 'Is there a free trial available?', a: 'Yes, we offer a 14-day free trial on all plans without requiring a credit card.' }
          ].map((faq, idx) => (
            <div key={idx} className="claude-card p-5 cursor-pointer" onClick={() => toggleFaq(idx)}>
              <div className="flex items-center justify-between">
                <h4 className="font-black text-base text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>{faq.q}</h4>
                {openFaq === idx ? <ChevronUp className="w-4 h-4 text-[#1F57F5]" /> : <ChevronDown className="w-4 h-4 text-slate-400" />}
              </div>
              {openFaq === idx && (
                <p className="text-xs text-slate-500 pt-3 border-t border-slate-100 mt-3 leading-relaxed font-medium">
                  {faq.a}
                </p>
              )}
            </div>
          ))}
        </div>
      </section>

      {/* VIBRANT ICE BLUE CTA BANNER (NO BLACK) */}
      <section className="px-6 lg:px-12 py-16 max-w-7xl mx-auto">
        <div 
          className="rounded-3xl p-10 sm:p-16 text-white relative overflow-hidden space-y-6 text-center shadow-2xl border border-white/20"
          style={{
            background: 'linear-gradient(135deg, #1E3A8A 0%, #1F57F5 50%, #2BAFF2 100%)'
          }}
        >
          {/* Subtle Ambient Radial Glows */}
          <div className="absolute top-[-50px] left-[-50px] w-72 h-72 rounded-full bg-white/20 blur-3xl pointer-events-none" />
          <div className="absolute bottom-[-50px] right-[-50px] w-72 h-72 rounded-full bg-[#00DDFF]/30 blur-3xl pointer-events-none" />

          {/* Floating Rocket Icon */}
          <div className="w-16 h-16 rounded-2xl bg-white/15 backdrop-blur-md border border-white/30 text-[#00DDFF] flex items-center justify-center mx-auto shadow-lg">
            <Rocket className="w-8 h-8" />
          </div>

          <h2 className="text-3xl sm:text-5xl font-black tracking-tight text-white max-w-2xl mx-auto leading-tight" style={{ fontFamily: 'var(--font-heading)' }}>
            Ready to skyrocket your outreach?
          </h2>

          <p className="text-sm text-cyan-100 max-w-xl mx-auto font-medium">
            Join thousands of businesses using MailAlly to build better connections and boost sales conversion.
          </p>

          <div className="flex flex-wrap items-center justify-center gap-4 pt-4">
            <button 
              onClick={() => navigate('/register')} 
              className="bg-white text-[#1F57F5] hover:bg-[#F0F7FF] py-4 px-8 text-sm font-black rounded-2xl shadow-lg transition-all"
            >
              <span>Start Free Trial</span>
              <ArrowRight className="w-4 h-4 ml-2 inline-block" />
            </button>
            <button 
              onClick={() => navigate('/dashboard')} 
              className="px-8 py-4 rounded-2xl bg-white/15 hover:bg-white/25 border border-white/30 text-white font-black text-sm transition-all cursor-pointer backdrop-blur-md"
            >
              Book a Demo
            </button>
          </div>
        </div>
      </section>

      {/* FOOTER - ICE BLUE THEME (NO BLACK) */}
      <footer 
        className="text-white border-t border-blue-900/30 pt-16 pb-12 px-6 lg:px-12 relative"
        style={{
          background: 'linear-gradient(180deg, #1E3A8A 0%, #172554 100%)'
        }}
      >
        <div className="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-5 gap-10 pb-12 border-b border-blue-800/40 text-xs">
          <div className="md:col-span-2 space-y-4">
            <div className="flex items-center space-x-3 cursor-pointer" onClick={scrollToTop}>
              <CometLogo size="md" />
              <span className="text-2xl font-black tracking-tight text-white" style={{ fontFamily: 'var(--font-heading)' }}>
                MailAlly<span className="text-[#00DDFF]">.</span>
              </span>
            </div>
            <p className="text-cyan-100/80 max-w-sm leading-relaxed font-medium">
              The enterprise email marketing platform that helps you automate outreach, engage smarter, and grow faster.
            </p>
            <div className="flex space-x-4 text-cyan-200/80 pt-2">
              <Linkedin className="w-4 h-4 hover:text-[#00DDFF] cursor-pointer transition-colors" />
              <Globe className="w-4 h-4 hover:text-[#00DDFF] cursor-pointer transition-colors" />
              <Mail className="w-4 h-4 hover:text-white cursor-pointer transition-colors" />
            </div>
          </div>

          <div className="space-y-3">
            <h5 className="font-black text-white uppercase text-[11px] tracking-wider" style={{ fontFamily: 'var(--font-heading)' }}>Product</h5>
            <ul className="space-y-2 text-cyan-100/80 font-medium">
              <li><a href="#showcase" className="hover:text-white transition-colors">Showcase</a></li>
              <li><a href="#features" className="hover:text-white transition-colors">Features</a></li>
              <li><a href="#integrations" className="hover:text-white transition-colors">Integrations</a></li>
              <li><a href="#pricing" className="hover:text-white transition-colors">Pricing</a></li>
            </ul>
          </div>

          <div className="space-y-3">
            <h5 className="font-black text-white uppercase text-[11px] tracking-wider" style={{ fontFamily: 'var(--font-heading)' }}>Company</h5>
            <ul className="space-y-2 text-cyan-100/80 font-medium">
              <li><a href="#" className="hover:text-white transition-colors">About Us</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Careers</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Blog</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Contact</a></li>
            </ul>
          </div>

          <div className="space-y-3">
            <h5 className="font-black text-white uppercase text-[11px] tracking-wider" style={{ fontFamily: 'var(--font-heading)' }}>Legal</h5>
            <ul className="space-y-2 text-cyan-100/80 font-medium">
              <li><a href="#" className="hover:text-white transition-colors">Privacy Policy</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Terms of Service</a></li>
              <li><a href="#" className="hover:text-white transition-colors">Cookie Policy</a></li>
            </ul>
          </div>
        </div>

        <div className="max-w-7xl mx-auto pt-8 flex flex-col sm:flex-row items-center justify-between text-xs text-cyan-200/70 gap-4 font-medium">
          <p>© 2026 MailAlly Enterprise SaaS. All rights reserved.</p>
          <button 
            onClick={scrollToTop} 
            className="w-9 h-9 rounded-full bg-blue-800/80 hover:bg-[#1F57F5] text-white flex items-center justify-center transition-colors shadow-sm cursor-pointer"
          >
            ↑
          </button>
        </div>
      </footer>
    </div>
  );
};
