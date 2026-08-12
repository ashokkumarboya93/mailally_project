import React, { useEffect } from 'react';
import { createPortal } from 'react-dom';
import { X } from 'lucide-react';

export const Modal = ({ isOpen, onClose, title, description, children, size = 'md' }) => {
  // Close on Escape key
  useEffect(() => {
    if (!isOpen) return;
    const handleEsc = (e) => {
      if (e.key === 'Escape') onClose();
    };
    document.addEventListener('keydown', handleEsc);
    return () => document.removeEventListener('keydown', handleEsc);
  }, [isOpen, onClose]);

  // Prevent body scroll when open
  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }
    return () => { document.body.style.overflow = ''; };
  }, [isOpen]);

  if (!isOpen) return null;

  const sizeClasses = {
    sm: 'max-w-md',
    md: 'max-w-lg',
    lg: 'max-w-2xl',
    xl: 'max-w-4xl',
  };

  return createPortal(
    <div
      className="fixed inset-0 z-[9999] flex items-center justify-center p-4"
      style={{ backgroundColor: 'rgba(0, 0, 0, 0.45)', backdropFilter: 'blur(4px)' }}
      onClick={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
    >
      <div
        className={`w-full ${sizeClasses[size] || sizeClasses.md} bg-white rounded-[20px] border border-[#E5E5E7] shadow-[0_20px_60px_rgba(0,0,0,0.18)] overflow-hidden animate-scaleIn`}
      >
        {/* Header */}
        <div className="flex items-start justify-between px-6 py-5 border-b border-[#E5E5E7]">
          <div>
            <h3 className="text-lg font-bold tracking-tight text-[#0A0A0B]">
              {title}
            </h3>
            {description && (
              <p className="mt-1 text-[13px] text-[#9CA3AF] font-medium">{description}</p>
            )}
          </div>
          <button
            onClick={onClose}
            className="p-1.5 rounded-lg transition-all cursor-pointer text-[#9CA3AF] hover:text-[#0A0A0B] hover:bg-[#F3F4F6] flex-shrink-0 ml-4"
            aria-label="Close modal"
          >
            <X className="w-4 h-4" />
          </button>
        </div>

        {/* Content */}
        <div className="px-6 py-5 max-h-[75vh] overflow-y-auto">
          {children}
        </div>
      </div>
    </div>,
    document.body
  );
};

