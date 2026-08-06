import React, { useState, useRef, useEffect } from 'react';
import { Search, Bell, LogOut, User, Plus, Command, ChevronDown } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate, useLocation } from 'react-router-dom';

export const Navbar = () => {
  const { currentUser, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [showProfileMenu, setShowProfileMenu] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [searchFocused, setSearchFocused] = useState(false);
  const profileRef = useRef(null);

  const currentPath = location.pathname.substring(1) || 'Dashboard';
  const pageTitle = currentPath.split('/')[0];
  const formattedTitle = pageTitle.charAt(0).toUpperCase() + pageTitle.slice(1);
  const userName = currentUser?.email ? currentUser.email.split('@')[0] : 'Akash';
  const initials = userName.substring(0, 2).toUpperCase();

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (profileRef.current && !profileRef.current.contains(e.target)) {
        setShowProfileMenu(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/dashboard?search=${encodeURIComponent(searchQuery)}`);
    }
  };

  return (
    <header className="h-[72px] px-6 lg:px-8 flex items-center justify-between z-20 bg-[#F7FAFF]/80 backdrop-blur-md border-b border-blue-100/50">
      {/* Title & Greeting */}
      <div className="flex flex-col">
        <h1 className="text-xl font-black tracking-tight text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
          {formattedTitle}
        </h1>
        <p className="text-[11px] font-medium text-slate-500 hidden sm:block">
          Welcome back, <span className="font-bold text-[#2563EB]">{userName}!</span> 👋 Here's what's happening today.
        </p>
      </div>

      {/* Global Search & Actions */}
      <div className="flex items-center space-x-4">
        {/* Modern Floating Glass Search */}
        <form onSubmit={handleSearch} className="relative w-80 hidden md:block">
          <Search
            className="w-4 h-4 absolute left-4 top-1/2 transform -translate-y-1/2 transition-colors"
            style={{ color: searchFocused ? '#2563EB' : '#94A3B8' }}
          />
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Search campaigns, contacts, templates..."
            className="w-full pl-11 pr-14 py-2.5 rounded-2xl text-xs font-medium transition-all outline-none text-[#1E3A8A] bg-white border"
            style={{
              borderColor: searchFocused ? '#2563EB' : 'rgba(37,99,235,0.12)',
              boxShadow: searchFocused 
                ? '0 4px 20px rgba(37, 99, 235, 0.15)' 
                : '0 2px 10px rgba(37, 99, 235, 0.04)',
            }}
            onFocus={() => setSearchFocused(true)}
            onBlur={() => setSearchFocused(false)}
          />
          <div className="absolute right-3.5 top-1/2 transform -translate-y-1/2 flex items-center gap-0.5 px-2 py-0.5 rounded-lg text-[10px] font-bold bg-blue-50 text-[#2563EB] border border-blue-100">
            <Command className="w-2.5 h-2.5" />
            <span>K</span>
          </div>
        </form>

        {/* New Campaign Button */}
        <button
          onClick={() => navigate('/campaigns')}
          className="flex items-center space-x-2 px-5 py-2.5 text-white rounded-2xl text-xs font-bold transition-all cursor-pointer shadow-md hover:shadow-lg hover:-translate-y-0.5"
          style={{
            background: 'linear-gradient(135deg, #2563EB 0%, #3B82F6 100%)',
            boxShadow: '0 4px 16px rgba(37, 99, 235, 0.3)',
          }}
        >
          <Plus className="w-4 h-4 stroke-[2.5]" />
          <span>New Campaign</span>
        </button>

        {/* Notification Bell */}
        <button
          onClick={() => navigate('/notifications')}
          className="relative p-2.5 rounded-2xl bg-white border border-blue-100/80 transition-all cursor-pointer text-slate-500 hover:text-[#2563EB] hover:border-blue-200 shadow-xs hover:shadow-md"
        >
          <Bell className="w-4.5 h-4.5" />
          <span className="absolute -top-1 -right-1 w-4 h-4 flex items-center justify-center rounded-full text-[9px] font-extrabold text-white bg-[#2563EB] shadow-xs">
            3
          </span>
        </button>

        {/* Redesigned Profile Card */}
        <div className="relative" ref={profileRef}>
          <button
            onClick={() => setShowProfileMenu(!showProfileMenu)}
            className="flex items-center space-x-2.5 p-1.5 px-2.5 rounded-2xl bg-white border border-blue-100/80 hover:border-blue-200 cursor-pointer shadow-xs transition-all hover:shadow-sm"
          >
            <div className="relative">
              <div className="w-8 h-8 rounded-xl bg-gradient-to-br from-[#2563EB] to-[#3B82F6] text-white flex items-center justify-center font-black text-xs shadow-xs">
                {initials}
              </div>
              <span className="w-2.5 h-2.5 rounded-full bg-emerald-500 border-2 border-white absolute -bottom-0.5 -right-0.5" />
            </div>
            <div className="text-left hidden lg:block">
              <span className="text-xs font-black block text-[#1E3A8A] leading-tight" style={{ fontFamily: 'var(--font-heading)' }}>
                {userName}
              </span>
              <span className="text-[9px] font-bold text-[#2563EB] block uppercase tracking-wider">
                {currentUser?.role || 'Admin'}
              </span>
            </div>
            <ChevronDown className="w-3.5 h-3.5 text-slate-400 hidden lg:block" />
          </button>

          {showProfileMenu && (
            <div className="absolute right-0 mt-2 w-56 rounded-2xl p-2 z-50 animate-slideInDown bg-white border border-blue-100 shadow-xl space-y-1">
              <div className="px-3 py-2 border-b border-slate-100 mb-1">
                <p className="text-xs font-black truncate text-[#1E3A8A]" style={{ fontFamily: 'var(--font-heading)' }}>
                  {currentUser?.email || 'admin@mailally.com'}
                </p>
                <p className="text-[10px] font-bold text-[#2563EB] uppercase tracking-wider">{currentUser?.role || 'Admin User'}</p>
              </div>
              <button
                onClick={() => { setShowProfileMenu(false); navigate('/settings'); }}
                className="w-full text-left px-3 py-2 text-xs flex items-center space-x-2 font-bold text-slate-700 hover:text-[#2563EB] hover:bg-blue-50 rounded-xl transition-colors"
              >
                <User className="w-4 h-4 text-slate-400" />
                <span>Account Settings</span>
              </button>
              <button
                onClick={logout}
                className="w-full text-left px-3 py-2 text-xs flex items-center space-x-2 font-black text-rose-600 hover:bg-rose-50 rounded-xl transition-colors mt-1"
              >
                <LogOut className="w-4 h-4 text-rose-500" />
                <span>Sign Out</span>
              </button>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};
