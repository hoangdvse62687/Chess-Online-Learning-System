<template>
  <v-container class="pa-6">
    <v-card :elevation="8">
      <course-background @onBackground="getFile"></course-background>
    <course-form @createdCourse="createCourse"></course-form>
    </v-card>
  </v-container>
</template>

<script>
import CourseBackground from './courseComponents/CourseBackground'
import CourseForm from './courseComponents/CourseForm'
import {RepositoryFactory} from '@/repository/RepositoryFactory'
import Loader from '@/components/Loader'
const courseRepository = RepositoryFactory.get('course')
export default {
  components: {
    CourseForm,
    CourseBackground
  },
  data() {
    return {
      courseImage: null,
      loader:false
    }
  },
  methods: {
    getFile(file) {
      this.courseImage = file
    },
    async createCourse(course) {
      this.loader = true
      let match = this.$store.state.user.email.match(/^([^@]*)/)
        let courseSlug = course.name.toLowerCase().split(" ").join('-')
        course.image = this.courseImage === null ? 'https://firebasestorage.googleapis.com/v0/b/cols-fpt.appspot.com/o/images%2Fcourses%2Ftuyetnganphamhoang%2Fnh%E1%BA%ADp-m%C3%B4n?alt=media&token=c4cef16e-19ce-48db-8702-4d8f4f8412e7' : 
                        await this.uploadImageByFile(this.courseImage, courseSlug, `courses/${match[0]}`)  
        console.log(course)
        const data = await courseRepository.createCourse(course).then(res => {
          const courseId = res.data.data.savedId
          this.$router.push({path: `/dashboard/courses/${courseId}`})
          this.loader = false
        })
    }
  },
  watch:{
    loader:{
      handler:function(){
        if(this.loader){
          this.$store.commit('incrementLoader', 1)
        }else{
          this.$store.commit('incrementLoader', -1)
        }
      },deep:true
    }
  }
}
</script>

<style scoped>
</style>
