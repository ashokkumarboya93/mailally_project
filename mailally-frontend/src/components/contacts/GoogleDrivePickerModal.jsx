import React, { useState, useEffect } from 'react';
import { Modal } from '../common/Modal';
import { contactApi } from '../../api/contactApi';
import { HardDrive, FileSpreadsheet, Check, RefreshCw, Lock, LogOut, Search, ExternalLink } from 'lucide-react';

export const GoogleDrivePickerModal = ({ isOpen, onClose, onImportSuccess }) => {
  const [status, setStatus] = useState('LOADING'); // LOADING, NOT_CONNECTED, CONNECTED, REVOKED, ERROR
  const [accountEmail, setAccountEmail] = useState('');
  const [driveFiles, setDriveFiles] = useState([]);
  const [search, setSearch] = useState('');
  const [selectedFile, setSelectedFile] = useState(null);
  
  // Sheets State
  const [worksheets, setWorksheets] = useState([]);
  const [selectedWorksheet, setSelectedWorksheet] = useState('');
  const [loadingWorksheets, setLoadingWorksheets] = useState(false);

  const [importTag, setImportTag] = useState('');
  const [loadingFiles, setLoadingFiles] = useState(false);
  const [importing, setImporting] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const loadStatus = async () => {
    try {
      const res = await contactApi.getGoogleStatus();
      if (res?.data?.status === 'CONNECTED') {
        setStatus('CONNECTED');
        setAccountEmail(res.data.accountEmail || 'Connected Google Account');
        loadFiles();
      } else {
        setStatus('NOT_CONNECTED');
        setDriveFiles([]);
      }
    } catch (e) {
      setStatus('NOT_CONNECTED');
    }
  };

  const loadFiles = async (searchQuery = '') => {
    setLoadingFiles(true);
    setErrorMessage('');
    try {
      const res = await contactApi.getGoogleDriveFiles(searchQuery);
      if (res?.data && Array.isArray(res.data)) {
        setDriveFiles(res.data);
      }
    } catch (e) {
      setErrorMessage(e.response?.data?.message || 'Failed to load files from Google Drive');
    } finally {
      setLoadingFiles(false);
    }
  };

  useEffect(() => {
    if (isOpen) {
      loadStatus();
    }
  }, [isOpen]);

  // Handle message from OAuth Callback popup window
  useEffect(() => {
    const handleMessage = (event) => {
      if (event.data?.type === 'GOOGLE_AUTH_SUCCESS') {
        setStatus('CONNECTED');
        setAccountEmail(event.data.email || 'Connected Google Account');
        loadFiles();
      }
    };
    window.addEventListener('message', handleMessage);
    return () => window.removeEventListener('message', handleMessage);
  }, []);

  const handleConnectGoogle = async () => {
    try {
      setErrorMessage('');
      const res = await contactApi.getGoogleConnectUrl();
      const authUrl = res?.data?.authorizationUrl;
      if (authUrl) {
        const width = 600;
        const height = 700;
        const left = window.screen.width / 2 - width / 2;
        const top = window.screen.height / 2 - height / 2;
        window.open(
          authUrl,
          'Connect Google OAuth',
          `width=${width},height=${height},top=${top},left=${left},scrollbars=yes`
        );
      }
    } catch (e) {
      setErrorMessage(e.response?.data?.message || 'Failed to generate Google auth URL');
    }
  };

  const handleDisconnect = async () => {
    try {
      await contactApi.disconnectGoogle();
      setStatus('NOT_CONNECTED');
      setAccountEmail('');
      setDriveFiles([]);
      setSelectedFile(null);
      setWorksheets([]);
    } catch (e) {
      console.error(e);
    }
  };

  const handleSelectFile = async (file) => {
    setSelectedFile(file);
    setSelectedWorksheet('');
    setWorksheets([]);
    
    if (file.isSpreadsheet) {
      setLoadingWorksheets(true);
      try {
        const res = await contactApi.getGoogleWorksheets(file.id);
        if (res?.data && Array.isArray(res.data)) {
          setWorksheets(res.data);
          if (res.data.length > 0) {
            setSelectedWorksheet(res.data[0].title);
          }
        }
      } catch (e) {
        setErrorMessage('Failed to load worksheets for spreadsheet');
      } finally {
        setLoadingWorksheets(false);
      }
    }
  };

  const handleImport = async () => {
    if (!selectedFile) return;
    setImporting(true);
    setErrorMessage('');
    try {
      let res;
      if (selectedFile.isSpreadsheet) {
        if (!selectedWorksheet) {
          setErrorMessage('Please select a worksheet tab to import');
          setImporting(false);
          return;
        }
        res = await contactApi.importGoogleSheet(selectedFile.id, selectedWorksheet, importTag);
      } else {
        res = await contactApi.importGoogleDriveFile(selectedFile.id, importTag);
      }

      setImporting(false);
      onClose();
      if (onImportSuccess) {
        onImportSuccess(res?.data);
      }
    } catch (e) {
      setErrorMessage(e.response?.data?.message || 'Import failed');
      setImporting(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Import from Google Drive & Sheets" size="lg">
      <div className="space-y-6 text-slate-800 font-sans">
        
        {/* Connection Header Bar */}
        <div className="flex items-center justify-between p-4 bg-blue-50/70 border border-blue-100 rounded-2xl">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-xl bg-blue-600 text-white flex items-center justify-center font-bold shadow-xs">
              <HardDrive className="w-5 h-5" />
            </div>
            <div>
              <div className="flex items-center gap-2">
                <span className="text-sm font-bold text-slate-900">Google OAuth 2.0</span>
                {status === 'CONNECTED' ? (
                  <span className="inline-flex items-center gap-1 text-xs px-2 py-0.5 rounded-full bg-emerald-100 text-emerald-700 font-bold border border-emerald-200">
                    <Check className="w-3 h-3" /> Connected
                  </span>
                ) : (
                  <span className="inline-flex items-center gap-1 text-xs px-2 py-0.5 rounded-full bg-slate-100 text-slate-600 font-bold border border-slate-200">
                    Not Connected
                  </span>
                )}
              </div>
              <p className="text-xs text-slate-500 font-medium">
                {status === 'CONNECTED' ? accountEmail : 'Connect your account to browse Drive & Sheets'}
              </p>
            </div>
          </div>

          {status === 'CONNECTED' ? (
            <div className="flex items-center gap-2">
              <button
                type="button"
                onClick={() => loadFiles(search)}
                className="p-2 rounded-xl text-slate-600 hover:text-blue-600 bg-white border border-slate-200 hover:bg-slate-50 transition-colors cursor-pointer"
                title="Refresh Files"
              >
                <RefreshCw className="w-4 h-4" />
              </button>
              <button
                type="button"
                onClick={handleDisconnect}
                className="flex items-center gap-1 text-xs font-bold text-rose-600 hover:text-rose-700 bg-white hover:bg-rose-50 px-3 py-2 rounded-xl border border-rose-200 transition-colors cursor-pointer"
              >
                <LogOut className="w-3.5 h-3.5" /> Disconnect
              </button>
            </div>
          ) : (
            <button
              type="button"
              onClick={handleConnectGoogle}
              className="flex items-center gap-1.5 px-4 py-2 rounded-xl bg-blue-600 hover:bg-blue-700 text-white text-xs font-bold transition-all shadow-xs cursor-pointer"
            >
              <ExternalLink className="w-3.5 h-3.5" /> Connect Google
            </button>
          )}
        </div>

        {errorMessage && (
          <div className="p-3.5 bg-rose-50 border border-rose-200 rounded-xl text-xs font-semibold text-rose-700">
            {errorMessage}
          </div>
        )}

        {/* Content Section */}
        {status === 'CONNECTED' ? (
          <div className="space-y-4">
            {/* Search Bar */}
            <div className="flex items-center gap-2">
              <div className="relative flex-1">
                <Search className="w-4 h-4 text-slate-400 absolute left-3 top-1/2 -translate-y-1/2" />
                <input
                  type="text"
                  placeholder="Search CSV, Excel, or Google Sheets..."
                  value={search}
                  onChange={(e) => {
                    setSearch(e.target.value);
                    loadFiles(e.target.value);
                  }}
                  className="w-full pl-9 pr-3 h-9 text-xs font-medium bg-slate-50 border border-slate-200 rounded-xl outline-none focus:border-blue-500"
                />
              </div>
            </div>

            {/* Drive Files List */}
            <div className="space-y-2 max-h-60 overflow-y-auto pr-1">
              {loadingFiles ? (
                <div className="p-8 text-center text-xs font-medium text-slate-500">
                  <RefreshCw className="w-5 h-5 animate-spin mx-auto mb-2 text-blue-600" />
                  Loading Drive files...
                </div>
              ) : driveFiles.length === 0 ? (
                <div className="p-8 text-center border-2 border-dashed border-slate-200 rounded-2xl">
                  <p className="text-xs font-bold text-slate-600">No supported files found in Drive</p>
                  <p className="text-[11px] text-slate-400 mt-1">Make sure you have CSV, Excel (.xlsx), or Google Sheets files in your Drive.</p>
                </div>
              ) : (
                driveFiles.map((file) => (
                  <div
                    key={file.id}
                    onClick={() => handleSelectFile(file)}
                    className={`p-3.5 rounded-2xl border transition-all cursor-pointer flex items-center justify-between ${
                      selectedFile?.id === file.id
                        ? 'bg-blue-50 border-blue-500 shadow-xs'
                        : 'bg-white border-slate-200 hover:border-slate-300 hover:bg-slate-50/60'
                    }`}
                  >
                    <div className="flex items-center gap-3">
                      <div className={`p-2 rounded-xl border ${
                        file.isSpreadsheet ? 'bg-emerald-50 text-emerald-600 border-emerald-200' : 'bg-blue-50 text-blue-600 border-blue-200'
                      }`}>
                        <FileSpreadsheet className="w-5 h-5" />
                      </div>
                      <div>
                        <p className="text-xs font-bold text-slate-900">{file.name}</p>
                        <p className="text-[11px] text-slate-500 font-medium">
                          {file.isSpreadsheet ? 'Google Sheet' : file.mimeType?.includes('sheet') || file.name.endsWith('.xlsx') ? 'Excel Spreadsheet' : 'CSV File'}
                        </p>
                      </div>
                    </div>

                    <div className="flex items-center gap-2">
                      {selectedFile?.id === file.id ? (
                        <div className="w-5 h-5 rounded-full bg-blue-600 text-white flex items-center justify-center shadow-xs">
                          <Check className="w-3 h-3" />
                        </div>
                      ) : (
                        <div className="w-5 h-5 rounded-full border border-slate-300" />
                      )}
                    </div>
                  </div>
                ))
              )}
            </div>

            {/* Worksheet Selector (if selected file is a Google Sheet) */}
            {selectedFile?.isSpreadsheet && (
              <div className="p-4 bg-slate-50 border border-slate-200 rounded-2xl space-y-3">
                <label className="block text-xs font-bold text-slate-700">Select Worksheet Tab</label>
                {loadingWorksheets ? (
                  <div className="text-xs text-slate-500 flex items-center gap-2">
                    <RefreshCw className="w-3.5 h-3.5 animate-spin" /> Loading worksheet tabs...
                  </div>
                ) : (
                  <select
                    value={selectedWorksheet}
                    onChange={(e) => setSelectedWorksheet(e.target.value)}
                    className="w-full h-10 px-3 rounded-xl border border-slate-300 text-xs font-semibold bg-white outline-none focus:border-blue-500"
                  >
                    {worksheets.map(ws => (
                      <option key={ws.sheetId} value={ws.title}>
                        {ws.title} ({ws.rowCount || 0} rows)
                      </option>
                    ))}
                  </select>
                )}
              </div>
            )}

            {/* Optional Tag Input */}
            <div>
              <label className="block text-xs font-bold text-slate-700 mb-1">TAG <span className="text-slate-400 font-normal">(optional)</span></label>
              <input
                type="text"
                placeholder="e.g. Drive Leads, Google Sheets Import"
                value={importTag}
                onChange={(e) => setImportTag(e.target.value)}
                className="w-full h-10 px-3 rounded-xl border border-slate-200 text-xs font-medium bg-white outline-none focus:border-blue-500"
              />
            </div>
          </div>
        ) : (
          <div className="p-10 text-center border-2 border-dashed border-slate-200 rounded-3xl bg-slate-50/50 space-y-4">
            <div className="w-14 h-14 rounded-2xl bg-blue-100 text-blue-600 mx-auto flex items-center justify-center">
              <HardDrive className="w-7 h-7" />
            </div>
            <div>
              <h3 className="text-base font-extrabold text-slate-900">Connect Google Account</h3>
              <p className="text-xs text-slate-500 max-w-sm mx-auto mt-1 leading-relaxed">
                Connect your Google account securely to import CSV files, Excel spreadsheets, or Google Sheets directly into your MailAlly contact list.
              </p>
            </div>
            <button
              type="button"
              onClick={handleConnectGoogle}
              className="px-6 py-3 rounded-xl bg-blue-600 hover:bg-blue-700 text-white font-bold text-xs shadow-md transition-all inline-flex items-center gap-2 cursor-pointer"
            >
              <ExternalLink className="w-4 h-4" /> Connect Google Account
            </button>
          </div>
        )}

        {/* Action Row */}
        <div className="flex items-center justify-between pt-4 border-t border-slate-100">
          <div className="flex items-center gap-1.5 text-xs text-slate-500 font-medium">
            <Lock className="w-3.5 h-3.5 text-slate-400" /> Secure AES-256 encrypted integration
          </div>
          <div className="flex items-center gap-3">
            <button
              onClick={onClose}
              className="px-4 py-2 text-xs font-bold text-slate-600 hover:text-slate-900 bg-slate-100 hover:bg-slate-200 rounded-xl transition-colors cursor-pointer"
            >
              Cancel
            </button>
            {status === 'CONNECTED' && (
              <button
                onClick={handleImport}
                disabled={!selectedFile || importing}
                className="px-5 py-2 text-xs font-bold text-white bg-blue-600 hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed rounded-xl shadow-xs flex items-center gap-2 transition-all cursor-pointer"
              >
                {importing ? (
                  <>
                    <RefreshCw className="w-3.5 h-3.5 animate-spin" /> Importing...
                  </>
                ) : (
                  'Import Selected Source'
                )}
              </button>
            )}
          </div>
        </div>

      </div>
    </Modal>
  );
};
