import http from './http'
import type { ApiResult, MessageItem } from './types'

export function submitMessage(payload: { nickname: string; email: string; content: string }) {
  return http.post<ApiResult<void>>('/public/messages', payload)
}

export function fetchAdminMessages() {
  return http.get<ApiResult<MessageItem[]>>('/admin/messages')
}

export function fetchUnreadCount() {
  return http.get<ApiResult<{ count: number }>>('/admin/messages/unread-count')
}

export function markMessageRead(id: number) {
  return http.put<ApiResult<void>>(`/admin/messages/${id}/read`)
}

export function deleteAdminMessage(id: number) {
  return http.delete<ApiResult<void>>(`/admin/messages/${id}`)
}
