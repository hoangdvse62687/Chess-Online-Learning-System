<template>
  <v-container>
    <v-layout wrap>
      <v-flex xs12>
        <v-breadcrumbs :items="breadcrumbs">
          <template v-slot:divider>
            <v-icon>mdi-chevron-right</v-icon>
          </template>
        </v-breadcrumbs>
      </v-flex>
      <v-flex xs12>
        <v-card class="pa-3">
          <v-layout wrap v-if="instructorDetail !== null">
            <v-flex xs12>
              <InstructorInfo :instructorDetail="instructorDetail" />
            </v-flex>
            <v-flex xs12 mt-5>
              <v-divider></v-divider>
            </v-flex>
            <v-flex xs12>
              <Certificates :certificates="instructorDetail.certificates" />
            </v-flex>
            <v-flex xs12 mt-5>
              <v-divider></v-divider>
            </v-flex>
            <v-flex xs12>
              <InstructorCourse :courseDetailViewModels="instructorDetail.courseDetailViewModels" />
            </v-flex>
          </v-layout>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import InstructorInfo from '@/components/Instructor/InstructorInfo'
import Certificates from '@/components/Instructor/Certificates'
import InstructorCourse from '@/components/Instructor/InstructorCourse'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const userRepository = RepositoryFactory.get('user')
export default {
  components: {
    InstructorInfo,
    InstructorCourse,
    Certificates
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
          text: 'Giảng viên',
          disabled: true
        }
      ],
      instructorId: this.$route.params.instructorId,
      instructorDetail: null
    }
  },
  mounted() {
    this.fetchData()
  },

  methods: {
    async fetchData() {
      this.$store.commit('incrementLoader', 1)
      await this.getUserById()
      setTimeout(() => {
        this.$store.commit('incrementLoader', -1)
      }, 500)
    },
    async getUserById() {
      const { data } = await userRepository.getById(this.instructorId)
      this.instructorDetail = data.data
    }
  }
}
</script>

<style scoped>
</style>
