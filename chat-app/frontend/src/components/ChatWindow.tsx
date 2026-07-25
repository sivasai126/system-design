import { useEffect, useRef, useState, type FormEvent } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import * as api from '../api'
import type { Conversation, Message } from '../types'
import { useAuth } from '../context/AuthContext'

interface Props {
  conversation: Conversation
}

export default function ChatWindow({ conversation }: Props) {
  const { user } = useAuth()
  const qc = useQueryClient()
  const [text, setText] = useState('')
  const bottomRef = useRef<HTMLDivElement>(null)

  const { data: messages = [], isLoading } = useQuery({
    queryKey: ['messages', conversation.id],
    queryFn: () => api.getMessages(conversation.id),
  })

  const send = useMutation({
    mutationFn: (content: string) => api.sendMessage(conversation.id, content),
    onMutate: async (content) => {
      // Optimistic update — add message immediately before server responds
      await qc.cancelQueries({ queryKey: ['messages', conversation.id] })
      const optimistic: Message = {
        id: `optimistic-${Date.now()}`,
        conversationId: conversation.id,
        sender: user!,
        content,
        createdAt: new Date().toISOString(),
        delivered: false,
      }
      qc.setQueryData<Message[]>(['messages', conversation.id], (old = []) => [
        ...old,
        optimistic,
      ])
      return { optimistic }
    },
    onSuccess: (saved, _vars, ctx) => {
      // Replace optimistic message with the real one from server
      qc.setQueryData<Message[]>(['messages', conversation.id], (old = []) =>
        old.map((m) => (m.id === ctx?.optimistic.id ? saved : m))
      )
    },
    onError: (_err, _vars, ctx) => {
      // Roll back optimistic message
      qc.setQueryData<Message[]>(['messages', conversation.id], (old = []) =>
        old.filter((m) => m.id !== ctx?.optimistic.id)
      )
    },
  })

  // Scroll to bottom when messages change
  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages])

  function handleSubmit(e: FormEvent) {
    e.preventDefault()
    const content = text.trim()
    if (!content) return
    setText('')
    send.mutate(content)
  }

  function getTitle() {
    if (conversation.type === 'GROUP') return conversation.name ?? 'Group'
    return conversation.members.find((m) => m.id !== user?.id)?.username ?? 'Direct'
  }

  function formatTime(iso: string) {
    return new Date(iso).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
  }

  return (
    <div className="flex flex-col h-full">
      {/* Header */}
      <div className="flex items-center gap-3 px-5 py-3 border-b border-gray-200 bg-white">
        <div className="w-9 h-9 rounded-full bg-indigo-400 flex items-center justify-center text-white font-semibold text-sm shrink-0">
          {getTitle().charAt(0).toUpperCase()}
        </div>
        <div>
          <p className="font-semibold text-gray-800 text-sm">{getTitle()}</p>
          <p className="text-xs text-gray-400">
            {conversation.type === 'GROUP'
              ? `${conversation.members.length} members`
              : 'Direct message'}
          </p>
        </div>
      </div>

      {/* Messages */}
      <div className="flex-1 overflow-y-auto px-5 py-4 space-y-1 bg-gray-50">
        {isLoading && (
          <p className="text-center text-sm text-gray-400 mt-8">Loading…</p>
        )}
        {messages.map((msg, i) => {
          const isOwn = msg.sender.id === user?.id
          const prevMsg = messages[i - 1]
          const showSender = !isOwn && msg.sender.id !== prevMsg?.sender.id

          return (
            <div key={msg.id} className={`flex ${isOwn ? 'justify-end' : 'justify-start'}`}>
              <div className={`max-w-xs lg:max-w-md ${isOwn ? '' : 'items-start'}`}>
                {showSender && (
                  <p className="text-xs text-gray-400 mb-0.5 ml-1">{msg.sender.username}</p>
                )}
                <div
                  className={`px-3.5 py-2 rounded-2xl text-sm leading-relaxed ${
                    isOwn
                      ? 'bg-blue-600 text-white rounded-br-sm'
                      : 'bg-white text-gray-800 shadow-sm rounded-bl-sm'
                  } ${msg.id.startsWith('optimistic') ? 'opacity-60' : ''}`}
                >
                  {msg.content}
                </div>
                <p className={`text-xs mt-0.5 ${isOwn ? 'text-right text-gray-400' : 'text-gray-400'}`}>
                  {formatTime(msg.createdAt)}
                  {isOwn && (
                    <span className="ml-1">{msg.delivered ? '✓✓' : '✓'}</span>
                  )}
                </p>
              </div>
            </div>
          )
        })}
        <div ref={bottomRef} />
      </div>

      {/* Input */}
      <form
        onSubmit={handleSubmit}
        className="flex items-center gap-3 px-4 py-3 border-t border-gray-200 bg-white"
      >
        <input
          type="text"
          value={text}
          onChange={(e) => setText(e.target.value)}
          placeholder="Type a message…"
          className="flex-1 border border-gray-200 rounded-full px-4 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
          autoFocus
        />
        <button
          type="submit"
          disabled={!text.trim()}
          className="bg-blue-600 hover:bg-blue-700 disabled:opacity-40 text-white rounded-full w-9 h-9 flex items-center justify-center transition shrink-0"
          aria-label="Send message"
        >
          <svg className="w-4 h-4 rotate-45" fill="currentColor" viewBox="0 0 24 24">
            <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z" />
          </svg>
        </button>
      </form>
    </div>
  )
}
