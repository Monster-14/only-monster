import http from './http'
import type { ApiResult, HealthData } from './types'

export function fetchHealth() {
  return http.get<ApiResult<HealthData>>('/public/health')
}
