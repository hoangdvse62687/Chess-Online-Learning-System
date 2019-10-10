<template>
  <v-footer height="auto" style="background-color: rgba(0,0,0,0.67)">
    <v-layout row class="grey--text text--lighten-1 text-xs-center" wrap py-2>
      <v-container class="py-0">
        <v-flex class="text-right">
        <span class="px-3 body-2">
          &copy; {{ new Date().getFullYear() }} Copyright
          <router-link to="/">COLS Project Team</router-link>
        </span>
      </v-flex>
      </v-container>
    </v-layout>
  </v-footer>
</template>

<script>
export default {
};
</script>

<style scoped>
.v-footer {
  position: absolute;
  width: 100%;
  bottom: 0;
}
</style>
<style>
.v-application .v-footer a {
  text-decoration: none;
  color: #FBC02D;
}
</style>