import Vue from 'vue'
import Vuex from 'vuex'

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
    }
  },
  actions: {
  },
  modules: {
  }
})


