<template>
  <v-content>
    <v-layout align-end justify-center row fill-height xs12 :style="homeBackground"></v-layout>
    <v-layout align-end justify-center row fill-height class="login-container xs12">
      <LoginField/>
    </v-layout>
  </v-content>
</template>

<script>
import LoginField from '@/components/LoginField'
export default {
  components: {
    LoginField
  },
  data() {
    return {
      backgroundImage: require('@/assets/images/chess-background.jpg')
    }
  },
  computed: {
    homeBackground() {
      return {
        backgroundImage: `url(${this.backgroundImage})`,
        backgroundPosition: `center center`,
        backgroundSize: `cover`,
        filter: `brightness(0.5)`,
        minHeight: `100vh`
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  position: absolute;
  left: 0px;
  top: 0px;
  width: 100%;
}
</style>
