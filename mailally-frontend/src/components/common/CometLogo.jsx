import React from 'react';
import mainLogo from '../../../../images/main_logo.png';

export const CometLogo = ({ size = 'sm', className = '' }) => {
  const sizeMap = {
    xs: 'w-7 h-7',
    sm: 'w-9 h-9',
    md: 'w-11 h-11',
    lg: 'w-14 h-14',
    xl: 'w-18 h-18',
  };

  const dim = sizeMap[size] || sizeMap.sm;

  return (
    <img
      src={mainLogo}
      alt="MailAlly Main Logo"
      className={`${dim} ${className} flex-shrink-0 object-contain transition-transform duration-200 filter drop-shadow-xs hover:scale-105`}
    />
  );
};
