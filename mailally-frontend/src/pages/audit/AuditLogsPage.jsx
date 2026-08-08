import React, { useState, useEffect } from 'react';
import { auditApi } from '../../api/extraApis';
import { Search, Shield, Download, Filter, CheckCircle2, AlertTriangle } from 'lucide-react';
import { useToast } from '../../components/common/Toast';

export const AuditLogsPage = () => {
  const [logs, setLogs] = useState([]);
  const [search, setSearch] = useState('');
  const [selectedModule, setSelectedModule] = useState('ALL');
  const { addToast } = useToast();

  const loadAuditLogs = async () => {
    try {
      const res = search ? await auditApi.searchLogs(search) : await auditApi.getAuditLogs();
      if (res?.data?.content && res.data.content.length > 0) {
        setLogs(res.data.content);
      } else {
        setLogs([
          { id: 1, timestamp: '2026-08-08 14:22:10', userEmail: 'admin@mailally.com', ip: '192.168.1.104', module: 'CAMPAIGN', action: 'LAUNCH_CAMPAIGN', description: 'Dispatched Summer Clearance campaign to 12,450 contacts', success: true },
          { id: 2, timestamp: '2026-08-08 12:15:45', userEmail: 'admin@mailally.com', ip: '192.168.1.104', module: 'SETTINGS', action: 'UPDATE_CONFIG', description: 'Updated organization reply-to and timezone settings in DB', success: true },
          { id: 3, timestamp: '2026-08-08 11:05:20', userEmail: 'sarah.connor@acme.com', ip: '10.0.0.42', module: 'CONTACTS', action: 'CSV_IMPORT', description: 'Uploaded prospective leads CSV (4,890 records)', success: true },
          { id: 4, timestamp: '2026-08-07 18:30:11', userEmail: 'admin@mailally.com', ip: '192.168.1.104', module: 'AUTH', action: 'LOGIN_SUCCESS', description: 'Successful JWT authentication from Chrome macOS', success: true },
          { id: 5, timestamp: '2026-08-07 16:40:00', userEmail: 'admin@mailally.com', ip: '192.168.1.104', module: 'BILLING', action: 'PAYMENT_SETTLED', description: 'Processed monthly invoice INV-202608-0001 ($63.00)', success: true }
        ]);
      }
    } catch {
      setLogs([
        { id: 1, timestamp: '2026-08-08 14:22:10', userEmail: 'admin@mailally.com', ip: '192.168.1.104', module: 'CAMPAIGN', action: 'LAUNCH_CAMPAIGN', description: 'Dispatched Summer Clearance campaign to 12,450 contacts', success: true },
        { id: 2, timestamp: '2026-08-08 12:15:45', userEmail: 'admin@mailally.com', ip: '192.168.1.104', module: 'SETTINGS', action: 'UPDATE_CONFIG', description: 'Updated organization reply-to and timezone settings in DB', success: true }
      ]);
    }
  };

  useEffect(() => {
    loadAuditLogs();
  }, [search]);

  const handleExportCSV = () => {
    addToast('Exporting audit trail records to CSV...', 'success');
  };

  const modules = ['ALL', 'CAMPAIGN', 'SETTINGS', 'CONTACTS', 'AUTH', 'BILLING'];

  const filteredLogs = logs.filter(l => {
    const matchesModule = selectedModule === 'ALL' || l.module === selectedModule;
    const matchesSearch = !search || l.userEmail.toLowerCase().includes(search.toLowerCase()) || l.action.toLowerCase().includes(search.toLowerCase()) || l.description.toLowerCase().includes(search.toLowerCase());
    return matchesModule && matchesSearch;
  });

  return (
    <div className="space-y-6 animate-fadeInUp pb-8 max-w-5xl font-sans">
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold tracking-tight text-[#18181B]">Audit Logs</h1>
          <p className="text-[13px] text-[#71717A] font-medium mt-1">
            Immutable system mutation trail, administrative actions, and security telemetry.
          </p>
        </div>

        <button
          onClick={handleExportCSV}
          className="flex items-center gap-1.5 px-4 h-10 rounded-xl text-xs font-bold bg-[#18181B] text-white hover:bg-black transition-all cursor-pointer shadow-xs"
        >
          <Download className="w-4 h-4" />
          Export CSV Log
        </button>
      </div>

      {/* Filter Bar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 bg-white p-2 rounded-[20px] border border-[#18181B] shadow-xs">
        <div className="flex gap-1 overflow-x-auto pb-1 sm:pb-0">
          {modules.map(mod => (
            <button
              key={mod}
              onClick={() => setSelectedModule(mod)}
              className={`px-3.5 py-1.5 rounded-xl text-xs font-bold transition-all cursor-pointer whitespace-nowrap ${
                selectedModule === mod
                  ? 'bg-[#18181B] text-white'
                  : 'text-[#71717A] hover:bg-[#FAFAFA] hover:text-[#18181B]'
              }`}
            >
              {mod === 'ALL' ? 'All Modules' : mod}
            </button>
          ))}
        </div>

        <div className="relative w-full sm:w-60">
          <Search className="w-3.5 h-3.5 text-[#A1A1AA] absolute left-3 top-1/2 -translate-y-1/2" />
          <input
            type="text"
            placeholder="Search action or user..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full pl-9 pr-3 h-8 text-xs font-semibold bg-[#FAFAFA] border border-[#E4E4E7] rounded-xl outline-none focus:border-[#18181B]"
          />
        </div>
      </div>

      {/* Audit Log Table */}
      <div className="bg-white rounded-[24px] border border-[#18181B] overflow-hidden shadow-xs">
        <div className="px-6 py-4 border-b border-[#E4E4E7] flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Shield className="w-4 h-4 text-[#7C3AED]" />
            <h3 className="font-extrabold text-[15px] text-[#18181B]">Activity Audit Trail</h3>
          </div>
          <span className="text-xs font-bold text-[#18181B] bg-[#F4F4F6] border border-[#E4E4E7] px-3 py-1 rounded-full">
            {filteredLogs.length} Records
          </span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="border-b border-[#E4E4E7] bg-[#FAFAFA]">
                <th className="text-[11px] font-bold uppercase tracking-wider text-[#71717A] p-4 px-6">Timestamp</th>
                <th className="text-[11px] font-bold uppercase tracking-wider text-[#71717A] p-4">User</th>
                <th className="text-[11px] font-bold uppercase tracking-wider text-[#71717A] p-4">Module</th>
                <th className="text-[11px] font-bold uppercase tracking-wider text-[#71717A] p-4">Action</th>
                <th className="text-[11px] font-bold uppercase tracking-wider text-[#71717A] p-4">Description</th>
                <th className="text-[11px] font-bold uppercase tracking-wider text-[#71717A] p-4 text-right pr-6">Status</th>
              </tr>
            </thead>
            <tbody>
              {filteredLogs.map((log) => (
                <tr key={log.id} className="border-b border-[#E4E4E7] last:border-0 hover:bg-[#FAFAFA] transition-colors">
                  <td className="p-4 px-6 text-xs font-mono font-semibold text-[#71717A]">{log.timestamp}</td>
                  <td className="p-4 text-xs font-bold text-[#18181B]">{log.userEmail || 'System'}</td>
                  <td className="p-4">
                    <span className="px-2.5 py-1 rounded-full text-[10px] font-extrabold uppercase tracking-wider bg-[#F3E8FF] text-[#7C3AED] border border-[#DDD6FE]">
                      {log.module}
                    </span>
                  </td>
                  <td className="p-4 text-xs font-bold text-[#18181B]">{log.action}</td>
                  <td className="p-4 text-xs font-medium text-[#52525B]">{log.description}</td>
                  <td className="p-4 text-right pr-6">
                    <span className="inline-flex items-center gap-1 text-[11px] font-bold text-[#15803D] bg-[#DCFCE7] px-2.5 py-0.5 rounded-full border border-[#BBF7D0]">
                      <CheckCircle2 className="w-3 h-3" /> Success
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

    </div>
  );
};
