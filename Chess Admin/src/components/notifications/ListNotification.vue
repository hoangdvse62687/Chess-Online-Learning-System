<template>
<v-list :two-line="true" :min-width="isLoadAll ? '1000' : '500'">
          <div v-for="(item,index) in notifications" :key="index" :style=" !item.viewed ? 'background-color: #edf2fa' : ''">
            <v-list-item @click="redirectToNotificationItem(index)">
              <v-list-item-avatar>
                <img :src="item.objectAvatar" :alt="item.objectName">
              </v-list-item-avatar>
              <v-list-item-content>
                <v-list-item-title class="body-2"><strong>{{item.objectName}}</strong> {{item.content}}</v-list-item-title>
                <v-list-item-subtitle>{{moment(item.createDate).fromNow()}}</v-list-item-subtitle>
              </v-list-item-content>
          </v-list-item>
          <v-divider></v-divider>
          </div>
          <div v-if="isLoading" style="text-align: center;">
              <img height="50px" width="50px" :src="loadingImg"/>
          </div>
          <div class="text-xs-center pt-2" v-if="!isLoadAll">
            <v-layout justify-center>
              <a @click="redirectToNotificationPage()">Xem tất cả</a>
            </v-layout>
          </div>
        </v-list>
</template>

<script>
import { RepositoryFactory } from '@/repository/RepositoryFactory'
import Moment from '@/assets/js/Moment'
import firebase from 'firebase'
const notificationRepository = RepositoryFactory.get('notification')
const MomentSetting = new Moment()
export default {
    props:{
        pageSize:{
            required:true,
            type:Number
        },
        sortBy:{
            required:false,
            type:String,
            default:''
        },
        sortDirection:{
            required:false,
            type:String,
            default:''
        },
        isLoadAll:{
          required:false,
          type:Boolean,
          default:false
        }
    },
    data(){
      return{
        notifications:[],
        unreadNotifications:0,
        page:1,
        moment:MomentSetting.getInstance(),
        ObjectType:{
          User:0,
          Course:1,
          Review:4
        },
        loadingImg: require('@/assets/images/loading.gif'),
        isLoading: false,
        totalPages:0
      }
    },
    methods:{
        async getNotification(){
            this.isLoading = true
            const data = await notificationRepository.getNotificationPagination(this.page,this.pageSize,this.sortBy,this.sortDirection)
            if(data.data.data){
                this.unreadNotifications = data.data.data.totalNotViewedElements
                this.handleUnreadNotifications(this.unreadNotifications)
                this.notifications = this.notifications.concat(data.data.data.data.content)
                this.page++
                this.isLoading = false
                this.totalPages = data.data.data.data.totalPages
            }
        },
        async updateIsViewed(notificationIds){
            const data = await notificationRepository.updateIsViewed(notificationIds)
            console.log(data.data)
        },
        redirectToNotificationItem(index){
          if(!this.notifications[index].viewed){
            var notificationIds = []
            notificationIds.push(this.notifications[index].notificationId)
            this.updateIsViewed(notificationIds)
            this.notifications[index].viewed = true
            this.unreadNotifications--
            this.handleUnreadNotifications(this.unreadNotifications,this.notifications[index].notificationId)
          }
          this.handleRedirect(this.notifications[index])
        },
        handleRedirect(notification){
          if(this.$store.state.user.roleId == 3){
            switch(notification.objectTypeId){
              case this.ObjectType.User:
                this.$router.push(`/dashboard/instructor-request?email=${notification.objectName}`)
              break
              case this.ObjectType.Course:
                this.$router.push(`/dashboard/courses?courseName=${notification.objectName}`)
              break
            }
          }else if(this.$store.state.user.roleId == 1){
            switch(notification.objectTypeId){
              case this.ObjectType.User:
                this.$router.push(`/dashboard/profile`)
              break
              case this.ObjectType.Course:
                this.$router.push(`/dashboard/courses?courseName=${notification.objectName}`)
              break
              case this.ObjectType.Review:
                window.open('http://cols-capstone.ml/course/' + notification.objectId)
              break
            }
          }
        },
        handleUnreadNotifications(counterUnread,notificationId){
          sessionStorage.setItem('unread-notification-counter',counterUnread)
          this.$store.commit('changeUnreadNotifications',counterUnread)
          this.$store.commit('changeNotificationUpdatedId',notificationId)  
        },
        redirectToNotificationPage(){
          this.$router.push('/dashboard/notifications')
        },
        onScrollAction(){
          if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
            if(this.totalPages >= this.page){
              this.getNotification()
            }
          }
        }
    },
    mounted(){
        this.getNotification()
        if(this.isLoadAll){
          window.onscroll = this.onScrollAction
        }
        const messaging = firebase.messaging();
        var self = this
        messaging.onMessage(function(payload) {
          var notification = {
            content: payload.data.content,
            createDate: self.moment.utc().format(),
            viewed: payload.data.isViewed == "true" ? true : false,
            notificationId: parseFloat(payload.data.notificationId),
            objectAvatar: payload.data.objectAvatar,
            objectId: parseFloat(payload.data.objectId),
            objectName: payload.data.objectName,
            objectTypeId: parseFloat(payload.data.objectTypeId),
            roleTarget: payload.data.roleTarget,
          }
          self.notifications.unshift(notification)
          self.unreadNotifications++
          self.$store.commit('changeUnreadNotifications',self.unreadNotifications)
        });
    },
    watch:{
        pageSize:{
            handler:function(){
                this.getNotification()
            }
        },
        '$store.state.notificationUpdatedId':{
          handler:function(notificationId){
            for(var i = 0; i < this.notifications.length;i++){
              if(this.notifications[i].notificationId == notificationId){
                this.notifications[i].viewed = true
              }
            }
          },
          deep:true
        },
    }
}
</script>

        