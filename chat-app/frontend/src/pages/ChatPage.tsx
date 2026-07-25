import { useState, useCallback } from 'react'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import * as api from '../api'
import type { Message, WsEnvelope } from '../types'
import { useAuth } from '../context/AuthContext'
import { useWebSocket } from '../hooks/useWebSocket'
import ConversationList from '../components/ConversationList'
import ChatWindow from '../components/ChatWindow'

export default function ChatPage() {
  const { token } = useAuth()
  const qc = useQueryClient()
  const [selectedConvId, setSelectedConvId] = useState<string | null>(null)

  const { data: conversations = [] } = useQuery({
    queryKey: ['conversations'],
    queryFn: api.getConversations,
  })

  const selectedConv = conversations.find((c) => c.id === selectedConvId) ?? null

  // Handle incoming WebSocket messages — update React Query cache directly
  const handleWsMessage = useCallback(
    (envelope: WsEnvelope) => {
      if (envelope.type === 'NEW_MESSAGE') {
        const msg = envelope.payload as Message

        // Append to the conversation's message list (create cache entry if missing)
        qc.setQueryData<Message[]>(['messages', msg.conversationId], (old = []) => {
          // Avoid duplicates (optimistic updates may already have it via REST response)
          if (old.some((m) => m.id === msg.id)) return old
          return [...old, msg]
        })

        // Bubble conversation to the top by invalidating the list
        qc.invalidateQueries({ queryKey: ['conversations'] })
      }
    },
    [qc]
  )

  useWebSocket({ token, onMessage: handleWsMessage })

  return (
    <div className="flex h-screen bg-gray-100 overflow-hidden">
      <ConversationList
        selectedId={selectedConvId}
        onSelect={setSelectedConvId}
      />

      <main className="flex-1 flex flex-col overflow-hidden">
        {selectedConv ? (
          <ChatWindow conversation={selectedConv} />
        ) : (
          <div className="flex-1 flex flex-col items-center justify-center text-gray-400">
            <svg className="w-16 h-16 mb-4 opacity-30" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5}
                d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
            </svg>
            <p className="text-sm">Select a conversation to start chatting</p>
          </div>
        )}
      </main>
    </div>
  )
}
