<template>
  <v-card flat>
    <v-card-title class="tab-title">Khóa học của tôi</v-card-title>
    <v-layout wrap px-4 v-if="listCourse !== null">
      <RegistedCourseRow
        :isAuthor="false"
        :course-group="item"
        v-for="(item, index) in listCourse"
        :key="index"
      />
    </v-layout>
  </v-card>
</template>

<script>
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const courseRepository = RepositoryFactory.get('course')
import RegistedCourseRow from '@/components/Profile/RegistedCourseRow'
export default {
  components: {
    RegistedCourseRow
  },
  data() {
    return {
      filter: {
        page: 1,
        pageSize: 12,
        userId: 0
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
      await this.getCoursesPaginationsByUserId()
      setTimeout(() => {
        this.$store.commit('incrementLoader', -1)
      }, 500)
    },
    async getCoursesPaginationsByUserId() {
      this.filter.userId = this.$store.state.user.userId
      const { data } = await courseRepository.getCoursesPaginationsByUserId(
        this.filter
      )
      this.listCourse = data.data.content
      this.formatListCourse()
      this.listCourse = this.arrayToGroup(this.listCourse, 4)
    },
    arrayToGroup(list, howMany) {
      var result = []
      let input = [...list]
      while (input[0]) {
        result.push(input.splice(0, howMany))
      }
      return result
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
.tab-title {
  font-size: 18px;
}
</style>
