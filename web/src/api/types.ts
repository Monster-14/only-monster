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

export interface SiteProfile {
  name: string
  headline: string
  bio: string
  avatarUrl: string
  email: string
  githubUrl: string
  blogUrl: string
  wechat: string
}

export interface ProjectItem {
  id: number
  slug: string
  title: string
  coverUrl: string
  summary: string
  content: string
  techStack: string[]
  demoUrl: string
  repoUrl: string
  status: number
  sort: number
}

export interface ArticleItem {
  id: number
  slug: string
  title: string
  coverUrl: string
  summary: string
  contentMd: string
  status: number
  publishedAt: string | null
}
