<template>
    <div>
    <div v-if="isLoading" style="text-align: center;">
        <img height="50px" width="50px" :src="loadingImg"/>
    </div>
    <div v-if="!isLoading">
        <v-container>
            <v-row no-gutters>
                <v-col :cols="6">
                    <ColumnChart :id="id" :name="columnChartName" :columnSettings="chartColumns" :dataRows="chartData"/>
                    <div id="course_status_column_chart"/>
                </v-col>
                <v-col :cols="6">
                    <PieChart :name="pieChartName" :dataRows="dataRows" :id="idPieChart"/>
                    <div id="course_status_chart_pie_chart"/>
                </v-col>
            </v-row>
        </v-container>
    </div>
  </div>
</template>

<script>
import ColumnChart from './GoogleCharts/ColumnChart'
import PieChart from './GoogleCharts/PieChart'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const reportRepository = RepositoryFactory.get('report')

export default {
    props:{
        
    },
    components:{
        ColumnChart,
        PieChart
    },
    data(){
        return{
            chartData:[],
            columnChartName:"Biểu đồ tổng số lượng khóa học công khai",
            loadingImg: require('@/assets/images/loading.gif'),
            isLoading: false,
            chartColumns:[
                ["string","Tổng"],
                ["number","Công khai"]
            ],
            id:"course_status_column_chart",
            idPieChart:"course_status_chart_pie_chart",
            pieChartName:"Biểu đồ trạng thái khóa học",
            dataRows:[]
        }
    },
    mounted(){
        this.getData()
    },
    methods:{
        async getData(){    
            this.isLoading = true
            await reportRepository.getPublishCourseReport().then(res => {
                if (res.status === 200) {
                    this.chartData.push(["Tổng",res.data.data[0]])
                }
            })
            await reportRepository.getCourseStatusReport().then(res =>{
                if (res.status === 200) {
                    this.dataRows.push(['This is header','header type'])
                    this.dataRows.push(['Soạn thảo',res.data.data[0]])
                    this.dataRows.push(['Công khai',res.data.data[1]])
                    this.dataRows.push(['Đang soạn thảo',res.data.data[2]])
                    this.dataRows.push(['Bị từ chối',res.data.data[3]])
                }
            })
            this.isLoading = false
        },
    },
    watch:{
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