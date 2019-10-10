<template>
  <v-parallax :src="course !== null ? course.image : backgroundImage" height="250">
    <v-layout pa-5 column fill-height class="white--text">
      <p v-if="course !== null" class="text-uppercase text-center headline font-weight-bold mt-8">{{course.name}}</p>
      <p v-if="course !== null" class="text-center">{{course.description}}</p>
      <p v-if="course !== null" class="text-center ">
        <v-chip
        class="pa-3"
      color="amber lighten-1" label
    >
    <v-icon left small>fa-crown</v-icon>
      {{formatLevel}}
    </v-chip>
    <v-chip class="ml-2" label dark :color="courseStatusColor[course.statusId - 1]">{{formatCourseStatus}}</v-chip>
      </p>
      <v-spacer></v-spacer>
      <v-card-actions class="py-0">
        <v-btn @click="chooseUpload" text icon>
          <v-icon color="white">fa-camera</v-icon>
        </v-btn>
        <v-btn @click="editedCourseDialog = true" v-if="course !== null" text icon>
          <v-icon color="white">mdi-pencil</v-icon>
        </v-btn>
      </v-card-actions>
        <input ref="input" type="file" style="display:none;" accept="image/png, image/jpeg" @change="fileSelected" />
        <v-snackbar absolute
      v-model="errorSnackbar" 
    > 
    <v-icon color="red">fa-exclamation-triangle</v-icon>
      {{ errorSnackbarContent }}
      <v-btn
        text
        icon
        @click="errorSnackbar = false"
      >
        <v-icon>close</v-icon>
      </v-btn>
    </v-snackbar>
    <v-snackbar absolute
      v-model="successSnackbar" 
    > 
    <v-icon color="green">fa-check-circle</v-icon>
      {{ successSnackbarContent }}
      <v-btn
        text
        icon
        @click="successSnackbar = false"
      >
        <v-icon>close</v-icon>
      </v-btn>
    </v-snackbar>
    <v-dialog v-model="editedCourseDialog" width="700" persistent v-if="course !== null">
      <v-card :elevation="8">
        <v-card-title>Thay đổi thông tin khóa học</v-card-title>
        <v-container class="pt-0">
        <course-form @closeForm="closeForm" @createdCourse="editCourse" :editedCourse="course"></course-form>
      </v-container>
      </v-card>
    </v-dialog>
    </v-layout>
  </v-parallax>
</template>

<script>
import CourseForm from './CourseForm'
import {RepositoryFactory} from '@/repository/RepositoryFactory'
const courseRepository =  RepositoryFactory.get('course')
export default {
  props: {
    course: {
      type: Object,
      default: null
    }
  },
  components: {
    CourseForm
  },
  data() {
    return {
      courseStatusColor: [
        'yellow darken-4',
        'green',
        'red',
        'orange darken-4',
        'red darken-3'
      ],
      courseStatusName: [
        'Đang soạn thảo',
        'Công khai',
        'Đã xóa',
        'Chờ xét duyệt',
        'Đã từ chối'
      ],
      errorSnackbar: false,
      errorSnackbarContent: 'Hình ảnh không đúng định dạng',
      successSnackbar: false,
      successSnackbarContent: '',
      editedCourseDialog: false,
      backgroundImage: ''
    }
  },
    computed: {
    formatLevel: function() {
      return this.getLevelById(this.course.requiredElo)
    },
    formatCourseStatus: function() {
      return this.getCourseRoleName(this.course.statusId)
    }

  },
  mounted() {
  },
  methods: {
    chooseUpload() {
      this.$refs.input.click()
    },
    fileSelected(e) {
      let files = e.target.files || e.dataTransfer.files
      if (!files.length || files.length === 0) {
        return
      }
      if (files[0].type !== 'image/png' && files[0].type !== 'image/jpeg') {
        this.errorSnackbar = true
        this.errorSnackbarContent = "Hình ảnh không đúng định dạng"
      } else {
        this.courseImage = files[0]
        if (this.course !== null) {
          this.course.image = files[0]
          this.editCourseImage()
          this.$store.state.loader = 1
        } else {
          this.$emit('onBackground', files[0])
        }
        this.backgroundImage = window.URL.createObjectURL(files[0])
      }
    },
    saveCourse() {

    },
    async editCourseImage() {
      let match = this.$store.state.user.email.match(/^([^@]*)/)
      let courseSlug = this.course.name.toLowerCase().split(" ").join('-')
      this.course.image = await this.uploadImageByFile(this.course.image, courseSlug, `courses/${match[0]}`)
      const data = await courseRepository.updateCourse(this.course).then(res => {
        if (res.status === 200) {
          this.successSnackbar = true
          this.successSnackbarContent = 'Thay đổi ảnh khóa học thành công'
          this.$store.state.loader = 0
        }
      })
    },
    async editCourse(course) {
      const data = await courseRepository.updateCourse(this.course).then(res => {
        if (res.status === 200) {
          this.editedCourseDialog = false
          this.successSnackbar = true
          this.successSnackbarContent = 'Thay đổi thông tin khóa học thành công'
          this.$store.state.loader = 0
        }
      })
    },
    closeForm(data) {
      if (data) {
        this.editedCourseDialog = false
      }
    }
  }
}
</script>

<style scoped>
.create-course-title {
  font-family: 'Roboto', sans-serif;
}
.v-image {
  background-color: #454545;
}
>>> .v-parallax__content {
  background-color: rgba(0, 0, 0, 0.7)
}
</style>
