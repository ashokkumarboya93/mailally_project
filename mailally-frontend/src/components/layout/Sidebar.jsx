import React from 'react';
import { NavLink } from 'react-router-dom';
import { 
  LayoutDashboard, Users, Clock, BarChart3, Bell, 
  Settings, CreditCard, Shield, Sparkles, FileText, Send, 
  DollarSign, ChevronLeft, ChevronRight, Globe
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { CometLogo } from '../common/CometLogo';

export const Sidebar = ({ isCollapsed, toggleSidebar }) => {
  const { currentUser } = useAuth();
  const role = currentUser?.role || 'ADMIN';

  const sections = [
    {
      label: 'Showcase',
      items: [
        { name: 'SaaS Showcase', path: '/landing', icon: Globe },
      ]
    },
    {
      label: 'Core Platform',
      items: [
        { name: 'Dashboard', path: '/dashboard', icon: LayoutDashboard },
        { name: 'Campaigns', path: '/campaigns', icon: Send },
        { name: 'Contacts', path: '/contacts', icon: Users },
        { name: 'Templates', path: '/templates', icon: FileText },
      ]
    },
    {
      label: 'Operations',
      items: [
        { name: 'Scheduler', path: '/scheduler', icon: Clock },
        { name: 'Analytics', path: '/analytics', icon: BarChart3 },
        { name: 'Notifications', path: '/notifications', icon: Bell },
        { name: 'AI Assistant', path: '/ai', icon: Sparkles },
      ]
    },
    {
      label: 'Management',
      items: [
        { name: 'Billing', path: '/billing', icon: DollarSign, roles: ['ADMIN', 'MANAGER'] },
        { name: 'Subscriptions', path: '/subscriptions', icon: CreditCard, roles: ['ADMIN'] },
        { name: 'Settings', path: '/settings', icon: Settings },
        { name: 'Audit Logs', path: '/audit', icon: Shield, roles: ['ADMIN'] },
      ]
    }
  ];

  return (
    <aside
      className={`flex flex-col z-30 transition-all duration-300 ease-[cubic-bezier(0.16,1,0.3,1)] bg-white border-r border-slate-200/90 ${isCollapsed ? 'w-[72px]' : 'w-[250px]'}`}
    >
      {/* Brand Header */}
      <div className={`h-[64px] flex items-center ${isCollapsed ? 'justify-center' : 'justify-between px-4'} border-b border-slate-100`}>
        {!isCollapsed ? (
          <>
            <div className="flex items-center space-x-2.5 overflow-hidden">
              <CometLogo size="sm" />
              <div className="overflow-hidden">
                <span
                  className="font-black text-xl tracking-tight block text-[#1E3A8A]"
                  style={{ fontFamily: "var(--font-heading)" }}
                >
                  MailAlly<span className="text-[#1F57F5]">.</span>
                </span>
                <span className="text-[9px] font-black uppercase tracking-[1.8px] block -mt-1 text-[#2BAFF2]">
                  ENTERPRISE SAAS
                </span>
              </div>
            </div>
            <button
              onClick={toggleSidebar}
              className="p-1.5 rounded-lg border border-slate-100 bg-white hover:bg-slate-50 shadow-3xs transition-all cursor-pointer text-slate-400 hover:text-[#1F57F5] hidden lg:block"
              title="Collapse Sidebar"
            >
              <ChevronLeft className="w-4 h-4" />
            </button>
          </>
        ) : (
          <button
            onClick={toggleSidebar}
            className="p-2 rounded-xl border border-slate-200 bg-white hover:bg-slate-50 shadow-3xs transition-all cursor-pointer text-slate-400 hover:text-[#1F57F5]"
            title="Expand Sidebar"
          >
            <ChevronRight className="w-5 h-5" />
          </button>
        )}
      </div>

      {/* Nav Menu */}
      <div className="flex-1 overflow-y-auto py-3 px-3 space-y-1">
        {sections.map((section, sIdx) => {
          const filteredItems = section.items.filter(item => !item.roles || item.roles.includes(role));
          if (filteredItems.length === 0) return null;

          return (
            <div key={section.label}>
              {!isCollapsed && (
                <div className="px-3 pt-4 pb-1 mt-4 first:mt-1">
                  <span className="text-[9px] font-black uppercase tracking-[2px] text-slate-400/80">
                    {section.label}
                  </span>
                </div>
              )}
              {isCollapsed && sIdx > 0 && (
                <div className="my-3 mx-4 h-px bg-slate-100" />
              )}

              <div className="space-y-0.5">
                {filteredItems.map((item) => {
                  const Icon = item.icon;
                  return (
                    <NavLink
                      key={item.path}
                      to={item.path}
                      title={isCollapsed ? item.name : undefined}
                      className={({ isActive }) =>
                        `group flex items-center ${isCollapsed ? 'justify-center px-0 py-2.5' : 'px-3.5 py-2.5'} rounded-xl font-bold text-xs transition-all relative ${
                          isActive
                            ? 'bg-blue-50/70 text-[#1F57F5] border border-blue-100/60 shadow-3xs before:absolute before:left-0 before:top-[20%] before:bottom-[20%] before:w-[3.5px] before:bg-gradient-to-b before:from-[#1F57F5] before:to-[#2BAFF2] before:rounded-r-md'
                            : 'text-slate-500 hover:text-[#1F57F5] hover:bg-slate-50/60'
                        }`
                      }
                    >
                      {({ isActive }) => (
                        <>
                          <Icon className={`w-4 h-4 flex-shrink-0 ${isCollapsed ? '' : 'mr-3'} transition-all duration-300 group-hover:scale-110 ${isActive ? 'text-[#1F57F5]' : 'text-slate-400 group-hover:text-[#1F57F5]'}`} />
                          {!isCollapsed && <span className="truncate">{item.name}</span>}
                        </>
                      )}
                    </NavLink>
                  );
                })}
              </div>
            </div>
          );
        })}
      </div>

      {/* User Footer */}
      <div className={`p-3 ${isCollapsed ? 'flex justify-center border-t border-slate-100 bg-slate-50/30' : ''}`}>
        {!isCollapsed ? (
          <div className="flex items-center space-x-3 p-2.5 bg-slate-50/80 border border-slate-200/60 rounded-2xl shadow-3xs overflow-hidden w-full">
            <div className="w-8 h-8 rounded-xl bg-gradient-to-br from-[#1F57F5] to-[#2BAFF2] text-white flex items-center justify-center font-bold text-sm shadow-sm relative group flex-shrink-0">
              {currentUser?.email ? currentUser.email.charAt(0).toUpperCase() : 'A'}
              <span className="absolute -bottom-0.5 -right-0.5 w-2.5 h-2.5 bg-emerald-500 border-2 border-white rounded-full"></span>
            </div>
            <div className="overflow-hidden flex-1">
              <p className="text-xs font-bold truncate text-[#1E3A8A] font-sans" title={currentUser?.email || 'Admin User'}>
                {currentUser?.email || 'Admin User'}
              </p>
              <span className="text-[9px] font-black uppercase tracking-wider text-[#1F57F5]">
                {role}
              </span>
            </div>
          </div>
        ) : (
          <div className="w-8 h-8 rounded-xl bg-gradient-to-br from-[#1F57F5] to-[#2BAFF2] text-white flex items-center justify-center font-bold text-sm shadow-sm relative flex-shrink-0">
            {currentUser?.email ? currentUser.email.charAt(0).toUpperCase() : 'A'}
            <span className="absolute -bottom-0.5 -right-0.5 w-2.5 h-2.5 bg-emerald-500 border-2 border-white rounded-full"></span>
          </div>
        )}
      </div>
    </aside>
  );
};
