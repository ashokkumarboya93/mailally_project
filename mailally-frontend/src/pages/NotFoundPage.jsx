import React from 'react';
import { Link } from 'react-router-dom';
import { AlertCircle, ArrowLeft } from 'lucide-react';

export const NotFoundPage = () => {
  return (
    <div
      className="min-h-screen flex items-center justify-center p-4 transition-colors duration-300"
      style={{ backgroundColor: 'var(--claude-bg)' }}
    >
      <div className="text-center max-w-md space-y-5 animate-scaleIn">
        <div
          className="w-20 h-20 rounded-3xl flex items-center justify-center mx-auto shadow-lg"
          style={{ backgroundColor: 'var(--claude-primary-soft)', color: 'var(--claude-primary)' }}
        >
          <AlertCircle className="w-10 h-10" />
        </div>
        <h1
          className="text-6xl font-extrabold tracking-tight"
          style={{ color: 'var(--claude-primary)', fontFamily: 'var(--font-heading)' }}
        >
          404
        </h1>
        <h2 className="text-xl font-bold" style={{ color: 'var(--claude-text)', fontFamily: 'var(--font-heading)' }}>
          Page Not Found
        </h2>
        <p className="text-xs leading-relaxed" style={{ color: 'var(--claude-text-muted)' }}>
          The requested page route does not exist or has been relocated within the workspace.
        </p>
        <Link
          to="/dashboard"
          className="claude-btn-primary inline-flex py-3.5 px-6 mt-2"
        >
          <ArrowLeft className="w-4 h-4" />
          <span>Return to Dashboard</span>
        </Link>
      </div>
    </div>
  );
};
