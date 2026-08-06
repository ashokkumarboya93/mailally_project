import React from 'react';
import { Modal } from '../common/Modal';
import { CheckCircle2, AlertTriangle, XCircle, ShieldCheck, Clock, DollarSign } from 'lucide-react';

export const CampaignDiagnosticsModal = ({ isOpen, onClose, diagnostics, templates, onAttachTemplate, onConfirmLaunch }) => {
  if (!diagnostics) return null;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Pre-Launch Campaign Health Diagnostics" size="lg">
      <div className="space-y-6 text-slate-800 font-sans">
        
        {/* Readiness Status Banner */}
        <div className={`p-4 rounded-2xl border flex items-center gap-3 ${
          diagnostics.isReady
            ? 'bg-emerald-50 border-emerald-200 text-emerald-800'
            : 'bg-rose-50 border-rose-200 text-rose-800'
        }`}>
          {diagnostics.isReady ? <CheckCircle2 className="w-6 h-6 text-emerald-600 shrink-0" /> : <XCircle className="w-6 h-6 text-rose-600 shrink-0" />}
          <div>
            <h4 className="text-sm font-bold">{diagnostics.isReady ? 'Campaign Ready for Launch' : 'Validation Issues Detected'}</h4>
            <p className="text-xs font-medium opacity-90">
              {diagnostics.isReady
                ? 'All pre-flight diagnostic checks passed successfully.'
                : 'Please resolve errors below before launching campaign dispatches.'}
            </p>
          </div>
        </div>

        {/* Diagnostics Metrics */}
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 text-center">
          <div className="p-3 bg-slate-50 border border-slate-200 rounded-2xl">
            <span className="text-xs text-slate-500 font-medium block">Attached Recipients</span>
            <span className="text-lg font-bold text-slate-900">{diagnostics.totalRecipients || 0}</span>
          </div>
          <div className="p-3 bg-blue-50/60 border border-blue-100 rounded-2xl">
            <span className="text-xs text-blue-700 font-medium block">Est. Duration</span>
            <span className="text-lg font-bold text-blue-700">{diagnostics.estimatedDurationMinutes || 1} min</span>
          </div>
          <div className="p-3 bg-emerald-50/60 border border-emerald-100 rounded-2xl">
            <span className="text-xs text-emerald-700 font-medium block">Est. Cost</span>
            <span className="text-lg font-bold text-emerald-700">{diagnostics.estimatedCost || '$0.00'}</span>
          </div>
          <div className="p-3 bg-slate-50 border border-slate-200 rounded-2xl">
            <span className="text-xs text-slate-500 font-medium block">Active Provider</span>
            <span className="text-xs font-bold text-slate-800 mt-1 block">{diagnostics.activeProvider || 'SMTP'}</span>
          </div>
        </div>

        {/* Checklist */}
        <div className="space-y-2">
          <h4 className="text-xs font-bold text-slate-500 uppercase tracking-wider">Health Diagnostics Checklist</h4>
          
          <div className="space-y-2 text-xs">
            <div className="flex flex-col p-3 bg-slate-50 rounded-xl border border-slate-200 gap-2">
              <div className="flex items-center justify-between font-medium">
                <span className="text-slate-800">Email Template Attached</span>
                {diagnostics.templateExists ? <CheckCircle2 className="w-4 h-4 text-emerald-600" /> : <XCircle className="w-4 h-4 text-rose-600" />}
              </div>

              {!diagnostics.templateExists && templates && (
                <div className="pt-2 border-t border-slate-200 space-y-1.5">
                  <span className="text-[11px] font-bold text-rose-700">Select template below to resolve validation:</span>
                  <select
                    onChange={(e) => onAttachTemplate && onAttachTemplate(diagnostics.campaignId, e.target.value)}
                    className="w-full p-2 bg-white border border-rose-300 rounded-lg text-xs font-semibold text-slate-800 focus:outline-none"
                  >
                    <option value="">Attach Email Template...</option>
                    {templates.map(t => (
                      <option key={t.id} value={t.id}>{t.name} ({t.subject || 'No subject'})</option>
                    ))}
                  </select>
                </div>
              )}
            </div>

            <div className="flex items-center justify-between p-3 bg-slate-50 rounded-xl border border-slate-200 font-medium">
              <span className="text-slate-800">Recipients Count &gt; 0</span>
              {diagnostics.totalRecipients > 0 ? <CheckCircle2 className="w-4 h-4 text-emerald-600" /> : <XCircle className="w-4 h-4 text-rose-600" />}
            </div>

            <div className="flex items-center justify-between p-3 bg-slate-50 rounded-xl border border-slate-200 font-medium">
              <span className="text-slate-800">Provider Connection Healthy</span>
              {diagnostics.providerHealthy ? <CheckCircle2 className="w-4 h-4 text-emerald-600" /> : <XCircle className="w-4 h-4 text-rose-600" />}
            </div>
          </div>
        </div>

        {/* Action Buttons */}
        <div className="flex items-center justify-end gap-3 pt-4 border-t border-slate-100">
          <button
            onClick={onClose}
            className="px-4 py-2 text-sm font-bold text-slate-600 hover:text-slate-900 bg-slate-100 rounded-xl"
          >
            Cancel
          </button>
          <button
            onClick={onConfirmLaunch}
            disabled={!diagnostics.isReady}
            className="px-6 py-2.5 bg-emerald-600 hover:bg-emerald-700 disabled:opacity-50 text-white font-bold text-sm rounded-xl shadow-md"
          >
            Confirm & Launch Campaign Now
          </button>
        </div>

      </div>
    </Modal>
  );
};
