import React from 'react';
import { X } from 'lucide-react';

export const Modal = ({ isOpen, onClose, title, children }) => {
  if (!isOpen) return null;

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center p-4"
      style={{ backgroundColor: 'rgba(15, 14, 23, 0.65)', backdropFilter: 'blur(10px)' }}
      onClick={(e) => {
        if (e.target === e.currentTarget) onClose();
      }}
    >
      <div
        className="w-full max-w-lg overflow-hidden animate-scaleIn rounded-[32px] claude-card p-0 shadow-2xl border border-purple-500/20"
        style={{
          backgroundColor: 'var(--claude-surface)',
        }}
      >
        <div
          className="flex items-center justify-between px-8 py-5 border-b"
          style={{
            background: 'linear-gradient(135deg, rgba(123, 97, 255, 0.08), transparent)',
            borderColor: 'var(--claude-border)',
          }}
        >
          <h3
            className="text-xl font-extrabold tracking-tight"
            style={{ color: 'var(--claude-text)', fontFamily: 'var(--font-heading)' }}
          >
            {title}
          </h3>
          <button
            onClick={onClose}
            className="p-2 rounded-xl transition-all cursor-pointer text-slate-400 hover:text-[#7B61FF] hover:bg-purple-50 dark:hover:bg-purple-950/40"
          >
            <X className="w-5 h-5" />
          </button>
        </div>
        <div className="p-8 max-h-[80vh] overflow-y-auto">{children}</div>
      </div>
    </div>
  );
};
