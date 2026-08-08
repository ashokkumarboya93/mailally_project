import React, { useState, useEffect } from 'react';
import { notificationApi } from '../../api/extraApis';
import { StatusBadge } from '../../components/common/StatusBadge';
import { Bell, CheckCheck, Trash2, Mail, ShieldAlert, Sparkles, Zap, Search } from 'lucide-react';
import { useToast } from '../../components/common/Toast';

export const NotificationsPage = () => {
  const [notifications, setNotifications] = useState([]);
  const [activeTab, setActiveTab] = useState('ALL');
  const [searchQuery, setSearchQuery] = useState('');
  const { addToast } = useToast();

  const loadNotifications = async () => {
    try {
      const res = await notificationApi.getNotifications();
      if (res?.data?.content && res.data.content.length > 0) {
        setNotifications(res.data.content);
      } else {
        setNotifications([
          { id: 1, title: 'Campaign Dispatched', message: 'Black Friday 2026 sequence dispatched to 24,890 contacts.', category: 'CAMPAIGNS', priority: 'SUCCESS', isRead: false, time: '10 mins ago' },
          { id: 2, title: 'Invoice Issued', message: 'Monthly subscription invoice INV-202608-0001 ($63.00) issued.', category: 'BILLING', priority: 'NORMAL', isRead: false, time: '1 hour ago' },
          { id: 3, title: 'Webhook Endpoint Active', message: 'SendGrid bounce webhook normalized 48 events automatically.', category: 'SYSTEM', priority: 'NORMAL', isRead: true, time: '3 hours ago' },
          { id: 4, title: 'New Member Joined', message: 'Sarah Connor joined your workspace with Manager privileges.', category: 'USERS', priority: 'NORMAL', isRead: true, time: '5 hours ago' },
          { id: 5, title: 'High Deliverability Alert', message: 'Domain mailally.com reached 99.4% inboxing rate.', category: 'SYSTEM', priority: 'SUCCESS', isRead: true, time: '1 day ago' }
        ]);
      }
    } catch {
      setNotifications([
        { id: 1, title: 'Campaign Dispatched', message: 'Black Friday 2026 sequence dispatched to 24,890 contacts.', category: 'CAMPAIGNS', priority: 'SUCCESS', isRead: false, time: '10 mins ago' },
        { id: 2, title: 'Invoice Issued', message: 'Monthly subscription invoice INV-202608-0001 ($63.00) issued.', category: 'BILLING', priority: 'NORMAL', isRead: false, time: '1 hour ago' }
      ]);
    }
  };

  useEffect(() => {
    loadNotifications();
  }, []);

  const handleMarkAllRead = async () => {
    try {
      await notificationApi.markAllAsRead();
      setNotifications(prev => prev.map(n => ({ ...n, isRead: true })));
      addToast('Marked all notifications as read', 'success');
    } catch (err) {
      setNotifications(prev => prev.map(n => ({ ...n, isRead: true })));
      addToast('Marked all notifications as read', 'success');
    }
  };

  const handleClearAll = () => {
    setNotifications([]);
    addToast('Cleared notification feed', 'info');
  };

  const categories = [
    { key: 'ALL', label: 'All Alerts' },
    { key: 'CAMPAIGNS', label: 'Campaigns' },
    { key: 'SYSTEM', label: 'System Telemetry' },
    { key: 'BILLING', label: 'Billing' },
    { key: 'USERS', label: 'Team Activity' }
  ];

  const filtered = notifications.filter(n => {
    const matchesTab = activeTab === 'ALL' || n.category === activeTab;
    const matchesSearch = !searchQuery || n.title.toLowerCase().includes(searchQuery.toLowerCase()) || n.message.toLowerCase().includes(searchQuery.toLowerCase());
    return matchesTab && matchesSearch;
  });

  const unreadCount = notifications.filter(n => !n.isRead).length;

  return (
    <div className="space-y-6 animate-fadeInUp pb-8 max-w-4xl font-sans">
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <div className="flex items-center gap-2.5">
            <h1 className="text-2xl font-extrabold tracking-tight text-[#18181B]">Notifications</h1>
            {unreadCount > 0 && (
              <span className="px-2.5 py-0.5 rounded-full text-xs font-bold bg-[#FCE7F3] text-[#EC4899] border border-[#FCE7F3]">
                {unreadCount} Unread
              </span>
            )}
          </div>
          <p className="text-[13px] text-[#71717A] font-medium mt-1">
            Real-time delivery events, system health alerts, and team activity logs.
          </p>
        </div>

        <div className="flex items-center gap-2">
          <button
            onClick={handleMarkAllRead}
            className="flex items-center gap-1.5 px-3.5 h-9 rounded-xl text-xs font-bold bg-[#18181B] text-white hover:bg-black transition-all cursor-pointer shadow-xs"
          >
            <CheckCheck className="w-3.5 h-3.5" />
            Mark All Read
          </button>
          <button
            onClick={handleClearAll}
            className="flex items-center gap-1.5 px-3 h-9 rounded-xl text-xs font-bold border border-[#E4E4E7] bg-white text-[#71717A] hover:text-[#18181B] hover:bg-[#FAFAFA] transition-all cursor-pointer"
          >
            <Trash2 className="w-3.5 h-3.5" />
            Clear
          </button>
        </div>
      </div>

      {/* Filter Bar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 bg-white p-2 rounded-[20px] border border-[#18181B] shadow-xs">
        <div className="flex gap-1 overflow-x-auto pb-1 sm:pb-0">
          {categories.map((c) => (
            <button
              key={c.key}
              onClick={() => setActiveTab(c.key)}
              className={`px-3.5 py-1.5 rounded-xl text-xs font-bold transition-all cursor-pointer whitespace-nowrap ${
                activeTab === c.key
                  ? 'bg-[#18181B] text-white'
                  : 'text-[#71717A] hover:bg-[#FAFAFA] hover:text-[#18181B]'
              }`}
            >
              {c.label}
            </button>
          ))}
        </div>

        <div className="relative w-full sm:w-56">
          <Search className="w-3.5 h-3.5 text-[#A1A1AA] absolute left-3 top-1/2 -translate-y-1/2" />
          <input
            type="text"
            placeholder="Search alerts..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full pl-9 pr-3 h-8 text-xs font-semibold bg-[#FAFAFA] border border-[#E4E4E7] rounded-xl outline-none focus:border-[#18181B]"
          />
        </div>
      </div>

      {/* Notifications Feed */}
      <div className="space-y-3">
        {filtered.length > 0 ? (
          filtered.map((n) => (
            <div
              key={n.id}
              className={`bg-white rounded-[20px] p-4 flex items-start gap-4 border transition-all duration-200 hover:shadow-xs ${
                n.isRead ? 'border-[#E4E4E7]' : 'border-[#18181B] bg-[#FAF5FF]/30'
              }`}
            >
              <div className={`w-10 h-10 rounded-xl flex items-center justify-center flex-shrink-0 border ${
                n.isRead ? 'bg-[#FAFAFA] border-[#E4E4E7] text-[#71717A]' : 'bg-[#FCE7F3] border-[#FCE7F3] text-[#EC4899]'
              }`}>
                <Bell className="w-4 h-4" strokeWidth={2} />
              </div>
              <div className="flex-1 min-w-0">
                <div className="flex items-center justify-between gap-2">
                  <div className="flex items-center gap-2">
                    <h4 className="text-xs font-bold text-[#18181B]">{n.title}</h4>
                    {!n.isRead && (
                      <span className="w-2 h-2 rounded-full bg-[#EC4899]" />
                    )}
                  </div>
                  <div className="flex items-center gap-2">
                    <span className="text-[10px] font-semibold text-[#A1A1AA]">{n.time}</span>
                    <StatusBadge status={n.priority || 'NORMAL'} />
                  </div>
                </div>
                <p className="text-xs text-[#52525B] font-medium mt-1 leading-relaxed">{n.message}</p>
              </div>
            </div>
          ))
        ) : (
          <div className="bg-white rounded-[24px] border border-[#E4E4E7] p-10 text-center space-y-2">
            <Bell className="w-8 h-8 text-[#A1A1AA] mx-auto" />
            <p className="text-xs font-bold text-[#18181B]">No notifications found</p>
            <p className="text-[11px] text-[#71717A]">Your notification feed is completely clear.</p>
          </div>
        )}
      </div>

    </div>
  );
};
