import { createRouter, createWebHistory } from 'vue-router'
import SiteLayout from '@/layouts/SiteLayout.vue'
import AdminLayout from '@/layouts/AdminLayout.vue'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: SiteLayout,
      children: [
        {
          path: '',
          name: 'home',
          component: () => import('@/views/site/HomeView.vue'),
        },
        {
          path: 'projects',
          name: 'projects',
          component: () => import('@/views/site/ProjectsView.vue'),
        },
        {
          path: 'projects/:slug',
          name: 'project-detail',
          component: () => import('@/views/site/ProjectDetailView.vue'),
        },
      ],
    },
    {
      path: '/admin/login',
      name: 'admin-login',
      component: () => import('@/views/admin/LoginView.vue'),
    },
    {
      path: '/admin',
      component: AdminLayout,
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'admin-dashboard',
          component: () => import('@/views/admin/DashboardView.vue'),
        },
        {
          path: 'profile',
          name: 'admin-profile',
          component: () => import('@/views/admin/ProfileView.vue'),
        },
        {
          path: 'projects',
          name: 'admin-projects',
          component: () => import('@/views/admin/ProjectListView.vue'),
        },
        {
          path: 'projects/:id',
          name: 'admin-project-edit',
          component: () => import('@/views/admin/ProjectEditView.vue'),
        },
      ],
    },
  ],
})

router.beforeEach((to) => {
  const auth = useAuthStore()
  if (to.meta.requiresAuth && !auth.isLoggedIn) {
    return { path: '/admin/login', query: { redirect: to.fullPath } }
  }
  if (to.path === '/admin/login' && auth.isLoggedIn) {
    return { path: '/admin' }
  }
  return true
})

export default router
