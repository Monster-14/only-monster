import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import type { ApiResult } from './types'

const http = axios.create({
  baseURL: '/api',
  timeout: 10000,
})

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('monster_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  if (config.data instanceof FormData) {
    delete config.headers['Content-Type']
  }
  return config
})

http.interceptors.response.use(
  (response) => {
    const payload = response.data as ApiResult<unknown>
    if (payload && typeof payload.code === 'number' && payload.code !== 0) {
      ElMessage.error(payload.message || '请求失败')
      return Promise.reject(new Error(payload.message))
    }
    return response
  },
  (error) => {
    const status = error.response?.status
    const message = error.response?.data?.message || error.message || '网络异常'
    if (status === 401) {
      const auth = useAuthStore()
      auth.clearSession()
      if (!window.location.pathname.startsWith('/admin/login')) {
        window.location.assign('/admin/login')
      }
    }
    ElMessage.error(message)
    return Promise.reject(error)
  },
)

export default http
