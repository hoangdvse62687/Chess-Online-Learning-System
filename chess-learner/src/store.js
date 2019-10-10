import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    api: {
      login: 'http://cols-be.ml/oauth2/authorize/google'
    },
    user: JSON.parse(localStorage.getItem('user')),
    userToken: localStorage.getItem('access-token'),
    dialog: {
      enrolDialog: false,
      confirmEnrolDialog: false
    },
    loader: 0
  },
  mutations: {
    setUser(state, payload) {
      state.user = payload
    },
    setUserToken(state, payload) {
      state.userToken = payload
    },
    incrementLoader(state, payload) {
      state.loader += payload
    }
  },
  actions: {},
  getters: {}
})
