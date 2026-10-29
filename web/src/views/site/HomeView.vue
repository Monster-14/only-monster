<template>
  <div class="home">
    <section id="intro" class="hero">
      <div class="copy">
        <p class="kicker">{{ resumeMeta.studioName }}</p>
        <template v-if="profile">
          <h1>{{ profile.name }}</h1>
          <p class="role">{{ profile.headline || '个人站点' }}</p>
          <p v-if="profile.bio" class="lead">{{ profile.bio }}</p>
        </template>
        <template v-else-if="loading">
          <div class="skeleton title" />
          <div class="skeleton line" />
          <div class="skeleton line short" />
        </template>
        <p v-else class="lead">{{ loadError || '介绍暂时无法加载，可从上方导航继续浏览。' }}</p>
        <div class="actions">
          <a class="btn primary" href="#projects">查看项目</a>
          <button class="btn ghost" type="button" @click="openResume">个人履历</button>
        </div>
      </div>
      <StudioWorkbench :resume-open="resumeOpen" @action="onStageAction" />
    </section>

    <section id="projects" class="band">
      <div class="band-head">
        <h2>精选项目</h2>
        <router-link to="/projects">全部项目</router-link>
      </div>
      <div v-if="featuredProjects.length" class="project-list">
        <article v-for="item in featuredProjects" :key="item.id" class="project">
          <img v-if="item.coverUrl" :src="item.coverUrl" :alt="item.title" />
          <div>
            <h3>
              <router-link :to="`/projects/${item.slug}`">{{ item.title }}</router-link>
            </h3>
            <p>{{ item.summary }}</p>
            <p v-if="highlightFor(item.slug)?.goal"><span>目标</span>{{ highlightFor(item.slug)?.goal }}</p>
            <p v-if="highlightFor(item.slug)?.contribution"><span>贡献</span>{{ highlightFor(item.slug)?.contribution }}</p>
            <p v-if="highlightFor(item.slug)?.outcome"><span>成果</span>{{ highlightFor(item.slug)?.outcome }}</p>
            <div v-if="item.techStack.length" class="tags">
              <span v-for="tag in item.techStack" :key="tag">{{ tag }}</span>
            </div>
            <div class="links">
              <a v-if="item.demoUrl" :href="item.demoUrl" target="_blank" rel="noreferrer">演示</a>
              <a v-if="item.repoUrl" :href="item.repoUrl" target="_blank" rel="noreferrer">仓库</a>
            </div>
          </div>
        </article>
      </div>
      <p v-else class="empty">{{ loading ? '正在读取项目…' : '还没有已发布的项目。' }}</p>
    </section>

    <section id="resume" class="band resume-band">
      <div class="band-head">
        <h2>履历与技能</h2>
        <button class="text-btn" type="button" @click="openResume">打开履历</button>
      </div>
      <p class="muted">文件夹点开后可以看到概况、技能和联系方式。教育和工作经历有真实资料后再展示。</p>
      <div v-if="skillGroups.length" class="skill-row">
        <div v-for="group in skillGroups" :key="group.category">
          <h3>{{ group.category }}</h3>
          <p>{{ group.items.join('、') }}</p>
        </div>
      </div>
    </section>

    <section id="articles" class="band">
      <div class="band-head">
        <h2>技术文章</h2>
        <router-link to="/blog">全部文章</router-link>
      </div>
      <div v-if="articles.length" class="article-list">
        <router-link v-for="item in articles" :key="item.id" class="article" :to="`/blog/${item.slug}`">
          <h3>{{ item.title }}</h3>
          <p>{{ item.summary }}</p>
        </router-link>
      </div>
      <p v-else class="empty">{{ loading ? '正在读取文章…' : '还没有已发布的文章。' }}</p>
    </section>

    <section id="contact" class="band contact-band">
      <div class="band-head">
        <h2>联系方式</h2>
        <router-link to="/contact">打开留言页</router-link>
      </div>
      <div v-if="profile" class="contact">
        <a v-if="profile.email" :href="`mailto:${profile.email}`">{{ profile.email }}</a>
        <a v-if="profile.githubUrl" :href="profile.githubUrl" target="_blank" rel="noreferrer">GitHub</a>
        <span v-if="profile.wechat">微信：{{ profile.wechat }}</span>
      </div>
      <p class="muted">合作或打招呼可以直接留言，不必编造不存在的联系渠道。</p>
    </section>

    <ResumePanel
      :open="resumeOpen"
      :profile="profile"
      :projects="projects"
      @close="closeResume"
    />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { fetchPublicProfile } from '@/api/profile'
import { fetchPublicProjects } from '@/api/project'
import { fetchPublicArticles } from '@/api/article'
import { highlightFor, resumeMeta, skillGroups } from '@/data/resume'
import StudioWorkbench from '@/components/site/StudioWorkbench.vue'
import ResumePanel from '@/components/site/ResumePanel.vue'
import type { StudioAction } from '@/scene/studioScene'
import type { ArticleItem, ProjectItem, SiteProfile } from '@/api/types'

const route = useRoute()
const router = useRouter()
const loading = ref(true)
const loadError = ref('')
const resumeOpen = ref(false)
const profile = ref<SiteProfile | null>(null)
const projects = ref<ProjectItem[]>([])
const articles = ref<ArticleItem[]>([])
const featuredProjects = ref<ProjectItem[]>([])

function openResume() {
  resumeOpen.value = true
  if (route.hash !== '#resume') {
    router.replace({ hash: '#resume' })
  }
}

function closeResume() {
  resumeOpen.value = false
  if (route.hash === '#resume') {
    router.replace({ hash: '' })
  }
}

function onStageAction(action: StudioAction) {
  if (action === 'resume') {
    openResume()
    return
  }
  if (action === 'projects') {
    document.getElementById('projects')?.scrollIntoView({ behavior: 'smooth' })
    return
  }
  document.getElementById('articles')?.scrollIntoView({ behavior: 'smooth' })
}

watch(
  () => route.hash,
  (hash) => {
    if (hash === '#resume') {
      resumeOpen.value = true
    }
  },
  { immediate: true },
)

onMounted(async () => {
  try {
    const [profileRes, projectRes, articleRes] = await Promise.all([
      fetchPublicProfile(),
      fetchPublicProjects(),
      fetchPublicArticles(),
    ])
    profile.value = profileRes.data.data
    projects.value = projectRes.data.data
    featuredProjects.value = projectRes.data.data.slice(0, 3)
    articles.value = articleRes.data.data.slice(0, 3)
  } catch {
    loadError.value = '部分内容暂时无法加载。'
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.home {
  padding-bottom: 96px;
}

.hero {
  display: grid;
  grid-template-columns: minmax(0, 0.45fr) minmax(0, 0.55fr);
  gap: 48px;
  align-items: center;
  min-height: calc(100vh - 72px);
  padding: 32px 0 64px;
}

.copy {
  max-width: 460px;
}

.kicker {
  margin: 0 0 16px;
  color: #635bff;
  font-size: 14px;
}

h1 {
  margin: 0;
  font-size: clamp(40px, 6vw, 72px);
  letter-spacing: -0.045em;
  line-height: 0.95;
}

.role {
  margin: 18px 0 12px;
  font-size: 20px;
}

.lead,
.muted,
.empty {
  color: #6b6560;
  line-height: 1.75;
}

.actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 28px;
}

.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 44px;
  padding: 0 18px;
  border-radius: 999px;
  border: 1px solid transparent;
  font: inherit;
  text-decoration: none;
  cursor: pointer;
}

.btn.primary {
  background: #635bff;
  color: #fff;
}

.btn.ghost {
  background: transparent;
  border-color: #d8d1c6;
  color: #202124;
}

.skeleton {
  height: 18px;
  border-radius: 8px;
  background: #ece6dc;
  margin-bottom: 12px;
}

.skeleton.title {
  height: 48px;
  width: 70%;
}

.skeleton.short {
  width: 40%;
}

.band {
  padding: 72px 0 8px;
}

.band-head {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 24px;
}

.band-head h2 {
  margin: 0;
  font-size: 28px;
  letter-spacing: -0.03em;
}

.band-head a,
.text-btn {
  color: #635bff;
  background: none;
  border: 0;
  font: inherit;
  cursor: pointer;
  text-decoration: none;
}

.project-list,
.article-list {
  display: grid;
  gap: 20px;
}

.project,
.article {
  display: grid;
  grid-template-columns: 168px 1fr;
  gap: 24px;
  padding: 8px 0 24px;
  border-bottom: 1px solid #e8e1d6;
  text-decoration: none;
  color: inherit;
}

.project:not(:has(img)) {
  grid-template-columns: 1fr;
}

.project img {
  width: 100%;
  height: 112px;
  object-fit: cover;
  border-radius: 16px;
  background: #ece6dc;
}

.project h3,
.article h3 {
  margin: 0 0 8px;
  font-size: 22px;
}

.project a {
  color: inherit;
  text-decoration: none;
}

.project p span {
  display: inline-block;
  min-width: 42px;
  color: #635bff;
  font-size: 13px;
  margin-right: 8px;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}

.tags span {
  font-size: 12px;
  color: #635bff;
}

.links {
  display: flex;
  gap: 16px;
  margin-top: 10px;
}

.links a {
  color: #635bff;
}

.article {
  grid-template-columns: 1fr;
}

.skill-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.skill-row h3 {
  margin: 0 0 8px;
  font-size: 15px;
  color: #6b6560;
}

.contact {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 8px;
}

.contact a {
  color: #635bff;
}

@media (max-width: 860px) {
  .hero,
  .project,
  .skill-row {
    grid-template-columns: 1fr;
  }

  .hero {
    min-height: auto;
    gap: 24px;
    padding-top: 16px;
  }
}
</style>
