import React, { useState } from 'react';
import { Modal } from '../common/Modal';
import { HardDrive, FileSpreadsheet, Check, RefreshCw, Lock } from 'lucide-react';

export const GoogleDrivePickerModal = ({ isOpen, onClose, onImportFile }) => {
  const [selectedFile, setSelectedFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [isConnected, setIsConnected] = useState(true);

  const sampleDriveFiles = [
    { id: 'drive_101', name: 'Enterprise_Customers_2026.xlsx', size: '2.4 MB', modified: 'Today at 10:15 AM', mimeType: 'xlsx', iconColor: 'text-emerald-600 bg-emerald-50' },
    { id: 'drive_102', name: 'July_Leads_Healthcare.csv', size: '1.1 MB', modified: 'Yesterday at 4:30 PM', mimeType: 'csv', iconColor: 'text-blue-600 bg-blue-50' },
    { id: 'drive_103', name: 'University_Students_Q3.xlsx', size: '3.8 MB', modified: '3 days ago', mimeType: 'xlsx', iconColor: 'text-emerald-600 bg-emerald-50' },
    { id: 'drive_104', name: 'Global_Doctors_List.csv', size: '850 KB', modified: 'Last week', mimeType: 'csv', iconColor: 'text-blue-600 bg-blue-50' },
  ];

  const handleSelect = (file) => {
    setSelectedFile(file);
  };

  const handleImport = () => {
    if (!selectedFile) return;
    setLoading(true);
    setTimeout(() => {
      onImportFile(selectedFile);
      setLoading(false);
      onClose();
    }, 1200);
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Import From Google Drive" size="lg">
      <div className="space-y-6 text-slate-800 font-sans">
        <div className="flex items-center justify-between p-4 bg-blue-50/60 border border-blue-100 rounded-2xl">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-blue-600 text-white flex items-center justify-center font-bold shadow-sm">
              <HardDrive className="w-5 h-5" />
            </div>
            <div>
              <div className="flex items-center gap-2">
                <span className="text-sm font-bold text-slate-900">Google OAuth Account</span>
                <span className="inline-flex items-center gap-1 text-xs px-2 py-0.5 rounded-full bg-emerald-100 text-emerald-700 font-bold border border-emerald-200">
                  <Check className="w-3 h-3" /> Connected
                </span>
              </div>
              <p className="text-xs text-slate-500 font-medium">ashok.marketing@enterprise-mailally.com</p>
            </div>
          </div>
          <button 
            onClick={() => setIsConnected(!isConnected)} 
            className="text-xs font-bold text-slate-700 hover:text-blue-600 flex items-center gap-1 bg-white hover:bg-slate-50 px-3 py-1.5 rounded-xl transition-colors border border-slate-200 shadow-xs"
          >
            <RefreshCw className="w-3.5 h-3.5" /> Reconnect
          </button>
        </div>

        <div>
          <div className="flex items-center justify-between mb-3">
            <h4 className="text-xs font-bold text-slate-500 uppercase tracking-wider">Select CSV or Excel File from Drive</h4>
            <span className="text-xs text-slate-400 font-medium">Only accessible by your Google account</span>
          </div>

          <div className="space-y-2 max-h-64 overflow-y-auto pr-1">
            {sampleDriveFiles.map((file) => (
              <div
                key={file.id}
                onClick={() => handleSelect(file)}
                className={`p-3.5 rounded-2xl border transition-all cursor-pointer flex items-center justify-between ${
                  selectedFile?.id === file.id
                    ? 'bg-blue-50 border-blue-500 shadow-sm'
                    : 'bg-white border-slate-200 hover:border-slate-300 hover:bg-slate-50/50'
                }`}
              >
                <div className="flex items-center gap-3">
                  <div className={`p-2 rounded-xl border border-slate-200 ${file.iconColor}`}>
                    <FileSpreadsheet className="w-5 h-5" />
                  </div>
                  <div>
                    <p className="text-sm font-bold text-slate-900">{file.name}</p>
                    <p className="text-xs text-slate-500 font-medium">{file.size} • Modified {file.modified}</p>
                  </div>
                </div>

                <div className="flex items-center gap-2">
                  {selectedFile?.id === file.id ? (
                    <div className="w-6 h-6 rounded-full bg-blue-600 text-white flex items-center justify-center shadow-xs">
                      <Check className="w-3.5 h-3.5" />
                    </div>
                  ) : (
                    <div className="w-6 h-6 rounded-full border border-slate-300" />
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="flex items-center justify-between pt-4 border-t border-slate-100">
          <div className="flex items-center gap-2 text-xs text-slate-500 font-medium">
            <Lock className="w-3.5 h-3.5 text-slate-400" /> Secure direct Drive stream pipeline
          </div>
          <div className="flex items-center gap-3">
            <button
              onClick={onClose}
              className="px-4 py-2 text-sm font-bold text-slate-600 hover:text-slate-900 bg-slate-100 hover:bg-slate-200 rounded-xl transition-colors"
            >
              Cancel
            </button>
            <button
              onClick={handleImport}
              disabled={!selectedFile || loading}
              className="px-5 py-2 text-sm font-bold text-white bg-blue-600 hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed rounded-xl shadow-md flex items-center gap-2 transition-all"
            >
              {loading ? (
                <>
                  <RefreshCw className="w-4 h-4 animate-spin" /> Importing...
                </>
              ) : (
                'Import Selected File'
              )}
            </button>
          </div>
        </div>
      </div>
    </Modal>
  );
};
