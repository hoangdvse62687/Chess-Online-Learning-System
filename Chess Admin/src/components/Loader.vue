<template>
  <v-overlay :value="dialog">
    <v-progress-circular indeterminate color="primary" size="64" width="5"></v-progress-circular>
  </v-overlay>
</template>
<script>
export default {
  data() {
    return {
      dialog: true
    }
  }
}
</script>

<style scoped>
>>> .v-dialog {
  box-shadow: none !important;
  /* background-color: transparent; */
  height: 120px;
}
</style>
