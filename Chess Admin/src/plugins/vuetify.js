import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import '@/assets/style/global.styl'
import '@fortawesome/fontawesome-free/css/all.css'

Vue.use(Vuetify);

export default new Vuetify({
  icons: {
    iconfont: 'fa'
  }
});
