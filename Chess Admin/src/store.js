import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    api: {
      login: 'http://cols-be.ml/oauth2/authorize/google'
    },
    loader: 0,
    user: JSON.parse(localStorage.getItem('user')),
    currentLessonList: [],
    unreadNotifications: sessionStorage.getItem('unread-notification-counter') ? sessionStorage.getItem('unread-notification-counter') : 0,
    notificationUpdatedId: 0
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
    },
    changeUser(state, payload) {
      state.user = payload
    },
    changeUnreadNotifications(state, unreadNotifications) {
      state.unreadNotifications = unreadNotifications
    },
    changeNotificationUpdatedId(state, notificationUpdatedId) {
      state.notificationUpdatedId = notificationUpdatedId
    }
  },
  actions: {},
  getters: {}
})
