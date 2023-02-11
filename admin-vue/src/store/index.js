import Vue from 'vue'
import Vuex from 'vuex'
import router from "@/router"
import {resetRouter} from "@/router";

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
      currentMenu: ''
  },
  getters: {
  },
  mutations: {
    setPath (state) {
      state.currentMenu = localStorage.getItem("currentMenu")
    },
    logout() {
        localStorage.removeItem("user")
        localStorage.removeItem("menus")
        router.push("/login")
        resetRouter()
    }
  },
  actions: {
  },
  modules: {
  }
})


