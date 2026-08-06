import React from 'react';
import { Modal } from '../common/Modal';
import { FileText, Download, RotateCcw } from 'lucide-react';

export const ImportHistoryModal = ({ isOpen, onClose, history = [], onUndoImport }) => {
  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Import Execution History & Rollbacks" size="xl">
      <div className="space-y-4 text-slate-800 font-sans">
        {history.length === 0 ? (
          <div className="p-12 text-center border border-dashed border-slate-200 rounded-3xl bg-slate-50/50">
            <FileText className="w-10 h-10 text-slate-400 mx-auto mb-3" />
            <p className="text-sm font-bold text-slate-700">No import history records found.</p>
            <p className="text-xs text-slate-500 mt-1 font-medium">Uploaded Excel and CSV files will be logged here with error report download and undo support.</p>
          </div>
        ) : (
          <div className="overflow-x-auto border border-slate-200 rounded-2xl bg-white shadow-xs">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="bg-slate-50 text-xs font-bold text-slate-600 uppercase tracking-wider border-b border-slate-200">
                  <th className="py-3 px-4">File / Batch Name</th>
                  <th className="py-3 px-4">Source</th>
                  <th className="py-3 px-4 text-center">Total</th>
                  <th className="py-3 px-4 text-center">Valid</th>
                  <th className="py-3 px-4 text-center">Skipped</th>
                  <th className="py-3 px-4 text-center">Invalid</th>
                  <th className="py-3 px-4">Imported At</th>
                  <th className="py-3 px-4 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 text-sm text-slate-800 font-medium">
                {history.map((batch) => (
                  <tr key={batch.id} className="hover:bg-blue-50/30 transition-colors">
                    <td className="py-3 px-4 font-bold text-slate-900">
                      <div className="flex items-center gap-2">
                        <FileText className="w-4 h-4 text-blue-600" />
                        <div>
                          <div>{batch.originalFileName || batch.batchName || 'Import_Batch'}</div>
                          <div className="text-xs text-slate-400 font-mono font-normal">{batch.batchCode}</div>
                        </div>
                      </div>
                    </td>
                    <td className="py-3 px-4">
                      <span className="text-xs px-2 py-0.5 rounded-md bg-slate-100 text-slate-700 font-mono border border-slate-200">
                        {batch.sourceType || 'EXCEL'}
                      </span>
                    </td>
                    <td className="py-3 px-4 text-center font-bold text-slate-900">{batch.totalRows || 0}</td>
                    <td className="py-3 px-4 text-center text-emerald-600 font-bold">{batch.importedCount || 0}</td>
                    <td className="py-3 px-4 text-center text-amber-600 font-bold">{batch.skippedCount || 0}</td>
                    <td className="py-3 px-4 text-center text-rose-600 font-bold">{batch.invalidCount || 0}</td>
                    <td className="py-3 px-4 text-xs text-slate-500 font-normal">
                      {batch.importDate ? new Date(batch.importDate).toLocaleString() : 'Just now'}
                    </td>
                    <td className="py-3 px-4 text-right">
                      <div className="flex items-center justify-end gap-2">
                        {batch.invalidCount > 0 && (
                          <button
                            title="Download Invalid Rows CSV"
                            className="p-1.5 rounded-lg bg-rose-50 text-rose-600 hover:bg-rose-100 border border-rose-200 transition-colors"
                          >
                            <Download className="w-4 h-4" />
                          </button>
                        )}
                        <button
                          onClick={() => onUndoImport && onUndoImport(batch.id)}
                          title="Undo Import (Rollback Batch)"
                          className="px-2.5 py-1 text-xs font-bold bg-amber-50 text-amber-700 hover:bg-amber-100 border border-amber-200 rounded-lg flex items-center gap-1 transition-colors"
                        >
                          <RotateCcw className="w-3.5 h-3.5" /> Undo
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </Modal>
  );
};
