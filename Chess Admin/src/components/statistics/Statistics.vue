<template>
  <v-container fluid>
    <div v-if="userRoleId == 1">
      <MonthLineChart 
      :monthChartName="enrollmentMonthLineChartNmae" 
      :chartCaptions="enrollmentChartCaption" 
      :fnGetDataAPI="getEnrollment"/>
      <v-card-title>
        <v-text-field label="Search" v-model="courseNameSearch"/>
        <v-spacer></v-spacer>
        <v-flex xs2>
          <v-select label="Số lượng dòng" :items="pageSizeOptions" v-model="pageSize"/>
        </v-flex>
      </v-card-title>
      <LearnerStatusCourseChart :courseName="courseNameSearch" :pageSize="pageSize"/>
    </div>
    <div v-if="userRoleId == 3">
      <MonthLineChart 
      :monthChartName="userRegisterMonthLineChartName" 
      :chartCaptions="userRegisterChartCaption" 
      :fnGetDataAPI="getUsersRegister"/>

      <MonthColumnChart 
      :name="RateWinnableChartName" 
      :monthChartColumns="RateWinnableColumnSettings"
      :fnGetDataAPI="getRateWinnable" @newdata="handleYearTrigger($event)"/>

      <RateWinnableLevelChart
        :nameChart="winRateLevelChartName"
        :year="yearExchangeComponents"
        :id="winLevelChartId"
      />

      <CourseStatusChart/>
    </div>
  </v-container>
</template>

<script>
import LearnerStatusCourseChart from './charts/LearnerStatusCourseChart'
import MonthLineChart from './charts/MonthLineChart'
import MonthColumnChart from './charts/MonthColumnChart'
import RateWinnableLevelChart from './charts/RateWinnableLevelChart'
import CourseStatusChart from './charts/CourseStatusChart'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const reportRepository = RepositoryFactory.get('report')
const userRepository = RepositoryFactory.get('user')
var dt = new Date()
export default {
  data(){
      return{
        userRoleId:0,
        userRegisterMonthLineChartName: "Biểu đồ lượt đăng ký tài khoản",
        userRegisterChartCaption:["Tháng","Google"],
        enrollmentMonthLineChartNmae: "Biểu đồ lượt đăng ký",
        enrollmentChartCaption:["Tháng","Lượt đăng ký"],
        RateWinnableChartName:"Biểu đồ kết quả chơi cờ với bot",
        RateWinnableColumnSettings:[
          ["string","Tháng"],
          ["number","Bỏ cuộc"],
          ["number","Thua"],
          ["number","Hòa"],
          ["number","Thắng"],
        ],
        winRateLevelChartName: "Biểu đồ tổng lượt chơi theo level bot",
        winLevelChartId:"LevelChart",
        yearExchangeComponents:dt.getFullYear(),
        courseNameSearch:"",
        pageSize:10,
        pageSizeOptions:[10,20,50],
      }
  },
  components: {
    MonthLineChart,
    LearnerStatusCourseChart,
    MonthColumnChart,
    RateWinnableLevelChart,
    CourseStatusChart
  },
  methods:{
    async getCurrentUserDetail(){
      // const user = await userRepository.getCurrentUserDetail()
      // if(user.data.data.roleId){
      //   this.userRoleId = user.data.data.roleId
      if(this.$store.state.user.roleId){
        this.userRoleId = this.$store.state.user.roleId
      }
      console.log(user)
    },
    async getUsersRegister(year){
      return await reportRepository.getUsersRegister(year)
    },
    async getEnrollment(year){
      return await reportRepository.getEnrollment(year)
    },
    async getRateWinnable(year){
      return await reportRepository.getRateWinnable(year)
    },
    handleYearTrigger(e){
      this.yearExchangeComponents = e
    }
  },
  mounted(){
    this.getCurrentUserDetail()
  }
}
</script>

<style></style>
