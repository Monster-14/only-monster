/**
 * 履历补充资料。只放已核对的真实信息。
 * 空数组 / 空字符串的栏目不会出现在正式页面上。
 */
export interface ResumeEntry {
  id: string
  period: string
  title: string
  org?: string
  summary: string
}

export interface SkillGroup {
  category: string
  items: string[]
}

export interface ProjectHighlight {
  slug: string
  goal?: string
  contribution?: string
  outcome?: string
}

export const resumeMeta = {
  studioName: 'Monster 的数字工作室',
  /** 待补充：真实 PDF 简历。有文件后放到 public/resume.pdf 并填写此路径。 */
  pdfUrl: '',
}

/**
 * 待补充：教育经历（学校、专业、起止时间）。
 * 没有可公开的学历信息前保持为空。
 */
export const education: ResumeEntry[] = []

/**
 * 待补充：工作 / 实习经历（公司、岗位、起止时间、职责）。
 * 没有可核对的任职信息前保持为空。
 */
export const workExperience: ResumeEntry[] = []

/** 来自当前站点真实技术栈，不表示年限或熟练度百分比。 */
export const skillGroups: SkillGroup[] = [
  {
    category: '前端',
    items: ['Vue 3', 'TypeScript', 'Vite', 'Element Plus'],
  },
  {
    category: '后端',
    items: ['Java 17', 'Spring Boot', 'MyBatis-Plus', 'MySQL'],
  },
  {
    category: '工程',
    items: ['Git', 'JWT 鉴权', '本地文件上传'],
  },
]

/**
 * 按项目 slug 补充目标 / 贡献 / 成果。未填写的字段不展示。
 * 待补充：其它项目的真实目标、贡献和可核对成果。
 */
export const projectHighlights: ProjectHighlight[] = [
  {
    slug: 'monster-portfolio',
    goal: '做一个能自己维护资料、项目和文章的个人站点。',
    contribution: '独立完成前台、后台管理和本地图片上传。',
    outcome: '已支持资料、项目、文章、留言和图片上传。',
  },
]

export function highlightFor(slug: string) {
  return projectHighlights.find((item) => item.slug === slug)
}
