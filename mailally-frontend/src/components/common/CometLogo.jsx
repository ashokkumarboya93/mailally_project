import React from 'react';

export const CometLogo = ({ size = 'md', className = '' }) => {
  const dimensions = {
    sm: 'w-7 h-7',
    md: 'w-10 h-10',
    lg: 'w-12 h-12',
    xl: 'w-16 h-16',
  }[size] || 'w-10 h-10';

  return (
    <div
      className={`${dimensions} flex items-center justify-center relative flex-shrink-0 transition-all duration-300 transform hover:scale-105 ${className}`}
    >
      <svg
        viewBox="0 0 100 100"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
        className="w-full h-full filter drop-shadow-[0_8px_16px_rgba(0,82,255,0.25)]"
      >
        <defs>
          {/* Main Left Pillar Gradient */}
          <linearGradient id="mPillarGradLeft" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stopColor="#0052FF" />
            <stop offset="100%" stopColor="#1F57F5" />
          </linearGradient>

          {/* Center Envelope Fold Cyan/Sky Gradient */}
          <linearGradient id="mFoldGradCenter" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stopColor="#00C6FF" />
            <stop offset="50%" stopColor="#00F0FF" />
            <stop offset="100%" stopColor="#2BAFF2" />
          </linearGradient>

          {/* Right Pillar Gradient */}
          <linearGradient id="mPillarGradRight" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stopColor="#0070FF" />
            <stop offset="100%" stopColor="#00E0FF" />
          </linearGradient>

          {/* Glowing Shadow Filter */}
          <filter id="glowCyan" x="-20%" y="-20%" width="140%" height="140%">
            <feGaussianBlur stdDeviation="3" result="blur" />
            <feComposite in="SourceGraphic" in2="blur" operator="over" />
          </filter>
        </defs>

        {/* Outer Hex / Rounded Square Backplate Shield */}
        <rect x="6" y="6" width="88" height="88" rx="26" fill="url(#mPillarGradLeft)" opacity="0.06" />

        {/* ─── INVENTED "M" LOGO RIBBON GEOMETRY ─── */}

        {/* Left Vertical Pillar */}
        <path
          d="M 22 22 C 22 17, 27 13, 32 13 C 37 13, 41 17, 41 22 L 41 78 C 41 83, 37 87, 32 87 C 27 87, 22 83, 22 78 Z"
          fill="url(#mPillarGradLeft)"
        />

        {/* Right Vertical Pillar */}
        <path
          d="M 59 22 C 59 17, 63 13, 68 13 C 73 13, 78 17, 78 22 L 78 78 C 78 83, 73 87, 68 87 C 63 87, 59 83, 59 78 Z"
          fill="url(#mPillarGradRight)"
        />

        {/* Left Diagonal Envelope Wing Fold */}
        <path
          d="M 31 20 L 50 48 L 40 55 L 22 28 Z"
          fill="url(#mFoldGradCenter)"
          opacity="0.9"
        />

        {/* Right Diagonal Envelope Wing Fold (Overlapping V) */}
        <path
          d="M 69 20 L 50 48 L 60 55 L 78 28 Z"
          fill="url(#mFoldGradCenter)"
          opacity="0.95"
        />

        {/* Center Dynamic Envelope Fold Point (V-Peak) */}
        <path
          d="M 30 18 C 30 18, 44 38, 50 47 C 56 38, 70 18, 70 18 C 74 21, 74 27, 70 32 L 54 56 C 52 59, 48 59, 46 56 L 30 32 C 26 27, 26 21, 30 18 Z"
          fill="url(#mFoldGradCenter)"
          filter="url(#glowCyan)"
        />

        {/* Core Electric Pulse Sparkle / Mail Node */}
        <circle cx="50" cy="50" r="5" fill="#FFFFFF" />
        <path d="M 50 42 L 50 58 M 42 50 L 58 50" stroke="#0052FF" strokeWidth="2" strokeLinecap="round" opacity="0.85" />
      </svg>
    </div>
  );
};
