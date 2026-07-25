export interface User {
  id: string
  username: string
  email: string
  createdAt: string
}

export interface AuthResponse {
  token: string
  username: string
  userId: string
}

export interface Conversation {
  id: string
  type: 'DIRECT' | 'GROUP'
  name: string | null
  members: User[]
  createdAt: string
}

export interface Message {
  id: string
  conversationId: string
  sender: User
  content: string
  createdAt: string
  delivered: boolean
}

// WebSocket envelope types
export type WsMessageType = 'NEW_MESSAGE' | 'USER_ONLINE' | 'USER_OFFLINE'

export interface WsEnvelope {
  type: WsMessageType
  payload: Message | { userId: string }
  timestamp: string
}
