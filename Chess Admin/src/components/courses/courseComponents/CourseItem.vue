<template>
  <v-flex xs12 class="black--text mx-auto course-item">
    <v-layout>
      <v-flex course-item__image--wrapper xs3 shrink>
        <v-img :src="content.courseImage" height="180"></v-img>
      </v-flex>
      <v-flex xs6 xl7 ml-3>
        <p class="mb-0 course-title font-weight-medium headline">{{ content.courseName }}</p>
        <p class="course-author body-2"><i class="fas fa-calendar-week"></i> {{ formatCreatedDate }} <v-chip small class="pa-3" color="amber lighten-1" label><v-icon left small>fa-crown</v-icon>{{formatLevel}}</v-chip> <v-chip class="ml-2" label dark :color="courseStatusColor[content.statusId - 1]" small>{{formatCourseStatus}}</v-chip></p>
        <p class="mb-0 course-author body-1">{{content.courseDescription}}</p>
        <v-card-text class="px-0">
          <v-chip v-for="(item) in content.listCategorys" :key="item.categoryId" class="mr-1" small >{{item.name}}</v-chip>
        </v-card-text>
      </v-flex>
      <v-divider inset vertical></v-divider>
      <v-flex xs3 xl2 class="course-rating-container">
          <v-card-actions>
            <v-spacer>
            <v-rating
            dense
            v-model="content.rating"
            readonly
            small
            :full-icon="fullIcons"
            :empty-icon="emptyIcon"
            :half-icon="halfIcon"
            half-increments
            background-color="amber"
            color="amber lighten-1"
          ></v-rating>
          </v-spacer>
          </v-card-actions>
          <p class="grey--text text--darken-2 headline font-weight-bold">({{ content.rating }})</p>
          <v-card-actions class="pa-0 ma-auto" v-if="$store.state.user.roleId == 1">
            <v-spacer></v-spacer>
            <v-btn @click="goToCourseDetail()" color="orange darken-1" dark depressed>Chỉnh sửa</v-btn>
            <!-- <v-btn color="orange darken-4" dark depressed>Xóa</v-btn> -->
            <v-spacer></v-spacer>
          </v-card-actions>
          <v-card-actions class="pa-0 ma-auto" v-if="$store.state.user.roleId == 3">
            <v-spacer></v-spacer>
            <v-btn @click="goToCourseDetail()" color="orange darken-1" dark depressed>Chi tiết</v-btn>
            <v-spacer></v-spacer>
          </v-card-actions>
      </v-flex>
    </v-layout>
  </v-flex>
</template>

<script>
import { RepositoryFactory } from '@/repository/RepositoryFactory'
import Loader from '@/components/Loader'
const courseRepository = RepositoryFactory.get('course')
export default {
  props: {
    content: Object
  },
  components: {
    Loader
  },
  data() {
    return {
      loader: false,
      dialog: false,
      listCourse: [],
      courseId: '',
      fullIcons: 'fa-star',
      emptyIcon: 'far fa-star',
      halfIcon: 'fa-star-half-alt',
      courseStatusColor: ['yellow darken-4', 'green', 'red', 'orange darken-4', 'red darken-3']
    }
  },
  computed: {
    formatCreatedDate: function() {
      const date = new Date(this.content.courseCreatedDate)
      return `${date.getMonth()}-${date.getDate()}-${date.getFullYear()}`
    },
    formatCourseStatus: function() {
      return this.getCourseRoleName(this.content.statusId)
    },
    formatLevel: function() {
      return this.getLevelById(this.content.requiredElo)
    }    
  },
  created() {
    this.content.rating = this.content.rating === undefined ? 5 : this.content.rating
  },
  methods: {
    // refreshPage() {
    //   window.location.reload()
    // },
    // async removeCourse() {
    //   const { data } = await courseRepository.removeCourse(
    //     this.content.courseId
    //   )
    //   console.log(data)
    // },
    goToCourseDetail() {
      this.$router.push(`/dashboard/courses/${this.content.courseId}`)
    }
  }
}
</script>

<style scoped>
.course-item__image--wrapper,
.course-item__image--wrapper .v-image {
  border-top-left-radius: inherit;
  border-bottom-left-radius: inherit;
}
.course-rating-container {
  text-align: center;
  align-self: flex-end;
}
</style>
