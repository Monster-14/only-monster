import http from './http'
import type { ApiResult, ArticleItem } from './types'

export function fetchPublicArticles() {
  return http.get<ApiResult<ArticleItem[]>>('/public/articles')
}

export function fetchPublicArticle(slug: string) {
  return http.get<ApiResult<ArticleItem>>(`/public/articles/${slug}`)
}

export function fetchAdminArticles() {
  return http.get<ApiResult<ArticleItem[]>>('/admin/articles')
}

export function fetchAdminArticle(id: number) {
  return http.get<ApiResult<ArticleItem>>(`/admin/articles/${id}`)
}

export function createAdminArticle(payload: Omit<ArticleItem, 'id' | 'publishedAt'>) {
  return http.post<ApiResult<ArticleItem>>('/admin/articles', payload)
}

export function updateAdminArticle(id: number, payload: Omit<ArticleItem, 'id' | 'publishedAt'>) {
  return http.put<ApiResult<ArticleItem>>(`/admin/articles/${id}`, payload)
}

export function deleteAdminArticle(id: number) {
  return http.delete<ApiResult<void>>(`/admin/articles/${id}`)
}
