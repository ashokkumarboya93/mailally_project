import React, { useEffect, useState } from 'react';
import { campaignApi } from '../../api/campaignApi';
import { Play, Pause, XCircle, Activity, CheckCircle, AlertTriangle, ShieldCheck, Cpu, X, FileText, RefreshCw, AlertOctagon } from 'lucide-react';

export const LiveSendingDashboard = ({ campaignId, onClose, onFinished }) => {
  const [progress, setProgress] = useState({
    status: 'RUNNING',
    progressPercentage: 0,
    totalRecipients: 0,
    queuedCount: 0,
    sendingCount: 0,
    sentCount: 0,
    deliveredCount: 0,
    failedCount: 0,
    authFailures: 0,
    connectionFailures: 0,
    invalidRecipientFailures: 0,
    templateFailures: 0,
    providerErrors: 0,
    retryCount: 0,
    emailsPerMinute: 0,
    remainingSeconds: 0,
    activeProvider: 'SMTP (Gmail Enterprise)',
    workers: [
      { workerId: 'Worker-1', status: 'Initializing', processedCount: 0 },
      { workerId: 'Worker-2', status: 'Initializing', processedCount: 0 }
    ],
    recentActivity: [
      'Campaign execution stream starting...'
    ]
  });

  const [showFailureModal, setShowFailureModal] = useState(false);
  const [failureDetails, setFailureDetails] = useState([]);
  const [loadingFailures, setLoadingFailures] = useState(false);

  const fetchProgress = async () => {
    try {
      const res = await campaignApi.getLiveProgress(campaignId);
      if (res.data) {
        setProgress(res.data);
      }
    } catch (e) {
      console.error('Telemetry stream poll failed:', e);
    }
  };

  const fetchFailures = async () => {
    setLoadingFailures(true);
    try {
      const res = await campaignApi.getCampaignFailures(campaignId);
      if (res.data) {
        setFailureDetails(res.data);
      }
    } catch (e) {
      console.error('Failed to load failure details:', e);
    } finally {
      setLoadingFailures(false);
    }
  };

  const handleOpenFailures = () => {
    setShowFailureModal(true);
    fetchFailures();
  };

  useEffect(() => {
    fetchProgress();

    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const wsUrl = `${protocol}//localhost:8081/ws-connect/websocket`;
    let ws = null;
    let fallbackInterval = null;

    try {
      ws = new WebSocket(wsUrl);

      ws.onopen = () => {
        ws.send("CONNECT\naccept-version:1.1,1.0\nheart-beat:10000,10000\n\n\u0000");
      };

      ws.onmessage = (event) => {
        const msg = event.data;
        if (msg.startsWith("CONNECTED")) {
          ws.send(`SUBSCRIBE\nid:sub-0\ndestination:/topic/campaigns/${campaignId}/progress\n\n\u0000`);
        } else if (msg.includes("MESSAGE")) {
          try {
            const bodyStart = msg.indexOf("\n\n");
            if (bodyStart !== -1) {
              const body = msg.substring(bodyStart + 2).replace(/\u0000/g, "").trim();
              const data = JSON.parse(body);
              if (data) {
                setProgress(prev => ({
                  ...prev,
                  status: data.status,
                  sentCount: data.sent,
                  failedCount: data.failed,
                  totalRecipients: data.total,
                  progressPercentage: data.total > 0 ? Math.min(100, Math.ceil(((data.sent + data.failed) / data.total) * 100)) : 0
                }));
              }
            }
          } catch (e) {
            console.error("Failed to parse WebSocket progress payload:", e);
          }
        }
      };

      ws.onerror = (err) => {
        console.warn("WebSocket error, falling back to HTTP polling:", err);
        fallbackInterval = setInterval(fetchProgress, 2000);
      };

      ws.onclose = () => {
        if (!fallbackInterval) {
          fallbackInterval = setInterval(fetchProgress, 2000);
        }
      };
    } catch (e) {
      console.warn("WebSocket connection failed, falling back to HTTP polling:", e);
      fallbackInterval = setInterval(fetchProgress, 2000);
    }

    return () => {
      if (ws) ws.close();
      if (fallbackInterval) clearInterval(fallbackInterval);
    };
  }, [campaignId]);

  const handleControl = async (action) => {
    try {
      await campaignApi.controlCampaign(campaignId, action);
      fetchProgress();
      if (action === 'CANCEL') {
        if (onFinished) onFinished();
        if (onClose) onClose();
      }
    } catch (e) {
      alert('Control action failed: ' + (e.response?.data?.message || e.message));
    }
  };

  return (
    <div className="bg-white border-2 border-blue-200 rounded-3xl p-6 space-y-6 shadow-xl animate-fadeIn relative">
      
      {/* Header & Status Control Toolbar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-4 border-b border-slate-100">
        <div>
          <div className="flex items-center gap-3">
            <span className="relative flex h-3 w-3">
              <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
              <span className="relative inline-flex rounded-full h-3 w-3 bg-emerald-500"></span>
            </span>
            <h2 className="text-xl font-black text-slate-900">Live Campaign Telemetry Dashboard</h2>
            <span className="text-xs px-3 py-0.5 rounded-full bg-emerald-50 text-emerald-700 font-bold border border-emerald-200">
              {progress.status}
            </span>
          </div>
          <p className="text-xs text-slate-500 mt-1 font-medium">Multi-threaded parallel batch execution • Provider: {progress.activeProvider}</p>
        </div>

        {/* Live Controls & Action Buttons */}
        <div className="flex items-center gap-2">
          {progress.failedCount > 0 && (
            <button
              onClick={handleOpenFailures}
              className="px-4 py-2 bg-rose-600 hover:bg-rose-700 text-white font-bold text-xs rounded-xl flex items-center gap-1.5 transition-colors shadow-sm animate-pulse"
            >
              <AlertOctagon className="w-3.5 h-3.5" /> View Failure Details ({progress.failedCount})
            </button>
          )}

          {progress.status === 'PAUSED' ? (
            <button
              onClick={() => handleControl('RESUME')}
              className="px-4 py-2 bg-emerald-600 hover:bg-emerald-700 text-white font-bold text-xs rounded-xl flex items-center gap-1.5 transition-colors shadow-sm"
            >
              <Play className="w-3.5 h-3.5" /> Resume
            </button>
          ) : (
            <button
              onClick={() => handleControl('PAUSE')}
              className="px-4 py-2 bg-amber-600 hover:bg-amber-700 text-white font-bold text-xs rounded-xl flex items-center gap-1.5 transition-colors shadow-sm"
            >
              <Pause className="w-3.5 h-3.5" /> Pause
            </button>
          )}
          <button
            onClick={() => handleControl('CANCEL')}
            className="px-4 py-2 bg-rose-50 hover:bg-rose-100 text-rose-700 border border-rose-200 font-bold text-xs rounded-xl flex items-center gap-1.5 transition-colors"
          >
            <XCircle className="w-3.5 h-3.5" /> Cancel Campaign
          </button>
          {onClose && (
            <button
              onClick={onClose}
              className="p-2 text-slate-400 hover:text-slate-700 bg-slate-100 rounded-xl"
              title="Hide Dashboard"
            >
              <X className="w-4 h-4" />
            </button>
          )}
        </div>
      </div>

      {/* Progress Bar Display */}
      <div className="space-y-2">
        <div className="flex items-center justify-between text-xs">
          <span className="font-bold text-slate-700">Real-Time Dispatch Progress</span>
          <span className="font-extrabold text-blue-600 text-sm">{progress.progressPercentage}%</span>
        </div>
        <div className="w-full bg-slate-100 rounded-full h-4 p-0.5 border border-slate-200 overflow-hidden">
          <div
            className="bg-gradient-to-r from-blue-600 via-sky-500 to-emerald-400 h-full rounded-full transition-all duration-500 shadow-xs"
            style={{ width: `${progress.progressPercentage}%` }}
          />
        </div>
      </div>

      {/* Real-Time Main Counters */}
      <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3">
        <div className="bg-slate-50 p-3.5 rounded-2xl border border-slate-100 text-center">
          <span className="text-xs text-slate-500 font-medium block">Total</span>
          <span className="text-lg font-bold text-slate-900">{progress.totalRecipients}</span>
        </div>
        <div className="bg-amber-50/60 p-3.5 rounded-2xl border border-amber-100 text-center">
          <span className="text-xs text-amber-700 font-medium block">Queued</span>
          <span className="text-lg font-bold text-amber-700">{progress.queuedCount}</span>
        </div>
        <div className="bg-blue-50/60 p-3.5 rounded-2xl border border-blue-100 text-center">
          <span className="text-xs text-blue-700 font-medium block">Sending</span>
          <span className="text-lg font-bold text-blue-700">{progress.sendingCount}</span>
        </div>
        <div className="bg-emerald-50/60 p-3.5 rounded-2xl border border-emerald-100 text-center">
          <span className="text-xs text-emerald-700 font-medium block">Delivered</span>
          <span className="text-lg font-bold text-emerald-700">{progress.deliveredCount}</span>
        </div>
        <div className="bg-rose-50/60 p-3.5 rounded-2xl border border-rose-100 text-center cursor-pointer hover:bg-rose-100/70 transition-colors" onClick={handleOpenFailures}>
          <span className="text-xs text-rose-700 font-medium block">Failed</span>
          <span className="text-lg font-bold text-rose-700">{progress.failedCount}</span>
        </div>
        <div className="bg-blue-50/60 p-3.5 rounded-2xl border border-blue-100 text-center">
          <span className="text-xs text-blue-700 font-medium block">Velocity</span>
          <span className="text-lg font-bold text-blue-700">{progress.emailsPerMinute} /m</span>
        </div>
      </div>

      {/* Enterprise SMTP Diagnostic Breakdown */}
      <div className="bg-slate-900 text-white p-4 rounded-2xl space-y-3 shadow-inner border border-slate-800">
        <div className="flex items-center justify-between text-xs border-b border-slate-800 pb-2">
          <span className="font-bold tracking-wider text-slate-300 flex items-center gap-2">
            <ShieldCheck className="w-4 h-4 text-emerald-400" /> Enterprise Delivery Diagnostics & Failure Analysis
          </span>
          <span className="text-slate-400 font-mono text-[11px]">Provider: {progress.activeProvider}</span>
        </div>
        <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-2 pt-1 text-center">
          <div className="bg-slate-800/80 p-2.5 rounded-xl border border-slate-700/50">
            <span className="text-[11px] text-slate-400 block">Auth Failures</span>
            <span className="text-sm font-bold text-rose-400">{progress.authFailures || 0}</span>
          </div>
          <div className="bg-slate-800/80 p-2.5 rounded-xl border border-slate-700/50">
            <span className="text-[11px] text-slate-400 block">Conn Failures</span>
            <span className="text-sm font-bold text-amber-400">{progress.connectionFailures || 0}</span>
          </div>
          <div className="bg-slate-800/80 p-2.5 rounded-xl border border-slate-700/50">
            <span className="text-[11px] text-slate-400 block">Invalid Recipient</span>
            <span className="text-sm font-bold text-purple-400">{progress.invalidRecipientFailures || 0}</span>
          </div>
          <div className="bg-slate-800/80 p-2.5 rounded-xl border border-slate-700/50">
            <span className="text-[11px] text-slate-400 block">Template Errors</span>
            <span className="text-sm font-bold text-orange-400">{progress.templateFailures || 0}</span>
          </div>
          <div className="bg-slate-800/80 p-2.5 rounded-xl border border-slate-700/50">
            <span className="text-[11px] text-slate-400 block">Provider Errors</span>
            <span className="text-sm font-bold text-rose-400">{progress.providerErrors || 0}</span>
          </div>
          <div className="bg-slate-800/80 p-2.5 rounded-xl border border-slate-700/50">
            <span className="text-[11px] text-slate-400 block">Total Retries</span>
            <span className="text-sm font-bold text-sky-400">{progress.retryCount || 0}</span>
          </div>
        </div>
      </div>

      {/* Workers & Activity Stream Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 pt-1">
        {/* Worker Threads Monitor */}
        <div className="bg-slate-50 p-4 rounded-2xl border border-slate-200 space-y-3">
          <div className="flex items-center gap-2 text-xs font-bold text-slate-800">
            <Cpu className="w-4 h-4 text-blue-600" /> Active Worker Threads
          </div>
          <div className="space-y-2">
            {progress.workers && progress.workers.length > 0 ? (
              progress.workers.map(w => (
                <div key={w.workerId} className="flex items-center justify-between p-3 bg-white rounded-xl text-xs border border-slate-200 shadow-xs">
                  <span className="font-mono text-slate-900 font-semibold">{w.workerId}</span>
                  <span className="text-emerald-600 font-bold">{w.status} ({w.processedCount} emails)</span>
                </div>
              ))
            ) : (
              <div className="text-xs text-slate-400 italic p-2">Initializing worker thread pool...</div>
            )}
          </div>
        </div>

        {/* Live Activity Stream */}
        <div className="bg-slate-50 p-4 rounded-2xl border border-slate-200 space-y-3">
          <div className="flex items-center gap-2 text-xs font-bold text-slate-800">
            <Activity className="w-4 h-4 text-emerald-600" /> Live Telemetry Log
          </div>
          <div className="space-y-1.5 max-h-32 overflow-y-auto pr-1">
            {progress.recentActivity && progress.recentActivity.length > 0 ? (
              progress.recentActivity.map((log, idx) => (
                <div key={idx} className="text-[11px] font-mono text-slate-600 truncate">
                  {log}
                </div>
              ))
            ) : (
              <div className="text-xs text-slate-400 italic p-2">Awaiting batch events...</div>
            )}
          </div>
        </div>
      </div>

      {/* Failure Details Modal */}
      {showFailureModal && (
        <div className="fixed inset-0 bg-slate-900/60 backdrop-blur-xs flex items-center justify-center p-4 z-50 animate-fadeIn">
          <div className="bg-white rounded-3xl p-6 max-w-4xl w-full max-h-[85vh] flex flex-col shadow-2xl space-y-4 border border-slate-200">
            <div className="flex items-center justify-between border-b border-slate-100 pb-3">
              <div className="flex items-center gap-2.5">
                <AlertOctagon className="w-5 h-5 text-rose-600" />
                <h3 className="text-lg font-extrabold text-slate-900">Campaign Failure Diagnostics</h3>
                <span className="text-xs px-2.5 py-0.5 rounded-full bg-rose-100 text-rose-700 font-bold">
                  {failureDetails.length} Failed Recipients
                </span>
              </div>
              <button
                onClick={() => setShowFailureModal(false)}
                className="p-1.5 text-slate-400 hover:text-slate-700 rounded-xl bg-slate-100"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            <div className="flex-1 overflow-y-auto space-y-2 pr-1">
              {loadingFailures ? (
                <div className="text-center py-8 text-slate-500 font-medium text-xs flex items-center justify-center gap-2">
                  <RefreshCw className="w-4 h-4 animate-spin text-blue-600" /> Loading failure details...
                </div>
              ) : failureDetails.length > 0 ? (
                <table className="w-full text-left text-xs border-collapse">
                  <thead>
                    <tr className="bg-slate-50 text-slate-500 border-b border-slate-200 font-semibold">
                      <th className="p-3">Recipient</th>
                      <th className="p-3">Status</th>
                      <th className="p-3">Exact Failure Reason / Diagnostic Trace</th>
                      <th className="p-3">Failed At</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-slate-100">
                    {failureDetails.map(item => (
                      <tr key={item.recipientId} className="hover:bg-rose-50/30 transition-colors">
                        <td className="p-3 font-semibold text-slate-900">
                          <div>{item.recipientName}</div>
                          <div className="text-[11px] text-slate-500 font-mono">{item.recipientEmail}</div>
                        </td>
                        <td className="p-3">
                          <span className="px-2 py-0.5 rounded-md bg-rose-100 text-rose-800 font-bold text-[10px]">
                            {item.status}
                          </span>
                        </td>
                        <td className="p-3 font-mono text-[11px] text-rose-700 bg-rose-50/50 rounded-lg max-w-md break-words">
                          {item.failureReason || 'No detailed reason captured.'}
                        </td>
                        <td className="p-3 text-slate-500 font-mono text-[11px]">
                          {item.failedAt ? new Date(item.failedAt).toLocaleTimeString() : 'N/A'}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              ) : (
                <div className="text-center py-8 text-slate-400 italic text-xs">
                  No failed recipient records found for this campaign.
                </div>
              )}
            </div>

            <div className="flex items-center justify-between border-t border-slate-100 pt-3">
              <span className="text-xs text-slate-400">All exceptions and SMTP diagnostic traces are persisted.</span>
              <button
                onClick={() => setShowFailureModal(false)}
                className="px-5 py-2 bg-slate-900 hover:bg-slate-800 text-white font-bold text-xs rounded-xl transition-colors"
              >
                Close Diagnostics
              </button>
            </div>
          </div>
        </div>
      )}

    </div>
  );
};
