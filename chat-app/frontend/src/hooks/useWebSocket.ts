import { useEffect, useRef, useCallback } from 'react'
import type { WsEnvelope } from '../types'

interface UseWebSocketOptions {
  token: string | null
  onMessage: (envelope: WsEnvelope) => void
}

const RECONNECT_DELAY_MS = 3000

export function useWebSocket({ token, onMessage }: UseWebSocketOptions) {
  const wsRef = useRef<WebSocket | null>(null)
  const reconnectTimer = useRef<ReturnType<typeof setTimeout> | null>(null)
  const unmountedRef = useRef(false)
  const onMessageRef = useRef(onMessage)
  onMessageRef.current = onMessage  // always use latest callback without re-subscribing

  const connect = useCallback(() => {
    if (!token || unmountedRef.current) return

    const ws = new WebSocket(`ws://localhost:8080/ws/chat?token=${token}`)
    wsRef.current = ws

    ws.onopen = () => {
      console.log('[WS] Connected')
    }

    ws.onmessage = (event) => {
      try {
        const envelope: WsEnvelope = JSON.parse(event.data)
        onMessageRef.current(envelope)
      } catch (e) {
        console.warn('[WS] Failed to parse message', e)
      }
    }

    ws.onclose = (event) => {
      console.log('[WS] Closed', event.code, event.reason)
      if (!unmountedRef.current && token) {
        reconnectTimer.current = setTimeout(connect, RECONNECT_DELAY_MS)
      }
    }

    ws.onerror = (err) => {
      console.warn('[WS] Error', err)
    }
  }, [token])

  useEffect(() => {
    unmountedRef.current = false
    connect()

    return () => {
      unmountedRef.current = true
      if (reconnectTimer.current) clearTimeout(reconnectTimer.current)
      wsRef.current?.close(1000, 'component unmounted')
    }
  }, [connect])
}
