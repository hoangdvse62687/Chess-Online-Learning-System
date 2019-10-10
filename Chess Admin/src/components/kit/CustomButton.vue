<template>
  <v-card color="amber lighten-1">
    <v-layout wrap>
      <v-flex xs2 pa-5>
        <v-icon large>{{icon}}</v-icon>
      </v-flex>
      <v-card-title>
        <span class="title font-weight-regular">{{name}}</span>
      </v-card-title>
    </v-layout>
  </v-card>
</template>

<script>
export default {
  props: {
    icon: {
      type: String,
      default: ''
    },
    name: {
      type: String,
      default: ''
    }
  }
}
</script>

<style scoped>
.v-card {
  border-radius: 4px;
}
</style>
