import {
  AmbientLight,
  BoxGeometry,
  CapsuleGeometry,
  Color,
  ConeGeometry,
  DirectionalLight,
  Group,
  HemisphereLight,
  Mesh,
  MeshStandardMaterial,
  Object3D,
  PerspectiveCamera,
  Raycaster,
  Scene,
  SphereGeometry,
  Vector2,
  Vector3,
  WebGLRenderer,
} from 'three'

export type StudioAction = 'projects' | 'resume' | 'articles'

export interface StudioHandle {
  setResumeOpen(open: boolean): void
  setPointer(nx: number, ny: number): void
  dispose(): void
}

const ACCENT = 0x635bff
const WARM = 0xf5f3ee
const GRAPHITE = 0x202124

function mat(color: number, extras: { roughness?: number; metalness?: number } = {}) {
  return new MeshStandardMaterial({
    color,
    roughness: extras.roughness ?? 0.82,
    metalness: extras.metalness ?? 0.04,
  })
}

function mesh(geometry: BoxGeometry | SphereGeometry | CapsuleGeometry | ConeGeometry, material: MeshStandardMaterial) {
  const item = new Mesh(geometry, material)
  item.castShadow = true
  item.receiveShadow = true
  return item
}

export function createStudioScene(
  canvas: HTMLCanvasElement,
  onAction: (action: StudioAction) => void,
  options: { reducedMotion: boolean },
): StudioHandle {
  const scene = new Scene()
  scene.background = new Color(WARM)

  const camera = new PerspectiveCamera(32, 1, 0.1, 40)
  camera.position.set(0, 1.42, 3.7)
  camera.lookAt(0, 0.32, 0)

  const renderer = new WebGLRenderer({ canvas, antialias: true, alpha: false })
  renderer.setPixelRatio(Math.min(window.devicePixelRatio, 1.75))
  renderer.shadowMap.enabled = true

  scene.add(new AmbientLight(0xffffff, 0.55))
  const hemi = new HemisphereLight(0xffffff, 0xe8e0d4, 0.7)
  scene.add(hemi)
  const key = new DirectionalLight(0xfff6ea, 1.15)
  key.position.set(-2.2, 4.2, 3.2)
  key.castShadow = true
  key.shadow.mapSize.set(1024, 1024)
  scene.add(key)

  const desk = mesh(new BoxGeometry(2.6, 0.1, 1.35), mat(0xe7dfd2, { roughness: 0.9 }))
  desk.position.set(0, -0.05, 0)
  scene.add(desk)

  const monster = new Group()
  monster.position.set(-0.12, 0.48, -0.12)
  const body = mesh(new CapsuleGeometry(0.34, 0.28, 8, 16), mat(ACCENT, { roughness: 0.7 }))
  const head = new Group()
  head.position.set(0, 0.58, 0)
  const skull = mesh(new SphereGeometry(0.3, 24, 18), mat(ACCENT, { roughness: 0.68 }))
  const hornL = mesh(new ConeGeometry(0.07, 0.18, 10), mat(0x4f48d6, { roughness: 0.6 }))
  hornL.position.set(-0.16, 0.28, 0)
  hornL.rotation.z = 0.35
  const hornR = hornL.clone()
  hornR.position.x = 0.16
  hornR.rotation.z = -0.35
  const eyeL = mesh(new SphereGeometry(0.07, 16, 12), mat(0xffffff, { roughness: 0.3 }))
  eyeL.position.set(-0.1, 0.04, 0.24)
  const eyeR = eyeL.clone()
  eyeR.position.x = 0.1
  const pupilL = mesh(new SphereGeometry(0.032, 12, 10), mat(GRAPHITE, { roughness: 0.35 }))
  pupilL.position.set(-0.1, 0.04, 0.3)
  const pupilR = pupilL.clone()
  pupilR.position.x = 0.1
  head.add(skull, hornL, hornR, eyeL, eyeR, pupilL, pupilR)
  monster.add(body, head)
  scene.add(monster)

  const monitor = new Group()
  monitor.name = 'projects'
  monitor.position.set(0.78, 0.58, -0.12)
  const screen = mesh(new BoxGeometry(0.92, 0.62, 0.06), mat(0x2a2740, { roughness: 0.35, metalness: 0.12 }))
  const bezel = mesh(new BoxGeometry(1.02, 0.72, 0.05), mat(0xd9d3c8, { roughness: 0.55 }))
  bezel.position.z = -0.03
  const stand = mesh(new BoxGeometry(0.16, 0.28, 0.12), mat(0xcec6b8))
  stand.position.set(0, -0.5, 0)
  const base = mesh(new BoxGeometry(0.42, 0.05, 0.28), mat(0xc6bdae))
  base.position.set(0, -0.64, 0.04)
  const glow = mesh(new BoxGeometry(0.78, 0.48, 0.01), mat(0x8b85ff, { roughness: 0.4 }))
  glow.position.z = 0.035
  monitor.add(bezel, screen, glow, stand, base)
  scene.add(monitor)

  const folder = new Group()
  folder.name = 'resume'
  folder.position.set(-0.78, 0.08, 0.32)
  folder.rotation.y = 0.35
  const folderBack = mesh(new BoxGeometry(0.62, 0.08, 0.48), mat(0xf0c56e, { roughness: 0.7 }))
  const folderFront = mesh(new BoxGeometry(0.62, 0.05, 0.46), mat(0xf6d48a, { roughness: 0.65 }))
  folderFront.position.set(0, 0.05, 0.02)
  folderFront.name = 'folder-lid'
  folder.add(folderBack, folderFront)
  scene.add(folder)

  const notebook = new Group()
  notebook.name = 'articles'
  notebook.position.set(0.18, 0.08, 0.52)
  notebook.rotation.y = -0.4
  const cover = mesh(new BoxGeometry(0.58, 0.04, 0.42), mat(0xefeafc, { roughness: 0.55 }))
  const pages = mesh(new BoxGeometry(0.54, 0.03, 0.38), mat(0xfffdf8, { roughness: 0.9 }))
  pages.position.y = 0.03
  notebook.add(cover, pages)
  scene.add(notebook)

  const clickables: Object3D[] = [monitor, folder, notebook]
  const raycaster = new Raycaster()
  const pointerNdc = new Vector2()
  const lookTarget = new Vector3()
  const headRest = new Vector3(0, 0.58, 0.35)

  function actionFrom(obj: Object3D | null): StudioAction | null {
    let current: Object3D | null = obj
    while (current) {
      if (current.name === 'projects' || current.name === 'resume' || current.name === 'articles') {
        return current.name
      }
      current = current.parent
    }
    return null
  }

  let resumeOpen = false
  let pointerX = 0
  let pointerY = 0
  let running = true
  let frame = 0
  const reduced = options.reducedMotion

  const monsterRest = new Vector3(-0.12, 0.48, -0.12)
  const monsterOpen = new Vector3(-1.28, 0.32, 0.12)
  const folderRest = new Vector3(-0.78, 0.08, 0.32)
  const folderOpenPos = new Vector3(-0.78, 0.14, 0.48)

  function resize() {
    const width = canvas.clientWidth || 1
    const height = canvas.clientHeight || 1
    camera.aspect = width / height
    camera.updateProjectionMatrix()
    renderer.setSize(width, height, false)
  }

  function pick(clientX: number, clientY: number) {
    const rect = canvas.getBoundingClientRect()
    pointerNdc.x = ((clientX - rect.left) / rect.width) * 2 - 1
    pointerNdc.y = -((clientY - rect.top) / rect.height) * 2 + 1
    raycaster.setFromCamera(pointerNdc, camera)
    const hits = raycaster.intersectObjects(clickables, true)
    return hits.length ? actionFrom(hits[0].object) : null
  }

  function onClick(event: MouseEvent) {
    const action = pick(event.clientX, event.clientY)
    if (action) {
      onAction(action)
    }
  }

  function onMove(event: MouseEvent) {
    const rect = canvas.getBoundingClientRect()
    pointerX = ((event.clientX - rect.left) / rect.width) * 2 - 1
    pointerY = -((event.clientY - rect.top) / rect.height) * 2 + 1
    canvas.style.cursor = pick(event.clientX, event.clientY) ? 'pointer' : 'default'
  }

  const resizeObserver = new ResizeObserver(resize)
  resizeObserver.observe(canvas)
  canvas.addEventListener('click', onClick)
  canvas.addEventListener('mousemove', onMove)
  resize()

  function tick(now: number) {
    if (!running) {
      return
    }
    frame = requestAnimationFrame(tick)
    const t = now * 0.001
    const openMix = resumeOpen ? 1 : 0

    monster.position.lerpVectors(monsterRest, monsterOpen, openMix)
    const scale = 1 - openMix * 0.42
    const breath = reduced ? 0 : Math.sin(t * 1.6) * 0.018 * (1 - openMix)
    monster.scale.set(scale, scale * (1 + breath), scale)

    folder.position.lerpVectors(folderRest, folderOpenPos, openMix)
    folderFront.rotation.x = -openMix * 1.05

    if (!reduced) {
      lookTarget.set(pointerX * 0.8, 0.6 + pointerY * 0.35, 0.9)
      head.lookAt(lookTarget)
    } else {
      head.lookAt(headRest)
    }

    renderer.render(scene, camera)
  }
  frame = requestAnimationFrame(tick)

  return {
    setResumeOpen(open: boolean) {
      resumeOpen = open
      if (reduced) {
        monster.position.copy(open ? monsterOpen : monsterRest)
        folder.position.copy(open ? folderOpenPos : folderRest)
        folderFront.rotation.x = open ? -1.05 : 0
        const scale = open ? 0.58 : 1
        monster.scale.setScalar(scale)
      }
    },
    setPointer(nx: number, ny: number) {
      pointerX = nx
      pointerY = ny
    },
    dispose() {
      running = false
      cancelAnimationFrame(frame)
      resizeObserver.disconnect()
      canvas.removeEventListener('click', onClick)
      canvas.removeEventListener('mousemove', onMove)
      renderer.dispose()
      scene.traverse((obj) => {
        const item = obj as Mesh
        item.geometry?.dispose?.()
        const material = item.material as MeshStandardMaterial | MeshStandardMaterial[] | undefined
        if (Array.isArray(material)) {
          material.forEach((entry) => entry.dispose())
        } else {
          material?.dispose?.()
        }
      })
    },
  }
}
