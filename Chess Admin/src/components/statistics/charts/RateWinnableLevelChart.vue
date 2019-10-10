<template>
<div>
    <PieChart :name="pieChartName" :dataRows="dataRows" :id="id"/>
</div>
</template>
<script>
import PieChart from './GoogleCharts/PieChart'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
const reportRepository = RepositoryFactory.get('report')
export default {
    components:{
        PieChart
    },
    props:{
        nameChart:{
            required:true,
            type:String
        },
        isWin:{
            required:false,
            type:Number,
            default:1
        },
        year:{
            required:true,
            type:Number
        },
        id:{
            required:true,
            type:String
        }
    },
    data(){
        return{
            dataRows:[],
            pieChartName:"",
            isLoading:false
        }
    },
    methods:{
        async getData(){
            this.isLoading = true
            const data = await reportRepository.getRateLevelWinnable(this.year,this.isWin)
            if(data.data.data){
                this.handleData(data.data.data)
                this.isLoading = false
            }
        },
        handleData(data){
            this.pieChartName = this.nameChart + " " + this.year
            this.dataRows = []

            this.dataRows.push(['This is header','header type'])
            this.dataRows.push(['Cấp độ 1',data[0]])
            this.dataRows.push(['Cấp độ 2',data[1]])
            this.dataRows.push(['Cấp độ 3',data[2]])
            this.dataRows.push(['Cấp độ 4',data[3]])
            this.dataRows.push(['Cấp độ 5',data[4]])
        },
    },
    mounted(){
        this.getData()
    },
    watch:{
        year:{
            handler:function(){
                this.getData()
            },
            deep:true
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
