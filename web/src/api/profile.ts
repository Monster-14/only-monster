import http from './http'
import type { ApiResult, SiteProfile } from './types'

export function fetchPublicProfile() {
  return http.get<ApiResult<SiteProfile>>('/public/profile')
}

export function fetchAdminProfile() {
  return http.get<ApiResult<SiteProfile>>('/admin/profile')
}

export function updateAdminProfile(payload: SiteProfile) {
  return http.put<ApiResult<SiteProfile>>('/admin/profile', payload)
}
