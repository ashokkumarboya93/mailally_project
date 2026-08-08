import React, { useState } from 'react';
import { StatusBadge } from '../../components/common/StatusBadge';
import { Modal } from '../../components/common/Modal';
import { useToast } from '../../components/common/Toast';
import { UserPlus, Shield, Mail, Trash2, Search, Check, SlidersHorizontal } from 'lucide-react';

export const UsersPage = () => {
  const [users, setUsers] = useState([
    { id: 1, email: 'admin@mailally.com', firstName: 'Admin', lastName: 'User', role: 'ADMIN', status: 'ACTIVE', joined: 'Aug 01, 2026' },
    { id: 2, email: 'sarah.connor@acme.com', firstName: 'Sarah', lastName: 'Connor', role: 'MANAGER', status: 'ACTIVE', joined: 'Aug 04, 2026' },
    { id: 3, email: 'dave.miller@acme.com', firstName: 'Dave', lastName: 'Miller', role: 'MEMBER', status: 'ACTIVE', joined: 'Aug 06, 2026' },
    { id: 4, email: 'alex.vance@acme.com', firstName: 'Alex', lastName: 'Vance', role: 'MEMBER', status: 'PENDING', joined: 'Aug 08, 2026' }
  ]);

  const [isInviteModalOpen, setIsInviteModalOpen] = useState(false);
  const [inviteEmail, setInviteEmail] = useState('');
  const [inviteRole, setInviteRole] = useState('MEMBER');
  const [search, setSearch] = useState('');
  const [selectedRoleFilter, setSelectedRoleFilter] = useState('ALL');
  const { addToast } = useToast();

  const handleSendInvite = (e) => {
    e.preventDefault();
    if (!inviteEmail || !inviteEmail.includes('@')) {
      addToast('Please enter a valid email address', 'error');
      return;
    }

    const newUser = {
      id: Date.now(),
      email: inviteEmail,
      firstName: inviteEmail.split('@')[0],
      lastName: 'Invited',
      role: inviteRole,
      status: 'PENDING',
      joined: 'Just now'
    };

    setUsers(prev => [newUser, ...prev]);
    setIsInviteModalOpen(false);
    setInviteEmail('');
    addToast(`Invitation sent to ${inviteEmail}!`, 'success');
  };

  const handleRevoke = (id, email) => {
    if (!window.confirm(`Are you sure you want to remove ${email} from your workspace?`)) return;
    setUsers(prev => prev.filter(u => u.id !== id));
    addToast(`Removed ${email} from workspace`, 'info');
  };

  const roleColors = {
    ADMIN: 'bg-[#FCE7F3] text-[#DB2777] border-[#FCE7F3]',
    MANAGER: 'bg-[#F3E8FF] text-[#7C3AED] border-[#DDD6FE]',
    MEMBER: 'bg-[#DCFCE7] text-[#15803D] border-[#BBF7D0]',
  };

  const filteredUsers = users.filter(u => {
    const matchesRole = selectedRoleFilter === 'ALL' || u.role === selectedRoleFilter;
    const matchesSearch = !search || u.email.toLowerCase().includes(search.toLowerCase()) || `${u.firstName} ${u.lastName}`.toLowerCase().includes(search.toLowerCase());
    return matchesRole && matchesSearch;
  });

  return (
    <div className="space-y-6 animate-fadeInUp pb-8 max-w-5xl font-sans">
      
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl font-extrabold tracking-tight text-[#18181B]">Users & Roles</h1>
          <p className="text-[13px] text-[#71717A] font-medium mt-1">
            Manage organization team seats, role-based access permissions, and invite members.
          </p>
        </div>

        <button
          onClick={() => setIsInviteModalOpen(true)}
          className="flex items-center gap-1.5 px-4 h-10 rounded-xl text-xs font-bold bg-[#18181B] text-white hover:bg-black transition-all cursor-pointer shadow-xs"
        >
          <UserPlus className="w-4 h-4" />
          Invite Team Member
        </button>
      </div>

      {/* Filter Bar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 bg-white p-2 rounded-[20px] border border-[#18181B] shadow-xs">
        <div className="flex items-center gap-1.5">
          {['ALL', 'ADMIN', 'MANAGER', 'MEMBER'].map(role => (
            <button
              key={role}
              onClick={() => setSelectedRoleFilter(role)}
              className={`px-3 py-1.5 rounded-xl text-xs font-bold transition-all cursor-pointer ${
                selectedRoleFilter === role
                  ? 'bg-[#18181B] text-white'
                  : 'text-[#71717A] hover:bg-[#FAFAFA] hover:text-[#18181B]'
              }`}
            >
              {role === 'ALL' ? 'All Members' : role}
            </button>
          ))}
        </div>

        <div className="relative w-full sm:w-60">
          <Search className="w-3.5 h-3.5 text-[#A1A1AA] absolute left-3 top-1/2 -translate-y-1/2" />
          <input
            type="text"
            placeholder="Search member email..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="w-full pl-9 pr-3 h-8 text-xs font-semibold bg-[#FAFAFA] border border-[#E4E4E7] rounded-xl outline-none focus:border-[#18181B]"
          />
        </div>
      </div>

      {/* User Cards Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
        {filteredUsers.map((u) => (
          <div
            key={u.id}
            className="bg-white rounded-[24px] border border-[#18181B] p-5 flex flex-col justify-between space-y-4 hover:shadow-md transition-all duration-300 group"
          >
            <div className="flex items-start justify-between">
              <div className="w-10 h-10 rounded-full bg-[#FAF5FF] border border-[#E9D5FF] text-[#7C3AED] flex items-center justify-center font-black text-sm shadow-xs">
                {u.firstName.charAt(0)}
              </div>
              <StatusBadge status={u.status} />
            </div>

            <div>
              <h3 className="font-extrabold text-[16px] text-[#18181B]">
                {u.firstName} {u.lastName}
              </h3>
              <p className="text-xs font-semibold mt-0.5 text-[#71717A] truncate">
                {u.email}
              </p>
            </div>

            <div className="pt-3 border-t border-[#E4E4E7] flex items-center justify-between">
              <span className={`px-3 py-1 rounded-full text-[10px] font-bold uppercase tracking-wider border ${roleColors[u.role] || 'bg-[#F4F4F6] text-[#71717A]'}`}>
                {u.role}
              </span>
              
              {u.role !== 'ADMIN' && (
                <button
                  type="button"
                  onClick={() => handleRevoke(u.id, u.email)}
                  className="p-1.5 rounded-lg text-[#A1A1AA] hover:text-rose-600 hover:bg-rose-50 transition-colors cursor-pointer"
                  title="Remove Member"
                >
                  <Trash2 className="w-4 h-4" />
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Invite Modal */}
      <Modal
        isOpen={isInviteModalOpen}
        onClose={() => setIsInviteModalOpen(false)}
        title="Invite Team Member"
      >
        <form onSubmit={handleSendInvite} className="space-y-4">
          <div>
            <label className="block text-xs font-bold text-[#18181B] mb-1.5">Work Email Address</label>
            <input
              type="email"
              value={inviteEmail}
              onChange={(e) => setInviteEmail(e.target.value)}
              placeholder="colleague@company.com"
              required
              className="w-full h-11 px-4 rounded-xl border border-[#18181B] text-xs font-semibold text-[#18181B] outline-none"
            />
          </div>

          <div>
            <label className="block text-xs font-bold text-[#18181B] mb-1.5">Assign Access Role</label>
            <select
              value={inviteRole}
              onChange={(e) => setInviteRole(e.target.value)}
              className="w-full h-11 px-4 rounded-xl border border-[#18181B] text-xs font-semibold text-[#18181B] bg-white outline-none"
            >
              <option value="MEMBER">Member (Create & send campaigns)</option>
              <option value="MANAGER">Manager (Full analytics & contact import)</option>
              <option value="ADMIN">Admin (Full billing & organization settings)</option>
            </select>
          </div>

          <div className="pt-2 flex justify-end gap-2">
            <button
              type="button"
              onClick={() => setIsInviteModalOpen(false)}
              className="px-4 h-10 rounded-xl border border-[#E4E4E7] text-xs font-bold text-[#71717A] hover:bg-[#FAFAFA]"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-5 h-10 rounded-xl bg-[#18181B] hover:bg-black text-white text-xs font-bold flex items-center gap-2"
            >
              <UserPlus className="w-4 h-4" /> Send Invite
            </button>
          </div>
        </form>
      </Modal>

    </div>
  );
};
