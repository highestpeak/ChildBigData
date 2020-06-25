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
    path: '/center',
    name: 'Center',
    component: () => import(/* webpackChunkName: "about" */ '../views/Center.vue')
  },
  {
    path: '/map',
    name: 'Map',
    component: () => import(/* webpackChunkName: "about" */ '../views/MapAnalysis.vue')
  },
  {
    path: '/chart',
    name: 'Chart',
    component: () => import(/* webpackChunkName: "about" */ '../views/ChartView.vue')
  },
  {
    path: '/searchResult',
    name: 'SearchResult',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/SearchResultPage.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
