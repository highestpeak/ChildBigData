import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import 'font-awesome/css/font-awesome.css'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI);

import echarts from "echarts";
Vue.prototype.$echarts = echarts;

import axios from 'axios';
Vue.prototype.$axios = axios;

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
