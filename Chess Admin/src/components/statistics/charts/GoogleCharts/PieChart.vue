<template>
    
</template>
<script>
import {GoogleCharts} from 'google-charts'
export default {
    props:{
        name:{
            required:true,
            type:String
        },
        dataRows:{
            required:true,
            type:Array
        },
        // height:{
        //     required:false,
        //     type:Number,
        //     default:500
        // },
        // width:{
        //     required:false,
        //     type:Number,
        //     default:608
        // },
        id:{
            required:true,
            type:String
        }
    },
    methods:{
        drawChart() {

        var data = new GoogleCharts.api.visualization.arrayToDataTable(this.dataRows);

        var options = {
          title: this.name,
        //   width:this.width,
        //   height:this.height
        };

        var chart = new GoogleCharts.api.visualization.PieChart(document.getElementById(this.id));

        chart.draw(data, options);
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
       } 
    }
}
</script>
