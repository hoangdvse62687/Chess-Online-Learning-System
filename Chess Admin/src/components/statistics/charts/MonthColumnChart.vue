<template>
  <div>
    <div v-if="isLoading" style="text-align: center;">
        <img height="50px" width="50px" :src="loadingImg"/>
    </div>
    <div v-if="!isLoading">
        <v-card-title>
            <v-spacer></v-spacer>
            <v-flex xs2>
              <v-select label="Năm" :items="listYears" v-model="chartYear"/>
            </v-flex>
          </v-card-title>
        <v-container>
            <v-row no-gutters>
                <v-col :cols="8">
                    <ColumnChart :id="id" :name="columnChartName" :columnSettings="monthChartColumns" :dataRows="chartData"/>
                    <div id="month_column_chart"/>
                </v-col>
                <v-col :cols="4">
                    <div id="LevelChart"/>
                </v-col>
            </v-row>
        </v-container>
    </div>
  </div>
</template>
<script>
import ColumnChart from './GoogleCharts/ColumnChart'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const userRepository = RepositoryFactory.get('user')

var dt = new Date()
export default {
    props:{
        name:{
            required:true,
            type:String,
        },
        monthChartColumns:{
            required:true,
            type:Array
        },
        fnGetDataAPI:{
          require:true,
          Type:Function
        }
    },
    components:{
        ColumnChart
    },
    data(){
        return{
            chartData:[],
            columnChartName:"",
            chartYear:dt.getFullYear(),
            listYears:[],
            loadingImg: require('@/assets/images/loading.gif'),
            isLoading: false,
            id:"month_column_chart"
        }
    },
    mounted(){
        //setting for enrollment report
        this.getData()
        this.handleYearDisplay()
    },
    methods:{
        async getData(){    
            this.isLoading = true
            const data = await this.fnGetDataAPI(this.chartYear)
            if(data.data.data){
                this.columnChartName = this.name + " " + this.chartYear
                this.handleData(data.data.data)
                this.isLoading = false
            }
        },
        async handleYearDisplay(){
            userRepository.getCurrentUserDetail().then(
                res => {
                    var userCreatedYear = (new Date(res.data.data.createdDate)).getFullYear()
                    for(var i = dt.getFullYear();i >= userCreatedYear; i--){
                        this.listYears.push(i)
                    }
                }
            )
        },
        handleData(data){
            var currentYear = dt.getFullYear()
            var currentMonth = dt.getMonth() + 1

            var lengthMonthReport = currentYear == this.chartYear ? currentMonth : 12 
            //clear data 
            this.chartData = []

            for(var i = 0; i < lengthMonthReport;i++){
                this.chartData.push(["Tháng " + (i + 1),data[i],data[i + 12],data[i + 12 * 2],data[i + 12 * 3]])
            }
        },
    },
    watch:{
        chartYear:{
        handler:function(){
            this.getData()
            this.$emit('newdata',parseInt(this.chartYear))
        }
        },
        isLoading:{
            handler:function(){
                if(this.isLoading){
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
