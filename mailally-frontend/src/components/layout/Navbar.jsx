import React, { useState, useRef, useEffect } from 'react';
import { Search, Bell, LogOut, User, Plus, Command, ChevronDown, Menu } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate, useLocation } from 'react-router-dom';

export const Navbar = ({ onMenuClick }) => {
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
    <header className="h-[56px] px-5 lg:px-8 flex items-center justify-between z-20 bg-white border-b border-[#E5E5E7]">
      {/* Left: Mobile Menu + Title */}
      <div className="flex items-center gap-3">
        {/* Mobile menu button */}
        <button
          onClick={onMenuClick}
          className="lg:hidden p-1.5 rounded-lg text-[#0A0A0B] hover:bg-[#F3F4F6] transition-all cursor-pointer flex items-center justify-center"
          title="Toggle Navigation Menu"
        >
          <Menu className="w-5 h-5" />
        </button>

        <div>
          <h1 className="text-[15px] font-bold tracking-tight text-[#0A0A0B]">
            {formattedTitle}
          </h1>
        </div>
      </div>

      {/* Right: Search + Actions */}
      <div className="flex items-center gap-2.5">
        {/* Search */}
        <form onSubmit={handleSearch} className="relative hidden md:block">
          <Search
            className="w-3.5 h-3.5 absolute left-3 top-1/2 -translate-y-1/2 transition-colors"
            style={{ color: searchFocused ? '#0A0A0B' : '#C0C5CC' }}
          />
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Search..."
            className="w-56 lg:w-64 pl-9 pr-16 h-9 rounded-lg text-[13px] font-medium text-[#0A0A0B] bg-[#F9FAFB] border border-[#E5E5E7] outline-none transition-all placeholder:text-[#C0C5CC]"
            style={{
              borderColor: searchFocused ? '#D1D5DB' : '#E5E5E7',
              background: searchFocused ? '#FFFFFF' : '#F9FAFB',
            }}
            onFocus={() => setSearchFocused(true)}
            onBlur={() => setSearchFocused(false)}
          />
          <div className="absolute right-2.5 top-1/2 -translate-y-1/2 flex items-center gap-0.5 px-1.5 py-0.5 rounded-md text-[10px] font-medium bg-[#F3F4F6] text-[#9CA3AF] border border-[#E5E5E7]">
            <Command className="w-2.5 h-2.5" />
            <span>K</span>
          </div>
        </form>

        {/* New Campaign */}
        <button
          onClick={() => navigate('/campaigns')}
          className="hidden sm:flex items-center gap-1.5 px-3.5 h-9 text-white rounded-lg text-[13px] font-semibold transition-all cursor-pointer bg-[#0A0A0B] hover:bg-[#1F1F20] hover:-translate-y-px"
        >
          <Plus className="w-3.5 h-3.5" />
          <span>New Campaign</span>
        </button>

        {/* Notification Bell */}
        <button
          onClick={() => navigate('/notifications')}
          className="relative p-2 rounded-lg text-[#9CA3AF] hover:text-[#0A0A0B] hover:bg-[#F3F4F6] transition-all cursor-pointer"
        >
          <Bell className="w-[18px] h-[18px]" strokeWidth={1.5} />
          <span className="absolute top-1 right-1 w-2 h-2 rounded-full bg-[#EC4899]" />
        </button>

        {/* Profile */}
        <div className="relative" ref={profileRef}>
          <button
            onClick={() => setShowProfileMenu(!showProfileMenu)}
            className="flex items-center gap-2 p-1 pr-2 rounded-lg hover:bg-[#F3F4F6] cursor-pointer transition-all"
          >
            <div className="flex items-center gap-1.5">
              <div className="w-8 h-8 rounded-full bg-[#F4F4F6] border border-[#E4E4E7] shadow-2xs overflow-hidden flex items-center justify-center">
                <svg className="w-full h-full text-[#52525B]" viewBox="0 0 36 36" fill="none">
                  <rect width="36" height="36" rx="18" fill="#F4F4F6" />
                  <circle cx="18" cy="12.5" r="5.5" fill="#52525B" />
                  <path d="M 6.5 32 C 6.5 25.5 11.5 20.5 18 20.5 C 24.5 20.5 29.5 25.5 29.5 32 Z" fill="#52525B" />
                </svg>
              </div>
            </div>
            <ChevronDown className="w-3 h-3 text-[#9CA3AF] hidden lg:block" />
          </button>

          {showProfileMenu && (
            <div className="absolute right-0 mt-1.5 w-52 rounded-xl bg-white border border-[#E5E5E7] shadow-[0_8px_30px_rgba(0,0,0,0.08)] p-1.5 z-50 animate-fadeInDown">
              <div className="px-3 py-2.5 border-b border-[#F0F0F2] mb-1 flex items-center gap-2.5">
                <div className="w-9 h-9 rounded-full bg-[#F4F4F6] border border-[#E4E4E7] flex-shrink-0 flex items-center justify-center overflow-hidden">
                  <svg className="w-full h-full text-[#52525B]" viewBox="0 0 36 36" fill="none">
                    <rect width="36" height="36" rx="18" fill="#F4F4F6" />
                    <circle cx="18" cy="12.5" r="5.5" fill="#52525B" />
                    <path d="M 6.5 32 C 6.5 25.5 11.5 20.5 18 20.5 C 24.5 20.5 29.5 25.5 29.5 32 Z" fill="#52525B" />
                  </svg>
                </div>
                <div className="overflow-hidden min-w-0 flex-1">
                  <p className="text-[12px] font-bold text-[#0A0A0B] truncate">
                    {currentUser?.email || 'admin@mailally.com'}
                  </p>
                  <p className="text-[10px] font-semibold text-[#9CA3AF] uppercase tracking-wider mt-0.5">
                    {currentUser?.role || 'Admin'}
                  </p>
                </div>
              </div>
              <button
                onClick={() => { setShowProfileMenu(false); navigate('/settings'); }}
                className="w-full text-left px-3 py-2 text-[13px] flex items-center gap-2.5 font-medium text-[#5F6368] hover:text-[#0A0A0B] hover:bg-[#F9FAFB] rounded-lg transition-colors cursor-pointer"
              >
                <User className="w-4 h-4 text-[#9CA3AF]" />
                <span>Account Settings</span>
              </button>
              <button
                onClick={logout}
                className="w-full text-left px-3 py-2 text-[13px] flex items-center gap-2.5 font-semibold text-[#E11D48] hover:bg-[#FFE4E6] rounded-lg transition-colors mt-0.5 cursor-pointer"
              >
                <LogOut className="w-4 h-4 text-[#F43F5E]" />
                <span>Sign Out</span>
              </button>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};
