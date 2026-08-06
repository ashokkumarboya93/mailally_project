import React, { useState } from 'react';
import { Sidebar } from './Sidebar';
import { Navbar } from './Navbar';
import { FloatingAiDrawer } from '../common/FloatingAiDrawer';
import { Outlet } from 'react-router-dom';

export const MainLayout = () => {
  const [isCollapsed, setIsCollapsed] = useState(false);

  return (
    <div className="flex h-screen overflow-hidden" style={{ backgroundColor: 'var(--claude-bg)' }}>
      <Sidebar isCollapsed={isCollapsed} toggleSidebar={() => setIsCollapsed(!isCollapsed)} />
      <div className="flex-1 flex flex-col min-w-0 overflow-hidden">
        <Navbar />
        <main className="flex-1 overflow-y-auto p-6 lg:p-8">
          <div className="max-w-7xl mx-auto space-y-6 animate-fadeInUp">
            <Outlet />
          </div>
        </main>
      </div>
      <FloatingAiDrawer />
    </div>
  );
};
