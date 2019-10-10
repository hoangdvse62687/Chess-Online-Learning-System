<template>
  <v-container>
    <PageTitle :title="'Quản lý danh mục'" @search="handleSearch($event)" :searchInput="search"/>
    <v-flex xs12 justify-center ma-auto>
        <v-container>
          <v-card class="pa-5" :elevation="8">
            <CategoryDataTable :search="search"/>
          </v-card>
        </v-container>
    </v-flex>    
  </v-container>
</template>

<script>
import PageTitle from '@/components/kit/PageTitle'
import CategoryDataTable from './CategoryDataTable'
export default {
  components: {
    PageTitle,
    CategoryDataTable
  },
  data() {
    return {
      search:''
    }
  },
  methods:{
    handleSearch(e){
      this.search = e
    },
  }
}
</script>

<style scoped>
.course-item {
  flex-basis: 32.6666666%;
  -webkit-box-flex: 0;
  -ms-flex-positive: 0;
  flex-grow: 0;
}
.v-card {
  border-radius: 10px;
}
</style>
