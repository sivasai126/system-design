import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import * as api from '../api'
import type { Conversation, User } from '../types'
import { useAuth } from '../context/AuthContext'

interface Props {
  selectedId: string | null
  onSelect: (id: string) => void
}

export default function ConversationList({ selectedId, onSelect }: Props) {
  const { user, logout } = useAuth()
  const qc = useQueryClient()
  const [searchQ, setSearchQ] = useState('')
  const [searchResults, setSearchResults] = useState<User[]>([])
  const [showNewGroup, setShowNewGroup] = useState(false)
  const [groupName, setGroupName] = useState('')
  const [groupMembers, setGroupMembers] = useState<User[]>([])

  const { data: conversations = [] } = useQuery({
    queryKey: ['conversations'],
    queryFn: api.getConversations,
    refetchInterval: false,
  })

  const startDirect = useMutation({
    mutationFn: (recipientId: string) => api.createDirectConversation(recipientId),
    onSuccess: (conv) => {
      qc.invalidateQueries({ queryKey: ['conversations'] })
      setSearchQ('')
      setSearchResults([])
      onSelect(conv.id)
    },
  })

  const createGroup = useMutation({
    mutationFn: () =>
      api.createGroupConversation(groupName, groupMembers.map((m) => m.id)),
    onSuccess: (conv) => {
      qc.invalidateQueries({ queryKey: ['conversations'] })
      setShowNewGroup(false)
      setGroupName('')
      setGroupMembers([])
      onSelect(conv.id)
    },
  })

  async function handleSearch(q: string) {
    setSearchQ(q)
    if (q.trim().length < 1) { setSearchResults([]); return }
    const results = await api.searchUsers(q)
    setSearchResults(results)
  }

  function getConvLabel(conv: Conversation) {
    if (conv.type === 'GROUP') return conv.name ?? 'Group'
    const other = conv.members.find((m) => m.id !== user?.id)
    return other?.username ?? 'Direct'
  }

  return (
    <div className="flex flex-col h-full bg-white border-r border-gray-200 w-72 shrink-0">
      {/* Header */}
      <div className="flex items-center justify-between px-4 py-3 border-b border-gray-100">
        <span className="font-semibold text-gray-800">💬 {user?.username}</span>
        <button
          onClick={logout}
          className="text-xs text-gray-400 hover:text-red-500 transition"
        >
          Sign out
        </button>
      </div>

      {/* User search to start DM */}
      <div className="px-3 py-2 border-b border-gray-100">
        <input
          type="text"
          placeholder="Search users…"
          value={searchQ}
          onChange={(e) => handleSearch(e.target.value)}
          className="w-full text-sm border border-gray-200 rounded-lg px-3 py-1.5 focus:outline-none focus:ring-2 focus:ring-blue-400"
        />
        {searchResults.length > 0 && (
          <div className="mt-1 bg-white border border-gray-200 rounded-lg shadow-sm z-10">
            {searchResults.map((u) => (
              <button
                key={u.id}
                onClick={() => startDirect.mutate(u.id)}
                className="w-full text-left px-3 py-2 text-sm hover:bg-blue-50 flex items-center gap-2"
              >
                <Avatar name={u.username} size="sm" />
                {u.username}
              </button>
            ))}
          </div>
        )}
      </div>

      {/* New group button */}
      <div className="px-3 py-2 border-b border-gray-100">
        {!showNewGroup ? (
          <button
            onClick={() => setShowNewGroup(true)}
            className="text-xs text-blue-600 hover:text-blue-700 font-medium"
          >
            + New group
          </button>
        ) : (
          <div className="space-y-2">
            <input
              type="text"
              placeholder="Group name"
              value={groupName}
              onChange={(e) => setGroupName(e.target.value)}
              className="w-full text-sm border border-gray-200 rounded-lg px-3 py-1.5 focus:outline-none focus:ring-2 focus:ring-blue-400"
            />
            <GroupMemberPicker
              selected={groupMembers}
              onAdd={(u) => setGroupMembers((p) => [...p, u])}
              onRemove={(id) => setGroupMembers((p) => p.filter((m) => m.id !== id))}
            />
            <div className="flex gap-2">
              <button
                onClick={() => createGroup.mutate()}
                disabled={!groupName.trim() || groupMembers.length === 0}
                className="flex-1 text-xs bg-blue-600 hover:bg-blue-700 disabled:opacity-40 text-white rounded-lg py-1.5 font-medium transition"
              >
                Create
              </button>
              <button
                onClick={() => setShowNewGroup(false)}
                className="flex-1 text-xs border border-gray-300 rounded-lg py-1.5 text-gray-600 hover:bg-gray-50 transition"
              >
                Cancel
              </button>
            </div>
          </div>
        )}
      </div>

      {/* Conversation list */}
      <div className="flex-1 overflow-y-auto">
        {conversations.length === 0 && (
          <p className="text-center text-xs text-gray-400 mt-8">No conversations yet</p>
        )}
        {conversations.map((conv) => (
          <button
            key={conv.id}
            onClick={() => onSelect(conv.id)}
            className={`w-full flex items-center gap-3 px-4 py-3 text-left hover:bg-gray-50 transition ${
              selectedId === conv.id ? 'bg-blue-50 border-r-2 border-blue-500' : ''
            }`}
          >
            <Avatar name={getConvLabel(conv)} />
            <div className="min-w-0">
              <p className="text-sm font-medium text-gray-800 truncate">{getConvLabel(conv)}</p>
              <p className="text-xs text-gray-400">{conv.type === 'GROUP' ? 'Group' : 'Direct'}</p>
            </div>
          </button>
        ))}
      </div>
    </div>
  )
}

// ── Small sub-components ──────────────────────────────────────────────────────

function Avatar({ name, size = 'md' }: { name: string; size?: 'sm' | 'md' }) {
  const colors = [
    'bg-blue-400', 'bg-green-400', 'bg-purple-400',
    'bg-pink-400', 'bg-yellow-400', 'bg-indigo-400',
  ]
  const color = colors[name.charCodeAt(0) % colors.length]
  const cls = size === 'sm' ? 'w-6 h-6 text-xs' : 'w-9 h-9 text-sm'
  return (
    <div className={`${cls} ${color} rounded-full flex items-center justify-center text-white font-semibold shrink-0`}>
      {name.charAt(0).toUpperCase()}
    </div>
  )
}

function GroupMemberPicker({
  selected,
  onAdd,
  onRemove,
}: {
  selected: User[]
  onAdd: (u: User) => void
  onRemove: (id: string) => void
}) {
  const [q, setQ] = useState('')
  const [results, setResults] = useState<User[]>([])

  async function search(val: string) {
    setQ(val)
    if (val.trim().length < 1) { setResults([]); return }
    const r = await api.searchUsers(val)
    setResults(r.filter((u) => !selected.find((s) => s.id === u.id)))
  }

  return (
    <div>
      {selected.length > 0 && (
        <div className="flex flex-wrap gap-1 mb-1">
          {selected.map((u) => (
            <span key={u.id} className="flex items-center gap-1 bg-blue-100 text-blue-700 text-xs rounded-full px-2 py-0.5">
              {u.username}
              <button onClick={() => onRemove(u.id)} className="text-blue-400 hover:text-blue-700">×</button>
            </span>
          ))}
        </div>
      )}
      <input
        type="text"
        placeholder="Add members…"
        value={q}
        onChange={(e) => search(e.target.value)}
        className="w-full text-sm border border-gray-200 rounded-lg px-3 py-1.5 focus:outline-none focus:ring-2 focus:ring-blue-400"
      />
      {results.length > 0 && (
        <div className="mt-1 border border-gray-200 rounded-lg bg-white shadow-sm">
          {results.map((u) => (
            <button
              key={u.id}
              onClick={() => { onAdd(u); setQ(''); setResults([]) }}
              className="w-full text-left px-3 py-1.5 text-sm hover:bg-blue-50"
            >
              {u.username}
            </button>
          ))}
        </div>
      )}
    </div>
  )
}
