<template>
  <v-container>
    <PageTitle :title="'Giảng viên mới'" @search="handleSearch($event)" :searchInput="email"/>
    <v-container>
      <v-layout wrap justify-space-between>
        <v-flex class="new-instructor-item" xs6 xl4 mb-5 v-for="(item,index) in instructorRequests" :key="index">
          <NewInstructorItem 
          :userId="item.userId"
          :avatar="item.avatar" 
          :fullName="item.fullName" 
          :email="item.email"
          :createdDate="item.createdDate"/>
        </v-flex>
      </v-layout>
      <Pagination :currentPage="currentPage" :pages="totalPages" :rowDataLength="instructorRequests.length" @triggerpaging="handlPaging($event)"/>
    </v-container>
  </v-container>
</template>

<script>
import PageTitle from '@/components/kit/PageTitle'
import NewInstructorItem from './NewInstructorItem'
import Pagination from '@/components/kit/Pagination'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const userRepository = RepositoryFactory.get('user')

export default {
  components: {
    PageTitle,
    NewInstructorItem,
    Pagination
  },
  data(){
    return{
      currentPage:1,
      pageSize:10,
      totalPages:1,
      email:'',
      instructorRequests:[]
    }
  },
  methods:{
    async getUserRequestInstructor(){
        const data = await userRepository.getUsersPagination(this.currentPage,
        this.pageSize,this.email,'false','false','1')
        if(data.data.data){
          this.instructorRequests = data.data.data.content
          this.totalPages = data.data.data.totalPages
        }
    },
    handlPaging(e){
      this.currentPage = e
    },
    handleSearch(e){
      this.email = e
    }
  },
  mounted(){
    if(this.$route.query.email){
      this.email = this.$route.query.email
    }else{
      this.getUserRequestInstructor()
    }
  },
  watch:{
    currentPage:{
      handler:function(){
        if(this.currentPage != 0){
          this.getUserRequestInstructor()
        }
      },
      deep:true
    },
    email:{
      handler:function(){
        this.currentPage = 0
        this.currentPage = 1
      }
    }
  }
}
</script>

<style>
.flex.xs6.new-instructor-item {
  max-width: 49%;
}
@media (min-width: 1904px) {
  .flex.xl4.new-instructor-item {
    max-width: 32.5%;
  }
}
</style>
