<template>
  <v-container>
    <v-card-title>
      <h5 class="text-uppercase grey--text text--darken-2">Quản lý tài khoản</h5>
      <v-spacer></v-spacer>
          <v-flex xs3>
            <v-text-field label="Tìm kiếm" v-model="search" append-icon="search" color="amber accent-3"></v-text-field>
          </v-flex>
    </v-card-title>
    <v-flex xs12 justify-center ma-auto>
        <v-container>
          <v-card class="pa-5" :elevation="8">
            <UserDataTable :search="search"/>
          </v-card>
        </v-container>
    </v-flex>
        
  </v-container>
</template>

<script>
import UserDataTable from './UserDataTable'
export default {
  components: {
    UserDataTable
  },
  data () {
    return  {
      search: ''
    }
  },
  created() {
    console.log(localStorage.getItem("access-token"))
  }
}
</script>

<style scoped>
.v-card {
  border-radius: 10px;
}
</style>
