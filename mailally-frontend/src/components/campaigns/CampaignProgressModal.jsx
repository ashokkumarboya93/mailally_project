import React, { useState, useEffect, useRef, useCallback } from 'react';
import { X, Rocket, CheckCircle2, AlertTriangle, Clock, Zap, BarChart3, XCircle } from 'lucide-react';
import { campaignApi } from '../../api/campaignApi';

/**
 * Premium animated modal that shows live campaign dispatch progress.
 * Uses polling (with SSE-ready architecture) to stream real-time progress updates.
 */
export const CampaignProgressModal = ({ isOpen, onClose, campaignId, campaignName }) => {
  const [progress, setProgress] = useState({
    campaignId: null,
    campaignName: campaignName || 'Campaign',
    campaignStatus: 'RUNNING',
    totalRecipients: 0,
    sentCount: 0,
    failedCount: 0,
    pendingCount: 0,
    progressPercentage: 0,
  });
  const [startTime] = useState(Date.now());
  const [emailsPerSecond, setEmailsPerSecond] = useState(0);
  const [isCompleted, setIsCompleted] = useState(false);
  const [showCelebration, setShowCelebration] = useState(false);
  const pollRef = useRef(null);
  const prevSentRef = useRef(0);
  const speedTracker = useRef([]);

  const calculateSpeed = useCallback((sent) => {
    const now = Date.now();
    speedTracker.current.push({ time: now, count: sent });
    // Keep last 10 seconds of data
    speedTracker.current = speedTracker.current.filter(p => now - p.time < 10000);
    if (speedTracker.current.length >= 2) {
      const oldest = speedTracker.current[0];
      const newest = speedTracker.current[speedTracker.current.length - 1];
      const timeDiff = (newest.time - oldest.time) / 1000;
      const countDiff = newest.count - oldest.count;
      if (timeDiff > 0) {
        setEmailsPerSecond(Math.round((countDiff / timeDiff) * 10) / 10);
      }
    }
  }, []);

  useEffect(() => {
    if (!isOpen || !campaignId) return;

    const fetchProgress = async () => {
      try {
        const res = await campaignApi.getCampaignProgress(campaignId);
        if (res && res.data) {
          const data = res.data;
          setProgress(data);
          calculateSpeed(data.sentCount + data.failedCount);

          if (data.campaignStatus === 'COMPLETED' || data.campaignStatus === 'FAILED' || data.campaignStatus === 'CANCELLED') {
            setIsCompleted(true);
            if (data.campaignStatus === 'COMPLETED') {
              setShowCelebration(true);
              setTimeout(() => setShowCelebration(false), 3000);
            }
            if (pollRef.current) {
              clearInterval(pollRef.current);
              pollRef.current = null;
            }
          }
        }
      } catch (err) {
        console.error('Progress fetch error:', err);
      }
    };

    // Initial fetch
    fetchProgress();

    // Poll every 800ms for live updates
    pollRef.current = setInterval(fetchProgress, 800);

    return () => {
      if (pollRef.current) {
        clearInterval(pollRef.current);
        pollRef.current = null;
      }
    };
  }, [isOpen, campaignId, calculateSpeed]);

  if (!isOpen) return null;

  const elapsedSeconds = Math.floor((Date.now() - startTime) / 1000);
  const elapsedMinutes = Math.floor(elapsedSeconds / 60);
  const elapsedSecs = elapsedSeconds % 60;
  const elapsedDisplay = elapsedMinutes > 0
    ? `${elapsedMinutes}m ${elapsedSecs}s`
    : `${elapsedSecs}s`;

  const eta = emailsPerSecond > 0
    ? Math.ceil(progress.pendingCount / emailsPerSecond)
    : null;
  const etaDisplay = eta
    ? eta > 60
      ? `~${Math.floor(eta / 60)}m ${eta % 60}s`
      : `~${eta}s`
    : 'Calculating...';

  const progressPct = Math.min(progress.progressPercentage, 100);
  const circumference = 2 * Math.PI * 88;
  const strokeDashoffset = circumference - (progressPct / 100) * circumference;

  const statusConfig = {
    RUNNING: { color: '#3B82F6', label: 'Sending...', icon: Rocket, glow: '0 0 30px rgba(59, 130, 246, 0.3)' },
    COMPLETED: { color: '#10B981', label: 'Completed!', icon: CheckCircle2, glow: '0 0 30px rgba(16, 185, 129, 0.3)' },
    FAILED: { color: '#EF4444', label: 'Failed', icon: XCircle, glow: '0 0 30px rgba(239, 68, 68, 0.3)' },
    CANCELLED: { color: '#F59E0B', label: 'Cancelled', icon: AlertTriangle, glow: '0 0 30px rgba(245, 158, 11, 0.3)' },
  };

  const status = statusConfig[progress.campaignStatus] || statusConfig.RUNNING;
  const StatusIcon = status.icon;

  return (
    <div
      className="fixed inset-0 z-[9999] flex items-center justify-center"
      style={{ backgroundColor: 'rgba(0, 0, 0, 0.7)', backdropFilter: 'blur(8px)' }}
    >
      {/* Celebration particles */}
      {showCelebration && (
        <div className="absolute inset-0 pointer-events-none overflow-hidden">
          {Array.from({ length: 30 }).map((_, i) => (
            <div
              key={i}
              className="absolute w-2 h-2 rounded-full"
              style={{
                backgroundColor: ['#10B981', '#3B82F6', '#F59E0B', '#8B5CF6', '#EC4899'][i % 5],
                left: `${Math.random() * 100}%`,
                top: '-10px',
                animation: `confetti-fall ${1.5 + Math.random() * 2}s ease-out forwards`,
                animationDelay: `${Math.random() * 0.5}s`,
              }}
            />
          ))}
        </div>
      )}

      <div
        className="relative w-full max-w-lg mx-4 rounded-3xl overflow-hidden"
        style={{
          backgroundColor: 'var(--claude-surface)',
          border: '1px solid var(--claude-border)',
          boxShadow: `0 25px 60px rgba(0, 0, 0, 0.4), ${status.glow}`,
        }}
      >
        {/* Header */}
        <div
          className="px-6 py-4 flex items-center justify-between"
          style={{ borderBottom: '1px solid var(--claude-border)' }}
        >
          <div className="flex items-center gap-3">
            <div
              className="w-10 h-10 rounded-xl flex items-center justify-center"
              style={{
                background: `linear-gradient(135deg, ${status.color}22, ${status.color}44)`,
                border: `1px solid ${status.color}33`,
              }}
            >
              <StatusIcon className="w-5 h-5" style={{ color: status.color }} />
            </div>
            <div>
              <h3 className="font-bold text-sm" style={{ color: 'var(--claude-text)' }}>
                Campaign Progress
              </h3>
              <p className="text-[11px]" style={{ color: 'var(--claude-text-muted)' }}>
                {progress.campaignName || campaignName}
              </p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="w-8 h-8 rounded-lg flex items-center justify-center transition-all duration-200"
            style={{
              backgroundColor: 'var(--claude-surface-elevated)',
              color: 'var(--claude-text-muted)',
            }}
            onMouseEnter={e => e.currentTarget.style.backgroundColor = 'var(--claude-border)'}
            onMouseLeave={e => e.currentTarget.style.backgroundColor = 'var(--claude-surface-elevated)'}
          >
            <X className="w-4 h-4" />
          </button>
        </div>

        {/* Circular Progress */}
        <div className="px-6 py-8 flex flex-col items-center">
          <div className="relative w-48 h-48 mb-6">
            <svg className="w-full h-full transform -rotate-90" viewBox="0 0 200 200">
              {/* Background track */}
              <circle
                cx="100" cy="100" r="88"
                fill="none"
                stroke="var(--claude-border)"
                strokeWidth="10"
              />
              {/* Progress arc */}
              <circle
                cx="100" cy="100" r="88"
                fill="none"
                stroke={status.color}
                strokeWidth="10"
                strokeLinecap="round"
                strokeDasharray={circumference}
                strokeDashoffset={strokeDashoffset}
                style={{
                  transition: 'stroke-dashoffset 0.6s ease-out, stroke 0.3s ease',
                  filter: `drop-shadow(0 0 8px ${status.color}66)`,
                }}
              />
            </svg>
            {/* Center text */}
            <div className="absolute inset-0 flex flex-col items-center justify-center">
              <span
                className="text-3xl font-black tabular-nums"
                style={{ color: status.color }}
              >
                {Math.round(progressPct)}%
              </span>
              <span
                className="text-[10px] font-semibold uppercase tracking-wider mt-1"
                style={{ color: 'var(--claude-text-muted)' }}
              >
                {status.label}
              </span>
            </div>
          </div>

          {/* Linear progress bar (supplementary) */}
          <div className="w-full mb-6">
            <div
              className="w-full h-2 rounded-full overflow-hidden"
              style={{ backgroundColor: 'var(--claude-surface-elevated)' }}
            >
              <div
                className="h-full rounded-full"
                style={{
                  width: `${progressPct}%`,
                  backgroundColor: status.color,
                  transition: 'width 0.6s ease-out',
                  boxShadow: `0 0 12px ${status.color}55`,
                }}
              />
            </div>
          </div>

          {/* Stats Grid */}
          <div className="grid grid-cols-4 gap-3 w-full mb-6">
            <StatBox
              label="Total"
              value={progress.totalRecipients}
              icon={BarChart3}
              color="#8B5CF6"
            />
            <StatBox
              label="Sent"
              value={progress.sentCount}
              icon={CheckCircle2}
              color="#10B981"
            />
            <StatBox
              label="Failed"
              value={progress.failedCount}
              icon={XCircle}
              color="#EF4444"
            />
            <StatBox
              label="Pending"
              value={progress.pendingCount}
              icon={Clock}
              color="#F59E0B"
            />
          </div>

          {/* Speed & Time */}
          <div
            className="w-full grid grid-cols-3 gap-3 p-3 rounded-2xl"
            style={{ backgroundColor: 'var(--claude-surface-elevated)' }}
          >
            <div className="text-center">
              <div className="flex items-center justify-center gap-1 mb-1">
                <Zap className="w-3 h-3" style={{ color: '#F59E0B' }} />
                <span className="text-[10px] font-semibold" style={{ color: 'var(--claude-text-muted)' }}>
                  Speed
                </span>
              </div>
              <span className="text-sm font-bold tabular-nums" style={{ color: 'var(--claude-text)' }}>
                {emailsPerSecond}/s
              </span>
            </div>
            <div className="text-center" style={{ borderLeft: '1px solid var(--claude-border)', borderRight: '1px solid var(--claude-border)' }}>
              <div className="flex items-center justify-center gap-1 mb-1">
                <Clock className="w-3 h-3" style={{ color: '#3B82F6' }} />
                <span className="text-[10px] font-semibold" style={{ color: 'var(--claude-text-muted)' }}>
                  Elapsed
                </span>
              </div>
              <span className="text-sm font-bold tabular-nums" style={{ color: 'var(--claude-text)' }}>
                {elapsedDisplay}
              </span>
            </div>
            <div className="text-center">
              <div className="flex items-center justify-center gap-1 mb-1">
                <Rocket className="w-3 h-3" style={{ color: '#10B981' }} />
                <span className="text-[10px] font-semibold" style={{ color: 'var(--claude-text-muted)' }}>
                  ETA
                </span>
              </div>
              <span className="text-sm font-bold tabular-nums" style={{ color: 'var(--claude-text)' }}>
                {isCompleted ? 'Done' : etaDisplay}
              </span>
            </div>
          </div>
        </div>

        {/* Footer Actions */}
        <div
          className="px-6 py-4 flex items-center justify-between"
          style={{ borderTop: '1px solid var(--claude-border)' }}
        >
          {!isCompleted ? (
            <button
              onClick={async () => {
                try {
                  await campaignApi.cancelCampaign(campaignId);
                } catch (err) {
                  console.error('Cancel failed:', err);
                }
              }}
              className="px-4 py-2 rounded-xl text-xs font-semibold transition-all duration-200"
              style={{
                backgroundColor: 'rgba(239, 68, 68, 0.1)',
                color: '#EF4444',
                border: '1px solid rgba(239, 68, 68, 0.2)',
              }}
            >
              Cancel Campaign
            </button>
          ) : (
            <span className="text-xs font-semibold" style={{ color: status.color }}>
              {progress.campaignStatus === 'COMPLETED' ? '🎉 All emails dispatched!' : `Status: ${progress.campaignStatus}`}
            </span>
          )}
          <button
            onClick={onClose}
            className="px-4 py-2 rounded-xl text-xs font-semibold transition-all duration-200"
            style={{
              backgroundColor: 'var(--claude-surface-elevated)',
              color: 'var(--claude-text)',
              border: '1px solid var(--claude-border)',
            }}
          >
            {isCompleted ? 'View Analytics' : 'Run in Background'}
          </button>
        </div>
      </div>

      <style>{`
        @keyframes confetti-fall {
          0% { transform: translateY(0) rotate(0deg); opacity: 1; }
          100% { transform: translateY(100vh) rotate(720deg); opacity: 0; }
        }
      `}</style>
    </div>
  );
};

/** Individual stat box with animated counter */
const StatBox = ({ label, value, icon: Icon, color }) => {
  const [displayValue, setDisplayValue] = useState(0);

  useEffect(() => {
    // Animate counter
    const start = displayValue;
    const diff = value - start;
    if (diff === 0) return;

    const duration = 400;
    const startTime = performance.now();

    const animate = (now) => {
      const elapsed = now - startTime;
      const progress = Math.min(elapsed / duration, 1);
      // Ease out
      const eased = 1 - Math.pow(1 - progress, 3);
      setDisplayValue(Math.round(start + diff * eased));
      if (progress < 1) {
        requestAnimationFrame(animate);
      }
    };
    requestAnimationFrame(animate);
  }, [value]);

  return (
    <div
      className="flex flex-col items-center p-3 rounded-2xl"
      style={{
        backgroundColor: `${color}08`,
        border: `1px solid ${color}15`,
      }}
    >
      <Icon className="w-4 h-4 mb-1" style={{ color }} />
      <span className="text-lg font-black tabular-nums" style={{ color }}>
        {displayValue.toLocaleString()}
      </span>
      <span className="text-[9px] font-semibold uppercase tracking-wider" style={{ color: 'var(--claude-text-muted)' }}>
        {label}
      </span>
    </div>
  );
};
