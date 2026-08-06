import React, { useState } from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Mail, Lock, ArrowRight, Eye, EyeOff, Shield, Rocket, BarChart2, Sparkles, Globe, ChevronDown } from 'lucide-react';
import { CometLogo } from '../../components/common/CometLogo';

// Importing custom images from images folder
import messageIllustration from '../../../../images/message.png';

export const LoginPage = () => {
  const location = useLocation();
  const [email, setEmail] = useState(location.state?.email || 'admin@mailally.com');
  const [password, setPassword] = useState(location.state?.password || (location.state?.email ? '' : 'password123'));
  const [showPassword, setShowPassword] = useState(false);
  const [rememberMe, setRememberMe] = useState(true);
  const [error, setError] = useState('');
  const [language, setLanguage] = useState('English');
  const { login, loading } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    const res = await login(email, password);
    if (res.success) {
      navigate('/dashboard');
    } else {
      setError(res.message || 'Invalid email or password');
    }
  };

  return (
    <div className="min-h-screen w-full flex items-center justify-center p-4 sm:p-6 lg:p-10 relative overflow-hidden font-sans bg-[#EBF3FF]">
      
      {/* Background Soft Radial Lighting */}
      <div className="absolute top-[-10%] left-[-10%] w-[600px] h-[600px] rounded-full bg-[#0052FF]/15 blur-[140px] pointer-events-none" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[650px] h-[650px] rounded-full bg-[#00C6FF]/20 blur-[150px] pointer-events-none" />
      <div className="absolute top-[30%] right-[30%] w-[400px] h-[400px] rounded-full bg-[#2BAFF2]/10 blur-[120px] pointer-events-none" />

      {/* MAIN CONTAINER FRAME MATCHING DESIGN 1-TO-1 */}
      <div
        className="w-full max-w-[1240px] grid grid-cols-1 lg:grid-cols-12 rounded-[40px] overflow-hidden relative z-10 animate-scaleIn bg-[#0A60FF] border-[6px] border-white shadow-[0_30px_90px_-20px_rgba(0,85,255,0.35)] my-auto"
      >
        {/* LEFT BRANDING & VISUAL PANEL */}
        <div
          className="lg:col-span-6 p-8 lg:p-12 flex flex-col justify-between relative overflow-hidden text-white"
          style={{
            background: 'linear-gradient(145deg, #0052FF 0%, #0066FF 50%, #00C6FF 100%)',
          }}
        >
          {/* Background Ambient Lighting Circles */}
          <div className="absolute top-[-40px] right-[-40px] w-64 h-64 rounded-full bg-white/10 backdrop-blur-md border border-white/20 pointer-events-none" />
          <div className="absolute bottom-[-50px] left-[-50px] w-72 h-72 rounded-full bg-white/10 backdrop-blur-md pointer-events-none" />

          {/* Top Logo & Enterprise Badge */}
          <div className="relative z-10 space-y-6">
            <div className="flex items-center space-x-3 cursor-pointer" onClick={() => navigate('/landing')}>
              <CometLogo size="md" />
              <div>
                <span className="text-2xl font-black tracking-tight block text-white" style={{ fontFamily: 'var(--font-heading)' }}>
                  MailAlly<span className="text-[#00F0FF]">.</span>
                </span>
                <span className="text-[9px] font-extrabold uppercase tracking-[2.2px] block text-cyan-100 mt-0.5">
                  ENTERPRISE SAAS PLATFORM
                </span>
              </div>
            </div>

            <div className="inline-flex items-center space-x-2 px-4 py-1.5 rounded-full text-xs font-bold text-white border backdrop-blur-md bg-white/15 border-white/30 shadow-xs">
              <Sparkles className="w-3.5 h-3.5 text-[#00F0FF]" />
              <span>Enterprise Email Marketing Solution</span>
            </div>
          </div>

          {/* Headline & Sub-headline */}
          <div className="my-4 relative z-10 space-y-3">
            <h1
              className="text-3xl lg:text-5xl font-black leading-[1.12] text-white tracking-tight"
              style={{ fontFamily: 'var(--font-heading)' }}
            >
              Power Smarter <br />
              Email <span className="text-[#00F0FF] drop-shadow-sm">Campaigns</span>
            </h1>
            <p className="text-xs lg:text-sm leading-relaxed text-white/90 max-w-md font-medium">
              Smart automation, multi-provider delivery, and real-time analytics to maximize engagement and ROI.
            </p>
          </div>

          {/* Center Visual Asset — Replaced with message.png from images folder */}
          <div className="relative z-10 my-4 flex items-center justify-center">
            <div className="p-4 bg-white/10 backdrop-blur-md rounded-3xl border border-white/20 shadow-2xl">
              <img
                src={messageIllustration}
                alt="MailAlly Outreach Illustration"
                className="w-full max-w-[360px] max-h-[260px] object-contain filter drop-shadow-lg transform hover:scale-105 transition-transform duration-500 rounded-2xl"
              />
            </div>
          </div>

          {/* Bottom 3 Glass Feature Badges */}
          <div className="grid grid-cols-3 gap-3 relative z-10 pt-2">
            <div className="p-3 rounded-2xl bg-white/15 border border-white/25 backdrop-blur-md flex items-center gap-2.5 shadow-xs">
              <div className="w-8 h-8 rounded-xl bg-white/20 flex items-center justify-center text-[#00F0FF] shrink-0">
                <Shield className="w-4 h-4" />
              </div>
              <div className="overflow-hidden">
                <h4 className="text-[11px] font-extrabold text-white truncate">Secure & Reliable</h4>
                <p className="text-[9px] text-white/80 font-medium truncate">Enterprise-grade security</p>
              </div>
            </div>

            <div className="p-3 rounded-2xl bg-white/15 border border-white/25 backdrop-blur-md flex items-center gap-2.5 shadow-xs">
              <div className="w-8 h-8 rounded-xl bg-white/20 flex items-center justify-center text-[#00F0FF] shrink-0">
                <Rocket className="w-4 h-4" />
              </div>
              <div className="overflow-hidden">
                <h4 className="text-[11px] font-extrabold text-white truncate">High Deliverability</h4>
                <p className="text-[9px] text-white/80 font-medium truncate">Multi-provider routing</p>
              </div>
            </div>

            <div className="p-3 rounded-2xl bg-white/15 border border-white/25 backdrop-blur-md flex items-center gap-2.5 shadow-xs">
              <div className="w-8 h-8 rounded-xl bg-white/20 flex items-center justify-center text-[#00F0FF] shrink-0">
                <BarChart2 className="w-4 h-4" />
              </div>
              <div className="overflow-hidden">
                <h4 className="text-[11px] font-extrabold text-white truncate">Real-time Analytics</h4>
                <p className="text-[9px] text-white/80 font-medium truncate">Track performance live</p>
              </div>
            </div>
          </div>
        </div>

        {/* RIGHT FORM CARD PANEL */}
        <div className="lg:col-span-6 p-8 lg:p-12 bg-white flex flex-col justify-between items-center text-slate-800 relative">
          
          {/* Top Language Selector */}
          <div className="w-full flex items-center justify-between mb-4">
            <div />
            <div className="relative">
              <button className="flex items-center gap-1.5 px-3 py-1.5 rounded-xl border border-slate-200 bg-slate-50 hover:bg-slate-100 text-xs font-semibold text-slate-600 transition-colors">
                <Globe className="w-3.5 h-3.5 text-slate-500" />
                <span>{language}</span>
                <ChevronDown className="w-3.5 h-3.5 text-slate-400" />
              </button>
            </div>
          </div>

          <div className="w-full max-w-[440px] my-auto space-y-6">
            
            {/* Center Logo Icon Box */}
            <div className="mx-auto flex justify-center">
              <div className="w-16 h-16 rounded-2xl bg-gradient-to-br from-blue-50 to-sky-100 border border-blue-200 flex items-center justify-center text-blue-600 shadow-md">
                <CometLogo size="md" />
              </div>
            </div>

            {/* Header */}
            <div className="text-center space-y-1">
              <h2
                className="text-3xl font-black text-slate-900 tracking-tight"
                style={{ fontFamily: 'var(--font-heading)' }}
              >
                Welcome Back
              </h2>
              <p className="text-xs text-slate-500 font-medium">
                Sign in to access your enterprise workspace
              </p>
            </div>

            {/* Error Notification */}
            {error && (
              <div className="p-3.5 rounded-xl text-xs font-bold bg-rose-50 text-rose-600 border border-rose-200 animate-fadeIn text-center">
                {error}
              </div>
            )}

            {/* Form */}
            <form onSubmit={handleSubmit} className="space-y-4 text-left">
              <div>
                <label className="block text-xs font-bold text-slate-700 mb-1.5">
                  Work Email
                </label>
                <div className="relative">
                  <Mail className="w-4 h-4 absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" />
                  <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="name@yourcompany.com"
                    required
                    className="w-full h-[52px] px-5 pl-11 rounded-xl border-2 border-slate-200 focus:border-blue-600 focus:ring-4 focus:ring-blue-100 text-xs font-semibold transition-all outline-none text-slate-900 bg-white"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-700 mb-1.5">
                  Password
                </label>
                <div className="relative">
                  <Lock className="w-4 h-4 absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" />
                  <input
                    type={showPassword ? 'text' : 'password'}
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="••••••••••••"
                    required
                    className="w-full h-[52px] px-5 pl-11 pr-11 rounded-xl border-2 border-slate-200 focus:border-blue-600 focus:ring-4 focus:ring-blue-100 text-xs font-semibold transition-all outline-none text-slate-900 bg-white"
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-4 top-1/2 -translate-y-1/2 text-slate-400 hover:text-blue-600 transition-colors cursor-pointer"
                  >
                    {showPassword ? <Eye className="w-4 h-4" /> : <EyeOff className="w-4 h-4" />}
                  </button>
                </div>
              </div>

              <div className="flex items-center justify-between text-xs pt-1">
                <label className="flex items-center space-x-2 cursor-pointer">
                  <input
                    type="checkbox"
                    checked={rememberMe}
                    onChange={(e) => setRememberMe(e.target.checked)}
                    className="w-4 h-4 rounded border-slate-300 text-blue-600 focus:ring-blue-500 cursor-pointer"
                  />
                  <span className="font-semibold text-slate-600">Remember me</span>
                </label>

                <Link to="/register" className="font-bold text-blue-600 hover:underline">
                  Forgot password?
                </Link>
              </div>

              <button
                type="submit"
                disabled={loading}
                className="w-full h-[52px] rounded-xl font-bold text-white text-sm flex items-center justify-center space-x-2 transition-all duration-200 cursor-pointer shadow-lg shadow-blue-500/25 mt-2"
                style={{
                  background: 'linear-gradient(135deg, #0052FF 0%, #0066FF 50%, #00C6FF 100%)',
                }}
              >
                {loading ? (
                  <span>Authenticating...</span>
                ) : (
                  <>
                    <span>Sign In to Dashboard</span>
                    <ArrowRight className="w-4 h-4" />
                  </>
                )}
              </button>
            </form>

            {/* Divider */}
            <div className="relative flex py-1 items-center">
              <div className="flex-grow border-t border-slate-200"></div>
              <span className="flex-shrink mx-4 text-[11px] font-semibold text-slate-400">or continue with</span>
              <div className="flex-grow border-t border-slate-200"></div>
            </div>

            {/* Google & Microsoft Social OAuth Buttons */}
            <div className="grid grid-cols-2 gap-3">
              <button
                type="button"
                onClick={() => {
                  setEmail('admin@mailally.com');
                  setPassword('password123');
                  alert('Google OAuth connected stub. Logging in as Admin.');
                }}
                className="flex items-center justify-center gap-2 h-11 px-3 rounded-xl border border-slate-200 hover:border-slate-300 bg-white hover:bg-slate-50 text-xs font-bold text-slate-700 transition-colors shadow-xs"
              >
                <svg className="w-4 h-4" viewBox="0 0 24 24">
                  <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
                  <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
                  <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.06H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.94l2.85-2.22.81-.63z"/>
                  <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.06l3.66 2.84c.87-2.6 3.3-4.52 6.16-4.52z"/>
                </svg>
                <span>Continue with Google</span>
              </button>

              <button
                type="button"
                onClick={() => {
                  setEmail('ashok@mailally.com');
                  setPassword('password123');
                  alert('Microsoft OAuth connected stub. Logging in as Ashok.');
                }}
                className="flex items-center justify-center gap-2 h-11 px-3 rounded-xl border border-slate-200 hover:border-slate-300 bg-white hover:bg-slate-50 text-xs font-bold text-slate-700 transition-colors shadow-xs"
              >
                <svg className="w-4 h-4" viewBox="0 0 23 23">
                  <path fill="#f35325" d="M1 1h10v10H1z"/>
                  <path fill="#81bc06" d="M12 1h10v10H12z"/>
                  <path fill="#05a6f0" d="M1 12h10v10H1z"/>
                  <path fill="#ffba08" d="M12 12h10v10H12z"/>
                </svg>
                <span>Continue with Microsoft</span>
              </button>
            </div>

            {/* Bottom Registration Prompt */}
            <p className="text-xs font-semibold text-slate-500 pt-2 text-center">
              Don't have an enterprise workspace?{' '}
              <Link to="/register" className="font-bold text-blue-600 hover:underline">
                Create Organization
              </Link>
            </p>
          </div>
        </div>

      </div>
    </div>
  );
};
