import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/Login.vue')
    },
    // 使用Layout作为主布局的路由
    {
      path: '/',
      component: () => import('../components/Layout.vue'),
      meta: { requiresAuth: true },
      redirect: '/profile', // 默认重定向到个人中心
      children: [
        {
          path: 'profile', // 移除斜杠，使用相对路径
          name: 'profile',
          component: () => import('../views/Profile.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'profile/info', // 移除斜杠，使用相对路径
          name: 'profileInfo',
          component: () => import('../views/ProfileInfo.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'profile/password', // 移除斜杠，使用相对路径
          name: 'profilePassword',
          component: () => import('../views/ProfilePassword.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'studentList', // 移除斜杠，使用相对路径
          name: 'studentList',
          component: () => import('../views/StudentList.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'system', // 移除斜杠，使用相对路径
          name: 'system',
          component: () => import('../views/System.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'messageBoard',
          name: 'messageBoard',
          component: () => import('../views/MessageBoard.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'updateLog',
          name: 'updateLog',
          component: () => import('../views/UpdateLog.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'contact',
          name: 'contact',
          component: () => import('../views/Contact.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'newContent',
          name: 'newContent',
          component: () => import('../views/NewContent.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: 'post',
          name: 'post',
          component: () => import('../views/Post.vue'),
          meta: { requiresAuth: true }
        }
      ]
    }
  ]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 判断是否需要登录
  if (to.meta.requiresAuth) {
    // 检查是否有token
    const token = localStorage.getItem('token')
    if (token) {
      next()
    } else {
      // 没有token，跳转到登录页
      next('/login')
    }
  } else {
    next()
  }
})

export default router
