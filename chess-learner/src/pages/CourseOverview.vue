<template>
  <v-container px-0>
    <v-layout wrap>
      <v-flex xs12>
        <v-breadcrumbs :items="breadcrumbs">
          <template v-slot:divider>
            <v-icon>mdi-chevron-right</v-icon>
          </template>
        </v-breadcrumbs>
      </v-flex>
      <v-flex xs12>
        <ListCourse />
      </v-flex>
      <v-snackbar
        v-model="snackbar"
        :color="color"
        :multi-line="mode === 'multi-line'"
        :timeout="timeout"
        :vertical="mode === 'vertical'"
      >
        {{ snackbarText }}
        <v-btn dark text @click="snackbar = false">Đóng</v-btn>
      </v-snackbar>
    </v-layout>
  </v-container>
</template>

<script>
import ListCourse from '@/components/CourseOverview/ListCourse'

export default {
  components: {
    ListCourse
  },
  data() {
    return {
      breadcrumbs: [
        {
          text: 'Trang chủ',
          disabled: false,
          href: '/'
        },
        {
          text: 'Khóa học',
          disabled: true
        }
      ],
      snackbar: false,
      timeout: 6000,
      snackbarText: '',

      color: '',
      mode: ''
    }
  },
  mounted() {
    if (this.$route.params.isNew) {
      setTimeout(() => {
        const user = JSON.parse(localStorage.user)
        this.snackbarText = `Chúc mừng bạn đã đăng kí tài khoản thành công! Bạn đang ở ELO ${user.point}. Chúc bạn luyện tập vui vẻ.`
        this.snackbar = true
      }, 1000)
    }
  }
}
</script>

<style></style>
