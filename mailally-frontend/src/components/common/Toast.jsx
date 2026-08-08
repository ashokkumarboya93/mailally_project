import React, { useState, useEffect, useCallback, createContext, useContext } from 'react';
import { CheckCircle2, AlertCircle, AlertTriangle, Info, X } from 'lucide-react';

const TOAST_CONFIG = {
  success: {
    bg: '#DCFCE7',
    border: '#BBF7D0',
    text: '#166534',
    icon: CheckCircle2,
  },
  error: {
    bg: '#FFE4E6',
    border: '#FECDD3',
    text: '#9F1239',
    icon: AlertCircle,
  },
  warning: {
    bg: '#FEF3C7',
    border: '#FDE68A',
    text: '#92400E',
    icon: AlertTriangle,
  },
  info: {
    bg: '#DBEAFE',
    border: '#BFDBFE',
    text: '#1E40AF',
    icon: Info,
  },
};

/* ── Individual Toast ── */
const ToastItem = ({ id, type = 'info', message, onDismiss, duration = 4000 }) => {
  const [isVisible, setIsVisible] = useState(false);

  useEffect(() => {
    requestAnimationFrame(() => setIsVisible(true));
    const timer = setTimeout(() => {
      setIsVisible(false);
      setTimeout(() => onDismiss(id), 200);
    }, duration);
    return () => clearTimeout(timer);
  }, [id, duration, onDismiss]);

  const config = TOAST_CONFIG[type] || TOAST_CONFIG.info;
  const Icon = config.icon;

  return (
    <div
      className={`flex items-center gap-3 px-4 py-3 rounded-xl shadow-md border max-w-sm transition-all duration-200 ${
        isVisible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-2'
      }`}
      style={{
        backgroundColor: config.bg,
        borderColor: config.border,
      }}
      role="alert"
    >
      <Icon className="w-4 h-4 flex-shrink-0" style={{ color: config.text }} />
      <p className="text-[13px] font-medium flex-1" style={{ color: config.text }}>
        {message}
      </p>
      <button
        onClick={() => {
          setIsVisible(false);
          setTimeout(() => onDismiss(id), 200);
        }}
        className="p-0.5 rounded-md hover:bg-black/5 transition-colors cursor-pointer flex-shrink-0"
        style={{ color: config.text }}
      >
        <X className="w-3.5 h-3.5" />
      </button>
    </div>
  );
};

/* ── Toast Context ── */
const ToastContext = createContext(null);

export const ToastProvider = ({ children }) => {
  const [toasts, setToasts] = useState([]);

  const addToast = useCallback((type, message, duration) => {
    const id = Date.now() + Math.random();
    setToasts((prev) => [...prev, { id, type, message, duration }]);
  }, []);

  const dismissToast = useCallback((id) => {
    setToasts((prev) => prev.filter((t) => t.id !== id));
  }, []);

  const toast = {
    success: (msg, dur) => addToast('success', msg, dur),
    error: (msg, dur) => addToast('error', msg, dur),
    warning: (msg, dur) => addToast('warning', msg, dur),
    info: (msg, dur) => addToast('info', msg, dur),
  };

  return (
    <ToastContext.Provider value={toast}>
      {children}
      {/* Toast Container */}
      <div className="fixed bottom-6 right-6 z-[100] flex flex-col gap-2 items-end">
        {toasts.map((t) => (
          <ToastItem
            key={t.id}
            id={t.id}
            type={t.type}
            message={t.message}
            duration={t.duration}
            onDismiss={dismissToast}
          />
        ))}
      </div>
    </ToastContext.Provider>
  );
};

export const useToast = () => {
  const context = useContext(ToastContext);
  if (!context) {
    // Fallback for pages not wrapped in ToastProvider — silent no-ops
    return {
      success: () => {},
      error: () => {},
      warning: () => {},
      info: () => {},
    };
  }
  return context;
};
