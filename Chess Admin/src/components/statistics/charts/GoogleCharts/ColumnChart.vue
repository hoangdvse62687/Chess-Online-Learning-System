<template>
    
</template>
<script>
import {GoogleCharts} from 'google-charts'
export default {
    props:{
        name:{
            required:false,
            type:String,
            default:"Column Chart"
        },
        dataRows:{
            required:true,
            type:Array
        },
        columnSettings:{
            required:true,
            type:Array
        },
        id:{
            required:true,
            type:String
        }
    },
    methods:{
        drawChart(){
            var data = new GoogleCharts.api.visualization.DataTable()
            for(var i = 0; i < this.columnSettings.length;i++){
                data.addColumn(this.columnSettings[i][0],this.columnSettings[i][1]);
            }
            data.addRows(this.dataRows);

            var options = {
                title: this.name,
            }

            var chart = new GoogleCharts.api.visualization.ColumnChart(document.getElementById(this.id))

            chart.draw(data, options)
        }
    },
    mounted(){
        GoogleCharts.load(this.drawChart)
    },
    watch:{
       dataRows:{
           handler:function(){
               this.drawChart()
           }
       } 
    }
}
</script>
