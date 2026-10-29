import http from './http'
import type { AdminProfile, ApiResult, LoginData } from './types'

export function login(username: string, password: string) {
  return http.post<ApiResult<LoginData>>('/auth/login', { username, password })
}

export function fetchAdminMe() {
  return http.get<ApiResult<AdminProfile>>('/admin/me')
}
