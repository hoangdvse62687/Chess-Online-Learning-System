<template>
    <v-container class="pa-10">
    <v-layout align-start justify-space-between>
      <v-card :elevation="8">
    <v-card-title>
            <h3 class="grey--text text--darken-2 title">Thông báo</h3>
            <v-spacer></v-spacer>
            <!-- <v-flex xs2>
              <v-select label="Trạng thái" :items="filterStatusItems" v-model="filterStatusItem" item-value="value"/>
            </v-flex> -->
          </v-card-title>
    <v-flex>
      <ListNotification 
            :pageSize="defaultLoadNotifications" :isLoadAll="true"/>
    </v-flex>
</v-card>
    </v-layout>
  </v-container>
</template>
<script>
import ListNotification from './ListNotification'
export default {
  components:{
    ListNotification
  },
  data(){
      return {
        defaultLoadNotifications:20
      }
  },  
}
</script>
