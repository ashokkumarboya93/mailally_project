import React, { useState } from 'react';
import { Sidebar } from './Sidebar';
import { Navbar } from './Navbar';
import { FloatingAiDrawer } from '../common/FloatingAiDrawer';
import { Outlet } from 'react-router-dom';

export const MainLayout = () => {
  const [isCollapsed, setIsCollapsed] = useState(false);
  const [isMobileOpen, setIsMobileOpen] = useState(false);

  return (
    <div className="flex h-screen overflow-hidden" style={{ backgroundColor: 'var(--ma-bg)' }}>
      {/* Mobile Overlay */}
      {isMobileOpen && (
        <div
          className="fixed inset-0 bg-black/30 z-40 lg:hidden"
          onClick={() => setIsMobileOpen(false)}
        />
      )}

      {/* Sidebar */}
      <Sidebar
        isCollapsed={isCollapsed}
        toggleSidebar={() => setIsCollapsed(!isCollapsed)}
        isMobileOpen={isMobileOpen}
        closeMobile={() => setIsMobileOpen(false)}
      />

      {/* Main Content Area */}
      <div className="flex-1 flex flex-col min-w-0 overflow-hidden">
        <Navbar onMenuClick={() => setIsMobileOpen(true)} />
        <main className="flex-1 overflow-y-auto">
          <div className="max-w-[1360px] mx-auto px-5 lg:px-8 py-6 lg:py-8 space-y-6 animate-fadeInUp">
            <Outlet />
          </div>
        </main>
      </div>

      <FloatingAiDrawer />
    </div>
  );
};
