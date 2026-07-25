import { createContext, useContext, useState, useCallback, type ReactNode } from 'react'
import * as api from '../api'
import type { User } from '../types'

interface AuthState {
  token: string | null
  user: User | null
}

interface AuthContextValue extends AuthState {
  login: (username: string, password: string) => Promise<void>
  register: (username: string, email: string, password: string) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthContextValue | null>(null)

function loadState(): AuthState {
  try {
    return {
      token: localStorage.getItem('token'),
      user: JSON.parse(localStorage.getItem('user') ?? 'null'),
    }
  } catch {
    return { token: null, user: null }
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [state, setState] = useState<AuthState>(loadState)

  const persist = useCallback((token: string, user: User) => {
    localStorage.setItem('token', token)
    localStorage.setItem('user', JSON.stringify(user))
    setState({ token, user })
  }, [])

  const loginFn = useCallback(async (username: string, password: string) => {
    const res = await api.login(username, password)
    const user: User = { id: res.userId, username: res.username, email: '', createdAt: '' }
    persist(res.token, user)
  }, [persist])

  const registerFn = useCallback(async (username: string, email: string, password: string) => {
    const res = await api.register(username, email, password)
    const user: User = { id: res.userId, username: res.username, email, createdAt: '' }
    persist(res.token, user)
  }, [persist])

  const logout = useCallback(() => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    setState({ token: null, user: null })
  }, [])

  return (
    <AuthContext.Provider value={{ ...state, login: loginFn, register: registerFn, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
