import Vue from 'vue'
import Router from 'vue-router'

import Introduce from '@/pages/Introduce'
import CourseOverview from '@/pages/CourseOverview'
import CourseDetail from '@/pages/CourseDetail'
import Learning from '@/pages/Learning'
import PlayChess from '@/pages/PlayChess'
import Profile from '@/pages/Profile'
import Register from '@/pages/Register'
import Instructor from '@/pages/Instructor'
Vue.use(Router)

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    { path: '/', name: 'home', component: Introduce },
    { path: '/course', name: 'courseOverView', component: CourseOverview },
    {
      path: '/course/:courseId',
      component: CourseDetail,
      name: 'courseDetail'
    },
    {
      path: '/learning/course/:courseId/lesson/:lessonId',
      component: Learning
    },
    { path: '/play-chess', component: PlayChess },
    { path: '/profile/:tabItem', component: Profile },
    { path: '/register', component: Register },
    { path: '/instructor/:instructorId', component: Instructor }
  ]
})
