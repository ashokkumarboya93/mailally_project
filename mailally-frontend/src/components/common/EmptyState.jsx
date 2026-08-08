import React from 'react';
import { Inbox } from 'lucide-react';

export const EmptyState = ({
  icon: Icon = Inbox,
  title = 'No data yet',
  description = 'Get started by creating your first item.',
  actionLabel,
  onAction,
  accentColor = 'pink', // pink | purple | blue | green | orange
}) => {
  const accents = {
    pink: { iconBg: 'bg-[#FCE7F3]', iconColor: 'text-[#EC4899]' },
    purple: { iconBg: 'bg-[#F3E8FF]', iconColor: 'text-[#A855F7]' },
    blue: { iconBg: 'bg-[#DBEAFE]', iconColor: 'text-[#3B82F6]' },
    green: { iconBg: 'bg-[#DCFCE7]', iconColor: 'text-[#22C55E]' },
    orange: { iconBg: 'bg-[#FFEDD5]', iconColor: 'text-[#F97316]' },
  };

  const accent = accents[accentColor] || accents.pink;

  return (
    <div className="flex flex-col items-center justify-center py-16 px-6 text-center animate-fadeIn">
      {/* Decorative Background Blob */}
      <div className="relative mb-6">
        <div className={`absolute -inset-4 ${accent.iconBg} rounded-full blur-xl opacity-40`} />
        <div className={`relative w-14 h-14 rounded-2xl ${accent.iconBg} ${accent.iconColor} flex items-center justify-center`}>
          <Icon className="w-6 h-6" strokeWidth={1.5} />
        </div>
      </div>

      <h3 className="text-lg font-bold text-[#0A0A0B] tracking-tight mb-1.5">
        {title}
      </h3>

      <p className="text-[13px] text-[#9CA3AF] font-medium max-w-sm leading-relaxed mb-5">
        {description}
      </p>

      {actionLabel && onAction && (
        <button
          onClick={onAction}
          className="ma-btn ma-btn-primary text-[13px] gap-2"
        >
          {actionLabel}
        </button>
      )}
    </div>
  );
};
