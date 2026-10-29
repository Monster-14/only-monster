import http from './http'
import type { ApiResult, ProjectItem } from './types'

export function fetchPublicProjects() {
  return http.get<ApiResult<ProjectItem[]>>('/public/projects')
}

export function fetchPublicProject(slug: string) {
  return http.get<ApiResult<ProjectItem>>(`/public/projects/${slug}`)
}

export function fetchAdminProjects() {
  return http.get<ApiResult<ProjectItem[]>>('/admin/projects')
}

export function fetchAdminProject(id: number) {
  return http.get<ApiResult<ProjectItem>>(`/admin/projects/${id}`)
}

export function createAdminProject(payload: Omit<ProjectItem, 'id'>) {
  return http.post<ApiResult<ProjectItem>>('/admin/projects', payload)
}

export function updateAdminProject(id: number, payload: Omit<ProjectItem, 'id'>) {
  return http.put<ApiResult<ProjectItem>>(`/admin/projects/${id}`, payload)
}

export function deleteAdminProject(id: number) {
  return http.delete<ApiResult<void>>(`/admin/projects/${id}`)
}
