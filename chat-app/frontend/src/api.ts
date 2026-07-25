import axios from 'axios'
import type { AuthResponse, Conversation, Message, User } from './types'

const api = axios.create({ baseURL: '/api' })

// Attach JWT to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

// Redirect to login on 401
api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(err)
  }
)

// ── Auth ──────────────────────────────────────────────────────────────────────

export const register = (username: string, email: string, password: string) =>
  api.post<AuthResponse>('/auth/register', { username, email, password }).then((r) => r.data)

export const login = (username: string, password: string) =>
  api.post<AuthResponse>('/auth/login', { username, password }).then((r) => r.data)

// ── Users ─────────────────────────────────────────────────────────────────────

export const getMe = () =>
  api.get<User>('/users/me').then((r) => r.data)

export const searchUsers = (q: string) =>
  api.get<User[]>(`/users/search?q=${encodeURIComponent(q)}`).then((r) => r.data)

// ── Conversations ─────────────────────────────────────────────────────────────

export const getConversations = () =>
  api.get<Conversation[]>('/conversations').then((r) => r.data)

export const createDirectConversation = (recipientId: string) =>
  api.post<Conversation>('/conversations/direct', { recipientId }).then((r) => r.data)

export const createGroupConversation = (name: string, members: string[]) =>
  api.post<Conversation>('/conversations/group', { name, members }).then((r) => r.data)

// ── Messages ──────────────────────────────────────────────────────────────────

export const getMessages = (conversationId: string) =>
  api.get<Message[]>(`/messages?conversationId=${conversationId}`).then((r) => r.data)

export const sendMessage = (conversationId: string, content: string) =>
  api.post<Message>('/messages', { conversationId, content }).then((r) => r.data)

export default api
