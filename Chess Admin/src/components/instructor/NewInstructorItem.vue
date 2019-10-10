<template>
  <v-card class="pa-5" :elevation="3">
    <v-layout>
      <v-flex xs2 mr-3>
        <v-avatar size="70">
          <img :src="avatar" alt="avatar" />
        </v-avatar>
      </v-flex>
      <v-flex xs10>
        <p
          class="mb-0 font-weight-bold title grey--text text--darken-3"
        >{{fullName}}</p>
        <p class="grey--text">{{email}}</p>
        <p class="grey--text font-weight-thin body-1 mb-0">{{moment(createdDate).add(7,'hours').fromNow()}}</p>
        <v-card-actions class="pa-0">
          <v-spacer></v-spacer>
          <v-btn fab icon dark small color="grey" @click="redirectToInstructorDetail">
            <v-icon dark>fa-info</v-icon>
          </v-btn>
        </v-card-actions>
      </v-flex>
    </v-layout>
  </v-card>
</template>

<script>
import Moment from '@/assets/js/Moment'
const MomentSetting = new Moment()

export default {
  props:{
    userId:{
      required:true,
      type:Number
    },
    avatar:{
      required:true,
      type:String
    },
    fullName:{
      required:true,
      type:String
    },
    email:{
      required:true,
      type:String
    },
    createdDate:{
      required:true,
      type:String
    }
  },
  data(){
    return{
      moment:MomentSetting.getInstance()
    }
  },
  methods:{
    redirectToInstructorDetail(){
      this.$router.push(`/dashboard/new-ins-profile/${this.userId}`)
    }
  }
}
</script>

<style>
</style>
