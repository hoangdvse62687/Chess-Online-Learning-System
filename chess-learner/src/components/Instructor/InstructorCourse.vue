<template>
  <v-layout wrap>
    <v-flex xs12>
      <v-card-title primary-title>
        <div class="title-instructor">Khóa học</div>
      </v-card-title>
    </v-flex>
    <v-flex xs12>
      <v-layout wrap px-4 v-if="listCourse !== null">
        <RegistedCourseRow
          :isAuthor="true"
          :course-group="item"
          v-for="(item, index) in listCourse"
          :key="index"
        />
      </v-layout>
    </v-flex>
  </v-layout>
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
      listCourse: null,
      filter: {
        page: 1,
        pageSize: 12,
        userId: this.$route.params.instructorId,
        statusId: 2
      }
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
    arrayToGroup(list, howMany) {
      var result = []
      let input = [...list]
      while (input[0]) {
        result.push(input.splice(0, howMany))
      }
      return result
    },
    async getCoursesPaginationsByUserId() {
      const { data } = await courseRepository.getCoursesPaginationsByUserId(
        this.filter
      )
      data.data.content.forEach(el => {
        el.author = null
      })
      this.listCourse = data.data.content
      this.listCourse = this.arrayToGroup(this.listCourse, 4)
    }
  }
}
</script>

<style scoped>
</style>