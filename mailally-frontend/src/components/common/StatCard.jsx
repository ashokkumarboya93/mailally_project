import React from 'react';
import { TrendingUp, TrendingDown } from 'lucide-react';

export const StatCard = ({ title, value, change, isPositive, icon: Icon, description, accentColor }) => {
  // Determine accent color for the icon background
  const accents = {
    pink: { bg: 'bg-[#FCE7F3]', text: 'text-[#EC4899]', border: 'border-[#FCE7F3]' },
    purple: { bg: 'bg-[#F3E8FF]', text: 'text-[#A855F7]', border: 'border-[#F3E8FF]' },
    green: { bg: 'bg-[#DCFCE7]', text: 'text-[#22C55E]', border: 'border-[#DCFCE7]' },
    blue: { bg: 'bg-[#DBEAFE]', text: 'text-[#3B82F6]', border: 'border-[#DBEAFE]' },
    orange: { bg: 'bg-[#FFEDD5]', text: 'text-[#F97316]', border: 'border-[#FFEDD5]' },
    cyan: { bg: 'bg-[#CFFAFE]', text: 'text-[#06B6D4]', border: 'border-[#CFFAFE]' },
    default: { bg: 'bg-[#F3F4F6]', text: 'text-[#6B7280]', border: 'border-[#F3F4F6]' },
  };

  const accent = accents[accentColor] || accents.default;

  return (
    <div className="bg-white rounded-[16px] p-5 border border-[#E5E5E7] transition-all duration-200 hover:-translate-y-0.5 hover:shadow-[0_12px_36px_rgba(0,0,0,0.06)] relative group">
      <div className="flex items-start justify-between mb-4">
        <p className="text-[11px] font-semibold uppercase tracking-[0.05em] text-[#9CA3AF]">
          {title}
        </p>
        {Icon && (
          <div className={`w-9 h-9 rounded-[10px] ${accent.bg} ${accent.text} flex items-center justify-center flex-shrink-0 transition-transform duration-200 group-hover:scale-105`}>
            <Icon className="w-[18px] h-[18px]" strokeWidth={1.8} />
          </div>
        )}
      </div>

      <div className="flex items-end justify-between">
        <div className="text-[28px] font-extrabold tracking-tight text-[#0A0A0B] leading-none animate-countUp">
          {value}
        </div>
        {change && (
          <div
            className={`flex items-center gap-1 px-2 py-0.5 rounded-full text-[11px] font-semibold ${
              isPositive !== false
                ? 'bg-[#DCFCE7] text-[#16A34A]'
                : 'bg-[#FFE4E6] text-[#E11D48]'
            }`}
          >
            {isPositive !== false ? (
              <TrendingUp className="w-3 h-3" />
            ) : (
              <TrendingDown className="w-3 h-3" />
            )}
            {change}
          </div>
        )}
      </div>

      {description && (
        <p className="mt-2 text-[12px] font-medium text-[#9CA3AF] truncate">
          {description}
        </p>
      )}
    </div>
  );
};
