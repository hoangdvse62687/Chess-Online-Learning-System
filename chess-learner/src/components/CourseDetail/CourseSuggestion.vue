<template>
  <div>
    <v-card-title primary-title>
      <div class="title-about text-black">Khóa học liên quan</div>
    </v-card-title>
    <v-layout wrap v-if="listCourse !== null">
      <CourseSuggestionItem :course-detail="item" v-for="(item, index) in listCourse" :key="index" />
    </v-layout>
  </div>
</template>

<script>
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const courseRepository = RepositoryFactory.get('course')
import CourseSuggestionItem from '@/components/CourseDetail/CourseSuggestionItem'
export default {
  components: {
    CourseSuggestionItem
  },
  data() {
    return {
      filter: {
        page: 1,
        pageSize: 5,
        courseId: this.$route.params.courseId
      },
      listCourse: null
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.$store.commit('incrementLoader', 1)
      await this.getCommonSuggestion()
      setTimeout(() => {
        this.$store.commit('incrementLoader', -1)
      }, 500)
    },
    async getCommonSuggestion() {
      const { data } = await courseRepository.getCommonSuggestion(this.filter)
      this.listCourse = data.data.content
      this.formatListCourse()
    },
    formatListCourse() {
      this.listCourse.forEach(course => {
        course.requiredEloName = this.getEloName(course.requiredElo)
        course.requiredEloNumber = 600 + course.requiredElo * 200
        course.requiredEloClass = `elo-${course.requiredElo}`
      })
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
