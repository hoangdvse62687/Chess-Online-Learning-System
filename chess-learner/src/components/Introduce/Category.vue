<template>
  <v-hover>
    <v-card
      slot-scope="{ hover }"
      :class="`elevation-${hover ? 12 : 2}`"
      class="mx-auto"
      width="344"
      :to="content.url"
    >
      <v-img :aspect-ratio="16 / 9" :src="content.imageURL"></v-img>
      <v-card-title>
        <div>
          <span class="title">{{ content.title }}</span>
        </div>
        <v-spacer></v-spacer>
        <v-btn icon class="mr-0">
          <v-icon>mdi-chevron-right</v-icon>
        </v-btn>
      </v-card-title>
    </v-card>
  </v-hover>
</template>

<script>
export default {
  props: {
    content: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      reviews: 413,
      value: 4.5
    }
  }
}
</script>

<style></style>
