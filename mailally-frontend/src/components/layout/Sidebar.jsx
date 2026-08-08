import React from 'react';
import { NavLink } from 'react-router-dom';
import {
  LayoutDashboard, Users, Clock, BarChart3, Bell,
  Settings, CreditCard, Shield, Sparkles, FileText, Send,
  DollarSign, PanelLeftClose, PanelLeftOpen, Globe, X
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import { CometLogo } from '../common/CometLogo';

export const Sidebar = ({ isCollapsed, toggleSidebar, isMobileOpen, closeMobile }) => {
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
        { name: 'Contacts', path: '/contacts', icon: Users },
        { name: 'Campaigns', path: '/campaigns', icon: Send },
        { name: 'Templates', path: '/templates', icon: FileText },
        { name: 'Analytics', path: '/analytics', icon: BarChart3 },
        { name: 'Scheduler', path: '/scheduler', icon: Clock },
      ]
    },
    {
      label: 'Intelligence',
      items: [
        { name: 'AI Assistant', path: '/ai-assistant', icon: Sparkles },
        { name: 'Notifications', path: '/notifications', icon: Bell },
        { name: 'Users & Roles', path: '/users', icon: Shield, roles: ['ADMIN'] },
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

  const sidebarContent = (
    <>
      {/* Brand Header */}
      <div className={`h-[64px] flex items-center ${isCollapsed ? 'justify-center' : 'justify-between px-4'} border-b border-[#E5E5E7]`}>
        {!isCollapsed ? (
          <>
            <div className="flex items-center gap-2.5 overflow-hidden">
              <CometLogo size="md" />
              <span className="font-extrabold text-[17px] tracking-tight block text-[#0A0A0B]">
                MailAlly
              </span>
            </div>
            <button
              onClick={toggleSidebar}
              className="p-1.5 rounded-lg text-[#0A0A0B] hover:bg-[#F3F4F6] transition-all cursor-pointer hidden lg:flex items-center justify-center"
              title="Collapse Sidebar"
            >
              <PanelLeftClose className="w-4 h-4" />
            </button>
          </>
        ) : (
          <button
            onClick={toggleSidebar}
            className="p-1.5 rounded-lg text-[#0A0A0B] hover:bg-[#F3F4F6] transition-all cursor-pointer flex items-center justify-center"
            title="Expand Sidebar"
          >
            <PanelLeftOpen className="w-4 h-4" />
          </button>
        )}
      </div>

      {/* Navigation */}
      <nav className="flex-1 overflow-y-auto py-3 px-2.5">
        {sections.map((section, sIdx) => {
          const filteredItems = section.items.filter(item => !item.roles || item.roles.includes(role));
          if (filteredItems.length === 0) return null;

          return (
            <div key={section.label} className={sIdx > 0 ? 'mt-5' : ''}>
              {!isCollapsed && (
                <div className="px-2.5 mb-1.5">
                  <span className="text-[10px] font-semibold uppercase tracking-[0.08em] text-[#C0C5CC]">
                    {section.label}
                  </span>
                </div>
              )}
              {isCollapsed && sIdx > 0 && (
                <div className="my-2 mx-3 h-px bg-[#E5E5E7]" />
              )}

              <div className="space-y-0.5">
                {filteredItems.map((item) => {
                  const Icon = item.icon;
                  return (
                    <NavLink
                      key={item.path}
                      to={item.path}
                      title={isCollapsed ? item.name : undefined}
                      onClick={closeMobile}
                      className={({ isActive }) =>
                        `group flex items-center ${isCollapsed ? 'justify-center px-0 py-2' : 'px-2.5 py-[7px]'} rounded-lg text-[13px] font-medium transition-all relative ${
                          isActive
                            ? 'bg-[#FCE7F3] text-[#0A0A0B] before:absolute before:left-0 before:top-[25%] before:bottom-[25%] before:w-[3px] before:bg-[#EC4899] before:rounded-r-full'
                            : 'text-[#5F6368] hover:text-[#0A0A0B] hover:bg-[#F9FAFB]'
                        }`
                      }
                    >
                      {({ isActive }) => (
                        <>
                          <Icon
                            className={`w-[18px] h-[18px] flex-shrink-0 ${isCollapsed ? '' : 'mr-2.5'} transition-colors ${
                              isActive ? 'text-[#EC4899]' : 'text-[#9CA3AF] group-hover:text-[#5F6368]'
                            }`}
                            strokeWidth={isActive ? 2 : 1.5}
                          />
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
      </nav>

    </>
  );

  return (
    <>
      {/* Desktop Sidebar */}
      <aside
        className={`hidden lg:flex flex-col z-30 transition-all duration-300 ease-[cubic-bezier(0.16,1,0.3,1)] bg-white border-r border-[#E5E5E7] ${
          isCollapsed ? 'w-[64px]' : 'w-[240px]'
        }`}
      >
        {sidebarContent}
      </aside>

      {/* Mobile Sidebar Drawer */}
      <aside
        className={`lg:hidden fixed inset-y-0 left-0 z-50 w-[260px] bg-white border-r border-[#E5E5E7] flex flex-col transition-transform duration-300 ease-[cubic-bezier(0.16,1,0.3,1)] ${
          isMobileOpen ? 'translate-x-0' : '-translate-x-full'
        }`}
      >
        {/* Mobile Close */}
        <button
          onClick={closeMobile}
          className="absolute top-3 right-3 p-1.5 rounded-md text-[#9CA3AF] hover:text-[#0A0A0B] hover:bg-[#F3F4F6] transition-all cursor-pointer z-10"
        >
          <X className="w-4 h-4" />
        </button>
        {sidebarContent}
      </aside>
    </>
  );
};
