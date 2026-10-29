<template>
  <div
    v-if="open"
    class="overlay"
    role="dialog"
    aria-modal="true"
    aria-labelledby="resume-title"
    @click.self="close"
  >
    <section ref="panelRef" class="panel" tabindex="-1">
      <header class="panel-head">
        <div>
          <p class="kicker">个人履历</p>
          <h2 id="resume-title">{{ profile?.name || 'Monster' }}</h2>
        </div>
        <button class="icon-btn" type="button" aria-label="关闭履历" @click="close">关闭</button>
      </header>

      <div class="body">
        <section v-if="profile" class="block">
          <h3>概况</h3>
          <p v-if="profile.headline" class="headline">{{ profile.headline }}</p>
          <p v-if="profile.bio" class="bio">{{ profile.bio }}</p>
        </section>

        <section v-if="education.length" class="block">
          <h3>教育经历</h3>
          <ol class="timeline">
            <li v-for="item in education" :key="item.id">
              <span class="when">{{ item.period }}</span>
              <strong>{{ item.title }}</strong>
              <span v-if="item.org" class="org">{{ item.org }}</span>
              <p>{{ item.summary }}</p>
            </li>
          </ol>
        </section>

        <section v-if="workExperience.length" class="block">
          <h3>工作经历</h3>
          <ol class="timeline">
            <li v-for="item in workExperience" :key="item.id">
              <span class="when">{{ item.period }}</span>
              <strong>{{ item.title }}</strong>
              <span v-if="item.org" class="org">{{ item.org }}</span>
              <p>{{ item.summary }}</p>
            </li>
          </ol>
        </section>

        <section v-if="featuredProjects.length" class="block">
          <h3>代表项目</h3>
          <ul class="projects">
            <li v-for="item in featuredProjects" :key="item.id">
              <router-link :to="`/projects/${item.slug}`">{{ item.title }}</router-link>
              <p>{{ item.summary }}</p>
            </li>
          </ul>
        </section>

        <section v-if="skillGroups.length" class="block">
          <h3>技能</h3>
          <div v-for="group in skillGroups" :key="group.category" class="skills">
            <h4>{{ group.category }}</h4>
            <p>{{ group.items.join('、') }}</p>
          </div>
        </section>

        <section v-if="hasContact" class="block">
          <h3>联系方式</h3>
          <p v-if="profile?.email">
            <a :href="`mailto:${profile.email}`">{{ profile.email }}</a>
          </p>
          <p v-if="profile?.githubUrl">
            <a :href="profile.githubUrl" target="_blank" rel="noreferrer">GitHub</a>
          </p>
          <p v-if="profile?.wechat">微信：{{ profile.wechat }}</p>
          <p v-if="profile?.blogUrl">
            <a :href="profile.blogUrl" target="_blank" rel="noreferrer">博客</a>
          </p>
        </section>
      </div>

      <footer class="panel-foot">
        <a v-if="resumeMeta.pdfUrl" class="text-link" :href="resumeMeta.pdfUrl" download>下载 PDF</a>
        <router-link class="text-link" to="/contact">去留言</router-link>
      </footer>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { education, resumeMeta, skillGroups, workExperience } from '@/data/resume'
import type { ProjectItem, SiteProfile } from '@/api/types'

const props = defineProps<{
  open: boolean
  profile: SiteProfile | null
  projects: ProjectItem[]
}>()

const emit = defineEmits<{
  close: []
}>()

const panelRef = ref<HTMLElement | null>(null)
const featuredProjects = computed(() => props.projects.slice(0, 3))
const hasContact = computed(() =>
  Boolean(props.profile?.email || props.profile?.githubUrl || props.profile?.wechat || props.profile?.blogUrl),
)

function close() {
  emit('close')
}

function onKey(event: KeyboardEvent) {
  if (event.key === 'Escape' && props.open) {
    close()
  }
}

watch(
  () => props.open,
  async (open) => {
    if (open) {
      await nextTick()
      panelRef.value?.focus()
    }
  },
)

onMounted(() => window.addEventListener('keydown', onKey))
onBeforeUnmount(() => window.removeEventListener('keydown', onKey))
</script>

<style scoped>
.overlay {
  position: fixed;
  inset: 0;
  z-index: 30;
  background: rgba(32, 33, 36, 0.28);
  display: grid;
  place-items: center;
  padding: 24px;
}

.panel {
  width: min(720px, 100%);
  max-height: min(86vh, 860px);
  overflow: auto;
  background: #fffcf7;
  color: #202124;
  border-radius: 24px;
  padding: 28px 28px 20px;
  box-shadow: 0 24px 80px rgba(32, 33, 36, 0.18);
  outline: none;
}

.panel-head,
.panel-foot {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
}

.kicker {
  margin: 0 0 6px;
  color: #635bff;
  font-size: 14px;
}

h2 {
  margin: 0;
  font-size: 32px;
  letter-spacing: -0.03em;
}

.icon-btn,
.text-link {
  border: 0;
  background: transparent;
  color: #635bff;
  font: inherit;
  cursor: pointer;
  text-decoration: none;
}

.body {
  margin-top: 24px;
}

.block + .block {
  margin-top: 28px;
}

h3 {
  margin: 0 0 10px;
  font-size: 15px;
  color: #6b6560;
}

.headline {
  margin: 0 0 8px;
  font-size: 18px;
}

.bio,
.timeline p,
.projects p,
.skills p {
  margin: 6px 0 0;
  line-height: 1.7;
  user-select: text;
}

.timeline,
.projects {
  list-style: none;
  margin: 0;
  padding: 0;
}

.timeline li {
  position: relative;
  padding: 0 0 18px 18px;
  border-left: 1px solid #e4ddd2;
}

.timeline li::before {
  content: '';
  position: absolute;
  left: -5px;
  top: 6px;
  width: 9px;
  height: 9px;
  border-radius: 50%;
  background: #635bff;
}

.when,
.org {
  display: block;
  color: #6b6560;
  font-size: 13px;
}

.projects a,
.block a {
  color: #635bff;
}

.skills + .skills {
  margin-top: 12px;
}

h4 {
  margin: 0;
  font-size: 14px;
}

.panel-foot {
  margin-top: 20px;
  padding-top: 12px;
  border-top: 1px solid #eee7dc;
}

@media (max-width: 640px) {
  .overlay {
    padding: 0;
    align-items: stretch;
  }

  .panel {
    width: 100%;
    max-height: 100%;
    border-radius: 0;
  }
}
</style>
