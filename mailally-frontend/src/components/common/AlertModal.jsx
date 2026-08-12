import React, { useEffect } from 'react';
import { createPortal } from 'react-dom';
import { CheckCircle2, AlertCircle, X } from 'lucide-react';

export const AlertModal = ({
  isOpen,
  onClose,
  type = 'success',
  title,
  message
}) => {
  useEffect(() => {
    if (!isOpen) return;
    const handleEsc = (e) => {
      if (e.key === 'Escape' || e.key === 'Enter') onClose();
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

  const isSuccess = type === 'success';

  return createPortal(
    <div
      className="fixed inset-0 z-[10000] flex items-center justify-center p-4"
      style={{ backgroundColor: 'rgba(0, 0, 0, 0.5)', backdropFilter: 'blur(5px)' }}
      onClick={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
    >
      <div className="w-full max-w-sm bg-white rounded-[24px] border border-[#E5E5E7] shadow-[0_24px_72px_rgba(0,0,0,0.22)] overflow-hidden animate-scaleIn">
        
        {/* Header Badge & Close Icon */}
        <div className="flex items-center justify-between p-5 pb-0">
          <div className={`w-12 h-12 rounded-2xl flex items-center justify-center ${
            isSuccess ? 'bg-[#DCFCE7] text-[#16A34A]' : 'bg-[#FEE2E2] text-[#DC2626]'
          }`}>
            {isSuccess ? (
              <CheckCircle2 className="w-6 h-6" strokeWidth={2.2} />
            ) : (
              <AlertCircle className="w-6 h-6" strokeWidth={2.2} />
            )}
          </div>
          <button
            onClick={onClose}
            className="p-1.5 rounded-full text-[#9CA3AF] hover:text-[#0A0A0B] hover:bg-[#F3F4F6] transition-colors cursor-pointer"
          >
            <X className="w-4 h-4" />
          </button>
        </div>

        {/* Content Body */}
        <div className="p-6 pt-4 text-left">
          <h3 className="text-lg font-extrabold tracking-tight text-[#0A0A0B]">
            {title || (isSuccess ? 'Success' : 'Error')}
          </h3>
          <p className="mt-2 text-[13px] font-medium text-[#4B5563] leading-relaxed break-words">
            {message}
          </p>

          {/* Action Row */}
          <div className="mt-6">
            <button
              onClick={onClose}
              autoFocus
              className={`w-full py-2.5 px-4 rounded-xl font-bold text-[13px] transition-all shadow-sm cursor-pointer ${
                isSuccess
                  ? 'bg-[#16A34A] hover:bg-[#15803D] text-white shadow-green-200'
                  : 'bg-[#DC2626] hover:bg-[#B91C1C] text-white shadow-red-200'
              }`}
            >
              OK
            </button>
          </div>
        </div>

      </div>
    </div>,
    document.body
  );
};
