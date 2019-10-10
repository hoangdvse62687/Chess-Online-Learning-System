<template>
  <v-app>
    <Header />
    <v-content>
      <Loader v-if="$store.state.loader" />
      <router-view></router-view>
    </v-content>
    <Footer />
  </v-app>
</template>

<script>
import Header from '@/components/Header'
import Loader from '@/components/Loader'
import Footer from '@/components/Footer'
export default {
  components: {
    Header,
    Footer,
    Loader
  },
  watch: {
    $route(to, from) {
      if (to.path === '/') {
        document.getElementsByTagName('header')[0].style.display = 'none'
        document.getElementsByTagName('footer')[0].style.display = 'none'
      } else {
        document.getElementsByTagName('header')[0].style.display = 'block'
        document.getElementsByTagName('footer')[0].style.display = 'flex'
      }
    }
  },
  mounted() {
    if (this.$route.path === '/') {
      document.getElementsByTagName('header')[0].style.display = 'none'
      document.getElementsByTagName('footer')[0].style.display = 'none'
    }
  }
}
</script>
<style></style>
