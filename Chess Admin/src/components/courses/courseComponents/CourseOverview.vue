<template>
  <div>
    <v-container>
        <v-card :elevation="8">
            <v-card-title>
            <h3 class="grey--text text--darken-2 title">Danh sách khóa học</h3>
            <v-spacer></v-spacer>
            <v-flex xs2>
              <v-select label="Trạng thái" :items="filterStatusItems" v-model="filterStatusItem" item-value="value"/>
            </v-flex>
          </v-card-title>
          <v-divider class="mb-5"></v-divider>
          <!-- <v-layout column wrap :pagination.sync="pagination"> -->
          <v-flex
            v-for="(item, index) in listCourses"
            :key="index"
            class="course-item"
            px-5
          >
            <CourseItem :content="item" class="mb-3"/>
            <v-divider class="my-5 course-divider"></v-divider>
          </v-flex>
        <!-- </v-layout> -->
        <Pagination :currentPage="currentPage" :pages="totalPages" :rowDataLength="listCourses.length" @triggerpaging="handlPaging($event)"/>
        </v-card>
    </v-container>
  </div>
</template>


<script>
import CourseItem from './CourseItem'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
import Pagination from '@/components/kit/Pagination'
const courseRepository = RepositoryFactory.get('course')
export default {
  props:{
    search:{
      required:false,
      type:String,
      default:''
    },
    fnGetDataAPI:{
      required:true,
      type:Function
    }
  },
  components: {
    CourseItem,
    Pagination
  },
  data() {
    return {
      dialog: false,
      loader: false,
      listCourses: [],
      editedCourse: {},
      removedCourse: [],
      currentPage:1,
      pageSize:10,
      totalPages:1,
      filterStatusItems:[
        {text:'Tất cả',value:'0'},
        {text:'Đang soạn thảo',value:'1'},
        {text:'Công khai',value:'2'},
        {text:'Đã xóa',value:'3'},
        {text:'Chờ xét duyệt',value:'4'},
        {text:'Đã từ chối',value:'5'},
        ],
      filterStatusItem:'0'
    }
  },
  mounted() {
    if(this.$route.query.status){
      this.filterStatusItem = this.$route.query.status
    }else{
      this.getCoursesPagination()
    }
  },
  methods: {
    async getCoursesPagination() {
      this.loader = true
      const {
        data
      } = await this.fnGetDataAPI(this.currentPage, 
      this.pageSize,this.search,this.filterStatusItem == 0 ? '' : this.filterStatusItem)
      this.listCourses = this.formatListCourse(data.data.content)
      this.totalPages = data.data.totalPages
      this.loader = false
      console.log(data)
    },
    async removeCourse() {
      const { data } = await courseRepository.removeCourse(
        this.listCourses.courseId
      )
      console.log(data)
    },
    handlPaging(e){
      this.currentPage = e
    }
  },
  watch:{
    currentPage:{
      handler:function(){
        if(this.currentPage != 0){
          this.getCoursesPagination()
        }
      },
      deep:true
    },
    search:{
      handler:function(){
        //trigger
        this.currentPage = 0
        this.currentPage = 1
      },
      deep:true
    },
    filterStatusItem:{
      handler:function(){
        //trigger
        this.currentPage = 0
        this.currentPage = 1
      }
    },
    loader:{
      handler:function(){
        if(this.loader){
          this.$store.commit('incrementLoader', 1)
        }else{
          this.$store.commit('incrementLoader', -1)
        }
      },
      deep:true
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
.course-divider {
  border-style: dashed; 
}

</style>
