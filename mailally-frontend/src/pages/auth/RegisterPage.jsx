import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { Mail, Lock, Building, ArrowRight, User, Check, Globe, ChevronDown, Loader2 } from 'lucide-react';
import { CometLogo } from '../../components/common/CometLogo';

import emailServiceIllustration from '../../../../images/—Pngtree—email service is electronic mail_7258151.png';

export const RegisterPage = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    organizationName: ''
  });
  const [error, setError] = useState('');
  const [successMsg, setSuccessMsg] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [language, setLanguage] = useState('English');
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccessMsg('');

    if (!formData.email || !formData.email.includes('@')) {
      setError('Please enter a valid work email address');
      return;
    }

    if (!formData.password || formData.password.length < 8) {
      setError('Password must be at least 8 characters long');
      return;
    }

    setIsSubmitting(true);
    try {
      const res = await register(formData);
      if (res && res.success) {
        setSuccessMsg('Organization registered successfully! Redirecting to login...');
        setTimeout(() => {
          navigate('/login', { state: { email: formData.email, password: formData.password } });
        }, 1100);
      } else {
        setError(res?.message || 'Registration failed. Please check your inputs.');
      }
    } catch (err) {
      setError('Registration failed. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

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
                  Create Your <br />
                  <span className="px-2 py-0.5 rounded-lg bg-[#FCE7F3] text-[#DB2777] border border-[#FBCFE8]">Organization</span>
                </h1>
                <p className="text-xs lg:text-[13px] leading-relaxed text-[#52525B] font-medium max-w-sm">
                  Setup your enterprise workspace in under 2 minutes with zero friction.
                </p>
              </div>
            </div>

            {/* Center Visual Asset — PNG Illustration in Pale Glass Card */}
            <div className="relative z-10 my-8 flex items-center justify-center">
              <div className="relative w-full max-w-[340px] group/img">
                <div className="relative p-5 rounded-[22px] bg-white/80 backdrop-blur-md border border-[#18181B] shadow-sm transition-all duration-300 group-hover/img:shadow-md">
                  <img
                    src={emailServiceIllustration}
                    alt="MailAlly Email Service Illustration"
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
                <span>Dedicated Organization Workspace</span>
              </div>
              <div className="flex items-center gap-2.5 text-xs font-semibold text-[#18181B]">
                <div className="w-5 h-5 rounded-full bg-[#DCFCE7] flex items-center justify-center flex-shrink-0 border border-[#86EFAC]">
                  <Check className="w-3.5 h-3.5 text-[#16A34A] stroke-[2.5]" />
                </div>
                <span>Multi-User Seat Management & RBAC</span>
              </div>
              <div className="flex items-center gap-2.5 text-xs font-semibold text-[#18181B]">
                <div className="w-5 h-5 rounded-full bg-[#DCFCE7] flex items-center justify-center flex-shrink-0 border border-[#86EFAC]">
                  <Check className="w-3.5 h-3.5 text-[#16A34A] stroke-[2.5]" />
                </div>
                <span>Full Access to AI Content Studio</span>
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

            <div className="w-full max-w-[380px] mx-auto my-auto space-y-5">
              
              {/* Header */}
              <div className="space-y-1 text-left">
                <h2 className="text-2xl lg:text-3xl font-black tracking-tight text-[#18181B]">
                  Register Organization
                </h2>
                <p className="text-xs text-[#71717A] font-medium">
                  Start your 14-day free enterprise trial
                </p>
              </div>

              {/* Notifications */}
              {error && (
                <div className="p-3.5 rounded-xl text-xs font-semibold bg-rose-50 text-rose-600 border border-rose-200">
                  {error}
                </div>
              )}
              {successMsg && (
                <div className="p-[#E6F4EA] p-3.5 rounded-xl text-xs font-semibold bg-emerald-50 text-emerald-700 border border-emerald-200">
                  {successMsg}
                </div>
              )}

              {/* Form */}
              <form onSubmit={handleSubmit} className="space-y-3.5 text-left">
                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <label className="block text-xs font-bold text-[#18181B] mb-1">First Name</label>
                    <div className="relative flex items-center">
                      <User className="w-4 h-4 absolute left-3 text-[#71717A]" />
                      <input
                        type="text"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleChange}
                        placeholder="Sarah"
                        required
                        className="w-full h-10 pl-9 pr-3 rounded-xl border border-[#18181B] focus:ring-2 focus:ring-black/5 text-xs font-semibold text-[#18181B] bg-white outline-none placeholder:text-[#A1A1AA]"
                      />
                    </div>
                  </div>

                  <div>
                    <label className="block text-xs font-bold text-[#18181B] mb-1">Last Name</label>
                    <input
                      type="text"
                      name="lastName"
                      value={formData.lastName}
                      onChange={handleChange}
                      placeholder="Connor"
                      required
                      className="w-full h-10 px-3 rounded-xl border border-[#18181B] focus:ring-2 focus:ring-black/5 text-xs font-semibold text-[#18181B] bg-white outline-none placeholder:text-[#A1A1AA]"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-bold text-[#18181B] mb-1">Organization Name</label>
                  <div className="relative flex items-center">
                    <Building className="w-4 h-4 absolute left-3 text-[#71717A]" />
                    <input
                      type="text"
                      name="organizationName"
                      value={formData.organizationName}
                      onChange={handleChange}
                      placeholder="Acme Corp"
                      required
                      className="w-full h-10 pl-9 pr-3 rounded-xl border border-[#18181B] focus:ring-2 focus:ring-black/5 text-xs font-semibold text-[#18181B] bg-white outline-none placeholder:text-[#A1A1AA]"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-bold text-[#18181B] mb-1">Work Email</label>
                  <div className="relative flex items-center">
                    <Mail className="w-4 h-4 absolute left-3 text-[#71717A]" />
                    <input
                      type="email"
                      name="email"
                      value={formData.email}
                      onChange={handleChange}
                      placeholder="name@company.com"
                      required
                      className="w-full h-10 pl-9 pr-3 rounded-xl border border-[#18181B] focus:ring-2 focus:ring-black/5 text-xs font-semibold text-[#18181B] bg-white outline-none placeholder:text-[#A1A1AA]"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-bold text-[#18181B] mb-1">Password</label>
                  <div className="relative flex items-center">
                    <Lock className="w-4 h-4 absolute left-3 text-[#71717A]" />
                    <input
                      type="password"
                      name="password"
                      value={formData.password}
                      onChange={handleChange}
                      placeholder="Min. 8 characters"
                      required
                      className="w-full h-10 pl-9 pr-3 rounded-xl border border-[#18181B] focus:ring-2 focus:ring-black/5 text-xs font-semibold text-[#18181B] bg-white outline-none placeholder:text-[#A1A1AA]"
                    />
                  </div>
                </div>

                <button
                  type="submit"
                  disabled={isSubmitting}
                  className="w-full h-11 rounded-xl bg-[#18181B] hover:bg-black text-white font-bold text-xs flex items-center justify-center gap-2 transition-all cursor-pointer shadow-sm mt-1"
                >
                  {isSubmitting ? (
                    <>
                      <Loader2 className="w-4 h-4 animate-spin text-[#10B981]" />
                      <span>Creating Account...</span>
                    </>
                  ) : (
                    <>
                      <span>Get Started</span>
                      <ArrowRight className="w-3.5 h-3.5" />
                    </>
                  )}
                </button>
              </form>

              <p className="text-xs font-semibold text-[#71717A] text-center pt-1">
                Already have an account?{' '}
                <Link to="/login" className="font-bold text-[#18181B] hover:underline">
                  Sign In
                </Link>
              </p>
            </div>
          </div>

        </div>
      </div>
    </div>
  );
};
