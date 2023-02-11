import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import request from "@/util/request";
import "./assets/css/global.css"

Vue.config.productionTip = false

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI, { size: "small" });

import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
Vue.use(mavonEditor)

Vue.prototype.request=request


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
