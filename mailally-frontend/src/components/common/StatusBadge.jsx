import React from 'react';

export const StatusBadge = ({ status }) => {
  if (!status) return null;
  const s = String(status).toUpperCase();

  let bgColor = 'var(--claude-surface-elevated)';
  let textColor = 'var(--claude-text-secondary)';
  let borderColor = 'var(--claude-border)';
  let dotColor = '#9CA3AF';
  let animateDot = false;

  if (['ACTIVE', 'COMPLETED', 'PAID', 'SUBSCRIBED', 'DELIVERED', 'OPERATIONAL', 'CONNECTED'].includes(s)) {
    bgColor = 'rgba(16, 185, 129, 0.1)';
    textColor = '#10B981';
    borderColor = 'rgba(16, 185, 129, 0.2)';
    dotColor = '#10B981';
    if (s === 'ACTIVE' || s === 'OPERATIONAL') animateDot = true;
  } else if (['PENDING', 'SCHEDULED', 'DRAFT', 'RUNNING', 'TRIAL', 'PARTIALLY_PAID'].includes(s)) {
    bgColor = 'rgba(123, 97, 255, 0.1)';
    textColor = '#7B61FF';
    borderColor = 'rgba(123, 97, 255, 0.2)';
    dotColor = '#7B61FF';
    if (s === 'RUNNING') animateDot = true;
  } else if (['FAILED', 'CANCELLED', 'BOUNCED', 'CRITICAL', 'EXPIRED'].includes(s)) {
    bgColor = 'rgba(239, 68, 68, 0.1)';
    textColor = '#EF4444';
    borderColor = 'rgba(239, 68, 68, 0.2)';
    dotColor = '#EF4444';
  } else if (['HIGH', 'REFUNDED', 'PAUSED'].includes(s)) {
    bgColor = 'rgba(245, 158, 11, 0.1)';
    textColor = '#F59E0B';
    borderColor = 'rgba(245, 158, 11, 0.2)';
    dotColor = '#F59E0B';
  }

  return (
    <span
      className="inline-flex items-center px-3 py-1 rounded-full text-[11px] font-extrabold tracking-wide transition-all"
      style={{
        backgroundColor: bgColor,
        color: textColor,
        border: `1px solid ${borderColor}`,
      }}
    >
      <span className="relative flex h-2 w-2 mr-1.5">
        {animateDot && (
          <span
            className="animate-ping absolute inline-flex h-full w-full rounded-full opacity-75"
            style={{ backgroundColor: dotColor }}
          />
        )}
        <span
          className="relative inline-flex rounded-full h-2 w-2"
          style={{ backgroundColor: dotColor }}
        />
      </span>
      {s}
    </span>
  );
};
