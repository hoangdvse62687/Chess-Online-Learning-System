<template>
  <v-card>
    <v-card-title primary-title>
      <div class="title-about text-black">Thông tin chung</div>
    </v-card-title>
    <v-card-text>
      <span class="text-black">{{description}}</span>
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  props: {
    description: {
      type: String,
      default: ''
    }
  }
}
</script>

<style scoped>
.title-about {
  font-size: 18px;
  font-weight: 600;
  line-height: 32px;
  letter-spacing: 0.3px;
}
</style>
