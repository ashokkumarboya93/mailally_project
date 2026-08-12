import React, { useEffect } from 'react';
import { createPortal } from 'react-dom';
import { AlertTriangle, Trash2, X } from 'lucide-react';

export const ConfirmModal = ({
  isOpen,
  onClose,
  onConfirm,
  title = 'Are you sure?',
  message = 'This action cannot be undone.',
  confirmText = 'Delete',
  cancelText = 'Cancel',
  type = 'danger'
}) => {
  useEffect(() => {
    if (!isOpen) return;
    const handleEsc = (e) => {
      if (e.key === 'Escape') onClose();
    };
    document.addEventListener('keydown', handleEsc);
    return () => document.removeEventListener('keydown', handleEsc);
  }, [isOpen, onClose]);

  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }
    return () => { document.body.style.overflow = ''; };
  }, [isOpen]);

  if (!isOpen) return null;

  const isDanger = type === 'danger';

  return createPortal(
    <div
      className="fixed inset-0 z-[10000] flex items-center justify-center p-4"
      style={{ backgroundColor: 'rgba(0, 0, 0, 0.5)', backdropFilter: 'blur(5px)' }}
      onClick={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
    >
      <div className="w-full max-w-sm bg-white rounded-[24px] border border-[#E5E5E7] shadow-[0_24px_72px_rgba(0,0,0,0.22)] overflow-hidden animate-scaleIn">
        
        {/* Header Badge & Close Button */}
        <div className="flex items-center justify-between p-5 pb-0">
          <div className={`w-12 h-12 rounded-2xl flex items-center justify-center ${
            isDanger ? 'bg-[#FEE2E2] text-[#DC2626]' : 'bg-[#FEF3C7] text-[#D97706]'
          }`}>
            {isDanger ? (
              <Trash2 className="w-6 h-6" strokeWidth={2} />
            ) : (
              <AlertTriangle className="w-6 h-6" strokeWidth={2} />
            )}
          </div>
          <button
            onClick={onClose}
            className="p-1.5 rounded-full text-[#9CA3AF] hover:text-[#0A0A0B] hover:bg-[#F3F4F6] transition-colors cursor-pointer"
          >
            <X className="w-4 h-4" />
          </button>
        </div>

        {/* Body Content */}
        <div className="p-6 pt-4 text-left">
          <h3 className="text-lg font-extrabold tracking-tight text-[#0A0A0B]">
            {title}
          </h3>
          <p className="mt-2 text-[13px] font-medium text-[#4B5563] leading-relaxed break-words">
            {message}
          </p>

          {/* Action Buttons Row */}
          <div className="mt-6 flex items-center gap-3">
            <button
              onClick={onClose}
              type="button"
              className="flex-1 py-2.5 px-4 rounded-xl font-semibold text-[13px] bg-[#F3F4F6] hover:bg-[#E5E7EB] text-[#374151] transition-all cursor-pointer"
            >
              {cancelText}
            </button>
            <button
              onClick={() => {
                onConfirm();
                onClose();
              }}
              type="button"
              className={`flex-1 py-2.5 px-4 rounded-xl font-bold text-[13px] transition-all shadow-sm cursor-pointer ${
                isDanger
                  ? 'bg-[#DC2626] hover:bg-[#B91C1C] text-white shadow-red-200'
                  : 'bg-[#0A0A0B] hover:bg-[#27272A] text-white'
              }`}
            >
              {confirmText}
            </button>
          </div>
        </div>

      </div>
    </div>,
    document.body
  );
};
