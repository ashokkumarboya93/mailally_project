import React from 'react';

const STATUS_CONFIG = {
  // Success states
  ACTIVE: { bg: '#DCFCE7', text: '#16A34A', dot: '#22C55E', pulse: true },
  COMPLETED: { bg: '#DCFCE7', text: '#16A34A', dot: '#22C55E', pulse: false },
  PAID: { bg: '#DCFCE7', text: '#16A34A', dot: '#22C55E', pulse: false },
  SUBSCRIBED: { bg: '#DCFCE7', text: '#16A34A', dot: '#22C55E', pulse: false },
  DELIVERED: { bg: '#DCFCE7', text: '#16A34A', dot: '#22C55E', pulse: false },
  OPERATIONAL: { bg: '#DCFCE7', text: '#16A34A', dot: '#22C55E', pulse: true },
  CONNECTED: { bg: '#DCFCE7', text: '#16A34A', dot: '#22C55E', pulse: false },
  SUCCESS: { bg: '#DCFCE7', text: '#16A34A', dot: '#22C55E', pulse: false },

  // Info / In-progress states
  PENDING: { bg: '#F3E8FF', text: '#7C3AED', dot: '#8B5CF6', pulse: false },
  SCHEDULED: { bg: '#F3E8FF', text: '#7C3AED', dot: '#8B5CF6', pulse: false },
  DRAFT: { bg: '#F3F4F6', text: '#6B7280', dot: '#9CA3AF', pulse: false },
  RUNNING: { bg: '#DBEAFE', text: '#2563EB', dot: '#3B82F6', pulse: true },
  SENDING: { bg: '#DBEAFE', text: '#2563EB', dot: '#3B82F6', pulse: true },
  TRIAL: { bg: '#F3E8FF', text: '#7C3AED', dot: '#8B5CF6', pulse: false },
  PARTIALLY_PAID: { bg: '#F3E8FF', text: '#7C3AED', dot: '#8B5CF6', pulse: false },
  PROCESSING: { bg: '#DBEAFE', text: '#2563EB', dot: '#3B82F6', pulse: true },

  // Warning states
  PAUSED: { bg: '#FEF3C7', text: '#D97706', dot: '#F59E0B', pulse: false },
  HIGH: { bg: '#FEF3C7', text: '#D97706', dot: '#F59E0B', pulse: false },
  REFUNDED: { bg: '#FEF3C7', text: '#D97706', dot: '#F59E0B', pulse: false },

  // Error states
  FAILED: { bg: '#FFE4E6', text: '#E11D48', dot: '#F43F5E', pulse: false },
  CANCELLED: { bg: '#FFE4E6', text: '#E11D48', dot: '#F43F5E', pulse: false },
  BOUNCED: { bg: '#FFE4E6', text: '#E11D48', dot: '#F43F5E', pulse: false },
  CRITICAL: { bg: '#FFE4E6', text: '#E11D48', dot: '#F43F5E', pulse: true },
  EXPIRED: { bg: '#FFE4E6', text: '#E11D48', dot: '#F43F5E', pulse: false },
  ERROR: { bg: '#FFE4E6', text: '#E11D48', dot: '#F43F5E', pulse: false },
};

const DEFAULT_CONFIG = { bg: '#F3F4F6', text: '#6B7280', dot: '#9CA3AF', pulse: false };

export const StatusBadge = ({ status }) => {
  if (!status) return null;
  const s = String(status).toUpperCase();
  const config = STATUS_CONFIG[s] || DEFAULT_CONFIG;

  return (
    <span
      className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-[11px] font-semibold tracking-wide transition-all whitespace-nowrap"
      style={{
        backgroundColor: config.bg,
        color: config.text,
      }}
    >
      <span className="relative flex h-1.5 w-1.5">
        {config.pulse && (
          <span
            className="animate-ping absolute inline-flex h-full w-full rounded-full opacity-60"
            style={{ backgroundColor: config.dot }}
          />
        )}
        <span
          className="relative inline-flex rounded-full h-1.5 w-1.5"
          style={{ backgroundColor: config.dot }}
        />
      </span>
      {s}
    </span>
  );
};
