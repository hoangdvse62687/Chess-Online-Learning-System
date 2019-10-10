<template>
  <v-footer height="auto" padless>
    <v-layout class="bg-dark white--text text-center" wrap py-4>
      <v-flex xs12 mb-3>
        <v-layout justify-center>
          <span>
            <h5 class="footer-title">A CAPSTONE PROJECT AT</h5>
          </span>
        </v-layout>
      </v-flex>
      <v-flex xs12 mb-3>
        <v-img contain :src="logoFPT" height="60"></v-img>
      </v-flex>
      <v-flex xs12>
        <v-layout justify-center>
          <span class="body-2">
            &copy; {{ new Date().getFullYear() }} Copyright
            <router-link to="/">COLS Project Team</router-link>
          </span>
        </v-layout>
      </v-flex>
    </v-layout>
  </v-footer>
</template>

<script>
export default {
  data() {
    return {
      logoFPT: require('@/assets/images/fpt-university-logo.png')
    }
  }
}
</script>

<style scoped>
a {
  text-decoration: none;
}
.footer-title {
  font-size: 20px;
  font-weight: 500;
  line-height: 1;
  letter-spacing: 0.3px;
}
</style>
