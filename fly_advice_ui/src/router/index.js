import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/searchResult',
    name: 'SearchResult',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import('../views/SearchResultPage.vue'),
    props: true
  },
  {
    path: '/selectResult',
    name: 'SelectResult',
    component: () => import('../views/SelectResult.vue'),
    props: true
  },
  {
    path: '/dataAnalysis',
    name: 'DataAnalysis',
    component: () => import('../views/DataAnalysis.vue')
  },
  {
    path: '/flytoWhere',
    name: 'FlytoWhere',
    component: () => import('../views/FlytoWhere.vue')
  },
  {
    path: '/whenFly',
    name: 'WhenFly',
    component: () => import('../views/WhenFly.vue')
  },
  {
    path: '/chart',
    name: 'Chart',
    component: () => import('../views/ChartView.vue')
  },
  {
    path: '/center',
    name: 'Center',
    component: () => import('../views/Center.vue')
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
