import React, { useState, useEffect, useRef } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import {
  Sparkles, ArrowRight, Check, Star, Globe, Zap, Shield,
  TrendingUp, BarChart3, Users, Clock, Mail, CheckCircle2,
  ChevronRight, ArrowUpRight, Cpu, Layers, RefreshCw, Lock,
  Sliders, MessageSquare, Play, HelpCircle, Send
} from 'lucide-react';
import {
  ResponsiveContainer, AreaChart, Area, XAxis, YAxis, Tooltip, CartesianGrid
} from 'recharts';
import { CometLogo } from '../components/common/CometLogo';

const mockChartData = [
  { day: 'Mon', sent: 12000, opens: 5200 },
  { day: 'Tue', sent: 18000, opens: 8100 },
  { day: 'Wed', sent: 15000, opens: 7200 },
  { day: 'Thu', sent: 22000, opens: 10400 },
  { day: 'Fri', sent: 28000, opens: 13200 },
  { day: 'Sat', sent: 19000, opens: 9100 },
  { day: 'Sun', sent: 24890, opens: 11400 },
];

const trustedCompanies = [
  "Mailchimp",
  "HubSpot",
  "zendesk",
  "stripe",
  "DigitalOcean",
  "IntUIT",
  "pipedrive"
];

export const AgencyLandingPage = () => {
  const navigate = useNavigate();
  const [isYearly, setIsYearly] = useState(true);
  const [openFaq, setOpenFaq] = useState(null);
  const [isScrolled, setIsScrolled] = useState(false);
  const testimonialsScrollRef = useRef(null);

  useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY > 40) {
        setIsScrolled(true);
      } else {
        setIsScrolled(false);
      }
    };
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const toggleFaq = (index) => {
    setOpenFaq(openFaq === index ? null : index);
  };

  const scrollTestimonialsLeft = () => {
    if (testimonialsScrollRef.current) {
      testimonialsScrollRef.current.scrollBy({ left: -420, behavior: 'smooth' });
    }
  };

  const scrollTestimonialsRight = () => {
    if (testimonialsScrollRef.current) {
      testimonialsScrollRef.current.scrollBy({ left: 420, behavior: 'smooth' });
    }
  };

  return (
    <div className="min-h-screen bg-white text-[#111111] font-sans antialiased relative overflow-x-hidden selection:bg-[#FF7EB6]/20 selection:text-[#111111]">
      
      {/* ─── FLOATING PASTEL AMBIENT GLOWS ─── */}
      <div className="pointer-events-none absolute top-[-100px] left-1/2 -translate-x-1/2 w-[1000px] h-[550px] bg-gradient-to-r from-[#FF7EB6]/15 via-[#C084FC]/15 to-[#FFB88C]/15 blur-[120px] rounded-full -z-10" />
      <div className="pointer-events-none absolute top-[900px] left-[-200px] w-[600px] h-[600px] bg-gradient-to-tr from-[#A7F3D0]/18 via-[#BDEAFE]/18 to-[#FF7EB6]/12 blur-[130px] rounded-full -z-10" />
      <div className="pointer-events-none absolute top-[2200px] right-[-200px] w-[650px] h-[650px] bg-gradient-to-br from-[#FFEAA7]/18 via-[#FFC98B]/18 to-[#C084FC]/15 blur-[140px] rounded-full -z-10" />
      <div className="pointer-events-none absolute top-[3600px] left-1/2 -translate-x-1/2 w-[900px] h-[600px] bg-gradient-to-r from-[#FF7EB6]/14 via-[#C084FC]/14 to-[#A7F3D0]/14 blur-[130px] rounded-full -z-10" />

      {/* ─── NAVIGATION BAR (DYNAMIC SCROLL-AWARE ANIMATED FLOATING NAVBAR) ─── */}
      <header
        className={`fixed top-4 left-0 right-0 z-50 px-4 sm:px-6 mx-auto transition-all duration-500 ease-out ${
          isScrolled ? 'max-w-[860px]' : 'max-w-[1040px]'
        }`}
      >
        <div className="relative group">
          {/* Gradient Color Shadow Glow Behind Navbar - Intensifies & scales on scroll */}
          <div
            className={`absolute -inset-1.5 bg-gradient-to-r from-[#FF7EB6]/40 via-[#C084FC]/40 to-[#FFB88C]/40 blur-xl rounded-full -z-10 transition-all duration-500 ${
              isScrolled ? 'opacity-100 scale-100' : 'opacity-70 scale-95'
            } group-hover:scale-105 group-hover:opacity-100`}
          />
          
          <nav
            className={`bg-white/95 backdrop-blur-xl border border-[#111111] rounded-full flex items-center justify-between transition-all duration-500 ease-out ${
              isScrolled
                ? 'px-5 py-2 shadow-[0_12px_35px_rgba(0,0,0,0.08)]'
                : 'px-6 py-3 shadow-[0_8px_25px_rgba(0,0,0,0.04)]'
            }`}
          >
            
            {/* Brand Logo */}
            <Link to="/" className="flex items-center gap-2 group">
              <CometLogo size="sm" />
              <span className="text-lg font-extrabold tracking-tight text-[#111111]">
                MailAlly<span className="text-[#FF7EB6]">.</span>
              </span>
            </Link>

            {/* Navigation Links */}
            <div className="hidden md:flex items-center gap-6 text-xs font-bold text-[#111111]">
              <a href="#features" className="hover:text-[#FF7EB6] transition-colors">
                Features
              </a>
              <a href="#workflow" className="hover:text-[#C084FC] transition-colors">
                Workflow
              </a>
              <a href="#testimonials" className="hover:text-[#FFB88C] transition-colors">
                Testimonials
              </a>
              <a href="#pricing" className="hover:text-[#FF7EB6] transition-colors">
                Pricing
              </a>
              <a href="#faq" className="hover:text-[#C084FC] transition-colors">
                FAQ
              </a>
            </div>

            {/* Actions */}
            <div className="flex items-center gap-3">
              <Link
                to="/login"
                className="text-xs font-bold text-[#111111] hover:text-[#5F6368] transition-colors hidden sm:block px-2 py-1"
              >
                Log In
              </Link>
              <button
                onClick={() => navigate('/register')}
                className={`bg-[#111111] hover:bg-[#222222] text-white text-xs font-extrabold rounded-xl transition-all duration-300 shadow-sm hover:shadow-md hover:-translate-y-0.5 cursor-pointer ${
                  isScrolled ? 'px-3.5 py-1.5' : 'px-4 py-2'
                }`}
              >
                Start Free Trial
              </button>
            </div>
          </nav>
        </div>
      </header>

      {/* ─── HERO SECTION ─── */}
      <section className="pt-32 pb-28 md:pt-40 md:pb-36 px-6 lg:px-12 max-w-[1280px] mx-auto relative">
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-16 items-center">
          
          {/* Hero Left Content */}
          <div className="lg:col-span-6 space-y-8">
            
            {/* Pill Badge with Soft Pastel Glow */}
            <div className="relative inline-block">
              <div className="absolute -inset-1 bg-gradient-to-r from-[#FF7EB6]/30 to-[#C084FC]/30 blur-md rounded-full -z-10" />
              <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full border border-[#111111] bg-white shadow-xs text-xs font-bold text-[#111111]">
                <span className="w-2 h-2 rounded-full bg-[#FF7EB6] animate-pulse" />
                <span>AI POWERED EMAIL MARKETING</span>
              </div>
            </div>

            {/* Headline */}
            <h1 className="text-4xl sm:text-5xl lg:text-[64px] font-extrabold tracking-tight leading-[1.08] text-[#111111]">
              Boost your sales with{' '}
              <span className="bg-gradient-to-r from-[#FF7EB6] via-[#C084FC] to-[#FFB88C] bg-clip-text text-transparent">
                smarter email campaigns
              </span>
            </h1>

            {/* Subtitle Paragraph */}
            <p className="text-base sm:text-lg text-[#5F6368] leading-relaxed max-w-lg font-medium">
              MailAlly helps you automate outreach, engage prospects, and close more deals with real-time analytics, AI assistance, and intelligent workflows.
            </p>

            {/* CTA Buttons with Gradient Shadow behind Secondary Button */}
            <div className="flex flex-wrap items-center gap-4 pt-2">
              <button
                onClick={() => navigate('/register')}
                className="bg-[#111111] hover:bg-[#222222] text-white font-bold text-sm px-7 py-3.5 rounded-xl transition-all duration-300 shadow-sm hover:shadow-lg hover:-translate-y-0.5 flex items-center gap-2 cursor-pointer"
              >
                <span>Start Free Trial</span>
                <ArrowRight className="w-4 h-4" />
              </button>

              <div className="relative inline-block">
                <div className="absolute -inset-1 bg-gradient-to-r from-[#C084FC]/30 to-[#FFB88C]/30 blur-lg rounded-xl -z-10" />
                <button
                  onClick={() => navigate('/landing')}
                  className="bg-white hover:bg-[#F9F9F9] text-[#111111] border border-[#111111] font-bold text-sm px-7 py-3.5 rounded-xl transition-all duration-300 hover:-translate-y-0.5 cursor-pointer shadow-xs"
                >
                  Book a Demo
                </button>
              </div>
            </div>

            {/* Metrics & Social Proof */}
            <div className="pt-6 border-t border-[#ECECEC] grid grid-cols-3 gap-6">
              <div className="flex items-center gap-2">
                <div className="w-8 h-8 rounded-lg bg-[#FF7EB6]/10 flex items-center justify-center text-[#FF7EB6]">
                  <Globe className="w-4 h-4" />
                </div>
                <div>
                  <div className="text-sm font-extrabold text-[#111111]">120+</div>
                  <div className="text-[11px] text-[#8A8A8A] font-semibold">Countries</div>
                </div>
              </div>

              <div className="flex items-center gap-2">
                <div className="w-8 h-8 rounded-lg bg-[#C084FC]/10 flex items-center justify-center text-[#C084FC]">
                  <Zap className="w-4 h-4" />
                </div>
                <div>
                  <div className="text-sm font-extrabold text-[#111111]">99.4%</div>
                  <div className="text-[11px] text-[#8A8A8A] font-semibold">Deliverability</div>
                </div>
              </div>

              <div className="flex items-center gap-2">
                <div className="w-8 h-8 rounded-lg bg-[#FFB88C]/10 flex items-center justify-center text-[#FFB88C]">
                  <Star className="w-4 h-4 fill-current" />
                </div>
                <div>
                  <div className="text-sm font-extrabold text-[#111111]">4.9</div>
                  <div className="text-[11px] text-[#8A8A8A] font-semibold">Client Rating</div>
                </div>
              </div>
            </div>

            {/* Avatar Trust Pill */}
            <div className="flex items-center gap-3 pt-1">
              <div className="flex -space-x-2 overflow-hidden">
                <img className="inline-block h-7 w-7 rounded-full ring-2 ring-white object-cover" src="https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100&auto=format&fit=crop&q=80" alt="User" />
                <img className="inline-block h-7 w-7 rounded-full ring-2 ring-white object-cover" src="https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&auto=format&fit=crop&q=80" alt="User" />
                <img className="inline-block h-7 w-7 rounded-full ring-2 ring-white object-cover" src="https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100&auto=format&fit=crop&q=80" alt="User" />
              </div>
              <span className="text-xs font-semibold text-[#5F6368]">
                <strong className="text-[#111111]">2,157+</strong> businesses trust MailAlly
              </span>
            </div>

          </div>

          {/* Hero Right Content - Floating Dashboard Mockup with Pastel Gradient Color Shadow Glow */}
          <div className="lg:col-span-6 relative">
            
            {/* Vibrant Pastel Gradient Shadow Glow Behind Mockup */}
            <div className="absolute -inset-4 bg-gradient-to-tr from-[#FF7EB6]/40 via-[#C084FC]/40 to-[#FFB88C]/40 blur-3xl rounded-[36px] -z-10" />

            {/* White Dashboard Mockup Container with Thin 1px Black Border */}
            <div className="bg-white border border-[#111111] rounded-[24px] p-6 shadow-[0_20px_50px_rgba(0,0,0,0.06)] relative">
              
              {/* Mockup Header Bar */}
              <div className="flex items-center justify-between border-b border-[#ECECEC] pb-4 mb-6">
                <div className="flex items-center gap-3">
                  <div className="w-8 h-8 rounded-xl bg-[#111111] text-white flex items-center justify-center">
                    <BarChart3 className="w-4 h-4" />
                  </div>
                  <div>
                    <h3 className="text-sm font-bold text-[#111111]">Campaign Performance</h3>
                    <p className="text-[11px] text-[#8A8A8A] font-medium">Real-time engagement telemetry</p>
                  </div>
                </div>
                <div className="flex items-center gap-2 bg-[#F9F9F9] border border-[#111111]/20 px-3 py-1.5 rounded-lg text-xs font-bold text-[#111111]">
                  <span>Last 7 Days</span>
                </div>
              </div>

              {/* KPI Summary Cards inside Mockup */}
              <div className="grid grid-cols-3 gap-4 mb-6">
                <div className="bg-[#F9F9F9] border border-[#ECECEC] rounded-xl p-3.5">
                  <div className="text-[11px] text-[#8A8A8A] font-bold">Emails Sent</div>
                  <div className="text-lg font-black text-[#111111] mt-0.5">24,890</div>
                  <div className="text-[10px] text-emerald-600 font-bold mt-1 flex items-center gap-0.5">
                    ↑ 12.9%
                  </div>
                </div>

                <div className="bg-[#F9F9F9] border border-[#ECECEC] rounded-xl p-3.5">
                  <div className="text-[11px] text-[#8A8A8A] font-bold">Open Rate</div>
                  <div className="text-lg font-black text-[#111111] mt-0.5">42.7%</div>
                  <div className="text-[10px] text-emerald-600 font-bold mt-1 flex items-center gap-0.5">
                    ↑ 6.2%
                  </div>
                </div>

                <div className="bg-[#F9F9F9] border border-[#ECECEC] rounded-xl p-3.5">
                  <div className="text-[11px] text-[#8A8A8A] font-bold">Reply Rate</div>
                  <div className="text-lg font-black text-[#111111] mt-0.5">11.3%</div>
                  <div className="text-[10px] text-emerald-600 font-bold mt-1 flex items-center gap-0.5">
                    ↑ 8.7%
                  </div>
                </div>
              </div>

              {/* Recharts Area Chart */}
              <div className="h-56 w-full pt-2">
                <ResponsiveContainer width="100%" height="100%">
                  <AreaChart data={mockChartData}>
                    <defs>
                      <linearGradient id="heroGradient" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="5%" stopColor="#FF7EB6" stopOpacity={0.4} />
                        <stop offset="95%" stopColor="#C084FC" stopOpacity={0.0} />
                      </linearGradient>
                    </defs>
                    <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#ECECEC" />
                    <XAxis dataKey="day" axisLine={false} tickLine={false} tick={{ fontSize: 11, fill: '#8A8A8A' }} />
                    <YAxis hide />
                    <Tooltip contentStyle={{ background: '#111111', color: '#fff', borderRadius: '8px', fontSize: '12px', border: 'none' }} />
                    <Area type="monotone" dataKey="opens" stroke="#FF7EB6" strokeWidth={3} fillOpacity={1} fill="url(#heroGradient)" />
                  </AreaChart>
                </ResponsiveContainer>
              </div>

              {/* Floating Stat Pill (Top Right with Soft Gradient Shadow) */}
              <div className="absolute -top-5 -right-5 hidden sm:flex z-10">
                <div className="absolute -inset-2 bg-gradient-to-r from-[#FF7EB6]/40 to-[#C084FC]/40 blur-xl rounded-2xl -z-10" />
                <div className="bg-white border border-[#111111] rounded-2xl p-3 flex items-center gap-3 shadow-sm">
                  <div className="w-9 h-9 rounded-xl bg-gradient-to-tr from-[#FF7EB6] to-[#C084FC] flex items-center justify-center text-white font-black text-xs shadow-sm">
                    42.7%
                  </div>
                  <div>
                    <div className="text-xs font-bold text-[#111111]">Open Rate</div>
                    <div className="text-[10px] text-[#8A8A8A] font-semibold">Top 1% Industry Standard</div>
                  </div>
                </div>
              </div>

              {/* Floating Stat Pill (Bottom Left with Soft Gradient Shadow) */}
              <div className="absolute -bottom-5 -left-5 hidden sm:flex z-10">
                <div className="absolute -inset-2 bg-gradient-to-r from-[#C084FC]/40 to-[#A7F3D0]/40 blur-xl rounded-2xl -z-10" />
                <div className="bg-white border border-[#111111] rounded-2xl p-3 flex items-center gap-3 shadow-sm">
                  <div className="w-9 h-9 rounded-xl bg-[#111111] flex items-center justify-center text-white shadow-sm">
                    <TrendingUp className="w-4 h-4" />
                  </div>
                  <div>
                    <div className="text-xs font-extrabold text-[#111111]">+$18,240</div>
                    <div className="text-[10px] text-emerald-600 font-bold">Revenue Generated</div>
                  </div>
                </div>
              </div>

            </div>
          </div>

        </div>
      </section>

      {/* ─── TRUSTED BY FAST-GROWING COMPANIES (BORDERLESS FLOATING MARQUEE) ─── */}
      <section className="py-14 px-6 max-w-[1280px] mx-auto">
        <div className="relative text-center">
          {/* Ambient Soft Pastel Glow Behind Section */}
          <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-full max-w-4xl h-24 bg-gradient-to-r from-[#FF7EB6]/20 via-[#C084FC]/20 to-[#A7F3D0]/20 blur-3xl rounded-full -z-10 pointer-events-none" />

          {/* Section Sub-heading */}
          <p className="text-[11px] font-extrabold tracking-[0.2em] text-[#8A8A8A] uppercase mb-8">
            TRUSTED BY FAST-GROWING COMPANIES
          </p>

          {/* Continuous Borderless Infinite Marquee Banner */}
          <div className="overflow-hidden relative w-full py-4">
            <div className="absolute left-0 top-0 bottom-0 w-24 bg-gradient-to-r from-white to-transparent z-10 pointer-events-none" />
            <div className="absolute right-0 top-0 bottom-0 w-24 bg-gradient-to-l from-white to-transparent z-10 pointer-events-none" />
            
            <div className="flex w-max items-center gap-16 md:gap-24 animate-marquee">
              {[...trustedCompanies, ...trustedCompanies, ...trustedCompanies].map((company, index) => (
                <span
                  key={index}
                  className="text-xl md:text-2xl font-black text-[#111111] tracking-tight opacity-75 hover:opacity-100 hover:scale-105 transition-all duration-300 cursor-pointer select-none"
                >
                  {company}
                </span>
              ))}
            </div>
          </div>
        </div>
      </section>

      {/* ─── FEATURE SECTION (EVERY CARD WITH CRISP BLACK BORDER & PASTEL GRADIENT SHADOW) ─── */}
      <section id="features" className="py-28 md:py-36 px-6 lg:px-12 max-w-[1280px] mx-auto">
        
        {/* Section Header */}
        <div className="text-center max-w-2xl mx-auto space-y-4 mb-20">
          <div className="relative inline-block">
            <div className="absolute -inset-1 bg-gradient-to-r from-[#C084FC]/30 to-[#FF7EB6]/30 blur-md rounded-full -z-10" />
            <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full border border-[#111111] bg-white shadow-xs text-xs font-bold text-[#111111]">
              <span className="w-2 h-2 rounded-full bg-[#C084FC]" />
              <span>POWERFUL FEATURES</span>
            </div>
          </div>
          <h2 className="text-3xl sm:text-4xl lg:text-5xl font-extrabold tracking-tight text-[#111111]">
            Everything you need to run successful email campaigns
          </h2>
          <p className="text-base text-[#5F6368] font-medium">
            Designed for growth-focused sales teams, marketing agencies, and modern SaaS startups.
          </p>
        </div>

        {/* 3x2 Grid of Feature Cards (Two-Tone Pastel Cards with White Border Inset Frame) */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          
          {/* Card 1: Multi-Engine Delivery (Soft Pink Theme) */}
          <div className="relative group flex">
            <div className="absolute -inset-2 bg-gradient-to-br from-[#FF7EB6]/45 via-[#C084FC]/35 to-transparent blur-2xl rounded-[30px] -z-10 opacity-80 group-hover:opacity-100 group-hover:scale-105 transition-all duration-300" />
            
            {/* Outer White Card Frame with Thin 1px Black Border & Inner Padding */}
            <div className="bg-white border border-[#111111] rounded-[26px] p-3.5 flex flex-col justify-between w-full transition-all duration-300 group-hover:-translate-y-1.5 group-hover:scale-[1.02] shadow-[0_15px_30px_rgba(0,0,0,0.04)]">
              
              {/* Inner Colored Pastel Block with Distinct Rounded Corners */}
              <div className="bg-[#FFF0F5] border border-[#FF7EB6]/20 p-6 rounded-[20px] flex-1 flex flex-col justify-between mb-3">
                <div>
                  <div className="flex items-center justify-between mb-4">
                    <h3 className="text-2xl font-black text-[#111111] tracking-tight">Multi-Engine Delivery</h3>
                    <div className="w-9 h-9 rounded-xl bg-white text-[#FF7EB6] flex items-center justify-center border border-[#FF7EB6]/30 shadow-xs">
                      <Zap className="w-4.5 h-4.5" />
                    </div>
                  </div>
                  <p className="text-xs text-[#5F6368] leading-relaxed font-medium mb-6">
                    Utilize Amazon SES, SendGrid, Mailgun and custom SMTP relays for maximum deliverability and inboxing.
                  </p>
                </div>
                
                {/* Pill Tags inside colored section */}
                <div className="flex flex-wrap gap-2 pt-2">
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#FFD6E8] text-[#E02475]">
                    Amazon SES
                  </span>
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#FFD6E8] text-[#E02475]">
                    SendGrid
                  </span>
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#FFD6E8] text-[#E02475]">
                    › 99.9% Uptime
                  </span>
                </div>
              </div>

              {/* Bottom White Footer Row */}
              <div className="px-3 py-2 flex items-center justify-between">
                <span className="text-xs font-extrabold text-[#111111] tracking-wide">
                  Explore engine
                </span>
                <div className="w-8 h-8 rounded-xl bg-[#F9F9F9] border border-[#111111]/20 flex items-center justify-center text-[#111111] group-hover:bg-[#111111] group-hover:text-white transition-all duration-300 shadow-xs">
                  <ArrowRight className="w-4 h-4" />
                </div>
              </div>

            </div>
          </div>

          {/* Card 2: Dynamic Audience (Soft Purple Theme) */}
          <div className="relative group flex">
            <div className="absolute -inset-2 bg-gradient-to-br from-[#C084FC]/45 via-[#FFB88C]/35 to-transparent blur-2xl rounded-[30px] -z-10 opacity-80 group-hover:opacity-100 group-hover:scale-105 transition-all duration-300" />
            
            <div className="bg-white border border-[#111111] rounded-[26px] p-3.5 flex flex-col justify-between w-full transition-all duration-300 group-hover:-translate-y-1.5 group-hover:scale-[1.02] shadow-[0_15px_30px_rgba(0,0,0,0.04)]">
              
              {/* Inner Colored Pastel Block */}
              <div className="bg-[#F5EEFF] border border-[#C084FC]/20 p-6 rounded-[20px] flex-1 flex flex-col justify-between mb-3">
                <div>
                  <div className="flex items-center justify-between mb-4">
                    <h3 className="text-2xl font-black text-[#111111] tracking-tight">Dynamic Audience</h3>
                    <div className="w-9 h-9 rounded-xl bg-white text-[#C084FC] flex items-center justify-center border border-[#C084FC]/30 shadow-xs">
                      <Users className="w-4.5 h-4.5" />
                    </div>
                  </div>
                  <p className="text-xs text-[#5F6368] leading-relaxed font-medium mb-6">
                    Segment and target the exact right audience with granular real-time filters and behavioral triggers.
                  </p>
                </div>
                
                {/* Pill Tags inside colored section */}
                <div className="flex flex-wrap gap-2 pt-2">
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#E9D5FF] text-[#7C3AED]">
                    Real-time Filters
                  </span>
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#E9D5FF] text-[#7C3AED]">
                    Behavioral Triggers
                  </span>
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#E9D5FF] text-[#7C3AED]">
                    Smart Segmentation
                  </span>
                </div>
              </div>

              {/* Bottom White Footer Row */}
              <div className="px-3 py-2 flex items-center justify-between">
                <span className="text-xs font-extrabold text-[#111111] tracking-wide">
                  Explore audience
                </span>
                <div className="w-8 h-8 rounded-xl bg-[#F9F9F9] border border-[#111111]/20 flex items-center justify-center text-[#111111] group-hover:bg-[#111111] group-hover:text-white transition-all duration-300 shadow-xs">
                  <ArrowRight className="w-4 h-4" />
                </div>
              </div>

            </div>
          </div>

          {/* Card 3: AI Content Studio (Soft Mint Theme) */}
          <div className="relative group flex">
            <div className="absolute -inset-2 bg-gradient-to-br from-[#A7F3D0]/50 via-[#BDEAFE]/40 to-transparent blur-2xl rounded-[30px] -z-10 opacity-80 group-hover:opacity-100 group-hover:scale-105 transition-all duration-300" />
            
            <div className="bg-white border border-[#111111] rounded-[26px] p-3.5 flex flex-col justify-between w-full transition-all duration-300 group-hover:-translate-y-1.5 group-hover:scale-[1.02] shadow-[0_15px_30px_rgba(0,0,0,0.04)]">
              
              {/* Inner Colored Pastel Block */}
              <div className="bg-[#ECFDF5] border border-[#10B981]/20 p-6 rounded-[20px] flex-1 flex flex-col justify-between mb-3">
                <div>
                  <div className="flex items-center justify-between mb-4">
                    <h3 className="text-2xl font-black text-[#111111] tracking-tight">AI Content Studio</h3>
                    <div className="w-9 h-9 rounded-xl bg-white text-[#10B981] flex items-center justify-center border border-[#10B981]/30 shadow-xs">
                      <Sparkles className="w-4.5 h-4.5" />
                    </div>
                  </div>
                  <p className="text-xs text-[#5F6368] leading-relaxed font-medium mb-6">
                    Generate hyper-personalized, high-converting cold email copies and subject lines using Gemini AI.
                  </p>
                </div>
                
                {/* Pill Tags inside colored section */}
                <div className="flex flex-wrap gap-2 pt-2">
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#D1FAE5] text-[#059669]">
                    Gemini AI
                  </span>
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#D1FAE5] text-[#059669]">
                    Subject Lines
                  </span>
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#D1FAE5] text-[#059669]">
                    AI Assistant
                  </span>
                </div>
              </div>

              {/* Bottom White Footer Row */}
              <div className="px-3 py-2 flex items-center justify-between">
                <span className="text-xs font-extrabold text-[#111111] tracking-wide">
                  Explore AI studio
                </span>
                <div className="w-8 h-8 rounded-xl bg-[#F9F9F9] border border-[#111111]/20 flex items-center justify-center text-[#111111] group-hover:bg-[#111111] group-hover:text-white transition-all duration-300 shadow-xs">
                  <ArrowRight className="w-4 h-4" />
                </div>
              </div>

            </div>
          </div>

          {/* Card 4: Cron Scheduler (Soft Peach Theme) */}
          <div className="relative group flex">
            <div className="absolute -inset-2 bg-gradient-to-br from-[#FFB88C]/50 via-[#FFEAA7]/40 to-transparent blur-2xl rounded-[30px] -z-10 opacity-80 group-hover:opacity-100 group-hover:scale-105 transition-all duration-300" />
            
            <div className="bg-white border border-[#111111] rounded-[26px] p-3.5 flex flex-col justify-between w-full transition-all duration-300 group-hover:-translate-y-1.5 group-hover:scale-[1.02] shadow-[0_15px_30px_rgba(0,0,0,0.04)]">
              
              {/* Inner Colored Pastel Block */}
              <div className="bg-[#FFF7ED] border border-[#F97316]/20 p-6 rounded-[20px] flex-1 flex flex-col justify-between mb-3">
                <div>
                  <div className="flex items-center justify-between mb-4">
                    <h3 className="text-2xl font-black text-[#111111] tracking-tight">Cron Scheduler</h3>
                    <div className="w-9 h-9 rounded-xl bg-white text-[#F97316] flex items-center justify-center border border-[#F97316]/30 shadow-xs">
                      <Clock className="w-4.5 h-4.5" />
                    </div>
                  </div>
                  <p className="text-xs text-[#5F6368] leading-relaxed font-medium mb-6">
                    Schedule campaigns at optimal local times with full timezone matching and daily throttling limits.
                  </p>
                </div>
                
                {/* Pill Tags inside colored section */}
                <div className="flex flex-wrap gap-2 pt-2">
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#FFEDD5] text-[#EA580C]">
                    Timezone Matching
                  </span>
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#FFEDD5] text-[#EA580C]">
                    Daily Throttling
                  </span>
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#FFEDD5] text-[#EA580C]">
                    Time Optimization
                  </span>
                </div>
              </div>

              {/* Bottom White Footer Row */}
              <div className="px-3 py-2 flex items-center justify-between">
                <span className="text-xs font-extrabold text-[#111111] tracking-wide">
                  Explore scheduler
                </span>
                <div className="w-8 h-8 rounded-xl bg-[#F9F9F9] border border-[#111111]/20 flex items-center justify-center text-[#111111] group-hover:bg-[#111111] group-hover:text-white transition-all duration-300 shadow-xs">
                  <ArrowRight className="w-4 h-4" />
                </div>
              </div>

            </div>
          </div>

          {/* Card 5: Role-Based Access (Soft Rose Theme) */}
          <div className="relative group flex">
            <div className="absolute -inset-2 bg-gradient-to-br from-[#FFC98B]/50 via-[#FF7EB6]/35 to-transparent blur-2xl rounded-[30px] -z-10 opacity-80 group-hover:opacity-100 group-hover:scale-105 transition-all duration-300" />
            
            <div className="bg-white border border-[#111111] rounded-[26px] p-3.5 flex flex-col justify-between w-full transition-all duration-300 group-hover:-translate-y-1.5 group-hover:scale-[1.02] shadow-[0_15px_30px_rgba(0,0,0,0.04)]">
              
              {/* Inner Colored Pastel Block */}
              <div className="bg-[#FFF1F2] border border-[#EC4899]/20 p-6 rounded-[20px] flex-1 flex flex-col justify-between mb-3">
                <div>
                  <div className="flex items-center justify-between mb-4">
                    <h3 className="text-2xl font-black text-[#111111] tracking-tight">Role-Based Access</h3>
                    <div className="w-9 h-9 rounded-xl bg-white text-[#EC4899] flex items-center justify-center border border-[#EC4899]/30 shadow-xs">
                      <Shield className="w-4.5 h-4.5" />
                    </div>
                  </div>
                  <p className="text-xs text-[#5F6368] leading-relaxed font-medium mb-6">
                    Manage your agency team with granular permissions, API keys, and audit logging for security.
                  </p>
                </div>
                
                {/* Pill Tags inside colored section */}
                <div className="flex flex-wrap gap-2 pt-2">
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#FFE4E6] text-[#E11D48]">
                    Agency Permissions
                  </span>
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#FFE4E6] text-[#E11D48]">
                    API Keys
                  </span>
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#FFE4E6] text-[#E11D48]">
                    Secure & Scalable
                  </span>
                </div>
              </div>

              {/* Bottom White Footer Row */}
              <div className="px-3 py-2 flex items-center justify-between">
                <span className="text-xs font-extrabold text-[#111111] tracking-wide">
                  Explore security
                </span>
                <div className="w-8 h-8 rounded-xl bg-[#F9F9F9] border border-[#111111]/20 flex items-center justify-center text-[#111111] group-hover:bg-[#111111] group-hover:text-white transition-all duration-300 shadow-xs">
                  <ArrowRight className="w-4 h-4" />
                </div>
              </div>

            </div>
          </div>

          {/* Card 6: Live Telemetry (Soft Cyan Theme) */}
          <div className="relative group flex">
            <div className="absolute -inset-2 bg-gradient-to-br from-[#BDEAFE]/50 via-[#C084FC]/40 to-transparent blur-2xl rounded-[30px] -z-10 opacity-80 group-hover:opacity-100 group-hover:scale-105 transition-all duration-300" />
            
            <div className="bg-white border border-[#111111] rounded-[26px] p-3.5 flex flex-col justify-between w-full transition-all duration-300 group-hover:-translate-y-1.5 group-hover:scale-[1.02] shadow-[0_15px_30px_rgba(0,0,0,0.04)]">
              
              {/* Inner Colored Pastel Block */}
              <div className="bg-[#F0F9FF] border border-[#0284C7]/20 p-6 rounded-[20px] flex-1 flex flex-col justify-between mb-3">
                <div>
                  <div className="flex items-center justify-between mb-4">
                    <h3 className="text-2xl font-black text-[#111111] tracking-tight">Live Telemetry</h3>
                    <div className="w-9 h-9 rounded-xl bg-white text-[#0284C7] flex items-center justify-center border border-[#0284C7]/30 shadow-xs">
                      <BarChart3 className="w-4.5 h-4.5" />
                    </div>
                  </div>
                  <p className="text-xs text-[#5F6368] leading-relaxed font-medium mb-6">
                    Track open events, click-throughs, unsubscribes, and bounces in real-time with instant webhooks.
                  </p>
                </div>
                
                {/* Pill Tags inside colored section */}
                <div className="flex flex-wrap gap-2 pt-2">
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#E0F2FE] text-[#0284C7]">
                    Open Events
                  </span>
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#E0F2FE] text-[#0284C7]">
                    Click Tracking
                  </span>
                  <span className="px-3 py-1 rounded-full text-[11px] font-bold bg-[#E0F2FE] text-[#0284C7]">
                    Real-time Insights
                  </span>
                </div>
              </div>

              {/* Bottom White Footer Row */}
              <div className="px-3 py-2 flex items-center justify-between">
                <span className="text-xs font-extrabold text-[#111111] tracking-wide">
                  Explore telemetry
                </span>
                <div className="w-8 h-8 rounded-xl bg-[#F9F9F9] border border-[#111111]/20 flex items-center justify-center text-[#111111] group-hover:bg-[#111111] group-hover:text-white transition-all duration-300 shadow-xs">
                  <ArrowRight className="w-4 h-4" />
                </div>
              </div>

            </div>
          </div>

        </div>
      </section>

      {/* ─── WORKFLOW SECTION ─── */}
      <section id="workflow" className="py-28 md:py-36 px-6 lg:px-12 max-w-[1280px] mx-auto">
        
        {/* Section Header */}
        <div className="text-center max-w-2xl mx-auto space-y-4 mb-20">
          <div className="relative inline-block">
            <div className="absolute -inset-1 bg-gradient-to-r from-[#FFB88C]/30 to-[#FF7EB6]/30 blur-md rounded-full -z-10" />
            <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full border border-[#111111] bg-white shadow-xs text-xs font-bold text-[#111111]">
              <span className="w-2 h-2 rounded-full bg-[#FFB88C]" />
              <span>HOW IT WORKS</span>
            </div>
          </div>
          <h2 className="text-3xl sm:text-4xl lg:text-5xl font-extrabold tracking-tight text-[#111111]">
            Launch powerful email campaigns in just a few simple steps.
          </h2>
          <p className="text-base text-[#5F6368] font-medium">
            Automate cold outreach from lead import to conversion without technical hassle.
          </p>
        </div>

        {/* 5 Horizontal Workflow Step Cards Connected via Modern Gray SVG Arrows with Neon Borders */}
        <div className="flex flex-col lg:flex-row items-stretch justify-between gap-4 lg:gap-2">
          
          {/* Step 1 (Pink Neon Border) */}
          <div className="relative group flex-1 flex flex-col">
            <div className="absolute -inset-2 bg-gradient-to-b from-[#FF7EB6]/35 to-[#C084FC]/20 blur-2xl rounded-[28px] -z-10 opacity-70 group-hover:opacity-100 group-hover:scale-110 transition-all duration-300" />
            <div className="bg-white border-2 border-[#FF7EB6] shadow-[0_0_20px_rgba(255,126,182,0.25)] rounded-[22px] p-6 transition-all duration-300 group-hover:-translate-y-2 group-hover:scale-[1.02] group-hover:shadow-[0_0_30px_rgba(255,126,182,0.5)] flex flex-col justify-between h-full">
              <div>
                <div className="flex items-center justify-between mb-5">
                  <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-[10px] font-extrabold bg-[#111111] text-white tracking-wider uppercase shadow-xs">
                    <span className="w-1.5 h-1.5 rounded-full bg-[#FF7EB6] animate-pulse" />
                    STEP 01
                  </span>
                  <div className="w-9 h-9 rounded-xl bg-[#FF7EB6]/10 text-[#FF7EB6] flex items-center justify-center border border-[#FF7EB6]/40 shadow-xs">
                    <Users className="w-4 h-4" />
                  </div>
                </div>
                <h4 className="text-base font-extrabold text-[#111111] mb-2">Import Contacts</h4>
                <p className="text-xs text-[#5F6368] leading-relaxed font-medium mb-6">
                  Upload CSV/Excel lead lists or connect seamlessly with your CRM.
                </p>
              </div>
              <span className="inline-flex items-center gap-1 px-3 py-1 rounded-lg text-[10px] font-bold bg-[#FF7EB6]/10 text-[#FF7EB6] border border-[#FF7EB6]/30 self-start">
                <CheckCircle2 className="w-3 h-3 text-[#FF7EB6]" />
                CSV & CRM Sync
              </span>
            </div>
          </div>

          {/* Modern Gray Arrow 1 -> 2 */}
          <div className="hidden lg:flex items-center justify-center self-center px-1 text-[#8A8A8A]">
            <svg className="w-6 h-6 animate-pulse" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M13 7l5 5m0 0l-5 5m5-5H6" />
            </svg>
          </div>

          {/* Step 2 (Purple Neon Border) */}
          <div className="relative group flex-1 flex flex-col">
            <div className="absolute -inset-2 bg-gradient-to-b from-[#C084FC]/35 to-[#FFB88C]/20 blur-2xl rounded-[28px] -z-10 opacity-70 group-hover:opacity-100 group-hover:scale-110 transition-all duration-300" />
            <div className="bg-white border-2 border-[#C084FC] shadow-[0_0_20px_rgba(192,132,252,0.25)] rounded-[22px] p-6 transition-all duration-300 group-hover:-translate-y-2 group-hover:scale-[1.02] group-hover:shadow-[0_0_30px_rgba(192,132,252,0.5)] flex flex-col justify-between h-full">
              <div>
                <div className="flex items-center justify-between mb-5">
                  <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-[10px] font-extrabold bg-[#111111] text-white tracking-wider uppercase shadow-xs">
                    <span className="w-1.5 h-1.5 rounded-full bg-[#C084FC] animate-pulse" />
                    STEP 02
                  </span>
                  <div className="w-9 h-9 rounded-xl bg-[#C084FC]/10 text-[#C084FC] flex items-center justify-center border border-[#C084FC]/40 shadow-xs">
                    <Mail className="w-4 h-4" />
                  </div>
                </div>
                <h4 className="text-base font-extrabold text-[#111111] mb-2">Create Campaign</h4>
                <p className="text-xs text-[#5F6368] leading-relaxed font-medium mb-6">
                  Design high-converting cold email sequences with our drag-and-drop editor.
                </p>
              </div>
              <span className="inline-flex items-center gap-1 px-3 py-1 rounded-lg text-[10px] font-bold bg-[#C084FC]/10 text-[#C084FC] border border-[#C084FC]/30 self-start">
                <Sparkles className="w-3 h-3 text-[#C084FC]" />
                AI Content Studio
              </span>
            </div>
          </div>

          {/* Modern Gray Arrow 2 -> 3 */}
          <div className="hidden lg:flex items-center justify-center self-center px-1 text-[#8A8A8A]">
            <svg className="w-6 h-6 animate-pulse" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M13 7l5 5m0 0l-5 5m5-5H6" />
            </svg>
          </div>

          {/* Step 3 (Orange Neon Border) */}
          <div className="relative group flex-1 flex flex-col">
            <div className="absolute -inset-2 bg-gradient-to-b from-[#FFB88C]/35 to-[#FFEAA7]/20 blur-2xl rounded-[28px] -z-10 opacity-70 group-hover:opacity-100 group-hover:scale-110 transition-all duration-300" />
            <div className="bg-white border-2 border-[#F97316] shadow-[0_0_20px_rgba(249,115,22,0.25)] rounded-[22px] p-6 transition-all duration-300 group-hover:-translate-y-2 group-hover:scale-[1.02] group-hover:shadow-[0_0_30px_rgba(249,115,22,0.5)] flex flex-col justify-between h-full">
              <div>
                <div className="flex items-center justify-between mb-5">
                  <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-[10px] font-extrabold bg-[#111111] text-white tracking-wider uppercase shadow-xs">
                    <span className="w-1.5 h-1.5 rounded-full bg-[#F97316] animate-pulse" />
                    STEP 03
                  </span>
                  <div className="w-9 h-9 rounded-xl bg-[#F97316]/10 text-[#F97316] flex items-center justify-center border border-[#F97316]/40 shadow-xs">
                    <Sliders className="w-4 h-4" />
                  </div>
                </div>
                <h4 className="text-base font-extrabold text-[#111111] mb-2">Rules & Schedule</h4>
                <p className="text-xs text-[#5F6368] leading-relaxed font-medium mb-6">
                  Define target filters, daily throttling limits, and cron timers.
                </p>
              </div>
              <span className="inline-flex items-center gap-1 px-3 py-1 rounded-lg text-[10px] font-bold bg-[#F97316]/10 text-[#F97316] border border-[#F97316]/30 self-start">
                <Clock className="w-3 h-3 text-[#F97316]" />
                Smart Cron Engine
              </span>
            </div>
          </div>

          {/* Modern Gray Arrow 3 -> 4 */}
          <div className="hidden lg:flex items-center justify-center self-center px-1 text-[#8A8A8A]">
            <svg className="w-6 h-6 animate-pulse" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M13 7l5 5m0 0l-5 5m5-5H6" />
            </svg>
          </div>

          {/* Step 4 (Mint Green Neon Border) */}
          <div className="relative group flex-1 flex flex-col">
            <div className="absolute -inset-2 bg-gradient-to-b from-[#A7F3D0]/40 to-[#BDEAFE]/20 blur-2xl rounded-[28px] -z-10 opacity-70 group-hover:opacity-100 group-hover:scale-110 transition-all duration-300" />
            <div className="bg-white border-2 border-[#10B981] shadow-[0_0_20px_rgba(16,185,129,0.25)] rounded-[22px] p-6 transition-all duration-300 group-hover:-translate-y-2 group-hover:scale-[1.02] group-hover:shadow-[0_0_30px_rgba(16,185,129,0.5)] flex flex-col justify-between h-full">
              <div>
                <div className="flex items-center justify-between mb-5">
                  <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-[10px] font-extrabold bg-[#111111] text-white tracking-wider uppercase shadow-xs">
                    <span className="w-1.5 h-1.5 rounded-full bg-[#10B981] animate-pulse" />
                    STEP 04
                  </span>
                  <div className="w-9 h-9 rounded-xl bg-[#10B981]/10 text-[#10B981] flex items-center justify-center border border-[#10B981]/40 shadow-xs">
                    <Zap className="w-4 h-4" />
                  </div>
                </div>
                <h4 className="text-base font-extrabold text-[#111111] mb-2">We Deliver</h4>
                <p className="text-xs text-[#5F6368] leading-relaxed font-medium mb-6">
                  Automatically dispatch via Amazon SES, SendGrid, and SMTP relays.
                </p>
              </div>
              <span className="inline-flex items-center gap-1 px-3 py-1 rounded-lg text-[10px] font-bold bg-[#10B981]/10 text-[#10B981] border border-[#10B981]/30 self-start">
                <Globe className="w-3 h-3 text-[#10B981]" />
                Multi-Engine Route
              </span>
            </div>
          </div>

          {/* Modern Gray Arrow 4 -> 5 */}
          <div className="hidden lg:flex items-center justify-center self-center px-1 text-[#8A8A8A]">
            <svg className="w-6 h-6 animate-pulse" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M13 7l5 5m0 0l-5 5m5-5H6" />
            </svg>
          </div>

          {/* Step 5 (Cyan Blue Neon Border) */}
          <div className="relative group flex-1 flex flex-col">
            <div className="absolute -inset-2 bg-gradient-to-b from-[#BDEAFE]/40 to-[#C084FC]/20 blur-2xl rounded-[28px] -z-10 opacity-70 group-hover:opacity-100 group-hover:scale-110 transition-all duration-300" />
            <div className="bg-white border-2 border-[#00DDFF] shadow-[0_0_20px_rgba(0,221,255,0.25)] rounded-[22px] p-6 transition-all duration-300 group-hover:-translate-y-2 group-hover:scale-[1.02] group-hover:shadow-[0_0_30px_rgba(0,221,255,0.5)] flex flex-col justify-between h-full">
              <div>
                <div className="flex items-center justify-between mb-5">
                  <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-[10px] font-extrabold bg-[#111111] text-white tracking-wider uppercase shadow-xs">
                    <span className="w-1.5 h-1.5 rounded-full bg-[#0284C7] animate-pulse" />
                    STEP 05
                  </span>
                  <div className="w-9 h-9 rounded-xl bg-[#0284C7]/10 text-[#0284C7] flex items-center justify-center border border-[#0284C7]/40 shadow-xs">
                    <BarChart3 className="w-4 h-4" />
                  </div>
                </div>
                <h4 className="text-base font-extrabold text-[#111111] mb-2">Track & Optimize</h4>
                <p className="text-xs text-[#5F6368] leading-relaxed font-medium mb-6">
                  Analyze real-time opens, clicks, and conversion telemetry.
                </p>
              </div>
              <span className="inline-flex items-center gap-1 px-3 py-1 rounded-lg text-[10px] font-bold bg-[#0284C7]/10 text-[#0284C7] border border-[#0284C7]/30 self-start">
                <TrendingUp className="w-3 h-3 text-[#0284C7]" />
                Real-Time Telemetry
              </span>
            </div>
          </div>

        </div>
      </section>

      {/* ─── TESTIMONIALS SECTION (HORIZONTAL SCROLL CAROUSEL WITH 3D SPEECH-BUBBLE REVIEW CARDS) ─── */}
      <section id="testimonials" className="py-28 md:py-36 px-6 lg:px-12 max-w-[1280px] mx-auto">
        
        {/* Section Header + Scroll Control Buttons */}
        <div className="flex flex-col md:flex-row md:items-end justify-between gap-6 mb-12">
          <div className="space-y-4 max-w-2xl">
            <div className="relative inline-block">
              <div className="absolute -inset-1 bg-gradient-to-r from-[#FF7EB6]/30 to-[#C084FC]/30 blur-md rounded-full -z-10" />
              <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full border border-[#111111] bg-white shadow-xs text-xs font-bold text-[#111111]">
                <span className="w-2 h-2 rounded-full bg-[#FF7EB6]" />
                <span>WHAT OUR CLIENTS SAY</span>
              </div>
            </div>
            <h2 className="text-3xl sm:text-4xl lg:text-5xl font-extrabold tracking-tight text-[#111111]">
              Loved by 2,157+ businesses worldwide
            </h2>
            <p className="text-base text-[#5F6368] font-medium">
              See how MailAlly powers cold outreach for top sales leaders and SaaS startups.
            </p>
          </div>

          {/* Left & Right Scroll Navigation Arrows */}
          <div className="flex items-center gap-3">
            <button
              onClick={scrollTestimonialsLeft}
              className="w-11 h-11 rounded-2xl bg-white border border-[#111111] flex items-center justify-center text-[#111111] hover:bg-[#111111] hover:text-white transition-all duration-300 shadow-xs cursor-pointer active:scale-95"
              aria-label="Scroll Left"
            >
              <ArrowRight className="w-5 h-5 rotate-180" />
            </button>
            <button
              onClick={scrollTestimonialsRight}
              className="w-11 h-11 rounded-2xl bg-white border border-[#111111] flex items-center justify-center text-[#111111] hover:bg-[#111111] hover:text-white transition-all duration-300 shadow-xs cursor-pointer active:scale-95"
              aria-label="Scroll Right"
            >
              <ArrowRight className="w-5 h-5" />
            </button>
          </div>
        </div>

        {/* Horizontal Scrollable Testimonial Cards Track */}
        <div
          ref={testimonialsScrollRef}
          className="flex gap-6 overflow-x-auto snap-x snap-mandatory scrollbar-none py-6 px-1"
          style={{ scrollbarWidth: 'none', msOverflowStyle: 'none' }}
        >
          {[
            {
              name: "Leslie Alexander",
              role: "Marketing Manager, TechCorp",
              avatar: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=120&auto=format&fit=crop&q=80",
              bg: "bg-[#FFF0F5]",
              border: "border-[#FF7EB6]/30",
              glow: "from-[#FF7EB6]/45 via-[#C084FC]/35 to-transparent",
              reaction: "❤️",
              quote: "MailAlly has completely transformed our cold outreach. Our open rates increased by 2x in just two weeks, and inbox deliverability is flawless."
            },
            {
              name: "Jacob Jones",
              role: "Founder, GrowthLab",
              avatar: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=120&auto=format&fit=crop&q=80",
              bg: "bg-[#F5EEFF]",
              border: "border-[#C084FC]/30",
              glow: "from-[#C084FC]/45 via-[#FFB88C]/35 to-transparent",
              reaction: "⭐",
              quote: "The deliverability is unmatched. We can finally reach our enterprise audience without worrying about spam filters or domain reputation."
            },
            {
              name: "Jenny Wilson",
              role: "Head of Sales, SalesPro",
              avatar: "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=120&auto=format&fit=crop&q=80",
              bg: "bg-[#ECFDF5]",
              border: "border-[#10B981]/30",
              glow: "from-[#A7F3D0]/50 via-[#BDEAFE]/40 to-transparent",
              reaction: "🔥",
              quote: "Super easy to use and the AI content studio saves us hours every week. It feels like having a senior copywriter on demand."
            },
            {
              name: "Marcus Chen",
              role: "VP of Outbound, VelocitySaaS",
              avatar: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=120&auto=format&fit=crop&q=80",
              bg: "bg-[#FFF7ED]",
              border: "border-[#F97316]/30",
              glow: "from-[#FFB88C]/50 via-[#FFEAA7]/40 to-transparent",
              reaction: "🚀",
              quote: "The multi-engine SMTP routing alone is worth 10x the price. Our reply rates jumped from 3.2% to 11.8% within 30 days."
            },
            {
              name: "Sarah Jenkins",
              role: "Agency Owner, LeadForge",
              avatar: "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=120&auto=format&fit=crop&q=80",
              bg: "bg-[#F0F9FF]",
              border: "border-[#0284C7]/30",
              glow: "from-[#BDEAFE]/50 via-[#C084FC]/40 to-transparent",
              reaction: "⚡",
              quote: "Managing 15+ client accounts with MailAlly's role-based access and cron schedulers is effortless. Best outreach stack we've used."
            },
            {
              name: "David Milkon",
              role: "Growth Director, ApexMedia",
              avatar: "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=120&auto=format&fit=crop&q=80",
              bg: "bg-[#FFF1F2]",
              border: "border-[#EC4899]/30",
              glow: "from-[#FFC98B]/50 via-[#FF7EB6]/35 to-transparent",
              reaction: "❤️",
              quote: "Wonderful experience for me, I am really thrilled with the great outcome of the project. Inbox deliverability is unmatched! Welldone!"
            }
          ].map((item, index) => (
            <div
              key={index}
              className="relative min-w-[340px] sm:min-w-[400px] max-w-[420px] snap-start flex-none group flex flex-col"
            >
              {/* Vibrant Pastel Gradient Shadow Glow */}
              <div className={`absolute -inset-2 bg-gradient-to-br ${item.glow} blur-2xl rounded-[32px] -z-10 opacity-75 group-hover:opacity-100 group-hover:scale-105 transition-all duration-300`} />
              
              {/* Outer White Border Card Frame */}
              <div className="bg-white border border-[#111111] rounded-[26px] p-3.5 flex flex-col justify-between h-full transition-all duration-300 group-hover:-translate-y-1.5 group-hover:scale-[1.02] shadow-[0_15px_30px_rgba(0,0,0,0.04)] relative">
                
                {/* Floating 3D Reaction Heart Badge (Top Right) */}
                <div className="absolute -top-3.5 -right-3.5 w-10 h-10 rounded-full bg-white border border-[#111111] shadow-md flex items-center justify-center text-base z-20 group-hover:scale-110 transition-transform duration-300">
                  {item.reaction}
                </div>

                {/* Speech Bubble Inner Colored Pastel Block */}
                <div className={`${item.bg} border ${item.border} p-6 rounded-[20px] flex-1 flex flex-col justify-between relative`}>
                  
                  {/* Top Row: Avatar & Reviewer Info + 5 Stars */}
                  <div>
                    <div className="flex items-center gap-4 mb-5">
                      {/* Large Avatar Photo */}
                      <img
                        src={item.avatar}
                        alt={item.name}
                        className="w-14 h-14 rounded-full border-2 border-white object-cover shadow-sm flex-none"
                      />
                      <div>
                        {/* 5 Rating Stars */}
                        <div className="flex gap-1 text-[#F59E0B] mb-1">
                          <Star className="w-3.5 h-3.5 fill-current" />
                          <Star className="w-3.5 h-3.5 fill-current" />
                          <Star className="w-3.5 h-3.5 fill-current" />
                          <Star className="w-3.5 h-3.5 fill-current" />
                          <Star className="w-3.5 h-3.5 fill-current" />
                        </div>
                        <h4 className="text-base font-extrabold text-[#111111]">{item.name}</h4>
                        <p className="text-[11px] text-[#5F6368] font-semibold">{item.role}</p>
                      </div>
                    </div>

                    {/* Review Quote Text */}
                    <p className="text-xs sm:text-sm text-[#111111] leading-relaxed font-medium">
                      "{item.quote}"
                    </p>
                  </div>

                </div>

                {/* Bottom Verified Review Tag */}
                <div className="pt-3 px-2 flex items-center justify-between">
                  <span className="text-[11px] font-extrabold text-[#111111] flex items-center gap-1.5">
                    <CheckCircle2 className="w-3.5 h-3.5 text-emerald-500" />
                    Verified Customer Review
                  </span>
                  <span className="text-[10px] text-[#8A8A8A] font-bold">MailAlly User</span>
                </div>

              </div>
            </div>
          ))}
        </div>

        {/* Link below Testimonials Carousel */}
        <div className="text-center mt-10">
          <a href="#testimonials" className="inline-flex items-center gap-1.5 text-xs font-extrabold text-[#111111] hover:underline">
            <span>See all 2,157 reviews</span>
            <ArrowRight className="w-3.5 h-3.5" />
          </a>
        </div>

      </section>

      {/* ─── PRICING SECTION ─── */}
      <section id="pricing" className="py-28 md:py-36 px-6 lg:px-12 max-w-[1280px] mx-auto">
        
        {/* Section Header */}
        <div className="text-center max-w-2xl mx-auto space-y-4 mb-16">
          <div className="relative inline-block">
            <div className="absolute -inset-1 bg-gradient-to-r from-[#C084FC]/30 to-[#FFB88C]/30 blur-md rounded-full -z-10" />
            <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full border border-[#111111] bg-white shadow-xs text-xs font-bold text-[#111111]">
              <span className="w-2 h-2 rounded-full bg-[#C084FC]" />
              <span>SIMPLE PRICING</span>
            </div>
          </div>
          <h2 className="text-3xl sm:text-4xl lg:text-5xl font-extrabold tracking-tight text-[#111111]">
            Choose the plan that grows with you
          </h2>
          <p className="text-base text-[#5F6368] font-medium">
            Transparent pricing with zero hidden fees. Scale as your outreach grows.
          </p>

          {/* Billing Toggle Switch */}
          <div className="pt-6 flex items-center justify-center gap-3">
            <span className={`text-xs font-extrabold ${!isYearly ? 'text-[#111111]' : 'text-[#8A8A8A]'}`}>
              Monthly
            </span>
            <button
              onClick={() => setIsYearly(!isYearly)}
              className="w-12 h-6 rounded-full bg-[#111111] p-1 transition-colors duration-300 focus:outline-none flex items-center cursor-pointer"
            >
              <div className={`w-4 h-4 rounded-full bg-white transition-transform duration-300 ${isYearly ? 'translate-x-6' : 'translate-x-0'}`} />
            </button>
            <span className={`text-xs font-extrabold ${isYearly ? 'text-[#111111]' : 'text-[#8A8A8A]'} flex items-center gap-1.5`}>
              Yearly
              <span className="px-2 py-0.5 rounded-full text-[10px] font-extrabold bg-[#C084FC]/15 text-[#C084FC]">
                Save 20%
              </span>
            </span>
          </div>
        </div>

        {/* 3 Pricing Cards Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 items-stretch max-w-6xl mx-auto">
          
          {/* Starter Plan */}
          <div className="relative group flex">
            <div className="absolute -inset-2.5 bg-gradient-to-b from-[#FFB88C]/45 via-[#FFEAA7]/35 to-transparent blur-2xl rounded-[30px] -z-10 opacity-80 group-hover:opacity-100 group-hover:scale-110 transition-all duration-300" />
            <div className="bg-white border border-[#111111] rounded-[20px] p-8 flex flex-col justify-between w-full shadow-xs transition-all duration-300 ease-out group-hover:-translate-y-2 group-hover:scale-[1.03] group-hover:shadow-[0_20px_40px_rgba(255,184,140,0.25)]">
              <div>
                <h3 className="text-xl font-extrabold text-[#111111]">Starter</h3>
                <p className="text-xs text-[#5F6368] font-medium mt-1 mb-6">Perfect for getting started</p>
                <div className="mb-8">
                  <span className="text-4xl font-black text-[#111111]">
                    ${isYearly ? '18' : '23'}
                  </span>
                  <span className="text-xs text-[#8A8A8A] font-bold ml-1">/month</span>
                </div>
                <ul className="space-y-3.5 text-xs text-[#5F6368] font-medium">
                  <li className="flex items-center gap-2.5">
                    <Check className="w-4 h-4 text-[#10B981]" />
                    <span>5,000 Emails / Month</span>
                  </li>
                  <li className="flex items-center gap-2.5">
                    <Check className="w-4 h-4 text-[#10B981]" />
                    <span>Basic Email Analytics</span>
                  </li>
                  <li className="flex items-center gap-2.5">
                    <Check className="w-4 h-4 text-[#10B981]" />
                    <span>1 User Seat</span>
                  </li>
                  <li className="flex items-center gap-2.5">
                    <Check className="w-4 h-4 text-[#10B981]" />
                    <span>Standard Email Support</span>
                  </li>
                </ul>
              </div>
              <button
                onClick={() => navigate('/register')}
                className="mt-10 w-full bg-white hover:bg-[#111111] hover:text-white text-[#111111] border border-[#111111] font-bold text-xs py-3 rounded-xl transition-all duration-300 cursor-pointer shadow-xs"
              >
                Start for Free
              </button>
            </div>
          </div>

          {/* Professional Plan (Highlighted Middle Card) */}
          <div className="relative group flex lg:-translate-y-2">
            <div className="absolute -inset-3 bg-gradient-to-r from-[#FF7EB6]/50 via-[#C084FC]/50 to-[#FFB88C]/50 blur-2xl rounded-[32px] -z-10 opacity-90 group-hover:opacity-100 group-hover:scale-115 transition-all duration-300" />
            <div className="bg-white border border-[#111111] rounded-[20px] p-8 flex flex-col justify-between w-full shadow-lg relative transition-all duration-300 ease-out group-hover:-translate-y-3 group-hover:scale-[1.04] group-hover:shadow-[0_25px_50px_rgba(192,132,252,0.35)]">
              
              {/* Most Popular Badge */}
              <div className="absolute -top-3.5 right-6 bg-[#111111] text-white text-[10px] font-extrabold uppercase tracking-widest px-3.5 py-1 rounded-full shadow-sm">
                MOST POPULAR
              </div>

              <div>
                <h3 className="text-xl font-extrabold text-[#111111]">Professional</h3>
                <p className="text-xs text-[#5F6368] font-medium mt-1 mb-6">Ideal for growing businesses</p>
                <div className="mb-8">
                  <span className="text-4xl font-black text-[#111111]">
                    ${isYearly ? '63' : '79'}
                  </span>
                  <span className="text-xs text-[#8A8A8A] font-bold ml-1">/month</span>
                </div>
                <ul className="space-y-3.5 text-xs text-[#5F6368]">
                  <li className="flex items-center gap-2.5 font-bold text-[#111111]">
                    <Check className="w-4 h-4 text-[#10B981]" />
                    <span>25,000 Emails / Month</span>
                  </li>
                  <li className="flex items-center gap-2.5 font-medium">
                    <Check className="w-4 h-4 text-[#10B981]" />
                    <span>Advanced Telemetry & Analytics</span>
                  </li>
                  <li className="flex items-center gap-2.5 font-medium">
                    <Check className="w-4 h-4 text-[#10B981]" />
                    <span>5 User Seats</span>
                  </li>
                  <li className="flex items-center gap-2.5 font-medium">
                    <Check className="w-4 h-4 text-[#10B981]" />
                    <span>Priority Support</span>
                  </li>
                  <li className="flex items-center gap-2.5 font-medium">
                    <Check className="w-4 h-4 text-[#10B981]" />
                    <span>AI Content Studio</span>
                  </li>
                </ul>
              </div>
              <button
                onClick={() => navigate('/register')}
                className="mt-10 w-full bg-[#111111] hover:bg-[#222222] text-white font-extrabold text-xs py-3 rounded-xl transition-all duration-300 shadow-sm cursor-pointer hover:shadow-md"
              >
                Start for Free
              </button>
            </div>
          </div>

          {/* Enterprise Plan */}
          <div className="relative group flex">
            <div className="absolute -inset-2.5 bg-gradient-to-b from-[#A7F3D0]/45 via-[#BDEAFE]/35 to-transparent blur-2xl rounded-[30px] -z-10 opacity-80 group-hover:opacity-100 group-hover:scale-110 transition-all duration-300" />
            <div className="bg-white border border-[#111111] rounded-[20px] p-8 flex flex-col justify-between w-full shadow-xs transition-all duration-300 ease-out group-hover:-translate-y-2 group-hover:scale-[1.03] group-hover:shadow-[0_20px_40px_rgba(167,243,208,0.25)]">
              <div>
                <h3 className="text-xl font-extrabold text-[#111111]">Enterprise</h3>
                <p className="text-xs text-[#5F6368] font-medium mt-1 mb-6">For large teams & high volume</p>
                <div className="mb-8">
                  <span className="text-4xl font-black text-[#111111]">
                    ${isYearly ? '190' : '239'}
                  </span>
                  <span className="text-xs text-[#8A8A8A] font-bold ml-1">/month</span>
                </div>
                <ul className="space-y-3.5 text-xs text-[#5F6368] font-medium">
                  <li className="flex items-center gap-2.5">
                    <Check className="w-4 h-4 text-[#10B981]" />
                    <span>100,000+ Emails / Month</span>
                  </li>
                  <li className="flex items-center gap-2.5">
                    <Check className="w-4 h-4 text-[#10B981]" />
                    <span>Custom Dedicated SMTP Relays</span>
                  </li>
                  <li className="flex items-center gap-2.5">
                    <Check className="w-4 h-4 text-[#10B981]" />
                    <span>Unlimited User Seats</span>
                  </li>
                  <li className="flex items-center gap-2.5">
                    <Check className="w-4 h-4 text-[#10B981]" />
                    <span>Dedicated Account Manager</span>
                  </li>
                  <li className="flex items-center gap-2.5">
                    <Check className="w-4 h-4 text-[#10B981]" />
                    <span>Custom SLA & Onboarding</span>
                  </li>
                </ul>
              </div>
              <button
                onClick={() => navigate('/register')}
                className="mt-10 w-full bg-white hover:bg-[#111111] hover:text-white text-[#111111] border border-[#111111] font-bold text-xs py-3 rounded-xl transition-all duration-300 cursor-pointer shadow-xs"
              >
                Contact Sales
              </button>
            </div>
          </div>

        </div>

      </section>

      {/* ─── FAQ SECTION ─── */}
      <section id="faq" className="py-24 px-6 lg:px-12 max-w-[900px] mx-auto">
        <div className="text-center space-y-4 mb-16">
          <div className="relative inline-block">
            <div className="absolute -inset-1 bg-gradient-to-r from-[#C084FC]/30 to-[#FF7EB6]/30 blur-md rounded-full -z-10" />
            <div className="inline-flex items-center gap-2 px-3.5 py-1.5 rounded-full border border-[#111111] bg-white shadow-xs text-xs font-bold text-[#111111]">
              <HelpCircle className="w-3.5 h-3.5 text-[#C084FC]" />
              <span>FREQUENTLY ASKED QUESTIONS</span>
            </div>
          </div>
          <h2 className="text-3xl sm:text-4xl font-extrabold text-[#111111]">
            Everything you need to know
          </h2>
        </div>

        <div className="space-y-4">
          {[
            {
              q: "How does MailAlly ensure high email deliverability?",
              a: "We utilize multi-engine SMTP routing across Amazon SES, SendGrid, Mailgun, and custom relays, combined with automated IP warmups and SPF/DKIM verification."
            },
            {
              q: "Can I connect my existing CRM?",
              a: "Yes! MailAlly offers 1-click integrations and webhooks for HubSpot, Salesforce, Pipedrive, Zapier, and custom REST API endpoints."
            },
            {
              q: "What is the AI Content Studio?",
              a: "Our AI Content Studio uses Gemini AI to generate high-converting subject lines, body copy variations, and localized templates tuned specifically for cold outreach."
            },
            {
              q: "Is there a free trial?",
              a: "Yes, you can sign up for our free 14-day trial with full access to Professional features—no credit card required."
            }
          ].map((item, idx) => (
            <div key={idx} className="relative group">
              <div className="absolute -inset-1 bg-gradient-to-r from-[#FF7EB6]/25 via-[#C084FC]/25 to-[#FFB88C]/25 blur-lg rounded-[18px] -z-10 group-hover:opacity-100 opacity-60 transition-opacity duration-300" />
              <div className="bg-white border border-[#111111] rounded-[16px] overflow-hidden shadow-xs">
                <button
                  onClick={() => toggleFaq(idx)}
                  className="w-full p-5 text-left font-extrabold text-sm text-[#111111] flex items-center justify-between hover:bg-[#F9F9F9] transition-colors cursor-pointer"
                >
                  <span>{item.q}</span>
                  <span className="text-[#8A8A8A] text-lg font-bold">{openFaq === idx ? '−' : '+'}</span>
                </button>
                {openFaq === idx && (
                  <div className="px-5 pb-5 text-xs text-[#5F6368] leading-relaxed border-t border-[#ECECEC] pt-4 font-medium">
                    {item.a}
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* ─── CTA SECTION (WITH PALE LIGHT WATERCOLOR CONFETTI & FLOATING ENVELOPES BACKGROUND) ─── */}
      <section className="py-24 px-6 lg:px-12 max-w-[1280px] mx-auto">
        <div className="relative">
          
          {/* Blurred Pastel Gradient Shadow Glow behind CTA Box */}
          <div className="absolute -inset-4 bg-gradient-to-r from-[#FF7EB6]/45 via-[#C084FC]/45 to-[#FFB88C]/45 blur-3xl rounded-[40px] -z-10" />

          {/* Outer CTA Card Container with Crisp 1px Black Border */}
          <div className="bg-white border border-[#111111] rounded-[36px] p-10 lg:p-16 shadow-xs relative overflow-hidden">
            
            {/* Background Pale Light Watercolor Confetti & Floating Envelopes Layer */}
            <div className="absolute inset-0 pointer-events-none overflow-hidden z-0">
              
              {/* Soft Gradient Pale Light Splash */}
              <div className="absolute top-0 right-0 w-full h-full bg-gradient-to-bl from-[#FF7EB6]/25 via-[#C084FC]/20 via-[#FFEAA7]/25 to-transparent blur-xl" />

              {/* Colorful Watercolor Dots & Confetti Particles Overlay */}
              <svg className="absolute inset-0 w-full h-full opacity-75" preserveAspectRatio="none" viewBox="0 0 800 400" fill="none">
                <circle cx="650" cy="50" r="12" fill="#FF7EB6" opacity="0.6" />
                <circle cx="700" cy="90" r="8" fill="#C084FC" opacity="0.6" />
                <circle cx="580" cy="40" r="6" fill="#FFB88C" opacity="0.7" />
                <circle cx="620" cy="120" r="14" fill="#A7F3D0" opacity="0.5" />
                <circle cx="740" cy="140" r="10" fill="#BDEAFE" opacity="0.7" />
                <circle cx="520" cy="80" r="5" fill="#FFEAA7" opacity="0.8" />
                <circle cx="680" cy="200" r="16" fill="#FF7EB6" opacity="0.4" />
                <circle cx="760" cy="240" r="12" fill="#C084FC" opacity="0.5" />
                <circle cx="560" cy="180" r="9" fill="#0284C7" opacity="0.6" />
                <circle cx="610" cy="260" r="15" fill="#FFB88C" opacity="0.5" />
                <circle cx="720" cy="310" r="7" fill="#10B981" opacity="0.7" />
                <circle cx="480" cy="220" r="8" fill="#EC4899" opacity="0.6" />
                <circle cx="530" cy="320" r="11" fill="#F97316" opacity="0.5" />
                <circle cx="660" cy="360" r="13" fill="#BDEAFE" opacity="0.6" />
              </svg>

              {/* Floating Watercolor Email Envelopes */}
              <div className="absolute top-6 right-16 w-10 h-10 border-2 border-[#FF7EB6] bg-[#FFF0F5] rounded-lg rotate-12 flex items-center justify-center shadow-xs opacity-80 animate-bounce">
                <Mail className="w-5 h-5 text-[#FF7EB6]" />
              </div>
              <div className="absolute top-28 right-48 w-9 h-9 border-2 border-[#C084FC] bg-[#F5EEFF] rounded-lg -rotate-12 flex items-center justify-center shadow-xs opacity-80">
                <Mail className="w-4 h-4 text-[#C084FC]" />
              </div>
              <div className="absolute bottom-16 right-36 w-11 h-11 border-2 border-[#0284C7] bg-[#F0F9FF] rounded-lg rotate-6 flex items-center justify-center shadow-xs opacity-85">
                <Mail className="w-5 h-5 text-[#0284C7]" />
              </div>
              <div className="absolute bottom-8 right-80 w-8 h-8 border-2 border-[#F97316] bg-[#FFF7ED] rounded-lg -rotate-6 flex items-center justify-center shadow-xs opacity-80">
                <Mail className="w-4 h-4 text-[#F97316]" />
              </div>
              <div className="absolute top-1/2 right-12 -translate-y-1/2 w-10 h-10 border-2 border-[#10B981] bg-[#ECFDF5] rounded-lg rotate-4 flex items-center justify-center shadow-xs opacity-80">
                <Mail className="w-5 h-5 text-[#10B981]" />
              </div>

              {/* Soft Gradient Overlay for Crisp Text Contrast */}
              <div className="absolute inset-0 bg-gradient-to-r from-white via-white/90 to-white/35 z-0" />
            </div>

            {/* CTA Content Grid */}
            <div className="grid grid-cols-1 lg:grid-cols-12 gap-10 items-center relative z-10">
              
              <div className="lg:col-span-7 space-y-6">
                <h2 className="text-3xl sm:text-4xl lg:text-5xl font-extrabold text-[#111111] tracking-tight">
                  Ready to skyrocket your email results?
                </h2>
                <p className="text-base text-[#5F6368] max-w-md font-medium">
                  Join thousands of sales teams and businesses using MailAlly to grow faster.
                </p>
                <div className="flex flex-wrap items-center gap-4 pt-2">
                  <button
                    onClick={() => navigate('/register')}
                    className="bg-[#111111] hover:bg-[#222222] text-white font-extrabold text-sm px-7 py-3.5 rounded-xl transition-all duration-300 shadow-sm hover:shadow-lg hover:-translate-y-0.5 flex items-center gap-2 cursor-pointer"
                  >
                    <span>Start Free Trial</span>
                    <ArrowRight className="w-4 h-4" />
                  </button>
                  
                  <div className="relative inline-block">
                    <div className="absolute -inset-1 bg-gradient-to-r from-[#C084FC]/30 to-[#FFB88C]/30 blur-lg rounded-xl -z-10" />
                    <button
                      onClick={() => navigate('/landing')}
                      className="bg-white hover:bg-[#F9F9F9] text-[#111111] border border-[#111111] font-bold text-sm px-7 py-3.5 rounded-xl transition-all duration-300 hover:-translate-y-0.5 cursor-pointer shadow-xs"
                    >
                      Book a Demo
                    </button>
                  </div>
                </div>
              </div>

              {/* Right Mini Preview Card with Soft Gradient Shadow */}
              <div className="lg:col-span-5 hidden lg:block">
                <div className="relative">
                  <div className="absolute -inset-2 bg-gradient-to-r from-[#FF7EB6]/35 to-[#C084FC]/35 blur-xl rounded-[24px] -z-10" />
                  <div className="bg-white/90 backdrop-blur-md border border-[#111111] rounded-[20px] p-6 shadow-sm">
                    <div className="flex items-center justify-between mb-4">
                      <span className="text-xs font-bold text-[#111111]">Live Analytics</span>
                      <span className="w-2 h-2 rounded-full bg-emerald-500 animate-ping" />
                    </div>
                    <div className="space-y-3">
                      <div className="bg-white p-3.5 rounded-xl border border-[#ECECEC] flex items-center justify-between shadow-2xs">
                        <div>
                          <div className="text-[10px] text-[#8A8A8A] font-bold">Open Rate</div>
                          <div className="text-base font-extrabold text-[#111111]">42.7%</div>
                        </div>
                        <span className="text-xs text-emerald-600 font-bold bg-emerald-50 px-2 py-1 rounded-md">↑ +9.2%</span>
                      </div>

                      <div className="bg-white p-3.5 rounded-xl border border-[#ECECEC] flex items-center justify-between shadow-2xs">
                        <div>
                          <div className="text-[10px] text-[#8A8A8A] font-bold">Revenue Generated</div>
                          <div className="text-base font-extrabold text-[#111111]">$18,240</div>
                        </div>
                        <span className="text-xs text-emerald-600 font-bold bg-emerald-50 px-2 py-1 rounded-md">↑ +34.5%</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

            </div>
          </div>
        </div>
      </section>

      {/* ─── FOOTER (FULL WIDTH TO WEBSITE WITH FLUSH BOTTOM EDGE & TOP CURVES) ─── */}
      <footer className="bg-[#0B0B0E] text-white pt-14 pb-10 px-6 lg:px-16 w-full border-t border-[#222225] mt-16 rounded-t-[36px] rounded-b-none relative overflow-hidden font-sans">
        
        {/* Soft Ambient Glow (Right Side Neon Pink Radial Glow) */}
        <div className="pointer-events-none absolute top-0 right-0 w-[500px] h-full bg-gradient-to-l from-[#FF7EB6]/15 via-[#C084FC]/5 to-transparent blur-3xl rounded-full -z-10" />

        <div className="max-w-[1240px] mx-auto">
          
          {/* Top Section: 3-Column Layout with Vertical Border Dividers */}
          <div className="grid grid-cols-1 md:grid-cols-12 gap-8 md:gap-0 pb-8 border-b border-[#222225] items-stretch">
            
            {/* Column 1: Brand & Marcamor Badge (5 Cols) */}
            <div className="md:col-span-5 pr-0 md:pr-8 lg:pr-12 flex flex-col justify-between space-y-4">
              <div>
                <Link to="/" className="inline-flex items-center gap-3 group">
                  <CometLogo size="lg" />
                  <span className="text-2xl font-extrabold tracking-tight text-white">
                    MailAlly<span className="text-[#FF7EB6]">.</span>
                  </span>
                </Link>

                <p className="text-xs sm:text-sm text-gray-300 font-medium leading-relaxed max-w-sm mt-4">
                  AI email marketing engine built for high deliverability, smart segmentation, and automated cold outreach sequences.
                </p>
              </div>

              {/* Marcamor Pill Badge with Pink Glow */}
              <div>
                <a
                  href="https://marcamor.com"
                  target="_blank"
                  rel="noopener noreferrer"
                  className="inline-flex items-center gap-2.5 px-4 py-2 rounded-full bg-[#141419] border border-[#FF7EB6]/30 shadow-[0_0_15px_rgba(255,126,182,0.12)] hover:border-[#FF7EB6] transition-all duration-300 text-xs font-bold text-gray-300 hover:text-white group"
                >
                  <span className="w-2.5 h-2.5 rounded-full bg-[#FF7EB6] shadow-[0_0_8px_#FF7EB6] group-hover:animate-ping" />
                  <span>A product by <strong className="text-[#FF7EB6] font-extrabold">Marcamor Company</strong></span>
                  <ArrowUpRight className="w-3.5 h-3.5 text-gray-400 group-hover:text-white" />
                </a>
              </div>
            </div>

            {/* Column 2: Navigation Links with Vertical Dividers (3 Cols) */}
            <div className="md:col-span-3 px-0 md:px-8 lg:px-12 border-t md:border-t-0 md:border-l md:border-r border-[#222225] pt-6 md:pt-0">
              <h4 className="text-xs font-black tracking-widest text-[#FF7EB6] uppercase mb-4">NAVIGATION</h4>
              <ul className="space-y-3 text-xs sm:text-sm text-gray-200 font-semibold">
                <li>
                  <a href="#features" className="inline-flex items-center gap-2 hover:text-white transition-colors group">
                    <ChevronRight className="w-3.5 h-3.5 text-[#FF7EB6] group-hover:translate-x-0.5 transition-transform" />
                    <span>Features</span>
                  </a>
                </li>
                <li>
                  <a href="#workflow" className="inline-flex items-center gap-2 hover:text-white transition-colors group">
                    <ChevronRight className="w-3.5 h-3.5 text-[#FF7EB6] group-hover:translate-x-0.5 transition-transform" />
                    <span>How it Works</span>
                  </a>
                </li>
                <li>
                  <a href="#pricing" className="inline-flex items-center gap-2 hover:text-white transition-colors group">
                    <ChevronRight className="w-3.5 h-3.5 text-[#FF7EB6] group-hover:translate-x-0.5 transition-transform" />
                    <span>Pricing Plans</span>
                  </a>
                </li>
                <li>
                  <a href="#testimonials" className="inline-flex items-center gap-2 hover:text-white transition-colors group">
                    <ChevronRight className="w-3.5 h-3.5 text-[#FF7EB6] group-hover:translate-x-0.5 transition-transform" />
                    <span>Client Reviews</span>
                  </a>
                </li>
                <li>
                  <a href="#faq" className="inline-flex items-center gap-2 hover:text-white transition-colors group">
                    <ChevronRight className="w-3.5 h-3.5 text-[#FF7EB6] group-hover:translate-x-0.5 transition-transform" />
                    <span>FAQ & Support</span>
                  </a>
                </li>
              </ul>
            </div>

            {/* Column 3: Subscribe to Updates with Glowing White Capsule Input (4 Cols) */}
            <div className="md:col-span-4 pl-0 md:pl-8 lg:pl-12 pt-6 md:pt-0 space-y-3.5">
              <h4 className="text-xs font-black tracking-widest text-[#FF7EB6] uppercase mb-3">SUBSCRIBE TO UPDATES</h4>
              <p className="text-xs sm:text-sm text-gray-300 font-medium leading-relaxed">
                Get weekly deliverability benchmarks and AI copywriting tips.
              </p>
              
              {/* Capsule Shape Glowing White Input Box */}
              <div className="relative flex items-center w-full bg-white rounded-full p-1.5 border-2 border-[#FF7EB6] shadow-[0_0_25px_rgba(255,126,182,0.45)] mt-4">
                <Mail className="w-5 h-5 text-[#FF7EB6] ml-3 flex-shrink-0" />
                <input
                  type="email"
                  placeholder="Enter your work email"
                  className="w-full bg-transparent text-gray-900 placeholder:text-gray-400 font-semibold text-xs sm:text-sm px-3 focus:outline-none"
                />
                <button
                  aria-label="Subscribe"
                  className="w-9 h-9 rounded-full bg-gradient-to-tr from-[#FF7EB6] to-[#E052A0] text-white flex items-center justify-center shadow-md hover:scale-105 active:scale-95 transition-all flex-shrink-0 cursor-pointer"
                >
                  <Send className="w-4 h-4 text-white -mr-0.5" />
                </button>
              </div>
            </div>

          </div>

          {/* Bottom Bar: Social Icons + Copyright + Separated Legal Links */}
          <div className="pt-6 flex flex-col md:flex-row items-center justify-between gap-4 text-xs sm:text-sm text-gray-300 font-semibold">
            
            {/* Left: Glowing Social Icon Buttons */}
            <div className="flex items-center gap-3">
              <a
                href="https://marcamor.com"
                target="_blank"
                rel="noopener noreferrer"
                title="Marcamor Official Website"
                className="w-9 h-9 rounded-xl bg-[#141419] border border-[#FF7EB6]/30 shadow-[0_0_12px_rgba(255,126,182,0.15)] flex items-center justify-center text-white hover:border-[#FF7EB6] hover:bg-[#1E1E26] transition-all cursor-pointer"
              >
                <Globe className="w-4 h-4 text-white" />
              </a>

              <a
                href="https://x.com/marcamor"
                target="_blank"
                rel="noopener noreferrer"
                title="Marcamor X (Twitter)"
                className="w-9 h-9 rounded-xl bg-[#141419] border border-[#FF7EB6]/30 shadow-[0_0_12px_rgba(255,126,182,0.15)] flex items-center justify-center text-white hover:border-[#FF7EB6] hover:bg-[#1E1E26] transition-all cursor-pointer"
              >
                <svg className="w-3.5 h-3.5 fill-white" viewBox="0 0 24 24">
                  <path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-5.214-6.817L4.99 21.75H1.68l7.73-8.835L1.254 2.25H8.08l4.713 6.231zm-1.161 17.52h1.833L7.084 4.126H5.117z"/>
                </svg>
              </a>

              <a
                href="https://linkedin.com/company/marcamor"
                target="_blank"
                rel="noopener noreferrer"
                title="Marcamor LinkedIn"
                className="w-9 h-9 rounded-xl bg-[#141419] border border-[#FF7EB6]/30 shadow-[0_0_12px_rgba(255,126,182,0.15)] flex items-center justify-center text-white hover:border-[#FF7EB6] hover:bg-[#1E1E26] transition-all cursor-pointer"
              >
                <svg className="w-3.5 h-3.5 fill-white" viewBox="0 0 24 24">
                  <path d="M19 3a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h14m-.5 15.5v-5.3a3.26 3.26 0 0 0-3.26-3.26c-.85 0-1.84.52-2.28 1.3v-1.11h-2.79v8.37h2.79v-4.93c0-.77.62-1.4 1.39-1.4a1.4 1.4 0 0 1 1.4 1.4v4.93h2.75M6.46 10.9v8.37H9.25V10.9H6.46M7.86 6.74a1.62 1.62 0 1 0 0 3.24 1.62 1.62 0 0 0 0-3.24z"/>
                </svg>
              </a>

              <a
                href="https://github.com/marcamor"
                target="_blank"
                rel="noopener noreferrer"
                title="Marcamor GitHub"
                className="w-9 h-9 rounded-xl bg-[#141419] border border-[#FF7EB6]/30 shadow-[0_0_12px_rgba(255,126,182,0.15)] flex items-center justify-center text-white hover:border-[#FF7EB6] hover:bg-[#1E1E26] transition-all cursor-pointer"
              >
                <svg className="w-4 h-4 fill-white" viewBox="0 0 24 24">
                  <path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0 0 24 12c0-6.63-5.37-12-12-12z"/>
                </svg>
              </a>

              <a
                href="https://discord.gg/marcamor"
                target="_blank"
                rel="noopener noreferrer"
                title="Marcamor Community"
                className="w-9 h-9 rounded-xl bg-[#141419] border border-[#FF7EB6]/30 shadow-[0_0_12px_rgba(255,126,182,0.15)] flex items-center justify-center text-white hover:border-[#FF7EB6] hover:bg-[#1E1E26] transition-all cursor-pointer"
              >
                <svg className="w-4 h-4 fill-white" viewBox="0 0 24 24">
                  <path d="M20.317 4.37a19.791 19.791 0 0 0-4.885-1.515.074.074 0 0 0-.079.037c-.21.375-.444.864-.608 1.25a18.27 18.27 0 0 0-5.487 0 12.64 12.64 0 0 0-.617-1.25.077.077 0 0 0-.079-.037A19.736 19.736 0 0 0 3.677 4.37a.07.07 0 0 0-.032.027C.533 9.046-.32 13.58.099 18.057a.082.082 0 0 0 .031.057 19.9 19.9 0 0 0 5.993 3.03.078.078 0 0 0 .084-.028c.462-.63.874-1.295 1.226-1.994.021-.041.001-.09-.041-.106a13.107 13.107 0 0 1-1.872-.892.077.077 0 0 1-.008-.128 10.2 10.2 0 0 0 .372-.292.074.074 0 0 1 .077-.01c3.928 1.793 8.18 1.793 12.061 0a.074.074 0 0 1 .078.01c.12.098.246.198.373.292a.077.077 0 0 1-.006.127 12.299 12.299 0 0 1-1.873.892.077.077 0 0 0-.041.107c.36.698.772 1.362 1.225 1.993a.076.076 0 0 0 .084.028 19.839 19.839 0 0 0 6.002-3.03.077.077 0 0 0 .032-.054c.5-5.177-.838-9.674-3.549-13.66a.061.061 0 0 0-.031-.028zM8.02 15.33c-1.183 0-2.157-1.085-2.157-2.419 0-1.333.956-2.419 2.157-2.419 1.21 0 2.176 1.096 2.157 2.42 0 1.333-.956 2.418-2.157 2.418zm7.975 0c-1.183 0-2.157-1.085-2.157-2.419 0-1.333.955-2.419 2.157-2.419 1.21 0 2.176 1.096 2.157 2.42 0 1.333-.946 2.418-2.157 2.418z"/>
                </svg>
              </a>
            </div>

            {/* Center: Copyright */}
            <div className="text-xs text-gray-300 font-semibold">
              © 2026 <strong className="text-white font-bold">MailAlly</strong> by <a href="https://marcamor.com" target="_blank" rel="noopener noreferrer" className="text-[#FF7EB6] font-bold hover:underline">Marcamor Company</a>.
            </div>

            {/* Right: Legal Links with Vertical Line Separators */}
            <div className="flex items-center gap-3 text-xs text-gray-300 font-semibold">
              <a href="#faq" className="hover:text-white transition-colors">Privacy Policy</a>
              <span className="text-gray-600">|</span>
              <a href="#faq" className="hover:text-white transition-colors">Terms of Service</a>
              <span className="text-gray-600">|</span>
              <a href="#faq" className="hover:text-white transition-colors">Security</a>
            </div>

          </div>

        </div>
      </footer>

    </div>
  );
};
