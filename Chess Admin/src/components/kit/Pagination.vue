<template>
  <div class="text-xs-center pt-2">
    <v-layout justify-center>
      <p v-if="rowDataLength == 0 && isShowEmptyMessage">Không có dữ liệu</p>
      <v-pagination v-if="rowDataLength != 0" color="amber accent-3" v-model="pagination.page" :length="pages" circle></v-pagination>
    </v-layout>
  </div>
</template>

<script>
export default {
    props: {
        pages: {
            type: Number,
            default: 0
        },
        rowDataLength:{
          required:true,
          type:Number
        },
        isShowEmptyMessage:{
          required:false,
          type:Boolean,
          default:true
        },
        currentPage:{
          required:false,
          type:Number,
          default:1
        }
    },
  data() {
    return {
      pagination: {
        page:1
      },
    }
  },
  watch:{
    pagination:{
      handler:function(){
        this.$emit('triggerpaging', this.pagination.page)
      },
      deep:true
    },
    currentPage:{
      handler:function(){
        this.pagination.page = this.currentPage
      },
      deep:true
    }
  }
}
</script>

<style>
.v-pagination__item {
  outline: 0 !important;
}
</style>
