import React from 'react';

const SkeletonBlock = ({ className }) => (
  <div className={`rounded-lg bg-[#F3F4F6] animate-shimmer ${className}`} />
);

export const PageSkeletonLoader = ({ type = 'cards' }) => {
  return (
    <div className="space-y-6 animate-fadeIn">
      {/* Page Header Skeleton */}
      <div className="flex items-center justify-between">
        <div className="space-y-2">
          <SkeletonBlock className="h-7 w-48" />
          <SkeletonBlock className="h-4 w-72" />
        </div>
        <SkeletonBlock className="h-10 w-36 rounded-xl hidden sm:block" />
      </div>

      {/* Metric Cards Skeleton */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
        {[1, 2, 3, 4].map((i) => (
          <div key={i} className="bg-white border border-[#E5E5E7] p-5 rounded-[16px] space-y-3">
            <div className="flex justify-between items-center">
              <SkeletonBlock className="h-3 w-20" />
              <SkeletonBlock className="w-9 h-9 rounded-[10px]" />
            </div>
            <SkeletonBlock className="h-7 w-20" />
            <SkeletonBlock className="h-3 w-28" />
          </div>
        ))}
      </div>

      {/* Content Skeleton: Cards or Table */}
      {type === 'table' ? (
        <div className="bg-white border border-[#E5E5E7] rounded-[16px] overflow-hidden">
          {/* Table Header */}
          <div className="px-5 py-3 border-b border-[#F0F0F2] flex items-center gap-6">
            <SkeletonBlock className="h-3 w-16" />
            <SkeletonBlock className="h-3 w-24" />
            <SkeletonBlock className="h-3 w-20" />
            <SkeletonBlock className="h-3 w-16" />
            <SkeletonBlock className="h-3 w-20" />
          </div>
          {/* Table Rows */}
          {[1, 2, 3, 4, 5].map((row) => (
            <div key={row} className="flex items-center px-5 py-4 border-b border-[#F0F0F2] last:border-0 gap-6">
              <div className="flex items-center gap-3 flex-1">
                <SkeletonBlock className="w-8 h-8 rounded-full flex-shrink-0" />
                <div className="space-y-1.5 flex-1">
                  <SkeletonBlock className="h-3.5 w-32" />
                  <SkeletonBlock className="h-2.5 w-44" />
                </div>
              </div>
              <SkeletonBlock className="h-3 w-24 hidden md:block" />
              <SkeletonBlock className="h-6 w-16 rounded-full" />
            </div>
          ))}
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {[1, 2, 3, 4].map((card) => (
            <div key={card} className="bg-white border border-[#E5E5E7] p-5 rounded-[16px] space-y-4">
              <div className="flex justify-between items-start">
                <div className="space-y-2 flex-1">
                  <SkeletonBlock className="h-4 w-40" />
                  <SkeletonBlock className="h-3 w-56" />
                </div>
                <SkeletonBlock className="h-6 w-16 rounded-full flex-shrink-0" />
              </div>
              <div className="grid grid-cols-3 gap-3">
                <SkeletonBlock className="h-10 rounded-[10px]" />
                <SkeletonBlock className="h-10 rounded-[10px]" />
                <SkeletonBlock className="h-10 rounded-[10px]" />
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
