import React from 'react';
import { TrendingUp, TrendingDown, MoreHorizontal } from 'lucide-react';

export const StatCard = ({ title, value, change, isPositive, icon: Icon, description }) => {
  return (
    <div 
      className="bg-white rounded-[22px] p-6 border transition-all duration-300 hover:-translate-y-1 hover:shadow-xl relative overflow-hidden group"
      style={{
        borderColor: 'rgba(37,99,235,0.12)',
        boxShadow: '0 10px 30px -5px rgba(37,99,235,0.05)',
      }}
    >
      <div className="flex items-center justify-between">
        {Icon && (
          <div className="w-11 h-11 rounded-2xl bg-blue-50/80 text-[#2563EB] flex items-center justify-center flex-shrink-0 font-bold border border-blue-100/70 shadow-2xs group-hover:scale-105 transition-transform">
            <Icon className="w-5.5 h-5.5" />
          </div>
        )}
        <button className="text-slate-300 hover:text-slate-500 p-1 cursor-pointer transition-colors">
          <MoreHorizontal className="w-4 h-4" />
        </button>
      </div>

      <div className="mt-4">
        <span
          className="text-[11px] font-black uppercase tracking-wider text-slate-400 block"
          style={{ fontFamily: 'var(--font-heading)' }}
        >
          {title}
        </span>

        <div className="mt-2 flex items-baseline justify-between">
          <div
            className="text-3xl font-black tracking-tight text-[#1E3A8A]"
            style={{ fontFamily: 'var(--font-heading)' }}
          >
            {value}
          </div>
          {change && (
            <div
              className={`flex items-center px-2.5 py-1 rounded-full text-xs font-black ${
                isPositive !== false 
                  ? 'bg-blue-50 text-[#2563EB] border border-blue-100' 
                  : 'bg-rose-50 text-rose-600 border border-rose-100'
              }`}
            >
              {isPositive !== false ? (
                <TrendingUp className="w-3.5 h-3.5 mr-1" />
              ) : (
                <TrendingDown className="w-3.5 h-3.5 mr-1" />
              )}
              {change}
            </div>
          )}
        </div>
      </div>

      {description && (
        <p className="mt-3 text-xs truncate font-medium text-slate-400">
          {description}
        </p>
      )}
    </div>
  );
};
