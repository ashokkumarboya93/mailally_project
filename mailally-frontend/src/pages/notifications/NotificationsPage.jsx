import React, { useState, useEffect } from 'react';
import { notificationApi } from '../../api/extraApis';
import { StatusBadge } from '../../components/common/StatusBadge';
import { Bell, CheckCheck, Inbox } from 'lucide-react';

export const NotificationsPage = () => {
  const [notifications, setNotifications] = useState([]);

  const loadNotifications = async () => {
    try {
      const res = await notificationApi.getNotifications();
      if (res.data && res.data.content) {
        setNotifications(res.data.content);
      } else {
        setNotifications([
          { id: 1, title: 'Campaign Dispatched', message: 'Summer Clearance Sale dispatched to 12,450 contacts.', type: 'CAMPAIGN_LAUNCHED', priority: 'NORMAL', isRead: false },
          { id: 2, title: 'Invoice Issued', message: 'Invoice INV-202608-0001 has been generated.', type: 'BILLING_INVOICE_GENERATED', priority: 'HIGH', isRead: false }
        ]);
      }
    } catch {
      setNotifications([
        { id: 1, title: 'Campaign Dispatched', message: 'Summer Clearance Sale dispatched to 12,450 contacts.', type: 'CAMPAIGN_LAUNCHED', priority: 'NORMAL', isRead: false }
      ]);
    }
  };

  useEffect(() => {
    loadNotifications();
  }, []);

  const handleMarkAllRead = async () => {
    try {
      await notificationApi.markAllAsRead();
      loadNotifications();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="space-y-8 animate-fadeInUp font-sans max-w-5xl mx-auto pb-12">
      {/* ═══════════════════════════════════════════════ */}
      {/* HERO BANNER (MATCHES EXECUTIVE DASHBOARD)       */}
      {/* ═══════════════════════════════════════════════ */}
      <div 
        className="rounded-[28px] py-5 px-7 lg:py-5 lg:px-8 flex flex-col lg:flex-row items-center justify-between gap-6 relative overflow-hidden text-white border"
        style={{
          background: 'linear-gradient(135deg, #2563EB 0%, #3B82F6 60%, #60A5FA 100%)',
          borderColor: 'rgba(255, 255, 255, 0.9)',
          boxShadow: '0 0 0 1px rgba(15, 23, 42, 0.08), 0 15px 35px -10px rgba(37, 99, 235, 0.18)',
        }}
      >
        {/* Soft Radial Glows */}
        <div className="absolute top-[-80px] left-[-80px] w-96 h-96 rounded-full bg-white/20 blur-3xl pointer-events-none" />
        <div className="absolute bottom-[-80px] right-[-80px] w-96 h-96 rounded-full bg-white/10 blur-3xl pointer-events-none" />

        {/* Left Column Content */}
        <div className="space-y-3.5 max-w-sm relative z-10">
          <div className="inline-flex items-center space-x-2 px-3 py-1 rounded-full bg-white/15 text-white font-black text-[10px] border border-white/25 shadow-3xs backdrop-blur-md">
            <Inbox className="w-3 h-3 text-[#00DDFF]" />
            <span>Event Notification Subsystem</span>
          </div>

          <h1 className="text-2xl sm:text-3xl font-black tracking-tight leading-none text-white" style={{ fontFamily: 'var(--font-heading)' }}>
            Notification <br />
            <span className="text-[#00DDFF]">Center</span>
          </h1>

          <p className="text-[10px] sm:text-[11px] text-blue-50 leading-relaxed font-medium">
            Real-time system events, deliverability alerts, and compliance activity logs.
          </p>

          <div className="pt-1">
            <button
              onClick={handleMarkAllRead}
              className="px-6 py-2.5 rounded-full bg-white hover:bg-blue-50 text-[#2563EB] font-black text-[11px] shadow-md hover:scale-[1.02] transition-all flex items-center space-x-2 cursor-pointer group"
            >
              <CheckCheck className="w-3.5 h-3.5 text-[#2563EB]" />
              <span>Mark All Read</span>
            </button>
          </div>
        </div>

        {/* Center Outreach Mail Icon Graphic (Custom PNG) */}
        <div className="hidden xl:flex items-center justify-center relative z-10 px-2">
          <img 
            src="/envelope_outreach.png" 
            alt="MailAlly Outreach Campaign Icon" 
            className="w-36 h-auto object-contain max-h-32 select-none pointer-events-none drop-shadow-lg filter brightness-105" 
          />
        </div>
      </div>

      {/* Notifications Cards Stream */}
      <div className="space-y-3.5 pt-2">
        {notifications.map((n) => (
          <div
            key={n.id}
            className="bg-white rounded-2xl p-4.5 flex items-start space-x-4 border transition-all duration-200 hover:-translate-y-0.5 hover:shadow-md"
            style={{
              borderColor: n.isRead ? 'rgba(37,99,235,0.08)' : '#2563EB',
              backgroundColor: n.isRead ? '#FFFFFF' : '#F0F7FF',
            }}
          >
            <div className="w-10 h-10 rounded-xl bg-blue-50 text-[#2563EB] flex items-center justify-center flex-shrink-0 font-bold border border-blue-100 shadow-3xs">
              <Bell className="w-4.5 h-4.5" />
            </div>
            <div className="flex-1 space-y-1">
              <div className="flex items-center justify-between">
                <h4 className="text-sm font-black text-[#1E293B]" style={{ fontFamily: 'var(--font-heading)' }}>{n.title}</h4>
                <StatusBadge status={n.priority || 'NORMAL'} />
              </div>
              <p className="text-xs leading-relaxed text-slate-500 font-medium">{n.message}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
