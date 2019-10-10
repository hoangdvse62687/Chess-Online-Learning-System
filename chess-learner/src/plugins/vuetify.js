import Vue from 'vue'
import Vuetify from 'vuetify/lib'

import '@/assets/style/global.styl'
import '@fortawesome/fontawesome-free/css/all.css'

Vue.use(Vuetify)

export default new Vuetify({
  iconfont: 'fa' || 'mdi',
  breakpoint: {
    thresholds: {
      xs: 0,
      sm: 577,
      md: 769,
      lg: 993,
      xl: 1201
    }
  }
})
