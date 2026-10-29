export interface ApiResult<T> {
  code: number
  message: string
  data: T
}

export interface HealthData {
  app: string
  status: string
  time: string
}

export interface LoginData {
  token: string
  username: string
}

export interface AdminProfile {
  id: number
  username: string
}
