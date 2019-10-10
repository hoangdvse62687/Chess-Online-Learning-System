<template>
    <div>
        <div id="table_chart"></div>
        <br/>
        <p v-if="dataRows.length == 0" style="text-align: center;">Chưa có khóa học nào</p>
        <div v-if="dataRows.length != 0">
            <v-layout justify-center>
                <v-pagination color="amber accent-3" v-model="currentPage" :length="totalPages" circle></v-pagination>
            </v-layout>
        </div>
    </div>
</template>
<script>
import {GoogleCharts} from 'google-charts'
import Pagination from '@/components/kit/Pagination'
export default {
    props:{
        name:{
            required:false,
            type:String,
            default:"Table Chart"
        },
        dataRows:{
            required:true,
            type:Array
        },
        columnSettings:{
            required:true,
            type:Array
        },
        height:{
            required:true,
            type:String,
            default:"100%"
        },
        width:{
            required:true,
            type:String,
            default:"100%"
        },
        page:{
            required:true,
            type:Number
        },
        totalPages:{
            required:true,
            type:Number
        }
    },
    data(){
        return {
            currentPage:this.page
        }
    },
    methods:{
        drawChart(){
            var data = new GoogleCharts.api.visualization.DataTable();
            for(var i = 0; i < this.columnSettings.length;i++){
                data.addColumn(this.columnSettings[i][0],this.columnSettings[i][1]);
            }
            data.addRows(this.dataRows);

            var table = new GoogleCharts.api.visualization.Table(document.getElementById('table_chart'));
            var options = {
                showRowNumber: false, 
                width: this.width, 
                height: this.height,
                allowHtml:true
            }
            table.draw(data, options);
        },
    },
    mounted(){
        GoogleCharts.load(this.drawChart)
    },
    watch:{
        dataRows:{
            handler:function(){
                GoogleCharts.load(this.drawChart)
            },
            deep:true
        },
        currentPage:{
            handler:function(){
                this.$emit('newdata',this.currentPage)
            },
            deep:true
        },
        page:{
            handler:function(){
                this.currentPage = this.page
            }
        }
    }
}
</script>
<style>
    .google-visualization-table-td{
        text-align: center !important;
    }
</style>