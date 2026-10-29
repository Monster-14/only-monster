import http from './http'
import type { ApiResult } from './types'

export function uploadImage(file: File) {
  const form = new FormData()
  form.append('file', file)
  return http.post<ApiResult<{ url: string }>>('/admin/files', form, {
    timeout: 30000,
  })
}
