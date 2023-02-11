import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/404.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}

export const setRoutes = () => {
  const storeMenus= localStorage.getItem("menus");
  if (storeMenus) {
    // 获取当前的路由对象名称数组
    const currentRouteNames = router.getRoutes().map(v => v.name)
    if (!currentRouteNames.includes('Manage')) {
      // 拼装动态路由
      const manageRoute =
          { path: '/',
            name: 'Container',
            component: () => import('../views/Container.vue'),
            redirect: "/home",
            children: [
              { path: 'person',
                name: '个人信息',
                component: () => import('../views/Person.vue')
              },
              { path: 'noticeDetail',
                name:'博客详情页',
                component:()=> import('../views/NoticeDetail.vue')
              }
            ] }
      const menus = JSON.parse(storeMenus)
      menus.forEach(item => {
        // 当且仅当path不为空的时候才去设置路由
        if (item.path) {
          let itemMenu = {
            path: item.path.replace("/", ""),
            name: item.name,
            component: () => import('../views/' + item.component + '.vue')
          }
          manageRoute.children.push(itemMenu)
        } else if(item.children.length) {
          item.children.forEach(item => {
            if (item.path) {
              let itemMenu = {
                path: item.path.replace("/", ""),
                name: item.name,
                component: () => import('../views/' + item.component + '.vue')
              }
              manageRoute.children.push(itemMenu)
            }
          })
        }
      })
      router.addRoute(manageRoute)
    }

  }
}

setRoutes()

router.beforeEach((to, from, next) => {
  localStorage.setItem("currentMenu", to.name)  // 设置当前的路由名称
  store.commit("setPath")
  // 未找到路由的情况
  if (!to.matched.length) {
    const menus = localStorage.getItem("menus")
    if (menus) {
      next("/404")
    } else {
      // 跳回登录页面
      next("/login")
    }
  }
  // 其他的情况都放行
  next()
})
export default router
