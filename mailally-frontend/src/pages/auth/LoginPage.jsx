import React, { useState } from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Mail, Lock, ArrowRight, Eye, EyeOff, Check, Globe, ChevronDown, Loader2 } from 'lucide-react';
import { CometLogo } from '../../components/common/CometLogo';

import messageIllustration from '../../../../images/message.png';

export const LoginPage = () => {
  const location = useLocation();
  const [email, setEmail] = useState(location.state?.email || 'admin@mailally.com');
  const [password, setPassword] = useState(location.state?.password || (location.state?.email ? '' : 'password123'));
  const [showPassword, setShowPassword] = useState(false);
  const [rememberMe, setRememberMe] = useState(true);
  const [error, setError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [language, setLanguage] = useState('English');
  const { login, loading } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsSubmitting(true);
    try {
      const res = await login(email, password);
      if (res.success) {
        navigate('/dashboard');
      } else {
        setError(res.message || 'Invalid email or password');
      }
    } catch (err) {
      setError('Login failed. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  const isLoading = loading || isSubmitting;

  return (
    <div className="min-h-screen w-full flex items-center justify-center p-4 sm:p-6 lg:p-10 relative overflow-hidden font-sans bg-[#FAFAFA]">
      
      {/* Soft Background Glow */}
      <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[800px] h-[800px] rounded-full bg-gradient-to-tr from-[#FFF0F5] via-[#F3E8FF] to-[#E0F2FE] blur-[160px] pointer-events-none" />

      {/* Main Container Wrapper with Outer Pale Gradient Shadow Ring */}
      <div className="relative w-full max-w-[1100px] my-auto group">
        
        {/* OUTSIDE PALE GRADIENT SHADOW GLOW RING */}
        <div className="absolute -inset-2 rounded-[36px] bg-gradient-to-r from-[#FBCFE8] via-[#DDD6FE] to-[#BAE6FD] opacity-60 blur-xl pointer-events-none transition-all duration-500 group-hover:opacity-85 group-hover:blur-2xl" />
        
        {/* Main Box Card */}
        <div className="w-full grid grid-cols-1 lg:grid-cols-12 rounded-[28px] overflow-hidden relative z-10 bg-white border border-[#18181B] shadow-2xl">
          
          {/* Left Branding Panel — Pale Color Gradient Theme */}
          <div className="lg:col-span-6 p-8 lg:p-12 flex flex-col justify-between relative bg-gradient-to-br from-[#FFF0F5] via-[#FAF5FF] to-[#F0F9FF] text-[#18181B] border-b lg:border-b-0 lg:border-r border-[#E4E4E7] overflow-hidden">
            
            {/* Pale Ambient Light Blobs */}
            <div className="absolute top-[-10%] left-[-10%] w-[300px] h-[300px] rounded-full bg-[#FBCFE8]/40 blur-[80px] pointer-events-none" />
            <div className="absolute bottom-[-10%] right-[-10%] w-[300px] h-[300px] rounded-full bg-[#DDD6FE]/40 blur-[80px] pointer-events-none" />
            <div className="absolute top-[40%] right-[10%] w-[200px] h-[200px] rounded-full bg-[#BAE6FD]/40 blur-[70px] pointer-events-none" />

            {/* Top Logo */}
            <div className="space-y-6 relative z-10">
              <div className="flex items-center gap-3 cursor-pointer" onClick={() => navigate('/landing')}>
                <div className="p-1 rounded-xl bg-white shadow-xs border border-[#18181B]">
                  <CometLogo size="md" />
                </div>
                <div>
                  <span className="text-xl font-extrabold tracking-tight block text-[#18181B]">
                    MailAlly
                  </span>
                  <span className="text-[9px] font-bold uppercase tracking-[2px] block text-[#71717A]">
                    ENTERPRISE PLATFORM
                  </span>
                </div>
              </div>

              {/* Headline & Sub-headline */}
              <div className="space-y-3 pt-2">
                <h1 className="text-3xl lg:text-4xl font-black leading-[1.15] text-[#18181B] tracking-tight">
                  Power <span className="px-2 py-0.5 rounded-lg bg-[#FCE7F3] text-[#DB2777] border border-[#FBCFE8]">Smarter</span> <br />
                  Email Campaigns
                </h1>
                <p className="text-xs lg:text-[13px] leading-relaxed text-[#52525B] font-medium max-w-sm">
                  Automation, multi-provider delivery, and real-time telemetry to maximize engagement.
                </p>
              </div>
            </div>

            {/* Center Visual Asset — PNG Illustration in Pale Glass Card */}
            <div className="relative z-10 my-8 flex items-center justify-center">
              <div className="relative w-full max-w-[340px] group/img">
                <div className="relative p-5 rounded-[22px] bg-white/80 backdrop-blur-md border border-[#18181B] shadow-sm transition-all duration-300 group-hover/img:shadow-md">
                  <img
                    src={messageIllustration}
                    alt="MailAlly Email Campaign Illustration"
                    className="w-full h-auto max-h-[200px] object-contain filter brightness-105 transform group-hover/img:scale-[1.02] transition-transform duration-500 rounded-xl"
                  />
                </div>
              </div>
            </div>

            {/* Bottom Feature List with Green Checkmarks */}
            <div className="space-y-2.5 relative z-10 pt-4 border-t border-[#E4E4E7]">
              <div className="flex items-center gap-2.5 text-xs font-semibold text-[#18181B]">
                <div className="w-5 h-5 rounded-full bg-[#DCFCE7] flex items-center justify-center flex-shrink-0 border border-[#86EFAC]">
                  <Check className="w-3.5 h-3.5 text-[#16A34A] stroke-[2.5]" />
                </div>
                <span>5,000+ Automated Emails / Minute</span>
              </div>
              <div className="flex items-center gap-2.5 text-xs font-semibold text-[#18181B]">
                <div className="w-5 h-5 rounded-full bg-[#DCFCE7] flex items-center justify-center flex-shrink-0 border border-[#86EFAC]">
                  <Check className="w-3.5 h-3.5 text-[#16A34A] stroke-[2.5]" />
                </div>
                <span>Multi-Provider Relays & Failover</span>
              </div>
              <div className="flex items-center gap-2.5 text-xs font-semibold text-[#18181B]">
                <div className="w-5 h-5 rounded-full bg-[#DCFCE7] flex items-center justify-center flex-shrink-0 border border-[#86EFAC]">
                  <Check className="w-3.5 h-3.5 text-[#16A34A] stroke-[2.5]" />
                </div>
                <span>Real-Time Webhook Telemetry & Analytics</span>
              </div>
            </div>
          </div>

          {/* Right Form Panel — Clean White */}
          <div className="lg:col-span-6 p-8 lg:p-12 bg-white flex flex-col justify-between text-[#18181B] relative">
            
            {/* Top Language Selector */}
            <div className="w-full flex items-center justify-end">
              <button className="flex items-center gap-1.5 px-3 py-1.5 rounded-xl border border-[#18181B] bg-white hover:bg-[#FAFAFA] text-xs font-semibold text-[#18181B] transition-colors cursor-pointer">
                <Globe className="w-3.5 h-3.5 text-[#71717A]" />
                <span>{language}</span>
                <ChevronDown className="w-3.5 h-3.5 text-[#71717A]" />
              </button>
            </div>

            <div className="w-full max-w-[380px] mx-auto my-auto space-y-6">
              
              {/* Header */}
              <div className="space-y-1 text-left">
                <h2 className="text-2xl lg:text-3xl font-black tracking-tight text-[#18181B]">
                  Welcome back
                </h2>
                <p className="text-xs text-[#71717A] font-medium">
                  Sign in to your enterprise workspace
                </p>
              </div>

              {/* Error Banner */}
              {error && (
                <div className="p-3.5 rounded-xl text-xs font-semibold bg-rose-50 text-rose-600 border border-rose-200">
                  {error}
                </div>
              )}

              {/* Form */}
              <form onSubmit={handleSubmit} className="space-y-4 text-left">
                <div>
                  <label className="block text-xs font-bold text-[#18181B] mb-1.5">
                    Work Email
                  </label>
                  <div className="relative flex items-center">
                    <Mail className="w-4 h-4 absolute left-3.5 text-[#71717A]" />
                    <input
                      type="email"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      placeholder="admin@mailally.com"
                      required
                      className="w-full h-11 pl-10 pr-4 rounded-xl border border-[#18181B] focus:ring-2 focus:ring-black/5 text-xs font-semibold text-[#18181B] bg-white outline-none transition-all placeholder:text-[#A1A1AA]"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-bold text-[#18181B] mb-1.5">
                    Password
                  </label>
                  <div className="relative flex items-center">
                    <Lock className="w-4 h-4 absolute left-3.5 text-[#71717A]" />
                    <input
                      type={showPassword ? 'text' : 'password'}
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      placeholder="••••••••"
                      required
                      className="w-full h-11 pl-10 pr-10 rounded-xl border border-[#18181B] focus:ring-2 focus:ring-black/5 text-xs font-semibold text-[#18181B] bg-white outline-none transition-all placeholder:text-[#A1A1AA]"
                    />
                    <button
                      type="button"
                      onClick={() => setShowPassword(!showPassword)}
                      className="absolute right-3.5 text-[#71717A] hover:text-[#18181B] transition-colors cursor-pointer"
                    >
                      {showPassword ? <Eye className="w-4 h-4" /> : <EyeOff className="w-4 h-4" />}
                    </button>
                  </div>
                </div>

                <div className="flex items-center justify-between text-xs pt-0.5">
                  <label className="flex items-center gap-2 cursor-pointer">
                    <input
                      type="checkbox"
                      checked={rememberMe}
                      onChange={(e) => setRememberMe(e.target.checked)}
                      className="w-4 h-4 rounded border-[#18181B] accent-[#18181B] cursor-pointer"
                    />
                    <span className="font-semibold text-[#52525B]">Remember me</span>
                  </label>

                  <Link to="/register" className="font-bold text-[#18181B] hover:underline">
                    Forgot password?
                  </Link>
                </div>

                {/* Solid Black Button */}
                <button
                  type="submit"
                  disabled={isLoading}
                  className="w-full h-11 rounded-xl bg-[#18181B] hover:bg-black disabled:bg-[#18181B]/80 text-white font-bold text-xs flex items-center justify-center gap-2 transition-all cursor-pointer shadow-sm mt-1"
                >
                  {isLoading ? (
                    <>
                      <Loader2 className="w-4 h-4 animate-spin text-[#10B981]" />
                      <span>Signing in to workspace...</span>
                    </>
                  ) : (
                    <>
                      <span>Sign In</span>
                      <ArrowRight className="w-3.5 h-3.5" />
                    </>
                  )}
                </button>
              </form>

              {/* Social Divider */}
              <div className="relative flex py-1 items-center">
                <div className="flex-grow border-t border-[#E4E4E7]"></div>
                <span className="flex-shrink mx-3 text-[10px] font-bold tracking-wider uppercase text-[#A1A1AA]">OR CONTINUE WITH</span>
                <div className="flex-grow border-t border-[#E4E4E7]"></div>
              </div>

              {/* Social Buttons */}
              <div className="grid grid-cols-2 gap-3">
                <button
                  type="button"
                  onClick={() => {
                    setEmail('admin@mailally.com');
                    setPassword('password123');
                  }}
                  className="flex items-center justify-center gap-2 h-10 px-3 rounded-xl border border-[#18181B] hover:bg-[#FAFAFA] text-xs font-bold text-[#18181B] transition-colors cursor-pointer"
                >
                  <svg className="w-4 h-4" viewBox="0 0 24 24">
                    <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                    <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                    <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.06H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.94l2.85-2.22.81-.63z"/>
                    <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.06l3.66 2.84c.87-2.6 3.3-4.52 6.16-4.52z"/>
                  </svg>
                  <span>Google</span>
                </button>

                <button
                  type="button"
                  onClick={() => {
                    setEmail('ashok@mailally.com');
                    setPassword('password123');
                  }}
                  className="flex items-center justify-center gap-2 h-10 px-3 rounded-xl border border-[#18181B] hover:bg-[#FAFAFA] text-xs font-bold text-[#18181B] transition-colors cursor-pointer"
                >
                  <svg className="w-4 h-4" viewBox="0 0 23 23">
                    <path fill="#f35325" d="M1 1h10v10H1z"/>
                    <path fill="#81bc06" d="M12 1h10v10H1z"/>
                    <path fill="#05a6f0" d="M1 12h10v10H1z"/>
                    <path fill="#ffba08" d="M12 12h10v10H1z"/>
                  </svg>
                  <span>Microsoft 365</span>
                </button>
              </div>

              <p className="text-xs font-semibold text-[#71717A] text-center pt-2">
                Don't have an account?{' '}
                <Link to="/register" className="font-bold text-[#18181B] hover:underline">
                  Create Organization
                </Link>
              </p>
            </div>
          </div>

        </div>

      </div>
    </div>
  );
};
