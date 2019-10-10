<template>
    <v-container class="pa-6">
    <v-layout align-start justify-space-between v-if="user != null">
      <v-flex xs4>
            <HighlightProfile :userInput="user"/>
            <v-card-actions v-if="user && !user.reviewed && !user.active && $store.state.user.roleId == 3" class="mt-2">
                <v-spacer></v-spacer>
                <v-btn width="120" depressed large class="mr-2" color="success" @click="updateStatus(true)">Đồng ý</v-btn>
                <v-btn width="120" depressed large color="error" @click="updateStatus(false)">Từ chối</v-btn>
                <v-spacer></v-spacer>
            </v-card-actions>
      </v-flex>
      <v-flex xs8>
        <ProfileInfo :isInstructor="true" :userInput="user" :isUpdate="isUpdate"/>
      </v-flex>
    </v-layout>
    <Loader v-if="loader"/>
  </v-container>
</template>

<script>
import HighlightProfile from '@/components/profile/profile-pieces/HighlightProfile'
import ProfileInfo from '@/components/profile/profile-pieces/ProfileInfo'
import Loader from '@/components/Loader'
import { RepositoryFactory } from '@/repository/RepositoryFactory'
import Swal from 'sweetalert2'
const userRepository = RepositoryFactory.get('user')

export default {
    components: {
        HighlightProfile,
        ProfileInfo,
        Loader
    },
    data() {    
        return  {
            userId:this.$route.params.userId,
            user: null,
            isUpdate:false,
            loader:false
        }
    },
    methods:{
      async getUserById(){
        this.loader = true
        const data = await userRepository.getUserById(this.userId)
        if(data.data){
          this.user = data.data.data
          this.loader = false
          this.$forceUpdate()
        }
      },
      async updateUserStatus(isActive){
        this.loader = true
        const data = await userRepository.updateUserStatus(this.userId,isActive)
        if(data.data.data){
          this.user.active = isActive
          this.user.reviewed = true
          Swal.fire({
            title: this.user.fullName,
            text: 'đã được lưu thành công',
            type: 'success'
          })
          this.loader = false
        }else{
          Swal.fire({
            title: 'Oops...',
            text: 'đã có lỗi diễn ra trong quá trình lưu',
            type: 'error'
          })
          this.loader = false
        }
      },
      updateStatus(isActive){
        if(!isActive){
          Swal.fire({
            title: 'Cảnh báo',
            text: "Điều này có thể khiến người dùng không thể vào trang!",
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Chấp nhận',
            cancelButtonText: 'Hủy bỏ'
          }).then((result) => {
            if (result.value) {
              this.updateUserStatus(isActive) 
            }
          })
        }else{
          this.updateUserStatus(isActive)
        }
      }
    },
    mounted(){
      if(this.userId){
        this.getUserById()
      }
    },
    watch:{
      '$route.params.userId':{
          handler:function(userId){
            this.userId = userId
            this.getUserById()
          },
          deep:true,
          immediate: true
        }
    }
}
</script>

<style scoped>
.xs4 {
  max-width: 31.5%;
}
</style>
