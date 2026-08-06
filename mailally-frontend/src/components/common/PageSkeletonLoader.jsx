import React from 'react';

export const PageSkeletonLoader = ({ type = 'cards' }) => {
  return (
    <div className="p-6 space-y-6 max-w-7xl mx-auto animate-pulse font-sans">
      {/* Hero Header Skeleton */}
      <div className="h-28 rounded-3xl bg-gradient-to-r from-blue-100/80 via-blue-50/50 to-slate-100 border border-blue-100 flex items-center justify-between p-6">
        <div className="space-y-3 w-1/3">
          <div className="h-4 w-24 bg-blue-200/60 rounded-full" />
          <div className="h-7 w-48 bg-blue-300/40 rounded-xl" />
          <div className="h-3 w-64 bg-blue-200/40 rounded-lg" />
        </div>
        <div className="h-10 w-36 bg-blue-200/50 rounded-xl hidden sm:block" />
      </div>

      {/* Metric Cards Skeleton */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {[1, 2, 3, 4].map((i) => (
          <div key={i} className="bg-white border border-slate-100 p-5 rounded-2xl space-y-3 shadow-xs">
            <div className="flex justify-between items-center">
              <div className="h-3 w-20 bg-slate-100 rounded-md" />
              <div className="w-8 h-8 rounded-xl bg-blue-50" />
            </div>
            <div className="h-7 w-16 bg-slate-200/70 rounded-lg" />
            <div className="h-3 w-28 bg-slate-100 rounded-md" />
          </div>
        ))}
      </div>

      {/* Content Skeleton: Cards or Table */}
      {type === 'table' ? (
        <div className="bg-white border border-slate-100 rounded-3xl p-6 space-y-4 shadow-xs">
          <div className="h-4 w-32 bg-slate-200/60 rounded-md mb-2" />
          {[1, 2, 3, 4, 5].map((row) => (
            <div key={row} className="flex items-center justify-between py-3 border-b border-slate-50">
              <div className="flex items-center space-x-3 w-1/3">
                <div className="w-9 h-9 rounded-full bg-slate-100" />
                <div className="space-y-1.5 flex-1">
                  <div className="h-3.5 w-32 bg-slate-200/70 rounded-md" />
                  <div className="h-2.5 w-20 bg-slate-100 rounded-md" />
                </div>
              </div>
              <div className="h-3 w-24 bg-slate-100 rounded-md hidden md:block" />
              <div className="h-6 w-20 bg-blue-50 rounded-full" />
            </div>
          ))}
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
          {[1, 2, 3, 4].map((card) => (
            <div key={card} className="bg-white border border-slate-100 p-6 rounded-3xl space-y-4 shadow-xs">
              <div className="flex justify-between items-start">
                <div className="space-y-2 w-2/3">
                  <div className="h-5 w-40 bg-slate-200/80 rounded-lg" />
                  <div className="h-3 w-28 bg-slate-100 rounded-md" />
                </div>
                <div className="h-6 w-16 bg-slate-100 rounded-full" />
              </div>
              <div className="grid grid-cols-3 gap-2 p-3 bg-slate-50/80 rounded-2xl">
                <div className="h-6 bg-slate-200/50 rounded-lg" />
                <div className="h-6 bg-slate-200/50 rounded-lg" />
                <div className="h-6 bg-slate-200/50 rounded-lg" />
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
