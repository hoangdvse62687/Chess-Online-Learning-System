import Vue from "vue";
import App from "./App.vue";
import router from './router'
import store from './store';
import mixin from './mixin';
import vuetify from './plugins/vuetify';
import './assets/style/app.css'
import './assets/style/vue-chessboard.css'
import './assets/style/chessboard-0.3.0.css'
import firebase from 'firebase'
import Repository, { setAuthorizationHeader } from '@/repository/Repository.js'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const userRepository = RepositoryFactory.get('user')
const notificationRepository = RepositoryFactory.get('notification')

Vue.config.productionTip = false;
const vm = new Vue()
var firebaseConfig = {
  apiKey: 'AIzaSyDBz72G-L_nG1s2CgXHx0pPKc8tQLNyyS0',
  authDomain: 'cols-fpt.firebaseapp.com',
  databaseURL: 'https://cols-fpt.firebaseio.com',
  projectId: 'cols-fpt',
  storageBucket: 'cols-fpt.appspot.com',
  messagingSenderId: '1082474123206',
  appId: '1:1082474123206:web:79a9804e9ccc5382'
}
// Initialize Firebase
firebase.initializeApp(firebaseConfig)

const messaging = firebase.messaging();

async function getParamsFromHeader(to) {
  const token = to.query.token
  const role = to.query.role
  const acceptedRole = [1, 3]
  if (token && role && acceptedRole.includes(parseInt(role))) {
    localStorage.setItem('access-token', `Chess ${token}`)
    localStorage.setItem('role', role)
    setAuthorizationHeader(Repository, localStorage.getItem('access-token').trim())
  }
}

async function getCurrentUserDetail() {
  const { data } = await userRepository.getCurrentUserDetail()
  let user = data.data
  user.roleName = vm.getRoleName(user.roleId)
  user.status = vm.getStatusUser(user.active)
  localStorage.setItem('user', JSON.stringify(user))
  setUserState()
}
function setUserState() {
  const user = localStorage.getItem('user')
  const userToken = localStorage.getItem('access-token')
  store.commit('setUser', JSON.parse(user))
  store.commit('setUserToken', userToken)
}
router.beforeEach(async (to, from, next) => {
  await getParamsFromHeader(to)
  
  const publicPages = ['/']
  const roleAccepted = [1, 3]

  const authRequired = !publicPages.includes(to.path)
  const loggedIn = localStorage.getItem('access-token') != null
  let role = ''
  if (localStorage.getItem('user') != null) {
    role = JSON.parse(localStorage.getItem('user')).roleId
  } else {
    role = parseInt(localStorage.getItem('role'))
  }
  const roleRequired = roleAccepted.includes(role)

  // if ((authRequired && !loggedIn) || (authRequired && !roleRequired)) {

  if (authRequired && (!loggedIn || !roleRequired)) {
    console.log("not login yet")
    return next('/') //chua login -> ve trang login
  } else if (!authRequired && loggedIn && roleRequired) {
    store.state.user = JSON.parse(localStorage.getItem('user'))
    return next('/dashboard/profile') //login roi, nhung vao lai login -> qua dashboard
  } else if (loggedIn && !roleRequired) {
    localStorage.removeItem('access-token')
    localStorage.removeItem('role')
    return next('/')
  } else if (loggedIn && roleRequired) {
    await getCurrentUserDetail()
  }
  next()

  if(loggedIn){
    messaging
      .requestPermission()
      .then(function () {
        console.log("Notification permission granted.");

        // get the token in the form of promise
        return messaging.getToken()
      })
      .then(function(token) {
        notificationRepository.updateNotificationToken(token).then(res => {
          console.log(res)
        })
        console.log('token:' + token)
      })
      .catch(function (err) {
        console.log("Unable to get permission to notify.", err);
      });
  }
})

new Vue({
  vuetify,
  store,
  mixin,
  router,
  render: h => h(App)
}).$mount("#app");
