import React from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowLeft, Home } from 'lucide-react';

export const NotFoundPage = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen flex items-center justify-center bg-[#FCFCFD] px-6">
      <div className="text-center max-w-md animate-fadeInUp">
        {/* 404 Number */}
        <div className="text-[120px] font-extrabold leading-none tracking-tighter text-[#E5E5E7] select-none">
          404
        </div>

        <h1 className="text-2xl font-extrabold text-[#0A0A0B] tracking-tight mt-2">
          Page not found
        </h1>
        <p className="text-[14px] text-[#9CA3AF] font-medium mt-2 leading-relaxed">
          The page you're looking for doesn't exist or has been moved.
        </p>

        <div className="flex items-center justify-center gap-3 mt-6">
          <button
            onClick={() => navigate(-1)}
            className="ma-btn ma-btn-secondary gap-1.5"
          >
            <ArrowLeft className="w-4 h-4" />
            Go Back
          </button>
          <button
            onClick={() => navigate('/dashboard')}
            className="ma-btn ma-btn-primary gap-1.5"
          >
            <Home className="w-4 h-4" />
            Dashboard
          </button>
        </div>
      </div>
    </div>
  );
};
