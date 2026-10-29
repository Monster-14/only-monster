<template>
  <div class="stage" :class="{ compact: compact }">
    <canvas
      v-show="mode === 'webgl'"
      ref="canvasRef"
      class="canvas"
      aria-hidden="true"
    />
    <div v-if="mode !== 'webgl'" class="fallback" aria-hidden="true">
      <div class="desk">
        <div class="blob monster" :class="{ tucked: resumeOpen }">
          <span class="horn left" />
          <span class="horn right" />
          <span class="eye left" />
          <span class="eye right" />
        </div>
        <button class="prop monitor" type="button" aria-label="查看项目" @click="$emit('action', 'projects')">
          <span class="screen" />
        </button>
        <button class="prop folder" type="button" aria-label="打开履历" @click="$emit('action', 'resume')">
          <span class="lid" :class="{ open: resumeOpen }" />
        </button>
        <button class="prop notebook" type="button" aria-label="查看文章" @click="$emit('action', 'articles')">
          <span class="page" />
        </button>
      </div>
    </div>
    <div class="hotspots">
      <button class="hotspot folder" type="button" aria-label="打开履历" @click="$emit('action', 'resume')">履历</button>
      <button class="hotspot notebook" type="button" aria-label="查看文章" @click="$emit('action', 'articles')">文章</button>
      <button class="hotspot monitor" type="button" aria-label="查看项目" @click="$emit('action', 'projects')">项目</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref, watch } from 'vue'
import type { StudioAction, StudioHandle } from '@/scene/studioScene'

const props = defineProps<{
  resumeOpen: boolean
}>()

const emit = defineEmits<{
  action: [action: StudioAction]
}>()

const canvasRef = ref<HTMLCanvasElement | null>(null)
const mode = ref<'pending' | 'webgl' | 'fallback'>('pending')
const compact = ref(false)
let handle: StudioHandle | null = null

function prefersReducedMotion() {
  return window.matchMedia('(prefers-reduced-motion: reduce)').matches
}

function isCompact() {
  return window.matchMedia('(max-width: 860px)').matches
}

async function boot() {
  compact.value = isCompact()
  const canvas = canvasRef.value
  const canWebgl = !!window.WebGLRenderingContext
  if (!canvas || !canWebgl || compact.value) {
    handle?.dispose()
    handle = null
    mode.value = 'fallback'
    return
  }
  try {
    if (!handle) {
      const { createStudioScene } = await import('@/scene/studioScene')
      handle = createStudioScene(canvas, (action) => emit('action', action), {
        reducedMotion: prefersReducedMotion(),
      })
    }
    handle.setResumeOpen(props.resumeOpen)
    mode.value = 'webgl'
  } catch {
    mode.value = 'fallback'
  }
}

onMounted(() => {
  boot()
  window.addEventListener('resize', boot)
})

watch(
  () => props.resumeOpen,
  (open) => handle?.setResumeOpen(open),
)

onBeforeUnmount(() => {
  window.removeEventListener('resize', boot)
  handle?.dispose()
  handle = null
})
</script>

<style scoped>
.stage {
  position: relative;
  min-height: 380px;
  height: min(56vh, 500px);
  border-radius: 28px;
  background:
    radial-gradient(circle at 70% 20%, rgba(99, 91, 255, 0.12), transparent 42%),
    linear-gradient(180deg, #fbfaf6 0%, #efeae1 100%);
  overflow: hidden;
}

.canvas {
  width: 100%;
  height: 100%;
  display: block;
}

.hotspots {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.hotspot {
  pointer-events: auto;
  position: absolute;
  border: 0;
  background: rgba(255, 253, 248, 0.88);
  color: #202124;
  font: inherit;
  font-size: 13px;
  letter-spacing: 0.04em;
  padding: 6px 12px;
  border-radius: 999px;
  box-shadow: 0 8px 24px rgba(32, 33, 36, 0.08);
  cursor: pointer;
}

.hotspot:hover,
.hotspot:focus-visible {
  background: #635bff;
  color: #fff;
  outline: none;
}

.hotspot.folder {
  left: 10%;
  bottom: 18%;
}

.hotspot.notebook {
  left: 46%;
  bottom: 14%;
}

.hotspot.monitor {
  right: 6%;
  top: 10%;
}

.fallback {
  height: 100%;
  display: grid;
  place-items: center;
  padding: 28px;
}

.desk {
  position: relative;
  width: min(100%, 420px);
  height: 240px;
  background: #e7dfd2;
  border-radius: 22px;
}

.blob {
  position: absolute;
  left: 42%;
  top: 28px;
  width: 88px;
  height: 108px;
  margin-left: -44px;
  background: #635bff;
  border-radius: 44px 44px 36px 36px;
  transition: transform 0.5s ease;
}

.blob.tucked {
  transform: translate(-118px, 18px) scale(0.62);
}

.horn {
  position: absolute;
  top: -10px;
  width: 14px;
  height: 22px;
  background: #4f48d6;
  border-radius: 8px 8px 2px 2px;
}

.horn.left {
  left: 16px;
  transform: rotate(-18deg);
}

.horn.right {
  right: 16px;
  transform: rotate(18deg);
}

.eye {
  position: absolute;
  top: 38px;
  width: 14px;
  height: 14px;
  background: #fff;
  border-radius: 50%;
}

.eye.left {
  left: 20px;
}

.eye.right {
  right: 20px;
}

.prop {
  position: absolute;
  border: 0;
  padding: 0;
  cursor: pointer;
  background: transparent;
}

.monitor {
  right: 28px;
  top: 18px;
  width: 120px;
  height: 88px;
}

.monitor .screen {
  display: block;
  width: 100%;
  height: 100%;
  border-radius: 10px;
  background: linear-gradient(180deg, #8b85ff, #2a2740);
  box-shadow: 0 10px 0 #cec6b8;
}

.folder {
  left: 28px;
  bottom: 28px;
  width: 92px;
  height: 64px;
}

.folder .lid {
  display: block;
  width: 100%;
  height: 100%;
  background: #f0c56e;
  border-radius: 8px 18px 8px 8px;
  transform-origin: top center;
  transition: transform 0.5s ease;
}

.folder .lid.open {
  transform: rotateX(-28deg);
}

.notebook {
  left: 46%;
  bottom: 22px;
  width: 86px;
  height: 58px;
}

.notebook .page {
  display: block;
  width: 100%;
  height: 100%;
  background: #efeafc;
  border-radius: 8px;
  box-shadow: inset 0 -10px 0 #fffdf8;
}

.compact {
  min-height: 280px;
  height: 300px;
}

.compact .hotspots {
  display: none;
}

@media (prefers-reduced-motion: reduce) {
  .blob,
  .folder .lid {
    transition: none;
  }
}
</style>
