<template>
  <v-card>
    <v-card-title primary-title>
      <div class="title-instructor">Giảng viên</div>
    </v-card-title>
    <v-layout pa-3 wrap>
      <v-flex xs2 mr-4>
        <v-avatar :size="120" @click="goToInstructorPage()" class="instructor-avatar">
          <img :src="author.avatar" />
        </v-avatar>
      </v-flex>
      <v-flex xs7>
        <v-layout py-3 align-center wrap justify-center>
          <v-flex xs12>Giảng viên</v-flex>
          <v-flex xs12 py-1 class="author-fullname text-truncate">
            <router-link :to="`/instructor/${author.userId}`">{{ author.fullName }}</router-link>
          </v-flex>
        </v-layout>
      </v-flex>
    </v-layout>
  </v-card>
</template>
<script>
export default {
  props: {
    author: {
      type: Object,
      default: null
    },
    courseDetail: {
      type: Object,
      default: null
    }
  },
  methods: {
    goToInstructorPage() {
      this.$router.push(`/instructor/${this.author.userId}`)
    }
  }
}
</script>

<style scoped>
.title-instructor {
  font-size: 18px;
  font-weight: 600;
  line-height: 32px;
  letter-spacing: 0.3px;
}
.author-fullname a:hover {
  text-decoration: underline !important;
}
.author-fullname a {
  color: #4d4d4d;
}
.author-fullname {
  font-size: 24px;
  font-weight: 600;
  line-height: 32px;
  letter-spacing: 0.3px;
}
.instructor-avatar:hover {
  cursor: pointer;
}
</style>
