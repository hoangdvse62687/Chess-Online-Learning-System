<template>
    <div id="line_chart"></div>
</template>
<script>
import {GoogleCharts} from 'google-charts'

export default {
    props:{
        name:{
            required:false,
            type:String,
            default:"Line Chart"
        },
        data:{
            required:true,
            type:Array
        }
    },
    methods:{
        drawChart(){
            var data = new GoogleCharts.api.visualization.arrayToDataTable(this.data)

            var options = {
                title: this.name,
                curveType: 'function',
                legend: { position: 'bottom' }
            }

            var chart = new GoogleCharts.api.visualization.LineChart(document.getElementById('line_chart'))

            chart.draw(data, options)
        }
    },
    mounted(){
        GoogleCharts.load(this.drawChart)
    },
    watch:{
        data:{
            handler:function(){
                GoogleCharts.load(this.drawChart)
            },
            deep:true
        }
    }
}
</script>
