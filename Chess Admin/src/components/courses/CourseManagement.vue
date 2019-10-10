<template>
  <v-container>
    <PageTitle :title="'Quản lý khóa học'" @search="handleSearch($event)" :searchInput="search"/>
    <v-flex lg12 xl10 justify-center ma-auto>
        <CourseOverview :search="search" :fnGetDataAPI="getDataAPI"/>
    </v-flex>    
  </v-container>
</template>

<script>
import PageTitle from '@/components/kit/PageTitle'
import CourseOverview from './courseComponents/CourseOverview'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const courseRepository = RepositoryFactory.get('course')

export default {
  components: {
    PageTitle,
    CourseOverview
  },
  data() {
    return {
      search:''
    }
  },
  mounted(){
    if(this.$route.query.courseName){
      this.search = this.$route.query.courseName
    }
  },
  methods:{
    handleSearch(e){
      this.search = e
    },
    async getDataAPI(page, pageSize,nameCourse,statusId){
      if(this.$store.state.user.roleId == 1){
        return courseRepository.getCoursesPaginationByUserId(page, pageSize,nameCourse,statusId,this.$store.state.user.userId)
      }else if(this.$store.state.user.roleId == 3){
        return courseRepository.getCoursesPagination(page, pageSize,nameCourse,statusId)
      }
    }
  },
  watch:{
    '$route.query.courseName':{
      handler:function(search){
        this.search = search
      },
      deep:true,
      immediate: true
    }
  }
}
</script>

<style scoped>
.course-item {
  flex-basis: 32.6666666%;
  -webkit-box-flex: 0;
  -ms-flex-positive: 0;
  flex-grow: 0;
}
</style>
